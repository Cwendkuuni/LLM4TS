package com.fasterxml.jackson.databind.type; // make sure to import all required classes

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        Assert.assertEquals(0, empty.size());
        Assert.assertNull(empty.findBoundType("T"));
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class);
        Assert.assertEquals(0, bindings.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingNamesAndTypes() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, Arrays.asList(JavaType.of(String.class), JavaType.of(Integer.class)));
    }

    @Test
    public void testCreateWithOneTypeArg() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.of(Integer.class));
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("E", bindings.getBoundName(0));
        Assert.assertEquals(JavaType.of(Integer.class), bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        TypeBindings bindings = TypeBindings.create(Map.class, JavaType.of(String.class), JavaType.of(Integer.class));
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(JavaType.of(String.class), bindings.getBoundType(0));
        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(JavaType.of(Integer.class), bindings.getBoundType(1));
    }

    @Test
    public void testCreateWithUnboundVariables() {
        TypeBindings bindings = TypeBindings.create(Map.class, JavaType.of(TypeVariable.class), JavaType.of(Integer.class));
        Assert.assertEquals(2, bindings.size());
        Assert.assertArrayEquals(new String[]{"K", "V"}, bindings._names);
        Assert.assertArrayEquals(new JavaType[]{JavaType.of(TypeVariable.class), JavaType.of(Integer.class)}, bindings._types);
        Assert.assertTrue(bindings.hasUnbound("K"));
        Assert.assertTrue(bindings.hasUnbound("V"));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(Map.class, JavaType.of(TypeVariable.class), JavaType.of(Integer.class));
        TypeBindings withUnbound = bindings.withUnboundVariable("U");
        Assert.assertEquals(3, withUnbound.size());
        Assert.assertArrayEquals(new String[]{"K", "V", "U"}, withUnbound._names);
        Assert.assertArrayEquals(new JavaType[]{JavaType.of(TypeVariable.class), JavaType.of(Integer.class), null}, withUnbound._types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeIndex() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.of(Integer.class));
        bindings.getBoundType(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBoundTypeWithNull() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.of(Integer.class));
        bindings = bindings.withUnboundVariable("U");
        bindings.getBoundType(2);
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(Map.class, JavaType.of(String.class), JavaType.of(Integer.class));
        Assert.assertEquals(JavaType.of(String.class), bindings.findBoundType("K"));
        Assert.assertEquals(JavaType.of(Integer.class), bindings.findBoundType("V"));
        Assert.assertNull(bindings.findBoundType("W"));
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.create(Map.class, JavaType.of(String.class), JavaType.of(Integer.class));
        String toString = bindings.toString();
        String expected = "<Map<K, V>>";
        Assert.assertEquals(expected, toString);
    }

    @Test
    public void testHashCodeAndEquals() {
        TypeBindings bindings1 = TypeBindings.create(Map.class, JavaType.of(String.class), JavaType.of(Integer.class));
        TypeBindings bindings2 = TypeBindings.create(Map.class, JavaType.of(String.class), JavaType.of(Integer.class));
        Assert.assertEquals(bindings1, bindings2);

        TypeBindings bindings3 = TypeBindings.create(Map.class, JavaType.of(String.class), JavaType.of(String.class));
        Assert.assertNotEquals(bindings1, bindings3);
    }

    
}