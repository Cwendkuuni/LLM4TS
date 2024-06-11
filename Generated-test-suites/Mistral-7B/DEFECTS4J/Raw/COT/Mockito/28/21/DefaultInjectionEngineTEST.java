package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = mock(TestClass.class);
    }

    // Test for injectMocksOnFields method

    @Test
    public void testInjectMocksOnFields_NoInjectMocksAnnotatedFields() {
        Set<Field> injectMocksFields = Arrays.asList(new Field[0]).hashSet();
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertNotNull(testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksAnnotatedFieldWithMockCandidate() {
        Field injectMocksField = Mockito.spy(new Object()[0][0]);
        Field mockCandidate = new Object()[0];
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(injectMocksField);
        }};

        mocks.add(mockCandidate);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertNotNull(injectMocksField.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_InjectMocksAnnotatedFieldWithNoMockCandidate() {
        Field injectMocksField = new Object()[0];
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(injectMocksField);
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksAnnotatedFieldWithMultipleMockCandidates() {
        Field mockCandidate1 = mock(String.class);
        Field mockCandidate2 = mock(String.class);
        Field injectMocksField = new Object()[0];
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(injectMocksField);
        }};

        mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertNotNull(injectMocksField.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksAnnotatedFieldWithNoMockCandidateByName() {
        Field injectMocksField = new Object()[0];
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(injectMocksField);
        }};

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMocksOnFields_InjectMocksAnnotatedFieldWithMockCandidateByName() {
        Field injectMocksField = new Object()[0];
        Field mockCandidate = mock(String.class);
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(injectMocksField);
        }};

        mocks.add(mockCandidate);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertNotNull(injectMocksField.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFields_InjectMocksAnnotatedFieldWithMultipleMockCandidatesByName() {
        Field injectMocksField = new Object()[0];
        Field mockCandidate1 = mock(String.class);
        Field mockCandidate2 = mock(String.class);
        Set<Field> injectMocksFields = new HashSet<Field>() {{
            add(injectMocksField);
        }};

        mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    }

    // Test for injectMockCandidate method

    @Test
    public void testInjectMockCandidate_NoInjectMocksAnnotatedFields() {
        Class<?> awaitingInjectionClazz = Object.class;
        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
        assertNotNull(testClassInstance);
    }

    @Test
    public void testInjectMockCandidate_InjectMocksAnnotatedFieldWithMockCandidate() {
        Field mockCandidate = mock(String.class);
        Class<?> awaitingInjectionClazz = String.class;

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
        assertNotNull(testClassInstance);
    }

    @Test
    public void testInjectMockCandidate_InjectMocksAnnotatedFieldWithNoMockCandidate() {
        Field field = new Object()[0];
        Class<?> awaitingInjectionClazz = String.class;

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
    }

    @Test
    public void testInjectMockCandidate_InjectMocksAnnotatedFieldWithMultipleMockCandidates() {
        Field mockCandidate1 = mock(String.class);
        Field mockCandidate2 = mock(String.class);
        Field field = new Object()[0];
        Class<?> awaitingInjectionClazz = String.class;

        mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
        assertNotNull(field.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    // Test for orderedInstanceFieldsFrom method

    @Test
    public void testOrderedInstanceFieldsFrom_OrderedFields() {
        Class<?> awaitingInjectionClazz = TestClassWithOrderedFields.class;
        Field[] expectedFields = {testField1, testField2, testField3};
        Field[] actualFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertArrayEquals(expectedFields, actualFields);
    }

    // Test for filterCandidate method

    @Test
    public void testFilterCandidate_TypeBasedCandidateFilter() {
        Class<?> awaitingInjectionClazz = TestClassWithTypeBasedCandidate.class;
        Field mockCandidate = mock(String.class);
        Field field = new Object()[0];

        MockCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        Set<Object> mocksCopy = new HashSet<Object>(mocks);

        typeBasedCandidateFilter.filterCandidate(mocksCopy, field, fieldInstance).thenInject();

        assertFalse(mocksCopy.contains(mockCandidate));
        assertNotNull(field.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    @Test
    public void testFilterCandidate_NameBasedCandidateFilter() {
        Class<?> awaitingInjectionClazz = TestClassWithNameBasedCandidate.class;
        Field mockCandidate = mock(String.class);
        Field field = new Object()[0];

        MockCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(new FinalMockCandidateFilter());
        Set<Object> mocksCopy = new HashSet<Object>(mocks);

        nameBasedCandidateFilter.filterCandidate(mocksCopy, field, fieldInstance).thenInject();

        assertFalse(mocksCopy.contains(mockCandidate));
        assertNotNull(field.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    @Test
    public void testFilterCandidate_FinalMockCandidateFilter() {
        Class<?> awaitingInjectionClazz = TestClassWithFinalMockCandidate.class;
        Field mockCandidate = mock(String.class);
        Field field = new Object()[0];

        MockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        Set<Object> mocksCopy = new HashSet<Object>(mocks);

        finalMockCandidateFilter.filterCandidate(mocksCopy, field, fieldInstance).thenInject();

        assertFalse(mocksCopy.contains(mockCandidate));
        assertNotNull(field.get(testClassInstance));
        assertNotNull(testClassInstance);
    }

    // Helper classes

    public static class TestClass {
    }

    public static class TestClassWithOrderedFields {
        @InjectMocks
        private static Field testField1;
        @InjectMocks
        private static Field testField2;
        @InjectMocks
        private static Field testField3;
    }

    public static class TestClassWithTypeBasedCandidate {
        @InjectMocks
        private static Field field;
        @Mock
        private static String fieldInstance;
    }

    public static class TestClassWithNameBasedCandidate {
        @InjectMocks
        private static Field field;
        @Mock
        private static Object fieldInstance;
    }

    public static class TestClassWithFinalMockCandidate {
        @InjectMocks
        private static final Field field;
        @Mock
        private static Object fieldInstance;
    }
}