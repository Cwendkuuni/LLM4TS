package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        Object newTypeHandler = new Object() {};
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertNotSame(referenceType, newReferenceType);
        assertEquals(cls, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertNull(newReferenceType.getValueHandler());
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerNull() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        referenceType.withTypeHandler(null);
    }

    // Add test cases for other methods as needed

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType expected = refType;
        JavaType actual = referenceType.getReferencedType();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        boolean expected = true;
        boolean actual = referenceType.isReferenceType();

        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        int expected = 1;
        int actual = referenceType.containedTypeCount();

        assertEquals(expected, actual);
    }

    @Test
    public void testContainedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType expected = refType;
        JavaType actual = referenceType.containedType(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expected = "T";
        String actual = referenceType.containedTypeName(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<?> expected = cls;
        Class<?> actual = referenceType.getParameterSource();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T>Lcom.fasterxml.jackson.databind.JavaType;>");
        StringBuilder actual = new StringBuilder();
        referenceType.getGenericSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int.class);
        Object valueHandler1 = new Object() {};
        Object typeHandler1 = new Object() {};
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, valueHandler1, typeHandler1, false);

        Class<String> cls2 = String.class;
        JavaType refType2 = JavaType.construct(int.class);
        Object valueHandler2 = new Object() {};
        Object typeHandler2 = new Object() {};
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, valueHandler2, typeHandler2, false);

        ReferenceType referenceType3 = new ReferenceType(cls1, refType1, valueHandler1, typeHandler1, true);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expected = "[reference type, class [Ljava/lang/String;<T>Lcom.fasterxml.jackson.databind.JavaType;>]";
        String actual = referenceType.toString();

        assertEquals(expected, actual);
    }

    // Add test cases for other methods and edge cases as needed

    // Narrowing test cases

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType expected = new ReferenceType(String[].class, refType, null, null, false);
        JavaType actual = referenceType._narrow(String[].class);

        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowInvalidSubclass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType invalidSubclass = JavaType.construct(double.class);
        referenceType._narrow(invalidSubclass);
    }

    // Add more test cases for narrowing as needed

    // Other test cases

    // Test with null arguments

    @Test(expected = NullPointerException.class)
    public void testConstructNullClass() {
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        ReferenceType.construct(null, refType, valueHandler, typeHandler);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructNullRefType() {
        Class<String> cls = String.class;
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        ReferenceType.construct(cls, null, valueHandler, typeHandler);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructNullValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object typeHandler = new Object() {};
        ReferenceType.construct(cls, refType, null, typeHandler);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructNullTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        ReferenceType.construct(cls, refType, valueHandler, null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithTypeHandlerNullTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, null, false);
        referenceType.withTypeHandler(null);
    }

    // Test with empty input types

    @Test
    public void testWithTypeHandlerEmptyRefType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(JavaType.construct(int.class), int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        JavaType actual = referenceType.withTypeHandler(JavaType.construct(int.class)).getReferencedType();
        JavaType expected = JavaType.construct(int.class);

        assertEquals(expected, actual);
    }

    // Add more test cases for edge cases and exceptions as needed

}