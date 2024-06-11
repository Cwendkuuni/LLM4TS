package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeFile() throws IOException {
 File file = new File("test.txt");
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 JsonParser parser = MAPPER.getFactory().createParser(new File("test.txt"));
 File result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(file, result);
 }

 @Test
 public void testDeserializeUrl() throws IOException {
 URL url = new URL("http://example.com");
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 JsonParser parser = MAPPER.getFactory().createParser("http://example.com");
 URL result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(url, result);
 }

 @Test
 public void testDeserializeUri() throws IOException {
 URI uri = new URI("http://example.com");
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 JsonParser parser = MAPPER.getFactory().createParser("http://example.com");
 URI result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(uri, result);
 }

 @Test
 public void testDeserializeClass() throws IOException {
 Class<?> clazz = String.class;
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
 JsonParser parser = MAPPER.getFactory().createParser("java.lang.String");
 Class<?> result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(clazz, result);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 JavaType javaType = MAPPER.getTypeFactory().constructType(String[].class);
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JsonParser parser = MAPPER.getFactory().createParser("class [Ljava.lang.String;");
 JavaType result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(javaType, result);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 Currency currency = Currency.getInstance("USD");
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 JsonParser parser = MAPPER.getFactory().createParser("USD");
 Currency result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(currency, result);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 Pattern pattern = Pattern.compile("[a-z]+");
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 JsonParser parser = MAPPER.getFactory().createParser("/[a-z]+/");
 Pattern result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(pattern, result);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 Locale locale = Locale.US;
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 JsonParser parser = MAPPER.getFactory().createParser("en_US");
 Locale result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(locale, result);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 Charset charset = Charset.forName("UTF-8");
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 JsonParser parser = MAPPER.getFactory().createParser("UTF-8");
 Charset result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(charset, result);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 JsonParser parser = MAPPER.getFactory().createParser("UTC");
 TimeZone result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(timeZone, result);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 InetAddress inetAddress = InetAddress.getByName("localhost");
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 JsonParser parser = MAPPER.getFactory().createParser("127.0.0.1");
 InetAddress result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(inetAddress, result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 JsonParser parser = MAPPER.getFactory().createParser("[::1]:8080"); // IPv6 with port number
 InetSocketAddress result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(inetSocketAddress, result);
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer<Object> fileDeserializer = FromStringDeserializer.findDeserializer(Object.class);
 String emptyString = "";
 JsonParser parser = MAPPER.getFactory().createParser(emptyString);
 Object result = fileDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidString() throws IOException {
 FromStringDeserializer<Object> fileDeserializer = FromStringDeserializer.findDeserializer(Object.class);
 String invalidString = "invalid";
 JsonParser parser = MAPPER.getFactory().createParser(invalidString);
 fileDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 fail("Expected JsonMappingException but didn't get one.");
 }

}