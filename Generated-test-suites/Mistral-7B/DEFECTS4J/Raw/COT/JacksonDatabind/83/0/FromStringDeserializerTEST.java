package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 @Mock
 private DeserializationContext ctxt;
 @Mock
 private JsonFactory factory;
 @Mock
 private JsonParser parser;
 @Mock
 private Object ob;
 @InjectMocks
 private FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);

 @Test
 public void testDeserializeStringBuilderWithValidValue() throws Exception {
 String jsonString = "\"Hello World\"";
 when(parser.getValueAsString()).thenReturn(jsonString);
 assertEquals(new StringBuilder("Hello World"), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeStringBuilderWithEmptyString() throws Exception {
 when(parser.getValueAsString()).thenReturn("");
 stringBuilderDeserializer._deserializeFromEmptyString();
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeStringBuilderWithInvalidFormat() throws Exception {
 when(parser.getValueAsString()).thenReturn("InvalidFormat");
 stringBuilderDeserializer.deserialize(parser, ctxt);
 }

 @Test
 public void testDeserializeURLWithValidValue() throws Exception {
 String jsonString = "\"http://example.com\"";
 when(parser.getValueAsString()).thenReturn(jsonString);
 assertEquals(new URL("http://example.com"), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test(expected = MalformedURLException.class)
 public void testDeserializeURLWithInvalidFormat() throws Exception {
 when(parser.getValueAsString()).thenReturn("InvalidFormat");
 stringBuilderDeserializer.deserialize(parser, ctxt);
 }

 @Test
 public void testDeserializePatternWithValidValue() throws Exception {
 String jsonString = "/[a-zA-Z]+/g";
 when(parser.getValueAsString()).thenReturn(jsonString);
 assertEquals(Pattern.compile(jsonString), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeCurrencyWithValidValue() throws Exception {
 String jsonString = "USD";
 assertEquals(Currency.getInstance(jsonString), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeCurrencyWithInvalidFormat() throws Exception {
 stringBuilderDeserializer.deserialize("InvalidFormat", ctxt);
 }

 @Test
 public void testDeserializeLocaleWithValidValue() throws Exception {
 String jsonString = "en_US";
 assertEquals(Locale.forLanguageTag(jsonString), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeLocaleWithTwoPieces() throws Exception {
 String jsonString = "en_US_POSIX";
 assertEquals(Locale.forLanguageTag("en", "US", "POSIX"), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeLocaleWithInvalidFormat() throws Exception {
 stringBuilderDeserializer.deserialize("InvalidFormat", ctxt);
 }

 @Test
 public void testDeserializeCharsetWithValidValue() throws Exception {
 String jsonString = "UTF-8";
 assertEquals(Charset.forName(jsonString), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeTimeZoneWithValidValue() throws Exception {
 String jsonString = "America/New_York";
 assertEquals(TimeZone.getTimeZone(jsonString), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeInetSocketAddressWithValidValue() throws Exception {
 String jsonString = "[255.255.255.255]:8080";
 when(parser.getValueAsString()).thenReturn(jsonString);
 assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeInetSocketAddressWithoutPort() throws Exception {
 String jsonString = "[255.255.255.255]";
 when(parser.getValueAsString()).thenReturn(jsonString);
 assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 0), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeInetSocketAddressWithHostAndPort() throws Exception {
 String jsonString = "example.com:8080";
 when(parser.getValueAsString()).thenReturn(jsonString);
 assertEquals(new InetSocketAddress("example.com", 8080), stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeInetSocketAddressWithInvalidFormat() throws Exception {
 when(parser.getValueAsString()).thenReturn("InvalidFormat");
 stringBuilderDeserializer.deserialize(parser, ctxt);
 }

 @Test
 public void testDeserializeFileWithValidValue() throws Exception {
 String jsonString = "\"/path/to/file.txt\"";
 when(parser.getValueAsString()).thenReturn(jsonString);
 File expectedFile = File.createTempFile("test", null);
 expectedFile.deleteOnExit();
 assertEquals(expectedFile, stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test
 public void testDeserializeClassWithValidValue() throws Exception {
 String jsonString = "com.example.MyClass";
 when(ctxt.findClass(jsonString)).thenReturn(MyClass.class);
 assertEquals(MyClass.class, stringBuilderDeserializer.deserialize(parser, ctxt));
 }

 @Test(expected = NullPointerException.class)
 public void testDeserializeClassWithNullValue() throws Exception {
 when(parser.getValueAsString()).thenReturn(null);
 stringBuilderDeserializer.deserialize(parser, ctxt);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeJavaTypeWithInvalidValue() throws Exception {
 String jsonString = "InvalidFormat";
 when(parser.getValueAsString()).thenReturn(jsonString);
 stringBuilderDeserializer.deserialize(parser, ctxt);
 }

 @Test
 public void testDeserializeJavaTypeWithValidValue() throws Exception {
 String jsonString = "class com.example.MyClass";
 JavaType javaType = factory.constructType(JavaType.constructRawType(MyClass.class));
 when(ctxt.getTypeFactory().constructFromCanonical(jsonString)).thenReturn(javaType);
 assertEquals(javaType, stringBuilderDeserializer.deserialize(parser, ctxt));
 }
}