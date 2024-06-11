package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());

        JavaType type = parser.parse("java.util.List<java.lang.String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }

    @Test
    public void testParseWithGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());

        JavaType type = parser.parse("java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(Map.class, type.getContentType().getRawClass());
        assertEquals(String.class, type.getContentType().getKeyType().getRawClass());
        assertEquals(Integer.class, type.getContentType().getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());

        parser.parse("InvalidType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidGenericType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());

        parser.parse("java.util.List<InvalidType>");
    }

    @Test
    public void testParseWithEmptyString() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());

        JavaType type = parser.parse("");
        assertNull(type);
    }
}