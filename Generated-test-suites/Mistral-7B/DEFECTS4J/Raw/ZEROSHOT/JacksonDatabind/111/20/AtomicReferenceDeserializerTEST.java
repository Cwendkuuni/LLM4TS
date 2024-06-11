package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerProvider.EmptyTypeDeserializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() throws JsonMappingException {
 mapper = new ObjectMapper(new JsonMapperConfig().withoutFeatures(JsonMapper.Feature.AUTO_DETECT_TYPES));
 mapper.registerValueHandler(new StdSerializer<Object>(Object.class) {}.withType(SimpleType.construct(Object.class, "java.lang.Object"))).forProperty("value");
 }

 @Test
 public void testNullValue() throws IOException, JsonProcessingException {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 DeserializationContext ctxt = createContext();
 AtomicReference<Object> actual = AtomicReferenceDeserializer.getInstance().getNullValue(ctxt);
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyValue() throws IOException, JsonProcessingException {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = AtomicReferenceDeserializer.getInstance().getEmptyValue(ctxt);
 assertEquals(expected, actual);
 }

 @Test
 public void testReferenceValue() throws IOException, JsonProcessingException {
 Object contents = new Object();
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = AtomicReferenceDeserializer.getInstance().referenceValue(contents);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws IOException, JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = AtomicReferenceDeserializer.getInstance().getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws IOException, JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newValue = new Object();
 AtomicReference<Object> updatedReference = AtomicReferenceDeserializer.getInstance().updateReference(reference, newValue);
 assertEquals(updatedReference, reference);
 assertEquals(newValue, reference.get());
 }

 @Test
 public void testSupportsUpdate() throws IOException, JsonProcessingException {
 assertTrue(AtomicReferenceDeserializer.getInstance().supportsUpdate(null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws IOException, JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 try {
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, new Object());
 } catch (IllegalStateException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateNullValue() throws IOException, JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
 AtomicReference<Object> reference = null;
 try {
 deserializer.updateReference(reference, new Object());
 } catch (IllegalArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserialize() throws IOException, JsonProcessingException {
 JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, Object.class);
 String json = "{\"value\":1}";
 Object actual = mapper.readValue(json, type);
 AtomicReference<Object> expected = new AtomicReference<Object>(1);
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws IOException, JsonProcessingException {
 JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, String.class);
 String json = "{\"value\":\"hello\"}";
 ObjectMapper mapperWithTypeDeserializer = new ObjectMapper(new JsonMapperConfig().withoutFeatures(JsonMapper.Feature.AUTO_DETECT_TYPES));
 mapperWithTypeDeserializer.registerTypeDeserializer(type, new AtomicReferenceDeserializer(type, null, new EmptyTypeDeserializerProvider(), new EmptyTypeDeserializer()));
 Object actual = mapperWithTypeDeserializer.readValue(json, type);
 AtomicReference<String> expected = new AtomicReference<String>("hello");
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeWithValueInstantiator() throws IOException, JsonProcessingException {
 JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, Integer.class);
 String json = "{\"value\":1}";
 ObjectMapper mapperWithValueInstantiator = new ObjectMapper(new JsonMapperConfig().withoutFeatures(JsonMapper.Feature.AUTO_DETECT_TYPES));
 mapperWithValueInstantiator.registerValueHandler(new ValueInstantiator() {
 @SuppressWarnings("unchecked")
 @Override
 public Object instantiateValue(DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
 return new AtomicReference<Integer>(1);
 }
 });
 Object actual = mapperWithValueInstantiator.readValue(json, type);
 AtomicReference<Integer> expected = new AtomicReference<Integer>(1);
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeWithTypeDeserializerAndValueInstantiator() throws IOException, JsonProcessingException {
 JavaType type = mapper.getTypeFactory().constructType(AtomicReference.class, Integer.class);
 String json = "{\"value\":1}";
 ObjectMapper mapperWithTypeDeserializerAndValueInstantiator = new ObjectMapper(new JsonMapperConfig().withoutFeatures(JsonMapper.Feature.AUTO_DETECT_TYPES));
 mapperWithTypeDeserializerAndValueInstantiator.registerValueHandler(new ValueInstantiator() {
 @SuppressWarnings("unchecked")
 @Override
 public Object instantiateValue(DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
 return new AtomicReference<Integer>(1);
 }
 });
 mapperWithTypeDeserializerAndValueInstantiator.registerTypeDeserializer(type, new AtomicReferenceDeserializer(type, null, new EmptyTypeDeserializerProvider(), new EmptyTypeDeserializer()));
 Object actual = mapperWithTypeDeserializerAndValueInstantiator.readValue(json, type);
 AtomicReference<Integer> expected = new AtomicReference<Integer>(1);
 assertEquals(expected, actual);
 }

 private DeserializationContext createContext() {
 return mapper.getDeserializationContext().withValueHandler(new StdSerializer<Object>(Object.class) {}.withType(SimpleType.construct(Object.class, "java.lang.Object"))).withoutActiveValueHandler();
 }

}