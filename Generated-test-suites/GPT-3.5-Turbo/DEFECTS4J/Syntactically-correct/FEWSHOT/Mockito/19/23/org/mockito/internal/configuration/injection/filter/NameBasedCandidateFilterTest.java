package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMockAndMatchingName_returnMock() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock = mock(Object.class);
        String mockName = field.getName();
        when(mockUtil.getMockName(mock)).thenReturn(mockName);

        mocks.add(mock);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(any(), any(), any());
        verify(next, never()).filterCandidate(mockNameMatches, field, fieldInstance);
        assertEquals(mockNameMatches, result);
    }

    @Test
    public void testFilterCandidate_multipleMocksAndMatchingName_returnMatchingMocks() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        String mock1Name = "mock1";
        String mock2Name = "mock2";
        String mock3Name = field.getName();
        when(mockUtil.getMockName(mock1)).thenReturn(mock1Name);
        when(mockUtil.getMockName(mock2)).thenReturn(mock2Name);
        when(mockUtil.getMockName(mock3)).thenReturn(mock3Name);

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(any(), any(), any());
        verify(next, times(1)).filterCandidate(mockNameMatches, field, fieldInstance);
        assertTrue(mockNameMatches.contains(mock3));
        assertEquals(mockNameMatches, result);
    }

    @Test
    public void testFilterCandidate_multipleMocksAndNoMatchingName_returnAllMocks() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        String mock1Name = "mock1";
        String mock2Name = "mock2";
        String mock3Name = "mock3";
        when(mockUtil.getMockName(mock1)).thenReturn(mock1Name);
        when(mockUtil.getMockName(mock2)).thenReturn(mock2Name);
        when(mockUtil.getMockName(mock3)).thenReturn(mock3Name);

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(any(), any(), any());
        verify(next, never()).filterCandidate(mockNameMatches, field, fieldInstance);
        assertEquals(mocks, result);
    }

    @Test
    public void testFilterCandidate_multipleMocksAndNoMatchingName_returnMatchingMocks() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        String mock1Name = "mock1";
        String mock2Name = "mock2";
        String mock3Name = field.getName();
        when(mockUtil.getMockName(mock1)).thenReturn(mock1Name);
        when(mockUtil.getMockName(mock2)).thenReturn(mock2Name);
        when(mockUtil.getMockName(mock3)).thenReturn(mock3Name);

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(any(), any(), any());
        verify(next, times(1)).filterCandidate(mockNameMatches, field, fieldInstance);
        assertTrue(mockNameMatches.contains(mock3));
        assertEquals(mockNameMatches, result);
    }

    @Test
    public void testFilterCandidate_noMocks_returnNull() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(any(), any(), any());
        verify(next, never()).filterCandidate(mockNameMatches, field, fieldInstance);
        assertNull(result);
    }
}