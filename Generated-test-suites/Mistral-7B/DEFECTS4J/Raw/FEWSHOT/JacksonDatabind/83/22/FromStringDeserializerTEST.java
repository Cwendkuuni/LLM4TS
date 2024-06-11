package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws IOException, JsonProcessingException {
 File file = FromStringDeserializer.findDeserializer(File.class).deserialize(objectMapper, null, "test.txt");
 assertEquals("test.txt", file.getAbsolutePath());
 }

 @Test
 public void testDeserializeURL() throws IOException, JsonProcessingException {
 URL url = FromStringDeserializer.findDeserializer(URL.class).deserialize(objectMapper, null, "http://example.com");
 assertEquals("http", url.getProtocol());
 assertEquals("/example.com", url.getPath());
 }

 @Test
 public void testDeserializeURI() throws IOException, JsonProcessingException {
 URI uri = FromStringDeserializer.findDeserializer(URI.class).deserialize(objectMapper, null, "http://example.com/path/to/file");
 assertEquals("http", uri.getScheme());
 assertEquals("/path/to/file", uri.getPath());
 }

 @Test
 public void testDeserializeClass() throws IOException, JsonProcessingException {
 try {
 Class<?> clazz = FromStringDeserializer.findDeserializer(String.class).deserialize(objectMapper, null, "java.lang.String");
 assertEquals(String.class, clazz);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeJavaType() throws IOException, JsonProcessingException {
 JavaType javaType = FromStringDeserializer.findDeserializer(JavaType.class).deserialize(objectMapper, null, "com.example.MyClass");
 assertNotNull(javaType);
 }

 @Test
 public void testDeserializeCurrency() throws IOException, JsonProcessingException {
 Currency currency = FromStringDeserializer.findDeserializer(Currency.class).deserialize(objectMapper, null, "USD");
 assertEquals(Currency.getInstance("USD"), currency);
 }

 @Test
 public void testDeserializePattern() throws IOException, JsonProcessingException {
 Pattern pattern = FromStringDeserializer.findDeserializer(Pattern.class).deserialize(objectMapper, null, "\\d+");
 assertNotNull(pattern);
 }

 @Test
 public void testDeserializeLocale() throws IOException, JsonProcessingException {
 Locale locale = FromStringDeserializer.findDeserializer(Locale.class).deserialize(objectMapper, null, "en_US");
 assertEquals(Locale.US, locale);
 }

 @Test
 public void testDeserializeCharset() throws IOException, JsonProcessingException {
 Charset charset = FromStringDeserializer.findDeserializer(Charset.class).deserialize(objectMapper, null, "UTF-8");
 assertEquals(Charset.forName("UTF-8"), charset);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
 TimeZone timeZone = FromStringDeserializer.findDeserializer(TimeZone.class).deserialize(objectMapper, null, "America/New_York");
 assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
 InetAddress inetAddress = FromStringDeserializer.findDeserializer(InetAddress.class).deserialize(objectMapper, null, "192.168.1.1");
 assertNotNull(inetAddress);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
 InetSocketAddress inetSocketAddress = FromStringDeserializer.findDeserializer(InetSocketAddress.class).deserialize(objectMapper, null, "[255.255.255.255]:8080");
 assertEquals("255.255.255.255", inetSocketAddress.getAddress().getHostAddress());
 assertEquals(8080, inetSocketAddress.getPort());
 }

 @Test
 public void testDeserializeStringBuilder() throws IOException, JsonProcessingException {
 StringBuilder stringBuilder = (StringBuilder) FromStringDeserializer.findDeserializer(StringBuilder.class).deserialize(objectMapper, null, "Hello, World!");
 assertEquals("Hello, World!", stringBuilder.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeNull() throws IOException, JsonProcessingException {
 FromStringDeserializer.findDeserializer(String.class).deserialize(objectMapper, null, null);
 }
}