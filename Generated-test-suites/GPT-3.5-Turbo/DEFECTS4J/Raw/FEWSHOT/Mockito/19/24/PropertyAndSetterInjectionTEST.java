package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyAndSetterInjectionTest {
    
    @Test
    public void testProcessInjection() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        // Test with mocks
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        doReturn(mock(Object.class)).when(mockCandidateFilter).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
        injection.mockCandidateFilter = mockCandidateFilter;

        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = mock(Object.class);
        Set<Object> mockCandidates = new HashSet<>();
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        assertTrue(result);
        verify(mockCandidateFilter, times(2)).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
        
        // Test without mocks
        mockCandidateFilter = mock(MockCandidateFilter.class);
        injection.mockCandidateFilter = mockCandidateFilter;

        injectMocksField = mock(Field.class);
        injectMocksFieldOwner = mock(Object.class);
        mockCandidates = new HashSet<>();
        result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        assertFalse(result);
        verify(mockCandidateFilter, times(2)).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
    }

    @Test
    public void testInitializeInjectMocksField() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Field field = mock(Field.class);
        Object fieldOwner = mock(Object.class);
        FieldInitializationReport report = mock(FieldInitializationReport.class);
        try {
            whenNew(FieldInitializer.class).withArguments(fieldOwner, field).thenReturn(mock(FieldInitializer.class));
            when(report.fieldClass()).thenReturn(Object.class);
            when(report.fieldInstance()).thenReturn(mock(Object.class));
            PowerMockito.whenNew(FieldInitializationReport.class).withNoArguments().thenReturn(report);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FieldInitializationReport result = injection.initializeInjectMocksField(field, fieldOwner);
        assertNotNull(result);
        assertEquals(Object.class, result.fieldClass());
        verifyNew(FieldInitializer.class).withArguments(fieldOwner, field);
        verify(report).fieldClass();
        verify(report).fieldInstance();
    }

    @Test
    public void testInjectMockCandidates() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Set<Object> mocks = new HashSet<>();
        Object instance = mock(Object.class);
        Class<?> awaitingInjectionClazz = Object.class;
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        orderedInstanceFields.add(field1);
        orderedInstanceFields.add(field2);

        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        doReturn(mock(Object.class)).when(mockCandidateFilter).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
        injection.mockCandidateFilter = mockCandidateFilter;

        boolean result = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        assertTrue(result);
        verify(mockCandidateFilter, times(2)).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
        
        orderedInstanceFields.clear();
        orderedInstanceFields.add(field2);
        orderedInstanceFields.add(field1);
        
        result = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
        assertTrue(result);
        verify(mockCandidateFilter, times(4)).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
    }

    @Test
    public void testInjectMockCandidatesOnFields() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Set<Object> mocks = new HashSet<>();
        Object instance = mock(Object.class);
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        orderedInstanceFields.add(field1);
        orderedInstanceFields.add(field2);

        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        doReturn(mock(Object.class)).when(mockCandidateFilter).filterCandidate(mocks, field1, instance);
        doReturn(null).when(mockCandidateFilter).filterCandidate(mocks, field2, instance);
        injection.mockCandidateFilter = mockCandidateFilter;

        boolean result = injection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        assertTrue(result);
        verify(mockCandidateFilter).filterCandidate(mocks, field1, instance);
        verify(mockCandidateFilter).filterCandidate(mocks, field2, instance);
        
        result = injection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        assertTrue(result);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, field2, instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOrderedInstanceFieldsFrom() {
        PropertyAndSetterInjection injection = new PropertyAndSetterInjection();

        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;

        SuperTypesLastSorter sorter = mock(SuperTypesLastSorter.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        List<Field> declaredFields = Arrays.asList(field1, field2);
        ListUtil.Filter<Field> notFinalOrStatic = mock(ListUtil.Filter.class);
        when(ListUtil.filter(declaredFields, notFinalOrStatic)).thenReturn(declaredFields);
        when(sorter.sort(declaredFields)).thenReturn(declaredFields);

        injection.notFinalOrStatic = notFinalOrStatic;
        injection.sorter = sorter;

        List<Field> result = injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        assertNotNull(result);
        assertEquals(declaredFields, result);
        verify(sorter).sort(declaredFields);
    }
}