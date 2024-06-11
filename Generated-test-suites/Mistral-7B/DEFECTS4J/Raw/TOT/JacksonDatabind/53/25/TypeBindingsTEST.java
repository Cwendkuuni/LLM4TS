package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFunction;

import org.junit.Test;
import org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        Assert.assertNull(bindings.findBoundType(""));
        Assert.assertTrue(bindings.isEmpty());
        Assert.assertEquals("[]", bindings.toString());
    }

    @Test
    public void testCreateWithSingleTypeArgument() {
        Class<List<String>> listOfStrings = new ArrayList<String>().getClass();
        TypeBindings bindings = TypeBindings.create(listOfStrings, null);
        Assert.assertEquals("java.lang.String", bindings.getBoundType(0).getClass().getName());
        Assert.assertEquals(1, bindings.size());
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertEquals("[Ljava.lang.String;", bindings.toString());

        TypeBindings bindings2 = TypeBindings.createIfNeeded(listOfStrings, null);
        Assert.assertEquals(bindings, bindings2);
    }

    @Test
    public void testCreateWithArrayTypeArgument() {
        TypeBindings bindings = TypeBindings.create(int[].class, null);
        Assert.assertEquals("int[]", bindings.toString());
        Assert.assertEquals(1, bindings.size());
        Assert.assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateWithMultipleTypeArguments() {
        Class<Map<String, List<Integer>>> mapOfListInt = new HashMap<String, ArrayList<Integer>>().getClass();
        TypeBindings bindings = TypeBindings.create(mapOfListInt, null);
        Assert.assertEquals("java.lang.String", bindings.getBoundType(0).getClass().getName());
        Assert.assertEquals("java.util.List", bindings.getBoundType(1).getClass().getName());
        Assert.assertEquals(2, bindings.size());
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertEquals("{Ljava.lang.String;Ljava.util.List;}", bindings.toString());

        TypeBindings bindings2 = TypeBindings.createIfNeeded(mapOfListInt, null);
        Assert.assertEquals(bindings, bindings2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedArguments() {
        Class<List<String>> listOfStrings = new ArrayList<String>().getClass();
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(listOfStrings, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArguments() {
        Class<List<String>> listOfStrings = new ArrayList<String>().getClass();
        JavaType javaType1 = new TypeReference<List<Integer>>() {}.getType();
        JavaType javaType2 = new TypeReference<List<Double>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(listOfStrings, javaType1, javaType2);
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<Map<String, List<Integer>>> mapOfListInt = new HashMap<String, ArrayList<Integer>>().getClass();
        TypeBindings bindings = TypeBindings.create(mapOfListInt, null);
        TypeBindings bindings2 = bindings.withUnboundVariable("X");
        Assert.assertEquals("{Ljava.lang.String;Ljava.util.List<X>;}", bindings2.toString());
        Assert.assertTrue(bindings2.hasUnbound("X"));
    }
}