package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_EmptyCollection() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_EmptyMockTypeMatches_NullNext() {
        MockCandidateFilter nextMock = null;
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_EmptyMockTypeMatches_NotNullNext() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        OngoingInjecter nextResult = nextMock.filterCandidate(Collections.emptyList(), field, fieldInstance);

        assertNotNull(result);
        assertNotNull(nextResult);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_NullNext() {
        List<Object> mockTypeMatches = new ArrayList<>();
        MockCandidateFilter nextMock = null;
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_NotNullNext() {
        List<Object> mockTypeMatches = new ArrayList<>();
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        OngoingInjecter nextResult = nextMock.filterCandidate(mockTypeMatches, field, fieldInstance);

        assertNotNull(result);
        assertNotNull(nextResult);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_SingleMock() {
        List<Object> mockTypeMatches = new ArrayList<>();
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        Object mock = mock(Object.class);
        mockTypeMatches.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        OngoingInjecter nextResult = nextMock.filterCandidate(mockTypeMatches, field, fieldInstance);

        assertNotNull(result);
        assertNotNull(nextResult);
        assertEquals(mock, result.getMock());
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_MultipleMocks() {
        List<Object> mockTypeMatches = new ArrayList<>();
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = spy(new Object());

        List<Object> mocksResult = new ArrayList<>();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        OngoingInjecter nextResult = nextMock.filterCandidate(mockTypeMatches, field, fieldInstance);

        assertNotNull(result);
        assertNotNull(nextResult);

        mocksResult.add(result.getMock());
        mocksResult.addAll(nextResult.getMockMatches());

        assertEquals(2, mocksResult.size());
    }
}