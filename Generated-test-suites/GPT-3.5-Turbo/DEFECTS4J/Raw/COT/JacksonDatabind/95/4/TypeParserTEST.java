
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.util.List<String>");
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidString() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.util.List<String,>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.util.List<String>>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedEndOfString() {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("java.util.List<");
    }
}
