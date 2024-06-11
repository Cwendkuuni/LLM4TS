package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyMockTypeMatchesList() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> mockTypeMatches = new ArrayList<>();

        filter = spy(filter);
        when(filter.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter, times(1)).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMockTypeMatch() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock(Integer.class));

        filter = spy(filter);
        when(filter.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter, times(1)).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMockTypeMatches() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock(String.class));
        mockTypeMatches.add(mock(Integer.class));

        OngoingInjecter stringMock = mock(OngoingInjecter.class);
        OngoingInjecter integerMock = mock(OngoingInjecter.class);

        filter = spy(filter);
        when(filter.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(stringMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(filter, times(1)).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
        assertEquals(stringMock, result);
    }

    @Test
    public void testFilterCandidate_NextFilter() {
        TypeBasedCandidateFilter filter1 = new TypeBasedCandidateFilter(null);
        TypeBasedCandidateFilter filter2 = mock(TypeBasedCandidateFilter.class);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock(Integer.class));

        filter1 = spy(filter1);
        when(filter1.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(null);

        TypeBasedCandidateFilter filterChain = new TypeBasedCandidateFilter(filter1);
        filterChain = spy(filterChain);

        OngoingInjecter result = filterChain.filterCandidate(mocks, field, fieldInstance);

        verify(filter1, times(1)).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
        assertNull(result);
    }
}