package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws IOException {
 File file = new File("test.txt");
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
 String json = "\"test.txt\"";
 Object result = deserializer.deserialize(MAPPER.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPPER.getDeserializationContext());
 assertEquals(file, result);
 }

 @Test
 public void testDeserializeUrl() throws IOException, URISyntaxException {
 URL url = new URL("http://example.com");
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
 String json = "\"http://example.com\"";
 Object result = deserializer.deserialize(MAPPER.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPPER.getDeserializationContext());
 assertEquals(url, result);
 }

 @Test
 public void testDeserializeUri() throws IOException, URISyntaxException {
 URI uri = new URI("http://example.com");
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
 String json = "\"http://example.com\"";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(uri, result);
 }

 @Test
 public void testDeserializeClass() throws InstantiationException, IllegalAccessException {
 Class<?> clazz = String.class;
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(clazz);
 String json = "java.lang.String";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(clazz, result.getClass());
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 JavaType javaType = MAPper.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(javaType.getClass());
 String json = "{\"key\": \"value\", 1}";
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 Object result = deserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 Map<?,?> map = (Map<?,?>) result;
 assertEquals("value", map.get("key"));
 assertEquals(1, map.values().toArray()[0]);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class);
 String json = "USD";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class);
 String json = "/[a-z]+/i";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertNotNull(result);
 assertTrue(result instanceof Pattern);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
 String json = "en_US";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class);
 String json = "UTF-8";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class);
 String json = "UTC";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(TimeZone.getTimeZone("UTC"), result);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class);
 String json = "[255.255.255.255]";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(InetAddress.getByName("[255.255.255.255]"), result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
 String json = "[255.255.255.255]:80";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertEquals(new InetSocketAddress(InetAddress.getByName("[255.255.255.255]"), 80), result);
 }

 @Test
 public void testDeserializeNullOrEmptyString() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
 String json = "";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeInvalidString() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
 String json = "invalid";
 Object result = deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeInvalidType() throws IOException {
 FromStringDeserializer deserializer = new FromStringDeserializer.Std(Integer.class);
 String json = "\"test.txt\"";
 try {
 deserializer.deserialize(MAPper.getFactory().createParser(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))), MAPper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}