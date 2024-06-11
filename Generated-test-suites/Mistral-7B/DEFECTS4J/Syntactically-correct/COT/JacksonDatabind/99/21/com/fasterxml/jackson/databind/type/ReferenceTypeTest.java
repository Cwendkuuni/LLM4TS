package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = JavaType.construct(String.class, null, null, null);
        JavaType anchorType = JavaType.construct(Integer.class, null, null, null);
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null, refType, anchorType);
        assertEquals(Object.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class, null, null, null);
        JavaType refdType = JavaType.construct(Integer.class, null, null, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(Object.class, referenceType.getRawClass());
        assertEquals(refdType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        assertTrue(referenceType.isReferenceType());
        assertFalse(simpleType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        assertTrue(referenceType.hasContentType());
        assertFalse(simpleType.hasContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        assertEquals(simpleType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        assertEquals(simpleType, referenceType.getReferencedType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;<");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;<");
        expected.append(simpleType.getCanonicalName());
        expected.append(">");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        String expected = "[reference type, class [Ljava.lang.String;<Ljava.lang.String;>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType simpleType1 = JavaType.construct(String.class, null, null, null);
        JavaType simpleType2 = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType1 = JavaType.construct(Object.class, null, null, null, simpleType1, null);
        ReferenceType referenceType2 = JavaType.construct(Object.class, null, null, null, simpleType2, null);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(simpleType1));
        assertFalse(referenceType1.equals(simpleType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
        assertTrue(referenceType1.equals(referenceType2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidBaseType() {
        JavaType baseType = JavaType.construct(int[].class, null, null, null);
        JavaType refdType = JavaType.construct(String.class, null, null, null);
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test
    public void testGetAnchorType() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType simpleType = JavaType.construct(String.class, null, null, null);
        ReferenceType referenceType = JavaType.construct(Object.class, null, null, null, simpleType, null);
        assertFalse(referenceType.isAnchorType());
    }
}