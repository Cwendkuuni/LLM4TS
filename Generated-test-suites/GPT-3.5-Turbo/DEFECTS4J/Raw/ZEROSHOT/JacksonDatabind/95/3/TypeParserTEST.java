package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert;
import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        String canonical = "java.util.List<java.lang.String>";
        JavaType actual = parser.parse(canonical);
        JavaType expected = f.constructCollectionType(List.class, String.class);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        String canonical = "java.util.List<java.lang.String> unexpected";
        JavaType actual = parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedEndOfString() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        String canonical = "";
        JavaType actual = parser.parse(canonical);
    }

    @Test
    public void testParseType() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType actual = parser.parseType(tokens);
        JavaType expected = f.constructCollectionType(List.class, String.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseTypeWithEndOfString() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType actual = parser.parseType(tokens);
        JavaType expected = f.constructType(String.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseTypeWithGenericsEnd() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType actual = parser.parseType(tokens);
        JavaType expected = f.constructCollectionType(List.class, String.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseTypeWithGenericsMoreTokens() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String> extra tokens");
        JavaType actual = parser.parseType(tokens);
        JavaType expected = f.constructCollectionType(List.class, String.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseTypesAll() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, java.util.List<java.lang.Integer>");
        List<JavaType> actual = parser.parseTypes(tokens);
        List<JavaType> expected = new ArrayList<JavaType>();
        expected.add(f.constructType(String.class));
        expected.add(f.constructCollectionType(List.class, Integer.class));
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedEndOfString() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        List<JavaType> actual = parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedToken() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, unexpected");
        List<JavaType> actual = parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithException() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.UnknownClass");
        Class<?> actual = parser.findClass(tokens.nextToken(), tokens);
    }

    @Test
    public void testFindClass() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> actual = parser.findClass(tokens.nextToken(), tokens);
        Class<?> expected = String.class;
        Assert.assertEquals(expected, actual);
    }
}