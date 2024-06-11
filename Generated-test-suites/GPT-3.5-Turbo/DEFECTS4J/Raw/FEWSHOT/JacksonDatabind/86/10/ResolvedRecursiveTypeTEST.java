package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType();
        resolvedRecursiveType = new ResolvedRecursiveType(null, null, referredType);
    }

    @Test
    public void testSetReference() {
        resolvedRecursiveType.setReference(referredType);
        assertEquals(referredType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwiceThrowsException() {
        resolvedRecursiveType.setReference(referredType);
        assertEquals(referredType, resolvedRecursiveType.getSelfReferencedType());
        resolvedRecursiveType.setReference(referredType);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referredType.getGenericSignature(new StringBuilder());
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referredType.getErasedSignature(new StringBuilder());
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withContentType(contentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withTypeHandler(typeHandler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withContentTypeHandler(contentTypeHandler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withValueHandler(valueHandler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withContentValueHandler(contentValueHandler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType.withStaticTyping();
        assertEquals(expected, actual);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = new Class<?>();
        JavaType expected = resolvedRecursiveType;
        JavaType actual = resolvedRecursiveType._narrow(subclass);
        assertEquals(expected, actual);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[]{};
        JavaType expected = null;
        JavaType actual = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainerType() {
        boolean expected = false;
        boolean actual = resolvedRecursiveType.isContainerType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithNullReferencedType() {
        resolvedRecursiveType.setReference(null);
        String expected = "[recursive type; UNRESOLVED]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithNonNullReferencedType() {
        resolvedRecursiveType.setReference(referredType);
        String expected = "[recursive type; " + referredType.getRawClass().getName() + "]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithSameObject() {
        boolean expected = true;
        boolean actual = resolvedRecursiveType.equals(resolvedRecursiveType);
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null, referencedType);
        boolean expected = true;
        boolean actual = resolvedRecursiveType.equals(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        Object other = new Object();
        boolean expected = false;
        boolean actual = resolvedRecursiveType.equals(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithNull() {
        boolean expected = false;
        boolean actual = resolvedRecursiveType.equals(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithDifferentReferencedTypes() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null, new JavaType());
        boolean expected = false;
        boolean actual = resolvedRecursiveType.equals(other);
        assertEquals(expected, actual);
    }
}