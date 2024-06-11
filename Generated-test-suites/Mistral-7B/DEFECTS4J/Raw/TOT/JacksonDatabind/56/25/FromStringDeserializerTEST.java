package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFileDeserialization() throws JsonProcessingException {
 File file = new File("test.txt");
 FromStringDeserializer<File> serializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
 assertEquals(file, serializer.deserialize(mapper.getFactory().createParser("new File(\"test.txt\")"), mapper.getDeserializationContext()));
 }

 @Test
 public void testUrlDeserialization() throws JsonProcessingException {
 URL url = new URL("http://example.com");
 FromStringDeserializer<URL> serializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
 assertEquals(url, serializer.deserialize(mapper.getFactory().createParser("http://example.com"), mapper.getDeserializationContext()));
 }

 @Test
 public void testUriDeserialization() throws JsonProcessingException {
 URI uri = new URI("http://example.com");
 FromStringDeserializer<URI> serializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
 assertEquals(uri, serializer.deserialize(mapper.getFactory().createParser("http://example.com"), mapper.getDeserializationContext()));
 }

 @Test
 public void testClassDeserialization() throws JsonProcessingException {
 Class<?> clazz = String.class;
 FromStringDeserializer<Class<?>> serializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(clazz.getName());
 assertEquals(clazz, serializer.deserialize(mapper.getFactory().createParser("java.lang.String"), mapper.getDeserializationContext()));
 }

 @Test
 public void testJavaTypeDeserialization() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 FromStringDeserializer<JavaType> serializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(javaType.getClass());
 assertEquals(javaType, serializer.deserialize(mapper.getFactory().createParser("class [Ljava.lang.String;"), mapper.getDeserializationContext()));
 }

 @Test
 public void testCurrencyDeserialization() throws JsonProcessingException {
 Currency currency = Currency.getInstance("USD");
 FromStringDeserializer<Currency> serializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
 assertEquals(currency, serializer.deserialize(mapper.getFactory().createParser("USD"), mapper.getDeserializationContext()));
 }

 @Test
 public void testPatternDeserialization() throws JsonProcessingException {
 Pattern pattern = Pattern.compile("[a-z]+");
 FromStringDeserializer<Pattern> serializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
 assertEquals(pattern, serializer.deserialize(mapper.getFactory().createParser("[a-z]+"), mapper.getDeserializationContext()));
 }

 @Test
 public void testLocaleDeserialization() throws JsonProcessingException {
 Locale locale = Locale.US;
 FromStringDeserializer<Locale> serializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
 assertEquals(locale, serializer.deserialize(mapper.getFactory().createParser("en_US"), mapper.getDeserializationContext()));
 }

 @Test
 public void testCharsetDeserialization() throws JsonProcessingException {
 Charset charset = Charset.forName("UTF-8");
 FromStringDeserializer<Charset> serializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
 assertEquals(charset, serializer.deserialize(mapper.getFactory().createParser("UTF-8"), mapper.getDeserializationContext()));
 }

 @Test
 public void testTimeZoneDeserialization() throws JsonProcessingException {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FromStringDeserializer<TimeZone> serializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
 assertEquals(timeZone, serializer.deserialize(mapper.getFactory().createParser("UTC"), mapper.getDeserializationContext()));
 }

 @Test
 public void testInetAddressDeserialization() throws JsonProcessingException {
 InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
 FromStringDeserializer<InetAddress> serializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
 assertEquals(inetAddress, serializer.deserialize(mapper.getFactory().createParser("127.0.0.1"), mapper.getDeserializationContext()));
 }

 @Test
 public void testInetSocketAddressDeserialization() throws JsonProcessingException {
 InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
 FromStringDeserializer<InetSocketAddress> serializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 assertEquals(inetSocketAddress, serializer.deserialize(mapper.getFactory().createParser("[\"127.0.0.1\"]:8080"), mapper.getDeserializationContext()));
 assertEquals(inetSocketAddress, serializer.deserialize(mapper.getFactory().createParser("[\"127.0.0.1\"]:8080"), mapper.getDeserializationContext()));
 }

 @Test
 public void testEmptyStringDeserialization() throws JsonProcessingException {
 FromStringDeserializer<Object> serializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
 assertNull(serializer.deserialize(mapper.getFactory().createParser(""), mapper.getDeserializationContext()));
 }

 @Test
 public void testInvalidDeserialization() throws JsonProcessingException {
 String jsonString = "\"invalid_class\"";
 assertThrows(JsonMappingException.class, () -> mapper.readValue(jsonString, Object.class));
 }
}