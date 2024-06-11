package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = ReferenceType.construct(String[].class, javaType);
        assertNotNull(referenceType);
        assertSame(String[].class, referenceType.getRawClass());
        assertEquals(IntegerType.getInstance(), referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotNull(newReferenceType);
        assertSame(referenceType._class, newReferenceType._class);
        assertNotSame(referenceType._typeHandler, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertNotNull(newReferenceType);
        assertSame(referenceType._class, newReferenceType._class);
        assertNotSame(referenceType._referencedType._typeHandler, newReferenceType._referencedType._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotNull(newReferenceType);
        assertSame(referenceType._class, newReferenceType._class);
        assertNotSame(referenceType._valueHandler, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotNull(newReferenceType);
        assertSame(referenceType._class, newReferenceType._class);
        assertNotSame(referenceType._referencedType._valueHandler, newReferenceType._referencedType._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotNull(newReferenceType);
        assertTrue(newReferenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals("[Ljava.lang.String;Lcom/fasterxml/jackson/databind/type/SimpleType;", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType javaTypeReference = JavaType.construct(String.class, IntegerType.getInstance());
        JavaType javaTypeSubClass = JavaType.construct(String[].class, javaTypeReference);
        ReferenceType referenceType = new ReferenceType(String[].class, javaTypeReference, null, null, false);
        JavaType narrowType = referenceType._narrow(String[].class);
        assertNotNull(narrowType);
        assertSame(javaTypeSubClass, narrowType);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;<T extends java.lang.Object>Lcom.fasterxml.jackson.databind.type.SimpleType;T>");
        StringBuilder actual = new StringBuilder();
        referenceType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals("[reference type, class [Ljava.lang.String;, class java.lang.Integer, referenced type class java.lang.Integer]", referenceType.toString());
    }

    @Test
    public void testEquality() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        JavaType javaType2 = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String[].class, javaType2, null, null, false);
        assertTrue(referenceType.equals(referenceType2));
    }

    @Test
    public void testEqualsNull() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEqualsDifferentClasses() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        JavaType javaType2 = JavaType.construct(Long.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String[].class, javaType2, null, null, false);
        assertFalse(referenceType.equals(referenceType2));
    }

    @Test
    public void testEqualsSameInstance() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        JavaType expected = javaType;
        JavaType actual = referenceType.containedType(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = JavaType.construct(String.class, IntegerType.getInstance());
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals(String[].class, referenceType.getParameterSource());
    }
}