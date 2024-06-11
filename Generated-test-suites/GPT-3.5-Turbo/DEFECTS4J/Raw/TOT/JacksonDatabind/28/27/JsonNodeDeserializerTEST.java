import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeDeserializerTest {

	@Test
	public void testGetNullValue() {
		JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
		JsonNode nullValue = jsonNodeDeserializer.getNullValue(null);
		assertEquals(null, nullValue);
	}

	@Test
	public void testDeserialize() throws JsonParseException, IOException {
		String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
		JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode expectedNode = objectMapper.readTree(json);
		JsonNode actualNode = jsonNodeDeserializer.deserialize(objectMapper.getFactory().createParser(json), null);
		assertEquals(expectedNode, actualNode);
	}

	@Test
	public void testGetDeserializer() {
		JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
		assertEquals(JsonNodeDeserializer.ObjectDeserializer.getInstance(),
				jsonNodeDeserializer.getDeserializer(ObjectNode.class));
		assertEquals(JsonNodeDeserializer.ArrayDeserializer.getInstance(),
				jsonNodeDeserializer.getDeserializer(ArrayNode.class));
		assertEquals(jsonNodeDeserializer.instance,
				jsonNodeDeserializer.getDeserializer(JsonNode.class));
	}

	@Test
	public void testObjectDeserializer() throws JsonParseException, IOException {
		String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
		JsonNodeDeserializer.ObjectDeserializer objectDeserializer = new JsonNodeDeserializer.ObjectDeserializer();
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode expectedNode = (ObjectNode) objectMapper.readTree(json);
		ObjectNode actualNode = objectDeserializer.deserialize(objectMapper.getFactory().createParser(json), null);
		assertEquals(expectedNode, actualNode);
	}

	@Test
	public void testArrayDeserializer() throws JsonParseException, IOException {
		String json = "[1, 2, 3, 4, 5]";
		JsonNodeDeserializer.ArrayDeserializer arrayDeserializer = new JsonNodeDeserializer.ArrayDeserializer();
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode expectedNode = (ArrayNode) objectMapper.readTree(json);
		ArrayNode actualNode = arrayDeserializer.deserialize(objectMapper.getFactory().createParser(json), null);
		assertEquals(expectedNode, actualNode);
	}

	@Test
	public void testDeserializeNullValue() throws JsonParseException, IOException {
		String json = "null";
		JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode expectedNode = objectMapper.readTree(json);
		JsonNode actualNode = jsonNodeDeserializer.deserialize(objectMapper.getFactory().createParser(json), null);
		assertEquals(expectedNode, actualNode);
	}

}