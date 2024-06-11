package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFileDeserialization() throws JsonParseException, JsonMappingException, IOException {
 File file = new File("test.txt");
 Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 assertEquals(file, fileDeserializer.deserialize(objectMapper.getFactory().createParser("\"test.txt\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testURLDeserialization() throws JsonParseException, JsonMappingException, IOException {
 URL url = new URL("http://example.com");
 Std.StdURL urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 assertEquals(url, urlDeserializer.deserialize(objectMapper.getFactory().createParser("\"http://example.com\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testURIDeserialization() throws JsonParseException, JsonMappingException, IOException {
 URI uri = new URI("http://example.com");
 Std.StdURI uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
 assertEquals(uri, uriDeserializer.deserialize(objectMapper.getFactory().createParser("\"http://example.com\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testClassDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Class<?> stringClass = String.class;
 Std.StdClass stringDeserializer = FromStringDeserializer.findDeserializer(stringClass);
 assertEquals(stringClass, stringDeserializer.deserialize(objectMapper.getFactory().createParser("\"java.lang.String\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testJavaTypeDeserialization() throws JsonParseException, JsonMappingException, IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 Std.StdJavaType javaTypeDeserializer = FromStringDeserializer.findDeserializer(javaType.getClass());
 assertEquals(javaType, javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser("\"com.example.junit.Test JavaType\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testCurrencyDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdCurrency currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
 assertEquals(Currency.getInstance("USD"), currencyDeserializer.deserialize(objectMapper.getFactory().createParser("\"USD\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testPatternDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdPattern patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 assertEquals(Pattern.compile("\\d+"), patternDeserializer.deserialize(objectMapper.getFactory().createParser("\"\\\\d+\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testLocaleDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdLocale localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 // Single argument
 assertEquals(Locale.ENGLISH, localeDeserializer.deserialize(objectMapper.getFactory().createParser("\"en\""), objectMapper.getDeserializationContext()));
 // Two arguments
 assertEquals(Locale.FRENCH, localeDeserializer.deserialize(objectMapper.getFactory().createParser("\"fr_FR\""), objectMapper.getDeserializationContext()));
 // Three arguments
 assertEquals(Locale.ITALIAN, localeDeserializer.deserialize(objectMapper.getFactory().createParser("\"it_IT_IT\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testCharsetDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdCharset charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
 assertEquals(Charset.forName("UTF-8"), charsetDeserializer.deserialize(objectMapper.getFactory().createParser("\"UTF-8\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testTimeZoneDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdTimeZone timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 assertEquals(TimeZone.getTimeZone("UTC"), timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser("\"UTC\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testInetAddressDeserialization() throws JsonParseException, JsonMappingException, IOException {
 InetAddress inetAddress = InetAddress.getByName("192.168.1.1");
 Std.StdInetAddress inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 assertEquals(inetAddress, inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser("\"192.168.1.1\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testInetSocketAddressDeserialization() throws JsonParseException, JsonMappingException, IOException {
 InetAddress inetAddress = InetAddress.getByName("192.168.1.1");
 Std.StdInetSocketAddress inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 assertEquals(new InetSocketAddress(inetAddress, 80), inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser("\"192.168.1.1:80\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testInvalidDeserialization() throws JsonParseException, JsonMappingException, IOException {
 ObjectMapper context = Mockito.mock(ObjectMapper.class);
 Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 Std.StdURL urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);

 io.IOException ioe = Mockito.mock(IOException.class);
 JsonMappingException jme = new JsonMappingException("Test error");
 DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

 when(context.getDeserializationContext()).thenReturn(ctxt);
 when(ctxt.getFactory()).thenReturn(objectMapper.getFactory());

 when(ctxt.findClass(Mockito.anyString())).thenThrow(ioe);
 when(ctxt.weirdStringException(Mockito.anyString(), Mockito.any(Class.class), Mockito.anyString())).thenThrow(jme);

 try {
 fileDeserializer.deserialize(objectMapper.getFactory().createParser("\"java.util.Date\""), context);
 fail("Expected JsonMappingException or IOException");
 } catch (JsonMappingException ex) {
 // Expected
 assertEquals("Test error", ex.getMessage());
 } catch (IOException ex) {
 // Unexpected
 fail("Expected JsonMappingException");
 }

 try {
 urlDeserializer.deserialize(objectMapper.getFactory().createParser("\"java.lang.String\""), context);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException ex) {
 // Expected
 } catch (IOException ex) {
 // Unexpected
 fail("Expected JsonMappingException");
 }
}

 @Test
 public void testEmptyStringDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdFile emptyFileDeserializer = FromStringDeserializer.findDeserializer(File.class);
 assertEquals(null, emptyFileDeserializer.deserialize(objectMapper.getFactory().createParser("\"\""), objectMapper.getDeserializationContext()));

 Std.StdLocale emptyLocaleDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 assertEquals(Locale.getDefault(), emptyLocaleDeserializer.deserialize(objectMapper.getFactory().createParser("\"\""), objectMapper.getDeserializationContext()));
 }

 @Test
 public void testEmptyArrayDeserialization() throws JsonParseException, JsonMappingException, IOException {
 Std.StdURL emptyURLDeserializer = FromStringDeserializer.findDeserializer(URL.class);
 JsonParser parser = objectMapper.getFactory().createParser("\"[]\"");
 Object obj = emptyURLDeserializer.deserialize(parser, objectMapper.getDeserializationContext());
 assertNull(obj);
 }

 @After
 public void tearDown() throws Exception {
 // Nothing to do here
 }
}