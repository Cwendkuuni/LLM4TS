package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_WithMatchingMockName_ReturnsFilteredMockList() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn("mock1");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(Arrays.asList(mocks.toArray()[0]), result.getMockList());
        verify(next).filterCandidate(eq(Arrays.asList(mocks.toArray()[0])), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_WithNoMatchingMockName_ReturnsNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn("mock3");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertEquals(next, result);
    }

    @Test
    public void testFilterCandidate_WithMultipleMatchingMockNames_ReturnsFilteredMockList() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn("mock2");

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(Arrays.asList(mocks.toArray()[0]), result.getMockList());
        verify(next).filterCandidate(eq(Arrays.asList(mocks.toArray()[0])), eq(field), eq(fieldInstance));
    }

    // Edge Cases and Error Scenarios

    @Test
    public void testFilterCandidate_WithEmptyMocks_ReturnsNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertEquals(next, result);
    }

    @Test
    public void testFilterCandidate_WithNullMockNames_ReturnsNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn(null);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertEquals(next, result);
    }

    @Test
    public void testFilterCandidate_WithExceptionThrown_ReturnsNextFilterResult() throws Exception {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenThrow(new RuntimeException());

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
        assertEquals(next, result);
    }
}