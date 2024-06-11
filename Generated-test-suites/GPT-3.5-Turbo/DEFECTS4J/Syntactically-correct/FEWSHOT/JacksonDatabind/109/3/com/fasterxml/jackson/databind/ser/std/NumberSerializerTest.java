package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializerTest {
	private JsonSerializer<?> numberSerializer;
	
	@Before
	public void setup() {
		numberSerializer = new NumberSerializer(Number.class);
	}
	
	@Test
	public void testCreateContextual() throws JsonMappingException {
		SerializerProvider provider = null;
		BeanProperty property = null;
		JsonSerializer<?> serializer = numberSerializer.createContextual(provider, property);
		assertSame(numberSerializer, serializer);
	}
	
	@Test
	public void testSerialize() throws IOException {
		Number value = null;
		JsonGenerator gen = null;
		SerializerProvider provider = null; 
		numberSerializer.serialize(value, gen, provider);
	}
	
	@Test
	public void testGetSchema() {
		SerializerProvider provider = null;
		Type typeHint = null;
		JsonNode schema = numberSerializer.getSchema(provider, typeHint);
		assertNotNull(schema);
		assertTrue(schema.isObject());
		assertEquals("integer", schema.get("type").asText());
		assertTrue(schema.get("required").asBoolean());
	}
	
	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		JsonFormatVisitorWrapper visitor = null;
		JavaType typeHint = null;
		numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
	}
}