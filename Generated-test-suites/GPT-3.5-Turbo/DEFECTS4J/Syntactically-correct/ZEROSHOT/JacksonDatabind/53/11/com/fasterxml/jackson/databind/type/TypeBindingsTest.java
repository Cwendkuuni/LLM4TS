package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        Assert.assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testCreate() {
        Class<?> erasedType = ArrayList.class;
        List<JavaType> typeList = Collections.singletonList(JavaType.forType(String.class));
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeList);
        Assert.assertEquals(1, typeBindings.size());
        Assert.assertEquals("E", typeBindings.getBoundName(0));
        Assert.assertEquals(JavaType.forType(String.class), typeBindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeeded1() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.forType(String.class);
        TypeBindings typeBindings = TypeBindings.createIfNeeded(erasedType, typeArg1);
        Assert.assertEquals(1, typeBindings.size());
        Assert.assertEquals("K", typeBindings.getBoundName(0));
        Assert.assertEquals(JavaType.forType(String.class), typeBindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeeded2() {
        Class<?> erasedType = ArrayList.class;
        JavaType[] typeArray = {JavaType.forType(String.class), JavaType.forType(Integer.class)};
        TypeBindings typeBindings = TypeBindings.createIfNeeded(erasedType, typeArray);
        Assert.assertEquals(2, typeBindings.size());
        Assert.assertEquals("E", typeBindings.getBoundName(0));
        Assert.assertEquals(JavaType.forType(String.class), typeBindings.getBoundType(0));
        Assert.assertEquals("E", typeBindings.getBoundName(1));
        Assert.assertEquals(JavaType.forType(Integer.class), typeBindings.getBoundType(1));
    }

    @Test
    public void testWithUnboundVariable() {
        String name = "T";
        TypeBindings initialTypeBindings = TypeBindings.emptyBindings();
        TypeBindings updatedTypeBindings = initialTypeBindings.withUnboundVariable(name);
        Assert.assertEquals(1, updatedTypeBindings.size());
        Assert.assertEquals(name, updatedTypeBindings.getBoundName(0));
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = ArrayList.class;
        List<JavaType> typeList = Collections.singletonList(JavaType.forType(String.class));
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeList);
        Assert.assertEquals(JavaType.forType(String.class), typeBindings.findBoundType("E"));
    }

    @Test
    public void testSize() {
        List<JavaType> typeList = Collections.singletonList(JavaType.forType(String.class));
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, typeList);
        Assert.assertEquals(1, typeBindings.size());
        Assert.assertEquals(0, TypeBindings.emptyBindings().size());
    }

    @Test
    public void testToString() {
        List<JavaType> typeList = Arrays.asList(JavaType.forType(String.class), JavaType.forType(Integer.class));
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, typeList);
        Assert.assertEquals("<java.lang.String,java.lang.Integer>", typeBindings.toString());
        Assert.assertEquals("<>", TypeBindings.emptyBindings().toString());
    }

    @Test
    public void testHashCode() {
        List<JavaType> typeList = Collections.singletonList(JavaType.forType(String.class));
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, typeList);
        Assert.assertNotNull(typeBindings.hashCode());
    }

    @Test
    public void testEquals() {
        List<JavaType> typeList = Collections.singletonList(JavaType.forType(String.class));
        TypeBindings typeBindings1 = TypeBindings.create(ArrayList.class, typeList);
        TypeBindings typeBindings2 = TypeBindings.create(ArrayList.class, typeList);
        Assert.assertEquals(typeBindings1, typeBindings2);
        TypeBindings typeBindings3 = TypeBindings.create(Arrays.asList(Integer.class, Float.class));
        Assert.assertNotEquals(typeBindings1, typeBindings3);
    }
}