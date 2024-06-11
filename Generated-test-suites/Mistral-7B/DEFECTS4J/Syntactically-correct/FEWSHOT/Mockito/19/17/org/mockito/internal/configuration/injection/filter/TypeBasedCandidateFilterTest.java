package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedArrayList;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyMockTypeMatches() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = null;
        Object fieldInstance = null;

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullNextFilter() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = null;
        Object fieldInstance = null;

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_SingleMockTypeMatch() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Foo(), new Bar());
        Field field = new Field() {{
            type = Foo.class;
        }};
        Object fieldInstance = new Object();

        assertEquals(new Foo(), filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMockTypeMatches() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Foo(), new Foo(), new Bar());
        Field field = new Field() {{
            type = Foo.class;
        }};
        Object fieldInstance = new Object();

        assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsNull() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Foo(), new Bar());
        Field field = new Field() {{
            type = Foo.class;
        }};
        Object fieldInstance = new Object();

        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        filter = new TypeBasedCandidateFilter(nextFilter);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsMock() {
        MockCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Foo(), new Bar());
        Field field = new Field() {{
            type = Foo.class;
        }};
        Object fieldInstance = new Object();

        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(new FooMock());

        filter = new TypeBasedCandidateFilter(nextFilter);

        assertEquals(new FooMock(), filter.filterCandidate(mocks, field, fieldInstance));
    }

    @SuppressWarnings("unchecked")
    static class Foo extends Object {}
    static class FooMock extends Foo {}
    static class Bar extends Object {}
}