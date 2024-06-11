package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, refType, null, null);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(refType.getRawClass(), referenceType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(new Object() {});
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testNarrow() {
        JavaType intType = JavaType.constructPrimitive(int.class);
        JavaType stringListType = JavaType.constructCollectionType(List.class, String.class);
        JavaType stringArrayType = JavaType.constructArrayType(String[].class);
        ReferenceType referenceType = new ReferenceType(String[].class, stringListType, null, null, false);
        ReferenceType narrowedType = referenceType._narrow(intType.getRawClass());
        assertEquals(intType, narrowedType.getContentType());
        assertEquals(String[].class, narrowedType.getRawClass());
    }

    @Test
    public void testEquals() {
        JavaType intType = JavaType.constructPrimitive(int.class);
        JavaType stringListType = JavaType.constructCollectionType(List.class, String.class);
        JavaType stringArrayType = JavaType.constructArrayType(String[].class);
        ReferenceType referenceType1 = new ReferenceType(String[].class, stringListType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String[].class, stringListType, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(int[].class, stringListType, null, null, false);
        ReferenceType referenceType4 = new ReferenceType(String[].class, intType, null, null, false);
        assertEquals(referenceType1, referenceType2);
        assertNotEquals(referenceType1, referenceType3);
        assertNotEquals(referenceType1, referenceType4);
    }

    @Test
    public void testToString() {
        JavaType intType = JavaType.constructPrimitive(int.class);
        JavaType stringListType = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, stringListType, null, null, false);
        String expected = "[reference type, class [Ljava.lang.String;<T java.util.List<java.lang.String>>]";
        assertEquals(expected, referenceType.toString());
    }
}