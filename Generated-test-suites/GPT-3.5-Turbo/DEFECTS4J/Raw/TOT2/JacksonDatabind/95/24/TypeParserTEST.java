package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {

    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        TypeFactory factory = new TypeFactory();
        typeParser = new TypeParser(factory);
    }

    @Test
    public void testParse_withValidInput() {
        String input = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(input);
        assertEquals("java.util.List<java.lang.String>", result.toString());
    }
    
    @Test
    public void testParse_withEmptyInput() {
        try {
            typeParser.parse("");
            fail("Exception not thrown for empty input");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Failed to parse type"));
        }
    }
    
    @Test
    public void testParse_withInvalidInput() {
        String input = "InvalidInput";
        try {
            typeParser.parse(input);
            fail("Exception not thrown for invalid input");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not locate class"));
        }
    }
    
    @Test
    public void testParseType_withNoTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("Exception not thrown for no tokens");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected end-of-string"));
        }
    }
    
    @Test
    public void testParseTypes_withUnexpectedToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("Type<String>");
        try {
            typeParser.parseTypes(tokens);
            fail("Exception not thrown for unexpected token");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected token"));
        }
    }
}