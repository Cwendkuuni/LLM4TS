package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Rule
    public MockitoJUnit mockitoRule = new MockitoJUnit();

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_NoMockCandidates_NoFields() {
        Field field = mock(Field.class);
        Object owner = mock(Object.class);
        Set<Object> mockCandidates = newHashSet();

        assertFalse(propertyAndSetterInjection.processInjection(field, owner, mockCandidates));
    }

    @Test
    public void testProcessInjection_NoMockCandidates_FieldsExists() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);
        Object owner = mock(Object.class);
        Set<Object> mockCandidates = newHashSet();

        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(new ArrayList<>());

        assertFalse(propertyAndSetterInjection.processInjection(field, owner, mockCandidates));
    }

    @Test
    public void testProcessInjection_MockCandidates_NoFields() {
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        Object owner = mock(Object.class);
        Set<Object> mockCandidates = newHashSet(mock);

        assertFalse(propertyAndSetterInjection.processInjection(field, owner, mockCandidates));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_MockCandidates_FieldsWithException() throws MockitoException {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);
        Object owner = mock(Object.class);
        Set<Object> mockCandidates = newHashSet(mock(Object.class), mock(Object.class));

        when(awaitingInjectionClazz.getDeclaredFields()).thenReturn(new ArrayList<>());

        propertyAndSetterInjection.processInjection(field, owner, mockCandidates);
    }

    @Test
    public void testProcessInjection_MockCandidates_FieldsWithOneMock() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Field injectMocksField = mock(Field.class);
        Object owner = mock(Object.class);
        Object mock = mock(Object.class);
        Set<Object> mockCandidates = newHashSet(mock);

        when(awaitingInjectionClazz.getDeclaredFields())
                .thenReturn(new ArrayList<>(Arrays.asList(injectMocksField)));

        when(injectMocksField.getType()).thenReturn(mock.getClass());
        when(injectMocksField.get(owner)).thenReturn(null);

        Mockito.doAnswer(i -> {
            injectMocksField.set(owner, mock);
            return null;
        }).when(injectMocksField).set(anyObject());

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, owner, mockCandidates));
        verify(injectMocksField).set(owner, mock);
    }

    @Test
    public void testProcessInjection_MockCandidates_FieldsWithMultipleMocks() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Field injectMocksField = mock(Field.class);
        Object owner = mock(Object.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Set<Object> mockCandidates = newHashSet(mock1, mock2);

        when(awaitingInjectionClazz.getDeclaredFields())
                .thenReturn(new ArrayList<>(Arrays.asList(injectMocksField)));

        when(injectMocksField.getType()).thenReturn(mock1.getClass());
        when(injectMocksField.getName()).thenReturn("mock1Field");

        when(mock1.getClass()).thenReturn(mock2.getClass());
        when(mock1.getName()).thenReturn("mock1");
        when(mock2.getName()).thenReturn("mock2");

        when(injectMocksField.get(owner)).thenReturn(null);

        Mockito.doAnswer(i -> {
            injectMocksField.set(owner, mock1);
            return null;
        }).when(injectMocksField).set(anyObject());

        propertyAndSetterInjection.processInjection(injectMocksField, owner, mockCandidates);
        verify(injectMocksField).set(owner, mock1);
        assertEquals(mock2, mockCandidates.iterator().next());
    }

    @Test
    public void testProcessInjection_MockCandidates_FieldsWithNoMock() {
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Object instance = mock(Object.class);
        Field injectMocksField = mock(Field.class);
        Object owner = mock(Object.class);
        Set<Object> mockCandidates = newHashSet();

        when(awaitingInjectionClazz.getDeclaredFields())
                .thenReturn(new ArrayList<>(Arrays.asList(injectMocksField)));

        when(injectMocksField.getType()).thenReturn(Integer.class);
        when(injectMocksField.getName()).thenReturn("intField");
        when(injectMocksField.get(owner)).thenReturn(null);

        Mockito.doAnswer(i -> {
            injectMocksField.set(owner, 42);
            return null;
        }).when(injectMocksField).set(anyObject());

        propertyAndSetterInjection.processInjection(injectMocksField, owner, mockCandidates);
        verify(injectMocksField).set(owner, 42);
        assertNull(mockCandidates.iterator().next());
    }
}