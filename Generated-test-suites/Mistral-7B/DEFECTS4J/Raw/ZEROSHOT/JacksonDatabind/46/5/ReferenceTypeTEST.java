package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        assertEquals(javaType, referenceType.getReferencedType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContentTypeHandler() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType newContentHandler = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType newReferenceType = referenceType.withContentTypeHandler(newContentHandler);
        assertEquals(newContentHandler, newReferenceType.getContentTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testValueHandler() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType newValueHandler = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testTypeHandler() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType newTypeHandler = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testContentValueHandler() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType newContentValueHandler = TypeFactory.defaultInstance().constructType(Integer[].class);
        JavaType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newReferenceType.getContentValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testStaticTyping() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertFalse(referenceType.isStatic());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testCanonicalName() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        String canonicalName = referenceType.buildCanonicalName();
        assertTrue(canonicalName.startsWith("class [Ljava.lang.String;"));
        assertTrue(canonicalName.endsWith("<T>"));
        assertTrue(canonicalName.contains("class " + String.class.getName()));
        assertTrue(canonicalName.contains("class " + javaType.getRawClass().getName()));
    }

    @Test
    public void testNarrow() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType narrowedType = referenceType.narrow(String[].class);
        assertEquals(String[].class, narrowedType.getRawClass());
        assertSame(referenceType, narrowedType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetErasedSignature() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        StringBuilder signatureBuilder = new StringBuilder();
        referenceType.getErasedSignature(signatureBuilder);
        String signature = signatureBuilder.toString();
        assertEquals("[Ljava.lang.String;", signature);
    }

    @Test
    public void testGetGenericSignature() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        StringBuilder signatureBuilder = new StringBuilder();
        referenceType.getGenericSignature(signatureBuilder);
        String signature = signatureBuilder.toString();
        String expectedSignature = "class [" + String.class.getName() + "]java.lang.reflect.GenericArrayType<T>";
        assertEquals(expectedSignature, signature);
    }

    @Test
    public void testToString() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType = ReferenceType.construct(String.class, javaType, null, null);
        String stringRepresentation = referenceType.toString();
        String expectedString = "[reference type, class [" + String.class.getName() + "]java.lang.reflect.TypeVariable<T> java.lang.reflect.Type variable<java.lang.Object>]";
        assertEquals(expectedString, stringRepresentation);
    }

    @Test
    public void testEquals() {
        Type type = new Type[] { String.class, Integer.class }[0];
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(type);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(type);
        JavaType referenceType1 = ReferenceType.construct(String.class, javaType1, null, null);
        JavaType referenceType2 = ReferenceType.construct(String.class, javaType2, null, null);
        JavaType referenceType3 = ReferenceType.construct(String.class, javaType1, null, null);
        JavaType referenceType4 = new SimpleType(String[].class, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType4));
    }
}