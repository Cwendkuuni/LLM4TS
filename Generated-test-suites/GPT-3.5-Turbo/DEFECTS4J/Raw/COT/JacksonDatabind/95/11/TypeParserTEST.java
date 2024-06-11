
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        try {
            typeParser.parse(null);
            Assert.fail("Should throw IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            typeParser.parse("");
            Assert.fail("Should throw IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            typeParser.parse("com.fasterxml.jackson.databind.JavaType");
        } catch (IllegalArgumentException e) {
            Assert.fail("Should not throw any exception for valid input");
        }

        try {
            typeParser.parse("com.fasterxml.jackson.databind.JavaType.java");
            Assert.fail("Should throw exception for invalid class name");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            typeParser.parse("com.fasterxml.jackson.databind.JavaType,<com.fasterxml.jackson.databind.JavaType>");
            Assert.fail("Should throw exception for invalid type parameter");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testParseType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        try {
            typeParser.parseType(tokens);
            Assert.fail("Should throw IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException e) {
            //expected
        }

        tokens = typeParser.new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            Assert.fail("Should throw IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            //expected
        }

        tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.fail("Should not throw any exception for valid input");
        }

        tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType,<com.fasterxml.jackson.databind.JavaType>");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            Assert.fail("Should not throw any exception for valid input with type parameters");
        }
    }

    @Test
    public void testParseTypes() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
            Assert.fail("Should throw IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            //expected
        }

        tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            Assert.fail("Should not throw any exception for valid input with single type");
        }

        tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JavaType");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            Assert.fail("Should not throw any exception for valid input with multiple types");
        }

        tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JavaType<");
        try {
            typeParser.parseTypes(tokens);
            Assert.fail("Should throw IllegalArgumentException for invalid input with type parameter");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testFindClass() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        Class<?> clazz = typeParser.findClass("com.fasterxml.jackson.databind.JavaType", tokens);
        Assert.assertEquals(JavaType.class, clazz);
    }

    @Test
    public void testProblem() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        IllegalArgumentException exception = typeParser._problem(tokens, "Invalid input");
        Assert.assertEquals("Failed to parse type 'com.fasterxml.jackson.databind.JavaType' (remaining: ''): Invalid input", exception.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("com.fasterxml.jackson.databind.JavaType", tokens.nextToken());
        Assert.assertFalse(tokens.hasMoreTokens());
        Assert.assertNull(tokens.nextToken());

        tokens = typeParser.new MyTokenizer("");
        Assert.assertFalse(tokens.hasMoreTokens());
        Assert.assertNull(tokens.nextToken());

        tokens = typeParser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType,<com.fasterxml.jackson.databind.JavaType>");
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("com.fasterxml.jackson.databind.JavaType,", tokens.nextToken());
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("<", tokens.nextToken());
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("com.fasterxml.jackson.databind.JavaType>", tokens.nextToken());
        Assert.assertFalse(tokens.hasMoreTokens());
        Assert.assertNull(tokens.nextToken());

        tokens = typeParser.new MyTokenizer("1,2,3,4,5");
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("1", tokens.nextToken());
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("2", tokens.nextToken());
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("3", tokens.nextToken());
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("4", tokens.nextToken());
        Assert.assertTrue(tokens.hasMoreTokens());
        Assert.assertEquals("5", tokens.nextToken());
        Assert.assertFalse(tokens.hasMoreTokens());
        Assert.assertNull(tokens.nextToken());
    }
}
