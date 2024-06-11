package com.example.junit;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 private static final JsonParser mockParser = Mockito.mock(JsonParser.class);
 private static final DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);

 @Test
 public void testDeserialize_FileClass() throws IOException {
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 File expected = new File("test.txt");
 String text = "test.txt";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(File.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserialize_FileClass_InvalidString() throws IOException {
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_URLClass() throws IOException {
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL expected = new URL("http://example.com");
 String text = "http://example.com";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(URL.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = MalformedURLException.class)
 public void testDeserialize_URLClass_MalformedURL() throws IOException {
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 when(mockParser.getValueAsString()).thenReturn("invalid:/example");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_URIClass() throws IOException {
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI expected = new URI("http://example.com");
 String text = "http://example.com";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(URI.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserialize_URIClass_InvalidString() throws IOException {
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_ClassClass() throws IOException {
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
 Class<?> expected = String.class;
 String text = "java.lang.String";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(Class.class, result);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserialize_JavaTypeClass() throws IOException {
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JavaType expected = ObjectMapper.getDefaultInstance().getTypeFactory().constructParametrizedType(List.class, String.class);
 String text = "java.util.List<java.lang.String>";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(JavaType.class, result);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserialize_CurrencyClass() throws IOException {
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency expected = Currency.getInstance("USD");
 String text = "USD";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(Currency.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserialize_CurrencyClass_InvalidString() throws IOException {
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_PatternClass() throws IOException {
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern expected = Pattern.compile("[a-z]+");
 String text = "[a-z]+";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(Pattern.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = PatternSyntaxException.class)
 public void testDeserialize_PatternClass_InvalidString() throws IOException {
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_LocaleClass() throws IOException {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale expected = new Locale("en", "US");
 String text = "en_US";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(Locale.class, result);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserialize_LocaleClass_TwoArguments() throws IOException {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale expected = new Locale("en", "US", "California");
 String text = "en_US_California";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(Locale.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserialize_LocaleClass_InvalidString() throws IOException {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_CharsetClass() throws IOException {
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset expected = Charset.forName("UTF-8");
 String text = "UTF-8";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(Charset.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testDeserialize_CharsetClass_InvalidString() throws IOException {
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_TimeZoneClass() throws IOException {
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone expected = TimeZone.getTimeZone("America/New_York");
 String text = "America/New_York";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(TimeZone.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserialize_TimeZoneClass_InvalidString() throws IOException {
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_InetAddressClass() throws IOException {
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 InetAddress expected = InetAddress.getByName("127.0.0.1");
 String text = "127.0.0.1";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(InetAddress.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = UnknownHostException.class)
 public void testDeserialize_InetAddressClass_InvalidString() throws IOException {
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 when(mockParser.getValueAsString()).thenReturn("invalid");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_InetSocketAddressClass() throws IOException {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
 String text = "[localhost]:8080";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(InetSocketAddress.class, result);
 assertEquals(expected, result);
 }

 @Test(expected = InvalidFormatException.class)
 public void testDeserialize_InetSocketAddressClass_InvalidString() throws IOException {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 when(mockParser.getValueAsString()).thenReturn("invalid:8080");
 deserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testDeserialize_StringBuilderClass() throws IOException {
 FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder expected = new StringBuilder("test");
 String text = "test";
 when(mockParser.getValueAsString()).thenReturn(text);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(StringBuilder.class, result);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserialize_EmptyString() throws IOException {
 FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder expected = new StringBuilder();
 when(mockParser.getValueAsString()).thenReturn("");

 T result = deserializer.deserialize(mockParser, mockContext);
 assertInstanceOf(StringBuilder.class, result);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserialize_NullString() throws IOException {
 FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder expected = null;
 when(mockParser.getValueAsString()).thenReturn(null);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertNull(result);
 }

 @Test
 public void testDeserialize_EmptyArray() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockParser.getValueAsString()).thenReturn("");

 T result = deserializer.deserialize(mockParser, mockContext);
 assertNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserialize_EmbeddedObject() throws IOException {
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
 Object obj = new Object() {};
 String text = "{\"class\":\"com.example.MyClass\",\"value\":5}";
 when(mockParser.getValueAsString()).thenReturn(text);
 when(mockParser.getEmbeddedObject()).thenReturn(obj);

 T result = deserializer.deserialize(mockParser, mockContext);
 assertNull(result);
 }
}