package com.example.typeparser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class TypeParserTest {

    @Test
    public void testParseCanonicalType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType type = parser.parse("int");
        assertEquals(Integer.TYPE, type.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType listType = parser.parse("java.util.List<java.lang.Integer>");
        assertEquals(new TypeReference<List<Integer>>(){}.getType(), listType);
    }

    @Test
    public void testParseInvalidType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("invalidType"));
    }

    @Test
    public void testParseTypesWithInvalidToken() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>x>");
        assertThrows(IllegalArgumentException.class, () -> parser.parseType(tokens));
    }

    @Test
    public void testParseTypesWithMissingCloseBracket() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>");
        tokens.nextToken();
        tokens.nextToken();
        tokens.nextToken();
        tokens.nextToken(); // skip '>'
        assertThrows(IllegalArgumentException.class, () -> parser.parseType(tokens));
    }

    @Test
    public void testParseTypesWithMissingComma() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>java.util.List<java.lang.Double>");
        assertThrows(IllegalArgumentException.class, () -> parser.parseTypes(tokens));
    }

    @Test
    public void testFindClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType type = parser.parse("java.util.List<java.lang.String>");
        assertEquals(List.class, type.getRawClass().getComponentType().getComponentType());
        assertEquals(String.class, parser.findClass(type.getRawClass().getName(), null).getType().getRawClass());
    }
}