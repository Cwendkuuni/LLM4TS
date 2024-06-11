package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_WithSingleMock() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        verify(fieldInstance, times(1)).set(mock(Object.class));
        assertEquals(mock(Object.class), result);
    }
    
    @Test
    public void testFilterCandidate_WithMultipleMocks() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        verify(fieldInstance, never()).set(any());
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_WithSetFails() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        when(fieldInstance.set(any())).thenReturn(false);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        verify(fieldInstance, times(1)).set(mock(Object.class));
        verify(new Reporter(), times(1)).cannotInjectDependency(field, mock(Object.class), null);
        assertEquals(mock(Object.class), result);
    }
    
    @Test
    public void testFilterCandidate_WithRuntimeException() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        when(fieldInstance.set(any())).thenThrow(new RuntimeException());
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        verify(fieldInstance, times(1)).set(mock(Object.class));
        verify(new Reporter(), times(1)).cannotInjectDependency(field, mock(Object.class), any());
        assertEquals(mock(Object.class), result);
    }
    
    @Test
    public void testFilterCandidate_WithNullMocks() {
	     Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        assertNull(result);
    }
    
    @Test
    public void testThenInject_WithNullMocks() {
	     Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.new OngoingInjecter();
        
        Object result = ongoingInjecter.thenInject();
        
        assertNull(result);
    }
    
    @Test
    public void testThenInject_WithNonNullMocks() {
	     Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.new OngoingInjecter();
        
        Object result = ongoingInjecter.thenInject();
        
        assertNull(result);
    }

}