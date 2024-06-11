package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockComponent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private MockComponent mockComponent1;
    private MockComponent mockComponent2;
    private MockComponent mockComponent3;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mockComponent1 = Mockito.mock(MockComponent.class);
        mockComponent2 = Mockito.mock(MockComponent.class);
        mockComponent3 = Mockito.mock(MockComponent.class);
    }

    @Test
    public void testInjectMocksOnFields() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());

        Set<Object> mocks = new HashSet<>();
        mocks.add(mockComponent1);
        mocks.add(mockComponent2);

        Object testClassInstance = Whitebox.newInstance(testClass);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Field field1 = testClass.getDeclaredField("field1");
        Field field2 = testClass.getDeclaredField("field2");
        Field field3 = testClass.getDeclaredField("field3");

        assertNotNull(getInternalState(testClassInstance, "field1"));
        assertEquals(mockComponent1, getInternalState(testClassInstance, "field1"));

        assertNotNull(getInternalState(testClassInstance, "field2"));
        assertEquals(mockComponent2, getInternalState(testClassInstance, "field2"));

        assertNotNull(getInternalState(testClassInstance, "field3"));
        assertEquals(mockComponent3, getInternalState(testClassInstance, "field3"));

        // Check that mocks were removed from the mocks set
        assertEquals(2, mocks.size());
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithMultipleCandidates() {
        Class<TestClass> testClass = TestClass.class;
        Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());

        Set<Object> mocks = new HashSet<>();
        mocks.add(mockComponent1);
        mocks.add(mockComponent2);

        Object testClassInstance = Whitebox.newInstance(testClass);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        Field field1 = testClass.getDeclaredField("field1");
        Field field2 = testClass.getDeclaredField("field2");
        Field field3 = testClass.getDeclaredField("field3");

        assertEquals(mockComponent1, getInternalState(testClassInstance, "field1"));
        assertEquals(mockComponent2, getInternalState(testClassInstance, "field2"));
        assertEquals(mockComponent3, getInternalState(testClassInstance, "field3"));

        // Check that the same mock was not injected twice
        assertEquals(2, mocks.size());
        assertArrayEquals(new Object[]{mockComponent1, mockComponent2}, mocks.toArray());
    }

    @Test
    public void testInjectMockCandidate() {
        Class<TestClass> testClass = TestClass.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mockComponent1);

        Object testClassInstance = Whitebox.newInstance(testClass);

        for (Field field : orderedInstanceFieldsFrom(testClass)) {
            mockCandidateFilterTest(mocks, field, testClassInstance);
        }

        assertEquals(0, mocks.size());
    }

    @Test
    public void testInjectMockCandidateWithMultipleCandidatesByName() {
        Class<TestClass> testClass = TestClass.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mockComponent1);
        mocks.add(mockComponent2);

        Object testClassInstance = Whitebox.newInstance(testClass);

        for (Field field : orderedInstanceFieldsFrom(testClass)) {
            mockCandidateFilterTest(mocks, field, testClassInstance);
        }

        assertEquals(1, mocks.size());
        assertEquals(mockComponent2, getInternalState(testClassInstance, "field2"));
    }

    @Test
    public void testInjectMockCandidateWithMultipleCandidatesByType() {
        Class<TestClass> testClass = TestClass.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mockComponent1);
        mocks.add(mockComponent2);
        mocks.add(mockComponent3);

        Object testClassInstance = Whitebox.newInstance(testClass);

        for (Field field : orderedInstanceFieldsFrom(testClass)) {
            mockCandidateFilterTest(mocks, field, testClassInstance);
        }

        assertEquals(2, mocks.size());
        assertArrayEquals(new Object[]{mockComponent2, mockComponent3}, mocks.toArray());
    }

    @Test
    public void testInjectMockCandidateWithNoCandidates() {
        Class<TestClass> testClass = TestClass.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mockComponent1);

        Object testClassInstance = Whitebox.newInstance(testClass);

        for (Field field : orderedInstanceFieldsFrom(testClass)) {
            mockCandidateFilterTest(mocks, field, testClassInstance);
        }

        assertNull(getInternalState(testClassInstance, "field1"));
        assertNull(getInternalState(testClassInstance, "field2"));
        assertNull(getInternalState(testClassInstance, "field3"));
    }

    private void mockCandidateFilterTest(Set<Object> mocks, Field field, Object testClassInstance) {
        MockCandidateFilter candidateFilter = injectionEngine.mockCandidateFilter;

        MockitoMockCandidate candidate1 = new MockitoMockCandidate(mockComponent1.getClass(), field, mockComponent1);
        MockitoMockCandidate candidate2 = new MockitoMockCandidate(mockComponent2.getClass(), field, mockComponent2);
        MockitoMockCandidate candidate3 = new MockitoMockCandidate(mockComponent3.getClass(), field, mockComponent3);

        candidateFilter.filterCandidate(mocks, field, testClassInstance).thenInject();

        if (mocks.size() > 0) {
            Object mock = mocks.iterator().next();
            assertEquals(mock, getInternalState(testClassInstance, field.getName()));
            mocks.remove(mock);
        }
    }

    private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }

    private static class MockitoMockCandidate {
        private final Class<?> clazz;
        private final Field field;
        private final Object mock;

        public MockitoMockCandidate(Class<?> clazz, Field field, Object mock) {
            this.clazz = clazz;
            this.field = field;
            this.mock = mock;
        }

        public MockCandidateFilter filterCandidate(Set<Object> mocks, Field field, Object fieldInstance) {
            if (clazz.isAssignableFrom(fieldInstance.getClass())) {
                mocks.add(mock);
                return new MockCandidateFilter() {
                    @Override
                    public void inject() {
                        try {
                            FieldInitializer fieldInitializer = new FieldInitializer(fieldInstance, field);
                            fieldInitializer.inject(mock);
                        } catch (IllegalAccessException e) {
                            throw new AssertionError("Failed to inject mock via property setter", e);
                        }
                    }
                };
            }
            return new MockCandidateFilter() {
                @Override
                public void inject() {}
            };
        }
    }
}

class TestClass {
    @InjectMocks
    private Field1 field1;

    @InjectMocks
    private Field2 field2;

    @InjectMocks
    private Field3 field3;

    public static class Field1 {
        @Inject
        private Field3 field3;
    }

    public static class Field2 {
        @Inject
        private Field3 field3;
    }

    public static class Field3 {}
}