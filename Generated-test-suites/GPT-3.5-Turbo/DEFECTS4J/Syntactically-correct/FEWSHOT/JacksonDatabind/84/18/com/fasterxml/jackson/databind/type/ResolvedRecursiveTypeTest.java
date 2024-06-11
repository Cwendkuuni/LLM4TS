package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        type.setReference(null);
        assertNull(type.getSelfReferencedType());

        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());

        try {
            type.setReference(new JavaType());
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + new JavaType(), e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder res = type.getGenericSignature(sb);
        assertEquals(sb, res);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder res = type.getErasedSignature(sb);
        assertEquals(sb, res);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType res = type.withContentType(contentType);
        assertEquals(type, res);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType res = type.withTypeHandler(h);
        assertEquals(type, res);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType res = type.withContentTypeHandler(h);
        assertEquals(type, res);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType res = type.withValueHandler(h);
        assertEquals(type, res);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType res = type.withContentValueHandler(h);
        assertEquals(type, res);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType res = type.withStaticTyping();
        assertEquals(type, res);
    }

    @Test
    @Deprecated
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class<?>();
        JavaType res = type._narrow(subclass);
        assertEquals(type, res);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[1];
        JavaType res = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(res);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        boolean res = type.isContainerType();
        assertFalse(res);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        String res = type.toString();
        assertEquals("[recursive type; UNRESOLVED", res);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(null, null);
        assertFalse(type.equals(otherType));
        
        otherType.setReference(new JavaType());
        assertFalse(type.equals(otherType));
        
        type.setReference(new JavaType());
        assertFalse(type.equals(otherType));
        
        otherType.setReference(type.getSelfReferencedType());
        assertTrue(type.equals(otherType));
    }
}