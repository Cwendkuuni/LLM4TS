import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 public FromStringDeserializerTest() {
 objectMapper = JsonMapperFactory.getMapper();
 }

 @Test
 public void testDeserializeFile() throws IOException, JsonProcessingException {
 File file = new File("testFile.txt");
 Std.StdFile deserializer = FromStringDeserializer.findDeserializer(File.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new File("FromStringDeserializerTest.java")), objectMapper.getDeserializationContext());
 assertEquals(file, result);
 }

 @Test
 public void testDeserializeURL() throws IOException, JsonProcessingException {
 URL url = new URL("https://www.example.com");
 Std.StdURL deserializer = FromStringDeserializer.findDeserializer(URL.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("https://www.example.com".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(url, result);
 }

 @Test
 public void testDeserializeURI() throws IOException, JsonProcessingException {
 URI uri = new URI("https://www.example.com");
 Std.StdURI deserializer = FromStringDeserializer.findDeserializer(URI.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("https://www.example.com".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(uri, result);
 }

 @Test
 public void testDeserializeClass() throws IOException, JsonProcessingException {
 Class<?> clazz = String.class;
 Std.StdClass deserializer = FromStringDeserializer.findDeserializer(clazz);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("java.lang.String.class".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(clazz, result.getClass());
 }

 @Test
 public void testDeserializeJavaType() throws IOException, JsonProcessingException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(String[].class);
 Std.StdJavaType deserializer = FromStringDeserializer.findDeserializer(javaType.getTypeId());
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("[".getBytes())), objectMapper.getDeserializationContext());
 TypeFactory typeFactory = objectMapper.getTypeFactory();
 JavaType javaTypeResult = typeFactory.constructType(typeFactory.constructType(String.class).getComponentType());
 assertEquals(javaTypeResult, result);
 }

 @Test
 public void testDeserializeCurrency() throws IOException, JsonProcessingException {
 Currency currency = Currency.getInstance("USD");
 Std.StdCurrency deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("USD".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(currency, result);
 }

 @Test
 public void testDeserializePattern() throws IOException, JsonProcessingException {
 Pattern pattern = Pattern.compile("\\w+");
 Std.StdPattern deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("\\w+".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(pattern, result);
 }

 @Test
 public void testDeserializeLocale() throws IOException, JsonProcessingException {
 Locale locale = new Locale("en", "US");
 Std.StdLocale deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("en_US".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(locale, result);
 }

 @Test
 public void testDeserializeCharset() throws IOException, JsonProcessingException {
 Charset charset = Charset.forName("UTF-8");
 Std.StdCharset deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("UTF-8".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(charset, result);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Std.StdTimeZone deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("UTC".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(timeZone, result);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
 InetAddress inetAddress = InetAddress.getByName("192.168.1.1");
 Std.StdInetAddress deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("192.168.1.1".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(inetAddress, result);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
 InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.1.1", 80);
 Std.StdInetSocketAddress deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("192.168.1.1:80".getBytes())), objectMapper.getDeserializationContext());
 assertEquals(inetSocketAddress, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException, JsonProcessingException {
 Std.StdClass deserializer = FromStringDeserializer.findDeserializer(Object.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("java.lang.String.class".getBytes())), objectMapper.getDeserializationContext());
 fail("Should have thrown JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
 Std.StdLocale deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("".getBytes())), objectMapper.getDeserializationContext());
 fail("Should have thrown JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidStringFormat() throws IOException, JsonProcessingException {
 Std.StdURL deserializer = FromStringDeserializer.findDeserializer(URL.class);
 T result = deserializer.deserialize(objectMapper.getFactory().createParser(new ByteArrayInputStream("https://www.example.com:80".getBytes())), objectMapper.getDeserializationContext());
 fail("Should have thrown JsonMappingException");
 }
}