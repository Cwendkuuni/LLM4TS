package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, outerType, null);
        ReferenceType referenceTypeWithInnerType = ReferenceType.construct(Object.class, outerType, innerType, null);
        assertEquals(outerType, referenceType.getReferencedType());
        assertNotNull(referenceTypeWithInnerType.getReferencedType());
        assertEquals(innerType, referenceTypeWithInnerType.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Object.class, outerType, null, null, false);
        assertEquals(outerType, referenceType.getReferencedType());
        ReferenceType referenceTypeWithInnerType = new ReferenceType(Object.class, outerType, innerType, null, false);
        assertEquals(innerType, referenceTypeWithInnerType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Object.class, outerType, null, null, false);
        assertTrue(referenceType.isReferenceType());
        ReferenceType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isReferenceType());
    }

    @Test
    public void testToString() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Object.class, outerType, innerType, null, false);
        assertEquals("[reference type, class [I [Ljava.lang.String;]]]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType outerType1 = new SimpleType(int[].class);
        JavaType outerType2 = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType1 = new ReferenceType(Object.class, outerType1, innerType, null, false);
        ReferenceType referenceType2 = new ReferenceType(Object.class, outerType2, innerType, null, false);
        ReferenceType referenceType3 = new ReferenceType(Object.class, outerType1, innerType, null, true);
        ReferenceType referenceType4 = new ReferenceType(Object.class, outerType2, innerType, null, true);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));
    }

    // Add more test cases as needed for other methods in the class.

    @Test
    public void testHashCode() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType1 = new ReferenceType(Object.class, outerType, innerType, null, false);
        ReferenceType referenceType2 = new ReferenceType(Object.class, outerType, innerType, null, false);
        assertEquals(referenceType1.hashCode(), referenceType2.hashCode());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Object.class, outerType, innerType, null, false);
        assertEquals("[Ljava.lang.Object;Ljava/lang/reflect/GenericArrayType;Ljava/lang/String;", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType outerType = new SimpleType(int[].class);
        JavaType innerType = new SimpleType(String.class);
        JavaType narrowedType = new SimpleType(int[].class);
        ReferenceType referenceType = new ReferenceType(Object.class, outerType, innerType, null, false);
        JavaType narrowedReferenceType = referenceType._narrow(narrowedType.getClass());
        assertEquals(narrowedType, narrowedReferenceType);
    }
}