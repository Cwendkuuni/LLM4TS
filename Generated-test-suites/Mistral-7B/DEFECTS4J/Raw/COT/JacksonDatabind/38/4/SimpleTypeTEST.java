package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType originalSimpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copySimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType, copySimpleType);
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        Class<Map<String, String>> mapClass = Map.class;
        SimpleType simpleType = SimpleType.construct(mapClass);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        Class<List<String>> collectionClass = List.class;
        SimpleType simpleType = SimpleType.construct(collectionClass);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        Class<String[]> arrayClass = String[].class;
        SimpleType simpleType = SimpleType.construct(arrayClass);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testIsContainerType() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listClass = List.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(listClass, String.class);
        assertEquals("java.util.List<java.lang.String>", listType.toString());
    }

    @Test
    public void testToString() {
        Class<List<String>> listClass = List.class;
        JavaType listType = TypeFactory.defaultInstance().constructType(listClass, String.class);
        assertEquals("[simple type, class [Ljava.util.List;]", listType.toString());
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        Class<String> cls2 = String.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
    }

    
}