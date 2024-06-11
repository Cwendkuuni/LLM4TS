package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void filterCandidate_shouldReturnNullIfMocksEmpty() {
        // Arrange
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(new ArrayList<>(), field, fieldInstance);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void filterCandidate_shouldReturnNextFilterResultIfNoTypeMatches() {
        // Arrange
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        // Act
        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_shouldReturnNextFilterResultIfTypeMatches() {
        // Arrange
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        // Act
        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

}