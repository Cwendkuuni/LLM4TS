package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {
     TypeParser parser;

     @Test
     public void testTypeParserWithFactory() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         JavaType javaType = parser.parse("int");
         assertEquals(Integer.TYPE, javaType.getRawClass());
     }

     @Test
     public void testParseStringWithUnknownClass() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("com.unknown.UnknownClass"));
     }

     @Test
     public void testParseStringWithSimpleClass() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         JavaType javaType = parser.parse("int");
         assertEquals(Integer.TYPE, javaType.getRawClass());
     }

     @Test
     public void testParseStringWithArrayType() throws Exception {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         JavaType javaType = parser.parse("int[]");
         JavaType elementType = javaType.getComponentType();
         assertEquals(Integer.TYPE, elementType.getRawClass());
         assertEquals(Integer[].class, javaType.getRawClass());
     }

     @Test
     public void testParseStringWithClassAndGenerics() throws Exception {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         Class<List<Integer>> listClass = List.class.getGenericType();
         JavaType javaType = parser.parse("java.util.List<java.lang.Integer>");
         assertSame(listClass, javaType.getRawType());
         List<JavaType> typeArguments = javaType.getTypeArguments();
         assertArrayEquals(new JavaType[] { Integer.TYPE }, typeArguments.toArray());
     }

     @Test
     public void testParseStringWithClassAndGenericsWithWildcard() throws Exception {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         Class<List<?>> listClass = List.class.getGenericType();
         JavaType javaType = parser.parse("java.util.List<?>");
         assertSame(listClass, javaType.getRawType());
         List<JavaType> typeArguments = javaType.getTypeArguments();
         assertNull(typeArguments);
     }

     @Test
     public void testParseStringWithNestedGenerics() throws Exception {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         Class<Map<List<Integer>, List<Integer>>> mapClass = Map.class.getGenericType();
         JavaType javaType = parser.parse("java.util.Map<java.util.List<java.lang.Integer>, java.util.List<java.lang.Integer>>");
         assertSame(mapClass, javaType.getRawType());
         List<JavaType> mapKeyTypeArguments = javaType.getTypeArguments()[0].getTypeArguments();
         List<JavaType> mapValueTypeArguments = javaType.getTypeArguments()[1].getTypeArguments();
         assertArrayEquals(new JavaType[] { List.class, List.class }, mapKeyTypeArguments.toArray());
         assertArrayEquals(new JavaType[] { Integer.TYPE, Integer.TYPE }, mapValueTypeArguments.toArray());
     }

     @Test
     public void testParseStringWithInvalidToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("int<"));
     }

     @Test
     public void testParseStringWithMissingToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("int>"));
     }

     @Test
     public void testParseStringWithInvalidClassToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("int<java.util.List"));
     }

     @Test
     public void testParseStringWithInvalidTypeArguments() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<Integer, Integer>"));
     }

     @Test
     public void testParseStringWithEmptyTypeArguments() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         JavaType javaType = parser.parse("java.util.Map<>");
         assertNull(javaType);
     }

     @Test
     public void testParseStringWithInvalidEndToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("int<java.util.List>foo"));
     }

     @Test
     public void testParseStringWithInvalidEndToken2() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parse("int<java.util.List>,"));
     }

     @Test
     public void testParseTypesWithValidTypes() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         List<JavaType> javaTypes = parser.parseTypes("int, java.util.List<java.lang.Integer>, java.util.Map<java.util.List<java.lang.Integer>, java.util.List<java.lang.Integer>>");
         assertTrue(javaTypes.size() == 3);
         assertEquals(Integer.TYPE, javaTypes.get(0));
         assertEquals(List.class, javaTypes.get(1).getRawType());
         assertEquals(List.class, javaTypes.get(1).getComponentType().getRawType());
         assertEquals(Integer.TYPE, javaTypes.get(1).getComponentType().getComponentType().getRawClass());
         assertEquals(Map.class, javaTypes.get(2).getRawType());
         assertEquals(List.class, javaTypes.get(2).getTypeArguments()[0].getRawType());
         assertEquals(List.class, javaTypes.get(2).getTypeArguments()[1].getRawType());
         assertEquals(Integer.TYPE, javaTypes.get(2).getTypeArguments()[0].getTypeArguments()[0].getRawClass());
         assertEquals(Integer.TYPE, javaTypes.get(2).getTypeArguments()[1].getComponentType().getRawClass());
     }

     @Test
     public void testParseTypesWithEmptyInput() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parseTypes(""));
     }

     @Test
     public void testParseTypesWithOneToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parseTypes("int"));
     }

     @Test
     public void testParseTypesWithInvalidToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parseTypes("int<"));
     }

     @Test
     public void testParseTypesWithInvalidType() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parseTypes("int<java.util.List>foo"));
     }

     @Test
     public void testParseTypesWithMissingEndToken() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         assertThrows(IllegalArgumentException.class, () -> parser.parseTypes("int<java.util.List>,"));
     }

     @Test
     public void testParseTypesWithValidTypesAndEmptyEnd() {
         TypeFactory typeFactory = new TypeFactory();
         parser = new TypeParser(typeFactory);
         List<JavaType> javaTypes = parser.parseTypes("int, java.util.List<java.lang.Integer>, java.util.Map<java.util.List<java.lang.Integer>, java.util.List<java.lang.Integer>>");
         assertTrue(javaTypes.size() == 3);
         assertEquals(Integer.TYPE, javaTypes.get(0));
         assertEquals(List.class, javaTypes.get(1).getRawType());
         assertEquals(List.class, javaTypes.get(1).getComponentType().getRawType());
         assertEquals(Integer.TYPE, javaTypes.get(1).getComponentType().getComponentType().getRawClass());
         assertEquals(Map.class, javaTypes.get(2).getRawType());
         assertEquals(List.class, javaTypes.get(2).getTypeArguments()[0].getRawType());
         assertEquals(List.class, javaTypes.get(2).getTypeArguments()[1].getRawType());
         assertEquals(Integer.TYPE, javaTypes.get(2).getTypeArguments()[0].getTypeArguments()[0].getRawClass());
         assertEquals(Integer.TYPE, javaTypes.get(2).getTypeArguments()[1].getComponentType().getRawClass());
     }
}