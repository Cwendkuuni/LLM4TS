package com.fasterxml.jackson.databind.type.test;

 import com.fasterxml.jackson.databind.JavaType;
 import com.fasterxml.jackson.databind.type.MapType;
 import org.junit.Test;
 import static org.junit.Assert.*;

 /**
 * Test class for MapType.
 */
 public class MapTypeTest {

 @Test
 public void testMapTypeConstructor() {
 MapType mapType = MapType.construct(String.class, null, Object.class, null,
 String.class, Integer.class);
 assertEquals("map type; class Class<java.lang.String>, Class<java.lang.String> -> Class<java.lang.Integer>",
 mapType.toString());
 }

 @Test
 public void testMapTypeWithTypeHandler() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withTypeHandler = mapType.withTypeHandler(new Object() {});
 assertNotSame(mapType, withTypeHandler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapTypeWithInvalidKeyType() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 mapType.withKeyType(null);
 }

 @Test
 public void testMapTypeWithKeyTypeHandler() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withKeyTypeHandler = mapType.withKeyTypeHandler(new Object() {});
 assertNotSame(mapType, withKeyTypeHandler);
 }

 @Test
 public void testMapTypeWithValueTypeHandler() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withValueTypeHandler = mapType.withValueHandler(new Object() {});
 assertNotSame(mapType, withValueTypeHandler);
 }

 @Test
 public void testMapTypeWithContentValueHandler() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withContentValueHandler = mapType.withContentValueHandler(new Object() {});
 assertNotSame(mapType, withContentValueHandler);
 }

 @Test
 public void testMapTypeWithStaticTyping() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withStaticTyping = mapType.withStaticTyping();
 assertNotSame(mapType, withStaticTyping);
 }

 @Test
 public void testMapTypeWithKeyType() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withKeyType = mapType.withKeyType(String[].class);
 assertNotSame(mapType, withKeyType);
 }

 @Test
 public void testMapTypeWithContentType() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withContentType = mapType.withContentType(String[].class);
 assertNotSame(mapType, withContentType);
 }

 @Test
 public void testMapTypeWithValueType() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 MapType withValueType = mapType.withValueType(Double.class);
 assertNotSame(mapType, withValueType);
 }

 @Test
 public void testMapTypeRefine() {
 MapType mapType = new MapType(String.class, null, Object.class, null,
 String.class, Integer.class, null, null, false);
 JavaType refinedType = mapType.refine(String[].class, null, Object.class, null);
 assertNotSame(mapType, refinedType);
 }

 @Test
 public void testMapTypeConstructDeprecated() {
 MapType mapType = MapType.construct(String.class, String.class, Integer.class);
 assertEquals("map type; class Class<java.lang.String>, Class<java.lang.String> -> Class<java.lang.Integer>",
 mapType.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapTypeWithValueHandlerDeprecated() {
 MapType mapType = MapType.construct(String.class, null, Object.class, null,
 String.class, Integer.class);
 mapType.withValueHandler(new Object() {});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapTypeWithContentValueHandlerDeprecated() {
 MapType mapType = MapType.construct(String.class, null, Object.class, null,
 String.class, Integer.class);
 mapType.withContentValueHandler(new Object() {});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapTypeWithTypeHandlerDeprecated() {
 MapType mapType = MapType.construct(String.class, null, Object.class, null,
 String.class, Integer.class);
 mapType.withTypeHandler(new Object() {});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapTypeWithKeyTypeHandlerDeprecated() {
 MapType mapType = MapType.construct(String.class, null, Object.class, null,
 String.class, Integer.class);
 mapType.withKeyTypeHandler(new Object() {});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMapTypeWithKeyValueHandlerDeprecated() {
 MapType mapType = MapType.construct(String.class, null, Object.class, null,
 String.class, Integer.class);
 mapType.withKeyValueHandler(new Object() {});
 }
 }