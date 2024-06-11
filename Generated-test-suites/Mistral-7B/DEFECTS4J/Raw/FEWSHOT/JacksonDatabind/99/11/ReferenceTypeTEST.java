package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(List.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertFalse(referenceType.isAnchorType());
    }
}