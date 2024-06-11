import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StringArrayDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @After
 public void teardown() throws IOException {
 mapper.close();
 }

 @Test
 public void testDeserializeStandardArray() throws IOException {
 StringJsonNode jsonNode = mapper.readTree("[\"Hello\", \"World\"]");
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode);
 assertArrayEquals(new String[]{"Hello", "World"}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
 JsonDeserializer<String> stringDeserializer = mapper.getFactory().createValueDeserializer(String.class, new StdDeserializer.StubDeserializer<>());
 StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
 StringJsonNode jsonNode = mapper.readTree("[\"Hello\", \"World\"]");
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode, deserializer);
 assertArrayEquals(new String[]{"Hello", "World"}, result);
 }

 @Test
 public void testDeserializeArrayWithNullElement() throws IOException {
 JsonNode jsonNode = mapper.readTree("[\"Hello\", null]");
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode);
 assertArrayEquals(new String[]{"Hello", null}, result);
 }

 @Test
 public void testDeserializeArrayWithSingleValue() throws IOException {
 JsonNode jsonNode = mapper.readTree("\"Hello\"");
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode);
 assertArrayEquals(new String[]{"Hello"}, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonNode jsonNode = mapper.readTree("{ \"Hello\": \"World\" }");
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode);
 fail("Expected JsonMappingException, but received " + result.getClass().getName());
 }

 @Test
 public void testDeserializeArrayWithEmptyString() throws IOException {
 JsonNode jsonNode = mapper.readTree("[\"\"]");
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode);
 assertArrayEquals(new String[]{""}, result);
 }

 @Test
 public void testDeserializeContextualArrayWithCustomDeserializer() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "test");
 node.put("values", "[\"Value1\", \"Value2\"]");
 String[] expected = new String[]{"Value1", "Value2"};
 StringJsonNode jsonNode = mapper.readTree(node.toString());
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode.path("values"));
 assertArrayEquals(expected, result);

 ObjectReader reader = mapper.readerForObject(TestContextual.class);
 TestContextual contextual = reader.readValue(node);
 StringArrayDeserializer deserializer = contextual.deserializer();
 result = mapper.readerForArrayOf(String.class).readValue(jsonNode.path("values"), deserializer);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeserializeContextualArrayWithDefaultDeserializer() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "test");
 node.put("values", "[\"Value1\", \"Value2\"]");
 String[] expected = new String[]{"Value1", "Value2"};
 StringJsonNode jsonNode = mapper.readTree(node.toString());
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode.path("values"));
 assertArrayEquals(expected, result);

 ObjectReader reader = mapper.readerForObject(TestContextualWithDefaultDeserializer.class);
 TestContextualWithDefaultDeserializer contextual = reader.readValue(node);
 String[] deserializerResult = contextual.deserializer().deserialize(mapper.parserForObject(new StringReader("[\"Value1\", \"Value2\"]")), mapper.getContextualDeserializationContext());
 assertArrayEquals(expected, deserializerResult);
 }

 @Test
 public void testDeserializeContextualArrayWithNullDeserializer() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("name", "test");
 node.put("values", "[null]");
 String[] expected = new String[]{null};
 StringJsonNode jsonNode = mapper.readTree(node.toString());
 String[] result = mapper.readerForArrayOf(String.class).readValue(jsonNode.path("values"));
 assertArrayEquals(expected, result);

 ObjectReader reader = mapper.readerForObject(TestContextualWithNullDeserializer.class);
 TestContextualWithNullDeserializer contextual = reader.readValue(node);
 String[] deserializerResult = contextual.deserializer().deserialize(mapper.parserForObject(new StringReader("[null]")), mapper.getContextualDeserializationContext());
 assertArrayEquals(expected, deserializerResult);
 }
}

class TestContextual {
 String[] values;
 StringArrayDeserializer deserializer;

 @JsonProperty("values")
 public StringArrayDeserializer deserializer() {
 return this.deserializer != null ? this.deserializer : new StringArrayDeserializer();
 }

 @JsonProperty("values")
 public void setDeserializer(StringArrayDeserializer deserializer) {
 this.deserializer = deserializer;
 }
}

class TestContextualWithDefaultDeserializer extends TestContextual {
 @JsonProperty("values")
 public JsonDeserializer<?> deserializer() {
 return mapper.getFactory().findValueDeserializer(String.class, null);
 }
}

class TestContextualWithNullDeserializer extends TestContextual {
 @JsonProperty("values")
 public JsonDeserializer<?> deserializer() {
 return null;
 }
}