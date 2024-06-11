package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.mock.MockType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenAllMocksMatchType_ThenReturnNextFilter() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Field.class.getSuperclass()));
        mocks.add(Mockito.mock(Field.class));

        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_WhenNoMocksMatchType_ThenReturnEmptyCollection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));

        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, fieldInstance).getCandidates();

        // Then
        assertEquals(0, result.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenFieldIsNull_ThenThrowException() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        // When & Then
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenFieldInstanceIsNull_ThenThrowException() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = null;

        // When & Then
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_WhenMockCollectionIsEmpty_ThenThrowException() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When & Then
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenNextFilterIsNull_ThenReturnEmptyCollection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Field.class.getSuperclass()));
        mocks.add(Mockito.mock(Field.class));

        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        Mockito.doReturn(null).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));

        // When
        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, fieldInstance).getCandidates();

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsEmptyCollection_ThenReturnEmptyCollection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Field.class.getSuperclass()));
        mocks.add(Mockito.mock(Field.class));

        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        Mockito.doReturn(new ArrayList<>()).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));

        // When
        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, fieldInstance).getCandidates();

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsSingleMatchingMock_ThenReturnThatMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Field.class.getSuperclass()));
        mocks.add(Mockito.mock(Field.class));

        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        Mockito.doReturn(Mockito.mock(Field.class)).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));

        // When
        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, fieldInstance).getCandidates();

        // Then
        assertEquals(1, result.size());
        assertEquals(Mockito.mock(Field.class), result.get(0));
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsMultipleMatchingMocks_ThenReturnAllMatchingMocks() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Field.class.getSuperclass()));
        mocks.add(Mockito.mock(Field.class));
        mocks.add(Mockito.mock(Field.class));

        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        Mockito.doReturn(new ArrayList<>(List.of(Mockito.mock(Field.class), Mockito.mock(Field.class)))).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));

        // When
        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, fieldInstance).getCandidates();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(Mockito.mock(Field.class)));
        assertTrue(result.contains(Mockito.mock(Field.class)));
    }
}