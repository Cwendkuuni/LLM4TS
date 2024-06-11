package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = new MockCandidateFilterImpl();
        
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = null;
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertNotNull(ongoingInjecter);
    }
    
    @Test
    public void testFilterCandidate_withMocks() {
        MockCandidateFilter filter = new MockCandidateFilterImpl();
        
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        
        Field fieldToBeInjected = null;
        Object fieldInstance = null;
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertNotNull(ongoingInjecter);
        assertNotNull(ongoingInjecter.skipInjecting(null));
    }
    
    @Test
    public void testFilterCandidate_withMocksAndField() throws NoSuchFieldException {
        MockCandidateFilter filter = new MockCandidateFilterImpl();
        
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        
        Field fieldToBeInjected = MockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = null;
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertNotNull(ongoingInjecter);
        assertNotNull(ongoingInjecter.skipInjecting(null));
    }
    
    private class MockCandidateFilterImpl implements MockCandidateFilter {

        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return Mockito.mock(OngoingInjecter.class);
        }
        
    }

}