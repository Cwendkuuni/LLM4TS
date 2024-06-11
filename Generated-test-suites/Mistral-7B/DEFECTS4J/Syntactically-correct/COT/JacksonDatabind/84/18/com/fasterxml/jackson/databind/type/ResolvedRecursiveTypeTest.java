package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Test cases for the ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNotNull(type);
    }

    /**
     * Test setReference method with null argument.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(null);
    }

    /**
     * Test setReference method with non-null argument.
     */
    @Test
    public void testSetReference() {
        JavaType refType = new SimpleType(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(refType);
        JavaType referencedType = type.getSelfReferencedType();
        assertEquals(refType, referencedType);
    }

    /**
     * Test getSelfReferencedType method with uninitialized type.
     */
    @Test
    public void testGetSelfReferencedTypeUninitialized() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());
    }

    /**
     * Test getSelfReferencedType method with initialized type.
     */
    @Test
    public void testGetSelfReferencedTypeInitialized() {
        JavaType refType = new SimpleType(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(refType);
        JavaType referencedType = type.getSelfReferencedType();
        assertEquals(refType, referencedType);
    }

    /**
     * Test getGenericSignature method with null StringBuilder argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetGenericSignatureNullSb() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.getGenericSignature(null);
    }

    /**
     * Test getGenericSignature method with valid StringBuilder argument.
     */
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        refType.getGenericSignature(sb);
        String expected = "[recursive type; " + refType.getTypeName() + "]";
        assertEquals(expected, sb.toString());
    }

    /**
     * Test getErasedSignature method with null StringBuilder argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetErasedSignatureNullSb() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.getErasedSignature(null);
    }

    /**
     * Test getErasedSignature method with valid StringBuilder argument.
     */
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType refType = new SimpleType(Integer.class);
        type.setReference(refType);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        refType.getErasedSignature(sb);
        String expected = refType.getErasedSignature().toString();
        assertEquals(expected, sb.toString());
    }

    /**
     * Test withContentType method.
     */
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new SimpleType(Integer.class);
        ResolvedRecursiveType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    /**
     * Test withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        ResolvedRecursiveType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }

    /**
     * Test withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        ResolvedRecursiveType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }

    /**
     * Test withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        ResolvedRecursiveType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }

    /**
     * Test withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        ResolvedRecursiveType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }

    /**
     * Test withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    /**
     * Test _narrow method.
     * This method is marked as @Deprecated so no test case is needed.
     */

    /**
     * Test refine method with null arguments.
     */
    @Test
    public void testRefineNullArguments() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.refine(null, null, null, new JavaType[0]));
    }

    /**
     * Test refine method with valid arguments and returning null.
     */
    @Test
    public void testRefineNullReturn() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(String.class);
        JavaType[] superInterfaces = new JavaType[0];
        ResolvedRecursiveType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    /**
     * Test refine method with valid arguments and non-null return.
     */
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(String.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(List.class), new SimpleType(Map.class) };
        ResolvedRecursiveType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    /**
     * Test isContainerType method.
     */
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.isContainerType());
    }

    /**
     * Test toString method with uninitialized type.
     */
    @Test
    public void testToStringUninitialized() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, type.toString());
    }

    /**
     * Test toString method with initialized type.
     */
    @Test
    public void testToStringInitialized() {
        JavaType refType = new SimpleType(Integer.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(refType);
        String expected = "[recursive type; " + refType.getTypeName() + "]";
        assertEquals(expected, type.toString());
    }

    /**
     * Test equals method with null object.
     */
    @Test
    public void testEqualsNullObject() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.equals(null));
    }

    /**
     * Test equals method with different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object other = new Object();
        assertFalse(type1.equals(other));
    }

    /**
     * Test equals method with same reference.
     */
    @Test
    public void testEqualsSameReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = type1;
        assertTrue(type1.equals(type2));
    }

    /**
     * Test equals method with self-referenced types.
     */
    @Test
    public void testEqualsSelfReferencedTypes() {
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type1.setReference(refType1);
        type2.setReference(refType2);
        assertTrue(type1.equals(type2));
    }

    /**
     * Test equals method with unresolved self-referenced types.
     */
    @Test
    public void testEqualsUnresolvedSelfReferencedTypes() {
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type1.equals(type2));
    }
}