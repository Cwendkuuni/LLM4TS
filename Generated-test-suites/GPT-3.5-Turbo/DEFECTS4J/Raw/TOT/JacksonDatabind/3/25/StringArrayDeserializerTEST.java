package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

	@Test
	public void testDeserialize() throws IOException {
		StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[\"Hello\",\"World\"]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[] { "Hello", "World" }, result);
	}

	@Test
	public void testDeserialize_EmptyArray() throws IOException {
		StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[0], result);
	}

	@Test
	public void testDeserialize_WithNulls() throws IOException {
		StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[\"Hello\",null,\"World\"]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[] { "Hello", null, "World" }, result);
	}

	@Test
	public void testDeserialize_NonStringArray() throws IOException {
		StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[1,2,3]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[0], result);
	}

	@Test
	public void testDeserialize_InvalidJson() throws IOException {
		StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[\"Hello\",\"World\"");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		try {
			deserializer.deserialize(jp, ctxt);
			fail("Expected IOException");
		} catch (IOException e) {
			// Expected IOException
		}
	}

	@Test
	public void testDeserialize_CustomDeserializer() throws IOException {
		JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>() {
			public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
				if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
					return jp.getText();
				}
				return "";
			}
		};
		StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[\"Hello\",\"World\"]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[] { "Hello", "World" }, result);
	}

	@Test
	public void testDeserialize_CustomDeserializer_Nulls() throws IOException {
		JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>() {
			public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
				if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
					return jp.getText();
				}
				return null;
			}
		};
		StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[\"Hello\",null,\"World\"]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[] { "Hello", null, "World" }, result);
	}

	@Test
	public void testDeserialize_CustomDeserializer_NonStringArray() throws IOException {
		JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>() {
			public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
				if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
					return jp.getText();
				}
				return "";
			}
		};
		StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[1,2,3]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[0], result);
	}

	@Test
	public void testDeserialize_CustomDeserializerWithNull() throws IOException {
		JsonDeserializer<String> elementDeserializer = new JsonDeserializer<String>() {
			public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
				if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
					return jp.getText();
				}
				return null;
			}
		};
		StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
		ObjectMapper mapper = new ObjectMapper();
		JsonParser jp = mapper.getFactory().createParser("[1,2,3]");
		DeserializationContext ctxt = mapper.getDeserializationContext();
		String[] result = deserializer.deserialize(jp, ctxt);
		assertArrayEquals(new String[0], result);
	}

}