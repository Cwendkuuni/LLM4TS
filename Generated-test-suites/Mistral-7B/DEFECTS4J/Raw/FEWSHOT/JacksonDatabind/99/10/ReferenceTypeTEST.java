package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(List.class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(List.class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(List.class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType referenceType1 = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType1);
        JavaType referenceType2 = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType2);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType2));
        assertTrue(referenceType1.equals(referenceType2));

        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(referenceType1.equals(simpleType));
        assertFalse(simpleType.equals(referenceType1));
    }
}