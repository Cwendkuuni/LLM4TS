package com.fasterxml.jackson.databind.type;

import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Assert;
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
        Assert.assertNotNull(result);
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(1, result.containedTypeCount());
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParse_EmptyInput() {
        try {
            typeParser.parse("");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidClass() {
        try {
            typeParser.parse("InvalidClass");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'InvalidClass' (remaining: 'InvalidClass'): Can not locate class 'InvalidClass', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParse_UnexpectedTokensAfterCompleteType() {
        try {
            typeParser.parse("java.util.List<java.lang.String> unexpected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String> unexpected' (remaining: 'unexpected'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(1, result.containedTypeCount());
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParseType_UnexpectedEndOfString() {
        MyTokenizer tokens = new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType_Generics() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(1, result.containedTypeCount());
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParseType_ParameterTypes_NullBase() {
        MyTokenizer tokens = new MyTokenizer("null<java.lang.String>");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'null<java.lang.String>' (remaining: 'null'): Unexpected token 'null', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseType_ParameterTypes_Comma() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String,>");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String,>' (remaining: 'java.lang.String,>'): Unexpected token 'java.lang.String,', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseType_ParameterTypes_ClosedGeneric() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(1, result.containedTypeCount());
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParseType_ParameterTypes_ClosedGeneric_UnexpectedEndOfString() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: 'java.lang.String'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType_ParameterTypes_ClosedGeneric_UnexpectedToken() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String unexpected>");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String unexpected>' (remaining: 'unexpected>'): Unexpected token 'unexpected>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseType_ParameterTypes_EndOfString() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(1, result.containedTypeCount());
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParseTypes() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> result = typeParser.parseTypes(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(String.class, result.get(0).getRawClass());
        Assert.assertEquals(Integer.class, result.get(1).getRawClass());
    }

    @Test
    public void testParseTypes_EmptyTypes() {
        MyTokenizer tokens = new MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_UnclosedGeneric() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'java.lang.String, java.lang.Integer' (remaining: 'java.lang.String, java.lang.Integer'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        Class<?> result = typeParser.findClass(tokens.nextToken(), tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testFindClass_Exception() {
        MyTokenizer tokens = new MyTokenizer("InvalidClass");
        try {
            typeParser.findClass(tokens.nextToken(), tokens);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'InvalidClass' (remaining: 'InvalidClass'): Can not locate class 'InvalidClass', problem: null", e.getMessage());
        }
    }

    @Test
    public void testProblem() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        IllegalArgumentException result = typeParser._problem(tokens, "Unexpected end-of-string");
        Assert.assertNotNull(result);
        Assert.assertEquals("Failed to parse type 'java.lang.String' (remaining: 'java.lang.String'): Unexpected end-of-string", result.getMessage());
    }

    final static class TypeFactory {
        public Class<?> findClass(String className) throws Exception {
            return Class.forName(className);
        }
    }

    final static class MyTokenizer extends StringTokenizer {
        protected final String _input;
        protected int _index;
        protected String _pushbackToken;

        public MyTokenizer(String str) {
            super(str, "<,>", true);
            _input = str;
        }

        @Override
        public boolean hasMoreTokens() {
            return (_pushbackToken != null) || super.hasMoreTokens();
        }

        @Override
        public String nextToken() {
            String token;
            if (_pushbackToken != null) {
                token = _pushbackToken;
                _pushbackToken = null;
            } else {
                token = super.nextToken();
                _index += token.length();
                token = token.trim();
            }
            return token;
        }

        public void pushBack(String token) {
            _pushbackToken = token;
        }

        public String getAllInput() {
            return _input;
        }

        public String getRemainingInput() {
            return _input.substring(_index);
        }
    }
}