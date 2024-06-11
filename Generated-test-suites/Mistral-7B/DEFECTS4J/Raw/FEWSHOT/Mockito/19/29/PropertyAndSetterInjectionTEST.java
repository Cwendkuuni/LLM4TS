package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.*;
import java.util.*;

public class PropertyAndSetterInjectionTest {
    private PropertyAndSetterInjection injectionStrategy;
    private ListUtil listUtil;
    private SuperTypesLastSorter superTypesLastSorter;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        listUtil = new ListUtil();
        superTypesLastSorter = new SuperTypesLastSorter();
    }

    @Test
    public void testProcessInjection() throws Exception {
        Field field = getField("fieldToInject", MyClass.class);
        MyClass instance = new MyClass();
        Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(new MockedClass(), new OtherMockedClass()));
        FieldInitializationReport report = new FieldInitializer(instance, field).initialize();

        injectionStrategy.processInjection(field, instance, mockCandidates);

        assertTrue(mockCandidates.isEmpty());
        assertNotNull(report.fieldInstance());
    }

    @Test
    public void testProcessInjection_NoMockFound() throws Exception {
        Field field = getField("fieldToInject", MyClass.class);
        MyClass instance = new MyClass();
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.emptyList());
        FieldInitializationReport report = new FieldInitializer(instance, field).initialize();

        injectionStrategy.processInjection(field, instance, mockCandidates);

        assertFalse(report.wasInjected());
        assertNull(report.fieldInstance());
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Field field = getField("fieldToInject", MyClass.class);
        MyClass instance = new MyClass();

        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, instance);

        assertNotNull(report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_InitializationFailed() throws Exception {
        Field field = getField("fieldToInject", MyClass.class);
        MyClass instance = new MyClass();

        FieldInitializer fieldInitializer = new FieldInitializer(instance, field);
        MockitoException exception = assertThrows(MockitoException.class, () -> fieldInitializer.initialize());

        injectionStrategy.initializeInjectMocksField(field, instance);
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        MyClass awaitingInjectionClass = MyClass.class;
        Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(new MockedClass(), new OtherMockedClass()));
        Object instance = new MyClass();

        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClass, mockCandidates, instance);

        assertTrue(injectionOccurred);
        assertTrue(mockCandidates.isEmpty());
    }

    @Test
    public void testInjectMockCandidates_NoMockFound() throws Exception {
        MyClass awaitingInjectionClass = MyClass.class;
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.emptyList());
        Object instance = new MyClass();

        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        boolean injectionOccurred = injectionStrategy.injectMockCandidates(awaitingInjectionClass, mockCandidates, instance);

        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        MyClass awaitingInjectionClass = MyClass.class;
        Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(new MockedClass(), new OtherMockedClass()));
        Object instance = new MyClass();

        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        boolean injectionOccurred = injectionStrategy.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        assertTrue(injectionOccurred);
        assertTrue(mockCandidates.isEmpty());
    }

    @Test
    public void testInjectMockCandidatesOnFields_NoMockFound() throws Exception {
        MyClass awaitingInjectionClass = MyClass.class;
        Set<Object> mockCandidates = newMockSafeHashSet(Collections.emptyList());
        Object instance = new MyClass();

        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        boolean injectionOccurred = injectionStrategy.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields);

        assertFalse(injectionOccurred);
    }

    @Test
    public void testOrderedInstanceFieldsFrom() throws Exception {
        MyClass awaitingInjectionClass = MyClass.class;

        List<Field> orderedInstanceFields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClass);

        List<Field> expectedOrderedInstanceFields = Arrays.asList(getField("field3", MyClass2.class), getField("field2", MyClass2.class), getField("field1", MyClass2.class), getField("field0", MyClass2.class), getField("field0", MyClass.class), getField("field1", MyClass.class), getField("field2", MyClass.class), getField("field3", MyClass.class));

        assertEquals(expectedOrderedInstanceFields, orderedInstanceFields);
    }

    private Field getField(String name, Class<?> clazz) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    private static class MyClass {
        @InjectMocks
        @SuppressWarnings("unused")
        private MyClass2 fieldToInject;

        public MyClass() {
        }
    }

    private static class MyClass2 {
        // fields and constructors here
    }

    private static class MockedClass implements MyClass2 {
        // mock implementation here
    }

    private static class OtherMockedClass implements MyClass2 {
        // mock implementation here
    }
}