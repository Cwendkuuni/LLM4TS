package org.mockito.internal.configuration.injection;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {
    
    @Test
    public void testProcessInjection_shouldInjectMockCandidates() throws NoSuchFieldException, IllegalAccessException {
        // Setup
        PropertyAndSetterInjection injectionStrategy = new PropertyAndSetterInjection();
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object injectMocksFieldOwner = injectionStrategy;
        Set<Object> mockCandidates = new HashSet<>();
        
        // Mocks
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        
        // Stubbing
        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class))).thenReturn(null);
        
        // Test
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Verify
        assertFalse(result);
        verify(mockCandidateFilter, times(2)).filterCandidate(anySet(), any(Field.class), any(Object.class));
    }
    
    @Test
    public void testProcessInjection_shouldInitializeInjectMocksField() throws NoSuchFieldException, IllegalAccessException {
        // Setup
        PropertyAndSetterInjection injectionStrategy = new PropertyAndSetterInjection();
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter");
        Object injectMocksFieldOwner = injectionStrategy;
        Set<Object> mockCandidates = new HashSet<>();
        
        // Mocks
        FieldInitializationReport reportMock = mock(FieldInitializationReport.class);
        FieldInitializer fieldInitializerMock = mock(FieldInitializer.class);
        Reporter reporterMock = mock(Reporter.class);
        
        // Stubbing
        when(reportMock.fieldClass()).thenReturn(MockitoException.class);
        when(reportMock.fieldInstance()).thenReturn(fieldInitializerMock);
        whenNew(FieldInitializer.class).withArguments(any(), any()).thenReturn(fieldInitializerMock);
        whenNew(Reporter.class).withNoArguments().thenReturn(reporterMock);
        
        // Test
        boolean result = injectionStrategy.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
        
        // Verify
        assertTrue(result);
        verifyNew(FieldInitializer.class).withArguments(eq(injectMocksFieldOwner), eq(injectMocksField));
        verify(reportMock).fieldClass();
        verify(reportMock).fieldInstance();
        verify(reporterMock).fieldInitialisationThrewException(eq(injectMocksField), any(Throwable.class));
    }
    
    @Test
    public void testInjectMockCandidates_shouldInjectMocksByType() throws NoSuchFieldException, IllegalAccessException {
        // Setup
        PropertyAndSetterInjection injectionStrategy = new PropertyAndSetterInjection();
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        
        // Mocks
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        
        // Stubbing
        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class))).thenReturn(instance);
        
        // Test
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
        
        // Verify
        assertTrue(result);
        verify(mockCandidateFilter).filterCandidate(eq(mocks), any(Field.class), eq(instance));
    }
    
    @Test
    public void testInjectMockCandidates_shouldNotInjectMocksIfNoCandidate() throws NoSuchFieldException, IllegalAccessException {
        // Setup
        PropertyAndSetterInjection injectionStrategy = new PropertyAndSetterInjection();
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();
        
        // Mocks
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        
        // Stubbing
        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), any(Object.class))).thenReturn(null);
        
        // Test
        boolean result = injectionStrategy.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);
        
        // Verify
        assertFalse(result);
        verify(mockCandidateFilter).filterCandidate(eq(mocks), any(Field.class), eq(instance));
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom_shouldReturnOrderedFields() {
        // Setup
        PropertyAndSetterInjection injectionStrategy = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = PropertyAndSetterInjection.class;
        
        // Test
        List<Field> fields = injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        // Verify
        assertNotNull(fields);
        assertFalse(fields.isEmpty());
        assertEquals(5, fields.size());
    }
}