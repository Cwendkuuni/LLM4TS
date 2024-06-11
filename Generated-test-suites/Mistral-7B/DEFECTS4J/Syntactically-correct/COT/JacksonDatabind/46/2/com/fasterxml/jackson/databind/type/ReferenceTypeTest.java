package com.example.junit.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object oldTypeHandler = new Object();
        Object newTypeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, oldTypeHandler, false);

        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertNotSame(oldTypeHandler, newReferenceType.getTypeHandler());
        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertSame(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType.getValueHandler());
        assertSame(newTypeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object oldTypeHandler = new Object();
        Object newTypeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, oldTypeHandler, false);

        JavaType newContentType = JavaType.construct(long[].class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newTypeHandler);

        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertSame(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType.getValueHandler());
        assertSame(oldTypeHandler, newReferenceType.getTypeHandler());
        assertSame(newTypeHandler, newReferenceType.getContentTypeHandler());
        assertFalse(newReferenceType.isStatic());

        JavaType newContentTypeInstance = newContentType.noDefaultTyping();
        assertSame(newContentTypeInstance, newReferenceType.getContentType().noDefaultTyping());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object oldValueHandler = new Object();
        Object newValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, oldValueHandler, null, false);

        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertSame(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(oldValueHandler, newReferenceType.getValueHandler());
        assertSame(newValueHandler, newReferenceType.getValueHandler());
        assertSame(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertSame(referenceType.getContentTypeHandler(), newReferenceType.getContentTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object oldContentValueHandler = new Object();
        Object newContentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        JavaType newContentType = JavaType.construct(long[].class);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);

        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertSame(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(newContentValueHandler, newReferenceType.getContentValueHandler());
        assertSame(oldContentValueHandler, newReferenceType.getContentValueHandler());
        assertSame(referenceType.getValueHandler(), newReferenceType.getValueHandler());
        assertSame(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertSame(referenceType.getContentTypeHandler(), newReferenceType.getContentTypeHandler());
        assertFalse(newReferenceType.isStatic());

        JavaType newContentTypeInstance = newContentType.noDefaultTyping();
        assertSame(newContentTypeInstance, newReferenceType.getContentType().noDefaultTyping());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertFalse(referenceType.isStatic());
        assertTrue(newReferenceType.isStatic());
        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertSame(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType.getValueHandler());
        assertSame(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        referenceType.narrow(Object.class);
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType containedType = referenceType.containedType(0);

        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String containedTypeName = referenceType.containedTypeName(0);

        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        StringBuilder signature = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        referenceType.getErasedSignature(signature);

        assertEquals("Ljava/lang/String;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        StringBuilder signature = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        referenceType.getGenericSignature(signature);

        assertEquals("Ljava/lang/String;<T extends java.lang.Object;>", signature.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String stringRepresentation = referenceType.toString();

        assertTrue(stringRepresentation.contains("[reference type"));
        assertTrue(stringRepresentation.contains("class " + cls.getName()));
        assertTrue(stringRepresentation.contains("<" + refType.toCanonical() + ">]"));
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int[].class);
        Object valueHandler1 = new Object();
        Object typeHandler1 = new Object();
        boolean asStatic1 = false;

        Class<?> cls2 = String.class;
        JavaType refType2 = JavaType.construct(long[].class);
        Object valueHandler2 = new Object();
        Object typeHandler2 = new Object();
        boolean asStatic2 = false;

        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, valueHandler1, typeHandler1, asStatic1);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, valueHandler2, typeHandler2, asStatic2);

        assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls1, refType1, valueHandler1, typeHandler1, asStatic1);

        assertTrue(referenceType1.equals(referenceType3));
    }
}