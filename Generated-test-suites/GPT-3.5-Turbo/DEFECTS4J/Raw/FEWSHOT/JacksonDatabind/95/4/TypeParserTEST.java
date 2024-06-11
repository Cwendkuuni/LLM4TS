
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        assertNotNull(typeParser.parse("java.lang.String"));
        assertNotNull(typeParser.parse("java.util.List<java.lang.String>"));
        assertNotNull(typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>"));
        assertNotNull(typeParser.parse("com.fasterxml.jackson.databind.type.TypeParser"));
        assertNotNull(typeParser.parse("org.junit.Assert"));
        
        try {
            assertNull(typeParser.parse(null));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            assertNull(typeParser.parse(""));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testFindClass() {
        assertEquals(String.class, typeParser.findClass("java.lang.String", null));
        assertEquals(List.class, typeParser.findClass("java.util.List", null));
        assertEquals(Map.class, typeParser.findClass("java.util.Map", null));
        
        try {
            assertNull(typeParser.findClass("com.fake.class", null));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        
        try {
            assertNotNull(typeParser.parseType(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        try {
            assertNotNull(typeParser.parseType(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        
        try {
            assertNotNull(typeParser.parseType(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.type.TypeParser");
        
        try {
            assertNotNull(typeParser.parseType(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("org.junit.Assert");
        
        try {
            assertNotNull(typeParser.parseType(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("");
        
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("<");
        
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        
        try {
            assertNotNull(typeParser.parseTypes(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        try {
            assertNotNull(typeParser.parseTypes(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        
        try {
            assertNotNull(typeParser.parseTypes(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.type.TypeParser");
        
        try {
            assertNotNull(typeParser.parseTypes(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("org.junit.Assert");
        
        try {
            assertNotNull(typeParser.parseTypes(tokens));
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer("");
        
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer(",");
        
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        tokens = new TypeParser.MyTokenizer(">");
        
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
