package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

	@Test
	public void testAddAll() {
		Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
		NumberSerializers.addAll(allDeserializers);
		assertEquals(10, allDeserializers.size());
	}

	@Test
	public void testIntegerSerializer() throws IOException {
		JsonGenerator gen = null;
		SerializerProvider provider = null;
		IntegerSerializer serializer = new IntegerSerializer(Integer.class);
		Integer value = 10;

		gen = new NullJsonGenerator();
		serializer.serialize(value, gen, provider);
		assertEquals("10", gen.writeValueAsString(value));
	}

	@Test
	public void testIntLikeSerializer() throws IOException {
		JsonGenerator gen = null;
		SerializerProvider provider = null;
		IntLikeSerializer serializer = IntLikeSerializer.instance;
		Number value = 20;

		gen = new NullJsonGenerator();
		serializer.serialize(value, gen, provider);
		assertEquals("20", gen.writeValueAsString(value));
	}

	@Test
	public void testShortSerializer() throws IOException {
		JsonGenerator gen = null;
		SerializerProvider provider = null;
		ShortSerializer serializer = new ShortSerializer();
		Short value = 30;

		gen = new NullJsonGenerator();
		serializer.serialize(value, gen, provider);
		assertEquals("30", gen.writeValueAsString(value));
	}

	@Test
	public void testLongSerializer() throws IOException {
		JsonGenerator gen = null;
		SerializerProvider provider = null;
		LongSerializer serializer = new LongSerializer(Long.class);
		Long value = 40L;

		gen = new NullJsonGenerator();
		serializer.serialize(value, gen, provider);
		assertEquals("40", gen.writeValueAsString(value));
	}

	@Test
	public void testFloatSerializer() throws IOException {
		JsonGenerator gen = null;
		SerializerProvider provider = null;
		FloatSerializer serializer = FloatSerializer.instance;
		Float value = 50f;

		gen = new NullJsonGenerator();
		serializer.serialize(value, gen, provider);
		assertEquals("50.0", gen.writeValueAsString(value));
	}

	@Test
	public void testDoubleSerializer() throws IOException {
		JsonGenerator gen = null;
		SerializerProvider provider = null;
		DoubleSerializer serializer = new DoubleSerializer(Double.class);
		Double value = 60.0;

		gen = new NullJsonGenerator();
		serializer.serialize(value, gen, provider);
		assertEquals("60.0", gen.writeValueAsString(value));
	}

	private class NullJsonGenerator extends JsonGenerator {
	    @Override
	    public void writeStartArray() throws IOException {}
	    @Override
	    public void writeEndArray() throws IOException {}
	    @Override
	    public void writeStartObject() throws IOException {}
	    @Override
	    public void writeEndObject() throws IOException {}
	    @Override
	    public void writeFieldName(String name) throws IOException {}
	    @Override
	    public void writeString(String text) throws IOException {}
	    @Override
	    public void writeNumber(int v) throws IOException {}
	    @Override
	    public void writeNumber(long v) throws IOException {}
	    @Override
	    public void writeNumber(BigInteger v) throws IOException {}
	    @Override
	    public void writeNumber(double v) throws IOException {}
	    @Override
	    public void writeNumber(float v) throws IOException {}
	    @Override
	    public void writeNumber(BigDecimal v) throws IOException {}
	    @Override
	    public void writeBoolean(boolean state) throws IOException {}
	    @Override
	    public void writeNull() throws IOException {}
	    @Override
	    public void flush() throws IOException {}
	    @Override
	    public void close() throws IOException {}
	}
}