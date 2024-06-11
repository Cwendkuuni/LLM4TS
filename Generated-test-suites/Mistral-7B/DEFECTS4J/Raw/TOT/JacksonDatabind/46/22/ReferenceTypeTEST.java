package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>(" List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals("List<Integer>", referenceType.toString());
        assertEquals(javaType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeTypeHandlers() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>(" List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        JavaType newTypeHandler = new TypeReference<>("List<Long>") {}.getType();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());

        JavaType newContentTypeHandler = new TypeReference<>("List<Double>") {}.getType();
        ReferenceType newContentReferenceType = newReferenceType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, newContentReferenceType.getContentType().getTypeHandler());

        JavaType newValueHandler = new TypeReference<>("String[]") {}.getType();
        ReferenceType newValueReferenceType = newContentReferenceType.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newValueReferenceType.getValueHandler());

        JavaType newContentValueHandler = new TypeReference<>("Integer[]") {}.getType();
        ReferenceType newContentValueReferenceType = newValueReferenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newContentValueReferenceType.getContentType().getValueHandler());
    }

    @Test
    public void testReferenceTypeStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>(" List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        ReferenceType staticReferenceType = referenceType.withStaticTyping();
        assertTrue(staticReferenceType.isStatic());
        assertEquals(stringClass, staticReferenceType.getRawClass());
        assertEquals("List<Integer>", staticReferenceType.toString());
        assertEquals(javaType, staticReferenceType.getContentType());
    }

    @Test
    public void testReferenceTypeCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>(" List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        assertEquals("[reference type, class java.lang.String<T>]", referenceType.buildCanonicalName());
    }

    @Test
    public void testReferenceTypeNarrowing() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        Class<Integer> intClass = Integer.class;
        JavaType narrowedType = referenceType.narrow(intClass);

        assertEquals("[reference type, class java.lang.Integer]", narrowedType.toString());
        assertEquals(intClass, narrowedType.getRawClass());
        assertEquals(javaType, narrowedType.getContentType());
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypes() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(javaType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testReferenceTypeParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testReferenceTypeErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("[java.lang.String]", sb.toString());
    }

    @Test
    public void testReferenceTypeGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = new TypeReference<>("List<Integer> ") {}.getType();
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[reference type, class java.lang.String<T>]<T extends java.util.List<java.lang.Integer>>", sb.toString());
    }
}