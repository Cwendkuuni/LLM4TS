package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {
	private StdKeyDeserializer stdKeyDeserializer;

	@Before
	public void setUp() {
		stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
	}

	@Test
	public void testDeserializeKey() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = "{\"key\": 10}";
		DeserializationContext ctxt = new DeserializationContext(mapper, null, null);
		String key = "10";

		assertEquals(10, stdKeyDeserializer.deserializeKey(json, ctxt));
		assertEquals(null, stdKeyDeserializer.deserializeKey(null, ctxt));
		assertEquals(key, stdKeyDeserializer.deserializeKey(key, ctxt));
	}

	@Test(expected = JsonMappingException.class)
	public void testDeserializeKeyWithException() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = "{\"key\": \"invalidKey\"}";
		DeserializationContext ctxt = new DeserializationContext(mapper, null, null);
		String invalidKey = "invalidKey";

		stdKeyDeserializer.deserializeKey(json, ctxt);
	}

	@Test
	public void testGetKeyClass() {
		assertEquals(Integer.class, stdKeyDeserializer.getKeyClass());
	}
	
	@Test
	public void test_parse() throws Exception {
		DeserializationContext ctxt = new DeserializationContext(mapper, null, null);
		String key = "10";
		
		assertEquals(10, stdKeyDeserializer._parse(key, ctxt));

		key = "not a valid representation";
		try {
			stdKeyDeserializer._parse(key, ctxt);
			fail("Expected exception: JsonMappingException");
		} catch (JsonMappingException e) {
			assertEquals("Invalid representation for key class Integer: not a valid representation", e.getMessage());
		}
	}

	@Test
	public void test_parseInt() throws Exception {
		DeserializationContext ctxt = new DeserializationContext(mapper, null, null);
		String key = "10";

		assertEquals(10, stdKeyDeserializer._parseInt(key));

		key = "not a valid representation";
		try {
			stdKeyDeserializer._parseInt(key);
			fail("Expected exception: NumberFormatException");
		} catch (NumberFormatException e) {
			assertEquals("For input string: \"not a valid representation\"", e.getMessage());
		}
	}

	@Test
	public void test_parseLong() throws Exception {
		DeserializationContext ctxt = new DeserializationContext(mapper, null, null);
		String key = "10";

		assertEquals(10L, stdKeyDeserializer._parseLong(key));

		key = "not a valid representation";
		try {
			stdKeyDeserializer._parseLong(key);
			fail("Expected exception: NumberFormatException");
		} catch (NumberFormatException e) {
			assertEquals("For input string: \"not a valid representation\"", e.getMessage());
		}
	}

	@Test
	public void test_parseDouble() throws Exception {
		DeserializationContext ctxt = new DeserializationContext(mapper, null, null);
		String key = "10.5";

		assertEquals(10.5, stdKeyDeserializer._parseDouble(key), 0.001);

		key = "not a valid representation";
		try {
			stdKeyDeserializer._parseDouble(key);
			fail("Expected exception: NumberFormatException");
		} catch (NumberFormatException e) {
			assertEquals("For input string: \"not a valid representation\"", e.getMessage());
		}
	}
}