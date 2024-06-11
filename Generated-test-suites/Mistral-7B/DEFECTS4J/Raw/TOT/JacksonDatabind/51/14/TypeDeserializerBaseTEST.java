package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private static final TypeDeserializerBase testee;
 static {
 try {
 testee = new MinimalTypeDeserializerBase(Object.class.getTypeName(), null, null, null, null);
 } catch (JsonProcessingException e) {
 throw new RuntimeException("Failed to instantiate testee", e);
 }
 }

 @Test
 public void testDefaultDeserializer() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{\"typeId\":null}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializerByTypeId() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{\"typeId\":\"java.lang.String\"}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 String expected = "Hello World!";
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext()).getValueAsString();
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializerByMissingTypeId() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerByUnknownTypeId() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{\"typeId\":\"UnknownType\"}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 testee.deserializeWithNativeTypeId(jp, new TestContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerByTypeIdWithWrongClass() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{\"typeId\":\"java.util.List\"}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 testee.deserializeWithNativeTypeId(jp, new TestContext());
 }

 @Test
 public void testDeserializerByDefaultImpl() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{\"typeId\":null}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertSame(NullifyingDeserializer.instance, result);
 }

 @Test
 public void testDeserializerByDefaultImplWithTypeId() throws IOException {
 ObjectNode node = new ObjectMapper().readValue("{\"typeId\":null}", ObjectNode.class);
 JsonParser jp = new ObjectMapper().treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContextWithTypeId("java.lang.String"));
 assertSame(NullifyingDeserializer.instance, result);
 }

 @Test
 public void testDeserializerByDefaultImplWithCustomDeserializer() throws IOException {
 final String expected = "Hello World!";
 JacksonAnnotationsModule module = new JacksonAnnotationsModule();
 module.addDeserializer(String.class, new StringDeserializer());
 ObjectMapper mapper = new ObjectMapper(module);
 ObjectNode node = mapper.readValue("{\"typeId\":null}", ObjectNode.class);
 JsonParser jp = mapper.treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertEquals(expected, result.getValueAsString());
 }

 @Test
 public void testDeserializerByCustomDeserializer() throws IOException {
 final String expected = "Hello World!";
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule() {{
 addDeserializer(String.class, new StringDeserializer());
 }});
 ObjectNode node = mapper.readValue("{\"typeId\":\"java.lang.String\"}", ObjectNode.class);
 JsonParser jp = mapper.treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertEquals(expected, result.getValueAsString());
 }

 @Test
 public void testDeserializerByCustomDeserializerWithTypeId() throws IOException {
 final String expected = "Hello World!";
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule() {{
 addDeserializer(String.class, new StringDeserializer());
 }});
 ObjectNode node = mapper.readValue("{\"typeId\":\"java.lang.String\"}", ObjectNode.class);
 JsonParser jp = mapper.treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContextWithTypeId("java.lang.String"));
 assertEquals(expected, result.getValueAsString());
 }

 @Test
 public void testDeserializerByCustomDeserializerWithMissingTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule() {{
 addDeserializer(String.class, new StringDeserializer());
 }});
 ObjectNode node = mapper.readValue("{}", ObjectNode.class);
 JsonParser jp = mapper.treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializerByCustomDeserializerWithUnknownTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule() {{
 addDeserializer(String.class, new StringDeserializer());
 }});
 ObjectNode node = mapper.readValue("{\"typeId\":\"UnknownType\"}", ObjectNode.class);
 JsonParser jp = mapper.treeAsTokens(node);
 Object result = testee.deserializeWithNativeTypeId(jp, new TestContext());
 assertNull(result);
 }

 private static class TestContext extends DeserializationContext {
 private TestContext() {
 super(null, null, new StandardDeserializationContext.Builder()
 .build());
 }

 private TestContext(StandardDeserializationContext ctx) {
 super(null, ctx);
 }

 private TestContext(TestContext base, String typeId) {
 super(base, new StandardDeserializationContext.Builder()
 .withTypeIdResolver(base._idResolver)
 .withTypeId(typeId)
 .withDefaultDeserializer(base._defaultImplDeserializer)
 .withFeatureSet(base._features)
 .build());
 }
 }

 private static class TestContextWithTypeId extends TestContext {
 private TestContextWithTypeId(String typeId) {
 super(null, typeId);
 }
 }

 private static class StringDeserializer extends JsonDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "Hello World!";
 }
 }
}