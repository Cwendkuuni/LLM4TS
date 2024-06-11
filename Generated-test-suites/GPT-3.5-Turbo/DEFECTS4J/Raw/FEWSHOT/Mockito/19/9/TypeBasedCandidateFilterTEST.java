package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Create mocks and field instance
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = new Field();
        Object fieldInstance = new Object();

        // Create TypeBasedCandidateFilter
        MockCandidateFilter next = new MockCandidateFilter();
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        // Test case 1: No mock matches the field type
        mocks.add("mock1");
        mocks.add("mock2");
        OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result1);
        assertEquals(next, result1);

        // Test case 2: One mock matches the field type
        mocks.clear();
        mocks.add(new MockObject());
        OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result2);
        assertEquals(next, result2);
        assertEquals(1, result2.mockTypeMatches.size());
        assertEquals(new MockObject(), result2.mockTypeMatches.get(0));

        // Test case 3: Multiple mocks match the field type
        mocks.clear();
        mocks.add(new MockObject());
        mocks.add(new MockObject());
        mocks.add(new MockObject());
        OngoingInjecter result3 = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result3);
        assertEquals(next, result3);
        assertEquals(3, result3.mockTypeMatches.size());
        assertEquals(new MockObject(), result3.mockTypeMatches.get(0));
        assertEquals(new MockObject(), result3.mockTypeMatches.get(1));
        assertEquals(new MockObject(), result3.mockTypeMatches.get(2));
    }
}