package org.mockito.internal.configuration;

import static org.junit.Assert.assertNotNull;
import static org.mockito.exceptions.base.MockitoException.classNotFound;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.NO_MATCH;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.ONE_MATCH;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.multipleMatches;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.noMatch;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.singleMatch;
import static org.mockito.internal.util.reflection.FieldAccessor.getAnnotatedFields;
import static org.mockito.internal.util.reflection.FieldAccessor.getDeclaredFields;
import static org.mockito.internal.util.reflection.FieldAccessor.setFieldValue;
import static org.mockito.mockitoSession.MockitoSession.session;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldAccessor;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.TypeUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private static final String TEST_CLASS = DefaultInjectionEngineTest.class.getName();
    private static final String TEST_FIELD = "testField";
    private static final String TEST_FIELD_TYPE = "org.mockito.TestClass";
    private static final String TEST_DEPENDENCY = "org.mockito.TestDependency";

    @Rule
    public MockitoSession session = session();

    @Mock
    private Object mock1;
    @Mock
    private Object mock2;
    @Mock
    private Object mock3;

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
    }

    @Test
    public void testInjectMocksOnFields() {
        Object testClassInstance = new Object() {
            @InjectMocks
            private Object injectedField;
            @InjectMocks
            private Object injectedField2;

            @InjectMocks
            private static class InnerStaticClass {
                @InjectMocks
                private Object injectedField3;
            }
        };

        injectionEngine.injectMocksOnFields(getAnnotatedFields(testClassInstance), mocks, testClassInstance);

        assertNotNull(testClassInstance.injectedField);
        assertNotNull(testClassInstance.injectedField2);
        assertNotNull(testClassInstance.InnerStaticClass.injectedField3);
    }

    @Test
    public void testInjectMockCandidate_noMatch() {
        assertNull(injectMockCandidate(Object.class, mocks, null));
    }

    @Test
    public void testInjectMockCandidate_singleMatch() {
        Object testClassInstance = new Object() {
            @InjectMocks
            private Object injectedField;

            @InjectMocks
            private static class InnerStaticClass {
                @InjectMocks
                private Object injectedField;
            }

            private static class InnerClass {
                @InjectMocks
                private Object injectedField;
            }

            private static class InnerClass2 {
                @InjectMocks
                private Object injectedField;
            }

            private static class InnerClass3 {
                @InjectMocks
                private Object injectedField;
            }
        };

        Object testFieldInstance = new Object() {
        };

        Field testField = FieldUtils.getField(testClassInstance, TEST_FIELD);
        setFieldValue(testClassInstance, testField, testFieldInstance);

        Object candidate = injectionEngine.injectMockCandidate(TypeUtils.getClass(TEST_FIELD_TYPE, testClassInstance), mocks, testClassInstance);
        assertEquals(singleMatch, mockCandidateFilter(testField, testFieldInstance).compareTo(NO_MATCH));
        assertEquals(testFieldInstance, candidate);
    }

    @Test
    public void testInjectMockCandidate_multipleMatches() {
        Object testClassInstance = new Object() {
            @InjectMocks
            private Object injectedField;

            private static class InnerClass1 {
                @InjectMocks
                private Object injectedField;
            }

            private static class InnerClass2 {
                @InjectMocks
                private Object injectedField;
            }
        };

        Object testFieldInstance1 = new Object() {
        };

        Object testFieldInstance2 = new Object() {
        };

        Field testField = FieldUtils.getField(testClassInstance, TEST_FIELD);
        setFieldValue(testClassInstance, testField, testFieldInstance1);

        Set<Field> fields = Arrays.asList(
                FieldUtils.getField(InnerClass1.class, TEST_FIELD),
                FieldUtils.getField(InnerClass2.class, TEST_FIELD)
        ).stream().collect(Collectors.toSet());

        assertEquals(multipleMatches, mockCandidateFilter(testField, testFieldInstance1).compareTo(NO_MATCH));
        assertEquals(testFieldInstance1, injectionEngine.injectMockCandidate(TypeUtils.getClass(TEST_FIELD_TYPE, testClassInstance), mocks, testClassInstance).orElse(null));

        setFieldValue(testClassInstance, testField, testFieldInstance2);

        assertEquals(multipleMatches, mockCandidateFilter(testField, testFieldInstance2).compareTo(NO_MATCH));
        assertEquals(testFieldInstance2, injectionEngine.injectMockCandidate(TypeUtils.getClass(TEST_FIELD_TYPE, testClassInstance), mocks, testClassInstance).orElse(null));
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidate_classNotFound() {
        Object testClassInstance = new Object() {
            @InjectMocks
            private Object injectedField;
        };

        Field testField = FieldUtils.getField(testClassInstance, TEST_FIELD);
        setFieldValue(testClassInstance, testField, null);

        assertEquals(noMatch, mockCandidateFilter(testField, null).compareTo(NO_MATCH));
        injectionEngine.injectMockCandidate(TypeUtils.getClass(TEST_FIELD_TYPE, testClassInstance), mocks, testClassInstance);
    }

    @Test
    public void testInjectMockCandidate_noInjectedField() {
        Object testClassInstance = new Object() {
            private Object noInjectedField;
        };

        Field testField = FieldUtils.getField(testClassInstance, TEST_FIELD);
        setFieldValue(testClassInstance, testField, null);

        assertEquals(noMatch, mockCandidateFilter(testField, null).compareTo(NO_MATCH));
        injectionEngine.injectMockCandidate(TypeUtils.getClass(TEST_FIELD_TYPE, testClassInstance), mocks, testClassInstance);
        assertNull(testClassInstance.noInjectedField);
    }

    private Object injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
        return injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
    }

    private MockCandidateFilter mockCandidateFilter(Field field, Object fieldInstance) {
        return injectionEngine.mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    }

    private static Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}