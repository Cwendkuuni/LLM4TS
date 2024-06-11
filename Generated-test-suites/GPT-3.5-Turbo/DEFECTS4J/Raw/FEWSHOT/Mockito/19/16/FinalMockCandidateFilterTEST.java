/*
 * Test cases for the FinalMockCandidateFilter class
 */

package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithSingleMock() {
        // Create a mock collection with a single mock object
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        // Create a field and fieldInstance
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        // Test the filterCandidate method
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify that the filtered injecter object is correct
        assertEquals(mock, injecter.thenInject());
        verify(field, times(1)).setAccessible(true);
        verifyNoMoreInteractions(field);
    }
    
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Create a mock collection with multiple mock objects
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        // Create a field and fieldInstance
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        // Test the filterCandidate method
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify that the filtered injecter object is null
        assertNull(injecter.thenInject());
        verifyZeroInteractions(field);
    }
    
    @Test
    public void testFilterCandidateWithRuntimeException() {
        // Create a mock collection with a single mock object
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        // Create a field and fieldInstance
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        // Throw a RuntimeException when setting the field
        doThrow(new RuntimeException()).when(field).setAccessible(true);
        
        // Test the filterCandidate method
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify that the filtered injecter object is correct and the exception is reported
        assertEquals(mock, injecter.thenInject());
        verify(field, times(1)).setAccessible(true);
        verifyNew(Reporter.class).cannotInjectDependency(field, mock, any(RuntimeException.class));
        verifyNoMoreInteractions(field);
    }
}