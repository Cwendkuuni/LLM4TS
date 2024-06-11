package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonInclude.As;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 }

 @After
 public void teardown() throws Exception {
 mapper.close();
 }

 @Test
 public void testDeserializeTypedWithPropertyName() throws Exception {
 // Given: JSON with "typePropertyName" as key and typeId as value
 String json = "{\"typePropertyName\": \"MyCustomType\", \"someValue\": 42}";

 // When: deserialize using AsPropertyTypeDeserializer
 JavaType type = mapper.getTypeFactory().constructType(MyCustomType.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, idResolver, "typePropertyName", true, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

 // Then: result is an instance of MyCustomType
 assertEquals(MyCustomType.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedWithMissingPropertyName() throws Exception {
 // Given: JSON without "typePropertyName"
 String json = "{\"someValue\": 42}";

 // When: deserialize using AsPropertyTypeDeserializer
 JavaType type = mapper.getTypeFactory().constructType(MyCustomType.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, idResolver, "typePropertyName", true, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

 // Then: result is null
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedWithInvalidTypeId() throws Exception {
 // Given: JSON with invalid typeId
 String json = "{\"typePropertyName\": \"InvalidTypeId\", \"someValue\": 42}";

 // When: deserialize using AsPropertyTypeDeserializer
 JavaType type = mapper.getTypeFactory().constructType(MyCustomType.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, idResolver, "typePropertyName", true, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

 // Then: result is null
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedWithDefaultImpl() throws Exception {
 // Given: JSON without "typePropertyName" and with default implementation
 String json = "{\"someValue\": 42}";

 // When: deserialize using AsPropertyTypeDeserializer with default implementation
 JavaType type = mapper.getTypeFactory().constructType(DefaultImplType.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, idResolver, null, false, DefaultImplType.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

 // Then: result is an instance of DefaultImplType
 assertEquals(DefaultImplType.class, result.getClass());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeTypedWithInvalidJson() throws Exception {
 // Given: Invalid JSON
 String json = "42";

 // When: deserialize using AsPropertyTypeDeserializer
 JavaType type = mapper.getTypeFactory().constructType(MyCustomType.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(type, idResolver, "typePropertyName", true, null, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
 }

}

class MyCustomType {
 // Add your custom type implementation here
}

class DefaultImplType {
 // Add your default implementation type here
}