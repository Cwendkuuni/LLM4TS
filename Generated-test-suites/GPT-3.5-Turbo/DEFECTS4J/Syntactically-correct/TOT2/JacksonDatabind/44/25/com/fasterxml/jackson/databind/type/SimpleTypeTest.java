package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
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
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(Object.class);
        SimpleType narrowType = (SimpleType) baseType._narrow(String.class);
        assertEquals(String.class, narrowType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object handler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Float.class);
        simpleType.withContentType(Boolean.class);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Double.class);
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Long.class);
        SimpleType typedType = simpleType.withStaticTyping();
        assertTrue(typedType.isStatic());
    }

}