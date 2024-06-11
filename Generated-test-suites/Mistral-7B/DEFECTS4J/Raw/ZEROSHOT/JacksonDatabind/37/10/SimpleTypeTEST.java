package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());

        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings());
        JavaType javaTypeWithBindings = simpleTypeWithBindings.getRawType();
        assertEquals(String.class, javaTypeWithBindings.getRawClass());

        SimpleType simpleTypeWithSuperClass = new SimpleType(stringClass, TypeBindings.emptyBindings(),
                SimpleType.constructUnsafe(Object.class), new JavaType[] {SimpleType.constructUnsafe(Number.class)});
        JavaType javaTypeWithSuperClass = simpleTypeWithSuperClass.getRawType();
        assertEquals(String.class, javaTypeWithSuperClass.getRawClass());
        assertEquals(Object.class, simpleTypeWithSuperClass.getRawType().getSuperClass());
        assertEquals(Number.class, simpleTypeWithSuperClass.getRawType().getSuperInterfaces()[0].getRawClass());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(String.class);

        SimpleType narrowedType = simpleType.narrow(StringBuilder.class);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(StringBuilder.class, narrowedType.getRawType().getRawClass());

        try {
            simpleType.narrow(List.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(new MyStringTypeHandler());
        JavaType javaTypeWithTypeHandler = simpleTypeWithTypeHandler.getRawType();
        assertEquals(String.class, javaTypeWithTypeHandler.getRawClass());
        assertNotNull(simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(new MyStringValueHandler());
        JavaType javaTypeWithValueHandler = simpleTypeWithValueHandler.getRawType();
        assertEquals(String.class, javaTypeWithValueHandler.getRawClass());
        assertNotNull(simpleTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertTrue(simpleTypeWithStaticTyping.isStatic());

        SimpleType simpleTypeWithoutStaticTyping = simpleType.withStaticTyping().withoutStaticTyping();
        assertFalse(simpleTypeWithoutStaticTyping.isStatic());
    }

    @Test
    public void testSimpleTypeWithRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        JavaType refinedType = simpleType.refine(StringBuilder.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        String canonicalName = simpleType.getCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testSimpleTypeErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder erasedSignature = new StringBuilder();
        simpleType.getErasedSignature(erasedSignature);
        assertEquals("String", erasedSignature.toString());
    }

    @Test
    public void testSimpleTypeGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder genericSignature = new StringBuilder();
        simpleType.getGenericSignature(genericSignature);
        assertEquals("[Ljava.lang.String;", genericSignature.toString());
    }

    private static class MyStringTypeHandler {}
    private static class MyStringValueHandler {}
}