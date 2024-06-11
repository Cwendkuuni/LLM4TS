package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerProvider;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for NullifyingDeserializer
 */
public class NullifyingDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserialize(JsonParser, DeserializationContext)}
 */
 @Test
 public void testDeserialize() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(jsonParser, deserializationContext);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 */
 @Test
 public void testDeserializeWithType() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(int.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_START_ARRAY
 */
 @Test
 public void testDeserializeWithTypeStartArray() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructArrayType(int[].class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_START_OBJECT
 */
 @Test
 public void testDeserializeWithTypeStartObject() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(MyClass.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_FIELD_NAME
 */
 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithTypeFieldName() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(MyClass.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 fail("Expected JsonMappingException but got null");
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_VALUE_STRING
 */
 @Test
 public void testDeserializeWithTypeValueString() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(String.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("\"Hello World\"");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_VALUE_NUMBER_INT
 */
 @Test
 public void testDeserializeWithTypeValueNumberInt() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(int.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("123");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_VALUE_NUMBER_FLOAT
 */
 @Test
 public void testDeserializeWithTypeValueNumberFloat() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(float.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("123.45");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_VALUE_NUMBER_LONG
 */
 @Test
 public void testDeserializeWithTypeValueNumberLong() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(long.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("12345678901234567890");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_VALUE_BOOLEAN
 */
 @Test
 public void testDeserializeWithTypeValueBoolean() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(boolean.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("true");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_NULL
 */
 @Test
 public void testDeserializeWithTypeNull() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(String.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("null");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_END_ARRAY
 */
 @Test
 public void testDeserializeWithTypeEndArray() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructArrayType(int[].class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_END_OBJECT
 */
 @Test
 public void testDeserializeWithTypeEndObject() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(MyClass.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"John\"}");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }

 /**
 * Test method for {@link NullifyingDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
 * with JsonTokenId.ID_UNDEFINED
 */
 @Test
 public void testDeserializeWithTypeUndefined() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(String.class);
 JsonParser jsonParser = objectMapper.getFactory().createParser("/* some undefined token */");
 DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
 TypeDeserializerProvider typeDeserializerProvider = objectMapper.getTypeDeserializers();
 Object result = new NullifyingDeserializer().deserializeWithType(jsonParser, deserializationContext, typeDeserializer, typeDeserializerProvider);
 assertNull(result);
 }
}