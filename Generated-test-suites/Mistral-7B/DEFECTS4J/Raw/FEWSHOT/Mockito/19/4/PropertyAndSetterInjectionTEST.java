package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Field field = clazz.getDeclaredField("injectMocksField");
        Object instance = new Object();
        FieldInitializationReport report = new PropertyAndSetterInjection().initializeInjectMocksField(field, instance);
        assertNotNull(report);
        assertEquals(clazz, report.fieldClass());
        assertEquals(instance, report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithException() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClassWithException");
        Field field = clazz.getDeclaredField("injectMocksField");
        Object instance = new Object();
        new PropertyAndSetterInjection().initializeInjectMocksField(field, instance);
    }

    @Test
    public void testProcessInjection() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        Field injectMocksField = clazz.getDeclaredField("injectMocksField");
        assertTrue(new PropertyAndSetterInjection().processInjection(injectMocksField, instance, mockCandidates));
        assertFalse(mockCandidates.contains(new MockitoTestClass()));
        assertFalse(mockCandidates.contains(new AnotherMockitoTestClass()));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithException() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClassWithException");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClassWithException(), new AnotherMockitoTestClassWithException()});
        Field injectMocksField = clazz.getDeclaredField("injectMocksField");
        new PropertyAndSetterInjection().processInjection(injectMocksField, instance, mockCandidates);
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        assertTrue(new PropertyAndSetterInjection().injectMockCandidates(clazz, mockCandidates, instance));
        assertFalse(mockCandidates.contains(new MockitoTestClass()));
        assertFalse(mockCandidates.contains(new AnotherMockitoTestClass()));
    }

    @Test
    public void testInjectMockCandidatesPass1() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        List<Field> orderedInstanceFields = Arrays.asList(clazz.getDeclaredFields());
        assertTrue(new PropertyAndSetterInjection().injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
        assertFalse(mockCandidates.contains(new MockitoTestClass()));
        assertFalse(mockCandidates.contains(new AnotherMockitoTestClass()));
    }

    @Test
    public void testInjectMockCandidatesPass2() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        List<Field> orderedInstanceFields = Arrays.asList(clazz.getDeclaredFields());
        assertTrue(new PropertyAndSetterInjection().injectMockCandidatesOnFields(mockCandidates, instance, true, orderedInstanceFields));
        assertFalse(mockCandidates.contains(new MockitoTestClass()));
        assertFalse(mockCandidates.contains(new AnotherMockitoTestClass()));
    }

    @Test
    public void testInjectMockCandidatesPass1WithMock() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        List<Field> orderedInstanceFields = Arrays.asList(clazz.getDeclaredFields());
        MockitoTestClass mock = mock(MockitoTestClass.class);
        MockCandidateFilter filter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        Object injected = filter.filterCandidate(mockCandidates, orderedInstanceFields.get(0), instance).thenInject();
        assertTrue(injected == mock);
        assertTrue(new PropertyAndSetterInjection().injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
        assertTrue(mockCandidates.contains(new MockitoTestClass()));
        assertFalse(mockCandidates.contains(new AnotherMockitoTestClass()));
        assertFalse(orderedInstanceFields.contains(orderedInstanceFields.get(0)));
    }

    @Test
    public void testInjectMockCandidatesPass2WithMock() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        List<Field> orderedInstanceFields = Arrays.asList(clazz.getDeclaredFields());
        MockitoTestClass mock = mock(MockitoTestClass.class);
        MockitoTestClass mock2 = mock(MockitoTestClass.class);
        MockCandidateFilter filter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
        Object injected = filter.filterCandidate(mockCandidates, orderedInstanceFields.get(0), instance).thenInject();
        assertTrue(injected == mock);
        assertTrue(new PropertyAndSetterInjection().injectMockCandidatesOnFields(mockCandidates, instance, true, orderedInstanceFields));
        assertFalse(mockCandidates.contains(new MockitoTestClass()));
        assertFalse(mockCandidates.contains(new AnotherMockitoTestClass()));
        assertFalse(orderedInstanceFields.contains(orderedInstanceFields.get(0)));

        injected = filter.filterCandidate(mockCandidates, orderedInstanceFields.get(1), instance).thenInject();
        assertTrue(injected == mock2);
        assertTrue(new PropertyAndSetterInjection().injectMockCandidatesOnFields(mockCandidates, instance, true, orderedInstanceFields));
        assertTrue(mockCandidates.contains(new MockitoTestClass()));
        assertTrue(mockCandidates.contains(new AnotherMockitoTestClass()));
        assertFalse(orderedInstanceFields.contains(orderedInstanceFields.get(0)));
        assertFalse(orderedInstanceFields.contains(orderedInstanceFields.get(1)));
    }

    @Test
    public void testInjectMockCandidatesWithNull() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        Object instance = new Object();
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new MockitoTestClass(), new AnotherMockitoTestClass()});
        List<Field> orderedInstanceFields = Arrays.asList(clazz.getDeclaredFields());
        assertFalse(new PropertyAndSetterInjection().injectMockCandidatesOnFields(mockCandidates, null, false, orderedInstanceFields));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        Class<?> clazz = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjectionTest$TestClass");
        List<Field> orderedInstanceFields = new PropertyAndSetterInjection().orderedInstanceFieldsFrom(clazz);
        assertEquals(2, orderedInstanceFields.size());
        Field field1 = orderedInstanceFields.get(0);
        assertTrue(field1.getName().equals("field1"));
        Field field2 = orderedInstanceFields.get(1);
        assertTrue(field2.getName().equals("field2"));
    }

    @Test
    public void testOrderedInstanceFieldsFromNull() {
        assertNull(new PropertyAndSetterInjection().orderedInstanceFieldsFrom(null));
    }

    static class TestClass {
        @InjectMocks
        private Field injectMocksField;
        private Field field1;
        private Field field2;
    }

    static class TestClassWithException extends TestClass {
        @InjectMocks
        private Field injectMocksField;
        private Field field1;
        private Field field2;

        @PostConstruct
        public void init() {
            throw new RuntimeException();
        }
    }

    static class MockitoTestClass {

    }

    static class AnotherMockitoTestClass {

    }

    static class MockitoTestClassWithException {

    }

    static class AnotherMockitoTestClassWithException {

    }
}