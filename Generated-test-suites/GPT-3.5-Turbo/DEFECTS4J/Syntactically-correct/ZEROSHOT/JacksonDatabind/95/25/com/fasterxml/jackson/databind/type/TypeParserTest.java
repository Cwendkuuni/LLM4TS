package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setup() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse_NoTokens_ThrowsException() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            MyTokenizer tokens = new MyTokenizer("");
            typeParser.parseType(tokens);
        });

        Assert.assertEquals("Unexpected end-of-string", exception.getMessage());
    }

    @Test
    public void testParse_NonGenericBase_ReturnsJavaType() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        JavaType result = typeParser.parseType(tokens);

        Assert.assertNotNull(result);
    }

    @Test
    public void testParse_GenericTypes_ReturnsJavaType() {
        MyTokenizer tokens = new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        JavaType result = typeParser.parseType(tokens);

        Assert.assertNotNull(result);
    }

    @Test
    public void testParse_TokensAfterCompleteType_ThrowsException() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            MyTokenizer tokens = new MyTokenizer("java.lang.String extra");
            typeParser.parse(tokens.getAllInput());
        });

        Assert.assertEquals("Unexpected tokens after complete type", exception.getMessage());
    }

    @Test
    public void testParseTypes_NoTokens_ThrowsException() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            MyTokenizer tokens = new MyTokenizer("");
            typeParser.parseTypes(tokens);
        });

        Assert.assertEquals("Unexpected end-of-string", exception.getMessage());
    }

    @Test
    public void testParseTypes_InvalidToken_ThrowsException() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            MyTokenizer tokens = new MyTokenizer("java.lang.String < java.lang.Integer");
            typeParser.parseTypes(tokens);
        });

        Assert.assertEquals("Unexpected token '<', expected ',' or '>')", exception.getMessage());
    }

    @Test
    public void testFindClass_ClassNotFound_ThrowsException() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            MyTokenizer tokens = new MyTokenizer("invalid.class.name");
            typeParser.findClass(tokens.nextToken(), tokens);
        });

        Assert.assertEquals("Can not locate class 'invalid.class.name', problem: null", exception.getMessage());
    }
}