package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        // Given
        Object mock = mock(String.class);

        // When
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);
        String result = (String) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));

        // Then
        assertEquals("Mock for " + Mockito.type(String.class).getName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        // Given
        Object mock = mock(Number.class);
        Object another = mock(Number.class);

        // When
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        // Then
        int result = (Integer) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("compareTo") && invocation.getArguments().length == 1));
        verify(mock).equals(another);
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveTypes() {
        // Given
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        // When
        byte result1 = (byte) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("byteValue")));
        short result2 = (short) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("shortValue")));
        int result3 = (int) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("intValue")));
        long result4 = (long) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("longValue")));
        float result5 = (float) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("floatValue")));
        double result6 = (double) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("doubleValue")));
        boolean result7 = (boolean) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("booleanValue")));
        char result8 = (char) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("charValue")));

        // Then
        assertEquals((byte) 0, result1);
        assertEquals((short) 0, result2);
        assertEquals(0, result3);
        assertEquals(0L, result4);
        assertEquals(0f, result5, 0.01f);
        assertEquals(0d, result6, 0.01d);
        assertEquals(false, result7);
        assertEquals((char) 0, result8);
    }

    @Test
    public void testCollections() {
        // Given
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        // When
        Collection<String> result1 = (Collection<String>) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("values")));
        Set<String> result2 = (Set<String>) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("keySet")));
        SortedSet<String> result3 = (SortedSet<String>) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("descendingSet")));
        List<String> result4 = (List<String>) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("subList")));

        // Then
        assertEquals(new ArrayList<String>(), result1);
        assertEquals(new HashSet<String>(), result2);
        assertEquals(new TreeSet<String>(Arrays.asList("foo", "bar", "baz")), result3);
        assertEquals(new ArrayList<String>(), result4);
    }

    @Test
    public void testMaps() {
        // Given
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        // When
        Map<String, Integer> result1 = (Map<String, Integer>) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("entrySet")));

        // Then
        assertEquals(new HashMap<String, Integer>(), result1);
    }
}