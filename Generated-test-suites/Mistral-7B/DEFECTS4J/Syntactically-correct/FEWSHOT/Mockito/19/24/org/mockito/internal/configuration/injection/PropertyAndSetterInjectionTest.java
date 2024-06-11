package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        Class<Object> targetClass = DummyClass.class;
        Object instance = targetClass.getDeclaredConstructor().newInstance();
        Field injectMocksField = targetClass.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new DummyClassWithMockA());
        mockCandidates.add(new DummyClassWithMockB());
        mockCandidates.add(new DummyClassWithMockC());

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        assertTrue(injection.processInjection(injectMocksField, instance, mockCandidates));

        assertNull(injectMocksField.get(instance));

        Class<?> fieldClass = DummyClassWithMockA.class;
        Object fieldInstanceNeedingInjection = new DummyClassWithMockA();
        while (fieldClass != Object.class) {
            assertTrue(injection.injectMockCandidates(fieldClass, mockCandidates, fieldInstanceNeedingInjection));
            fieldClass = fieldClass.getSuperclass();
        }

        assertNotNull(injectMocksField.get(instance));
    }

    @Test
    public void testProcessInjectionNoMockFound() throws Exception {
        Class<Object> targetClass = DummyClass.class;
        Object instance = targetClass.getDeclaredConstructor().newInstance();
        Field injectMocksField = targetClass.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new DummyClassWithoutMock());

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        assertFalse(injection.processInjection(injectMocksField, instance, mockCandidates));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionInitializationException() throws Exception {
        Class<Object> targetClass = DummyClass.class;
        Object instance = targetClass.getDeclaredConstructor().newInstance();
        Field injectMocksField = targetClass.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new DummyClassWithExceptionThrowingMock());

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        injection.processInjection(injectMocksField, instance, mockCandidates);
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        Class<Object> targetClass = DummyClass.class;
        Object instance = targetClass.getDeclaredConstructor().newInstance();
        Set<Object> mockCandidates = new HashSet<>();

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Class<?> firstClass = DummyClassWithMockA.class;
        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(firstClass);
        assertTrue(injection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
        assertNotNull(instance.getClass().getFields().get(0).get(instance));

        Class<?> secondClass = DummyClassWithMockB.class;
        assertTrue(injection.injectMockCandidatesOnFields(mockCandidates, instance, true, orderedInstanceFields));
        assertNull(instance.getClass().getFields().get(1).get(instance));
    }

    @Test
    public void testInjectMockCandidatesNoMockFound() throws Exception {
        Class<Object> targetClass = DummyClass.class;
        Object instance = targetClass.getDeclaredConstructor().newInstance();
        Set<Object> mockCandidates = new HashSet<>();

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(targetClass);
        assertTrue(injection.injectMockCandidatesOnFields(mockCandidates, instance, false, orderedInstanceFields));
        assertNull(instance.getClass().getFields().get(0).get(instance));
    }

    static class DummyClass {
        @InjectMocks
        private Object injectMocksField;

        public DummyClass() {
        }

        public DummyClass(Object injectMocksField) {
            this.injectMocksField = injectMocksField;
        }

        public static class DummyClassWithMockA extends DummyClass {
            public DummyClassWithMockA() {
                super(new DummyClassWithMockC());
            }
        }

        public static class DummyClassWithMockB extends DummyClass {
            public DummyClassWithMockB() {
                super(new DummyClassWithMockA());
            }
        }

        public static class DummyClassWithMockC extends DummyClass {
            public DummyClassWithMockC() {
                super();
            }
        }

        public static class DummyClassWithoutMock extends DummyClass {
        }

        public static class DummyClassWithExceptionThrowingMock extends DummyClass {
            public DummyClassWithExceptionThrowingMock() {
                throw new RuntimeException();
            }
        }
    }
}