package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructors() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getClass());
        assertEquals(Integer.class, referenceType.getContentType().getClass());
        assertSame(refType, referenceType.getReferencedType());

        referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(String.class, referenceType.getClass());
        assertEquals(Integer.class, referenceType.getContentType().getClass());
        assertSame(refType, referenceType.getReferencedType());
    }

    @Test
    public void testConstructorsWithNull() {
        try {
            JavaType baseType = null;
            JavaType refType = new SimpleType(Integer.class);
            ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            JavaType baseType = new SimpleType(String.class);
            JavaType refType = null;
            ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(int[].class);
        JavaType refType = new SimpleType(Integer.class);
        try {
            ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowing() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Long.class);

        ReferenceType referenceType1 = ReferenceType.upgradeFrom(baseType, refType1);
        ReferenceType referenceType2 = ReferenceType.upgradeFrom(referenceType1, refType2);

        assertSame(referenceType2, referenceType2.narrow(String.class));
        assertSame(referenceType2, referenceType2.narrow(String[].class));
        assertSame(referenceType2, referenceType2.narrow(Object.class));
        assertSame(referenceType2, referenceType2.narrow(ReferenceType.class));

        assertFalse(referenceType2.isInstance(new SimpleType(Double.class)));
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(Integer.class, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(Integer.class, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(Integer[].class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        StringBuilder expected = new StringBuilder("java.lang.Object[]");
        assertEquals(expected.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refType = new SimpleType(Integer[].class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        StringBuilder expected = new StringBuilder("java.lang.Object[]<");
        expected.append(refType.getTypeName());
        expected.append(">");
        assertEquals(expected.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNull(referenceType.getAnchorType());

        JavaType baseType2 = new SimpleType(ReferenceType.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType2 = ReferenceType.upgradeFrom(baseType2, refType2);
        assertEquals(referenceType2, referenceType2.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertFalse(referenceType.isAnchorType());

        JavaType baseType2 = new SimpleType(ReferenceType.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType2 = ReferenceType.upgradeFrom(baseType2, refType2);
        assertTrue(referenceType2.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refType = new SimpleType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        StringBuilder expected = new StringBuilder("[reference type, class [Ljava.lang.String;<class java.lang.Integer>]");
        assertEquals(expected.toString(), referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType baseType1 = new SimpleType(String.class);
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType baseType2 = new SimpleType(String.class);
        JavaType refType2 = new SimpleType(Integer.class);

        ReferenceType referenceType1 = ReferenceType.upgradeFrom(baseType1, refType1);
        ReferenceType referenceType2 = ReferenceType.upgradeFrom(baseType2, refType2);

        ReferenceType referenceType3 = new ReferenceType(baseType2, TypeBindings.emptyBindings(), null, null, refType1);
        ReferenceType referenceType4 = new ReferenceType(baseType1, TypeBindings.emptyBindings(), null, null, refType2);

        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType2);
        assertEquals(referenceType1, referenceType3);
        assertEquals(referenceType1, referenceType4);

        assertNotEquals(referenceType1, null);
        assertNotEquals(referenceType1, new Object());
    }
}