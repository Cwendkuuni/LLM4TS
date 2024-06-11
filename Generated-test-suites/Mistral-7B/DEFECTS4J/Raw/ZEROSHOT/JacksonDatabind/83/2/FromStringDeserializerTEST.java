package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
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
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 File file = deserializer.deserialize(mapper.getFactory().createParser("\"/path/to/file\""), mapper.getDeserializationContext());
 assertEquals("/path/to/file", file.getAbsolutePath());
 }

 @Test
 public void testDeserializeUrl() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL url = deserializer.deserialize(mapper.getFactory().createParser("\"http://www.example.com\""), mapper.getDeserializationContext());
 assertEquals("http://www.example.com", url.toString());
 }

 @Test
 public void testDeserializeUri() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI uri = deserializer.deserialize(mapper.getFactory().createParser("\"http://www.example.com/path?query#fragment\""), mapper.getDeserializationContext());
 assertEquals("http", uri.getScheme());
 assertEquals("/path", uri.getPath());
 assertEquals("query", uri.getQuery());
 assertEquals("fragment", uri.getFragment());
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 String result = deserializer.deserialize(mapper.getFactory().createParser("\"Hello World\""), mapper.getDeserializationContext());
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
 Integer result = (Integer) deserializer.deserialize(mapper.getFactory().createParser("\"42\""), mapper.getDeserializationContext());
 assertEquals(42, result.intValue());
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 String text = "USD";
 Currency currency = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), currency);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 String text = "[a-z]+";
 Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertTrue(pattern.matcher("hello").matches());
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 String text = "en_US";
 Locale locale = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals(Locale.US, locale);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 String text = "UTF-8";
 Charset charset = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), charset);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 String text = "America/Los_Angeles";
 TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals("America/Los_Angeles", timeZone.getID());
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 String text = "127.0.0.1";
 InetAddress inetAddress = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals("127.0.0.1", inetAddress.getHostAddress());
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 String text = "[255.255.255.255]:80";
 InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals("[255.255.255.255]", inetSocketAddress.getAddress().getHostAddress());
 assertEquals(80, inetSocketAddress.getPort());
 }

 @Test
 public void testDeserializeStringBuilder() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 String text = "Hello World";
 StringBuilder stringBuilder = deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 assertEquals("Hello World", stringBuilder.toString());
 }

 @Test
 public void testEmptyString() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 String result = (String) deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnexpectedToken() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
 deserializer.deserialize(parser, mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidFormatException() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 String text = "apple";
 deserializer.deserialize(mapper.getFactory().createParser(text), mapper.getDeserializationContext());
 }
}