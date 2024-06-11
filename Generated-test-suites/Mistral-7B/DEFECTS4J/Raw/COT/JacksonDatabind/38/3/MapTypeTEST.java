package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

// Test constructor 1
@Test
public void testMapTypeConstruction() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 assertNotNull(mapType);
}

// Test constructor 2 deprecated
@Test(expected = DeprecationWarning.class)
public void testDeprecatedConstruct() {
 MapType mapType = MapType.construct(String.class, null, null);
 assertNotNull(mapType);
}

// Test _narrow method
@Test
public void testNarrowMethod() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 JavaType narrowedType = mapType._narrow(HashMap.class);
 assertNotNull(narrowedType);
 assertEquals(mapType.getClass(), narrowedType.getClass());
}

// Test withTypeHandler method
@Test
public void testWithTypeHandler() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 JavaType valueType = new TypeReference<Map<String, Integer>>() {}.getType();
 Object typeHandler = new Object() {};
 MapType withTypeHandler = mapType.withTypeHandler(typeHandler);
 JavaType resultType = withTypeHandler.getValueType();
 assertNotNull(resultType);
 assertEquals(valueType, resultType);
}

// Test construct with null keyT or valueT
@Test(expected = NullPointerException.class)
public void testNullKeyOrValueT() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 mapType = mapType.withKeyType(null);
 assertThrows(NullPointerException.class, () -> mapType.withValueType(null));
}

// Test withStaticTyping method
@Test
public void testWithStaticTyping() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 MapType withStaticTyping = mapType.withStaticTyping();
 assertFalse(mapType.isStatic());
 assertTrue(withStaticTyping.isStatic());
}

// Test withContentType method
@Test
public void testWithContentType() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 JavaType contentType = new TypeReference<Map<String, String>>() {}.getType();
 MapType withContentType = mapType.withContentType(contentType);
 JavaType resultType = withContentType.getValueType();
 assertNotNull(resultType);
 assertEquals(String.class, resultType.getRawClass());
}

// Test withKeyType method
@Test
public void testWithKeyType() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 JavaType keyType = new TypeReference<String>() {}.getType();
 MapType withKeyType = mapType.withKeyType(keyType);
 JavaType resultKeyType = withKeyType.getKeyType();
 assertNotNull(resultKeyType);
 assertEquals(keyType, resultKeyType);
}

// Test refine method
@Test
public void testRefineMethod() {
 MapType originalMapType = MapType.construct(HashMap.class, null, null, null, null, null);
 JavaType keyType = new TypeReference<String>() {}.getType();
 JavaType valueType = new TypeReference<Integer>() {}.getType();
 MapType refinedMapType = originalMapType.refine(Map.class, null, null, null);
 JavaType resultKeyType = refinedMapType.getKeyType();
 JavaType resultValueType = refinedMapType.getValueType();
 assertNotNull(refinedMapType);
 assertEquals(Map.class, refinedMapType.getRawClass());
 assertEquals(keyType, resultKeyType);
 assertEquals(valueType, resultValueType);
}

// Test withKeyTypeHandler method
@Test
public void testWithKeyTypeHandler() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 Object keyTypeHandler = new Object() {};
 MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
 JavaType resultKeyType = withKeyTypeHandler.getKeyType();
 assertNotNull(resultKeyType);
 assertNull(resultKeyType.getTypeHandler());
 assertEquals(keyTypeHandler, resultKeyType.getRawType());
}

// Test withKeyValueHandler method
@Test
public void testWithKeyValueHandler() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 Object keyValueHandler = new Object() {};
 MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
 JavaType keyType = withKeyValueHandler.getKeyType();
 JavaType valueType = withKeyValueHandler.getValueType();
 assertNotNull(keyType);
 assertNull(keyType.getValueHandler());
 assertNotNull(valueType);
 assertNull(valueType.getValueHandler());
 assertEquals(keyValueHandler, keyType.getRawType());
 assertEquals(keyValueHandler, valueType.getRawType());
}

// Test toString method
@Test
public void testToString() {
 MapType mapType = MapType.construct(HashMap.class, null, null, null, null, null);
 String toString = mapType.toString();
 String expected = "[map type; class java.util.HashMap, class java.lang.String -> class java.lang.Integer]";
 assertEquals(expected, toString);
}

}