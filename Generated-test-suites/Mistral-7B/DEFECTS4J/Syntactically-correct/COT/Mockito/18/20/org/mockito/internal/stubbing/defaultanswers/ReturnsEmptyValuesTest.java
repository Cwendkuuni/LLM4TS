package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
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
    public void testToStringAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        InvocationOnMock invocation = new InvocationOnMock(mock, "toString", new Class[0]);

        // When
        String result = (String) returnsEmptyValues.answer(invocation);

        // Then
        Assert.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        Object other = new Object();
        InvocationOnMock invocation = new InvocationOnMock(mock, "compareTo", new Object[]{other});

        // When
        int result = (Integer) returnsEmptyValues.answer(invocation);

        // Then
        Assert.assertEquals(invocation.getMock() == other ? 0 : 1, result);
    }

    @Test
    public void testPrimitiveReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        byte byteResult = (byte) returnsEmptyValues.returnValueFor(byte.class);
        short shortResult = (short) returnsEmptyValues.returnValueFor(short.class);
        int intResult = (int) returnsEmptyValues.returnValueFor(int.class);
        long longResult = (long) returnsEmptyValues.returnValueFor(long.class);
        char charResult = (char) returnsEmptyValues.returnValueFor(char.class);
        boolean booleanResult = (boolean) returnsEmptyValues.returnValueFor(boolean.class);

        // Then
        Assert.assertEquals(Byte.MIN_VALUE, byteResult);
        Assert.assertEquals(Short.MIN_VALUE, shortResult);
        Assert.assertEquals(0, intResult);
        Assert.assertEquals(0L, longResult);
        Assert.assertEquals((char) 0, charResult);
        Assert.assertEquals(false, booleanResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionReturnTypeAnswer_ModifiyCollection() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<String> collection = (Collection<String>) returnsEmptyValues.returnValueFor(Collection.class);

        // When
        collection.add("test");

        // Then
        // Assertion will throw UnsupportedOperationException
    }

    @Test
    public void testCollectionReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Collection<String> result = (Collection<String>) returnsEmptyValues.returnValueFor(Collection.class);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testSetReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<String> result = (Set<String>) returnsEmptyValues.returnValueFor(Set.class);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testSortedSetReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedSet<String> result = (SortedSet<String>) returnsEmptyValues.returnValueFor(SortedSet.class);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testMapReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.returnValueFor(Map.class);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testSortedMapReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedMap<String, String> result = (SortedMap<String, String>) returnsEmptyValues.returnValueFor(SortedMap.class);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testLinkedHashMapReturnTypeAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        // Then
        Assert.assertTrue(result.isEmpty());
    }
}