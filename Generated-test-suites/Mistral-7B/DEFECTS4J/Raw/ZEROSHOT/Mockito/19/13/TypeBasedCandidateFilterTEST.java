package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableToMockType_thenReturnNextFilter() {
        // given
        Field field = new Field("testField", String.class, null);
        Object fieldInstance = mock(String.class);
        List<Object> mocks = new ExtendedArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableToMockType_thenReturnEmptyList() {
        // given
        Field field = new Field("testField", String.class, null);
        Object fieldInstance = mock(Integer.class);
        List<Object> mocks = new ExtendedArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));

        // when
        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, fieldInstance).getCandidates();

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableToMultipleMockTypes_thenReturnNextFilterWithTypeMatches() {
        // given
        Field field = new Field("testField", List.class, null);
        Object fieldInstance = mock(List.class);
        List<Object> mocks = new ExtendedArrayList<>();
        mocks.add(mock(ArrayList.class));
        mocks.add(mock(LinkedList.class));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        MockCandidateFilter nextWithTypeMatches = mock(MockCandidateFilter.class);
        when(nextMock.filterCandidate(ArgumentMatchers.anyList(), ArgumentMatchers.eq(field), ArgumentMatchers.eq(fieldInstance)))
                .thenReturn(nextWithTypeMatches);
        assertEquals(nextWithTypeMatches, result.getFilter());
    }
}