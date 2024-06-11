package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
        
        verifyZeroInteractions(mock);
    }
    
    @Test
    public void testFilterCandidate_withMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
        
        verifyZeroInteractions(mock1);
        verifyZeroInteractions(mock2);
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = null;
        
        doThrow(new Exception()).when(mock).someMethod();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
        
        verify(mock).someMethod();
    }
}