
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test cases for non-generic types
        assertEquals(JavaType.class, parser.parse("com.fasterxml.jackson.databind.type.JavaType").getClass());
        assertEquals(ArrayList.class, parser.parse("java.util.ArrayList").getClass());
        assertEquals(String.class, parser.parse("java.lang.String").getClass());
        
        // Test cases for generic types
        JavaType genericType = parser.parse("java.util.List<java.lang.String>");
        assertEquals(ArrayList.class, genericType.getRawClass());
        assertTrue(genericType.getBindings().size() == 1);
        assertEquals(String.class, genericType.getBindings().getBoundType(0).getRawClass());
        
        genericType = parser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertEquals(HashMap.class, genericType.getRawClass());
        assertTrue(genericType.getBindings().size() == 2);
        assertEquals(String.class, genericType.getBindings().getBoundType(0).getRawClass());
        assertEquals(Integer.class, genericType.getBindings().getBoundType(1).getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        parser.parse("InvalidType");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidEnclosingSymbol() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        parser.parse("java.util.List<java.lang.String)");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedTokens() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        parser.parse("java.util.List<java.lang.String>,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        parser.parse("java.util.List<java.lang.String");
    }
}
