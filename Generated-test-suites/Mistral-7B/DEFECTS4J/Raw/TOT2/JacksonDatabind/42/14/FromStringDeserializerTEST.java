import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws Exception {
 File file = new File("path/to/file");
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 Object readValue = mapper.readValue("\"path/to/file\"", deserializer.getType());
 assertEquals(file, readValue);
 }

 @Test
 public void testDeserializeUrl() throws Exception {
 URL url = new URL("http://example.com");
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 Object readValue = mapper.readValue("\"http://example.com\"", deserializer.getType());
 assertEquals(url, readValue);
 }

 @Test
 public void testDeserializeUri() throws Exception {
 URI uri = new URI("http://example.com");
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 Object readValue = mapper.readValue("\"http://example.com\"", deserializer.getType());
 assertEquals(uri, readValue);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 Class<?> clazz = String.class;
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(clazz);
 Object readValue = mapper.readValue("java.lang.String.class", deserializer.getType());
 assertEquals(clazz, readValue);
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 Object readValue = mapper.readValue("com.fasterxml.jackson.core.type.ArrayType@535596e2", deserializer.getType());
 assertEquals(javaType, readValue);
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 Currency currency = Currency.getInstance("USD");
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Object readValue = mapper.readValue("USD", deserializer.getType());
 assertEquals(currency, readValue);
 }

 @Test
 public void testDeserializePattern() throws Exception {
 Pattern pattern = Pattern.compile(".*\\.txt");
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Object readValue = mapper.readValue("(.*\\.txt)", deserializer.getType());
 assertEquals(pattern, readValue);
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 Locale locale = new Locale("en", "US");
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Object readValue = mapper.readValue("en_US", deserializer.getType());
 assertEquals(locale, readValue);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 Charset charset = Charset.forName("UTF-8");
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Object readValue = mapper.readValue("UTF-8", deserializer.getType());
 assertEquals(charset, readValue);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 Object readValue = mapper.readValue("America/New_York", deserializer.getType());
 assertEquals(timeZone, readValue);
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 Object readValue = mapper.readValue("127.0.0.1", deserializer.getType());
 assertEquals(inetAddress, readValue);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws Exception {
 InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 Object readValue = mapper.readValue("[\"localhost\",8080]", deserializer.getType());
 assertEquals(inetSocketAddress, readValue);
 }

 @Test
 public void testEmptyStringFile() throws Exception {
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringUrl() throws Exception {
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringUri() throws Exception {
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 assertEquals(URI.create(""), mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringClass() throws Exception {
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(String.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringJavaType() throws Exception {
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringCurrency() throws Exception {
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringPattern() throws Exception {
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringLocale() throws Exception {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 assertEquals(Locale.ROOT, mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringCharset() throws Exception {
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringTimeZone() throws Exception {
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringInetAddress() throws Exception {
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test
 public void testEmptyStringInetSocketAddress() throws Exception {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 assertNull(mapper.readValue("", deserializer.getType()));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidFormatConfiguration() throws Exception {
 ObjectMapper mapperWithInvalidConfig = new ObjectMapper();
 mapperWithInvalidConfig.configure(DeserializationFeature.FAIL_ON_EMPTY_STRINGS, false);
 FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
 mapperWithInvalidConfig.readValue("", deserializer.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmbeddedObjectNull() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 mapper.readValue("null", deserializer.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmbeddedObjectInvalidType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object ob = new Object();
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 mapper.readValue("\"[1, 2, 3]\"", deserializer.getType(), ob);
 }
}