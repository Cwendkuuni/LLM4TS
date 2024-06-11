package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injector.OngoingStubbing;
import org.mockito.internal.configuration.injector.OngoingStubbingImpl;
import org.mockito.internal.configuration.injector.OngoingStubbingType;
import org.mockito.internal.configuration.injector.TypeBasedMockFinder;
import org.mockito.internal.util.reflection.Whitebox;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_NoMatchingMocks_NullReturned() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = Whitebox.getField(Mockito.class, "TYPE_BASED_MOCK_FINDER");
        Object fieldInstance = Whitebox.getInternalState(Mockito.MockitoSession.class, "session");

        Collection<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMockMatch_PassedToNextFilter() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new NoOpMockCandidateFilter());
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        Field field = Whitebox.getField(Mockito.class, "TYPE_BASED_MOCK_FINDER");
        Object fieldInstance = Whitebox.getInternalState(Mockito.MockitoSession.class, "session");

        Collection<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mocks.get(0), result.iterator().next());
    }

    @Test
    public void testFilterCandidate_MultipleMockMatches_PassedToNextFilter() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new NoOpMockCandidateFilter());
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(StringBuilder.class));
        Field field = Whitebox.getField(Mockito.class, "TYPE_BASED_MOCK_FINDER");
        Object fieldInstance = Whitebox.getInternalState(Mockito.MockitoSession.class, "session");

        Collection<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(mocks.containsAll(result));
    }

    private static class NoOpMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsResult() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new ReturnFirstMockCandidateFilter());
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(StringBuilder.class));
        Field field = Whitebox.getField(Mockito.class, "TYPE_BASED_MOCK_FINDER");
        Object fieldInstance = Whitebox.getInternalState(Mockito.MockitoSession.class, "session");

        OngoingStubbing expectedResult = Mockito.mock(OngoingStubbing.class);
        OngoingStubbing result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(expectedResult, result);
    }

    private static class ReturnFirstMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return new OngoingStubbingImpl(Mockito.mock(OngoingStubbingType.class), null);
        }
    }
}