package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ExtendedList;
import org.mockito.internal.util.reflection.FieldUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter nextMock;
    private TypeBasedCandidateFilter filterUnderTest;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filterUnderTest = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void shouldFilterTypeMatchedMocks() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object[].class), mock(Object.class));

        // When
        Object result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        List<Object> expectedFilters = Arrays.asList(mock(Object.class), mock(Object.class));
        Collection<Object> resultFilters = ((ExtendedList<Object>) result).getList();
        assertEquals(expectedFilters, resultFilters);
        verifyFilterNextCalled(mocks, field, fieldInstance);
    }

    @Test
    public void shouldNotFilterNonTypeMatchedMocks() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock(String.class), mock(Object.class), mock(Object.class));

        // When
        Object result = filterUnderTest.filterCandidate(mocks, field, fieldInstance);

        // Then
        Collection<Object> expectedFilters = Arrays.asList(mock(Object.class));
        Collection<Object> resultFilters = ((ExtendedList<Object>) result).getList();
        assertEquals(expectedFilters, resultFilters);
        verifyFilterNextNotCalled();
    }

    private void verifyFilterNextCalled(Collection<Object> mocks, Field field, Object fieldInstance) {
        verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    private void verifyFilterNextNotCalled() {
        verifyNoMoreInteractions(nextMock);
    }

    private void verify(MockCandidateFilter mockCandidateFilter) {
        Mockito.verify(mockCandidateFilter, Mockito.atLeastOnce());
    }
}