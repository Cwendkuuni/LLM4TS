package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

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
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void shouldFilterMockWithSameType() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mockType = mock(String.class);
        mocks.add(mockType);
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        field.getType().setAssignableFrom(String.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mockType, result.getMock());
    }

    @Test
    public void shouldDelegateToNextFilterWhenNoMatchingMockFound() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mockType = mock(String.class);
        Object otherMock = mock(Integer.class);
        mocks.add(mockType);
        mocks.add(otherMock);
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        // When
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> answer = Mockito.answer(invocation);
        Mockito.doAnswer(answer).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Mockito.verify(nextMock).filterCandidate(Mockito.eq(new ArrayList<>(List.of(otherMock))), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void shouldDelegateToNextFilterWhenMultipleMatchingMocksFound() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mockType1 = mock(String.class);
        Object mockType2 = mock(String.class);
        mocks.add(mockType1);
        mocks.add(mockType2);
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        field.getType().setAssignableFrom(String.class);

        // When
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> answer = Mockito.answer(invocation);
        Mockito.doAnswer(answer).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Mockito.verify(nextMock, Mockito.times(1)).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void shouldFilterCandidatesFromNextFilterWhenNoMatchingMockFoundInThisFilter() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mockType = mock(String.class);
        Object otherMock = mock(Integer.class);
        mocks.add(mockType);
        mocks.add(otherMock);
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        field.getType().setAssignableFrom(Object.class);

        // When
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> answer = Mockito.answer(invocation);
        Mockito.doAnswer(answer).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Mockito.verify(nextMock, Mockito.times(1)).filterCandidate(Mockito.eq(new ArrayList<>(List.of(otherMock))), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void shouldFilterCandidateWithSameTypeAsField() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object mockType = mock(String[].class);
        Object fieldInstance = mock(Object[].class);
        Field field = mock(Field.class);
        field.setType(String[].class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mockType, result.getMock());
    }
}