package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NullifyingDeserializerTest {

 private static final ObjectNode EMPTY_JSON = JsonFactory.getInstance().createObjectNode();

 @Test
 public void testDeserializeNullifyingDeserializerWhenRootIsArray() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createValueNode("[{}]");
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWhenRootIsObject() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createObjectNode();
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWhenRootIsFieldName() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createObjectNode();
 json.put("name", JsonFactory.getInstance().createValueNode("value"));
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWhenRootIsValue() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createValueNode("null");
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWhenRootIsStartObject() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createObjectNode();
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 parser.nextToken(); // idling to consume [
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWhenRootIsStartArray() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createValueNode("[{}]");
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 parser.nextToken(); // idling to consume [
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializerWhenRootIsArray() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createValueNode("[{}]");
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializerWhenRootIsObject() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createObjectNode();
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializerWhenRootIsFieldName() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createObjectNode();
 json.put("name", JsonFactory.getInstance().createValueNode("value"));
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializerWhenRootIsValue() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createValueNode("null");
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializerWhenRootIsStartObject() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createObjectNode();
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 parser.nextToken(); // idling to consume [
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithTypeDeserializerWhenRootIsStartArray() throws Exception {
 ObjectNode json = JsonFactory.getInstance().createValueNode("[{}]");
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 parser.nextToken(); // idling to consume [
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithEmptyJson() throws Exception {
 ObjectNode json = EMPTY_JSON;
 JsonParser parser = new JsonFactory().createParser(json);
 DeserializationContext ctxt = new TestDeserializationContext();
 Object result = new NullifyingDeserializer().deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithEmptyJsonAndTypeDeserializer() throws Exception {
 ObjectNode json = EMPTY_JSON;
 JsonParser parser = new JsonFactory().createParser(json);
 ObjectMapper mapper = new ObjectMapper();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class);
 Object result = new NullifyingDeserializer().deserializeWithType(parser, new TestDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

}

 class TestDeserializationContext extends DeserializationContext {
 @Override
 public void handleUnexpectedToken(JsonParser p, JsonToken t) throws IOException {
 fail("Unexpected token: " + t);
 }

 @Override
 public void handleMissingValueException(MissingValueException mve) throws IOException {
 fail("Missing value exception: " + mve.getMessage());
 }

 @Override
 public void handleNonInstancableValue(Class<?> valueType) throws IOException {
 fail("Non instancable value: " + valueType);
 }

 @Override
 public void handleInstantiationFailed(Class<?> valueType, String name) throws IOException {
 fail("Instantiation failed: " + valueType + ", " + name);
 }

 @Override
 public void handleInvalidTypeId(String typeName, int id) throws IOException {
 fail("Invalid type id: " + typeName + ", " + id);
 }

 @Override
 public void handleUnknownProperty(String name, Object bean) throws IOException {
 fail("Unknown property: " + name + ", " + bean.getClass());
 }

 @Override
 public void handleMissingProperty(String name, Object bean) throws IOException {
 fail("Missing property: " + name + ", " + bean.getClass());
 }

 @Override
 public void handlePropertyMissingValue(String name, Object bean) throws IOException {
 fail("Property missing value: " + name + ", " + bean.getClass());
 }

 @Override
 public void handleNonWritableProperty(String name, Object bean) throws IOException {
 fail("Non-writable property: " + name + ", " + bean.getClass());
 }

 @Override
 public void handleNonConfigurableInstance(Object instance) throws IOException {
 fail("Non-configurable instance: " + instance.getClass());
 }

 @Override
 public void handleUnresolvableValue(Object value) throws IOException {
 fail("Unresolvable value: " + value);
 }

 @Override
 public void handleInvalidDefinition(Class<?> valueType, PropertyName name, JsonDeserializer<?> deserializer) throws IOException {
 fail("Invalid definition: " + valueType + ", " + name + ", " + deserializer);
 }
}