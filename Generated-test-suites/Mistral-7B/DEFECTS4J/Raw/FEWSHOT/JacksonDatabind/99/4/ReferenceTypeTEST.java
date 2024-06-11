package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, refType);
        assertNotNull(referenceType);
        assertEquals(int[].class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, TypeFactory.defaultInstance().constructType(Integer.class));
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetContentType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        assertEquals(simpleType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testHasContentType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testNarrow() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(int[].class, null, null, simpleType);
        JavaType narrowedType = referenceType._narrow(String[].class);
        assertNotNull(narrowedType);
        assertEquals(int[].class, narrowedType.getRawClass());
        assertEquals(String[].class, narrowedType.getContentType().getRawClass());
    }
}