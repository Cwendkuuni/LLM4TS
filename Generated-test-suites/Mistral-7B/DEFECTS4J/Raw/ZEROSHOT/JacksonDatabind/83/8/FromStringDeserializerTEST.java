package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @BeforeEach
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testUrlDeserialization() throws JsonProcessingException {
 FromStringDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"http://example.com\"");
 T url = (T) urlDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(URL.class, url);
 Assertions.assertEquals("http://example.com", url.toString());
 }

 @Test
 public void testUriDeserialization() throws JsonProcessingException {
 FromStringDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"http://example.com/path\"");
 T uri = (T) uriDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(URI.class, uri);
 Assertions.assertEquals("http://example.com/path", uri.toString());
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 FromStringDeserializer stringClassDeserializer = FromStringDeserializer.findDeserializer(String.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"java.lang.String\"");
 T clazz = (T) stringClassDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(Class.class, clazz);
 Assertions.assertEquals(String.class, clazz);
 }

 @Test
 public void testJavaTypeDeserialization() throws JsonProcessingException {
 FromStringDeserializer stringJavaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JsonNode\"");
 T javaType = (T) stringJavaTypeDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(JavaType.class, javaType);
 Assertions.assertEquals(JavaType.TypeFetcher.constructType(JavaType.TypeReference.construct(JavaType.class, "com.fasterxml.jackson.databind.JsonNode")), javaType);
 }

 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException {
 FromStringDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"EUR\"");
 T currency = (T) currencyDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(Currency.class, currency);
 Assertions.assertEquals(Currency.EUR, currency);
 }

 @Test
 public void testPatternDeserialization() throws JsonProcessingException {
 FromStringDeserializer patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"/\\w+\\.js\"");
 T pattern = (T) patternDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(Pattern.class, pattern);
 Assertions.assertEquals("/\\w+\\.js", pattern.toString());
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 FromStringDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 // single argument
 JsonParser parser1 = objectMapper.getFactory().createParser("\"en_US\"");
 T locale1 = (T) localeDeserializer.deserialize(parser1, ctxt);
 Assertions.assertInstanceOf(Locale.class, locale1);
 Assertions.assertEquals(Locale.US, locale1);
 // two arguments
 JsonParser parser2 = objectMapper.getFactory().createParser("\"en_US_POSIX\"");
 T locale2 = (T) localeDeserializer.deserialize(parser2, ctxt);
 Assertions.assertInstanceOf(Locale.class, locale2);
 Assertions.assertEquals( new Locale("en", "US", "POSIX"), locale2);
 }

 @Test
 public void testCharsetDeserialization() throws JsonProcessingException {
 FromStringDeserializer charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"UTF-8\"");
 T charset = (T) charsetDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(Charset.class, charset);
 Assertions.assertEquals(Charset.forName("UTF-8"), charset);
 }

 @Test
 public void testTimeZoneDeserialization() throws JsonProcessingException {
 FromStringDeserializer timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"UTC\"");
 T timeZone = (T) timeZoneDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(TimeZone.class, timeZone);
 Assertions.assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
 }

 @Test
 public void testInetAddressDeserialization() throws JsonProcessingException {
 FromStringDeserializer inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 // host name
 JsonParser parser1 = objectMapper.getFactory().createParser("\"192.168.1.1\"");
 T inetAddress1 = (T) inetAddressDeserializer.deserialize(parser1, ctxt);
 Assertions.assertInstanceOf(InetAddress.class, inetAddress1);
 Assertions.assertEquals("192.168.1.1", inetAddress1.toString());
 // host name with port number
 JsonParser parser2 = objectMapper.getFactory().createParser("\"[255.255.255.255]:8080\"");
 T inetAddress2 = (T) inetAddressDeserializer.deserialize(parser2, ctxt);
 Assertions.assertInstanceOf(InetSocketAddress.class, inetAddress2);
 Assertions.assertEquals("255.255.255.255:8080", inetAddress2.toString());
 }

 @Test
 public void testInetSocketAddressDeserialization() throws JsonProcessingException {
 FromStringDeserializer inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 // host name and port number
 JsonParser parser1 = objectMapper.getFactory().createParser("\"192.168.1.1:8080\"");
 T inetSocketAddress1 = (T) inetSocketAddressDeserializer.deserialize(parser1, ctxt);
 Assertions.assertInstanceOf(InetSocketAddress.class, inetSocketAddress1);
 Assertions.assertEquals("192.168.1.1:8080", inetSocketAddress1.toString());
 // bracketed IPv6 address with port number
 JsonParser parser2 = objectMapper.getFactory().createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80\"");
 T inetSocketAddress2 = (T) inetSocketAddressDeserializer.deserialize(parser2, ctxt);
 Assertions.assertInstanceOf(InetSocketAddress.class, inetSocketAddress2);
 Assertions.assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334:80", inetSocketAddress2.toString());
 }

 @Test
 public void testStringBuilderDeserialization() throws JsonProcessingException {
 FromStringDeserializer stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"Hello, World!\"");
 T stringBuilder = (T) stringBuilderDeserializer.deserialize(parser, ctxt);
 Assertions.assertInstanceOf(StringBuilder.class, stringBuilder);
 Assertions.assertEquals("Hello, World!", stringBuilder.toString());
 }

 @Test
 public void testUnexpectedToken() throws JsonProcessingException {
 FromStringDeserializer stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 JsonParser parser = objectMapper.getFactory().createParser("\"[1, 2, 3]\"");
 T string = (T) stringDeserializer.deserialize(parser, ctxt);
 Assertions.assertNull(string);
 }
}