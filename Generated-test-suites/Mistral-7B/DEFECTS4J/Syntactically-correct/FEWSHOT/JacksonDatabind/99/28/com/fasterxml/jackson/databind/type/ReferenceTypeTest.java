package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType._class);
        assertEquals(Integer.class, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefType() {
        JavaType baseType = new SimpleType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(new Object() {}.getClass());
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testConstruct() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType);
        assertEquals(String.class, referenceType._class);
        assertEquals(Integer.class, referenceType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType referencedType = new SimpleType(Integer.class);
        JavaType newContentType = new SimpleType(Double.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(String.class, newReferenceType._class);
        assertEquals(Double.class, newReferenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, null, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertSame(handler, newReferenceType._typeHandler);
    }

    @Test
    public void testGetContentType() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, null, null, null, false);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, null, null, null, false);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, referenceType, null, null, false);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType, null, null, null, false);
        assertEquals("[reference type, class " + String.class.getName() + "<Integer>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType referencedType1 = new SimpleType(Integer.class);
        JavaType referencedType2 = new SimpleType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType2, null, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        ReferenceType referenceType3 = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, referencedType1, null, null, null, true);
        assertFalse(referenceType1.equals(referenceType3));
        Object other = new Object();
        assertFalse(referenceType1.equals(other));
    }
}