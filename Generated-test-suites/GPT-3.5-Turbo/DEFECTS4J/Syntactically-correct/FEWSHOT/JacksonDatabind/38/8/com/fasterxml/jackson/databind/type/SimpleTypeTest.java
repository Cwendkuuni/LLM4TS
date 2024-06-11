package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.JavaType;

import java.util.Map;
import java.util.Collection;

import org.junit.Test;

public class SimpleTypeTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        assertFalse(type.hasGenericType());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        assertEquals(type, type.withContentType(SimpleType.constructUnsafe(String.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeException() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        type.withContentType(SimpleType.construct(Map.class));
    }

    @Test
    public void testWithGenericType() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        assertEquals(type, type.withTypeHandler(new Object()));
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        assertEquals(type, type.withContentValueHandler(new Object()));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        assertEquals(type, type.withStaticTyping());
    }
}