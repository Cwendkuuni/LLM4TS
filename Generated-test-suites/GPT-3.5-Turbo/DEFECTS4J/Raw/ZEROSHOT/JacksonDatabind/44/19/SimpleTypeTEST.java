package com.fasterxml.jackson.databind.type;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(Object.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, newType);
        assertSame(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertFalse(simpleType.isStaticTyping());
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, new TypeBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/TypeBase;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/TypeBase<Ljava/lang/Object;>;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.TypeBase]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType sameType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType);
        assertEquals(simpleType, sameType);
        assertNotEquals(simpleType, null);
        assertNotEquals(simpleType, "string");
    }
}