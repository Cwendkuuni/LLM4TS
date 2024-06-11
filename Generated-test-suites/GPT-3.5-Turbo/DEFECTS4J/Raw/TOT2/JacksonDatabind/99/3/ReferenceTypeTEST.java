package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    // Test getters for referenced and anchor types
    @Test
    public void testGetters() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        
        assertEquals(Object.class, referenceType.getContentType().getRawClass());
        assertEquals(Object.class, referenceType.getReferencedType().getRawClass());
        assertNull(referenceType.getAnchorType());
    }

    // Test upgrading from base type
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(Object.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    // Test content value handler
    @Test
    public void testContentValueHandler() {
        Object handler = new Object();

        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        ReferenceType newType = referenceType.withContentValueHandler(handler);

        assertEquals(handler, newType.getReferencedType().getValueHandler());
    }

    // Test static typing
    @Test
    public void testStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        ReferenceType newType = referenceType.withStaticTyping();

        assertTrue(newType.isStaticTyping());
    }

    // Test equality
    @Test
    public void testEquality() {
        ReferenceType referenceType1 = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);

        assertTrue(referenceType1.equals(referenceType2));
    }

    // Test null referenced type for upgradeFrom
    @Test(expected = IllegalArgumentException.class)
    public void testNullUpgradeFrom() {
        JavaType baseType = new SimpleType(Object.class);

        ReferenceType.upgradeFrom(baseType, null);
    }

}