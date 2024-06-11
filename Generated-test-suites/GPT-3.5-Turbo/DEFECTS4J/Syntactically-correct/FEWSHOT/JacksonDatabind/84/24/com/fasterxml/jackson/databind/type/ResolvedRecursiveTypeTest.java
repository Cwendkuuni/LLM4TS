package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType(null, null);
        type.setReference(ref);
        assertEquals(ref.getGenericSignature(sb), type.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType(null, null);
        type.setReference(ref);
        assertEquals(ref.getErasedSignature(sb), type.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(null, null);
        assertEquals(type, type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(type, type.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(type, type.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(type, type.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        assertEquals(type, type.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertEquals(type, type.withStaticTyping());
    }

    @Deprecated
    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class subclass = new Class();
        assertEquals(type, type._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class rawType = new Class();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(null, null);
        JavaType[] superInterfaces = {new JavaType(null, null)};
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertEquals(false, type.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null);
        type1.setReference(ref);
        type2.setReference(ref);
        assertEquals(true, type1.equals(type2));
    }
}