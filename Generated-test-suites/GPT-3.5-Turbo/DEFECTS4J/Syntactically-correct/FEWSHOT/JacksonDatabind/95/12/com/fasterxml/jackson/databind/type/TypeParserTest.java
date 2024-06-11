
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse() {
        JavaType result = typeParser.parse("java.util.List<java.lang.String>");
        assertEquals("java.util.List<java.lang.String>", result.toCanonical());
    }

    @Test
    public void testParseWithSpaces() {
        JavaType result = typeParser.parse("  java.util.List <  java.lang.String  >  ");
        assertEquals("java.util.List<java.lang.String>", result.toCanonical());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() {
        typeParser.parse("java.lang.String<>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTokens() {
        typeParser.parse("java.util.List<java.lang.String> ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidEnd() {
        typeParser.parse("java.util.List<java.lang.String>>");
    }
}
