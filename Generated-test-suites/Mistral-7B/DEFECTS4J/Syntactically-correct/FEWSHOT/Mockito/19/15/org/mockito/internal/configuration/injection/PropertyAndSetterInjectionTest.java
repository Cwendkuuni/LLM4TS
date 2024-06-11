package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.mockCandidate;
import static org.mockito.internal.util.collections.Lists.newArrayList;
import static org.mockito.internal.util.reflection.ReflectionUtils.getField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {

    private static class TestClass {

        @InjectMocks
        private TestInnerClass innerClass;

        private TestClass() {
        }

        static class TestInnerClass {
            @Inject
            private TestAnotherClass anotherClass;

            private TestInnerClass() {
            }
        }

        static class TestAnotherClass {
            private TestAnotherClass() {
            }
        }

    }

    private TestClass testClass;
    private TestClass testClassSpy;
    private Object mockAnotherClass;
    private Field innerClassField;
    private Field anotherClassField;

    @Before
    public void setUp() throws Exception {
        testClass = new TestClass();
        testClassSpy = Mockito.spy(TestClass.class);
        mockAnotherClass = Mockito.mock(TestClass.TestAnotherClass.class);
        innerClassField = getField(TestClass.class, "innerClass");
        anotherClassField = getField(TestClass.TestInnerClass.class, "anotherClass");
    }

    @Test
    public void processInjection_noInjectMocksAnnotation_returnsFalse() throws Exception {
        Set<Object> mockCandidates = new HashSet<>();
        assertFalse(new PropertyAndSetterInjection().processInjection(null, testClassSpy, mockCandidates));
    }

    @Test
    public void processInjection_withoutMocks_returnsFalse() throws Exception {
        Set<Object> mockCandidates = new HashSet<>();
        assertFalse(new PropertyAndSetterInjection().processInjection(innerClassField, testClass, mockCandidates));
    }

    @Test
    public void processInjection_injectsMockThroughSetter() throws Exception {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockAnotherClass);
        Mockito.when(mockAnotherClass.getName()).thenReturn("mockAnotherClass");

        boolean injectionOccurred = new PropertyAndSetterInjection().processInjection(innerClassField, testClass, mockCandidates);
        assertSame(mockAnotherClass, getFieldValue(innerClassField, testClass));
        assertFalse(mockCandidates.contains(mockAnotherClass));
        assertTrue(injectionOccurred);
    }

    @Test
    public void processInjection_injectsMockThroughField() throws Exception {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockAnotherClass);
        Mockito.when(mockAnotherClass.getName()).thenReturn("mockAnotherClass");

        // Injecting through setter is tried first, so we need to replace the value set by the setter
        setField(testClass, "innerClass", new TestClass().new TestInnerClass());

        boolean injectionOccurred = new PropertyAndSetterInjection().processInjection(innerClassField, testClass, mockCandidates);
        assertSame(mockAnotherClass, getFieldValue(anotherClassField, testClass.innerClass));
        assertFalse(mockCandidates.contains(mockAnotherClass));
        assertTrue(injectionOccurred);
    }

    @Test
    public void processInjection_multipleCandidates_injectsByName() throws Exception {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(mockAnotherClass);
        mockCandidates.add(Mockito.mock(TestClass.TestAnotherClass.class));

        Mockito.when(mockAnotherClass.getName()).thenReturn("mockAnotherClass");
        Mockito.when(mockAnotherClass2.getName()).thenReturn("mockAnotherClass2");

        boolean injectionOccurred = new PropertyAndSetterInjection().processInjection(innerClassField, testClass, mockCandidates);
        assertSame(mockAnotherClass, getFieldValue(anotherClassField, testClass.innerClass));
        assertFalse(mockCandidates.contains(mockAnotherClass));
        assertTrue(injectionOccurred);
    }

    @Test
    public void processInjection_noCandidates_doesNotFail() throws Exception {
        Set<Object> mockCandidates = new HashSet<>();
        assertTrue(new PropertyAndSetterInjection().processInjection(innerClassField, testClass, mockCandidates));
    }

    @Test
    public void initializeInjectMocksField_noAnnotation_returnsNull() throws Exception {
        Field field = Mockito.spy(innerClassField);
        assertNull(new PropertyAndSetterInjection().initializeInjectMocksField(field, testClass));
    }

    @Test(expected = InvocationTargetException.class)
    public void initializeInjectMocksField_throwsExceptionOnInitialization() throws Exception {
        Mockito.doThrow(new RuntimeException("Test")).when(testClass).new TestInnerClass();
        new PropertyAndSetterInjection().initializeInjectMocksField(innerClassField, testClass);
    }

    @Test
    public void injectMockCandidates_noCandidates_returnsFalse() throws Exception {
        Set<Object> mocks = new HashSet<>();
        Class<?> awaitingInjectionClazz = TestClass.class;
        Object instance = testClass;
        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        assertFalse(injectionOccurred);
    }

    @Test
    public void injectMockCandidates_injectsOneByType() throws Exception {
        Set<Object> mocks = new HashSet<>();
        mockCandidates.add(mockAnotherClass);
        mockCandidates.add(Mockito.mock(TestClass.TestAnotherClass.class));
        Class<?> awaitingInjectionClazz = TestClass.class;
        Object instance = testClass;

        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        assertSame(mockAnotherClass, getFieldValue(anotherClassField, testClass.innerClass));
        assertFalse(mockCandidates.contains(mockAnotherClass));
        assertTrue(injectionOccurred);
    }

    @Test
    public void injectMockCandidates_injectsOneByName() throws Exception {
        Set<Object> mocks = new HashSet<>();
        mockCandidates.add(mockAnotherClass);
        mockCandidates.add(Mockito.mock(TestClass.TestAnotherClass.class));
        Class<?> awaitingInjectionClazz = TestClass.class;
        Object instance = testClass;

        // Replace the value set by the setter
        setField(testClass, "innerClass", new TestClass().new TestInnerClass());

        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        assertSame(mockAnotherClass, getFieldValue(anotherClassField, testClass.innerClass));
        assertFalse(mockCandidates.contains(mockAnotherClass));
        assertTrue(injectionOccurred);
    }

    @Test
    public void injectMockCandidates_injectsMultipleByName() throws Exception {
        Set<Object> mocks = new HashSet<>();
        mockCandidates.add(mockAnotherClass);
        mockCandidates.add(Mockito.mock(TestClass.TestAnotherClass.class));
        mockCandidates.add(Mockito.mock(TestClass.TestAnotherClass.class));
        Class<?> awaitingInjectionClazz = TestClass.class;
        Object instance = testClass;

        boolean injectionOccurred = new PropertyAndSetterInjection().injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        assertSame(mockAnotherClass, getFieldValue(anotherClassField, testClass.innerClass));
        assertFalse(mockCandidates.contains(mockAnotherClass));
        assertTrue(injectionOccurred);
    }

    private Object getFieldValue(Field field, Object object) throws IllegalAccessException {
        return field.get(object);
    }

}