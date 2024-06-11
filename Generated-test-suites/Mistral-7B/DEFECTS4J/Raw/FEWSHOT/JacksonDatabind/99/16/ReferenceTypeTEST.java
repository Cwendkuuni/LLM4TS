package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType refType = TypeFactory.defaultInstance().constructValueType(List.class, "java.util.List<java.lang.String>");
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(List.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType refType = TypeFactory.defaultInstance().constructValueType(List.class, "java.util.List<java.lang.String>");
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType);
        assertTrue(referenceType.isReferenceType());

        simpleType = TypeFactory.defaultInstance().constructValueType(int.class, "int");
        assertFalse(simpleType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType);
        assertTrue(referenceType.hasContentType());

        simpleType = TypeFactory.defaultInstance().constructValueType(int.class, "int");
        assertFalse(simpleType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType refType = TypeFactory.defaultInstance().constructValueType(List.class, "java.util.List<java.lang.String>");
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType refType = TypeFactory.defaultInstance().constructValueType(List.class, "java.util.List<java.lang.String>");
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertNull(referenceType.getAnchorType());

        JavaType anchorType = TypeFactory.defaultInstance().constructValueType(Map.class, "java.util.Map<java.lang.Integer, java.lang.String>");
        JavaType referenceTypeAnchored = new ReferenceType(anchorType, refType);
        assertEquals(anchorType, referenceTypeAnchored.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType refType = TypeFactory.defaultInstance().constructValueType(List.class, "java.util.List<java.lang.String>");
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertFalse(referenceType.isAnchorType());

        JavaType anchorType = TypeFactory.defaultInstance().constructValueType(Map.class, "java.util.Map<java.lang.Integer, java.lang.String>");
        JavaType referenceTypeAnchored = new ReferenceType(anchorType, refType);
        assertTrue(referenceTypeAnchored.isAnchorType());
    }

    @Test
    public void testEquals() {
        JavaType simpleType1 = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType simpleType2 = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType referenceType1 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType1);
        JavaType referenceType2 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType2);
        assertTrue(referenceType1.equals(referenceType2));

        JavaType simpleType3 = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType referenceType3 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType3);
        assertFalse(referenceType1.equals(referenceType3));

        JavaType referenceType4 = new ReferenceType(referenceType1, null);
        assertTrue(referenceType1.equals(referenceType4));

        JavaType referenceType5 = new ReferenceType(referenceType2, null);
        assertTrue(referenceType1.equals(referenceType5));

        JavaType referenceType6 = new ReferenceType(referenceType3, null);
        assertFalse(referenceType1.equals(referenceType6));
    }

    @Test
    public void testToString() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType refType = TypeFactory.defaultInstance().constructValueType(List.class, "java.util.List<java.lang.String>");
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals("[reference type, class [Ljava.lang.String;<java.util.List<java.lang.String>>]", referenceType.toString());
    }

    @Test
    public void testNarrow() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(String.class, "java.lang.String");
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType);
        JavaType narrowedType = referenceType._narrow(String.class);
        assertEquals(simpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        JavaType simpleType = TypeFactory.defaultInstance().constructValueType(int.class, "int");
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, null, simpleType);
        referenceType._narrow(List.class);
    }
}