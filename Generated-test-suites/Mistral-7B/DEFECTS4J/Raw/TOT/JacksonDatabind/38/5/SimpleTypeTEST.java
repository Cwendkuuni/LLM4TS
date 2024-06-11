package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructSimpleType() {
        Class<String> stringClass = String.class;
        JavaType simpleType = TypeFactory.defaultInstance().constructType(stringClass);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
        assertEquals(0, simpleType.getNumberOfParameters());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toCanonical());
        assertEquals("String", simpleType.getParameterName(0).orElse(""));
        assertEquals("java.lang.String", simpleType.getErasedSignature().toString());
        assertEquals("java.lang.String[]", simpleType.getGenericSignature().toString());
    }

    @Test
    public void testConstructWithBindings() {
        Class<List<Integer>> listClass = List.class.getClassGenType(Integer.class);
        JavaType simpleType = TypeFactory.defaultInstance().constructType(listClass, TypeBindings.defaultBindings());
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(List.class, simpleType.getType());
        assertEquals(1, simpleType.getNumberOfParameters());
        assertTrue(simpleType.isContainerType());
        assertEquals("[Ljava.lang.Integer;", simpleType.getErasedSignature().toString());
        assertEquals("<Ljava.lang.Integer;>", simpleType.getGenericSignature().toString());
        assertEquals("java.util.List", simpleType.toCanonical());
        assertEquals("list", simpleType.getParameterName(0).orElse(""));
    }

    @Test
    public void testConstructWithSuperclass() {
        Class<Object> objectClass = Object.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), superClass, null);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(Object.class, simpleType.getType());
        assertEquals(0, simpleType.getNumberOfParameters());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.Object", simpleType.toCanonical());
        assertEquals("Object", simpleType.getParameterName(0).orElse(""));
        assertEquals("java.lang.Object", simpleType.getErasedSignature().toString());
        assertEquals("java.lang.Object", simpleType.getGenericSignature().toString());
    }

    @Test
    public void testConstructWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType valueHandler = TypeFactory.defaultInstance().constructType(String.class);
        JavaType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, valueHandler, false);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
        assertEquals(0, simpleType.getNumberOfParameters());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toCanonical());
        assertEquals("String", simpleType.getParameterName(0).orElse(""));
        assertNotEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType typeHandler = TypeFactory.defaultInstance().constructType(String.class);
        JavaType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false, typeHandler);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
        assertEquals(0, simpleType.getNumberOfParameters());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toCanonical());
        assertEquals("String", simpleType.getParameterName(0).orElse(""));
        assertNotEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testNarrowToSubclass() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        JavaType superType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType subType = new SimpleType(stringBuilderClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = superType._narrow(stringBuilderClass);
        assertNotNull(narrowedType);
        assertInstanceOf(subType.getClass(), narrowedType);
        assertEquals(subType.getType(), narrowedType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowToUnrelatedClass() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        JavaType superType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = superType._narrow(integerClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        JavaType simpleType = TypeFactory.defaultInstance().constructUnsafe(stringClass);
        assertNotNull(simpleType);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithGeneric() {
        Class<List<Integer>> listClass = List.class.getClassGenType(Integer.class);
        JavaType simpleType = TypeFactory.defaultInstance().constructUnsafe(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(int.class);
    }
}