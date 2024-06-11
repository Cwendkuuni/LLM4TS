package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        InvocationOnMock invocation = new InvocationOnMock(answer, new MockName("Mock"), mock.getClass().getMethod("toString"));
        Object result = answer.answer(invocation);

        Assert.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result.toString());
    }

    @Test
    public void testAnswer_compareToMethod() {
        Object mock = mock(Comparable.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        InvocationOnMock invocation = new InvocationOnMock(answer, mock.getClass().getMethod("compareTo", Object.class), mock, new Object[]{mock});
        int result = (Integer) answer.answer(invocation);

        Assert.assertEquals(0, result);
    }

    @Test
    public void testReturnValueFor_primitiveType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte resultByte = (byte) returnsEmptyValues.returnValueFor(Byte.TYPE);
        short resultShort = (short) returnsEmptyValues.returnValueFor(Short.TYPE);
        int resultInt = (int) returnsEmptyValues.returnValueFor(Integer.TYPE);
        long resultLong = (long) returnsEmptyValues.returnValueFor(Long.TYPE);
        float resultFloat = returnsEmptyValues.returnValueFor(Float.TYPE);
        double resultDouble = returnsEmptyValues.returnValueFor(Double.TYPE);
        char resultChar = (char) returnsEmptyValues.returnValueFor(Character.TYPE);
        boolean resultBoolean = (boolean) returnsEmptyValues.returnValueFor(Boolean.TYPE);

        Assert.assertEquals(0, resultByte);
        Assert.assertEquals(0, resultShort);
        Assert.assertEquals(0, resultInt);
        Assert.assertEquals(0L, resultLong);
        Assert.assertEquals(0f, resultFloat, 0.001f);
        Assert.assertEquals(0d, resultDouble, 0.001);
        Assert.assertEquals(0, resultChar);
        Assert.assertEquals(false, resultBoolean);
    }

    @Test
    public void testReturnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<String> result = returnsEmptyValues.returnValueFor(Collection.class);

        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_Set() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<String> resultSet = returnsEmptyValues.returnValueFor(Set.class);

        Assert.assertTrue(resultSet instanceof HashSet);
        Assert.assertTrue(resultSet.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<String> result = returnsEmptyValues.returnValueFor(HashSet.class);

        Assert.assertTrue(result instanceof HashSet);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedSet<String> result = returnsEmptyValues.returnValueFor(SortedSet.class);

        Assert.assertTrue(result instanceof TreeSet);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedSet<String> result = returnsEmptyValues.returnValueFor(TreeSet.class);

        Assert.assertTrue(result instanceof TreeSet);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<String> result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);

        Assert.assertTrue(result instanceof LinkedHashSet);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_List() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<String> result = returnsEmptyValues.returnValueFor(List.class);

        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<String> result = returnsEmptyValues.returnValueFor(LinkedList.class);

        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<String> result = returnsEmptyValues.returnValueFor(ArrayList.class);

        Assert.assertTrue(result instanceof ArrayList);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<String, String> result = returnsEmptyValues.returnValueFor(Map.class);

        Assert.assertTrue(result instanceof HashMap);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<String, String> result = returnsEmptyValues.returnValueFor(HashMap.class);

        Assert.assertTrue(result instanceof HashMap);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedMap<String, String> result = returnsEmptyValues.returnValueFor(SortedMap.class);

        Assert.assertTrue(result instanceof TreeMap);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedMap<String, String> result = returnsEmptyValues.returnValueFor(TreeMap.class);

        Assert.assertTrue(result instanceof TreeMap);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<String, String> result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        Assert.assertTrue(result instanceof LinkedHashMap);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_unknownMethod() {
        Object mock = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        InvocationOnMock invocation = new InvocationOnMock(answer, mock.getClass().getMethod("unknownMethod", new Class[0]));

        verifyZeroInteractions(mock);
        answer.answer(invocation);
    }
}