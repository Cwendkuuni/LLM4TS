package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.testutils.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.internal.util.reflection.testutils.FieldAccessTester.setField;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injection;
    private Object mockCandidates;
    private Object target;

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
        target = new TestClass();
        mockCandidates = Arrays.asList(new MockitoTestClass(), new OtherMockitoTestClass(), new NonMockitoClass()).toArray();
    }

    @Test
    public void testProcessInjection() {
        Set<Object> mocks = newMockSafeHashSet(mockCandidates);
        Set<Object> mocksToBeInjected = new HashSet<>(mocks);

        Field field = TestUtil.getDeclaredField(target, "fieldToInjectMock");
        assertNull(field.get(target));

        FieldInitializationReport report = injection.initializeInjectMocksField(field, target);
        assertTrue(report.isInitializationSuccessful());
        assertEquals(MockitoTestClass.class, report.fieldClass().getType());
        assertNotNull(field.get(target));

        assertFalse(mocksToBeInjected.contains(field.get(target)));
        assertEquals(2, mocksToBeInjected.size());

        // for each field in the class hierarchy
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        while (fieldClass != Object.class) {
            assertTrue(injection.injectMockCandidates(fieldClass, mocks, fieldInstanceNeedingInjection));
            fieldClass = fieldClass.getSuperclass();
        }

        assertFalse(mocksToBeInjected.isEmpty());
        assertFalse(mocksToBeInjected.contains(field.get(target)));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_failedInitialization() {
        Field field = TestUtil.getDeclaredField(target, "fieldToInjectMockWithException");
        injection.initializeInjectMocksField(field, target);
    }

    @Test
    public void testInjectMockCandidates() {
        Class<?> awaitingInjectionClazz = target.getClass();
        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // pass 1
        assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));
        assertFalse(mocks.contains(orderedInstanceFields.get(0).get(target)));

        // pass 2
        assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));
        assertTrue(mocks.isEmpty());

        // check no side effect on target object
        assertNull(target.getInjectedField());
    }

    @Test
    public void testInjectMockCandidates_noMockFound() {
        Class<?> awaitingInjectionClazz = TestClassWithNoMock.class;
        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));
        assertFalse(target.hasInjectedField());
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        Set<Object> mocks = newHashSet(mockCandidates);
        Object instance = new TestClassWithFields();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass().getDeclaredFields());

        // pass 1
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.contains(orderedInstanceFields.get(0).get(instance)));
        assertFalse(mocks.contains(orderedInstanceFields.get(1).get(instance)));

        // pass 2
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.isEmpty());

        // check no side effect on target object
        assertNull(instance.injectedField1);
        assertNull(instance.injectedField2);
    }

    @Test
    public void testInjectMockCandidatesOnFields_noMockFound() {
        Set<Object> mocks = newHashSet(mockCandidates);
        Object instance = new TestClassWithNoMockFields();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass().getDeclaredFields());

        assertFalse(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertNull(instance.injectedField1);
        assertNull(instance.injectedField2);
    }

    @Test
    public void testInjectMockCandidatesOnFields_multipleMockCandidates() {
        Set<Object> mocks = newHashSet(mockCandidates);
        Object instance = new TestClassWithMultipleMockCandidates();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass().getDeclaredFields());

        // pass 1
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertTrue(mocks.contains(orderedInstanceFields.get(0).get(instance)));
        assertFalse(mocks.contains(orderedInstanceFields.get(1).get(instance)));

        // pass 2
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.contains(orderedInstanceFields.get(0).get(instance)));
        assertTrue(mocks.contains(orderedInstanceFields.get(1).get(instance)));
    }

    @Test
    public void testInjectMockCandidateOnFieldBySetter() {
        Set<Object> mocks = newHashSet(mockCandidates);
        Object instance = new TestClassWithMockCandidateBySetter();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass().getDeclaredFields());

        // pass 1
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.contains(orderedInstanceFields.get(0).get(instance)));
        assertFalse(mocks.contains(orderedInstanceFields.get(1).get(instance)));

        // pass 2
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.isEmpty());

        // check injection by setter
        assertNotNull(instance.mockCandidateBySetter);
        assertEquals(MockitoTestClass.class, instance.mockCandidateBySetter.getClass());
    }

    @Test
    public void testInjectMockCandidateOnFieldByField() {
        Set<Object> mocks = newHashSet(mockCandidates);
        Object instance = new TestClassWithMockCandidateByField();
        List<Field> orderedInstanceFields = Arrays.asList(instance.getClass().getDeclaredFields());

        // pass 1
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.contains(orderedInstanceFields.get(0).get(instance)));
        assertFalse(mocks.contains(orderedInstanceFields.get(1).get(instance)));

        // pass 2
        assertTrue(injection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields));
        assertFalse(mocks.isEmpty());

        // check injection by field
        assertNotNull(instance.mockCandidateByField);
        assertEquals(OtherMockitoTestClass.class, instance.mockCandidateByField.getClass());
    }

    private static class TestClass {
        @InjectMocks
        private Object fieldToInjectMock;
        private Object fieldToInjectMockWithException;
        private Object injectedField;

        public Object getInjectedField() {
            return injectedField;
        }

        public void setInjectedField(Object injectedField) {
            this.injectedField = injectedField;
        }
    }

    private static class TestClassWithFields {
        @InjectMocks
        private Object injectedField1;
        @InjectMocks
        private Object injectedField2;
    }

    private static class TestClassWithNoMock {
        @InjectMocks
        private Object fieldToInjectMock;
    }

    private static class TestClassWithNoMockFields {
        @InjectMocks
        private Object injectedField1;
        @InjectMocks
        private Object injectedField2;
    }

    private static class TestClassWithMultipleMockCandidates {
        @InjectMocks
        private Object fieldToInjectMock;

        private static class Candidate1 {}
        private static class Candidate2 {}

        @Inject
        private Candidate1 candidate1;
        @Inject
        private Candidate2 candidate2;
    }

    private static class TestClassWithMockCandidateBySetter {
        private Object mockCandidateBySetter;

        @InjectMocks
        private Object fieldToInjectMock;

        @Inject
        public void setMockCandidateBySetter(@Mock MockitoTestClass mockCandidateBySetter) {
            this.mockCandidateBySetter = mockCandidateBySetter;
        }
    }

    private static class TestClassWithMockCandidateByField {
        private Object mockCandidateByField;

        @InjectMocks
        private Object fieldToInjectMock;

        @Inject
        private OtherMockitoTestClass mockCandidateByField;

        public Object getMockCandidateByField() {
            return this.mockCandidateByField;
        }

        @Inject
        public void setMockCandidateByField(@Mock OtherMockitoTestClass mockCandidateByField) {
            this.mockCandidateByField = mockCandidateByField;
        }
    }
}

class MockitoTestClass {}
class OtherMockitoTestClass {}
class NonMockitoClass {}