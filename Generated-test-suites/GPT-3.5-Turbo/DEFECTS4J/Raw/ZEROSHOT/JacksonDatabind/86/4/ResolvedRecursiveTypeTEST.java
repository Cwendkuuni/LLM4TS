package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(String.class);
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        assertSame(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringUnresolved() {
        String expected = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringResolved() {
        JavaType ref = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; java.lang.String]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEqualsUnresolved() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, null);
        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref1);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, null);
        other.setReference(ref2);
        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsSameReferencedType() {
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, null);
        other.setReference(ref);
        assertTrue(resolvedRecursiveType.equals(other));
    }
}