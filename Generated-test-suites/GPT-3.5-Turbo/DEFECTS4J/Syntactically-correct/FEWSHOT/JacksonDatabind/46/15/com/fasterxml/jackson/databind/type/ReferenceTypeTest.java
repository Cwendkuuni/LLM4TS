package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(JavaType.STRING, type.getReferencedType());
        assertFalse(type.isContainerType());
        assertEquals(1, type.containedTypeCount());
        assertEquals(JavaType.STRING, type.containedType(0));
        assertNull(type.containedTypeName(0));
        assertEquals(String.class, type.getParameterSource());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        ReferenceType newType = type.withTypeHandler("handler");
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(JavaType.STRING, newType.getReferencedType());
        assertFalse(newType.isContainerType());
        assertEquals(1, newType.containedTypeCount());
        assertEquals(JavaType.STRING, newType.containedType(0));
        assertNull(newType.containedTypeName(0));
        assertEquals(String.class, newType.getParameterSource());
        assertEquals("handler", newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        ReferenceType newType = type.withContentTypeHandler("handler");
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(JavaType.STRING, newType.getReferencedType());
        assertFalse(newType.isContainerType());
        assertEquals(1, newType.containedTypeCount());
        assertEquals(JavaType.STRING, newType.containedType(0));
        assertNull(newType.containedTypeName(0));
        assertEquals(String.class, newType.getParameterSource());
        assertEquals("handler", newType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        ReferenceType newType = type.withValueHandler("handler");
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(JavaType.STRING, newType.getReferencedType());
        assertFalse(newType.isContainerType());
        assertEquals(1, newType.containedTypeCount());
        assertEquals(JavaType.STRING, newType.containedType(0));
        assertNull(newType.containedTypeName(0));
        assertEquals(String.class, newType.getParameterSource());
        assertEquals("handler", newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        ReferenceType newType = type.withContentValueHandler("handler");
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(JavaType.STRING, newType.getReferencedType());
        assertFalse(newType.isContainerType());
        assertEquals(1, newType.containedTypeCount());
        assertEquals(JavaType.STRING, newType.containedType(0));
        assertNull(newType.containedTypeName(0));
        assertEquals(String.class, newType.getParameterSource());
        assertEquals("handler", newType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        ReferenceType newType = type.withStaticTyping();
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(JavaType.STRING, newType.getReferencedType());
        assertFalse(newType.isContainerType());
        assertEquals(1, newType.containedTypeCount());
        assertEquals(JavaType.STRING, newType.containedType(0));
        assertNull(newType.containedTypeName(0));
        assertEquals(String.class, newType.getParameterSource());
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertEquals(JavaType.STRING, type.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertTrue(type.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertEquals(1, type.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertEquals(JavaType.STRING, type.containedType(0));
        assertNull(type.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertNull(type.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        assertEquals(String.class, type.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;<Ljava/lang/String;>;", sb.toString());
    }
}