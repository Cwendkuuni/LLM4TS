package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class AtomicReferenceDeserializerTest {

 private AtomicReferenceDeserializer deserializer;
 private ObjectMapper mapper;
 private JsonDeserializer<?> valueDeserializer;
 private TypeDeserializer typeDeserializer;
 private ValueInstantiator valueInstantiator;

 @Before
 public void setup() throws JsonProcessingException {
 mapper = new ObjectMapper();
 valueDeserializer = mock(JsonDeserializer.class);
 typeDeserializer = mock(TypeDeserializer.class);
 valueInstantiator = mock(ValueInstantiator.class);
 deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<Object>>() {}, valueInstantiator, typeDeserializer, valueDeserializer);
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReference<Object> actual = deserializer.getNullValue(mock(DeserializationContext.class));
 assertNull(actual);
 }

 @Test
 public void testGetEmptyValue() throws JsonMappingException {
 AtomicReference<Object> actual = deserializer.getEmptyValue(mock(DeserializationContext.class));
 assertEquals(new AtomicReference<Object>(), actual);
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object contents = new Object();
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = deserializer.referenceValue(contents);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object actual = deserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReference<Object> actual = deserializer.updateReference(reference, newContents);
 assertEquals(newContents, actual.get());
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException {
 Object config = mock(DeserializationConfig.class);
 when(config.canOverride(TypeDeserializer.None.class)).thenReturn(false);
 boolean actual = deserializer.supportsUpdate(config);
 assertEquals(true, actual);
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 ObjectReader reader = mapper.readerForArrayOf(AtomicReference.class);
 String json = "[]";
 AtomicReference<?>[] actual = reader.readValuesAs(new TypeReference<AtomicReference<?>[]>() {}).toArray(new AtomicReference[0]);
 assertEquals(new AtomicReference[0].length, actual.length);
 for (int i = 0; i < actual.length; i++) {
 assertEquals(new AtomicReference<Object>(), actual[i]);
 }
 }

 @Test
 public void testDeserializeSingleNull() throws IOException {
 ObjectReader reader = mapper.readerForValue(AtomicReference.class);
 String json = "null";
 AtomicReference<Object> actual = reader.readValueAs(AtomicReference.class);
 assertNull(actual);
 }

 @Test
 public void testDeserializeSingleValue() throws IOException {
 ObjectReader reader = mapper.readerForValue(AtomicReference.class);
 String json = "{\"value\":1}";
 Object actual = reader.readValueAs(Object.class);
 AtomicReference<Object> expected = new AtomicReference<Object>(1);
 assertEquals(expected, deserializer.deserialize(mapper, null, json, Object.class, null));
 assertEquals(expected.get(), actual);
 }

 @Test
 public void testDeserializeSingleValueWithName() throws IOException {
 ObjectReader reader = mapper.readerForValue(AtomicReference.class);
 String json = "{\"value\":1}";
 String fieldName = "myField";
 Object actual = reader.readValueAs(Object.class);
 AtomicReference<Object> expected = new AtomicReference<Object>(1);
 ObjectDeserializer deserializer = mapper.findValueDeserializer(AtomicReference.class, new NameTransformer("myField", ""));
 deserializer.handleNullValue(null, null);
 expected = (AtomicReference<Object>) deserializer.deserialize(mapper, null, json, new TypeReference<AtomicReference<Object>>() {}, null);
 assertEquals(expected, deserializer.handleValueDeserialization(actual, null, null));
 assertEquals(expected.get(), deserializer.handleValueDeserialization(actual, null, null).get());
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ObjectReader reader = mapper.readerForListOf(AtomicReference.class);
 String json = "[{\"value\":1},{\"value\":2}]";
 AtomicReference<?>[] actual = reader.readValuesAs(new TypeReference<AtomicReference<?>[]>() {});
 AtomicReference<Object> expected1 = new AtomicReference<Object>(1);
 AtomicReference<Object> expected2 = new AtomicReference<Object>(2);
 assertEquals(2, actual.length);
 assertEquals(expected1, actual[0]);
 assertEquals(expected2, actual[1]);
 }
}