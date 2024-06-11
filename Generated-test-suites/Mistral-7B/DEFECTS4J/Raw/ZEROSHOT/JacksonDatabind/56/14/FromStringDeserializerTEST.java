package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDeserializeFile() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("/path/to/file.txt");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(new File("/path/to/file.txt"), result);
 }

 @Test
 public void testDeserializeUrl() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("http://example.com");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(new URL("http://example.com"), result);
 }

 @Test
 public void testDeserializeUri() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("http://example.com/path/to/resource");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(new URI("http://example.com/path/to/resource"), result);
 }

 @Test
 public void testDeserializeClass() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("java.lang.String");
 DeserializationContext context = mock(DeserializationContext.class);
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(String.class, result.getClass());
 }

 @Test
 public void testDeserializeJavaType() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("java.lang.Integer");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 assertEquals(javaType, result);
 }

 @Test
 public void testDeserializeCurrency() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("USD");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDeserializePattern() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("\\d{5}");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(Pattern.compile("\\d{5}"), result);
 }

 @Test
 public void testDeserializeLocale() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("en_US");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(Locale.forLanguageTag("en_US"), result);
 }

 @Test
 public void testDeserializeCharset() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("UTF-8");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testDeserializeTimeZone() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("America/Los_Angeles");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
 }

 @Test
 public void testDeserializeInetAddress() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("192.168.0.1");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(InetAddress.getByName("192.168.0.1"), result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("[255.255.255.255]:8080");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), result);
 }

 @Test
 public void testDeserializeNull() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn(null);
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeEmptyString() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("");
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertEquals("", result.toString());
 }

 @Test
 public void testDeserializeInvalidString() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("invalid");
 DeserializationContext context = mock(DeserializationContext.class);
 try {
 deserializer.deserialize(parser, context);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeEmbeddedObject() throws JsonMappingException, IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JsonParser parser = mock(JsonParser.class);
 Object embeddedObject = new Object();
 when(parser.getEmbeddedObject()).thenReturn(embeddedObject);
 DeserializationContext context = mock(DeserializationContext.class);
 Object result = deserializer.deserialize(parser, context);
 assertNull(result);
 }

}