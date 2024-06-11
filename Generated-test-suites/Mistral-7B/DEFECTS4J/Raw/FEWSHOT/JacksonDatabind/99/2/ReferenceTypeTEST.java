package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(String.class, referenceType._class);
        assertEquals(Integer.class, referenceType._referencedType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMissingRefType() {
        JavaType baseType = SimpleType.construct(String.class, null, null);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = SimpleType.construct(int[].class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testConstruct() {
        JavaType javaType = ReferenceType.construct(String[].class, null, null, null, SimpleType.construct(Integer.class, null));

        assertEquals(String[].class, javaType._class);
        assertEquals(Integer.class, javaType._referencedType.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructNullClass() {
        JavaType javaType = ReferenceType.construct(null, null, null, null, SimpleType.construct(Integer.class, null));
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        assertEquals(String.class, referenceType._class);
        assertEquals(Integer.class, referenceType._referencedType.getType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        ReferenceType referenceType = (ReferenceType) javaType.withTypeHandler(null);

        assertEquals(String.class, referenceType._class);
        assertNull(referenceType._referencedType);
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        assertEquals(Integer.class, referenceType.getContentType().getType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        StringBuilder expected = new StringBuilder("[Ljava.lang.String;<T extends java.lang.Number;>T");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType javaType = SimpleType.construct(String.class, null, null);
        JavaType refType = SimpleType.construct(Integer.class, null);
        ReferenceType referenceType = (ReferenceType) javaType.withContentType(refType);

        assertFalse(referenceType.isAnchorType());
    }
}