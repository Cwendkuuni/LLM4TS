package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testCollectionDeserializerCreation() throws Exception {
 JavaType collectionType = mapper.getTypeFactory().constructParametricType(Collection.class, String.class);
 JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
 TypeDeserializer valueTypeDeserializer = mapper.findValueTypeDeserializer(String.class, null);
 ValueInstantiator valueInstantiator = mapper.findValueInstantiator(String.class);
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator);
 assertEquals(collectionType, collectionDeserializer.getType());
 assertEquals(valueDeserializer, collectionDeserializer.getValueDeserializer());
 assertEquals(valueTypeDeserializer, collectionDeserializer.getValueTypeDeserializer());
 assertEquals(valueInstantiator, collectionDeserializer.getValueInstantiator());
 }

 @Test
 public void testCollectionDeserializerIsCachable() throws Exception {
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructParametricType(Collection.class, String.class), null, null, null);
 assertFalse(collectionDeserializer.isCachable());
 }

 @Test
 public void testCreateContextual() throws Exception {
 BeanProperty property = new BeanProperty("testProperty", mapper.getTypeFactory().constructParametricType(Collection.class, String.class));
 JsonDeserializer<String> delegateDeserializer = mapper.findValueDeserializer(String.class);
 TypeDeserializer valueTypeDeserializer = mapper.findValueTypeDeserializer(String.class, null);
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructParametricType(Collection.class, String.class), null, valueTypeDeserializer, null);
 CollectionDeserializer contextualDeserializer = collectionDeserializer.createContextual(mapper.getDeserializationContext(), property);
 assertEquals(collectionDeserializer, contextualDeserializer);
 }

 @Test
 public void testDeserializeEmptyCollection() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[]");
 Collection<String> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertTrue(collection.isEmpty());
 }

 @Test
 public void testDeserializeCollectionWithSingleElement() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[\"test\"]");
 Collection<String> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(1, collection.size());
 assertEquals("test", collection.iterator().next());
 }

 @Test
 public void testDeserializeCollectionWithMultipleElements() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[\"test1\", \"test2\"]");
 Collection<String> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(2, collection.size());
 List<String> list = new ArrayList<>(collection);
 assertEquals("test1", list.get(0));
 assertEquals("test2", list.get(1));
 }

 @Test
 public void testDeserializeCollectionWithNullElement() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[null]");
 Collection<String> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(1, collection.size());
 assertNull(collection.iterator().next());
 }

 @Test
 public void testDeserializeCollectionWithTypeInformation() throws Exception {
 JavaType collectionType = mapper.getTypeFactory().constructParametricType(Collection.class, Integer.class);
 JsonDeserializer<Integer> valueDeserializer = mapper.findValueDeserializer(Integer.class);
 TypeDeserializer valueTypeDeserializer = mapper.findValueTypeDeserializer(Integer.class, null);
 ValueInstantiator valueInstantiator = mapper.findValueInstantiator(Integer.class);
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator);
 JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
 Collection<Integer> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(3, collection.size());
 List<Integer> list = new ArrayList<>(collection);
 assertEquals(1, list.get(0).intValue());
 assertEquals(2, list.get(1).intValue());
 assertEquals(3, list.get(2).intValue());
 }

 @Test
 public void testDeserializeCollectionWithDelegateDeserializer() throws Exception {
 JsonDeserializer<List<String>> listDeserializer = mapper.findValueDeserializer(List.class);
 JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
 TypeDeserializer stringTypeDeserializer = mapper.findValueTypeDeserializer(String.class, null);
 ValueInstantiator stringValueInstantiator = mapper.findValueInstantiator(String.class);
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructParametricType(Collection.class, String.class), null, null, null);
 CollectionDeserializer contextualDeserializer = collectionDeserializer.withResolved(listDeserializer, stringDeserializer, stringTypeDeserializer);
 JsonParser parser = mapper.getFactory().createParser("[\"test1\", \"test2\"]");
 Collection<String> collection = mapper.readValue(parser, contextualDeserializer.getType()).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(2, collection.size());
 List<String> list = new ArrayList<>(collection);
 assertEquals("test1", list.get(0));
 assertEquals("test2", list.get(1));
 }

 @Test
 public void testDeserializeCollectionWithSingleValueAsArray() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[\"]");
 Collection<String> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(0, collection.size());
 }

 @Test
 public void testDeserializeCollectionWithSingleValueAsArrayWrapped() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[[]]");
 Collection<String> collection = mapper.readValue(parser, CollectionDeserializer.class).deserialize(parser, mapper.getDeserializationContext());
 assertEquals(0, collection.size());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeCollectionWithUnexpectedToken() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[{ \"test\": \"value\" }]");
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructParametricType(Collection.class, String.class), null, null, null);
 Collection<String> collection = collectionDeserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeCollectionWithInvalidType() throws Exception {
 JsonParser parser = mapper.getFactory().createParser("[1]");
 CollectionDeserializer collectionDeserializer = new CollectionDeserializer(mapper.getTypeFactory().constructParametricType(Collection.class, Integer.class), null, null, null);
 Collection<String> collection = (Collection<String>) collectionDeserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }
}