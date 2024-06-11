package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertNull(referenceType.getBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertNull(referenceType.getReferencedType());
        assertEquals(String.class, referenceType.getAnchorType().getRawClass());
        assertFalse(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_NullRefdType() {
        ReferenceType.upgradeFrom(SimpleType.stringType(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_InvalidBaseType() {
        ReferenceType.upgradeFrom(TypeFactory.unknownType(), SimpleType.stringType());
    }

    @Test
    public void testUpgradeFrom_ValidBaseType() {
        ReferenceType referenceType = ReferenceType.upgradeFrom(SimpleType.stringType(), SimpleType.booleanType());
        assertEquals(SimpleType.stringType().getRawClass(), referenceType.getBindings().getBoundType(0).getRawClass());
        assertEquals(SimpleType.booleanType().getRawClass(), referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType newType = (ReferenceType) referenceType.withContentType(SimpleType.intType());
        assertEquals(SimpleType.intType().getRawClass(), newType.getReferencedType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType newType = referenceType.withTypeHandler(new Object());
        assertEquals(referenceType.getTypeHandler(), newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType newType = referenceType.withContentTypeHandler(new Object());
        assertEquals(referenceType.getContentType().getTypeHandler(), newType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType newType = referenceType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        assertTrue(referenceType.isReferenceType());
    }
}