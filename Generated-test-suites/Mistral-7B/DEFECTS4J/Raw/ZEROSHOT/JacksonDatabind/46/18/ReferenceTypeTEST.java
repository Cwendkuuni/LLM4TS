package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testReferenceTypeGetContentType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        assertEquals(javaType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getTypeHandler());
        assertNull(newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        Object handler = new Object();
        JavaType newContentType = new SimpleType(Long.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertEquals(handler, newReferenceType.getValueHandler());
        assertEquals(newContentType, newReferenceType.getContentTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(javaType, newReferenceType.getContentType());
        assertNull(newReferenceType.getTypeHandler());
        assertEquals(handler, newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        Object handler = new Object();
        JavaType newContentType = new SimpleType(Long.class);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(javaType, newReferenceType.getContentType());
        assertEquals(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertEquals(handler, newReferenceType.getContentValueHandler());
        assertEquals(newContentType, newReferenceType.getContentTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertEquals(stringClass, newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());
        assertNull(newReferenceType.getValueHandler());
        assertNull(newReferenceType.getTypeHandler());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeBuildCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        String canonicalName = referenceType.buildCanonicalName();
        String expectedCanonicalName = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testReferenceTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        JavaType integerJavaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, integerJavaType, null);
        Class<Integer> integerClass = Integer.class;
        JavaType narrowedType = referenceType._narrow(integerClass);
        assertEquals(integerJavaType, narrowedType);
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testReferenceTypeContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaType, containedType);
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testReferenceTypeGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        StringBuilder stringBuilder = new StringBuilder();
        referenceType.getErasedSignature(stringBuilder);
        String expectedErasedSignature = "Ljava/lang/String;";
        assertEquals(expectedErasedSignature, stringBuilder.toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        StringBuilder stringBuilder = new StringBuilder();
        referenceType.getGenericSignature(stringBuilder);
        String expectedGenericSignature = "Ljava/lang/String<Ljava/lang/Integer;>;";
        assertEquals(expectedGenericSignature, stringBuilder.toString());
    }

    @Test
    public void testReferenceTypeToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null);
        String toString = referenceType.toString();
        String expectedToString = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testReferenceTypeEquals() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType1 = ReferenceType.construct(stringClass, javaType, null);
        ReferenceType referenceType2 = ReferenceType.construct(stringClass, javaType, null);
        assertTrue(referenceType1.equals(referenceType2));
    }
}