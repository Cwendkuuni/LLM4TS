package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, null, null, null, refType);
        assertNotNull(referenceType);
        assertSame(refType, referenceType.getReferencedType());
    }

    @Test
    public void testConstructWithAnchorType() {
        JavaType refType = new JavaType(String.class);
        JavaType anchorType = new JavaType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, null, null, null, refType, anchorType);
        assertNotNull(referenceType);
        assertSame(refType, referenceType.getReferencedType());
        assertSame(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNotNull(referenceType);
        assertSame(refType, referenceType.getReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = new JavaType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType invalidBaseType = new JavaType(Object.class);
        JavaType refType = new JavaType(Integer.class);
        ReferenceType.upgradeFrom(invalidBaseType, refType);
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, null, null, null, false);
        JavaType newContentType = new JavaType(Long.class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertNotNull(newReferenceType);
        assertSame(newContentType, newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getReferencedType());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, null, null, null, false);
        assertSame(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType baseType = new JavaType(String[].class);
        JavaType refType = new JavaType(Integer[].class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, null, null, null, false);
        StringBuilder expected = new StringBuilder("[[I");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType baseType = new JavaType(String[].class);
        JavaType refType = new JavaType(Integer[].class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, null, null, null, false);
        StringBuilder expected = new StringBuilder("java.lang.String[]<java.lang.Integer>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        JavaType anchorType = new JavaType(Long.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, anchorType, null, null, false);
        assertSame(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(Integer.class);
        JavaType anchorType = new JavaType(Long.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, null, refType, anchorType, null, null, false);
        assertFalse(referenceType.isAnchorType());
    }
}