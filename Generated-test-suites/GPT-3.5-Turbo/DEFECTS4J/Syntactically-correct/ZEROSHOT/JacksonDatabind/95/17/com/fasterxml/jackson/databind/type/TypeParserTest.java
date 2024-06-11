package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse_singleNonGenericType() {
        TypeParser tp = new TypeParser(new TypeFactory());
        JavaType result = tp.parse("java.lang.String");
        assertEquals("java.lang.String", result.getRawClass().getName());
        assertNull(result.getBindings());
    }
    
    @Test
    public void testParse_singleGenericType() {
        TypeParser tp = new TypeParser(new TypeFactory());
        JavaType result = tp.parse("java.util.List<java.lang.String>");
        assertEquals("java.util.List", result.getRawClass().getName());
        assertNotNull(result.getBindings());
        assertNotNull(result.getBindings().getBoundType(0));
        assertEquals("java.lang.String", result.getBindings().getBoundType(0).getRawClass().getName());
    }
    
    @Test
    public void testParse_multipleGenericTypes() {
        TypeParser tp = new TypeParser(new TypeFactory());
        JavaType result = tp.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals("java.util.Map", result.getRawClass().getName());
        assertNotNull(result.getBindings());
        assertNotNull(result.getBindings().getBoundType(0));
        assertEquals("java.lang.String", result.getBindings().getBoundType(0).getRawClass().getName());
        assertNotNull(result.getBindings().getBoundType(1));
        assertEquals("java.util.List", result.getBindings().getBoundType(1).getRawClass().getName());
        assertNotNull(result.getBindings().getBoundType(1).getBindings());
        assertNotNull(result.getBindings().getBoundType(1).getBindings().getBoundType(0));
        assertEquals("java.lang.Integer", result.getBindings().getBoundType(1).getBindings().getBoundType(0).getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidType() {
        TypeParser tp = new TypeParser(new TypeFactory());
        tp.parse("invalidType");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidGenericType() {
        TypeParser tp = new TypeParser(new TypeFactory());
        tp.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_tokenAfterCompleteType() {
        TypeParser tp = new TypeParser(new TypeFactory());
        tp.parse("java.lang.String extraToken");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedEndOfString() {
        TypeParser tp = new TypeParser(new TypeFactory());
        tp.parse("java.util.List<java.lang.String,");
    }
}