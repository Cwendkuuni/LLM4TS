package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertTrue(typeBindings.isEmpty());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreate() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE, JavaType.LONG_TYPE};
        TypeBindings typeBindings = TypeBindings.create(clazz, types);
        assertFalse(typeBindings.isEmpty());
        assertEquals(2, typeBindings.size());
        assertEquals("java.lang.Integer", typeBindings.getBoundType(0).getRawClass().getName());
        assertEquals("int", typeBindings.getBoundType(0).getGenericSignature());
        assertEquals("java.lang.Long", typeBindings.getBoundType(1).getRawClass().getName());
        assertEquals("long", typeBindings.getBoundType(1).getGenericSignature());
    }

    @Test
    public void testCreateIfNeededWithParameters() {
        Class<?> clazz = List.class;
        JavaType[] types = {JavaType.INT_TYPE, JavaType.LONG_TYPE};
        TypeBindings typeBindings = TypeBindings.createIfNeeded(clazz, types);
        assertFalse(typeBindings.isEmpty());
        assertEquals(2, typeBindings.size());
        assertEquals("E", typeBindings.getBoundType(0).getGenericSignature());
        assertEquals("E", typeBindings.getBoundType(0).getRawClass().getName());
        assertEquals("E", typeBindings.getBoundType(1).getGenericSignature());
        assertEquals("E", typeBindings.getBoundType(1).getRawClass().getName());
    }

    @Test
    public void testCreateIfNeededWithoutParameters() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE, JavaType.LONG_TYPE};
        TypeBindings typeBindings = TypeBindings.createIfNeeded(clazz, types);
        assertTrue(typeBindings.isEmpty());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE};
        TypeBindings typeBindings = TypeBindings.create(clazz, types);
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("T");
        assertNotNull(newTypeBindings);
        assertFalse(newTypeBindings.isEmpty());
        assertEquals(1, newTypeBindings.size());
        assertEquals("N", newTypeBindings.getBoundType(0).getGenericSignature());
        assertEquals("E", newTypeBindings.getBoundType(0).getRawClass().getName());
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE};
        TypeBindings typeBindings = TypeBindings.create(clazz, types);
        JavaType javaType = typeBindings.findBoundType("E");
        assertNotNull(javaType);
        assertEquals("E", javaType.getGenericSignature());
    }

    @Test
    public void testToString() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE};
        TypeBindings typeBindings = TypeBindings.create(clazz, types);
        String result = typeBindings.toString();
        assertEquals("<int>", result);
    }

    @Test
    public void testHashCode() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE};
        TypeBindings typeBindings = TypeBindings.create(clazz, types);
        int hashCode = typeBindings.hashCode();
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testEquals() {
        Class<?> clazz = Integer.class;
        JavaType[] types = {JavaType.INT_TYPE};
        TypeBindings typeBindings1 = TypeBindings.create(clazz, types);
        TypeBindings typeBindings2 = TypeBindings.create(clazz, types);
        assertTrue(typeBindings1.equals(typeBindings2));
        assertTrue(typeBindings2.equals(typeBindings1));
    }

}