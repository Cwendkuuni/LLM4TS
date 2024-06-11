
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {
    private TypeParser typeParser = new TypeParser(TypeFactory.defaultInstance());

    @Test
    public void testParse() {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
        assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
        
        javaType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", javaType.toCanonical());
        
        javaType = typeParser.parse("java.util.Set<java.lang.Double>");
        assertEquals("java.util.Set<java.lang.Double>", javaType.toCanonical());
        
        javaType = typeParser.parse("java.lang.Object");
        assertEquals("java.lang.Object", javaType.toCanonical());

        javaType = typeParser.parse("");
        assertEquals("", javaType.toCanonical());
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = typeParser.parseType(tokens);
        assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
        
        tokens = new TypeParser.MyTokenizer("java.util.Set<java.lang.Double>");
        javaType = typeParser.parseType(tokens);
        assertEquals("java.util.Set<java.lang.Double>", javaType.toCanonical());
        
        tokens = new TypeParser.MyTokenizer("java.lang.Object");
        javaType = typeParser.parseType(tokens);
        assertEquals("java.lang.Object", javaType.toCanonical());
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = typeParser.parseType(tokens);
        assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
        
        tokens = new TypeParser.MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        javaType = typeParser.parseType(tokens);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", javaType.toCanonical());
        
        tokens = new TypeParser.MyTokenizer("java.util.Set<java.lang.Double>");
        javaType = typeParser.parseType(tokens);
        assertEquals("java.util.Set<java.lang.Double>", javaType.toCanonical());
    }

    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> clazz = typeParser.findClass(tokens.nextToken(), tokens);
        assertEquals("java.lang.String", clazz.getCanonicalName());
        
        tokens = new TypeParser.MyTokenizer("java.util.List");
        clazz = typeParser.findClass(tokens.nextToken(), tokens);
        assertEquals("java.util.List", clazz.getCanonicalName());
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        IllegalArgumentException exception = typeParser._problem(tokens, "Failed to parse");
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Failed to parse", exception.getMessage());
        
        tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        exception = typeParser._problem(tokens, "Failed to parse");
        assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Failed to parse", exception.getMessage());
    }
}
