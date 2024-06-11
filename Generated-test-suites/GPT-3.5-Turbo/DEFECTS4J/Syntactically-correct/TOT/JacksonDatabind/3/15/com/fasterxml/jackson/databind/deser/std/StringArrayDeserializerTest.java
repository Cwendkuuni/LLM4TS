import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

	@Test
	public void testDeserialize() throws JsonParseException, JsonMappingException, IOException {
		StringArrayDeserializer deserializer = new StringArrayDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
		
		String[] expectedArray = new String[] { "apple", "banana", "cherry" };
		String json = "[\"apple\", \"banana\", \"cherry\"]";
		JsonNode node = mapper.readTree(json);
		JsonParser parser = mapper.treeAsTokens(node);
		
		String[] actualArray = deserializer.deserialize(parser, mapper.getDeserializationContext());

		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void testDeserialize_WithNullValues() throws JsonParseException, JsonMappingException, IOException {
		StringArrayDeserializer deserializer = new StringArrayDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
		
		String[] expectedArray = new String[] { null, null, null };
		String json = "[null, null, null]";
		JsonNode node = mapper.readTree(json);
		JsonParser parser = mapper.treeAsTokens(node);
		
		String[] actualArray = deserializer.deserialize(parser, mapper.getDeserializationContext());

		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void testDeserialize_WithEmptyArray() throws JsonParseException, JsonMappingException, IOException {
		StringArrayDeserializer deserializer = new StringArrayDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
		
		String[] expectedArray = new String[] {};
		String json = "[]";
		JsonNode node = mapper.readTree(json);
		JsonParser parser = mapper.treeAsTokens(node);
		
		String[] actualArray = deserializer.deserialize(parser, mapper.getDeserializationContext());

		assertArrayEquals(expectedArray, actualArray);
	}

	@Test(expected = JsonParseException.class)
	public void testDeserialize_WithInvalidJson() throws JsonParseException, JsonMappingException, IOException {
		StringArrayDeserializer deserializer = new StringArrayDeserializer();
		ObjectMapper mapper = new ObjectMapper();
		ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
		
		String json = "[\"apple\", \"banana\",]";
		JsonNode node = mapper.readTree(json);
		JsonParser parser = mapper.treeAsTokens(node);
		
		deserializer.deserialize(parser, mapper.getDeserializationContext());
	}

}