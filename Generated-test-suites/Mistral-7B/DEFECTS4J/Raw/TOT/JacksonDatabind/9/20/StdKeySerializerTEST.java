import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 @Test
 public void testDateSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();
 StdKeySerializer serializer = new StdKeySerializer();
 String expected = "\"Date\":\"" + date.toISO8601String() + "\"";
 JsonNode actual = mapper.writerWithDefaultPrettyPrinter().withSerializer(new StdKeySerializer())
 .writeValueAsTree(date);
 JsonNode jsonNode = actual.path("StdKeySerializer");
 assertEquals(expected, jsonNode.asText());
 }

 @Test
 public void testStringSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String value = "TestValue";
 StdKeySerializer serializer = new StdKeySerializer();
 String expected = "\"TestValue\"";
 JsonNode actual = mapper.writerWithDefaultPrettyPrinter().withSerializer(new StdKeySerializer())
 .writeValueAsTree(value);
 JsonNode jsonNode = actual.path("StdKeySerializer");
 assertEquals(expected, jsonNode.asText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullValueSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 StdKeySerializer serializer = new StdKeySerializer();
 JsonProcessingException exception = assertThrows(JsonProcessingException.class, () ->
 mapper.writerWithDefaultPrettyPrinter().withSerializer(new StdKeySerializer())
 .writeValueAsString(null));
 assertEquals("Null keys not allowed in Object map", exception.getMessage());
 }

 @Test
 public void testEmptyStringSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String value = "";
 StdKeySerializer serializer = new StdKeySerializer();
 String expected = "\"\"";
 JsonNode actual = mapper.writerWithDefaultPrettyPrinter().withSerializer(new StdKeySerializer())
 .writeValueAsTree(value);
 JsonNode jsonNode = actual.path("StdKeySerializer");
 assertEquals(expected, jsonNode.asText());
 }

 @Test
 public void testInvalidClassSerialization() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 Integer value = new Integer(1);
 StdKeySerializer serializer = new StdKeySerializer();
 assertThrows(JsonMappingException.class, () -> mapper.writerWithDefaultPrettyPrinter().withSerializer(new StdKeySerializer())
 .writeValueAsTree(value));
 }
}