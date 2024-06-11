package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertEquals(bindings, resolvedRecursiveType.getTypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(Integer[].class);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(Integer[].class);
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType(Integer[].class);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new SimpleType(Integer[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder expectedSignature = new StringBuilder("[Ljava.lang.Class;");
        StringBuilder actualSignature = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actualSignature);
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new SimpleType(Integer[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder expectedSignature = new StringBuilder("Ljava/lang/Object;[Ljava/lang/Class;");
        StringBuilder actualSignature = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(actualSignature);
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    public void testWithContentType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new SimpleType(Integer.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(javaType));
    }

    @Test
    public void testWithTypeHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        Object handler = new Object();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testToString() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new SimpleType(Integer[].class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);
        String expectedString = "[recursive type; [Ljava.lang.Class;]";
        String actualString = resolvedRecursiveType.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Class<Integer> erasedType1 = Integer.class;
        Class<Integer> erasedType2 = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new SimpleType(Integer[].class);
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings);
        assertEquals(false, resolvedRecursiveType1.equals(resolvedRecursiveType2));

        resolvedRecursiveType1.setReference(javaType);
        resolvedRecursiveType2.setReference(javaType);
        assertEquals(true, resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}