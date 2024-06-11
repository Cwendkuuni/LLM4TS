package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Class<?> cls = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(baseType, referenceType.getBaseType());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonJavaType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, TypeFactory.defaultInstance().constructTypeValue(int[].class));
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("[I", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/Integer;>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertEquals("[Ljava/lang/Integer;<T:Ljava/lang/Integer;>", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType subClass = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType, null, null, new JavaType[0], null, null, null, null, false);
        JavaType narrowType = referenceType._narrow(subClass);
        assertEquals(subClass, narrowType);
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        assertEquals("[reference type, class [Ljava/lang/Integer;<T:Ljava/lang/Integer;>] of type " + refType.toString() + "]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType2, null, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType1, null, null, null, true);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referenceType3));
    }
}