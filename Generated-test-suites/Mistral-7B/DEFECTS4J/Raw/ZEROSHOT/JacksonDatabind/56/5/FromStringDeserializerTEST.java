package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;
 private DeserializationContext context;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 context = mock(DeserializationContext.class);
 }

 @Test
 public void testDeserializeUrl() throws Exception {
 FromStringDeserializer urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 JsonParser parser = objectMapper.getFactory().createParser("http://example.com");
 T result = urlDeserializer.deserialize(parser, context);
 assertEquals("http://example.com", result.toString());
 }

 @Test
 public void testDeserializeUri() throws Exception {
 FromStringDeserializer uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 JsonParser parser = objectMapper.getFactory().createParser("http://example.com/path/to/file");
 T result = uriDeserializer.deserialize(parser, context);
 assertEquals("http://example.com/path/to/file", result.toString());
 }

 @Test
 public void testDeserializeFile() throws Exception {
 FromStringDeserializer fileDeserializer = new FromStringDeserializer<File>(File.class);
 JsonParser parser = objectMapper.getFactory().createParser("path/to/file");
 T result = fileDeserializer.deserialize(parser, context);
 assertNotNull(result);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
 JsonParser parser = objectMapper.getFactory().createParser("java.lang.String");
 JavaType javaType = context.getTypeFactory().constructType(parser.getValueAsString());
 T result = classDeserializer.deserialize(parser, context);
 assertEquals(javaType.getClass(), result.getClass());
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 FromStringDeserializer javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 JsonParser parser = objectMapper.getFactory().createParser("java.lang.String");
 JavaType javaType = context.getTypeFactory().constructType(parser.getValueAsString());
 T result = javaTypeDeserializer.deserialize(parser, context);
 assertEquals(javaType, result);
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 FromStringDeserializer currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 JsonParser parser = objectMapper.getFactory().createParser("EUR");
 T result = currencyDeserializer.deserialize(parser, context);
 assertEquals(Currency.getInstance("EUR"), result);
 }

 @Test
 public void testDeserializePattern() throws Exception {
 FromStringDeserializer patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 JsonParser parser = objectMapper.getFactory().createParser("/(ab){2}c/g");
 T result = patternDeserializer.deserialize(parser, context);
 assertEquals(Pattern.compile("/(ab){2}c/g"), result);
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 FromStringDeserializer localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 JsonParser parser = objectMapper.getFactory().createParser("en_US");
 T result = localeDeserializer.deserialize(parser, context);
 assertEquals(Locale.US, result);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 FromStringDeserializer charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 JsonParser parser = objectMapper.getFactory().createParser("UTF-8");
 T result = charsetDeserializer.deserialize(parser, context);
 assertEquals(Charset.forName("UTF-8"), result);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 FromStringDeserializer timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 JsonParser parser = objectMapper.getFactory().createParser("UTC");
 T result = timeZoneDeserializer.deserialize(parser, context);
 assertEquals(TimeZone.getTimeZone("UTC"), result);
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 FromStringDeserializer inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 JsonParser parser = objectMapper.getFactory().createParser("192.168.1.1");
 T result = inetAddressDeserializer.deserialize(parser, context);
 assertNotNull(result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws Exception {
 FromStringDeserializer inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 // IPv4
 JsonParser parser = objectMapper.getFactory().createParser("[192.168.1.1]:8080");
 T result = inetSocketAddressDeserializer.deserialize(parser, context);
 assertEquals(new InetSocketAddress("192.168.1.1", 8080), result);

 // IPv6
 parser = objectMapper.getFactory().createParser("[2001:db8:85a3:0:0:8a2e:370:7334]:8080");
 result = inetSocketAddressDeserializer.deserialize(parser, context);
 assertEquals(new InetSocketAddress("2001:db8:85a3:0:0:8a2e:370:7334", 8080), result);
 }

 @Test
 public void testDeserializeEmptyStringFile() throws Exception {
 FromStringDeserializer fileDeserializer = new FromStringDeserializer<File>(File.class);
 JsonParser parser = objectMapper.getFactory().createParser("");
 T result = fileDeserializer.deserialize(parser, context);
 assertNotNull(result);
 }

 @Test
 public void testDeserializeEmptyStringUri() throws Exception {
 FromStringDeserializer uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 JsonParser parser = objectMapper.getFactory().createParser("");
 T result = uriDeserializer.deserialize(parser, context);
 assertEquals(URI.create(""), result);
 }

 @Test
 public void testDeserializeEmptyStringLocale() throws Exception {
 FromStringDeserializer localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 JsonParser parser = objectMapper.getFactory().createParser("");
 T result = localeDeserializer.deserialize(parser, context);
 assertEquals(Locale.ROOT, result);
 }
}