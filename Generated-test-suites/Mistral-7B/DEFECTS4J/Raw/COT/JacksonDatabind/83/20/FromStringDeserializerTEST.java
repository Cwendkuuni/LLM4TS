package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDeserializeString() throws IOException {
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
 @Override
 protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return value;
 }
 };

 JsonParser parser = new JsonFactory().createParser("\"Hello World\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(String.class, parser)).thenReturn(null);

 String result = deserializer.deserialize(parser, context);
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
 @Override
 protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 protected String _deserializeFromEmptyString() throws IOException {
 return "";
 }
 };

 JsonParser parser = new JsonFactory().createParser("\"\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(String.class, parser)).thenReturn(null);

 String result = deserializer.deserialize(parser, context);
 assertEquals("", result);
 }

 @Test
 public void testDeserializeNull() throws IOException {
 FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
 @Override
 protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return null;
 }

 @Override
 protected String _deserializeFromEmptyString() throws IOException {
 return null;
 }
 };

 JsonParser parser = new JsonFactory().createParser("null");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(String.class, parser)).thenReturn(null);

 String result = deserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeInvalidType() throws IOException {
 FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {};

 JsonParser parser = new JsonFactory().createParser("\"Hello World\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Integer.class, parser)).thenReturn(null);

 try {
 deserializer.deserialize(parser, context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializeURL() throws IOException {
 FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class) {
 @Override
 protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new URL(value);
 }
 };

 JsonParser parser = new JsonFactory().createParser("\"http://example.com/test\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(URL.class, parser)).thenReturn(null);

 URL result = deserializer.deserialize(parser, context);
 assertEquals(new URI("http", "example.com", "/test", null), result.toURI());
 }

 @Test
 public void testDeserializeURI() throws IOException {
 FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class) {
 @Override
 protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return new URI(value);
 }
 };

 JsonParser parser = new JsonFactory().createParser("\"http://example.com/test\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(URI.class, parser)).thenReturn(null);

 URI result = deserializer.deserialize(parser, context);
 assertEquals(new URI("http", "example.com", "/test", null), result);
 }

 @Test
 public void testDeserializeClass() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(String[].class);
 FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<String[]>>(Class.class) {
 @Override
 protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return Class.forName(value);
 }
 };

 JsonParser parser = new JsonFactory().createParser("\"java.lang.String[\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Class.class, parser)).thenReturn(null);

 Class<?> result = deserializer.deserialize(parser, context);
 assertEquals(javaType.getRawClass(), result);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(String[].class);
 FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class) {
 @Override
 protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
 return objectMapper.getTypeFactory().constructType(value);
 }
 };

 JsonParser parser = new JsonFactory().createParser("\"java.lang.String[]\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(JavaType.class, parser)).thenReturn(null);

 JavaType result = deserializer.deserialize(parser, context);
 assertEquals(javaType, result);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class) {};

 JsonParser parser = new JsonFactory().createParser("\"USD\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Currency.class, parser)).thenReturn(null);

 Currency result = deserializer.deserialize(parser, context);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class) {};

 JsonParser parser = new JsonFactory().createParser("\"/[a-z]+/g\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Pattern.class, parser)).thenReturn(null);

 Pattern result = deserializer.deserialize(parser, context);
 assertEquals(Pattern.compile("/[a-z]+/g"), result);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class) {};

 JsonParser parser = new JsonFactory().createParser("\"en_US\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Locale.class, parser)).thenReturn(null);

 Locale result = deserializer.deserialize(parser, context);
 assertEquals(Locale.forLanguageTag("en_US"), result);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class) {};

 JsonParser parser = new JsonFactory().createParser("\"UTF-8\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Charset.class, parser)).thenReturn(null);

 Charset result = deserializer.deserialize(parser, context);
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class) {};

 JsonParser parser = new JsonFactory().createParser("\"UTC\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(TimeZone.class, parser)).thenReturn(null);

 TimeZone result = deserializer.deserialize(parser, context);
 assertEquals(TimeZone.getTimeZone("UTC"), result);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class) {};

 JsonParser parser = new JsonFactory().createParser("\"127.0.0.1\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(InetAddress.class, parser)).thenReturn(null);

 InetAddress result = deserializer.deserialize(parser, context);
 assertEquals(InetAddress.getByName("127.0.0.1"), result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class) {};

 JsonParser parser = new JsonFactory().createParser("\"[127.0.0.1]:8080\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(InetSocketAddress.class, parser)).thenReturn(null);

 InetSocketAddress result = deserializer.deserialize(parser, context);
 assertEquals(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8080), result);
 }

 @Test
 public void testDeserializeStringBuilder() throws IOException {
 FromStringDeserializer<StringBuilder> deserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class) {};

 JsonParser parser = new JsonFactory().createParser("\"Hello World\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(StringBuilder.class, parser)).thenReturn(null);

 StringBuilder result = deserializer.deserialize(parser, context);
 assertEquals("Hello World", result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidFormatException() throws IOException {
 FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {};

 JsonParser parser = new JsonFactory().createParser("\"abc\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(Integer.class, parser)).thenReturn(null);

 deserializer.deserialize(parser, context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeMalformedURLException() throws IOException {
 FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class) {};

 JsonParser parser = new JsonFactory().createParser("\"malformed://url\"");
 DeserializationContext context = mock(DeserializationContext.class);
 when(context.handleUnexpectedToken(URL.class, parser)).thenReturn(null);

 deserializer.deserialize(parser, context);
 }
}