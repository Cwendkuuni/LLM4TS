package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.ClassNotFoundException;
import java.lang.ClassTypeName;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType = ReferenceType.construct(Object.class, javaType);
        assertSame(Object.class, refType.getClass());
        assertSame(javaType, refType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType refType = new ReferenceType(Object.class, null, null, null, false);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        assertEquals(javaType, refType.getReferencedType());
    }

    @Test(expected = ClassCastException.class)
    public void testGetReferencedTypeWrongType() {
        JavaType javaType = new SimpleType(int.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        refType.getReferencedType();
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new SimpleType(String.class);
        Object handler = new Object();
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        ReferenceType newRefType = refType.withTypeHandler(handler);
        assertSame(refType.getContentType(), newRefType.getContentType());
        assertNotSame(refType.getTypeHandler(), newRefType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new SimpleType(String.class);
        Object handler = new Object();
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        ReferenceType newRefType = refType.withContentTypeHandler(handler);
        assertNotSame(refType.getContentType(), newRefType.getContentType());
        assertSame(refType.getTypeHandler(), newRefType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        ReferenceType refType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType newRefType = refType.withValueHandler(handler);
        assertNotSame(refType, newRefType);
        assertNull(refType.getValueHandler());
        assertSame(handler, newRefType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ReferenceType refType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType newRefType = refType.withContentValueHandler(handler);
        assertNotSame(refType, newRefType);
        assertNull(refType.getValueHandler());
        assertSame(handler, newRefType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType refType = new ReferenceType(Object.class, null, null, null, false);
        ReferenceType newRefType = refType.withStaticTyping();
        assertTrue(newRefType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> clazz = new ClassTypeName(int[].class).asRawType();
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType = new ReferenceType(clazz, javaType, null, null, false);
        String expected = "[Ljava.lang.int;Ljava/lang/String;";
        assertEquals(expected, refType.buildCanonicalName());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = new SimpleType(int.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        assertEquals(1, refType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = new SimpleType(int.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        JavaType containedType = refType.containedType(0);
        assertSame(javaType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaType = new SimpleType(int.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        String expected = "I";
        assertEquals(expected, refType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = new SimpleType(int.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        assertEquals(Object.class, refType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new SimpleType(int[].class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        String expected = "[I";
        assertEquals(expected, refType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new SimpleType(int[].class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        StringBuilder sb = new StringBuilder();
        refType.getGenericSignature(sb);
        String expected = "[Ljava.lang.Object;<T:Ljava.lang.Class<[I>;>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        String expected = "[reference type, class java.lang.Object<T:java.lang.String>]";
        assertEquals(expected, refType.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType1 = new ReferenceType(Object.class, javaType, null, null, false);
        ReferenceType refType2 = new ReferenceType(Object.class, javaType, null, null, false);
        assertTrue(refType1.equals(refType2));
    }

    @Test
    public void testNarrow() {
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        JavaType narrowType = refType._narrow(String[].class);
        assertSame(new ReferenceType(String[].class, javaType, null, null, false), narrowType);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowWrongType() {
        JavaType javaType = new SimpleType(String.class);
        ReferenceType refType = new ReferenceType(Object.class, javaType, null, null, false);
        refType._narrow(int.class);
    }
}