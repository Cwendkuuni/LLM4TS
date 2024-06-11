package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    // Basic test cases for each method

    @Test
    public void testConstruct() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = JavaType.construct(String.class, int.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;T");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String expected = "[reference type, class [Ljava/lang/String;<T>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refTypeA = JavaType.construct(String.class, int.class);
        JavaType refTypeB = JavaType.construct(String.class, int.class);
        ReferenceType referenceTypeA = new ReferenceType(String.class, refTypeA, null, null, false);
        ReferenceType referenceTypeB = new ReferenceType(String.class, refTypeB, null, null, false);
        assertTrue(referenceTypeA.equals(referenceTypeA));
        assertTrue(referenceTypeA.equals(referenceTypeB));
        ReferenceType referenceTypeC = new ReferenceType(Object.class, refTypeA, null, null, false);
        assertFalse(referenceTypeA.equals(referenceTypeC));
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullClass() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType.construct(null, refType, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullRefType() {
        ReferenceType.construct(String.class, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullValueHandler() {
        ReferenceType.construct(String.class, JavaType.construct(String.class, int.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullTypeHandler() {
        ReferenceType.construct(String.class, JavaType.construct(String.class, int.class), null, null);
    }

    @Test
    public void testWithTypeHandlerNull() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(null);
        assertNotSame(referenceType, newReferenceType);
        assertNull(newReferenceType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerNull() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        referenceType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerNull() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        referenceType.withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerNull() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        referenceType.withContentValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType.construct(Integer.class, refType, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidRefType() {
        ReferenceType.construct(String.class, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerWithInvalidType() {
        JavaType refType = JavaType.construct(String.class, int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        referenceType.withContentTypeHandler(Object.class);
    }
}