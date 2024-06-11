package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("com/fasterxml/jackson/databind/type/SimpleType", sb.toString());
        sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;", sb.toString());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("com/fasterxml/jackson/databind/type/SimpleType", sb.toString());
        sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;", sb.toString());
    }

    @Test
    public void testGetTypeParameters() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertFalse(simpleType.isContainerType());
        assertEquals(0, simpleType.getTypeParameters().length);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        SimpleType newType = simpleType.withContentType(SimpleType.constructUnsafe(String.class));
        assertFalse(newType.isContainerType());
        assertEquals(0, newType.getTypeParameters().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentType(SimpleType.constructUnsafe(String.class));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertFalse(newType.isContainerType());
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("com/fasterxml/jackson/databind/type/SimpleType", sb.toString());
        sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;", sb.toString());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType newType = simpleType.refine(String.class, null, null, null);
        assertFalse(newType.isContainerType());
    }
}