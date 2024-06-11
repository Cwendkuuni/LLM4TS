package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        // Test setReference with a non-null reference
        JavaType reference = new ResolvedRecursiveType(getClass(), null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        resolvedRecursiveType.setReference(reference);
        assertEquals(reference, resolvedRecursiveType.getSelfReferencedType());

        // Test setReference with a null reference
        resolvedRecursiveType.setReference(null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType reference = new ResolvedRecursiveType(getClass(), null);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        resolvedRecursiveType.setReference(reference);
        resolvedRecursiveType.setReference(reference); // Should throw IllegalStateException
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        StringBuilder stringBuilder = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(stringBuilder); // Stub test, no asserts
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        StringBuilder stringBuilder = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(stringBuilder); // Stub test, no asserts
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType contentType = new ResolvedRecursiveType(Object.class, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Class<?> subclass = Object.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        String expectedString = "[recursive type; UNRESOLVED]";
        assertEquals(expectedString, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType reference = new ResolvedRecursiveType(getClass(), null);
        resolvedRecursiveType1.setReference(reference);
        resolvedRecursiveType2.setReference(reference);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

}