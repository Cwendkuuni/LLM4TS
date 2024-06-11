package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        Class<String> stringType = String.class;
        Collection<Object> mocks = new ArrayList<>();

        // Test empty collection
        MockCandidateFilter filter1 = new TypeBasedCandidateFilter(null);
        assertNull(filter1.filterCandidate(mocks, null, null));

        // Test null field
        MockCandidateFilter filter2 = new TypeBasedCandidateFilter(null);
        assertNull(filter2.filterCandidate(mocks, null, null));

        // Test empty fieldMatches
        Object fieldInstance = mock(Object.class);
        Field field1 = mock(Field.class);
        MockCandidateFilter filter3 = new TypeBasedCandidateFilter(null);
        assertNull(filter3.filterCandidate(mocks, field1, fieldInstance));

        // Test fieldMatches contains one mock
        Object mock1 = mock(String.class);
        mocks.add(mock1);
        Field field2 = mock(Field.class);
        MockCandidateFilter filter4 = new TypeBasedCandidateFilter(null);
        OngoingInjecter result = filter4.filterCandidate(mocks, field2, fieldInstance);
        assertNotNull(result);

        // Test fieldMatches contains multiple mocks
        Object mock2 = mock(String.class);
        mocks.add(mock2);
        MockCandidateFilter filter5 = new TypeBasedCandidateFilter(null);
        result = filter5.filterCandidate(mocks, field2, fieldInstance);
        assertNotNull(result);
        assertNotNull(result.getMock());
        assertEquals(mock2, result.getMock());

        // Test next filter returns a result
        Object mock3 = mock(Object.class);
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(mocks, field2, fieldInstance)).thenReturn(mock3);
        TypeBasedCandidateFilter filter6 = new TypeBasedCandidateFilter(nextFilter);
        result = filter6.filterCandidate(mocks, field2, fieldInstance);
        assertNotNull(result);
        assertNotNull(result.getMock());
        assertEquals(mock3, result.getMock());
    }
}