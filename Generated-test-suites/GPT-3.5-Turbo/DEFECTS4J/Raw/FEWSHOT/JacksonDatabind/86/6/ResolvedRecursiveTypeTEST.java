package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType1 = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        JavaType javaType2 = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        assertEquals(javaType.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        assertEquals(javaType.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(javaType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(javaType));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(javaType));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(javaType));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(javaType));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(String.class));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertNull(resolvedRecursiveType.refine(String.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(null, 0, null, null, null, null, null, false, null, null);
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType2.setReference(javaType);
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(resolvedRecursiveType2));
    }
}