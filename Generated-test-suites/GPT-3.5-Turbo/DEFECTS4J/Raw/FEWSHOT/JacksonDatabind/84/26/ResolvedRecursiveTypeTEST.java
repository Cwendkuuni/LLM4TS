package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getReferencedType());
        assertEquals(0, type.hashCode());
        assertFalse(type.isContainerType());
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getReferencedType());

        JavaType reference = new JavaType(String.class, null, null);
        type.setReference(reference);

        assertEquals(reference, type.getReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, null);
        JavaType reference1 = new JavaType(Integer.class, null, null);
        JavaType reference2 = new JavaType(String.class, null, null);
        type.setReference(reference1);
        type.setReference(reference2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Double.class, null);
        JavaType reference = new JavaType(Float.class, null, null);
        type.setReference(reference);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Float;", result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Boolean.class, null);
        JavaType reference = new JavaType(Long.class, null, null);
        type.setReference(reference);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Long;", result.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Character.class, null);
        JavaType reference = new JavaType(Character.class, null, null);
        type.setReference(reference);

        JavaType contentType = new JavaType(Integer.class, null, null);
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Short.class, null);
        JavaType reference = new JavaType(Short.class, null, null);
        type.setReference(reference);

        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Byte.class, null);
        JavaType reference = new JavaType(Byte.class, null, null);
        type.setReference(reference);

        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Void.class, null);
        JavaType reference = new JavaType(Void.class, null, null);
        type.setReference(reference);

        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Void.class, null);
        JavaType reference = new JavaType(Void.class, null, null);
        type.setReference(reference);

        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Double.class, null);
        JavaType reference = new JavaType(Double.class, null, null);
        type.setReference(reference);

        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Float.class, null);
        JavaType reference = new JavaType(Float.class, null, null);
        type.setReference(reference);

        Class<?> subclass = Integer.class;
        JavaType result = type.narrow(subclass);
        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Long.class, null);
        JavaType reference = new JavaType(Long.class, null, null);
        type.setReference(reference);

        Class<?> rawType = Long.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.class, null);
        JavaType reference1 = new JavaType(Integer.class, null, null);
        type1.setReference(reference1);

        assertTrue(type1.equals(type1));

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Double.class, null);
        JavaType reference2 = null;
        type2.setReference(reference2);

        assertFalse(type1.equals(type2));

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Integer.class, null);
        JavaType reference3 = new JavaType(Integer.class, null, null);
        type3.setReference(reference3);

        assertFalse(type1.equals(type3));
    }
}