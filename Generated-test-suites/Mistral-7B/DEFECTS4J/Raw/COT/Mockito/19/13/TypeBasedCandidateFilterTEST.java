package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.collections.ArrayLists;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = ArrayLists.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks() {
        Collection<Object> mocks = ArrayLists.asList(new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMatchingMock() {
        Collection<Object> mocks = ArrayLists.asList(new MockType1(), new MockType2(), new MockType1());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result instanceof MockType1);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = ArrayLists.asList(new Object());
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = ArrayLists.asList(new Object());
        Field field = mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_NextFilterCalled() {
        Collection<Object> mocks = ArrayLists.asList(new MockType1(), new MockType2(), new MockType1());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(nextMock.filterCandidate(argThat(l -> l.size() > 0), field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result instanceof OngoingInjecter);
        verify(nextMock).filterCandidate(argThat(l -> l.size() > 0), field, fieldInstance);
    }

    private static class MockType1 {}
    private static class MockType2 {}
}