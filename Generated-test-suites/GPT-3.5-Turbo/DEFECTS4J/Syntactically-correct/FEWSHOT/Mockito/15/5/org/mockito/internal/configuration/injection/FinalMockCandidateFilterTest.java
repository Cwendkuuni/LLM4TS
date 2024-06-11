package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Collection;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field();
        Object fieldInstance = new Object();
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(injecter);
        
        mocks.add(new Object());
        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidateMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field();
        Object fieldInstance = new Object();
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(injecter);
        
        mocks.add(new Object());
        mocks.add(new Object());
        injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
    }
}