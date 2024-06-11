package com.example.junit; // replace with the actual package name of your test class

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Tests for the ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNotNull(type);
    }

    /**
     * Tests setReference method.
     */
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);
        JavaType expected = refType;
        JavaType actual = type.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    /**
     * Tests getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);
        JavaType expected = refType;
        JavaType actual = type.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    /**
     * Tests getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = type.getGenericSignature(expected);
        fail("Method getGenericSignature should not be called since this class does not implement any generics");
    }

    /**
     * Tests getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = type.getErasedSignature(expected);
        fail("Method getErasedSignature should not be called since this class does not implement any generics");
    }

    /**
     * Tests withContentType method.
     */
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new SimpleType(Integer.class);
        JavaType expected = type;
        JavaType actual = type.withContentType(contentType);
        assertEquals(expected, actual);
    }

    /**
     * Tests withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withTypeHandler(handler);
        assertEquals(expected, actual);
    }

    /**
     * Tests withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withContentTypeHandler(handler);
        assertEquals(expected, actual);
    }

    /**
     * Tests withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withValueHandler(handler);
        assertEquals(expected, actual);
    }

    /**
     * Tests withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withContentValueHandler(handler);
        assertEquals(expected, actual);
    }

    /**
     * Tests withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType expected = type;
        JavaType actual = type.withStaticTyping();
        assertEquals(expected, actual);
    }

    /**
     * Tests _narrow method (deprecated).
     */
    @Deprecated
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = Integer.class;
        JavaType expected = type;
        JavaType actual = type._narrow(subclass);
        assertEquals(expected, actual);
    }

    /**
     * Tests refine method.
     */
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Comparable.class) };
        JavaType expected = null;
        JavaType actual = type.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(expected, actual);
    }

    /**
     * Tests isContainerType method.
     */
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        boolean expected = false;
        boolean actual = type.isContainerType();
        assertEquals(expected, actual);
    }

    /**
     * Tests equals method.
     */
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        assertTrue(type1.equals(type2));

        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        type1.setReference(refType1);
        type2.setReference(refType2);
        assertFalse(type1.equals(type2));

        // Test with different classes
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Integer.class, null);
        assertFalse(type1.equals(type3));

        // Test null reference
        assertFalse(type1.equals(null));
    }

    /**
     * Tests illegal state exception when setting reference multiple times.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType refType1 = new SimpleType(Integer.class);
        type.setReference(refType1);
        JavaType refType2 = new SimpleType(Long.class);
        type.setReference(refType2);
    }
}