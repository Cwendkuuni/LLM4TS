package org.mockito.internal.configuration.injection;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    @Rule
    public MockitoJUnit MockitoJUnitRule = new MockitoJUnit();

    @Test
    public void testProcessInjection() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<?> testClass = MockitoJUnitRunner.class;
        Object mockitoJUnit = testClass.newInstance();
        Field injectMocksField = testClass.getDeclaredField("mocks");
        injectMocksField.setAccessible(true);
        Set<Object> mockCandidates = newHashSetOf(Mockito.mock(Number.class));

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        assertTrue(injection.processInjection(injectMocksField, mockitoJUnit, mockCandidates));
        assertFalse(mockCandidates.isEmpty());
        assertNull(injectMocksField.get(mockitoJUnit));
    }

    @Test
    public void testInitializeInjectMocksField() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        Class<?> testClass = MockitoJUnitRunner.class;
        Object mockitoJUnit = testClass.newInstance();
        Field injectMocksField = testClass.getDeclaredField("mocks");
        injectMocksField.setAccessible(true);

        Field field = testClass.getDeclaredField("mocks2");
        field.setAccessible(true);

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        FieldInitializationReport report = injection.initializeInjectMocksField(field, mockitoJUnit);
        assertNotNull(report);
        assertNull(field.get(mockitoJUnit));
    }

    @Test(expected = MockitoException.class)
    public void testInitializeInjectMocksFieldWithException() throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        Class<?> testClass = MockitoJUnitRunner.class;
        Object mockitoJUnit = testClass.newInstance();
        Field injectMocksField = testClass.getDeclaredField("mocks");
        injectMocksField.setAccessible(true);

        Field field = testClass.getDeclaredField("mocks2");
        field.setAccessible(true);

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        injection.initializeInjectMocksField(field, mockitoJUnit);
    }

    @Test
    public void testInjectMockCandidates() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<?> testClass = MockitoJUnitRunner.class;
        Object mockitoJUnit = testClass.newInstance();
        Field injectMocksField = testClass.getDeclaredField("mocks");
        injectMocksField.setAccessible(true);

        Set<Object> mockCandidates = newHashSetOf(Mockito.mock(Number.class), Mockito.mock(String.class));

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Class<?> numberClass = Number.class;
        Class<?> stringClass = String.class;

        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(testClass);

        assertTrue(injection.injectMockCandidates(numberClass, mockCandidates, null, orderedInstanceFields));
        assertFalse(mockCandidates.isEmpty());
        Mockito.verify(mockCandidates.iterator().next()).equals(numberClass);

        assertTrue(injection.injectMockCandidates(stringClass, mockCandidates, true, orderedInstanceFields));
        assertFalse(mockCandidates.isEmpty());
        Mockito.verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<?> testClass = MockitoJUnitRunner.class;
        Object mockitoJUnit = testClass.newInstance();
        Field injectMocksField = testClass.getDeclaredField("mocks");
        injectMocksField.setAccessible(true);

        Set<Object> mockCandidates = newHashSetOf(Mockito.mock(Number.class), Mockito.mock(String.class));

        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Class<?> numberClass = Number.class;
        Class<?> stringClass = String.class;

        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(testClass);

        assertTrue(injection.injectMockCandidatesOnFields(mockCandidates, mockitoJUnit, false, orderedInstanceFields));
        assertFalse(mockCandidates.isEmpty());
        Mockito.verify(mockCandidates.iterator().next()).equals(numberClass);

        assertTrue(injection.injectMockCandidatesOnFields(mockCandidates, mockitoJUnit, true, orderedInstanceFields));
        assertFalse(mockCandidates.isEmpty());
        Mockito.verifyNoMoreInteractions(mockCandidates.iterator().next());
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> testClass = MockitoJUnitRunner.class;
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(testClass);
        List<String> fieldNames = newArrayList();
        for (Field field : orderedInstanceFields) {
            fieldNames.add(field.getName());
        }
        List<String> expectedFieldNames = asList("mocks2", "mocks");
        assertEquals(expectedFieldNames, fieldNames);
    }

    private Set<Object> newHashSetOf(Object... objects) {
        return newMockSafeHashSet(Arrays.asList(objects));
    }

    private List<String> asList(Object... objects) {
        return Arrays.asList(objects);
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();
        return injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}