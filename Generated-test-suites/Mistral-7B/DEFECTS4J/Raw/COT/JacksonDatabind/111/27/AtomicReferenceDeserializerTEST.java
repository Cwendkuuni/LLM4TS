package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private AtomicReferenceDeserializer atomicReferenceDeserializer;
 private AtomicReference<Object> atomicReference;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 atomicReference = new AtomicReference<>();
 }

 @Test
 public void testConstructor() {
 // Test constructor
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, null, null);
 }

 @Test
 public void testWithResolved() throws Exception {
 // Test withResolved method
 TypeDeserializer typeDeser = new StdTypeDeserializer();
 JsonDeserializer<Object> valueDeser = new StdSerializer<Object>(Object.class) {};
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 new SimpleType(AtomicReference.class), null, typeDeser, valueDeser);
 AtomicReferenceDeserializer deserializer = atomicReferenceDeserializer
 .withResolved(typeDeser, valueDeser);
 assertSame(serializer, deserializer);
 }

 @Test
 public void testGetNullValue() throws Exception {
 // Test getNullValue method
 assertNull(atomicReferenceDeserializer.getNullValue(null));
 }

 @Test
 public void testGetEmptyValue() {
 // Test getEmptyValue method
 assertSame(atomicReference, atomicReferenceDeserializer.getEmptyValue(null));
 }

 @Test
 public void testReferenceValue() throws Exception {
 // Test referenceValue method
 Object contents = new Object();
 AtomicReference<Object> reference = atomicReferenceDeserializer.referenceValue(contents);
 assertSame(contents, reference.get());
 }

 @Test
 public void testGetReferenced() throws Exception {
 // Test getReferenced method
 Object originalContents = new Object();
 atomicReference.set(originalContents);
 Object referencedContents = atomicReferenceDeserializer.getReferenced(atomicReference);
 assertSame(originalContents, referencedContents);
 }

 @Test
 public void testUpdateReference() throws Exception {
 // Test updateReference method
 Object newContents = new Object();
 AtomicReference<Object> reference = atomicReferenceDeserializer.updateReference(atomicReference, newContents);
 assertSame(newContents, reference.get());
 }

 @Test(expected = JsonMappingException.class)
 public void testUnsupportedUpdate() throws Exception {
 // Test supportsUpdate method with unsupported update
 DeserializationConfig config = new DeserializationConfig(objectMapper.getConfig());
 config.setBeanDescription(new BeanDescription(Object.class));
 assertFalse(atomicReferenceDeserializer.supportsUpdate(config));
 }

 @Test
 public void testSupportsUpdate() throws Exception {
 // Test supportsUpdate method with supported update
 DeserializationConfig config = new DeserializationConfig(objectMapper.getConfig());
 config.setBeanDescription(new BeanDescription(AtomicReference.class));
 assertTrue(atomicReferenceDeserializer.supportsUpdate(config));
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 // Test deserialize method
 String json = "{\"@type\":\"com.fasterxml.jackson.databind.AtomicReference\",\"value\":123}";
 PropertyName propertyName = new PropertyName("value");
 Converter<Object,AtomicReference<Object>> atomicReferenceConverter = new StdConverter.StdValueConverter<Object, AtomicReference<Object>>(
 atomicReferenceDeserializer, AtomicReference.class, Object.class) {
 @Override public AtomicReference<Object> convert(Object value) {
 return atomicReferenceDeserializer.referenceValue(value);
 }
 };
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerConverter(atomicReferenceConverter);
 AtomicReference<Object> deserializedObject = mapper.readValue(json, AtomicReference.class);
 assertEquals(123, deserializedObject.get());
 }
}