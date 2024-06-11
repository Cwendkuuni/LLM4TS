package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, stringType, null, null);
        assertEquals("class String", referenceType.getRawClass().getName());
        assertEquals(stringType.hashCode(), referenceType.getContentType().hashCode());
        assertEquals("class java.lang.String", referenceType.toString());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        Object handler = new Object();
        ReferenceType referenceTypeWithHandler = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, referenceTypeWithHandler);
        assertEquals(handler, referenceTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        Object handler = new Object();
        JavaType intTypeWithHandler = intType.withTypeHandler(handler);
        ReferenceType referenceTypeWithContentTypeHandler = referenceType.withContentTypeHandler(intTypeWithHandler);
        assertNotSame(referenceType, referenceTypeWithContentTypeHandler);
        assertEquals(intTypeWithHandler, referenceTypeWithContentTypeHandler.getContentType());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        Object handler = new Object();
        ReferenceType referenceTypeWithValueHandler = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, referenceTypeWithValueHandler);
        assertEquals(handler, referenceTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        Object handler = new Object();
        JavaType intTypeWithHandler = intType.withValueHandler(handler);
        ReferenceType referenceTypeWithContentValueHandler = referenceType.withContentValueHandler(intTypeWithHandler);
        assertNotSame(referenceType, referenceTypeWithContentValueHandler);
        assertEquals(intTypeWithHandler, referenceTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        ReferenceType staticReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, staticReferenceType);
        assertTrue(staticReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertEquals(stringType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testReferenceTypeContainedType() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(stringType, containedType);
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testReferenceTypeGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, stringType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.lang.String;<T extends java.lang.Object>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }
}