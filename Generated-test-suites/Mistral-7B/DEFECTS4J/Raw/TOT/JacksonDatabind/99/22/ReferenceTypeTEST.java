package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

/**
 * Test cases for {@link ReferenceType}.
 */
public class ReferenceTypeTest
{
    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValid() {
        JavaType refType = new TypeReference<String>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(ReferenceType.class, referenceType.getClass());
        assertEquals(String.class, referenceType.getContentType().getType());
        assertEquals(String.class, referenceType.getReferencedType().getType());
        assertEquals(false, referenceType.isAnchorType());
    }

    /**
     * Test constructor with null referencedType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullRefdType() {
        JavaType baseType = new SimpleType(String.class, null);
        ReferenceType.upgradeFrom(baseType, null);
    }

    /**
     * Test {@link ReferenceType#withContentType(JavaType)} method.
     */
    @Test
    public void testWithContentType() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        JavaType newContentType = new SimpleType(Integer.class, null);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }

    /**
     * Test {@link ReferenceType#withTypeHandler(Object)} method.
     */
    @Test
    public void testWithTypeHandler() {
        JavaType refType = new TypeReference<String>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        ReferenceType newReferenceType = referenceType.withTypeHandler(valueHandler);
        assertEquals(valueHandler, newReferenceType.getTypeHandler());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }

    /**
     * Test {@link ReferenceType#withContentTypeHandler(Object)} method.
     */
    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        Object valueHandler = new Object();
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newReferenceType.<Object>getContentTypeHandler());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }

    /**
     * Test {@link ReferenceType#withValueHandler(Object)} method.
     */
    @Test
    public void testWithValueHandler() {
        JavaType refType = new TypeReference<String>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }

    /**
     * Test {@link ReferenceType#withContentValueHandler(Object)} method.
     */
    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new TypeReference<Integer>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        Object valueHandler = new Object();
        Object contentValueHandler = new Object();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newReferenceType.<Object>getValueHandler());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }

    /**
     * Test {@link ReferenceType#withStaticTyping()} method.
     */
    @Test
    public void testWithStaticTyping() {
        JavaType refType = new TypeReference<String>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }

    /**
     * Test {@link ReferenceType#refine(Class, TypeBindings, JavaType, JavaType[])} method.
     */
    @Test
    public void testRefine() {
        JavaType refType = new TypeReference<String>() {}.getType();
        SimpleType baseType = new SimpleType(String.class, null);
        JavaType newSuperClass = new SimpleType(List.class, null);
        JavaType[] newSuperInterfaces = new JavaType[] { new SimpleType(Collection.class, null), new SimpleType(Iterable.class, null) };
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        JavaType newReferenceType = referenceType.refine(String[].class, null, newSuperClass, newSuperInterfaces);
        assertEquals(String[].class, newReferenceType.getRawClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(false, newReferenceType.isAnchorType());
    }
}