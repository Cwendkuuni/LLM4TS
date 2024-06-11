package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {

   @Test
   public void testParse() {
      TypeFactory factory = new TypeFactory();
      TypeParser parser = new TypeParser(factory);
      
      // Test case 1: Parse simple type
      String canonical1 = "java.lang.String";
      JavaType result1 = parser.parse(canonical1);
      assertEquals("java.lang.String", result1.toCanonical());
      
      // Test case 2: Parse generic type
      String canonical2 = "java.util.List<java.lang.Integer>";
      JavaType result2 = parser.parse(canonical2);
      assertEquals("java.util.List<java.lang.Integer>", result2.toCanonical());
      
      // Test case 3: Parse nested generic type
      String canonical3 = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
      JavaType result3 = parser.parse(canonical3);
      assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", result3.toCanonical());
      
      // Test case 4: Parse type with spaces
      String canonical4 = "java.util.List < java.lang.Integer > ";
      JavaType result4 = parser.parse(canonical4);
      assertEquals("java.util.List<java.lang.Integer>", result4.toCanonical());
   }

   @Test
   public void testParseType() {
      TypeFactory factory = new TypeFactory();
      TypeParser parser = new TypeParser(factory);
      TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.Integer>");
      
      JavaType result = parser.parseType(tokens);
      assertEquals("java.util.List<java.lang.Integer>", result.toCanonical());
   }

   @Test
   public void testParseTypes() {
      TypeFactory factory = new TypeFactory();
      TypeParser parser = new TypeParser(factory);
      TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.Integer>, java.util.Set<java.lang.String>");
      
      JavaType result1 = parser.parseTypes(tokens).get(0);
      assertEquals("java.util.List<java.lang.Integer>", result1.toCanonical());
      
      JavaType result2 = parser.parseTypes(tokens).get(1);
      assertEquals("java.util.Set<java.lang.String>", result2.toCanonical());
   }

   @Test
   public void testFindClass() {
      TypeFactory factory = new TypeFactory();
      TypeParser parser = new TypeParser(factory);
      TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
      
      Class<?> result = parser.findClass("java.util.List", tokens);
      assertEquals("java.util.List", result.getName());
   }

   @Test
   public void testProblem() {
      TypeFactory factory = new TypeFactory();
      TypeParser parser = new TypeParser(factory);
      TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util."+"\u0000");
      
      IllegalArgumentException result = parser._problem(tokens, "Unexpected end-of-string");
      assertNotNull(result);
   }
   
}