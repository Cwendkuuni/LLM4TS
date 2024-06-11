package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String fileString = "/path/to/file.txt";
 File expectedFile = new File("/path/to/file.txt");
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(fileString.getBytes())));
 when(context.getTypeFactory().constructValueType(File.class)).thenReturn(JavaType.construct(File.class, mapper.getTypeFactory()));

 T result = fileDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(fileString.getBytes())), context);
 assertEquals(expectedFile, result);
 }

 @Test
 public void testDeserializeURL() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String urlString = "http://www.example.com";
 URL expectedUrl = new URL(urlString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(urlString.getBytes())));
 when(context.getTypeFactory().constructValueType(URL.class)).thenReturn(JavaType.construct(URL.class, mapper.getTypeFactory()));

 T result = urlDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(urlString.getBytes())), context);
 assertEquals(expectedUrl, result);
 }

 @Test
 public void testDeserializeURI() throws IOException {
 FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String uriString = "http://www.example.com/path/to/resource";
 URI expectedUri = new URI(uriString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(uriString.getBytes())));
 when(context.getTypeFactory().constructValueType(URI.class)).thenReturn(JavaType.construct(URI.class, mapper.getTypeFactory()));

 T result = uriDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(uriString.getBytes())), context);
 assertEquals(expectedUri, result);
 }

 @Test
 public void testDeserializeClass() throws IOException {
 FromStringDeserializer<Class<?>> clazzDeserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String className = "java.lang.String";
 Class<?> expectedClass = Class.forName(className);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(className.getBytes())));
 when(context.getTypeFactory().constructValueType(Class.class)).thenReturn(JavaType.construct(Class.class, mapper.getTypeFactory()));

 T result = clazzDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(className.getBytes())), context);
 assertEquals(expectedClass, result);
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String jsonTypeString = "{\"type\":\"int\",\"rawType\":\"java.lang.Integer\"}";
 JavaType expectedJavaType = mapper.getTypeFactory().constructType(JavaType.class);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(jsonTypeString.getBytes())));
 when(context.getTypeFactory().constructValueType(JavaType.class)).thenReturn(JavaType.construct(JavaType.class, mapper.getTypeFactory()));

 T result = javaTypeDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(jsonTypeString.getBytes())), context);
 assertEquals(expectedJavaType, result);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String currencyString = "USD";
 Currency expectedCurrency = Currency.getInstance(Locale.US);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(currencyString.getBytes())));
 when(context.getTypeFactory().constructValueType(Currency.class)).thenReturn(JavaType.construct(Currency.class, mapper.getTypeFactory()));

 T result = currencyDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(currencyString.getBytes())), context);
 assertEquals(expectedCurrency, result);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String patternString = ".*";
 Pattern expectedPattern = Pattern.compile(patternString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(patternString.getBytes())));
 when(context.getTypeFactory().constructValueType(Pattern.class)).thenReturn(JavaType.construct(Pattern.class, mapper.getTypeFactory()));

 T result = patternDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(patternString.getBytes())), context);
 assertEquals(expectedPattern, result);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String localeString = "en_US_POSIX";
 Locale expectedLocale = Locale.forLanguageTag(localeString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(localeString.getBytes())));
 when(context.getTypeFactory().constructValueType(Locale.class)).thenReturn(JavaType.construct(Locale.class, mapper.getTypeFactory()));

 T result = localeDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(localeString.getBytes())), context);
 assertEquals(expectedLocale, result);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String charsetString = "UTF-8";
 Charset expectedCharset = Charset.forName(charsetString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(charsetString.getBytes())));
 when(context.getTypeFactory().constructValueType(Charset.class)).thenReturn(JavaType.construct(Charset.class, mapper.getTypeFactory()));

 T result = charsetDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(charsetString.getBytes())), context);
 assertEquals(expectedCharset, result);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String timeZoneString = "America/Los_Angeles";
 TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(timeZoneString.getBytes())));
 when(context.getTypeFactory().constructValueType(TimeZone.class)).thenReturn(JavaType.construct(TimeZone.class, mapper.getTypeFactory()));

 T result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(timeZoneString.getBytes())), context);
 assertEquals(expectedTimeZone, result);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String inetAddressString = "192.168.1.1";
 InetAddress expectedInetAddress = InetAddress.getByName(inetAddressString);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(inetAddressString.getBytes())));
 when(context.getTypeFactory().constructValueType(InetAddress.class)).thenReturn(JavaType.construct(InetAddress.class, mapper.getTypeFactory()));

 T result = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(inetAddressString.getBytes())), context);
 assertEquals(expectedInetAddress, result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 DeserializationContext context = mock(DeserializationContext.class);
 String inetSocketAddressString = "[255.255.255.255]:8080";
 InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("255.255.255.255", 8080);
 when(context.getParser()).thenReturn(mapper.getFactory().createParser(new ByteArrayInputStream(inetSocketAddressString.getBytes())));
 when(context.getTypeFactory().constructValueType(InetSocketAddress.class)).thenReturn(JavaType.construct(InetSocketAddress.class, mapper.getTypeFactory()));

 T result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(inetSocketAddressString.getBytes())), context);
 assertEquals(expectedInetSocketAddress, result);
 }

 @Test
 public void testDeserializeNull() throws IOException {
 FromStringDeserializer<Object> objectDeserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectParser parser = mapper.getFactory().getParserForObject(context, null);

 T result = objectDeserializer.deserialize(parser, context);
 assertNull(result);
 }

 @Test(expected = InvalidFormatException.class)
 public void testDeserializeInvalidString() throws IOException {
 FromStringDeserializer<Object> objectDeserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
 DeserializationContext context = mock(DeserializationContext.class);
 ObjectParser parser = mapper.getFactory().getParserForObject(context, "invalidString");

 T result = objectDeserializer.deserialize(parser, context);
 }

}