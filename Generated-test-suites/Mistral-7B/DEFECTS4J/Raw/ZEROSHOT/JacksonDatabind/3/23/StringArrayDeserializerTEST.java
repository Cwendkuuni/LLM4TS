import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @After
 public void teardown() throws IOException {
 objectMapper.close();
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = objectMapper.readValue("[]", String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 String[] result = objectMapper.readValue("[\"hello\"]", String[].class);
 assertArrayEquals(new String[]{"hello"}, result);
 }

 @Test
 public void testDeserializeMultipleElementsArray() throws IOException {
 String[] result = objectMapper.readValue("[\"hello\",\"world\"]", String[].class);
 assertArrayEquals(new String[]{"hello", "world"}, result);
 }

 @Test
 public void testDeserializeEmptyStringAsNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 String[] result = mapper.readValue("[]", String[].class);
 assertArrayEquals(new String[0], result);

 mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 result = mapper.readValue("[]", String[].class);
 assertArrayEquals(new String[]{}, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws IOException {
 JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getText().toUpperCase();
 }
 };
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] result = objectMapper.reader(String[].class).withValueDeserializer(deserializer).readValue("[\"test\",\"foo\"]");
 assertArrayEquals(new String[]{"TEST", "FOO"}, result);
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 ObjectReader reader = objectMapper.readerForType(String[].class);
 reader = reader.withValueDeserializer(new StringArrayDeserializer());
 TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructTypeDeserializer(String[].class);
 String[] result = reader.readValue(new ArrayNode(new TextNode("[")), null, typeDeserializer);
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\": \"John\", \"age\": 30, \"interests\": [1, 2] }";
 mapper.readValue(json, String[].class);
 }

 @Test
 public void testCreateContextualDeserializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
 StringArrayDeserializer deserializer1 = (StringArrayDeserializer) mapper.findContextualValueDeserializer(String[].class, null);
 assertEquals(null, deserializer1._elementDeserializer);
 StringArrayDeserializer deserializer2 = new StringArrayDeserializer(stringDeserializer);
 assertEquals(stringDeserializer, deserializer2._elementDeserializer);
 }
}