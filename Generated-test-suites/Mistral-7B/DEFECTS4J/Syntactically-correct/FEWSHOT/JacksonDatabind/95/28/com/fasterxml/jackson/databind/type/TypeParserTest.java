package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory = new SimpleTypeFactory();
    private TypeParser _parser = new TypeParser(_factory);

    @Test
    public void testParseNull() {
        try {
            _parser.parse(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseEmptyString() {
        JavaType type = _parser.parse("");
        assertNull(type);
    }

    @Test
    public void testParseClass() {
        JavaType type = _parser.parse("String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParseClassWithArray() {
        JavaType type = _parser.parse("[Ljava.lang.String;");
        assertEquals(String[].class, type.getRawClass());
    }

    @Test
    public void testParseClassWithGeneric() {
        JavaType type = _parser.parse("java.util.ArrayList<java.lang.String>");
        assertEquals(ArrayList.class);
        assertEquals(String.class, type.getTypeParams()[0].getRawClass());
    }

    @Test
    public void testParseClassWithMultipleGeneric() {
        JavaType type = _parser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals(Map.class);
        assertEquals(String.class, type.getTypeParams()[0].getRawClass());
        assertEquals(List.class, type.getTypeParams()[1].getRawClass());
        assertEquals(Integer.class, ((List.class).cast(type.getTypeParams()[1])).getTypeParams()[0].getRawClass());
    }

    @Test
    public void testParseInvalidClassWithBrackets() {
        try {
            _parser.parse("java.util.ArrayList");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidClassWithAngleBrackets() {
        try {
            _parser.parse("java.util.ArrayList<>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidClassWithExtraAngleBrackets() {
        try {
            _parser.parse("java.util.ArrayList<>>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidClassWithMissingAngleBrackets() {
        try {
            _parser.parse("java.util.ArrayList<java.lang.String java.lang.String>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInvalidClassWithInvalidToken() {
        try {
            _parser.parse("java.util.ArrayList<java.lang.String;>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseClassWithCustomTokenizer() {
        _parser = new TypeParser(_factory) {
            @Override
            protected MyTokenizer newTokenizer(String input) {
                return new MyTokenizerForTest() {
                    @Override
                    public String nextToken() {
                        String token = super.nextToken();
                        if ("<".equals(token)) {
                            return ">";
                        }
                        return token;
                    }
                };
            }
        };
        JavaType type = _parser.parse("java.util.ArrayList<java.lang.String>");
        assertEquals(ArrayList.class, type.getRawClass());
    }

    private static class MyTokenizerForTest extends TypeParser.MyTokenizer {
        @Override
        public String nextToken() {
            String token = super.nextToken();
            if ("<".equals(token)) {
                return ">";
            }
            return token;
        }
    }
}