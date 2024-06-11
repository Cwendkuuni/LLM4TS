package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();

        assertNull(filter.filterCandidate(mocks, null, null));
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);

        Collection<Object> mocks = null;
        Field field = null;
        Object fieldInstance = null;

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithEmptyListOfMockMatches() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithOneMockMatch() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithMultipleMockMatches() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object1.class));
        mocks.add(mock(Object2.class));
        mocks.add(mock(Object3.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object expectedMock = mock(Object1.class);
        assertSame(expectedMock, filter.filterCandidate(mocks, field, fieldInstance).getMock());
    }

    @Test
    public void testFilterCandidateWithNextFilter() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new StubFilter());
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object1.class));
        mocks.add(mock(Object2.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    private static class Object1 {
    }

    private static class Object2 {
    }

    private static class Object3 {
    }

    private static class StubFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return mock(OngoingInjecter.class);
        }
    }
}