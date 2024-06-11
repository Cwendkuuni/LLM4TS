package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.Reflections.getDeclaredFields;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws IllegalAccessException, InstantiationException {
        Object target = Mockito.mock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(target, Mockito.mock(Object.class));

        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(Mockito.mock(ClassA.class));
        mockCandidates.add(Mockito.mock(ClassB.class));

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        assertTrue(injection.processInjection(injectMocksField, target, mockCandidates));

        // Verify that mock from ClassA was injected
        Field fieldA = Reflections.getDeclaredField(TargetClass.class, "fieldA");
        assertNotNull(fieldA.get(target));

        // Verify that mock from ClassB was not injected
        Field fieldB = Reflections.getDeclaredField(TargetClass.class, "fieldB");
        assertNull(fieldB.get(target));

        // Verify that no mocks are left in the list
        assertTrue(mockCandidates.isEmpty());
    }

    @Test
    public void testProcessInjection_NoMocksFound() throws IllegalAccessException, InstantiationException {
        Object target = Mockito.mock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(target, Mockito.mock(Object.class));

        Set<Object> mockCandidates = new HashSet<>();

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        assertFalse(injection.processInjection(injectMocksField, target, mockCandidates));

        // Verify that no mocks were injected
        Field fieldA = Reflections.getDeclaredField(TargetClass.class, "fieldA");
        assertNull(fieldA.get(target));

        Field fieldB = Reflections.getDeclaredField(TargetClass.class, "fieldB");
        assertNull(fieldB.get(target));

        // Verify that no mocks are left in the list
        assertTrue(mockCandidates.isEmpty());
    }

    @Test
    public void testProcessInjection_NoMockCandidates() throws IllegalAccessException, InstantiationException {
        Object target = Mockito.mock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);
        injectMocksField.setAccessible(true);
        injectMocksField.set(target, Mockito.mock(Object.class));

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        Set<Object> emptyMockCandidates = Collections.emptySet();

        assertFalse(injection.processInjection(injectMocksField, target, emptyMockCandidates));

        // Verify that no mocks were injected
        Field fieldA = Reflections.getDeclaredField(TargetClass.class, "fieldA");
        assertNull(fieldA.get(target));

        Field fieldB = Reflections.getDeclaredField(TargetClass.class, "fieldB");
        assertNull(fieldB.get(target));
    }

    @Test
    public void testInitializeInjectMocksField() throws InstantiationException, IllegalAccessException {
        Object target = Mockito.mock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, target);

        assertNotNull(report);
        assertNull(report.fieldInstance());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksField_InitializationFailed() throws InstantiationException, IllegalAccessException {
        Object target = Mockito.mock(Object.class);
        Field injectMocksField = Mockito.mock(Field.class);

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        FieldInitializationReport report = injection.initializeInjectMocksField(injectMocksField, target);

        Throwable cause = report.getCause();
        cause.printStackTrace();
    }

    static class TargetClass {
        @InjectMocks
        private ClassA fieldA;
        private ClassB fieldB;
    }

    static class ClassA {}
    static class ClassB {}
}