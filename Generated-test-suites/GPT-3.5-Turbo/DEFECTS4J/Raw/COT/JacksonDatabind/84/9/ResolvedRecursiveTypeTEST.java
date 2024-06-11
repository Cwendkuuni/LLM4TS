
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder expected = ref.getGenericSignature(new StringBuilder());
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder expected = ref.getErasedSignature(new StringBuilder());
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withTypeHandler(h);
        assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withContentTypeHandler(h);
        assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withValueHandler(h);
        assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withContentValueHandler(h);
        assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType actual = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class<?>();
        JavaType actual = resolvedRecursiveType._narrow(subclass);
        assertEquals(resolvedRecursiveType, actual);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType actual = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(actual);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        String expected = "[recursive type; UNRESOLVED]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType1.setReference(ref1);
        resolvedRecursiveType2.setReference(ref2);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testNotEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType1.setReference(ref1);
        resolvedRecursiveType2.setReference(ref2);
        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(""));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}

