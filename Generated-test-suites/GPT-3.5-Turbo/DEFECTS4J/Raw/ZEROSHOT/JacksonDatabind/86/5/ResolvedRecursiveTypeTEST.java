package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals(ref.getGenericSignature(sb), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals(ref.getErasedSignature(sb), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType(Object.class);
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = Object.class;
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
        
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType differentType = new ResolvedRecursiveType(Integer.class, new TypeBindings());

        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        sameType.setReference(ref);
        differentType.setReference(ref);

        assertTrue(type.equals(type));
        assertTrue(type.equals(sameType));
        assertFalse(type.equals(differentType));
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
    }
}