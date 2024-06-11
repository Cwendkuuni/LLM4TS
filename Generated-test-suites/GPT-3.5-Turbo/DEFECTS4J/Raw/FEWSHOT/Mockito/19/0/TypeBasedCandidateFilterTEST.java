package org.mockito.internal.configuration.injection.filter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
        mocks = new ArrayList<Object>();
        field = null;
        fieldInstance = null;
    }

    @Test
    public void testFilterCandidate_whenMocksEmpty_shouldReturnOngoingInjecterFromNextFilter() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertSame(result, nextFilter.filterCandidate(anyListOf(Object.class), same(field), same(fieldInstance)));
    }

    @Test
    public void testFilterCandidate_whenNoMatchingMockType_shouldReturnOngoingInjecterFromNextFilter() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertSame(result, nextFilter.filterCandidate(anyListOf(Object.class), same(field), same(fieldInstance)));
    }

    @Test
    public void testFilterCandidate_whenMatchingMockType_shouldReturnOngoingInjecterFromNextFilter() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object mock3 = mock(Float.class);
        Object mock4 = mock(Double.class);
        mocks.addAll(Arrays.asList(mock1, mock2, mock3, mock4));

        when(field.getType()).thenReturn(Number.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertSame(result, nextFilter.filterCandidate(anyListOf(Object.class), same(field), same(fieldInstance)));
    }

    @Test
    public void testFilterCandidate_whenMatchingMockType_shouldOnlyIncludeMatchingMocks() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object mock3 = mock(Float.class);
        Object mock4 = mock(Double.class);
        mocks.addAll(Arrays.asList(mock1, mock2, mock3, mock4));

        when(field.getType()).thenReturn(Number.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(eq(Arrays.asList(mock2, mock3)), same(field), same(fieldInstance));
    }
}