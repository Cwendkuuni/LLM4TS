package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class TypeBasedCandidateFilterTest {
    private MockCandidateFilter next;

    @Test
    public void testConstructor() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        assertNotNull(filter);
        assertEquals(next, filter.next);
    }
    
    @Test
    public void testFilterCandidate() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        // Test when no mocks match the field type
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);
        assertTrue(mocks.isEmpty());
        assertFalse(mockTypeMatches.contains(mock));
        
        // Test when some mocks match the field type
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);
        assertFalse(mocks.isEmpty());
        assertTrue(mockTypeMatchess.contains(mock));
    }
}