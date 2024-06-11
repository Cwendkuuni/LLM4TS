package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

/**
 * Test class for the ReturnsEmptyValues class
 */
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_PrimitiveInteger() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (int) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Integer(1).getType())));
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_PrimitiveBoolean() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        boolean result = (boolean) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Boolean(true).getType())));
        assertFalse(result);
    }

    @Test
    public void testAnswer_PrimitiveCharacter() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        char result = (char) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Character('a').getType())));
        assertEquals('\u0000', result);
    }

    @Test
    public void testAnswer_PrimitiveByte() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        byte result = (byte) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Byte((byte) 1).getType())));
        assertEquals((byte) 0, result);
    }

    @Test
    public void testAnswer_PrimitiveShort() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        short result = (short) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Short((short) 1).getType())));
        assertEquals((short) 0, result);
    }

    @Test
    public void testAnswer_PrimitiveLong() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        long result = (long) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Long((long) 1).getType())));
        assertEquals(0L, result);
    }

    @Test
    public void testAnswer_PrimitiveFloat() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        float result = (float) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Float((float) 1).getType())));
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testAnswer_PrimitiveDouble() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        double result = (double) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new Double((double) 1).getType())));
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testAnswer_PrimitiveByteArray() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        byte[] result = (byte[]) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, byte[].class)));
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testAnswer_PrimitiveCharArray() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        char[] result = (char[]) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, char[].class)));
        assertArrayEquals(new char[0], result);
    }

    @Test
    public void testAnswer_PrimitiveBooleanArray() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        boolean[] result = (boolean[]) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, boolean[].class)));
        assertArrayEquals(new boolean[0], result);
    }

    @Test
    public void testAnswer_PrimitiveByteArrayOfLengthOne() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        byte[] result = (byte[]) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new byte[1].getClass())));
        assertEquals(0, result[0]);
    }

    @Test
    public void testAnswer_PrimitiveCharArrayOfLengthOne() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        char[] result = (char[]) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new char[1].getClass())));
        assertEquals('\u0000', result[0]);
    }

    @Test
    public void testAnswer_PrimitiveBooleanArrayOfLengthOne() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        boolean[] result = (boolean[]) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, new boolean[1].getClass())));
        assertFalse(result[0]);
    }

    @Test
    public void testAnswer_Object() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, Object.class)));
        assertNull(result);
    }

    @Test
    public void testAnswer_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, Collection.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_ArrayList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, ArrayList.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_LinkedList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, LinkedList.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_HashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, HashSet.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_LinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, LinkedHashSet.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_TreeSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, TreeSet.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, Map.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_HashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, HashMap.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_TreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, TreeMap.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_SortedMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, SortedMap.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_TreeMapOrderedByString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<String, Object> result = (SortedMap<String, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, TreeMap.class, String.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_LinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, LinkedHashMap.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_LinkedHashMapOrderedByLong() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Long, Object> result = (Map<Long, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, LinkedHashMap.class, Long.class)));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_LinkedHashMapOrderedByLongWithAccessOrder() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Long, Object> result = (Map<Long, Object>) returnsEmptyValues.answer(mock(Answer.class).answers(new InvocationOnMock(null, null, LinkedHashMap.class, Long.class, true)));
        assertTrue(result.isEmpty());
    }
}