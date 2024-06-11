package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = {};
        JavaType refType = IntegerType.getInstance();
        ReferenceType expected = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        ReferenceType actual = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.construct(Integer.class);
        JavaType refdType = IntegerType.getInstance();
        ReferenceType expected = new ReferenceType((TypeBase) baseType, refdType);
        ReferenceType actual = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefdType() {
        JavaType baseType = SimpleType.construct(Integer.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpgradeFromNonTypeBase() {
        JavaType refdType = IntegerType.getInstance();
        ReferenceType actual = ReferenceType.upgradeFrom(String.class, refdType);
    }

    @Test
    public void testGetContentType() {
        JavaType refdType = IntegerType.getInstance();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        assertEquals(refdType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refdType = IntegerType.getInstance();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        assertEquals(refdType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType refdType = IntegerType.getInstance();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refdType = IntegerType.getInstance();
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        StringBuilder expected = new StringBuilder();
        expected.append("[reference type, class ");
        expected.append(cls.getName());
        expected.append("]");

        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        StringBuilder expected = new StringBuilder();
        expected.append("[reference type, class ");
        expected.append(cls.getName());
        expected.append("]");

        JavaType refdType = IntegerType.getInstance();
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = String.class;
        JavaType refdType = IntegerType.getInstance();
        ReferenceType expected = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refdType, null, null, null, false);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refdType, referenceType, null, null, false);
        assertEquals(expected, referenceType);
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = String.class;
        JavaType refdType = IntegerType.getInstance();
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refdType, referenceType, null, null, false);
        assertTrue(referenceType.isAnchorType());
    }
}