package com.yourpackagename.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Tests for {@link ResolvedRecursiveType} class
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor with valid arguments
     */
    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(type);
        assertEquals(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    /**
     * Test method setReference with valid JavaType argument
     */
    @Test
    public void testSetReferenceWithValidArgument() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType = new SimpleType("java.lang.String");
        type.setReference(javaType);
        assertEquals(javaType, type.getSelfReferencedType());
    }

    /**
     * Test method setReference with null argument
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithNullArgument() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        type.setReference(null);
    }

    /**
     * Test method getSelfReferencedType when _referencedType is null
     */
    @Test
    public void testGetSelfReferencedTypeWhenReferencedTypeIsNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());
    }

    /**
     * Test method getSelfReferencedType with assigned JavaType
     */
    @Test
    public void testGetSelfReferencedTypeWithAssignedJavaType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType = new SimpleType("java.lang.String");
        type.setReference(javaType);
        assertEquals(javaType, type.getSelfReferencedType());
    }

    /**
     * Test methods getGenericSignature and getErasedSignature with valid arguments
     */
    @Test
    public void testGetGenericSignatureAndGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType = new SimpleType("java.lang.String");
        type.setReference(javaType);
        type.getGenericSignature(sb);
        type.getErasedSignature(sb);

        String expectedGenericSignature = "<T extends java.lang.Object>";
        String expectedErasedSignature = "java.lang.Object";

        assertEquals(expectedGenericSignature, sb.toString());
        sb.setLength(0);
        type.getErasedSignature(sb);

        assertEquals(expectedErasedSignature, sb.toString());
    }

    /**
     * Test methods withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler,
     * withStaticTyping with null arguments
     */
    @Test
    public void testMethodsWithNullArguments() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());

        assertNull(type.withContentType(null));
        assertNull(type.withTypeHandler(null));
        assertNull(type.withContentTypeHandler(null));
        assertNull(type.withValueHandler(null));
        assertNull(type.withContentValueHandler(null));
        assertNull(type.withStaticTyping());
    }

    /**
     * Test methods withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler,
     * withStaticTyping with valid arguments
     */
    @Test
    public void testMethodsWithValidArguments() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType = new SimpleType("java.lang.String");
        Object handler = new Object();

        assertEquals(type, type.withContentType(javaType));
        assertEquals(type, type.withTypeHandler(handler));
        assertEquals(type, type.withContentTypeHandler(handler));
        assertEquals(type, type.withValueHandler(handler));
        assertEquals(type, type.withContentValueHandler(handler));
        assertEquals(type, type.withStaticTyping());
    }

    /**
     * Test method isContainerType with false expected result
     */
    @Test
    public void testIsContainerTypeWithFalseExpectedResult() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertFalse(type.isContainerType());
    }

    /**
     * Test method toString with null _referencedType
     */
    @Test
    public void testToStringWithNullReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        type.toString(sb);
        assertEquals("[recursive type; UNRESOLVED]", sb.toString());
    }

    /**
     * Test method toString with assigned _referencedType
     */
    @Test
    public void testToStringWithAssignedReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType = new SimpleType("java.lang.String");
        type.setReference(javaType);
        StringBuilder sb = new StringBuilder();
        type.toString(sb);
        assertEquals("[recursive type; java.lang.String]", sb.toString());
    }

    /**
     * Test method equals with null o
     */
    @Test
    public void testEqualsWithNullO() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertFalse(type1.equals(null));
    }

    /**
     * Test method equals with different types
     */
    @Test
    public void testEqualsWithDifferentTypes() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        assertFalse(type1.equals(type2));
    }

    /**
     * Test method equals with equal types but different _referencedType
     */
    @Test
    public void testEqualsWithEqualTypesButDifferentReferencedTypes() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType1 = new SimpleType("java.lang.String");
        type1.setReference(javaType1);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType2 = new SimpleType("java.lang.Integer");
        type2.setReference(javaType2);
        assertFalse(type1.equals(type2));
    }

    /**
     * Test method equals with equal types and equal _referencedType
     */
    @Test
    public void testEqualsWithEqualTypesAndEqualReferencedTypes() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType1 = new SimpleType("java.lang.String");
        type1.setReference(javaType1);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType javaType2 = javaType1; // assign the same JavaType instance to _referencedType of type2
        type2.setReference(javaType2);
        assertTrue(type1.equals(type2));
    }
}