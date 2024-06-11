package com.fasterxml.jackson.databind.type;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory typeFactory = new TypeFactory() {};
    private TypeParser typeParser = new TypeParser(typeFactory);

    @Test
    public void testParseEmptyString() {
        JavaType result = typeParser.parse("");
        assertNull(result);
    }

    @Test
    public void testParseSingleClass() {
        JavaType result = typeParser.parse("String");
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics() {
        JavaType result = typeParser.parse("List<Integer>");
        assertEquals(List.class, result.getRawClass());
        assertEquals(Integer.class, result.getTypeParams()[0].getRawClass());
    }

    @Test
    public void testParseClassWithGenericsAndMultipleTypeParams() {
        JavaType result = typeParser.parse("Map<String, List<Integer>>");
        assertEquals(Map.class, result.getRawClass());
        assertEquals(String.class, result.getTypeParams()[0].getRawClass());
        assertEquals(List.class, result.getTypeParams()[1].getRawClass());
        assertEquals(Integer.class, result.getTypeParams()[1].getTypeParams()[0].getRawClass());
    }

    @Test
    public void testParseClassWithGenericsAndMissingTypeParams() {
        assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("Map<String,>");
        });
    }

    @Test
    public void testParseClassWithInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("List<Integer$>");
        });
    }

    @Test
    public void testParseClassWithInvalidOrderOfGenerics() {
        assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("Map<List<Integer>, String>");
        });
    }

    @Test
    public void testParseSingleClassWithFactory() {
        JavaType result = typeParser.withFactory(new TypeFactory() {}).parse("String");
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testParseClassWithGenericsAndFactory() {
        JavaType result = typeParser.withFactory(new TypeFactory() {}).parse("List<Integer>");
        assertEquals(List.class, result.getRawClass());
        assertEquals(Integer.class, result.getTypeParams()[0].getRawClass());
    }

    @Test
    public void testParseClassWithGenericsAndMultipleTypeParamsAndFactory() {
        JavaType result = typeParser.withFactory(new TypeFactory() {}).parse("Map<String, List<Integer>>");
        assertEquals(Map.class, result.getRawClass());
        assertEquals(String.class, result.getTypeParams()[0].getRawClass());
        assertEquals(List.class, result.getTypeParams()[1].getRawClass());
        assertEquals(Integer.class, result.getTypeParams()[1].getTypeParams()[0].getRawClass());
    }
}