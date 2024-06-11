package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        MyMock mock = mock(MyMock.class);
        when(mock.toString()).then(answer);

        assertEquals("Mock for MyMock, hashCode: " + mock.hashCode(), mock.toString());
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        MyMock mock1 = mock(MyMock.class);
        MyMock mock2 = mock(MyMock.class);

        int result = answer.answer(new InvocationOnMock(mock1, mock1.getClass().getMethod("compareTo", MyMock.class), new Object[0]));
        assertEquals(0, result);

        result = answer.answer(new InvocationOnMock(mock1, mock2.getClass().getMethod("compareTo", MyMock.class), new Object[0]));
        assertNotEquals(0, result);
    }

    @Test
    public void testPrimitiveReturningMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        int result = (int) answer.answer(new InvocationOnMock(null, Integer.TYPE.getName(), new Object[0]));
        assertEquals(0, result);
    }

    @Test
    public void testCollectionReturningMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Collection<String> result = (Collection<String>) answer.answer(new InvocationOnMock(null, Collection.class.getName(), new Object[0]));
        assertTrue(result.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingReturnedCollection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Collection<String> result = (Collection<String>) answer.answer(new InvocationOnMock(null, ArrayList.class.getName(), new Object[0]));
        result.add("test");
    }

    @Test
    public void testMapReturningMethod() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Map<String, Integer> result = (Map<String, Integer>) answer.answer(new InvocationOnMock(null, Map.class.getName(), new Object[0]));
        assertTrue(result.isEmpty());
    }

    private static class MyMock implements Comparable<MyMock> {
    }
}