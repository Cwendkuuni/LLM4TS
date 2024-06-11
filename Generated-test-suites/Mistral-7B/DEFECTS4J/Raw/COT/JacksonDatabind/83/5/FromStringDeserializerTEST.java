package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;
import com.fasterxml.jackson.databind.util.VersionUtil;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeFile() throws IOException {
 Std.StdFile deserializer = FromStringDeserializer.findDeserializer(File.class);
 assertInstanceOf(Std.StdFile.class, deserializer);

 File expected = new File("test.txt");
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("test.txt");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(expected, value);
 }

 @Test
 public void testDeserializeURL() throws IOException {
 Std.StdURL deserializer = FromStringDeserializer.findDeserializer(URL.class);
 assertInstanceOf(Std.StdURL.class, deserializer);

 URL expected = new URL("http://example.com");
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("http://example.com");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(expected, value);
 }

 @Test
 public void testDeserializeURI() throws IOException {
 Std.StdURI deserializer = FromStringDeserializer.findDeserializer(URI.class);
 assertInstanceOf(Std.StdURI.class, deserializer);

 URI expected = URI.create("http://example.com/path");
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("http://example.com/path");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(expected, value);
 }

 @Test
 public void testDeserializeClass() throws IOException, ClassNotFoundException {
 Std.StdClass deserializer = FromStringDeserializer.findDeserializer(String.class);
 assertInstanceOf(Std.StdClass.class, deserializer);

 String expected = "Hello World";
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn(expected);
 DeserializationContext context = mock(DeserializationContext.class);
 TypeFactory typeFactory = context.getTypeFactory();
 JavaType javaType = typeFactory.constructType(String.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(expected, value);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 Std.StdJavaType deserializer = FromStringDeserializer.findDeserializer(Integer.class);
 assertInstanceOf(Std.StdJavaType.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("42");
 DeserializationContext context = mock(DeserializationContext.class);
 JavaType javaType = context.getTypeFactory().constructType(Integer.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(42, value);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 Std.StdCurrency deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 assertInstanceOf(Std.StdCurrency.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("USD");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(Currency.getInstance("USD"), value);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 Std.StdPattern deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 assertInstanceOf(Std.StdPattern.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("\\d+");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(Pattern.compile("\\d+"), value);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 Std.StdLocale deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 assertInstanceOf(Std.StdLocale.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("en_US");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(Locale.US, value);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 Std.StdCharset deserializer = FromStringDeserializer.findDeserializer(String.class);
 assertInstanceOf(Std.StdCharset.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("UTF-8");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals("UTF-8", value);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 Std.StdTimeZone deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 assertInstanceOf(Std.StdTimeZone.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("America/Los_Angeles");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), value);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 Std.StdInetAddress deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 assertInstanceOf(Std.StdInetAddress.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("192.168.1.1");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(InetAddress.getByName("192.168.1.1"), value);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 Std.StdInetSocketAddress deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 assertInstanceOf(Std.StdInetSocketAddress.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("[192.168.1.1:80]");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertEquals(new InetSocketAddress("192.168.1.1", 80), value);
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 Std.StdStringBuilder deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 assertInstanceOf(Std.StdStringBuilder.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("");
 DeserializationContext context = mock(DeserializationContext.class);
 T value = deserializer.deserialize(jsonParser, context);
 assertNull(value);
 }

 @Test
 public void testInvalidFormatException() throws IOException {
 Std.StdStringBuilder deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 assertInstanceOf(Std.StdStringBuilder.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("invalid");
 DeserializationContext context = mock(DeserializationContext.class);
 assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(jsonParser, context));
 }

 @Test
 public void testJsonMappingException() throws IOException {
 Std.StdStringBuilder deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 assertInstanceOf(Std.StdStringBuilder.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("invalid");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(_eq(StringBuilder.class), jsonParser)).thenThrow(new JsonMappingException("Unexpected token (expected String but found NULL)"));
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, context));
 }

 @Test
 public void testMalformedURLException() throws IOException {
 Std.StdURL deserializer = FromStringDeserializer.findDeserializer(URL.class);
 assertInstanceOf(Std.StdURL.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("invalid://example.com");
 DeserializationContext context = mock(DeserializationContext.class);
 assertThrows(MalformedURLException.class, () -> deserializer.deserialize(jsonParser, context));
 }

 @Test
 public void testIllegalArgumentException() throws IOException {
 Std.StdLocale deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 assertInstanceOf(Std.StdLocale.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("invalid");
 DeserializationContext context = mock(DeserializationContext.class);
 assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(jsonParser, context));
 }

 @Test
 public void testUnknownHostException() throws IOException {
 Std.StdInetAddress deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 assertInstanceOf(Std.StdInetAddress.class, deserializer);

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn("123.123.123.123.invalid");
 DeserializationContext context = mock(DeserializationContext.class);
 assertThrows(UnknownHostException.class, () -> deserializer.deserialize(jsonParser, context));
 }

}