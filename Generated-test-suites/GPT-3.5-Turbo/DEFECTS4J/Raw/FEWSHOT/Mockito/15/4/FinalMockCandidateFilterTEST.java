package org.mockito.internal.configuration.injection;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(mock(Object.class));
        
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(true, injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        when(mocks.size()).thenReturn(2);
        
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(false, injecter.thenInject());
    }

}