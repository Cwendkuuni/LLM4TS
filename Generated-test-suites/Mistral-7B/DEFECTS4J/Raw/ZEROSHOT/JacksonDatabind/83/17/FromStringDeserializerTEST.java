package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFileDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 File expected = new File("/path/to/file.txt");
 File actual = deserializer.deserialize(mapper.getFactory().createParser("\"/" + expected.getPath() + "\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testUrlDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL expected = new URL("http://www.example.com");
 URL actual = deserializer.deserialize(mapper.getFactory().createParser("\"http://www.example.com\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testUriDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI expected = new URI("http://www.example.com");
 URI actual = deserializer.deserialize(mapper.getFactory().createParser("\"http://www.example.com\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 Class<?> expected = String.class;
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 Class<?> actual = deserializer.deserialize(mapper.getFactory().createParser("\"java.lang.String\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testJavaTypeDeserialization() throws JsonProcessingException {
 JavaType expected = mapper.getTypeFactory().constructParametricType(int[].class, String[].class);
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JavaType actual = deserializer.deserialize(mapper.getFactory().createParser("\"com.fasterxml.jackson.core.type.TypeReference<java.lang.Integer[]>[java.lang.String[]]\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency expected = Currency.getInstance("USD");
 Currency actual = deserializer.deserialize(mapper.getFactory().createParser("\"USD\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testPatternDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern expected = Pattern.compile("^[a-z]+$");
 Pattern actual = deserializer.deserialize(mapper.getFactory().createParser("\"^[a-z]+$\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale expected = Locale.US;
 Locale actual = deserializer.deserialize(mapper.getFactory().createParser("\"US\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testCharsetDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset expected = Charset.forName("UTF-8");
 Charset actual = deserializer.deserialize(mapper.getFactory().createParser("\"UTF-8\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testTimeZoneDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone expected = TimeZone.getTimeZone("UTC");
 TimeZone actual = deserializer.deserialize(mapper.getFactory().createParser("\"UTC\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testInetAddressDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 InetAddress expected = InetAddress.getByName("127.0.0.1");
 InetAddress actual = deserializer.deserialize(mapper.getFactory().createParser("\"127.0.0.1\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testInetSocketAddressDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
 InetSocketAddress actual = deserializer.deserialize(mapper.getFactory().createParser("\"localhost:8080\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testStringBuilderDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder expected = new StringBuilder("Hello, world!");
 StringBuilder actual = deserializer.deserialize(mapper.getFactory().createParser("\"Hello, world!\""), mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDeserialization() throws JsonProcessingException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 deserializer.deserialize(mapper.getFactory().createParser("\"foo\""), mapper.getDeserializationContext());
 }
}