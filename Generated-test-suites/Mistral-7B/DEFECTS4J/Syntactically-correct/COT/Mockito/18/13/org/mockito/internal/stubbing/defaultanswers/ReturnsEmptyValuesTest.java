package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = Mockito.mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Assert.assertEquals("Mock for String, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(Mockito.argThat(invocation -> methodsGuru.isToString(invocation.getMethod()))).toString());
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Mockito.when(mock1.equals(mock2)).thenReturn(true);
        int result = (int) returnsEmptyValues.answer(new InvocationOnMock(Mockito.method(String.class, "compareTo", String.class), mock1, new Object[0], null));
        Assert.assertEquals(0, result);

        Mockito.when(mock1.equals(mock2)).thenReturn(false);
        result = (int) returnsEmptyValues.answer(new InvocationOnMock(Mockito.method(String.class, "compareTo", String.class), mock1, new Object[0], null));
        Assert.assertEquals(1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod() == null));
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        byte byteResult = (byte) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(byte.class)));
        short shortResult = (short) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(short.class)));
        int intResult = (int) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(int.class)));
        long longResult = (long) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(long.class)));
        float floatResult = (float) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(float.class)));
        double doubleResult = (double) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(double.class)));
        boolean booleanResult = (boolean) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(boolean.class)));
        char charResult = (char) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getParameterTypes()[0].isPrimitive() && invocation.getMethod().getParameterTypes()[0].equals(char.class)));

        Assert.assertEquals(0, byteResult);
        Assert.assertEquals(0, shortResult);
        Assert.assertEquals(0, intResult);
        Assert.assertEquals(0L, longResult);
        Assert.assertEquals(0f, floatResult, 0.001f);
        Assert.assertEquals(0d, doubleResult, 0.001d);
        Assert.assertEquals(false, booleanResult);
        Assert.assertEquals('\u0000', charResult);
    }

    @Test
    public void testCollectionReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<String> collectionResult = (Collection<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(Collection.class)));
        Assert.assertTrue(collectionResult.isEmpty());
    }

    @Test
    public void testSetReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<String> setResult = (Set<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(Set.class)));
        Assert.assertTrue(setResult instanceof HashSet);
        Assert.assertTrue(setResult.isEmpty());
    }

    @Test
    public void testHashSetReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<String> setResult = (Set<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(HashSet.class)));
        Assert.assertTrue(setResult instanceof HashSet);
        Assert.assertTrue(setResult.isEmpty());
    }

    @Test
    public void testSortedSetReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<String> sortedSetResult = (SortedSet<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(SortedSet.class)));
        Assert.assertTrue(sortedSetResult instanceof TreeSet);
        Assert.assertTrue(sortedSetResult.isEmpty());
    }

    @Test
    public void testTreeSetReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<String> sortedSetResult = (SortedSet<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(TreeSet.class)));
        Assert.assertTrue(sortedSetResult instanceof TreeSet);
        Assert.assertTrue(sortedSetResult.isEmpty());
    }

    @Test
    public void testLinkedHashSetReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<String> setResult = (Set<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(LinkedHashSet.class)));
        Assert.assertTrue(setResult instanceof LinkedHashSet);
        Assert.assertTrue(setResult.isEmpty());
    }

    @Test
    public void testListReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> listResult = (List<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(List.class)));
        Assert.assertTrue(listResult instanceof LinkedList);
        Assert.assertTrue(listResult.isEmpty());
    }

    @Test
    public void testLinkedListReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> listResult = (List<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(LinkedList.class)));
        Assert.assertTrue(listResult instanceof LinkedList);
        Assert.assertTrue(listResult.isEmpty());
    }

    @Test
    public void testArrayListReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> listResult = (List<String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(ArrayList.class)));
        Assert.assertTrue(listResult instanceof ArrayList);
        Assert.assertTrue(listResult.isEmpty());
    }

    @Test
    public void testMapReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, String> mapResult = (Map<String, String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(Map.class)));
        Assert.assertTrue(mapResult instanceof HashMap);
        Assert.assertTrue(mapResult.isEmpty());
    }

    @Test
    public void testHashMapReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, String> mapResult = (Map<String, String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(HashMap.class)));
        Assert.assertTrue(mapResult instanceof HashMap);
        Assert.assertTrue(mapResult.isEmpty());
    }

    @Test
    public void testSortedMapReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<String, String> sortedMapResult = (SortedMap<String, String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(SortedMap.class)));
        Assert.assertTrue(sortedMapResult instanceof TreeMap);
        Assert.assertTrue(sortedMapResult.isEmpty());
    }

    @Test
    public void testTreeMapReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<String, String> sortedMapResult = (SortedMap<String, String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(TreeMap.class)));
        Assert.assertTrue(sortedMapResult instanceof TreeMap);
        Assert.assertTrue(sortedMapResult.isEmpty());
    }

    @Test
    public void testLinkedHashMapReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, String> mapResult = (Map<String, String>) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().equals(LinkedHashMap.class)));
        Assert.assertTrue(mapResult instanceof LinkedHashMap);
        Assert.assertTrue(mapResult.isEmpty());
    }

}