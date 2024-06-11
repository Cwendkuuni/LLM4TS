import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

 private static final ObjectNode TEST_DATA = JsonFactory.instance.createObjectNode();
 private static final StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(null);

 @Test
 public void testSerializeNull() throws IOException {
 Object nullValue = null;
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(nullValue, gen, new SerializerProvider());
 assertTrue(gen.getOutputBuffer().isEmpty());
 }

 @Test
 public void testSerializeEmptyList() throws IOException {
 Object emptyList = new ArrayList<>();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(emptyList, gen, new SerializerProvider());
 assertNotNull(gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeEmptyMap() throws IOException {
 Object emptyMap = new HashMap<>();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(emptyMap, gen, new SerializerProvider());
 assertNotNull(gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeObject() throws IOException {
 Object testObject = new Object();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(testObject, gen, new SerializerProvider());
 assertNotNull(gen.getOutputBuffer().toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeDifferentType() throws IOException {
 Object testObject = new int[1];
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(testObject, gen, new SerializerProvider());
 }

 @Test
 public void testEmptySchema() throws JsonMappingException {
 ObjectNode testData = JsonFactory.instance.createObjectNode();
 SerializerProvider provider = new StandardSerializerProvider();
 JsonSchemaProvider schemaProvider = new JsonSchemaProvider();
 ObjectNode schema = serializer.getSchema(provider, Object.class.getType());
 testData.put("schema", schema);
 assertEquals(TestData.EmptySchema.class.getName(), testData.toString());
 }

 @Test
 public void testSchemaWithObject() throws IOException, JsonMappingException {
 Object testObject = new Object();
 ObjectNode testData = JsonFactory.instance.createObjectNode();
 SerializerProvider provider = new StandardSerializerProvider();
 JsonSchemaProvider schemaProvider = new JsonSchemaProvider();
 ObjectNode schema = serializer.getSchema(provider, Object.class.getType());
 testData.put("schema", schema);
 SerializerProvider schemaProvider2 = new StandardSerializerProvider();
 ObjectNode schema2 = serializer.getSchema(schemaProvider2, Object.class.getType());
 testData.put("schema2", schema2);
 JsonNode expectedSchema = JsonFactory.instance.createObjectNode();
 expectedSchema.put("$ref", "#/definitions/Object");
 ObjectNode definition = expectedSchema.deepCopy();
 definition.setMixIn("@JsonSerializer", serializer.getClass().getName());
 expectedSchema.set("definitions", expectedSchema);
 expectedSchema.set("properties", expectedSchema);
 assertEquals(expectedSchema.toString(), testData.toString());
 }

 @Test
 public void testSerializeWithType() throws IOException {
 Object testObject = new Object();
 ObjectNode testData = JsonFactory.instance.instance.createObjectNode();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 TypeSerializer typeSerializer = new TypeSerializer() {
 @Override
 public void serializeValue(JsonGenerator gen, Object value, TypeSerializer typeSerializer)
 throws IOException {
 gen.writeStringField("type", "Object");
 }

 @Override
 public Object deserializeValue(JsonParser p, DeserializationContext ctxt, Type type)
 throws IOException, JsonProcessingException {
 return Object.class;
 }
 };
 Type serializedType = TypeFactory.defaultInstance.constructType(Object.class, String.class);
 TypeSerializer typeSerializer2 = new TypeSerializer() {
 @Override
 public void serializeValue(JsonGenerator gen, Object value, TypeSerializer typeSerializer)
 throws IOException {
 gen.writeStringField("type", "ObjectType");
 }

 @Override
 public Object deserializeValue(JsonParser p, DeserializationContext ctxt, Type type)
 throws IOException, JsonProcessingException {
 return ObjectType.class;
 }
 };
 serializer.serializeWithType(testObject, gen, new SerializerProvider(), typeSerializer);
 gen.close();
 ObjectNode actualSchema = JsonFactory.instance.parse(gen.getOutputBuffer().toString()).getObjectValue();
 ObjectNode expectedSchema = JsonFactory.instance.createObjectNode();
 expectedSchema.put("$schema", "http://json-schema.org/draft-07/schema#");
 expectedSchema.put("title", "Root");
 expectedSchema.put("type", "object");
 expectedSchema.put("properties", JsonFactory.instance.createObjectNode());
 ObjectNode properties = expectedSchema.getObject("properties");
 properties.put("type", JsonFactory.instance.createObjectNode());
 ObjectNode type = properties.getObject("type");
 type.setMixIn("@JsonSerializer", serializer.getClass().getName());
 type.put("$ref", "#/definitions/Object");
 expectedSchema.put("definitions", expectedSchema);
 JsonNode actualJson = JsonFactory.instance.parse(gen.getOutputBuffer().toString());
 JsonNode expectedJson = JsonFactory.instance.parse(expectedSchema.toString());
 assertEquals(expectedJson.toString(), actualJson.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testMalformedJsonInput() throws IOException {
 ObjectNode testData = JsonFactory.instance.createObjectNode();
 JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream("{\"test\":{"));
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectNode schema = serializer.getSchema(provider, Object.class.getType());
 testData.put("schema", schema);
 ObjectNode schema2 = serializer.getSchema(provider, Object.class.getType());
 testData.put("schema2", schema2);
 ObjectNode schema3 = serializer.getSchema(provider, Object.class.getType());
 testData.put("schema3", schema3);
 ObjectNode schema4 = serializer.getSchema(provider, Object.class.getType());
 testData.put("schema4", schema4);
 testData.setVisitorHandler(new JsonSchemaVisitorHandler());
 parser.readValueAs(JsonNode.class, testData);
 }

 @Test
 public void testSerializeWithCustomConverter() throws IOException {
 Object testObject = new TestObject();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new StdDelegatingSerializer<>(new CustomConverter(), null, null);
 serializer.serialize(testObject, gen, new SerializerProvider());
 gen.close();
 ObjectNode actualSchema = JsonFactory.instance.parse(gen.getOutputBuffer().toString()).getObjectValue();
 ObjectNode expectedSchema = new ObjectMapper().readValue(getClass().getResourceAsStream("/expected_output.json"), ObjectNode.class);
 assertEquals(expectedSchema.toString(), actualSchema.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithNullConverter() throws IOException {
 Object testObject = new Object();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new StdDelegatingSerializer<>(null, null, null);
 serializer.serialize(testObject, gen, new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithNullDelegateSerializer() throws IOException {
 Object testObject = new Object();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new StdDelegatingSerializer<>(new CustomConverter(), null, null);
 serializer._delegateSerializer = null;
 serializer.serialize(testObject, gen, new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithNullDelegateType() throws IOException {
 Object testObject = new Object();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new StdDelegatingSerializer<>(new CustomConverter(), null, null);
 serializer._delegateType = null;
 serializer.serialize(testObject, gen, new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidDelegateType() throws IOException {
 Object testObject = new Object();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new StdDelegatingSerializer<>(new CustomConverter(), new TypeReference<String>() {}, null);
 serializer._delegateSerializer = new StdSerializer<String>(String.class) {};
 serializer._delegateType = null;
 serializer.serialize(testObject, gen, new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithInvalidDelegateSerializerClass() throws IOException {
 Object testObject = new Object();
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = new StdDelegatingSerializer<>(new CustomConverter(), new TypeReference<String>() {}, null);
 serializer._delegateSerializer = new StdSerializer<String>(String.class) {};
 serializer._delegateSerializer = new Object();
 serializer.serialize(testObject, gen, new SerializerProvider());
 }

 @Test
 public void testResolveSerializer() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 SerializerProvider provider = new StandardSerializerProvider();
 JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
 JsonSerializer<Object> resolvedSerializer = serializer.resolve(provider);
 assertEquals(delegateSerializer.getClass(), resolvedSerializer.getClass());
 }

 @Test
 public void testCreateContextual() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 SerializerProvider provider = new StandardSerializerProvider();
 JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
 JsonSerializer<Object> contextualSerializer = serializer.createContextual(provider, null);
 assertNotEquals(delegateSerializer, contextualSerializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualWithNullProvider() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 JsonSerializer<Object> contextualSerializer = serializer.createContextual(null, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateContextualWithNullProperty() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 SerializerProvider provider = new StandardSerializerProvider();
 JsonSerializer<Object> contextualSerializer = serializer.createContextual(provider, null);
 }

 @Test
 public void testWithDelegate() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 SerializerProvider provider = new StandardSerializerProvider();
 JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
 StdDelegatingSerializer<Object> serializer1 = new StdDelegatingSerializer<>(new CustomConverter(), null, delegateSerializer);
 StdDelegatingSerializer<Object> serializer2 = serializer1.withDelegate(new CustomConverter(), null, null);
 assertSame(serializer1, serializer2);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper()) {
 @Override
 public void visitArrayFormat(JsonFormatVisitor visitor, JsonFormatVisitor provider) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitObjectFormat(JsonFormatVisitor visitor, JsonFormatVisitor provider)
 throws JsonMappingException {
 visitor.visitObjectField("type", null, null);
 visitor.visitObjectField("value", null, null);
 }

 @Override
 public void visitStringFormat(JsonFormatVisitor visitor, JsonFormatVisitor provider) throws JsonMappingException {
 fail();
 }
 };
 serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorWithNullVisitor() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 serializer.acceptJsonFormatVisitor(null, Object.class.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorWithNullType() throws IOException, JsonMappingException {
 Object testObject = new TestObject();
 serializer.acceptJsonFormatVisitor(new ObjectMapper(), null);
 }

 private class CustomConverter implements Converter<Object, String> {
 @Override
 public String convert(Object value) {
 return "customValue";
 }
 }

 private class TestObject {
 // empty class for testing
 }

 private static class JsonSchemaVisitorHandler extends JsonSchemaVisitor {
 @Override
 public void visitSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitObjectSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitArraySchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitBooleanSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitNumberSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitStringSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitNullSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }

 @Override
 public void visitEnumSchema(JsonSchema schema) throws JsonMappingException {
 fail();
 }
 }
}