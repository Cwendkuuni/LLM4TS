package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter nextMock;
    private TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidateWithNullArguments() {
        Collection<Object> mocks = null;
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithEmptyMockCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithMockCollectionContainingTypesThatMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        field.getType().setRawType(Integer.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        MockCandidateFilter nextSpy = spy(nextMock);
        List<Object> nextMockMatches = new ArrayList<>();
        nextSpy.filterCandidate(Mockito.isA(List.class), Mockito.any(Field.class), Mockito.any(Object.class))
                .thenReturn(Mockito.argThat(arg -> nextMockMatches.addAll((Collection<?>) arg)));

        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(1, nextMockMatches.size());
        assertEquals(Integer.class, nextMockMatches.get(0).getClass());
    }

    @Test
    public void testFilterCandidateWithMockCollectionContainingTypesThatDontMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        field.getType().setRawType(Float.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        MockCandidateFilter nextSpy = spy(nextMock);
        List<Object> nextMockMatches = new ArrayList<>();
        nextSpy.filterCandidate(Mockito.isA(List.class), Mockito.any(Field.class), Mockito.any(Object.class))
                .thenReturn(result);

        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(0, nextMockMatches.size());
    }
}