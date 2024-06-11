package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithClassAndReferenceType() {
        JavaType refType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType);

        assertEquals(Integer.class, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testConstructorWithTypeBaseAndReferenceType() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType((TypeBase) baseType, refType);

        assertEquals(baseType.getClass(), referenceType.getClass().getSuperclass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(baseType, referenceType.getBaseType());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, JavaType.construct(String.class, TypeBindings.emptyBindings()), null, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        StringBuilder expected = new StringBuilder("I");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        StringBuilder expected = new StringBuilder("Ljava/lang/Integer;");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, referenceType, null, null, false);

        assertFalse(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidBaseType() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        ReferenceType referenceType = new ReferenceType(baseType, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithMissingReferencedType() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());

        assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(baseType, null));
    }
}