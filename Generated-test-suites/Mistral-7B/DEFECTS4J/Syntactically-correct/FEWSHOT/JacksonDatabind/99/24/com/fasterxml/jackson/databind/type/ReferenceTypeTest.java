package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(Integer.class, referenceType.getComponentType());
        assertEquals(String.class, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals(Integer.class, referenceType.getComponentType());
        assertEquals(String.class, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals(String.class, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("java.lang.Object", referenceType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("java.lang.ReferenceType<T extends java.lang.Object>[]", referenceType.getGenericSignature().toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertFalse(referenceType.isAnchorType());

        JavaType anchorType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, referenceType);
        assertTrue(anchorType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("[reference type, class [Ljava.lang.String;<T>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType referenceType1 = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));
        JavaType referenceType2 = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class));

        JavaType referenceType3 = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class));

        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType4 = ReferenceType.upgradeFrom(simpleType, TypeFactory.defaultInstance().constructType(Integer.class));

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(simpleType));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
        assertTrue(referenceType1.equals(referenceType4));
    }
}