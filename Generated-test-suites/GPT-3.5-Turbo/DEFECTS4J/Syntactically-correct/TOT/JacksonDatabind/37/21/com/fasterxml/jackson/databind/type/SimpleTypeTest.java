package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testCopyConstructor() {
        TypeBase base = new TypeBase();
        SimpleType simpleType = new SimpleType(base);
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(Integer.class));
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType newType = simpleType.refine(HashMap.class, TypeBindings.emptyBindings(), null, null);
        assertNull(newType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals(String.class.getName(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("L" + String.class.getName() + "<>;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class " + String.class.getName() + "]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(simpleType1, simpleType2);

        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertNotEquals(simpleType1, simpleType3);
    }
}