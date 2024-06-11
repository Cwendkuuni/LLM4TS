package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(injecter.thenInject());
        
        verify(fieldInstance).set(eq(mocks.iterator().next()));        
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertFalse(injecter.thenInject());
        
        verifyZeroInteractions(fieldInstance);
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() throws Exception {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        
        doThrow(new Exception()).when(fieldInstance).set(any());
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        injecter.thenInject();
    }
}