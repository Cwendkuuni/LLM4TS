package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(Parameterized.class)
public class FromStringDeserializerTest<T> {

 private Class<T> type;
 private ObjectMapper objectMapper;

 public FromStringDeserializerTest(Class<T> type) {
 this.type = type;
 objectMapper = new ObjectMapper();
 }

 @Parameterized.Parameters(name = "{index}: testing deserialization of {0}")
 public static Iterable<Class<?>> types() {
 return Arrays.asList(File.class, URL.class, URI.class, Class.class, JavaType.class, Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class);
 }

 @Test
 public void testDeserialize() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"value\":\"" + type.getName() + ".class\"}");
 jsonParser.nextToken();
 T actual = objectMapper.readValue(jsonParser, type);
 T expected = createExpectedInstance();
 assertEquals(expected, actual);

 jsonParser = jsonFactory.createParser("{\"value\":\"" + serializeExpectedValue(expected) + "\"}");
 jsonParser.nextToken();
 actual = objectMapper.readValue(jsonParser, type);
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\"" + type.getName() + ".class\",\"" + type.getName() + ".class\"]");
 jsonParser.nextToken();
 JsonToken token = jsonParser.nextToken();
 assertTrue(token == JsonToken.START_ARRAY);
 T[] expected = new T[2];
 expected[0] = createExpectedInstance();
 expected[1] = createExpectedInstance();
 T[] actual = objectMapper.readValue(jsonParser, JavaType.getTypeFactory().constructArrayType(type));
 assertArrayEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidString() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"value\":\"invalidString\"}");
 jsonParser.nextToken();
 objectMapper.readValue(jsonParser, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNullString() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"value\":null}");
 jsonParser.nextToken();
 objectMapper.readValue(jsonParser, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyString() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"value\":\"\"}");
 jsonParser.nextToken();
 objectMapper.readValue(jsonParser, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("{\"value\":\"java.lang.String.class\"}");
 jsonParser.nextToken();
 objectMapper.readValue(jsonParser, type);
 }

 private Object createExpectedInstance() throws IOException {
 return FromStringDeserializer.findDeserializer(type).deserialize(jsonFactory.createParser("\"testValue\""), objectMapper.getDeserializationContext());
 }

 private String serializeExpectedValue(Object value) throws IOException {
 return objectMapper.writeValueAsString(value);
 }

}