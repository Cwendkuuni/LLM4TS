package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @org.junit.Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);
        assertNotNull(refType);
        assertEquals(Integer.class, refType.getRawClass());
        assertEquals(String.class, refType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testWithContentType() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        JavaType contentType = new TypeReference<Double>() {}.getType();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        ReferenceType newRefType = refType.withContentType(contentType);
        assertNotNull(newRefType);
        assertEquals(TypeBase.class, newRefType.getRawClass());
        assertEquals(Double.class, newRefType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType[] superInts = new JavaType[0];
        JavaType refdType = new TypeReference<Integer>() {}.getType();
        ReferenceType refType = ReferenceType.construct(cls, null, superClass, superInts, refdType);
        assertNotNull(refType);
        assertEquals(cls, refType.getRawClass());
        assertEquals(Integer.class, refType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testGetContentType() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        JavaType contentType = refType.getContentType();
        assertNotNull(contentType);
        assertEquals(String.class, contentType.getRawClass());
    }

    @org.junit.Test
    public void testIsReferenceType() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        assertTrue(refType.isReferenceType());
    }

    @org.junit.Test
    public void testHasContentType() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        assertTrue(refType.hasContentType());
    }

    @org.junit.Test
    public void testWithTypeHandler() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ObjectMapper mapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        Object handler = mapper.getTypeFactory().constructType(String.class);
        ReferenceType newRefType = refType.withTypeHandler(handler);
        assertNotNull(newRefType);
        assertEquals(TypeBase.class, newRefType.getRawClass());
        assertEquals(String.class, newRefType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testWithContentTypeHandler() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ObjectMapper mapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        Object handler = mapper.getTypeFactory().constructType(String.class);
        JavaType contentTypeHandler = mapper.getTypeFactory().constructType(Integer.class);
        ReferenceType newRefType = refType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newRefType);
        assertEquals(TypeBase.class, newRefType.getRawClass());
        assertEquals(Integer.class, newRefType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testWithValueHandler() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ObjectMapper mapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        Object handler = mapper.getTypeFactory().constructType(String.class);
        ReferenceType newRefType = refType.withValueHandler(handler);
        assertNotNull(newRefType);
        assertEquals(TypeBase.class, newRefType.getRawClass());
        assertEquals(String.class, newRefType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testWithContentValueHandler() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ObjectMapper mapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        Object handler = mapper.getTypeFactory().constructType(Integer.class);
        JavaType contentType = refType.getContentType();
        JavaType newContentType = refType.withContentValueHandler(handler);
        assertNotNull(newContentType);
        assertEquals(TypeBase.class, newContentType.getRawClass());
        assertEquals(Integer.class, newContentType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testWithStaticTyping() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        ReferenceType newRefType = refType.withStaticTyping();
        assertNotNull(newRefType);
        assertEquals(TypeBase.class, newRefType.getRawClass());
        assertEquals(String.class, newRefType.getContentType().getRawClass());
        assertTrue(newRefType.isStaticTyping());
    }

    @org.junit.Test
    public void testRefine() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType[] superInterfaces = new JavaType[0];
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        JavaType refinedType = refType.refine(TypeBase.class, null, superClass, superInterfaces);
        assertNotNull(refinedType);
        assertEquals(TypeBase.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType().getRawClass());
    }

    @org.junit.Test
    public void testBuildCanonicalName() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType refdType = new TypeReference<String>() {}.getType();
        ReferenceType refType = new ReferenceType(TypeBase.class, baseType);
        String canonicalName = refType.buildCanonicalName();
        String expected = "[reference type, class java.lang.TypeBase<java.lang.Integer<java.lang.String>>]";
        assertEquals(expected, canonicalName);
    }
}