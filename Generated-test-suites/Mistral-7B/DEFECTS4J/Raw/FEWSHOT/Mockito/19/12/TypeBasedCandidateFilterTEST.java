package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.collections.Collections3.newArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null; // place holder
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterEmptyList() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null; // place holder
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(0, mockTypeMatches.size());
    }

    @Test
    public void testFilterSingleMock() {
        Collection<Object> mocks = newArrayList(new Object());
        Field field = null; // place holder
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(1, mockTypeMatches.size());
        assertEquals(mocks.get(0), mockTypeMatches.get(0));
    }

    @Test
    public void testFilterMultipleMocks() {
        Collection<Object> mocks = newArrayList(new Object(), new Object(), new Object());
        Field field = null; // place holder
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(2, mockTypeMatches.size());
        for (int i = 0; i < 2; i++) {
            Object mock = mocks.get(i);
            Object match = mockTypeMatches.get(i);
            assertEquals(mock, match);
        }
    }

    @Test
    public void testNextFilter() {
        Collection<Object> mocks = newArrayList(new Object(), new Object(), new Object());
        Field field = null; // place holder
        Object fieldInstance = new Object();

        TypeBasedCandidateFilter filter1 = new TypeBasedCandidateFilter(null);
        List<Object> mockTypeMatches = filter1.filterCandidate(mocks, field, fieldInstance);
        Object firstResult = mockTypeMatches.get(0);

        TypeBasedCandidateFilter filter2 = new TypeBasedCandidateFilter(new DummyMockCandidateFilter());
        List<Object> mockTypeMatches2 = filter2.filterCandidate(mocks, field, fieldInstance);

        assertEquals(1, mockTypeMatches.size());
        assertEquals(firstResult, mockTypeMatches.get(0));
        assertEquals(1, mockTypeMatches2.size());
        assertNotEquals(firstResult, mockTypeMatches2.get(0));
    }
}

class DummyMockCandidateFilter implements MockCandidateFilter {
    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        return new OngoingInjecter(new Object(), null);
    }
}