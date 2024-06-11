package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Test class for the ReturnsEmptyValues class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testReturnsToString() {
        // given
        ReturnsEmptyValues returningAnswer = new ReturnsEmptyValues();
        Mockito<Answer<String>> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(returningAnswer);
        String mock = mock(String.class);

        // when
        String result = mockAnswer.answer(mock(InvocationOnMock.class)).toString();

        // then
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        // given
        ReturnsEmptyValues returningAnswer = new ReturnsEmptyValues();
        Mockito<Answer<Integer>> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(returningAnswer);
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        // when
        int result = mockAnswer.answer(mock(InvocationOnMock.class)).compareTo(mock2);

        // then
        assertNotEquals(0, result);
    }

    @Test
    public void testReturnsEmptyCollection() {
        // given
        ReturnsEmptyValues returningAnswer = new ReturnsEmptyValues();
        Mockito<Answer<Collection<Integer>>> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(returningAnswer);

        // when
        Collection<Integer> result = mockAnswer.answer(mock(InvocationOnMock.class));

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnsEmptySet() {
        // given
        ReturnsEmptyValues returningAnswer = new ReturnsEmptyValues();
        Mockito<Answer<Set<Integer>>> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(returningAnswer);

        // when
        Set<Integer> result = mockAnswer.answer(mock(InvocationOnMock.class));

        // then
        assertTrue(result.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingEmptyCollection() {
        // given
        ReturnsEmptyValues returningAnswer = new ReturnsEmptyValues();
        Mockito<Answer<List<Integer>>> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(returningAnswer);
        List<Integer> result = mockAnswer.answer(mock(InvocationOnMock.class));

        // when & then
        result.add(1);
    }
}