package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType.getClass(), referenceType._referencedType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(Integer.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newReferenceType._typeHandler);
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testNarrow() {
        JavaType refType = new SimpleType(Integer.class);
        JavaType subclassType = new SimpleType(Short.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        JavaType narrowedType = referenceType._narrow(subclassType.getClass());
        assertEquals(subclassType, narrowedType);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        String expected = "[reference type, class java.lang.String]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        String expected = "[reference type, class java.lang.String<T>]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null);
        String expected = "[reference type, class java.lang.String<T>]";
        String result = referenceType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, refType1, null);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null);
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType2));
        ReferenceType referenceType3 = new ReferenceType(String.class, refType1, null);
        assertTrue(referenceType1.equals(referenceType3));
        ReferenceType referenceType4 = new ReferenceType(Integer.class, refType1, null);
        assertFalse(referenceType1.equals(referenceType4));
    }
}