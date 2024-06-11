package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setUp() {
        parser = new TypeParser(TypeFactory.defaultInstance());
    }

    @Test
    public void testParse_withValidCanonicalString() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidCanonicalString() {
        String canonical = "java.util.List<java.lang.Integer";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withEmptyCanonicalString() {
        String canonical = "";
        parser.parse(canonical);
    }

    @Test
    public void testParseType_withValidTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withInvalidTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.Integer");
        parser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withEmptyTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withNoEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String> extra");
        parser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withInvalidTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.Integer, java.lang.String>");
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withEmptyTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withNoEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.Integer, java.lang.String> extra");
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_withInvalidClassName() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.findClass("InvalidClassName", tokens);
    }
}