package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(List.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        StringBuilder expected = new StringBuilder("[class java.lang.String]");
        StringBuilder actual = referenceType.getErasedSignature();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String<T>, class java.util.List<T>]");
        StringBuilder actual = referenceType.getGenericSignature();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        String expected = "[reference type, class java.lang.String, class java.util.List<T>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType simpleType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType1 = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        JavaType simpleType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        JavaType referenceType1 = ReferenceType.upgradeFrom(simpleType1, refType1);
        JavaType referenceType2 = ReferenceType.upgradeFrom(simpleType2, refType2);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(simpleType1));
        assertFalse(referenceType1.equals(simpleType2));
        assertFalse(referenceType1.equals(refType1));
        assertFalse(referenceType1.equals(refType2));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
    }

    @Test
    public void testGetAnchorType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertNull(referenceType.getAnchorType());
        JavaType baseType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeParameterType.construct(TypeBase.class, "T"), TypeParameterType.construct(JavaType.class, "U"));
        ReferenceType anchorType = new ReferenceType(baseType, refType);
        ReferenceType referenceTypeWithAnchor = new ReferenceType(baseType, refType);
        referenceTypeWithAnchor.setAnchorType(anchorType);
        assertEquals(anchorType, referenceTypeWithAnchor.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(List.class, TypeParameterType.construct(String.class, "T"));
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertFalse(referenceType.isAnchorType());
        JavaType baseType = TypeFactory.defaultInstance().constructType(ReferenceType.class, TypeParameterType.construct(TypeBase.class, "T"), TypeParameterType.construct(JavaType.class, "U"));
        ReferenceType anchorType = new ReferenceType(baseType, refType);
        ReferenceType referenceTypeWithAnchor = new ReferenceType(baseType, refType);
        referenceTypeWithAnchor.setAnchorType(anchorType);
        assertTrue(referenceTypeWithAnchor.isAnchorType());
    }
}