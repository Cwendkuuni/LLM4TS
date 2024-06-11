package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;


public class TypeBasedCandidateFilterTest {
    @Test
    public void testFilterCandidate_noMatchingMocks() throws Exception {
        // Setup
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter candidateFilter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = getClass().getDeclaredField("field");
        Object fieldInstance = new Object();
        when(field.getType().isAssignableFrom(Object.class)).thenReturn(false);

        // Test
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(nextFilter).filterCandidate(anyList(), any(Field.class), anyObject());
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_singleMatchingMocks() throws Exception {
        // Setup
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter candidateFilter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = getClass().getDeclaredField("field");
        Object fieldInstance = new Object();
        Object mock = new Object();
        mocks.add(mock);
        when(field.getType().isAssignableFrom(Object.class)).thenReturn(true);

        // Test
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(nextFilter).filterCandidate(anyList(), any(Field.class), anyObject());
        assertEquals(Collections.singletonList(mock), result.getMocks());
    }
    
    @Test
    public void testFilterCandidate_multipleMatchingMocks() throws Exception {
        // Setup
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter candidateFilter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = getClass().getDeclaredField("field");
        Object fieldInstance = new Object();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getType().isAssignableFrom(Object.class)).thenReturn(true);

        // Test
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(nextFilter).filterCandidate(anyList(), any(Field.class), anyObject());
        assertEquals(Arrays.asList(mock1, mock2), result.getMocks());
    }
    
    @Test
    public void testFilterCandidate_noNextFilter() throws Exception {
        // Setup
        TypeBasedCandidateFilter candidateFilter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = getClass().getDeclaredField("field");
        Object fieldInstance = new Object();
        Object mock = new Object();
        mocks.add(mock);
        when(field.getType().isAssignableFrom(Object.class)).thenReturn(true);

        // Test
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        assertNull(result);
    }
}