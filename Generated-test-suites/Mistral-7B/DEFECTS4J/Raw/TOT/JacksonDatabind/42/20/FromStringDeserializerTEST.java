import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

 @Test
 public void testFileDeserialization() throws IOException {
 File file = new File("testFile.txt");
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
 JsonNode node = mapper.readTree("{\"className\":\"File\",\"value\":\"" + file.getAbsolutePath() + "\"}");
 File deserializedObject = deserializer.deserialize(mapper.treeToValues(node, File.class)[0], mapper.getContext());
 assertEquals(file, deserializedObject);
 }

 @Test
 public void testURLDeserialization() throws IOException {
 URL url = new URL("http://www.example.com");
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
 JsonNode node = mapper.readTree("{\"className\":\"URL\",\"value\":\"" + url.toString() + "\"}");
 URL deserializedObject = deserializer.deserialize(mapper.treeToValues(node, URL.class)[0], mapper.getContext());
 assertEquals(url, deserializedObject);
 }

 @Test
 public void testURIDeserialization() throws IOException {
 URI uri = new URI("http://www.example.com");
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
 JsonNode node = mapper.readTree("{\"className\":\"URI\",\"value\":\"" + uri.toString() + "\"}");
 URI deserializedObject = deserializer.deserialize(mapper.treeToValues(node, URI.class)[0], mapper.getContext());
 assertEquals(uri, deserializedObject);
 }

 @Test
 public void testClassDeserialization() throws IOException {
 Class<?> clazz = String.class;
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(clazz);
 JsonNode node = mapper.readTree("{\"className\":\"Class\",\"value\":\"" + clazz.getName() + "\"}");
 assertEquals(clazz, deserializer.deserialize(mapper.treeToValues(node, Class.class)[0], mapper.getContext()));
 }

 @Test
 public void testJavaTypeDeserialization() throws IOException {
 JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String[].class);
 ObjectMapper mapper = ObjectMapper.getDefaultInstance();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(javaType.getClass());
 JsonNode node = mapper.readTree("{\"className\":\"JavaType\",\"value\":\"" + javaType.getClass().getName() + "\"}");
 JavaType deserializedObject = deserializer.deserialize(mapper.treeToValues(node, JavaType.class)[0], mapper.getContext());
 assertEquals(javaType, deserializedObject);
 }

 @Test
 public void testCurrencyDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
 JsonNode node = mapper.readTree("{\"className\":\"Currency\",\"value\":\"EUR\"}");
 Currency deserializedObject = deserializer.deserialize(mapper.treeToValues(node, Currency.class)[0], mapper.getContext());
 assertEquals(Currency.getInstance("EUR"), deserializedObject);
 }

 @Test
 public void testPatternDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
 JsonNode node = mapper.readTree("{\"className\":\"Pattern\",\"value\":\"\\w+\\.\\w+\"}");
 Pattern deserializedObject = deserializer.deserialize(mapper.treeToValues(node, Pattern.class)[0], mapper.getContext());
 assertEquals(Pattern.compile("\\w+\\.\\w+"), deserializedObject);
 }

 @Test
 public void testLocaleDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
 JsonNode node = mapper.readTree("{\"className\":\"Locale\",\"value\":\"en_US\"}");
 Locale deserializedObject = deserializer.deserialize(mapper.treeToValues(node, Locale.class)[0], mapper.getContext());
 assertEquals(Locale.US, deserializedObject);
 }

 @Test
 public void testCharsetDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
 JsonNode node = mapper.readTree("{\"className\":\"Charset\",\"value\":\"UTF-8\"}");
 Charset deserializedObject = deserializer.deserialize(mapper.treeToValues(node, Charset.class)[0], mapper.getContext());
 assertEquals(Charset.forName("UTF-8"), deserializedObject);
 }

 @Test
 public void testTimeZoneDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
 JsonNode node = mapper.readTree("{\"className\":\"TimeZone\",\"value\":\"America/New_York\"}");
 TimeZone deserializedObject = deserializer.deserialize(mapper.treeToValues(node, TimeZone.class)[0], mapper.getContext());
 assertEquals(TimeZone.getTimeZone("America/New_York"), deserializedObject);
 }

 @Test
 public void testInetAddressDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
 JsonNode node = mapper.readTree("{\"className\":\"InetAddress\",\"value\":\"192.168.1.1\"}");
 InetAddress deserializedObject = deserializer.deserialize(mapper.treeToValues(node, InetAddress.class)[0], mapper.getContext());
 assertEquals(InetAddress.getByName("192.168.1.1"), deserializedObject);
 }

 @Test
 public void testInetSocketAddressDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
 JsonNode node = mapper.readTree("{\"className\":\"InetSocketAddress\",\"value\":\"[255.255.255.255]:80\"}");
 InetSocketAddress deserializedObject = deserializer.deserialize(mapper.treeToValues(node, InetSocketAddress.class)[0], mapper.getContext());
 assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), deserializedObject);
 }

 @Test
 public void testEmptyStringDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
 JsonNode node = mapper.readTree("{\"className\":\"Locale\",\"value\":\"\"}");
 Locale deserializedObject = localeDeserializer.deserialize(mapper.treeToValues(node, Locale.class)[0], mapper.getContext());
 assertEquals(Locale.getDefault(), deserializedObject);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownTypeDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(int.class);
 JsonNode node = mapper.readTree("{\"className\":\"UnknownType\",\"value\":\"test\"}");
 deserializer.deserialize(mapper.treeToValues(node, int.class)[0], mapper.getContext());
 }
}