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
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 File expected = new File("test.txt");
 String input = "'test.txt'";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeUrl() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL expected = new URL("http://www.example.com");
 String input = "http://www.example.com";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeUri() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI expected = new URI("http://www.example.com");
 String input = "http://www.example.com";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 String expected = "Test";
 String input = "java.lang.String";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JavaType expected = mapper.getTypeFactory().constructType(Integer.class);
 String input = "com.fasterxml.jackson.core.type.TypeReference<java.lang.Integer>";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency expected = Currency.getInstance("USD");
 String input = "USD";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializePattern() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern expected = Pattern.compile("^[A-Za-z]+$");
 String input = "(?i)^[A-Za-z]+$";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale expected = new Locale("en", "US");
 String input = "en_US";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset expected = Charset.forName("UTF-8");
 String input = "UTF-8";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone expected = TimeZone.getTimeZone("UTC");
 String input = "UTC";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 InetAddress expected = InetAddress.getByName("localhost");
 String input = "127.0.0.1";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
 String input = "[::1]:8080";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 String input = "java.lang.String";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringFile() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringUrl() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringUri() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringClass() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringJavaType() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringCurrency() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringPattern() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringLocale() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringCharset() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringTimeZone() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringInetAddress() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyStringInetSocketAddress() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 String input = "";
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test
 public void testDeserializeNull() throws Exception {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
 String input = null;
 T result = (T) deserializer.deserialize(mapper.getFactory().createParser(input), mapper.getDeserializationContext());
 assertNull(result);
 }
}