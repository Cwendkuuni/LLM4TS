import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StdKeySerializerTest {

 private static final Object MAP = new java.util.HashMap<>();
 static {
 MAP.put("key1", "value1");
 MAP.put("key2", new Date());
 }

 @Test
 public void testSerializeObject() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Object value = new Date();
 serializer.serialize(value, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 assertTrue(jsonGenerator.toString().contains("\"date\":"));
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializer.serialize(new Date(), jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 assertTrue(jsonGenerator.toString().contains("\"date\":"));
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Object value = "testString";
 serializer.serialize(value, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 assertTrue(jsonGenerator.toString().contains("\"testString\":"));
 }

 @Test
 public void testSerializeMap() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializer.serialize(MAP, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String expectedJson = "{\"key1\":\"value1\",\"key2\":\"Fri Jan 22 14:23:54 EST 2021\"}";
 assertTrue(jsonGenerator.toString().equals(expectedJson));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 serializer.serialize(null, jsonGenerator, new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonStringOrDate() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Object value = new Object();
 serializer.serialize(value, jsonGenerator, new SerializerProvider());
 }
}