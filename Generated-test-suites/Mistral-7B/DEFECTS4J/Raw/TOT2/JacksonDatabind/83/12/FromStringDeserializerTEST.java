import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeFile() throws Exception {
 File file = new File("test.txt");
 FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
 File deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"test.txt\""), mapper.getDeserializationContext());
 assertEquals(file, deserializedObject);
 }

 @Test
 public void testDeserializeUrl() throws Exception {
 URL url = new URL("http://example.com");
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 URL deserializedObject = (URL) deserializer.deserialize(mapper.getFactory().createParser("\"http://example.com\""), mapper.getDeserializationContext());
 assertEquals(url, deserializedObject);
 }

 @Test
 public void testDeserializeUri() throws Exception {
 URI uri = new URI("http://example.com");
 FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
 URI deserializedObject = (URI) deserializer.deserialize(mapper.getFactory().createParser("\"http://example.com\""), mapper.getDeserializationContext());
 assertEquals(uri, deserializedObject);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 Class<?> clazz = String.class;
 FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(clazz);
 Class<?> deserializedObject = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser("\"java.lang.String\""), mapper.getDeserializationContext());
 assertEquals(clazz, deserializedObject);
 }

 @Test
 public void testDeserializeJavaType() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
 JavaType deserializedObject = (JavaType) deserializer.deserialize(mapper.getFactory().createParser("\"java.lang.String[]\""), mapper.getDeserializationContext());
 assertEquals(javaType, deserializedObject);
 }

 @Test
 public void testDeserializeCurrency() throws Exception {
 FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 Currency deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"USD\""), mapper.getDeserializationContext());
 assertEquals(Currency.getInstance("USD"), deserializedObject);
 }

 @Test
 public void testDeserializePattern() throws Exception {
 FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 Pattern deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"/([a-z]+)\\.js\""), mapper.getDeserializationContext());
 assertNotNull(deserializedObject);
 }

 @Test
 public void testDeserializeLocale() throws Exception {
 FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 Locale deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"en_US\""), mapper.getDeserializationContext());
 assertEquals(Locale.US, deserializedObject);
 }

 @Test
 public void testDeserializeCharset() throws Exception {
 FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 Charset deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"UTF-8\""), mapper.getDeserializationContext());
 assertEquals(Charset.forName("UTF-8"), deserializedObject);
 }

 @Test
 public void testDeserializeTimeZone() throws Exception {
 FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"America/New_York\""), mapper.getDeserializationContext());
 assertEquals(TimeZone.getTimeZone("America/New_York"), deserializedObject);
 }

 @Test
 public void testDeserializeInetAddress() throws Exception {
 FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 InetAddress deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"127.0.0.1\""), mapper.getDeserializationContext());
 assertEquals(InetAddress.getByName("127.0.0.1"), deserializedObject);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws Exception {
 FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 InetSocketAddress deserializedObject = deserializer.deserialize(mapper.getFactory().createParser("\"127.0.0.1:80\""), mapper.getDeserializationContext());
 assertEquals(new InetSocketAddress("127.0.0.1", 80), deserializedObject);
 }

 @Test
 public void testDeserializeStringBuilder() throws Exception {
 FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder deserializedObject = (StringBuilder) deserializer.deserialize(mapper.getFactory().createParser("\"hello world\""), mapper.getDeserializationContext());
 assertEquals("hello world", deserializedObject.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidFromStringDeserializer() throws Exception {
 FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
 deserializer.deserialize(mapper.getFactory().createParser("\"invalid\""), mapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testEmptyString() throws Exception {
 FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
 String result = (String) deserializer.deserialize(mapper.getFactory().createParser("\"\""), mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got " + result);
 }

 @Test(expected = MalformedURLException.class)
 public void testInvalidUrl() throws Exception {
 FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
 deserializer.deserialize(mapper.getFactory().createParser("\"http://example.com/..\""), mapper.getDeserializationContext());
 }
}