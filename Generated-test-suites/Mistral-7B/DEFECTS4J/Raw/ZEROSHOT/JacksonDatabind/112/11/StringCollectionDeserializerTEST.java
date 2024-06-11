package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCollectionDeserializerTest {

 @BeforeAll
 static void setup() {
 // Initialize any static variables or setup any test data here
 }

 @AfterAll
 static void tearDown() {
 // Perform any necessary cleanup here
 }

 @Test
 void testDefaultDeserialization() throws Exception {
 // Test default deserialization of an empty Collection of Strings
 JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
 ObjectMapper mapper = new ObjectMapper();
 StringCollectionDeserializer deserializer = mapper.findValueDeserializer(collectionType);

 // Test deserialization of an empty JSON array
 String json = "[]";
 JsonParser parser = mapper.getFactory().createParser(json);
 Collection<String> result = deserializer.deserialize(parser, null);
 assertEquals(Collections.emptyList(), result);

 // Test deserialization of a non-empty JSON array
 json = "[\"foo\",\"bar\"]";
 parser = mapper.getFactory().createParser(json);
 result = deserializer.deserialize(parser, null);
 assertEquals(Arrays.asList("foo", "bar"), result);
 }

 @Test
 void testCustomDeserialization() throws Exception {
 // Test custom deserialization using a JsonDeserializer for Strings
 JsonDeserializer<String> valueDeserializer = new StringDeserializer();
 JsonDeserializer<Collection<String>> deserializer = new StringCollectionDeserializer(
 collectionType, null, null, valueDeserializer, null, null);

 // Test deserialization of an empty JSON array
 String json = "[]";
 JsonParser parser = mapper.getFactory().createParser(json);
 Collection<String> result = deserializer.deserialize(parser, null);
 assertEquals(Collections.emptyList(), result);

 // Test deserialization of a non-empty JSON array with custom String deserialization
 json = "[\"foo\",\"bar\"]";
 parser = mapper.getFactory().createParser(json);
 result = deserializer.deserialize(parser, null);
 assertEquals(Arrays.asList("fooCustom", "barCustom"), result);
 }

 @Test
 void testDelegateDeserialization() throws Exception {
 // Test delegate deserialization using a JsonDeserializer for Objects
 ObjectMapper mapper = new ObjectMapper();
 JsonDeserializer<Object> delegateDeserializer = new StandardDeserializer<>();
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 collectionType, null, delegateDeserializer, null, null, null);

 // Test deserialization of an empty JSON array
 String json = "[]";
 JsonParser parser = mapper.getFactory().createParser(json);
 Collection<String> result = deserializer.deserialize(parser, null);
 assertEquals(Collections.emptyList(), result);

 // Test deserialization of a non-empty JSON array with delegate deserialization
 ObjectNode node = mapper.createObjectNode();
 node.put("foo", "fooDelegate");
 node.put("bar", "barDelegate");
 json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 parser = mapper.getFactory().createParser(json);
 result = deserializer.deserialize(parser, null);
 assertEquals(Arrays.asList("fooDelegate", "barDelegate"), result);
 }

 @Test
 void testNullValue() throws Exception {
 // Test handling of null values
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 collectionType, null, null, null, new NullValueProviderDefault(), null);

 // Test deserialization of an empty JSON array with null values
 String json = "[null]";
 JsonParser parser = mapper.getFactory().createParser(json);
 Collection<String> result = deserializer.deserialize(parser, null);
 assertEquals(Collections.singletonList(null), result);

 // Test deserialization of a non-empty JSON array with null values
 json = "[\"foo\",null]";
 parser = mapper.getFactory().createParser(json);
 result = deserializer.deserialize(parser, null);
 assertEquals(Arrays.asList("foo", null), result);
 }

 @Test
 void testFormatFeature() throws Exception {
 // Test handling of single value as array using format feature
 StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
 collectionType, null, null, null, null, Boolean.TRUE);

 // Test deserialization of a single String value as empty array
 String json = "\"foo\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 Collection<String> result = deserializer.deserialize(parser, null);
 assertFalse(result.isEmpty());
 assertEquals(Collections.singletonList("foo"), result);

 // Test deserialization of a single String value as non-empty array
 json = "[\"foo\"]";
 parser = mapper.getFactory().createParser(json);
 result = deserializer.deserialize(parser, null);
 assertFalse(result.isEmpty());
 assertEquals(Arrays.asList("foo"), result);
 }
}

 class StringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
}

 class NullValueProviderDefault implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext ctxt) {
 return null;
 }
}