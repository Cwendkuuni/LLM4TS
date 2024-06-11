
package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.Test;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(mocks.iterator().next(), injecter.thenInject());
        verify(field, times(1)).set(matchingMock);
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(2);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(injecter.thenInject());
        verify(field, never()).set(any());
    }
}
