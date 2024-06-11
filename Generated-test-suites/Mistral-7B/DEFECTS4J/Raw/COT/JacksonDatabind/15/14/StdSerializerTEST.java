package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMapper;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationTest;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest extends StdDeserializationTest {

 private StdSerializer<Integer> intSerializer;
 private StdSerializer<String> strSerializer;

 @Before
 public void setup() throws Exception {
 JsonMapper mapper = new JsonMapper();
 intSerializer = mapper.constructorArgsFactory().constructArg(StdSerializer.class, Integer.class);
 strSerializer = mapper.constructorArgsFactory().constructArg(StdSerializer.class, String.class);
 }

 @Test
 public void testStdSerializerConstructor() {
 Class<?> handledType = intSerializer.handledType();
 assertEquals(Integer.class, handledType);
 }

 @Test
 public void testAbstractSerializeMethod() throws IOException {
 Integer value = 123;
 ObjectNode resultNode = mapper.createObjectNode();
 intSerializer.serialize(value, resultNode.getObjectPtr(), new SerializerProvider());
 ObjectNode result = resultNode;
 assertEquals(Integer.toString(value), result.get("@type").asText());
 assertEquals(value.toString(), result.get("value").asText());
 }

 @Test
 public void testGetSchemaMethod() throws JsonMappingException {
 ObjectNode schema = intSerializer.getSchema(new SerializerProvider(), null);
 assertEquals("number", schema.get("type").asText());
 }

 @Test
 public void testGetSchemaMethodWithOptionalFlag() throws JsonMappingException {
 ObjectNode schema = intSerializer.getSchema(new SerializerProvider(), null, true);
 assertEquals("number", schema.get("type").asText());
 schema.put("required", false);
 }

 @Test
 public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
 JsonNode schema = jsonNodeFactory.objectNode();
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(schema, null);
 intSerializer.acceptJsonFormatVisitor(visitor, null);
 assertEquals("[Number]", schema.get("properties").fields().next().getValueAsTree().asText());
 }

 @Test(expected = JsonMappingException.class)
 public void testWrapAndThrowMethodWithJsonMappingException() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 intSerializer.wrapAndThrow(provider, new JsonMappingException("Test error"), null, null);
 fail("Expected JsonMappingException to be thrown but no exception was thrown.");
 }

 @Test
 public void testWrapAndThrowMethodWithIOException() throws IOException {
 SerializerProvider provider = new SerializerProvider();
 intSerializer.wrapAndThrow(provider, new IOException("Test error"), null, null);
 fail("Expected IOException to be thrown but no exception was thrown.");
 }

 @Test
 public void testIsDefaultSerializerMethod() {
 assertTrue(intSerializer.isDefaultSerializer(intSerializer));
 assertFalse(intSerializer.isDefaultSerializer(new StdSerializer<String>(String.class, false)));
 }

 @Test
 public void testFindConvertingContentSerializerMethod() throws JsonMappingException {
 ObjectNode delegateNode = jsonNodeFactory.objectNode();
 delegateNode.put("type", "java.util.List");
 delegateNode.put("value", jsonNodeFactory.arrayNode(new ObjectNode[]{ jsonNodeFactory.objectNode() }));
 SerializerProvider provider = new SerializerProvider();
 provider.setDefaultPropertyInclusion(JsonProperty.Value.ALWAYS);
 JsonSerializer<Object> listSerializer = provider.findValueSerializer(JsonType.construct(ClassUtil.makeReferenceType(List.class, Integer.class)));
 JsonSerializer<Object> converterSerializer = intSerializer.findConvertingContentSerializer(provider, null, delegateNode);
 assertEquals(Integer[].class, converterSerializer.handledType());
 }

 @Test
 public void testFindPropertyFilterMethod() throws JsonMappingException {
 SerializerProvider provider = new SerializerProvider();
 PropertyFilter filter = intSerializer.findPropertyFilter(provider, "testFilter", null);
 assertNull(filter);
 filter = intSerializer.findPropertyFilter(provider, "filterName", jsonNodeFactory.numberNode(42));
 assertNotNull(filter);
 }

 @Test
 public void testAcceptJsonFormatVisitorWithCustomSchema() throws JsonMappingException {
 ObjectNode schemaNode = createCustomSchemaNode();
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(schemaNode, null);
 intSerializer.acceptJsonFormatVisitor(visitor, Integer.class);
 ObjectNode schema = (ObjectNode) schemaNode.get("properties").get("value").get("properties").get("value");
 assertEquals("number", schema.get("type").asText());
 schema.put("minimum", 123);
 schema.put("maximum", 456);
 }

 @Test
 public void testAcceptJsonFormatVisitorWithCustomSchemaAndOptionalFlag() throws JsonMappingException {
 ObjectNode schemaNode = createCustomSchemaNode();
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(schemaNode, null);
 intSerializer.acceptJsonFormatVisitor(visitor, Integer.class, true);
 ObjectNode schema = (ObjectNode) schemaNode.get("properties").get("value").get("properties").get("value");
 assertEquals("number", schema.get("type").asText());
 schema.put("minimum", 123);
 schema.put("maximum", 456);
 schema.put("required", false);
 }

 private ObjectNode createCustomSchemaNode() {
 ObjectNode schemaNode = jsonNodeFactory.objectNode();
 ObjectNode propertiesNode = jsonNodeFactory.objectNode();
 ObjectNode valueNode = jsonNodeFactory.objectNode();
 propertiesNode.put("value", valueNode);
 schemaNode.put("properties", propertiesNode);
 return schemaNode;
 }

}