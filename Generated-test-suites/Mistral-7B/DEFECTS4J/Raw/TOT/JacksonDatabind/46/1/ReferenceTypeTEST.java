package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaType, null, null);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(javaType.hashCode(), referenceType.getContentType().hashCode());
        assertFalse(referenceType.isStatic());
        assertTrue(referenceType instanceof ReferenceType);
    }

    @Test
    public void testReferenceTypeTypeHandlers() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ObjectMapper objectMapper = new ObjectMapper();

        ReferenceType referenceType = ReferenceType.construct(String.class, javaType, null, null);

        JavaType newTypeHandler = objectMapper.getTypeFactory().constructType(Integer.class);
        JavaType newContentTypeHandler = objectMapper.getTypeFactory().constructType(Long.class);

        JavaType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        JavaType newContentType = newReferenceType.withContentTypeHandler(newContentTypeHandler);

        assertEquals(Integer.class, newReferenceType.getContentType().getRawClass());
        assertEquals(Long.class, newContentType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeInvalidTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ObjectMapper objectMapper = new ObjectMapper();

        ReferenceType referenceType = ReferenceType.construct(String.class, javaType, null, null);

        JavaType newTypeHandler = objectMapper.getTypeFactory().constructType(Double.class);

        JavaType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        newReferenceType.withContentTypeHandler(null);
    }

    @Test
    public void testReferenceTypeStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaType, null, null);

        JavaType staticReferenceType = referenceType.withStaticTyping();

        assertEquals(stringClass, staticReferenceType.getRawClass());
        assertTrue(staticReferenceType.isStatic());
        assertFalse(staticReferenceType instanceof ReferenceType);
    }

    @Test
    public void testReferenceTypeCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaType, null, null);

        String canonicalName = referenceType.buildCanonicalName();

        String expectedCanonicalName = "[Ljava.lang.String;Lcom/fasterxml/jackson/core/type/TypeReference$1;@59e3209d";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testReferenceTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaStringArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaIntArrayType = TypeFactory.defaultInstance().constructType(int[].class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringArrayType, null, null);

        JavaType narrowType = referenceType._narrow(int[].class);

        assertEquals(int[].class, narrowType.getRawClass());
    }

    @Test(expected = ClassCastException.class)
    public void testReferenceTypeNarrowInvalidType() {
        Class<String> stringClass = String.class;
        JavaType javaStringArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaIntArrayType = TypeFactory.defaultInstance().constructType(float[].class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringArrayType, null, null);

        JavaType narrowType = referenceType._narrow(int[].class);
    }
}