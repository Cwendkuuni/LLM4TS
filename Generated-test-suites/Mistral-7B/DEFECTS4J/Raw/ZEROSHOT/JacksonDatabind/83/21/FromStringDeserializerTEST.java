package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 @InjectMocks
 private FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);

 @Test
 public void testSuccessfulDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validUrlString = "http://example.com";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validUrlString);
 Mockito.when(mockContext.findClass(Mockito.anyString())).thenReturn(URL.class);
 Mockito.when(mockContext.getTypeFactory().constructFromCanonical(Mockito.anyString()))
 .thenReturn(URL.class);
 Mockito.when(mockContext.handleInstantiationProblem(URL.class, Mockito.anyString(), Mockito.any()))
 .thenReturn(URL.class);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof URL);
 assertEquals(new URL("http://example.com"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidUrlDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidUrlString = "invalid_url_format";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidUrlString);
 Mockito.when(mockParser.handleUnexpectedToken(URL.class, Mockito.any()))
 .thenThrow(new JsonMappingException("Unexpected token (URL)"));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testSuccessfulDeserializationOfClass() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validClassName = "java.lang.String";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validClassName);
 Mockito.when(mockContext.findClass(Mockito.anyString())).thenReturn(Class.forName(validClassName));

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof Class<? >);
 assertEquals(Class.forName("java.lang.String"), result);
 }

 @Test
 public void testSuccessfulDeserializationOfJavaType() throws IOException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(JavaType.construct(String.class));
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validJavaTypeString = "class java.lang.String";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validJavaTypeString);
 Mockito.when(mockContext.getTypeFactory().constructFromCanonical(Mockito.anyString()))
 .thenReturn(JavaType.construct(String.class));

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertEquals(JavaType.construct(String.class), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidJavaTypeDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidJavaTypeString = "java.lang.InvalidClass";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidJavaTypeString);
 Mockito.when(mockContext.findClass(Mockito.anyString()))
 .thenThrow(new JsonMappingException("Cannot instantiate value of type [simple type, class java.lang.InvalidClass]"));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testSuccessfulDeserializationOfCurrency() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validCurrencyString = "USD";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validCurrencyString);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof Currency);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidCurrencyDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidCurrencyString = "XYZ";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidCurrencyString);
 Mockito.when(mockContext.handleMappingError(Mockito.any(JsonMappingException.class)))
 .thenThrow(new JsonMappingException("Unknown currency code: XYZ"));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testSuccessfulDeserializationOfPattern() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validPatternString = "/abc/def/";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validPatternString);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof Pattern);
 assertEquals(Pattern.compile(validPatternString), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidPatternDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidPatternString = "/[^]";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidPatternString);
 Mockito.when(mockContext.handleMappingError(Mockito.any(JsonMappingException.class)))
 .thenThrow(new JsonMappingException("Invalid regular expression: " + invalidPatternString));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testSuccessfulDeserializationOfLocale() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validLocaleString = "en_US";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validLocaleString);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof Locale);
 assertEquals(Locale.forLanguageTag("en_US"), result);
 }

 @Test
 public void testSuccessfulDeserializationOfLocaleWithVariants() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validLocaleString = "en_US_POSIX";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validLocaleString);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof Locale);
 assertEquals(Locale.forLanguageTag("en_US_POSIX"), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidLocaleDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidLocaleString = "en_US_XXX";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidLocaleString);
 Mockito.when(mockContext.handleMappingError(Mockito.any(JsonMappingException.class)))
 .thenThrow(new JsonMappingException("Unknown locale: " + invalidLocaleString));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testSuccessfulDeserializationOfCharset() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validCharsetString = "UTF-8";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validCharsetString);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof Charset);
 assertEquals(Charset.forName(validCharsetString), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidCharsetDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidCharsetString = "UTF-XXX";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidCharsetString);
 Mockito.when(mockContext.handleMappingError(Mockito.any(JsonMappingException.class)))
 .thenThrow(new JsonMappingException("Unknown charset: " + invalidCharsetString));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }

 @Test
 public void testSuccessfulDeserializationOfTimeZone() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String validTimeZoneIdString = "America/Los_Angeles";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(validTimeZoneIdString);

 // Call the method under test
 Object result = urlDeserializer.deserialize(mockParser, mockContext);

 // Verify the result
 assertTrue(result instanceof TimeZone);
 assertEquals(TimeZone.getTimeZone(validTimeZoneIdString), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTimeZoneDeserialization() throws IOException, JsonProcessingException {
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
 String invalidTimeZoneIdString = "America/XXX";

 // Set up expectations for the mocked objects
 Mockito.when(mockParser.getValueAsString()).thenReturn(invalidTimeZoneIdString);
 Mockito.when(mockContext.handleMappingError(Mockito.any(JsonMappingException.class)))
 .thenThrow(new JsonMappingException("Unknown timezone ID: " + invalidTimeZoneIdString));

 // Call the method under test
 urlDeserializer.deserialize(mockParser, mockContext);
 }
}