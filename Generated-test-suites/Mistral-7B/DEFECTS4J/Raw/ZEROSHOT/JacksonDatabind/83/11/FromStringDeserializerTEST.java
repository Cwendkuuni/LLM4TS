package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.VersionUtil;

public class FromStringDeserializerTest {

 private static final Class<?>[] SUPPORTED_TYPES = FromStringDeserializer.types();

 @Test
 public void testFindDeserializer() throws Exception {
 for (Class<?> clazz : SUPPORTED_TYPES) {
 Std deserializer = FromStringDeserializer.findDeserializer(clazz);
 if (deserializer != null) {
 assertSame(clazz, deserializer.getValueClass());
 }
 }
 }

 @Test
 public void testDeserialize_File() throws Exception {
 File expected = new File("test.txt");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("test.txt");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_URL() throws Exception {
 URL expected = new URL("http://example.com");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("http://example.com");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_URI() throws Exception {
 URI expected = new URI("http://example.com");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("http://example.com");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_Class() throws Exception {
 Class<String> expected = String.class;
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("java.lang.String");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<?>>(Class.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result.getClass());
 }

 @Test
 public void testDeserialize_JavaType() throws Exception {
 JavaType expected = TypeFactory.defaultInstance().constructType(String.class);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("java.lang.String");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_Currency() throws Exception {
 Currency expected = Currency.getInstance("USD");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("USD");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_Pattern() throws Exception {
 Pattern expected = Pattern.compile("[a-z]+");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("[a-z]+");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_Locale() throws Exception {
 Locale expected = Locale.US;
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("en_US");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_Charset() throws Exception {
 Charset expected = Charset.forName("UTF-8");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("UTF-8");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_TimeZone() throws Exception {
 TimeZone expected = TimeZone.getTimeZone("UTC");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("UTC");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_InetAddress() throws Exception {
 InetAddress expected = InetAddress.getByName("127.0.0.1");
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("127.0.0.1");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_InetSocketAddress() throws Exception {
 InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("[::1]:8080");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_StringBuilder() throws Exception {
 StringBuilder expected = new StringBuilder();
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertSame(expected, result);
 }

 @Test
 public void testDeserialize_UnexpectedToken() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("test");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
 T result = deserializer.deserialize(parser, ctxt);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserialize_InvalidFormatException() throws Exception {
 JsonParser parser = mock(JsonParser.class);
 when(parser.getValueAsString()).thenReturn("invalid");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
 T result = deserializer.deserialize(parser, ctxt);
 }
}