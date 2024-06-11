package org.mockito.internal.configuration.injection;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = null;
        Object fieldInstance = null;
        
        // Test when mocks size is 1
        mocks.add(mock(Object.class));
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
        
        // Test when mocks size is larger than 1
        mocks.add(mock(Object.class));
        OngoingInjecter injecter2 = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter2.thenInject());
    }
}