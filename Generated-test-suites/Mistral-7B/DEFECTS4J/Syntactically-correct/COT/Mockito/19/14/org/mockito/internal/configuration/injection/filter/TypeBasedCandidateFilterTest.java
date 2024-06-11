package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenMocksAreOfSameTypeAsField_thenReturnNextFilter() {
        // given
        Field field = Mockito.spy(new Field("testField", String.class, null, null));
        Object fieldInstance = mock(String.class);
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(String.class));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_whenNoMockMatchesFieldType_thenReturnEmptyList() {
        // given
        Field field = Mockito.spy(new Field("testField", Integer.class, null, null));
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));

        // when
        List<Object> result = filter.filterCandidate(mocks, field, null);

        // then
        assertEquals(0, result.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_whenFieldIsNull_thenThrowNullPointerException() {
        // given
        List<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = mock(String.class);

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        // exception should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_whenFieldInstanceIsNull_thenThrowNullPointerException() {
        // given
        Field field = Mockito.spy(new Field("testField", String.class, null, null));
        List<Object> mocks = new ArrayList<>();

        // when
        filter.filterCandidate(mocks, field, null);

        // then
        // exception should be thrown
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsNull_thenReturnEmptyList() {
        // given
        Field field = Mockito.spy(new Field("testField", String.class, null, null));
        Object fieldInstance = mock(String.class);
        List<Object> mocks = new ArrayList<>();

        Mockito.when(nextMock.filterCandidate(Mockito.anyList(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);

        // when
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertEquals(0, result.size());
    }
}