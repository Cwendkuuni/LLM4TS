package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NameBasedCandidateFilterTest {

    @Test
    public void testEmptyCollection() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testSingleMockWithMatchingName() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock)).thenReturn("mockName");
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        verify(nextMock, never()).filterCandidate(mocks, eq(field), eq(fieldInstance));
    }

    @Test
    public void testSingleMockWithNonMatchingName() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock)).thenReturn("anotherMockName");
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testMultipleMocksWithMatchingNames() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mockName");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName");
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertNotNull(result.getMock());
        assertEquals(mock1, result.getMock());

        verify(nextMock, never()).filterCandidate(mocks, eq(field), eq(fieldInstance));
    }

    @Test
    public void testMultipleMocksWithDifferentNames() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mock1Name");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2Name");
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
        OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result1);
        assertNull(result1.getMock());

        assertNotNull(result2);
        assertNotNull(result2.getMock());
        assertEquals(mock2, result2.getMock());

        verify(nextMock, times(2)).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testMultipleMocksWithConflictingNames() {
        MockCandidateFilter nextMock = spy(NameBasedCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockUtil.getMockName(mock1)).thenReturn("mockName");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName");
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
        OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(mock2, result2.getMock());

        verify(nextMock, never()).filterCandidate(mocks, eq(field), eq(fieldInstance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollection() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullField() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFieldInstance() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }
}