
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setup() {
        typeParser = new TypeParser(new TypeFactory());
    }
    
    @Test
    public void testParse() {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(1, javaType.getBindings().size());
        assertEquals(String.class, javaType.getBindings().getBoundType(0).getRawClass());
    }
    
    @Test
    public void testParse_InvalidType() {
        try {
            typeParser.parse("InvalidType");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParse_UnexpectedTokens() {
        try {
            typeParser.parse("java.util.List<java.lang.String> Unexpected");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParse_EmptyString() {
        try {
            typeParser.parse("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParse_MissingCloseAngleBracket() {
        try {
            typeParser.parse("java.util.List<java.lang.String");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParse_MissingOpenAngleBracket() {
        try {
            typeParser.parse("java.util.Listjava.lang.String>");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(1, javaType.getBindings().size());
        assertEquals(String.class, javaType.getBindings().getBoundType(0).getRawClass());
    }
    
    @Test
    public void testParseType_UnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParseType_NonGenericType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getBindings());
    }
    
    @Test
    public void testParseType_UnexpectedTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String> Unexpected");
        try {
            typeParser.parseType(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> javaTypes = typeParser.parseTypes(tokens);
        assertNotNull(javaTypes);
        assertEquals(2, javaTypes.size());
        assertEquals(String.class, javaTypes.get(0).getRawClass());
        assertEquals(Integer.class, javaTypes.get(1).getRawClass());
    }
    
    @Test
    public void testParseTypes_UnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes_UnexpectedToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, Unexpected");
        try {
            typeParser.parseTypes(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> clazz = typeParser.findClass("java.lang.String", tokens);
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }
    
    @Test
    public void testFindClass_ClassNotFoundException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("InvalidClass");
        try {
            typeParser.findClass("InvalidClass", tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
}
