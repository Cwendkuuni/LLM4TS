package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

public class JsonNodeDeserializerTest {

	@Test
	public void testGetNullValue() {
		JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
		DeserializationContext ctxt = null;
		JsonNode nullValue = deserializer.getNullValue(ctxt);
		
		assertEquals(NullNode.getInstance(), nullValue);
	}

	@Test
	public void testGetNullValueDeprecated() {
		JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
		JsonNode nullValue = deserializer.getNullValue();
		
		assertEquals(NullNode.getInstance(), nullValue);
	}

	@Test
	public void testDeserialize() throws IOException {
		JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
		DeserializationContext ctxt = null;
		JsonParser p = null;
		JsonNode jsonNode = deserializer.deserialize(p, ctxt);
		
		assertNotNull(jsonNode);
	}

	@Test
	public void testGetObjectDeserializer() throws IOException {
		JsonDeserializer<? extends JsonNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
		assertNotNull(objectDeserializer);
	}

	@Test
	public void testGetArrayDeserializer() throws IOException {
		JsonDeserializer<? extends JsonNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
		assertNotNull(arrayDeserializer);
	}
}