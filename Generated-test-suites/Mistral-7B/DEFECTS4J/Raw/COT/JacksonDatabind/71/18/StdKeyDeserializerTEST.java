package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializerFactory.ValueDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonDeserializer.With;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

 @InjectMocks
 private StdKeyDeserializer testee;

 @Test
 public void testDeserializeKey_Boolean() throws Exception {
 String key = "true";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testDeserializeKey_Boolean_Invalid() throws Exception {
 String key = "invalid";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeKey_Byte() throws Exception {
 String key = "55";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals((byte) 55, result);
 }

 @Test
 public void testDeserializeKey_Byte_Invalid() throws Exception {
 String key = "256";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeKey_Short() throws Exception {
 String key = "123";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals((short) 123, result);
 }

 @Test
 public void testDeserializeKey_Short_Invalid() throws Exception {
 String key = "32768";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeKey_Char() throws Exception {
 String key = "A";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals('A', result);
 }

 @Test
 public void testDeserializeKey_Char_Invalid() throws Exception {
 String key = "❎";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertNull(result);
 }

 @Test
 public void testDeserializeKey_Int() throws Exception {
 String key = "12345";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(12345, result);
 }

 @Test
 public void testDeserializeKey_Long() throws Exception {
 String key = "12345678901234567890";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(12345678901234567890L, result);
 }

 @Test
 public void testDeserializeKey_Float() throws Exception {
 String key = "123.45";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(123.45f, result, 0.001f);
 }

 @Test
 public void testDeserializeKey_Double() throws Exception {
 String key = "123.45";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(123.45, result, 0.001);
 }

 @Test(expected = IOException.class)
 public void testDeserializeKey_Locale() throws Exception {
 String key = "invalidLocale";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 testee.deserializeKey(key, context);
 }

 @Test
 public void testDeserializeKey_Locale_Valid() throws Exception {
 String key = "en_US";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(Locale.US, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeKey_Currency() throws Exception {
 String key = "invalidCurrency";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 testee.deserializeKey(key, context);
 }

 @Test
 public void testDeserializeKey_Currency_Valid() throws Exception {
 String key = "USD";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(Currency.getInstance("USD"), result);
 }

 @Test
 public void testDeserializeKey_Date() throws Exception {
 String key = "2022-12-31";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = mapper.readValue(new StringReader(key), Date.class);
 Object deserializedKey = testee.deserializeKey(key, context);
 assertEquals(result, deserializedKey);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeKey_Date_InvalidFormat() throws Exception {
 String key = "invalidDateFormat";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 Object result = mapper.readValue(new StringReader(key), Date.class);
 testee.deserializeKey(key, context);
 }

 @Test
 public void testDeserializeKey_Calendar() throws Exception {
 String key = "2022-12-31";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 Calendar expected = Calendar.getInstance();
 expected.setTime(mapper.readValue(new StringReader(key), Date.class));
 Object result = testee.deserializeKey(key, context);
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeKey_Calendar_InvalidFormat() throws Exception {
 String key = "invalidDateFormat";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 Calendar expected = Calendar.getInstance();
 expected.setTime(mapper.readValue(new StringReader(key), Date.class));
 testee.deserializeKey(key, context);
 }

 @Test
 public void testDeserializeKey_UUID() throws Exception {
 String key = "123e4567-e89b-12d3-a456-426614174000";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);
 }

 @Test
 public void testDeserializeKey_URL() throws Exception {
 String key = "https://example.com";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 URL expected = mapper.readValue(new StringReader(key), URL.class);
 Object result = testee.deserializeKey(key, context);
 assertEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeKey_URL_InvalidFormat() throws Exception {
 String key = "invalidURLFormat";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 ObjectMapper mapper = new ObjectMapper();
 URL expected = mapper.readValue(new StringReader(key), URL.class);
 testee.deserializeKey(key, context);
 }

 @Test
 public void testDeserializeKey_Class() throws Exception {
 String key = "java.util.Date";
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Class<?> expected = Class.forName(key);
 Object result = testee.deserializeKey(key, context);
 assertEquals(expected, result.getClass());
 }
}