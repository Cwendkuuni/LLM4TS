package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        boolean result = strategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        assertFalse(result);
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();

        FieldInitializationReport report = strategy.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        assertNotNull(report);
        assertEquals(MockitoException.class, report.getTargetException().getClass());
    }

    @Test
    public void testInjectMockCandidates() throws Exception {
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object instance = new Object();

        boolean result = strategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        assertFalse(result);
        assertTrue(mocks.isEmpty());
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(Object.class));
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        field.setAccessible(true);
        orderedInstanceFields.add(field);

        boolean result = strategy.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

        assertTrue(result);
        assertTrue(mocks.isEmpty());
        assertTrue(orderedInstanceFields.isEmpty());
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        PropertyAndSetterInjection strategy = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;

        List<Field> result = strategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        assertFalse(result.isEmpty());
    }

    private class MockCandidateFilter implements org.mockito.internal.configuration.injection.MockCandidateFilter {
        public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
            return null;
        }

        public Object thenInject() {
            return null;
        }
    }
}