package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, refType, null);
        assertNotNull(referenceType);
        assertEquals(String[].class, referenceType.getType());
        assertEquals(refType.getType(), referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotNull(newReferenceType);
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(refType.getType(), newReferenceType.getReferencedType());
        assertEquals(handler, newReferenceType._typeHandler);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        assertEquals(refType.getType(), referenceType.getReferencedType());
        assertNull(referenceType.getReferencedType().getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        assertEquals(refType.getType(), referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        StringBuilder expected = new StringBuilder("class [Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        StringBuilder expected = new StringBuilder("class [Ljava.lang.String;<T extends java.lang.Object>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null);
        String expected = "[reference type, class [Ljava.lang.String;<T extends java.lang.Object>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refTypeA = new SimpleType(String.class);
        JavaType refTypeB = new SimpleType(String.class);
        ReferenceType referenceTypeA = new ReferenceType(String[].class, refTypeA, null);
        ReferenceType referenceTypeB = new ReferenceType(String[].class, refTypeB, null);
        ReferenceType referenceTypeC = new ReferenceType(String[].class, refTypeA, null);
        assertFalse(referenceTypeA.equals(null));
        assertTrue(referenceTypeA.equals(referenceTypeA));
        assertTrue(referenceTypeA.equals(referenceTypeC));
        assertFalse(referenceTypeA.equals(referenceTypeB));
    }
}