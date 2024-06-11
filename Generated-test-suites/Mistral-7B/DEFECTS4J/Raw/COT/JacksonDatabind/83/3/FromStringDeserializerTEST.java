package com.yourpackagename.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeMethods() throws IOException {
 Class<?>[] types = FromStringDeserializer.types();
 for (Class<?> type : types) {
 StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(type);
 assertEquals(type, deserializer.getValueClass());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeStringValue() throws IOException {
 String text = "testStringValue";
 FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
 StringBuilder expected = new StringBuilder(text);
 assertEquals(expected, stringBuilderDeserializer.deserialize(mapper.createParser("{\"value\":\"" + text + "\"}"), null));
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
 assertEquals(null, stringDeserializer.deserialize(mapper.createParser("{\"value\":\"" + "\"}"), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidFormat() throws IOException {
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 String invalidFileValue = "invalidFileValue";
 fileDeserializer.deserialize(mapper.createParser("{\"value\":\"" + invalidFileValue + "\"}"), null);
 }

 @Test
 public void testDeserializeURL() throws IOException {
 String urlStringValue = "http://example.com";
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 URL expected = new URL(urlStringValue);
 assertEquals(expected, urlDeserializer.deserialize(mapper.createParser("{\"value\":\"" + urlStringValue + "\"}"), null));
 }

 @Test(expected = MalformedURLException.class)
 public void testDeserializeMalformedURL() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 String malformedURLStringValue = "invalidURLValue";
 urlDeserializer.deserialize(mapper.createParser("{\"value\":\"" + malformedURLStringValue + "\"}"), null);
 }

 @Test
 public void testDeserializeURI() throws IOException {
 String uriStringValue = "http://example.com/path";
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 URI expected = new URI(uriStringValue);
 assertEquals(expected, uriDeserializer.deserialize(mapper.createParser("{\"value\":\"" + uriStringValue + "\"}"), null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeInvalidClassValue() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(int.class);
 deserializer.deserialize(mapper.createParser("{\"value\":42}"), null);
 }

 @Test
 public void testDeserializeClass() throws IOException {
 Class<?> clazz = String.class;
 FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
 assertEquals(clazz, classDeserializer.deserialize(mapper.createParser("{\"value\":\"" + clazz.getName() + "\"}"), null));
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 assertEquals(javaType, javaTypeDeserializer.deserialize(mapper.createParser("{\"value\":\"" + javaType.toString() + "\"}"), null));
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 String currencyCode = "USD";
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 Currency expected = Currency.getInstance(currencyCode);
 assertEquals(expected, currencyDeserializer.deserialize(mapper.createParser("{\"value\":\"" + currencyCode + "\"}"), null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeInvalidPattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 patternDeserializer.deserialize(mapper.createParser("{\"value\":\"invalidPatternValue\"}"), null);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 String patternValue = "[a-z]+";
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 Pattern expected = Pattern.compile(patternValue);
 assertEquals(expected, patternDeserializer.deserialize(mapper.createParser("{\"value\":\"" + patternValue + "\"}"), null));
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 String localeStringValue = "en_US";
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 Locale expected = new Locale("en", "US");
 assertEquals(expected, localeDeserializer.deserialize(mapper.createParser("{\"value\":\"" + localeStringValue + "\"}"), null));
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 String charsetName = "UTF-8";
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 Charset expected = Charset.forName(charsetName);
 assertEquals(expected, charsetDeserializer.deserialize(mapper.createParser("{\"value\":\"" + charsetName + "\"}"), null));
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 String timeZoneID = "UTC";
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 TimeZone expected = TimeZone.getTimeZone(timeZoneID);
 assertEquals(expected, timeZoneDeserializer.deserialize(mapper.createParser("{\"value\":\"" + timeZoneID + "\"}"), null));
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 String inetAddressStringValue = "192.168.0.1";
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 InetAddress expected = InetAddress.getByName(inetAddressStringValue);
 assertEquals(expected, inetAddressDeserializer.deserialize(mapper.createParser("{\"value\":\"" + inetAddressStringValue + "\"}"), null));
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 String inetSocketAddressStringValue = "192.168.0.1:8080";
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 InetSocketAddress expected = new InetSocketAddress("192.168.0.1", 8080);
 assertEquals(expected, inetSocketAddressDeserializer.deserialize(mapper.createParser("{\"value\":\"" + inetSocketAddressStringValue + "\"}"), null));
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
 assertEquals(null, deserializer.deserialize(mapper.createParser("[]"), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidArrayValue() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
 deserializer.deserialize(mapper.createParser("[\"" + "invalidValue" + "\"]"), null);
 }

 @Test
 public void testDeserializeArrayOfStrings() throws IOException {
 String[] stringArrayValues = { "testString1", "testString2" };
 SimpleModule module = new SimpleModule();
 module.addSerializer(String[].class, new ArraySerializer<>());
 module.addDeserializer(String[].class, new ArrayDeserializer<>());
 mapper.registerModule(module);
 FromStringDeserializer<String[]> stringArrayDeserializer = new FromStringDeserializer<String[]>(String[].class);
 String jsonStringValue = "[\"" + String.join("\",\"", stringArrayValues) + "\"]";
 String[] expected = stringArrayValues;
 assertEquals(expected, stringArrayDeserializer.deserialize(mapper.createParser(jsonStringValue), null));
 }

 @Test
 public void testDeserializeArrayOfIntegers() throws IOException {
 int[] intArrayValues = { 1, 2, 3 };
 SimpleModule module = new SimpleModule();
 module.addSerializer(int[].class, new ArraySerializer<>());
 module.addDeserializer(int[].class, new ArrayDeserializer<>());
 mapper.registerModule(module);
 FromStringDeserializer<int[]> intArrayDeserializer = new FromStringDeserializer<int[]>(int[].class);
 String jsonIntArrayValue = "[" + Integer.toString(intArrayValues[0]) + "," +
 Integer.toString(intArrayValues[1]) + "," +
 Integer.toString(intArrayValues[2]) + "]";
 int[] expected = intArrayValues;
 assertEquals(expected, intArrayDeserializer.deserialize(mapper.createParser(jsonIntArrayValue), null));
 }

 @Test
 public void testDeserializeArrayOfCustomObjects() throws IOException {
 SimpleModule module = new SimpleModule();
 module.addSerializer(CustomObject.class, new CustomObjectSerializer());
 module.addDeserializer(CustomObject.class, new CustomObjectDeserializer());
 mapper.registerModule(module);
 FromStringDeserializer<CustomObject[]> customObjectArrayDeserializer = new FromStringDeserializer<CustomObject[]>(CustomObject[].class);
 CustomObject[] expectedObjects = { new CustomObject("test1"), new CustomObject("test2") };
 String jsonCustomObjectArrayValue = "[" + mapper.writeValueAsString(expectedObjects[0]) + "," +
 mapper.writeValueAsString(expectedObjects[1]) + "]";
 assertEquals(expectedObjects, customObjectArrayDeserializer.deserialize(mapper.createParser(jsonCustomObjectArrayValue), null));
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Test
 public void testDeserializeEmbeddedObject_Null() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
 assertEquals(null, deserializer._deserializeEmbedded(null, null));
 }

 @Test
 public void testDeserializeEmbeddedObject_InvalidClass() throws IOException {
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
 Object invalidEmbeddedObject = new Object() {
 };
 assertEquals(null, deserializer._deserializeEmbedded(invalidEmbeddedObject, null));
 }

 @Test
 public void testDeserializeEmbeddedObject_Object() throws IOException {
 Object embeddedObject = new Object() {
 };
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
 SimpleObject expected = new SimpleObject();
 assertEquals(expected, deserializer._deserializeEmbedded(embeddedObject, null));
 }

 @Test
 public void testDeserializeEmbeddedObject_StringBuilder() throws IOException {
 StringBuilder embeddedStringBuilder = new StringBuilder();
 FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(StringBuilder.class);
 StringBuilder expected = new StringBuilder();
 assertEquals(expected, deserializer._deserializeEmbedded(embeddedStringBuilder, null));
 }
}

 class ArraySerializer<T> extends StdSerializer<T[]> {
 @Override
 public T[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 ObjectMapper objectMapper = (ObjectMapper) ctxt.getContext();
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 JavaType javaType = typeFactory.constructArrayType(ctxt.getTypeFactory().constructType(getValueClass()), null);
 return objectMapper.readValue(p, javaType);
 }
 }

 class ArrayDeserializer<T> extends JsonDeserializer<T[]> {
 @Override
 public T[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 ObjectMapper objectMapper = (ObjectMapper) ctxt.getContext();
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 JavaType javaType = typeFactory.constructArrayType(ctxt.getTypeFactory().constructType(getValueClass()), null);
 return objectMapper.readValue(p, javaType);
 }
 }

 class CustomObject extends Object {
 private final String name;

 public CustomObject(String name) {
 this.name = name;
 }

 @Override
 public String toString() {
 return "CustomObject{" +
 "name='" + name + '\'' +
 '}';
 }
 }

 class CustomObjectSerializer extends StdSerializer<CustomObject> {
 protected CustomObjectSerializer() {
 this(null);
 }

 protected CustomObjectSerializer(Class<?> t) {
 super(t);
 }

 @Override
 public CustomObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String name = p.getValueAsString();
 return new CustomObject(name);
 }
 }

 class CustomObjectDeserializer extends JsonDeserializer<CustomObject> {
 @Override
 public CustomObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String name = p.getValueAsString();
 return new CustomObject(name);
 }
}