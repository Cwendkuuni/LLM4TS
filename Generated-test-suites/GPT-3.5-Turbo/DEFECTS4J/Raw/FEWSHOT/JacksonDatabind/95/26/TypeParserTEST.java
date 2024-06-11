package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeParserTest {
    
    TypeFactory factory = new TypeFactory();
    TypeParser parser = new TypeParser(factory);

    @Test
    public void testParse() {
        JavaType type1 = parser.parse("java.lang.String");
        assertEquals(JavaType.class, type1.getClass());
        
        JavaType type2 = parser.parse("java.util.List<java.lang.String>");
        assertEquals(JavaType.class, type2.getClass());
        assertEquals("java.util.List", type2.getRawClass().getName());
        assertEquals("java.lang.String", type2.getContentType().getRawClass().getName());
        
        JavaType type3 = parser.parse("com.fasterxml.jackson.databind.ObjectMapper");
        assertEquals(JavaType.class, type3.getClass());
        
        JavaType type4 = parser.parse("com.fasterxml.jackson.databind.ObjectMapper<java.lang.String>");
        assertEquals(JavaType.class, type4.getClass());
        assertEquals("com.fasterxml.jackson.databind.ObjectMapper", type4.getRawClass().getName());
        assertEquals("java.lang.String", type4.getContentType().getRawClass().getName());
        
        JavaType type5 = parser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals(JavaType.class, type5.getClass());
        assertEquals("java.util.Map", type5.getRawClass().getName());
        assertEquals("java.lang.String", type5.getKeyType().getRawClass().getName());
        assertEquals("java.util.List", type5.getContentType().getRawClass().getName());
        assertEquals("java.lang.Integer", type5.getContentType().getContentType().getRawClass().getName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalid() {
        parser.parse("com.fasterxml.jackson.doesnotexist");
    }

    @Test
    public void testParseTypes() {
        String input1 = "java.lang.String, java.util.List<java.lang.Integer>";
        MyTokenizer tokenizer1 = new MyTokenizer(input1);
        parser.parseTypes(tokenizer1);
        assertEquals(2, tokenizer1.getTypes().size());
        assertEquals("java.lang.String", tokenizer1.getTypes().get(0).getRawClass().getName());
        assertEquals("java.util.List", tokenizer1.getTypes().get(1).getRawClass().getName());
        assertEquals("java.lang.Integer", tokenizer1.getTypes().get(1).getContentType().getRawClass().getName());
        
        String input2 = "";
        MyTokenizer tokenizer2 = new MyTokenizer(input2);
        parser.parseTypes(tokenizer2);
        assertEquals(0, tokenizer2.getTypes().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypesInvalid() {
        String input = "java.lang.String,invalid";
        MyTokenizer tokenizer = new MyTokenizer(input);
        parser.parseTypes(tokenizer);
    }

    @Test
    public void testFindClass() {
        String className1 = "java.lang.String";
        MyTokenizer tokenizer1 = new MyTokenizer(className1);
        Class<?> class1 = parser.findClass(className1, tokenizer1);
        assertEquals(String.class, class1);
        
        String className2 = "com.fasterxml.jackson.databind.ObjectMapper";
        MyTokenizer tokenizer2 = new MyTokenizer(className2);
        Class<?> class2 = parser.findClass(className2, tokenizer2);
        assertEquals(ObjectMapper.class, class2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindClassInvalid() {
        String className = "com.fasterxml.jackson.doesnotexist";
        MyTokenizer tokenizer = new MyTokenizer(className);
        parser.findClass(className, tokenizer);
    }
}