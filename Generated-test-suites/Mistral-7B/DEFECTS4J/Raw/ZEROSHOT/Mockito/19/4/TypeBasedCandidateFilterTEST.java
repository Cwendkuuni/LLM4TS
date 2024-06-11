package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedLists;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMockType_thenMockIsAddedToFilteredList() {
        // given
        Collection<Object> mocks = ExtendedLists.newArrayList();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(Integer.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        List<Object> filteredMocks = (List<Object>) Mockito.arga(nextMock.filterCandidate(Mockito.isA(List.class), Mockito.eq(field), Mockito.eq(fieldInstance)), 0);
        List<Object> expectedFilteredMocks = ExtendedLists.newArrayList(Mockito.mock(String.class));
        assertThat(filteredMocks, is(instanceOf(List.class)));
        assertThat(filteredMocks, is(sameInstance(mockTypeMatches(mocks, field))));
        assertThat(filteredMocks, is(sameInstance(expectedFilteredMocks)));
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMockType_thenMockIsNotAddedToFilteredList() {
        // given
        Collection<Object> mocks = ExtendedLists.newArrayList();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(Integer.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        List<Object> filteredMocks = (List<Object>) Mockito.arga(nextMock.filterCandidate(Mockito.isA(List.class), Mockito.eq(field), Mockito.eq(fieldInstance)), 0);
        List<Object> expectedFilteredMocks = ExtendedLists.newArrayList();
        assertThat(filteredMocks, is(instanceOf(List.class)));
        assertThat(filteredMocks, is(sameInstance(expectedFilteredMocks)));
    }

    private List<Object> mockTypeMatches(Collection<Object> mocks, Field field) {
        List<Object> mockTypeMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                mockTypeMatches.add(mock);
            }
        }
        return mockTypeMatches;
    }
}