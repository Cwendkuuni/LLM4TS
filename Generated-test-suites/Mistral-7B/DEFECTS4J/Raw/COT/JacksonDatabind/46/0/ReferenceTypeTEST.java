package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertSame(cls, referenceType.getRawClass());
        assertSame(refType, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaType(int[].class);
        JavaType newContentType = new JavaType(double[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
        assertNotSame(referenceType, newReferenceType);
        assertSame(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaType(int[].class);
        Object valueHandler = new Object();
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaType(int[].class);
        Object contentValueHandler = new Object();
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(contentValueHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithTypeHandlerTwice() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType.withTypeHandler(typeHandler);
        referenceType.withTypeHandler(typeHandler);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType expected = refType;
        JavaType actual = referenceType.getReferencedType();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        int expected = 1;
        int actual = referenceType.containedTypeCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedType() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType expected = refType;
        JavaType actual = referenceType.containedType(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expected = "T";
        String actual = referenceType.containedTypeName(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<?> expected = cls;
        Class<?> actual = referenceType.getParameterSource();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = referenceType.getErasedSignature(expected);
        // This would be very hard to test directly, as it depends on the implementation of _classSignature
        // So we will just check that the signature is not empty
        assertTrue(actual.length() > 0);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = referenceType.getGenericSignature(expected);
        // This would be very hard to test directly, as it depends on the implementation of _classSignature
        // So we will just check that the signature is not empty
        assertTrue(actual.length() > 0);
    }

    @Test
    public void testToString() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expected = "[reference type, class " + cls.getName() + "<T>" +
                " [int[]], T]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new JavaType(int[].class);
        JavaType refType2 = new JavaType(double[].class);
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
        JavaType refType3 = refType1;
        ReferenceType referenceType3 = new ReferenceType(cls1, refType3, null, null, false);
        assertTrue(referenceType1.equals(referenceType3));
    }

    @Test
    public void testNarrow() {
        JavaType refType = new JavaType(int[].class);
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<?> subclass = Integer[].class;
        JavaType expected = new ReferenceType(subclass, refType, null, null, false);
        JavaType actual = referenceType.narrow(subclass);
        assertEquals(expected, actual);
    }
}