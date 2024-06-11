package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        Object handler = new Object();
        ReferenceType referenceTypeWithHandler = referenceType.withTypeHandler(handler);
        assertEquals(referenceType.getRawClass(), referenceTypeWithHandler.getRawClass());
        assertEquals(referenceType.getContentType().getRawClass(), referenceTypeWithHandler.getContentType().getRawClass());
        assertNotSame(referenceType._typeHandler, referenceTypeWithHandler._typeHandler);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String]");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String, type java.lang.Integer]");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        String expected = "[reference type, class java.lang.String<T extends java.lang.Integer>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, refType1, null);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null);
        JavaType referenceType3 = new SimpleType(String.class);
        ReferenceType referenceType4 = new ReferenceType(Integer.class, refType1, null);

        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertTrue(referenceType1.equals(referenceType4));
    }
}