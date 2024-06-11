package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonParser.EventType.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

 JavaType testType = null;
 TypeIdResolver idResolver = null;
 AsPropertyTypeDeserializer deserializer = null;

 @BeforeClass
 public static void setupClass() {
 testType = Object.class; // or any other JavaType
 idResolver = new StandardTypeIdResolver();
 }

 @Before
 public void setup() {
 deserializer = new AsPropertyTypeDeserializer(testType, idResolver, "typePropertyName", true, null, As.PROPERTY);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"typePropertyName\":\"String\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);

 jp = mapper.getFactory().createParser("{\"typePropertyName\":{\"type\":\"String\",\"value\":\"test\"}}");
 result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertEquals("test", result);

 jp = mapper.getFactory().createParser("{\"typePropertyName\":\"Integer\",\"value\":42}");
 result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"@class\":\"java.lang.String\",\"value\":\"test\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromObjectWhenNoTypePropertyNameFound() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"value\":\"test\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);

 jp = mapper.getFactory().createParser("[1]");
 result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertEquals(Integer.valueOf(1), result);
 }

 @Test
 public void testDeserializeTypedeUsingDefaultImplWithTokenBuffer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("[1]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TokenBuffer tb = new TokenBuffer(jp, ctxt);
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertEquals(Integer.valueOf(1), result);

 jp.nextToken();
 jp.nextToken(); // consume first two elements of the array
 jp.nextToken(); // consume value of the third element
 result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWhenNoArrayFound() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWithTokenBufferWhenNoArrayFound() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TokenBuffer tb = new TokenBuffer(jp, ctxt);
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);

 jp.nextToken(); // consume START_OBJECT
 result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithWrongTypePropertyName() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"anotherPropertyName\":\"String\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail();
 } catch (JsonMappingException e) {
 assertEquals("missing property 'typePropertyName' that is to contain type id  (for class java.lang.Object)", e.getMessage());
 }
 }

 @Test
 public void testDeserializeWithWrongTypePropertyNameAndTokenBuffer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"anotherPropertyName\":\"String\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TokenBuffer tb = new TokenBuffer(jp, ctxt);
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail();
 } catch (JsonMappingException e) {
 assertEquals("missing property 'typePropertyName' that is to contain type id  (for class java.lang.Object)", e.getMessage());
 }
 }

 @Test
 public void testDeserializeWithMissingTypePropertyName() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeWithMissingTypePropertyNameAndTokenBuffer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TokenBuffer tb = new TokenBuffer(jp, ctxt);
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }

 jp.nextToken(); // consume START_OBJECT
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeWithUnknownTypePropertyName() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"unknownPropertyName\":\"String\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithUnknownTypePropertyNameAndTokenBuffer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String.class, new StringDeserializer());
 mapper.registerModule(module);
 JsonParser jp = mapper.getFactory().createParser("{\"unknownPropertyName\":\"String\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TokenBuffer tb = new TokenBuffer(jp, ctxt);
 Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
 assertNull(result);

 jp.nextToken(); // consume START_OBJECT
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

}