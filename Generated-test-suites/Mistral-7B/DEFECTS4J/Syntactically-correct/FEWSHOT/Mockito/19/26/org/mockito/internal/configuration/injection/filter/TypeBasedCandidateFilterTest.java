package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testEmptyFilter() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterWithNoMatches() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = mock(Field.class);
        field.getType().set(field, Integer.class);
        Object fieldInstance = mock(Object.class);

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterWithSingleMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        field.getType().set(field, Integer.class);
        Object fieldInstance = mock(Object.class);

        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assert(field.getType().isAssignableFrom(candidate.getClass()));
    }

    @Test
    public void testFilterWithMultipleMatches() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        field.getType().set(field, Integer.class);
        Object fieldInstance = mock(Object.class);

        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assert(field.getType().isAssignableFrom(candidate.getClass()));
    }

    @Test
    public void testFilterWithNext() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(String.class));
        Field field = mock(Field.class);
        field.getType().set(field, Integer.class);
        Object fieldInstance = mock(Object.class);

        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assert(field.getType().isAssignableFrom(candidate.getClass()));

        assertNotNull(next.filterCandidate(mocks, field, fieldInstance));
    }
}