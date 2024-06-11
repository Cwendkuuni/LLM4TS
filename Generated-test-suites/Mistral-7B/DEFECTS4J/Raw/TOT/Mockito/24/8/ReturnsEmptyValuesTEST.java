package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues answers;

    @Before
    public void setup() {
        answers = new ReturnsEmptyValues();
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mock.toString()).thenAnswer(answers);
        Mockito.when(invocation.getMock()).thenReturn(mock);

        String result = answers.answer(invocation);
        assertEquals("Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareTo() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mock1.compareTo(ArgumentMatchers.any())).thenAnswer(answers);
        Mockito.when(mock2.compareTo(ArgumentMatchers.any())).thenAnswer(answers);
        Mockito.when(invocation1.getMock()).thenReturn(mock1);
        Mockito.when(invocation2.getMock()).thenReturn(mock2);

        int result1 = answers.answer(invocation1).getClass().getMethod("compareTo", Object.class).invoke(answers.answer(invocation1), mock2);
        int result2 = answers.answer(invocation2).getClass().getMethod("compareTo", Object.class).invoke(mock2, answers.answer(invocation2));

        assertEquals(1, result1);
        assertEquals(1, result2);
    }

    @Test
    public void testPrimitives() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        byte resultByte = (byte) answers.answer(invocation).byteValue();
        short resultShort = (short) answers.answer(invocation).shortValue();
        int resultInt = (int) answers.answer(invocation).intValue();
        long resultLong = (long) answers.answer(invocation).longValue();
        float resultFloat = (float) answers.answer(invocation).floatValue();
        double resultDouble = (double) answers.answer(invocation).doubleValue();
        boolean resultBoolean = (boolean) answers.answer(invocation).booleanValue();

        assertEquals(0, resultByte);
        assertEquals(0, resultShort);
        assertEquals(0, resultInt);
        assertEquals(0L, resultLong);
        assertEquals(0f, resultFloat, 0.01f);
        assertEquals(0d, resultDouble, 0.01d);
        assertEquals(false, resultBoolean);
    }

    @Test
    public void testCollections() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Collection<Integer> resultCollection = (Collection<Integer>) answers.answer(invocation);
        assertNotNull(resultCollection);
        assertEquals(0, resultCollection.size());
        assertNull(resultCollection.iterator().next());

        Set<String> resultSet = (Set<String>) answers.answer(invocation);
        assertNotNull(resultSet);
        assertEquals(0, resultSet.size());
        assertNull(resultSet.iterator().next());

        List<Double> resultList = (List<Double>) answers.answer(invocation);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
        assertNull(resultList.get(0));

        Map<String, Integer> resultMap = (Map<String, Integer>) answers.answer(invocation);
        assertNotNull(resultMap);
        assertEquals(0, resultMap.size());
        assertNull(resultMap.get("key"));
    }
}