package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullCollection() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(null, Mockito.mock(Field.class), Mockito.mock(Object.class)));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        Collection<Object> mocks = mock(Collection.class);
        assertNull(filter.filterCandidate(mocks, Mockito.mock(Field.class), null));
    }

    @Test
    public void testFilterCandidate_NullFieldToBeInjected() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        Collection<Object> mocks = mock(Collection.class);
        Object fieldInstance = mock(Object.class);
        assertNull(filter.filterCandidate(null, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
       MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        Collection<Object> mocks = mock(Collection.class);
        Mockito.when(mocks.isEmpty()).thenReturn(true);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyListWithNullFieldToBeInjected() {
        MockCandidateFilter filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        Collection<Object> mocks = mock(Collection.class);
        Mockito.when(mocks.isEmpty()).thenReturn(true);
        assertNull(filter.filterCandidate(mocks, null, mock(Object.class)));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        Mockito.verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_FilterReturnsSameInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object expected = mock(Object.class);
        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(expected);
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(expected, result);
    }

}