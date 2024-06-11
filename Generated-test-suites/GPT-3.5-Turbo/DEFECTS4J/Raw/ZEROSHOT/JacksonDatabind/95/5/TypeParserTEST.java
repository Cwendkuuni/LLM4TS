package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setup() {
         TypeFactory factory = new TypeFactory();
         parser = new TypeParser(factory);
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType type = parser.parse(canonical);
        assertNotNull(type);
        assertEquals(List.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertEquals(String.class, type.containedType(0).getRawClass());
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.Integer");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String,java.lang.Integer");
        List<JavaType> types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(2, types.size());
        assertEquals(String.class, types.get(0).getRawClass());
        assertEquals(Integer.class, types.get(1).getRawClass());
    }

    @Test
    public void testFindClass() {
        String className = "java.lang.String";
        Class<?> clazz = parser.findClass(className, null);
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testProblem() {
        String className = "java.lang.UnknownClass";
        try {
            parser.findClass(className, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
            assertTrue(e.getMessage().contains(className));
        }
    }
}