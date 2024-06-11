package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMock_thenReturnFilteredMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = new Object();
        Field field = new Field("testField", String.class, null, null);

        when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return mock1;
                    }
                });

        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsNotAssignableFromMock_thenReturnNextFilter() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = new Object();
        Field field = new Field("testField", Integer.class, null, null);

        when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return mock2;
                    }
                });

        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock2, result);
    }

    @Test
    public void testFilterCandidate_whenFieldTypeIsAssignableFromMultipleMocks_thenReturnFirstMatchingMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Object fieldInstance = new Object();
        Field field = new Field("testField", String.class, null, null);

        when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return mock2;
                    }
                });

        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result);
    }

    @Test
    public void testFilterCandidate_whenNextFilterReturnsNull_thenReturnNull() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = new Field("testField", String.class, null, null);

        when(nextMock.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(null);

        mocks.add(mock(String.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result);
    }
}