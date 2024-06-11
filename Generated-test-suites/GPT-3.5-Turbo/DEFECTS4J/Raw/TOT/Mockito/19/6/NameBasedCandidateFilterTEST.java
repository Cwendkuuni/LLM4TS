package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter mockCandidateFilter;
    private MockCandidateFilter nextFilterMock;
    private MockUtil mockUtilMock;

    @Before
    public void setUp() {
        nextFilterMock = mock(MockCandidateFilter.class);
        mockUtilMock = mock(MockUtil.class);
        mockCandidateFilter = new NameBasedCandidateFilter(nextFilterMock, mockUtilMock);
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks_ReturnsFilteredMocks() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        verify(nextFilterMock).filterCandidate(mocks, fieldMock, fieldInstanceMock);
        assertEquals(result, nextFilterMock.filterCandidate(mocks, fieldMock, fieldInstanceMock));
    }

    @Test
    public void testFilterCandidate_SingleMatchingMock_ReturnsFilteredMocks() throws Exception {
        Object mock1 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        when(fieldMock.getName()).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        verify(nextFilterMock).filterCandidate(mocks, fieldMock, fieldInstanceMock);
        assertEquals(result, nextFilterMock.filterCandidate(mocks, fieldMock, fieldInstanceMock));
    }

    @Test
    public void testFilterCandidate_MultipleMatchingMocks_ReturnsFilteredMocks() throws Exception {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        when(fieldMock.getName()).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock2)).thenReturn("mock1");

        List<Object> expectedFilteredMocks = new ArrayList<>();
        expectedFilteredMocks.add(mock1);
        expectedFilteredMocks.add(mock2);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        verify(nextFilterMock).filterCandidate(expectedFilteredMocks, fieldMock, fieldInstanceMock);
        assertEquals(result, nextFilterMock.filterCandidate(expectedFilteredMocks, fieldMock, fieldInstanceMock));
    }

    @Test
    public void testFilterCandidate_SingleMatchingMockWithNoOtherMocks_ReturnsFilteredMocks() throws Exception {
        Object mock1 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        when(fieldMock.getName()).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        verify(nextFilterMock).filterCandidate(mocks, fieldMock, fieldInstanceMock);
        assertEquals(result, nextFilterMock.filterCandidate(mocks, fieldMock, fieldInstanceMock));
    }

    @Test
    public void testFilterCandidate_SingleMatchingMockWithConflictingMocks_ReturnsFilteredMocks() throws Exception {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field fieldMock = mock(Field.class);
        Object fieldInstanceMock = mock(Object.class);

        when(fieldMock.getName()).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock2)).thenReturn("mock2");

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldMock, fieldInstanceMock);

        verify(nextFilterMock).filterCandidate(mocks, fieldMock, fieldInstanceMock);
        assertEquals(result, nextFilterMock.filterCandidate(mocks, fieldMock, fieldInstanceMock));
    }
}