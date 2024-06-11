package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import org.junit.Test;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_whenSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mock = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result.thenInject());
    }
    
    @Test
    public void testFilterCandidate_whenMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(result.thenInject());
    }
}