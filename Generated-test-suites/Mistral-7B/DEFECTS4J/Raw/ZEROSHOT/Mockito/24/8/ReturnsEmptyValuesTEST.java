package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

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

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        String expectedMockDescription = "Mock for " + String.class.getSimpleName() + ", hashCode: " + System.identityHashCode(mock);
        String result = (String) emptyValues.answer(new InvocationOnMock(Mockito.method(String.class, "toString"), mock));

        Assert.assertEquals(expectedMockDescription, result);
    }

    @Test
    public void testCompareTo() {
        Object mock1 = Mockito.mock(Number.class);
        Object mock2 = Mockito.mock(Number.class);

        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        int result = (Integer) emptyValues.answer(new InvocationOnMock(Mockito.method(Number.class, "compareTo", Number.class), mock1, mock2));

        Assert.assertNotSame(0, result);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        byte resultByte = (Byte) emptyValues.returnValueFor(byte.class);
        short resultShort = (Short) emptyValues.returnValueFor(short.class);
        int resultInt = (Integer) emptyValues.returnValueFor(int.class);
        long resultLong = (Long) emptyValues.returnValueFor(long.class);
        float resultFloat = (Float) emptyValues.returnValueFor(float.class);
        double resultDouble = (Double) emptyValues.returnValueFor(double.class);
        boolean resultBoolean = (Boolean) emptyValues.returnValueFor(boolean.class);
        char resultChar = (Character) emptyValues.returnValueFor(char.class);

        Assert.assertEquals(0, resultByte);
        Assert.assertEquals(0, resultShort);
        Assert.assertEquals(0, resultInt);
        Assert.assertEquals(0L, resultLong);
        Assert.assertEquals(0.0f, resultFloat, 0.001f);
        Assert.assertEquals(0.0d, resultDouble, 0.001d);
        Assert.assertEquals(false, resultBoolean);
        Assert.assertEquals('\u0000', resultChar);
    }

    @Test
    public void testEmptyCollections() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        List<Integer> resultList = (List<Integer>) emptyValues.returnValueFor(List.class);
        Set<String> resultSet = (Set<String>) emptyValues.returnValueFor(Set.class);
        HashSet<String> resultHashSet = (HashSet<String>) emptyValues.returnValueFor(HashSet.class);
        SortedSet<Float> resultTreeSet = (SortedSet<Float>) emptyValues.returnValueFor(TreeSet.class);
        LinkedHashSet<Float> resultLinkedHashSet = (LinkedHashSet<Float>) emptyValues.returnValueFor(LinkedHashSet.class);
        Map<String, String> resultMap = (Map<String, String>) emptyValues.returnValueFor(Map.class);
        HashMap<String, String> resultHashMap = (HashMap<String, String>) emptyValues.returnValueFor(HashMap.class);
        SortedMap<Integer, String> resultTreeMap = (SortedMap<Integer, String>) emptyValues.returnValueFor(TreeMap.class);
        LinkedHashMap<Integer, String> resultLinkedHashMap = (LinkedHashMap<Integer, String>) emptyValues.returnValueFor(LinkedHashMap.class);

        Assert.assertTrue(resultList instanceof LinkedList);
        Assert.assertTrue(resultSet instanceof HashSet);
        Assert.assertTrue(resultHashSet instanceof HashSet);
        Assert.assertTrue(resultTreeSet instanceof TreeSet);
        Assert.assertTrue(resultLinkedHashSet instanceof LinkedHashSet);
        Assert.assertTrue(resultMap instanceof HashMap);
        Assert.assertTrue(resultHashMap instanceof HashMap);
        Assert.assertTrue(resultTreeMap instanceof TreeMap);
        Assert.assertTrue(resultLinkedHashMap instanceof LinkedHashMap);

        Assert.assertTrue(resultList.isEmpty());
        Assert.assertTrue(resultSet.isEmpty());
        Assert.assertTrue(resultHashSet.isEmpty());
        Assert.assertTrue(resultTreeSet.isEmpty());
        Assert.assertTrue(resultLinkedHashSet.isEmpty());
        Assert.assertTrue(resultMap.isEmpty());
        Assert.assertTrue(resultHashMap.isEmpty());
        Assert.assertTrue(resultTreeMap.isEmpty());
        Assert.assertTrue(resultLinkedHashMap.isEmpty());
    }

}