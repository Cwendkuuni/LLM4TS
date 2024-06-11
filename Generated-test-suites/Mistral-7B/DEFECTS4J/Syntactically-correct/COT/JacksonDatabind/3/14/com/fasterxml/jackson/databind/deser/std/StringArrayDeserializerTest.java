package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest extends BaseMapTest {

 private static final StringArrayDeserializer serializer = StringArrayDeserializer.instance;

 @Test
 public void testDeserializeBasic() throws Exception {
 String[] expected = new String[]{"hi", "there"};
 String[] result = serializer.deserialize(objectMapper.getFactory().createParser("[\"hi\", \"there\"]"), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeWithCustomElementDeserializer() throws Exception {
 JsonDeserializer<String> elementDeserializer = createMock(JsonDeserializer.class);
 StringArrayDeserializer serializerWithElementDeserializer = new StringArrayDeserializer(elementDeserializer);
 String[] expected = new String[]{"mock", "value"};
 String[] result = serializerWithElementDeserializer.deserialize(objectMapper.getFactory().createParser("[\"mock\", \"value\"]"), objectMapper.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeWithTypeDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = createMock(TypeDeserializer.class);
 String[] result = (String[]) serializer.deserializeWithType(objectMapper.getFactory().createParser("[]"), objectMapper.getDeserializationContext(), typeDeserializer);
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = IOException.class)
 public void testHandleNonArray() throws Exception {
 StringParser parser = createStringParser("{\"name\": \"John\"}");
 String[] result = serializer.handleNonArray(parser, objectMapper.getDeserializationContext());
 fail("Expected an IOException");
 }

 @Test
 public void testCreateContextual() throws Exception {
 StringParser parser = createStringParser("[null, \"hi\"]");
 JsonDeserializer<String> elementDeserializer = createMock(JsonDeserializer.class);
 StringArrayDeserializer result = (StringArrayDeserializer) serializer.createContextual(objectMapper.getDeserializationContext(), createProperty());
 assertSame(elementDeserializer, result._elementDeserializer);
 }

 private JsonParser createStringParser(String json) {
 JsonParser parser = objectMapper.getFactory().createParser(json);
 parser.nextToken(); // move past start array token
 return parser;
 }

 private Property createProperty() {
 return new PropertyName("testProperty").getName();
 }

}