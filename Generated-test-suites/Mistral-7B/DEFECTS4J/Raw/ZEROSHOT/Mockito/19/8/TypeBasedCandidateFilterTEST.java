package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_emptyMockCollection() {
        Collection<Object> mocks = Collections3.emptyList();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // No mocks to filter
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(Collections.emptyList(), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_noTypeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // First mock is not of the required type
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_typeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object[].class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object[].class);

        // First mock is of the required type
        filter.filterCandidate(mocks, field, fieldInstance);

        // Next filter should be called with the type matching mocks only
        verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_nextReturnsNull() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object[].class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object[].class);

        when(nextMock.filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance)).thenReturn(null);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock, never()).filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_nextReturnsAnotherMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object[].class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object[].class);

        Object anotherMock = mock(Object.class);
        when(nextMock.filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance)).thenReturn(anotherMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Next filter was called and returned a mock
        verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
        verify(nextMock).filterCandidate(Collections.singletonList(anotherMock), field, fieldInstance);

        // This filter did not return null
        assertNotNull(result);
    }
}