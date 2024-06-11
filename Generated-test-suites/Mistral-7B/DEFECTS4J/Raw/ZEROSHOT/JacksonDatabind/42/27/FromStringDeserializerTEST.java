package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeUrl() throws JsonProcessingException {
 FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("http://www.example.com");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value = urlDeserializer.deserialize(jp, ctxt);
 assertEquals("http://www.example.com", value.toString());
 }

 @Test
 public void testDeserializeUri() throws JsonProcessingException {
 FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("http://www.example.com/path/to/file");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value = uriDeserializer.deserialize(jp, ctxt);
 assertEquals("http://www.example.com/path/to/file", value.toString());
 }

 @Test
 public void testDeserializeClass() throws JsonProcessingException {
 FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("java.lang.String");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType javaType = mock(JavaType.class);
 when(ctxt.getTypeFactory().constructFromCanonical("java.lang.String")).thenReturn(javaType);
 T value = classDeserializer.deserialize(jp, ctxt);
 assertEquals(javaType, value);
 }

 @Test
 public void testDeserializeCurrency() throws JsonProcessingException {
 FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("EUR");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value = currencyDeserializer.deserialize(jp, ctxt);
 assertEquals(Currency.getInstance("EUR"), value);
 }

 @Test
 public void testDeserializePattern() throws JsonProcessingException {
 FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("[a-zA-Z0-9]+");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value = patternDeserializer.deserialize(jp, ctxt);
 assertEquals(Pattern.compile("[a-zA-Z0-9]+"), value);
 }

 @Test
 public void testDeserializeEmptyStringForLocale() throws JsonProcessingException {
 FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value = localeDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 }

 @Test
 public void testDeserializeEmptyStringForOtherTypes() throws JsonProcessingException {
 FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
 FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value;
 value = fileDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 value = urlDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 value = uriDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 value = javaTypeDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 value = currencyDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 value = patternDeserializer.deserialize(jp, ctxt);
 assertNull(value);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws JsonProcessingException {
 FromStringDeserializer<Integer> integerDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
 JsonParser jp = mock(JsonParser.class);
 when(jp.getValueAsString()).thenReturn("5");
 DeserializationContext ctxt = mock(DeserializationContext.class);
 T value = integerDeserializer.deserialize(jp, ctxt);
 // should throw an exception
 }
}