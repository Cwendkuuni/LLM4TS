
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedType.setReference(referencedType);

        assertEquals(referencedType, resolvedType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        JavaType referencedType1 = new JavaType();
        JavaType referencedType2 = new JavaType();
        resolvedType.setReference(referencedType1);
        resolvedType.setReference(referencedType2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new JavaType();
        referencedType.setGenericSignature("genericSignature");
        resolvedType.setReference(referencedType);

        StringBuilder result = resolvedType.getGenericSignature(sb);

        assertEquals("genericSignature", result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new JavaType();
        referencedType.setErasedSignature("erasedSignature");
        resolvedType.setReference(referencedType);

        StringBuilder result = resolvedType.getErasedSignature(sb);

        assertEquals("erasedSignature", result.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();

        JavaType result = resolvedType.withContentType(contentType);

        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = resolvedType.withTypeHandler(handler);

        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = resolvedType.withContentTypeHandler(handler);

        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = resolvedType.withValueHandler(handler);

        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = resolvedType.withContentValueHandler(handler);

        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);

        JavaType result = resolvedType.withStaticTyping();

        assertEquals(resolvedType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;

        JavaType result = resolvedType._narrow(subclass);

        assertEquals(resolvedType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = resolvedType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(null, null);

        boolean result = resolvedType.isContainerType();

        assertFalse(result);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType referencedType1 = new JavaType();
        referencedType1.setRawClass(Object.class);
        resolvedType1.setReference(referencedType1);

        String result1 = resolvedType1.toString();
        assertEquals("[recursive type; java.lang.Object]", result1);

        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(null, null);

        String result2 = resolvedType2.toString();
        assertEquals("[recursive type; UNRESOLVED]", result2);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(null, null);
        JavaType referencedType1 = new JavaType();
        JavaType referencedType2 = new JavaType();
        referencedType1.setRawClass(String.class);
        referencedType2.setRawClass(Object.class);
        resolvedType1.setReference(referencedType1);
        resolvedType2.setReference(referencedType1);

        assertTrue(resolvedType1.equals(resolvedType2));
        assertFalse(resolvedType1.equals(null));
        assertFalse(resolvedType1.equals(new Object()));
        assertFalse(resolvedType1.equals(resolvedType1));
        assertFalse(resolvedType1.equals(resolvedType2));
        assertFalse(resolvedType1.equals(referencedType1));
        assertFalse(resolvedType1.equals(referencedType2));
    }

}

