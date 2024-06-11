package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(BaseTest.NiceMock.class)
public class FromStringDeserializerTest {

 private static final ObjectMapper objectMapper = new JsonMapper();

 @Test
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"/path/to/file.txt\"";
 File expectedResult = new File("/path/to/file.txt");
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { fileDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(File.class, context)).thenReturn(expectedResult);

 T result = fileDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeUrl() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"https://www.example.com\"";
 URL expectedResult = new URL("https://www.example.com");
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { urlDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(URL.class, context)).thenReturn(expectedResult);

 T result = urlDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeUri() throws IOException {
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"http://www.example.com/path?query=value#fragment\"";
 URI expectedResult = new URI("http", "www.example.com", "/path", null, null, null, null, null, null, null, null, "query=value", "#fragment");
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { uriDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(URI.class, context)).thenReturn(expectedResult);

 T result = uriDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"java.lang.String\"";
 String expectedResult = "Test String";
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { stringDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(String.class, context)).thenReturn(expectedResult);

 T result = stringDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"com.example.MyClass\"";
 JavaType expectedResult = objectMapper.getTypeFactory().constructType(JavaClass.class);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);
 Mockito.when(context.getFactory()).thenReturn(objectMapper.getTypeFactory());

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { javaTypeDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(JavaType.class, context)).thenReturn(expectedResult);

 T result = javaTypeDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"EUR\"";
 Currency expectedResult = Currency.getInstance("EUR");
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { currencyDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(Currency.class, context)).thenReturn(expectedResult);

 T result = currencyDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "/^[a-zA-Z0-9_]+$/";
 Pattern expectedResult = Pattern.compile(jsonString);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { patternDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(Pattern.class, context)).thenReturn(expectedResult);

 T result = patternDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"en_US\"";
 Locale expectedResult = new Locale("en", "US");
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { localeDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(Locale.class, context)).thenReturn(expectedResult);

 T result = localeDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"UTF-8\"";
 Charset expectedResult = Charset.forName(jsonString);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { charsetDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(Charset.class, context)).thenReturn(expectedResult);

 T result = charsetDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"America/New_York\"";
 TimeZone expectedResult = TimeZone.getTimeZone(jsonString);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { timeZoneDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(TimeZone.class, context)).thenReturn(expectedResult);

 T result = timeZoneDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "\"192.168.0.1\"";
 InetAddress expectedResult = InetAddress.getByName(jsonString);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { inetAddressDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(InetAddress.class, context)).thenReturn(expectedResult);

 T result = inetAddressDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 String jsonString = "[\"192.168.0.1\", 80]";
 InetSocketAddress expectedResult = new InetSocketAddress("192.168.0.1", 80);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

 Mockito.when(context.findAndRegisterParts(Mockito.any(Class.class), Mockito.anyListOf(Class.class)))
 .thenReturn(new Object[] { inetSocketAddressDeserializer });
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.readValueAs(String[].class, context)).thenReturn(new String[] {"192.168.0.1", "80"});
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);
 Mockito.when(parser.nextIntValue()).thenReturn(80);
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 T result = inetSocketAddressDeserializer.deserialize(parser, context);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testEmptyString() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn("");
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(String.class, context)).thenThrow(new InvalidFormatException(parser, "Empty string is not valid"));

 T result = stringDeserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test
 public void testUnknownType() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 JsonParser parser = Mockito.mock(JsonParser.class);
 Mockito.when(parser.getValueAsString()).thenReturn("\"java.util.ArrayList\"");
 Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(parser.getEmbeddedObject()).thenReturn(null);
 Mockito.when(parser.readValueAs(String.class, context)).thenReturn("Unsupported type \"java.util.ArrayList\"");

 T result = stringDeserializer.deserialize(parser, context);
 assertNull(result);
 }
}