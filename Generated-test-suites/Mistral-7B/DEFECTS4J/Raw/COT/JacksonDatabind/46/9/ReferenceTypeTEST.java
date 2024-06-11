package com.example.junit.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, javaType, null, null);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(javaType.getRawClass(), referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        assertEquals(javaType, referenceType.getReferencedType());
        assertEquals(true, referenceType.isReferenceType());
    }

    @Test(expected = AssertionError.class)
    public void testGetReferencedTypeNull() {
        Class<String> cls = String.class;
        ReferenceType referenceType = new ReferenceType(cls, null, null, null, false);
        referenceType.getReferencedType();
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotNull(newReferenceType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test(expected = AssertionError.class)
    public void testWithTypeHandlerNull() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(null);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertNotNull(newReferenceType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertEquals(handler, newReferenceType.getContentTypeHandler());
    }

    @Test(expected = AssertionError.class)
    public void testWithContentTypeHandlerNull() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotNull(newReferenceType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(referenceType.getValueHandler(), newReferenceType.getValueHandler());
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test(expected = AssertionError.class)
    public void testWithValueHandlerNull() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(null);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotNull(newReferenceType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(referenceType.getValueHandler(), newReferenceType.getValueHandler());
        assertEquals(referenceType.getContentTypeHandler(), newReferenceType.getContentTypeHandler());
        assertEquals(handler, newReferenceType.getContentValueHandler());
    }

    @Test(expected = AssertionError.class)
    public void testWithContentValueHandlerNull() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotNull(newReferenceType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        assertEquals("class java.lang.String<T>", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        JavaType narrowedType = referenceType.narrow(String[].class);
        assertNotNull(narrowedType);
        assertEquals(String[].class, narrowedType.getRawClass());
        assertEquals(javaType, narrowedType.getContentType());
    }

    @Test(expected = AssertionError.class)
    public void testNarrowInvalidSubclass() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        JavaType narrowedType = referenceType.narrow(Object.class);
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        int containedTypeCount = referenceType.containedTypeCount();
        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testContainedType() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaType, containedType);
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        StringBuilder expected = new StringBuilder("class java.lang.String<T>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType javaType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, javaType, null, null, false);
        String expected = "[reference type, class java.lang.String<int>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType javaType1 = JavaType.construct(int.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, javaType1, null, null, false);

        Class<String> cls2 = String.class;
        JavaType javaType2 = JavaType.construct(int.class);
        ReferenceType referenceType2 = new ReferenceType(cls2, javaType2, null, null, false);

        Class<String> cls3 = String.class;
        JavaType javaType3 = JavaType.construct(double.class);
        ReferenceType referenceType3 = new ReferenceType(cls3, javaType3, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(referenceType3));
    }
}