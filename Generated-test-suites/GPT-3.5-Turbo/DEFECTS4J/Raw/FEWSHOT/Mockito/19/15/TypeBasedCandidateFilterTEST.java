package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Field field = MockClass.class.getField("field");
        Object fieldInstance = new Object();

        List<Object> mocks = new ArrayList<>();
        mocks.add(new MockClass());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(next, filter.next);
        assertEquals(TypeBasedCandidateFilter.OngoingInjecter.class, injecter.getClass());
    }

    @Test
    public void testFilterCandidateNoMatches() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilterImpl();

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Field field = MockClass.class.getField("field");
        Object fieldInstance = new Object();

        List<Object> mocks = new ArrayList<>();
        mocks.add(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(next, filter.next);
        assertNull(injecter);
    }

    private static class MockClass {
        public Object field;
    }

    private static class MockCandidateFilterImpl implements MockCandidateFilter {

        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    }
}