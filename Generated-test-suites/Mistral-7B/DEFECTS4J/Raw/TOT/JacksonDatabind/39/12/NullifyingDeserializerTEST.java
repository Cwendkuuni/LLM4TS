import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

 @Test
 public void testNullifyingDeserializer_EmptyJson() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser("{}");
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext().getInstance());
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_NullJson() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser jsonParser = mapper.getFactory().createParser(null);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserialize(jsonParser, new DeserializationContext().getInstance());
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_ArrayJson() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "[1, 2, 3]";
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext().getInstance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_ObjectJson() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\": \"John\", \"age\": 30}";
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext().getInstance(), null);
 assertNull(result);
 }

 @Test
 public void testNullifyingDeserializer_FieldNameJson() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\": null}";
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext().getInstance(), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testNullifyingDeserializer_InvalidJson() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "Invalid JSON";
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 StdDeserializer<Object> deserializer = new NullifyingDeserializer();
 Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext().getInstance(), null);
 // The deserializer should throw an IOException for invalid JSON, but it doesn't,
 // so we'll test for that exception here instead.
 }
}