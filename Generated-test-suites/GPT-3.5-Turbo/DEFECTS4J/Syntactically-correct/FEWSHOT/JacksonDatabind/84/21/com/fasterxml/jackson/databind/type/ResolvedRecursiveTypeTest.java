package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.getTypeParameters().length);
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertFalse(type.withStaticTyping());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());
        type.setReference(new JavaType(String.class));
        assertEquals(new JavaType(String.class), type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type, type.withContentType(new JavaType(String.class)));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type, type.withTypeHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type, type.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.refine(null, null, null, null));
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
        type.setReference(new JavaType(String.class));
        assertEquals("[recursive type; java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type2));
        type1.setReference(new JavaType(String.class));
        assertTrue(type1.equals(type2));
    }
}