package org.mockito.internal.stubbing.defaultanswers; // make sure your test package is the same as the class package

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Answer answer = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.method(String.class, "toString");
        String result = (String) answer.answer(invocation);
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Answer answer = new ReturnsEmptyValues();
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        InvocationOnMock invocation = Mockito.method(Comparable.class, "compareTo", Object.class);
        int result = (Integer) answer.answer(invocation.withArguments(mock1, mock2));
        verify(mock1).equals(mock2);
        verify(mock1).hashCode();
        verify(mock2).hashCode();
        verifyZeroInteractions(mock1, mock2);
        assertEquals(0, result);
    }

    @Test
    public void testAnswerPrimitiveTypes() {
        Answer answer = new ReturnsEmptyValues();
        InvocationOnMock intInvocation = Mockito.method(int[].class, "length");
        int[] intArray = new int[0];
        int result = (int) answer.answer(intInvocation);
        assertEquals(0, result);

        InvocationOnMock booleanInvocation = Mockito.method(boolean[].class, "length");
        boolean[] booleanArray = new boolean[0];
        boolean resultBoolean = (boolean) answer.answer(booleanInvocation);
        assertEquals(false, resultBoolean);

        InvocationOnMock charInvocation = Mockito.method(char[].class, "length");
        char[] charArray = new char[0];
        char resultChar = (char) answer.answer(charInvocation);
        assertEquals('\u0000', resultChar);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerImmutableCollectionTypes() {
        Answer answer = new ReturnsEmptyValues();
        InvocationOnMock setInvocation = Mockito.method(SortedSet.class, "descendingIterator");
        SortedSet<Integer> set = new TreeSet<>();
        answer.answer(setInvocation);
    }

    @Test
    public void testAnswerListType() {
        Answer answer = new ReturnsEmptyValues();
        List<String> list = new LinkedList<>();
        InvocationOnMock listInvocation = Mockito.method(List.class, "size");
        int result = (int) answer.answer(listInvocation);
        assertEquals(0, result);

        InvocationOnMock getInvocation = Mockito.method(List.class, "get", int.class);
        String resultString = (String) answer.answer(getInvocation.withArguments(0));
        assertNull(resultString);

        InvocationOnMock addInvocation = Mockito.method(List.class, "add", Object.class);
        answer.answer(addInvocation);
        verifyNoMoreInteractions(list);
    }

    @Test
    public void testAnswerMapType() {
        Answer answer = new ReturnsEmptyValues();
        Map<String, Integer> map = new HashMap<>();
        InvocationOnMock mapInvocation = Mockito.method(Map.class, "size");
        int result = (int) answer.answer(mapInvocation);
        assertEquals(0, result);

        InvocationOnMock putInvocation = Mockito.method(Map.class, "put", String.class, Integer.class);
        answer.answer(putInvocation);
        verifyNoMoreInteractions(map);
    }

}