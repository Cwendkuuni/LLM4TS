package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TypeDeserializerBaseTest {

 private static final TypeIdResolver idResolver = new StandardTypeIdResolver();
 private static final ObjectMapper mapper = new ObjectMapper();

 @BeforeClass
 public static void setup() throws Exception {
 mapper.setSerializationInclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
 mapper.setTypeIdResolver(idResolver);
 mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
 }

 @AfterClass
 public static void teardown() throws Exception {
 mapper.close();
 }

 @Test
 public void testBaseClassDeserialization() throws Exception {
 Class<?> baseType = String.class;
 TypeDeserializerBase deserializer = new MapTypeDeserializerBase(
 mapper.getTypeFactory().constructType(Map.class), idResolver, "typePropertyName", true, Map.class, Map.class);
 TypeDeserializer<?> typeDeserializer = deserializer.forProperty(null);
 JsonTreeReader treeReader = mapper.treeReader();
 treeReader.setRootValue(mapper.readValue("{\"typePropertyName\":\"HashMap\", \"value\":[1, \"two\"]}", Map.class));
 Object result = typeDeserializer.deserialize(treeReader, null);
 assertEquals(Map.class, result.getClass());
 Map<?, ?> mapResult = (Map<?, ?>) result;
 assertEquals(1, mapResult.size());
 assertEquals("two", mapResult.get("value"));
 }

 @Test
 public void testUnknownTypeId() throws Exception {
 Class<?> baseType = String.class;
 TypeDeserializerBase deserializer = new MapTypeDeserializerBase(
 mapper.getTypeFactory().constructType(Map.class), idResolver, "typePropertyName", true, Map.class, Map.class);
 TypeDeserializer<?> typeDeserializer = deserializer.forProperty(null);
 JsonTreeReader treeReader = mapper.treeReader();
 treeReader.setRootValue(mapper.readValue("{\"typePropertyName\":\"UnknownTypeId\"}", Object.class));
 Object result = typeDeserializer.deserialize(treeReader, null);
 assertNull(result);
 }

 @Test
 public void testMissingTypeId() throws Exception {
 Class<?> baseType = String.class;
 TypeDeserializerBase deserializer = new MapTypeDeserializerBase(
 mapper.getTypeFactory().constructType(Map.class), idResolver, "typePropertyName", true, Map.class, Map.class);
 TypeDeserializer<?> typeDeserializer = deserializer.forProperty(null);
 JsonTreeReader treeReader = mapper.treeReader();
 treeReader.setRootValue(mapper.readValue("{\"value\":[1, \"two\"]}", Object.class));
 Object result = typeDeserializer.deserialize(treeReader, null);
 assertSame(Map.class, result.getClass());
 Map<?, ?> mapResult = (Map<?, ?>) result;
 assertEquals(1, mapResult.size());
 assertEquals("two", mapResult.get("value"));
 }

 @Test
 public void testTypeIdVisible() throws Exception {
 Class<?> baseType = String.class;
 TypeDeserializerBase deserializer = new MapTypeDeserializerBase(
 mapper.getTypeFactory().constructType(Map.class), idResolver, "typePropertyName", true, Map.class, Map.class);
 TypeDeserializer<?> typeDeserializer = deserializer.forProperty(null);
 JsonTreeReader treeReader = mapper.treeReader();
 treeReader.setRootValue(mapper.readValue("{\"typePropertyName\":\"HashMap\", \"typePropertyName\":\"HashMap\"}", Object.class));
 Object result = typeDeserializer.deserialize(treeReader, null);
 assertEquals(Map.class, result.getClass());
 Map<?, ?> mapResult = (Map<?, ?>) result;
 assertEquals(1, mapResult.size());
 assertEquals(Map.class, mapResult.get("typePropertyName").getClass());
 }
}