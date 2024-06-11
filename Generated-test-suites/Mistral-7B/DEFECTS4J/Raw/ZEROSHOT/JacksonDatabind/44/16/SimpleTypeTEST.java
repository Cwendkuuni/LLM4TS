package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(simpleClass, javaType.getRawClass());
        assertEquals(TypeFactory.defaultInstance().unconstructedType(simpleClass), javaType);

        SimpleType simpleTypeWithBindings = new SimpleType(simpleClass, TypeBindings.emptyBindings());
        assertEquals(simpleClass, simpleTypeWithBindings.getRawType().getRawClass());
        assertEquals(TypeFactory.defaultInstance().unconstructedType(simpleClass), simpleTypeWithBindings.getRawType());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithBindings.getBindings());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> stringClass = String.class;
        Class<?> stringSubClass = StringBuilder.class;

        SimpleType simpleType = new SimpleType(stringClass);

        JavaType narrowedType = simpleType.narrow(stringSubClass);
        assertNotNull(narrowedType);
        assertEquals(StringBuilder.class, narrowedType.getRawClass());

        // Test with unrelated classes
        JavaType unrelatedType = TypeFactory.defaultInstance().constructType(Integer.class);
        assertNull(simpleType.narrow(Integer.class));
        assertNull(unrelatedType.narrow(String.class));
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<?> simpleClass = String.class;
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);

        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType result = simpleType.withContentType(contentType);
        assertNull(result);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> simpleClass = String.class;
        Object handler = new Object() {};

        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType result = simpleType.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(simpleClass, result.getRawClass());
        assertNotNull(result.getTypeHandler());
        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> simpleClass = String.class;
        Object handler = new Object() {};

        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType result = simpleType.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(simpleClass, result.getRawClass());
        assertNotNull(result.getValueHandler());
        assertEquals(handler, result.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> simpleClass = String.class;

        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType result = simpleType.withStaticTyping();
        assertNotNull(result);
        assertEquals(simpleClass, result.getRawClass());
        assertTrue(result.isStatic());

        SimpleType simpleTypeStatic = new SimpleType(simpleClass, true);
        JavaType resultStatic = simpleTypeStatic.withStaticTyping();
        assertNotNull(resultStatic);
        assertEquals(simpleClass, resultStatic.getRawClass());
        assertTrue(resultStatic.isStatic());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<?> simpleClass = String.class;
        Class<?> subClass = StringBuilder.class;

        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType refinedType = simpleType.refine(subClass, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        Class<?> listClass = ArrayList.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);

        String canonicalName = javaType.getCanonicalName();
        assertEquals("java.util.ArrayList", canonicalName);
    }

    @Test
    public void testSimpleTypeGenericSignature() {
        Class<?> listClass = ArrayList.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);

        StringBuilder signature = new StringBuilder();
        javaType.getGenericSignature(signature);
        assertEquals("java.util.ArrayList<E>", signature.toString());
    }

    @Test
    public void testSimpleTypeErasedSignature() {
        Class<?> listClass = ArrayList.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);

        StringBuilder signature = new StringBuilder();
        javaType.getErasedSignature(signature);
        assertEquals("java.util.List", signature.toString());
    }
}