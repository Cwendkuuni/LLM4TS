package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FromStringDeserializerTest {

 // Test for deserializing different types using the Std deserializer
 @Test
 public void testDeserializeDifferentTypes() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  String fileString = "C:/temp/file.txt";
  FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
  File file = mapper.readValue(mapper.writerWithView(fileDeserializer).writeValueAsString(fileString), File.class);
  assertEquals(new File(fileString), file);

  String urlString = "http://www.example.com";
  FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
  URL url = mapper.readValue(mapper.writerWithView(urlDeserializer).writeValueAsString(urlString), URL.class);
  assertEquals(new URL(urlString), url);

  String uriString = "http://www.example.com/path/to/resource";
  FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
  URI uri = mapper.readValue(mapper.writerWithView(uriDeserializer).writeValueAsString(uriString), URI.class);
  assertEquals(new URI(uriString), uri);

  String className = "java.util.Date";
  FromStringDeserializer<Class<?>> classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
  Class<?> clazz = mapper.readValue(mapper.writerWithView(classDeserializer).writeValueAsString(className), Class.class);
  assertEquals(Date.class, clazz);

  String javaTypeString = "java.util.List<java.lang.String>";
  FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
  JavaType javaType = mapper.readValue(mapper.writerWithView(javaTypeDeserializer).writeValueAsString(javaTypeString), JavaType.class);
  assertEquals(mapper.getTypeFactory().constructParametricType(java.util.List.class, java.lang.String.class), javaType);

  String currencyString = "USD";
  FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
  Currency currency = mapper.readValue(mapper.writerWithView(currencyDeserializer).writeValueAsString(currencyString), Currency.class);
  assertEquals(Currency.getInstance("USD"), currency);

  String patternString = "(.*)\\.txt";
  FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
  Pattern pattern = mapper.readValue(mapper.writerWithView(patternDeserializer).writeValueAsString(patternString), Pattern.class);
  assertTrue(pattern.matcher("example.txt").matches());

  String localeString = "en_US";
  FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
  Locale locale = mapper.readValue(mapper.writerWithView(localeDeserializer).writeValueAsString(localeString), Locale.class);
  assertEquals(Locale.US, locale);

  String charsetString = "UTF-8";
  FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
  Charset charset = mapper.readValue(mapper.writerWithView(charsetDeserializer).writeValueAsString(charsetString), Charset.class);
  assertEquals(Charset.forName(charsetString), charset);

  String timeZoneString = "America/New_York";
  FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
  TimeZone timeZone = mapper.readValue(mapper.writerWithView(timeZoneDeserializer).writeValueAsString(timeZoneString), TimeZone.class);
  assertEquals(TimeZone.getTimeZone(timeZoneString), timeZone);

  String inetAddressString = "192.168.1.1";
  FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
  InetAddress inetAddress = mapper.readValue(mapper.writerWithView(inetAddressDeserializer).writeValueAsString(inetAddressString), InetAddress.class);
  assertEquals(InetAddress.getByName(inetAddressString), inetAddress);

  String inetSocketAddressString = "[255.255.255.255]:80";
  FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
  InetSocketAddress inetSocketAddress = mapper.readValue(mapper.writerWithView(inetSocketAddressDeserializer).writeValueAsString(inetSocketAddressString), InetSocketAddress.class);
  assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), inetSocketAddress);

  String stringBuilderString = "Hello World";
  FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
  StringBuilder stringBuilder = mapper.readValue(mapper.writerWithView(stringBuilderDeserializer).writeValueAsString(stringBuilderString), StringBuilder.class);
  assertEquals(new StringBuilder(stringBuilderString), stringBuilder);
 }

 // Test for handling null values
 @Test
 public void testDeserializeNullValues() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  String nullValue = null;
  FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
  String result = mapper.readValue(mapper.writerWithView(stringDeserializer).writeValueAsString(nullValue), String.class);
  assertEquals(null, result);
 }

 // Test for handling empty strings
 @Test
 public void testDeserializeEmptyStrings() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  String emptyString = "";
  FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
  String result = mapper.readValue(mapper.writerWithView(stringDeserializer).writeValueAsString(emptyString), String.class);
  assertEquals(emptyString, result);
 }

 // Test for handling JSON arrays
 @Test
 public void testDeserializeJsonArrays() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  String jsonArray = "[1, 2, 3]";
  FromStringDeserializer<int[]> intArrayDeserializer = FromStringDeserializer.findDeserializer(int[].class);
  int[] result = mapper.readValue(mapper.writerWithView(intArrayDeserializer).writeValueAsString(jsonArray), int[].class);
  assertArrayEquals(new int[]{1, 2, 3}, result);
 }

 // Test for handling JSON embedded objects
 @Test
 public void testDeserializeJsonEmbeddedObjects() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  String jsonEmbedded = "{\"value\": 42}";
  FromStringDeserializer<MyEmbeddedObject> embeddedObjectDeserializer = new MyEmbeddedObjectDeserializer();
  MyEmbeddedObject result = mapper.readValue(mapper.writerWithView(embeddedObjectDeserializer).writeValueAsString(jsonEmbedded), MyEmbeddedObject.class);
  assertEquals(42, result.getValue());
 }

 // Test for handling unexpected tokens
 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnexpectedTokens() throws IOException, JsonProcessingException {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  String unexpectedToken = "{";
  FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
  mapper.readValue(mapper.writerWithView(stringDeserializer).writeValueAsString(unexpectedToken), String.class);
 }

 // Helper class for testing JSON embedded objects
 static class MyEmbeddedObject {
  private int value;

  public int getValue() {
   return value;
  }

  public void setValue(int value) {
   this.value = value;
  }
 }

 static class MyEmbeddedObjectDeserializer extends FromStringDeserializer<MyEmbeddedObject> {
  public MyEmbeddedObjectDeserializer() {
   super(MyEmbeddedObject.class);
  }

  @Override
  protected MyEmbeddedObject _deserialize(String value, DeserializationContext ctxt) throws IOException {
   SimpleObjectMapper mapper = new SimpleObjectMapper();
   return mapper.readValue(mapper.writer().writeValueAsString("{\"value\": " + value + "}"), MyEmbeddedObject.class);
  }
 }
}