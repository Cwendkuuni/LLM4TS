package com.example;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 @Test
 public void testJsonValueSerializer() throws Exception {
 JsonValueSerializer serializer = new JsonValueSerializer(createTestMethod("getJsonValue"), null);
 JsonNode actual = mapper.writeValueAsNode(serializer, new TestClass());
 JsonNode expected = mapper.readTree("{\"jsonValue\":\"expectedValue\"}");
 assertEquals(expected, actual);
 }

 @Test
 public void testJsonValueSerializerWithValueSerializer() throws Exception {
 JsonValueSerializer serializer = new JsonValueSerializer(createTestMethod("getJsonValue"), new StringSerializer(mapper.getSerializerProvider()));
 JsonNode actual = mapper.writeValueAsNode(serializer, new TestClass());
 JsonNode expected = mapper.readTree("{\"jsonValue\":\"expectedValue\"}");
 assertEquals(expected, actual);
 }

 @Test
 public void testJsonValueSerializerWithContextualSerializer() throws Exception {
 ContextualSerializer contextualSerializer = mapper.findContextualSerializer(TestClass.class, mapper.getSerializerProvider());
 JsonValueSerializer serializer = new JsonValueSerializer(contextualSerializer, null, null, false);
 JsonNode actual = mapper.writeValueAsNode(serializer, new TestClass());
 JsonNode expected = mapper.readTree("{\"jsonValue\":\"expectedValue\"}");
 assertEquals(expected, actual);
 }

 @Test
 public void testJsonValueSerializerWithContextualSerializerAndProperty() throws Exception {
 BeanProperty property = new BeanProperty("jsonProperty", TestClass.class, "getJsonProperty", null, null, null, null, null, null, null);
 ContextualSerializer contextualSerializer = mapper.findContextualSerializer(TestClass.class, mapper.getSerializerProvider());
 JsonValueSerializer serializer = new JsonValueSerializer(contextualSerializer, property, null, false);
 JsonNode actual = mapper.writeValueAsNode(serializer, new TestClass());
 JsonNode expected = mapper.readTree("{\"jsonProperty\":\"expectedValue\"}");
 assertEquals(expected, actual);
 }

 @Test
 public void testJsonValueSerializerWithContextualSerializerAndForceTypeInformation() throws Exception {
 BeanProperty property = new BeanProperty("jsonProperty", TestClass.class, "getJsonProperty", null, null, null, null, null, null, null);
 JsonValueSerializer serializer = new JsonValueSerializer(createTestMethod("getJsonValue"), property, new StringSerializer(mapper.getSerializerProvider()), true);
 JsonNode actual = mapper.writeValueAsNode(serializer, new TestClass());
 JsonNode expected = mapper.readTree("\"expectedType\":\"expectedValue\"");
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonValueSerializerWithNullValue() throws Exception {
 JsonValueSerializer serializer = new JsonValueSerializer(createTestMethod("getJsonValue"), null);
 JsonNode actual = mapper.writeValueAsNode(serializer, null);
 fail("Should throw JsonMappingException for null values");
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonValueSerializerWithException() throws Exception {
 JsonValueSerializer serializer = new JsonValueSerializer(createTestMethodThrowingException("getJsonValue"), null);
 try {
 JsonNode actual = mapper.writeValueAsNode(serializer, new TestClass());
 fail("Should throw JsonMappingException for methods throwing exceptions");
 } catch (JsonMappingException e) {
 }
 }

 @Test
 public void testJsonValueSerializerAcceptJsonFormatVisitor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.schemaGenerator(EmptyContext.instance()).findSchemaProvider(new TypeReference<JsonNode>() {});
 JsonValueSerializer serializer = new JsonValueSerializer(createTestMethod("getJsonValue"), null);
 serializer.acceptJsonFormatVisitor(visitor, null);
 JsonNode actual = visitor.getSchema();
 JsonNode expected = mapper.readTree("{\"type\":\"object\",\"properties\":{\"jsonValue\":{\"type\":\"string\"}}}");
 assertEquals(expected, actual);
 }

 private Method createTestMethod(String methodName) throws NoSuchMethodException {return TestClass.class.getMethod(methodName);}

 static class TestClass {
 private String jsonValue;

 public String getJsonValue() {return jsonValue;}
 public void setJsonValue(String jsonValue) {this.jsonValue = jsonValue;}

 public String getJsonProperty() {return jsonValue;}
 }

 static class StringSerializer extends StdSerializer<String> {
 StringSerializer(SerializerProvider provider) {this(null, provider);}
 StringSerializer(Class<String> t, SerializerProvider provider) {super(t, provider);}

 @Override public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {gen.writeString(value);}
 }
}