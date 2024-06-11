package com.yourpackagename.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new ClassType(String.class);
        resolvedRecursiveType.setReference(refType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(refType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new ClassType(String.class);
        resolvedRecursiveType.setReference(refType);

        assertThrows(IllegalStateException.class, () -> resolvedRecursiveType.setReference(refType));
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[recursive type; ");
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(actual);
        assertNull(resolvedRecursiveType._referencedType);
        assertEquals(expected.toString(), actual.toString());

        JavaType refType = new ClassType(String.class);
        resolvedRecursiveType.setReference(refType);
        StringBuilder expectedWithReference = new StringBuilder("[recursive type; " + refType.getGenericSignature().toString() + "]");
        resolvedRecursiveType.getGenericSignature(actual);
        assertEquals(expectedWithReference.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("");
        StringBuilder actual = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(actual);
        assertNull(resolvedRecursiveType._referencedType);
        assertEquals(expected.toString(), actual.toString());

        JavaType refType = new ClassType(String.class);
        resolvedRecursiveType.setReference(refType);
        StringBuilder expectedWithReference = new StringBuilder(refType.getErasedSignature().toString());
        resolvedRecursiveType.getErasedSignature(actual);
        assertEquals(expectedWithReference.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new ClassType(String.class);

        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // Deprecated method, no test case needed
    }

    @Test
    public void testRefine() {
        // Method returns null, no test case needed
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
        StringBuilder actual = new StringBuilder();
        assertEquals(expected.toString(), resolvedRecursiveType.toString());

        JavaType refType = new ClassType(String.class);
        resolvedRecursiveType.setReference(refType);
        StringBuilder expectedWithReference = new StringBuilder("[recursive type; " + refType.getName() + "]");
        resolvedRecursiveType.toString(actual);
        assertEquals(expectedWithReference.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new ClassType(String.class);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(refType);

        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType3.setReference(refType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
    }
}