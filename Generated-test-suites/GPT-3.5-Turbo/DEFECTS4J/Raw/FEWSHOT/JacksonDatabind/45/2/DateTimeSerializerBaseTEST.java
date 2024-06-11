package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import junit.framework.Assert;

public class DateTimeSerializerBaseTest {
	
	private DateTimeSerializerBaseTestClass serializer;

	@Before
	public void setUp() {
		serializer = new DateTimeSerializerBaseTestClass(DateTimeSerializerBaseTestClass.class, true, null);
	}

	@Test
	public void testWithFormat() {
		DateTimeSerializerBaseTestClass newSerializer = serializer.withFormat(false, new SimpleDateFormat());
		assertEquals(false, newSerializer._useTimestamp);
		assertSame(SimpleDateFormat.class, newSerializer._customFormat.getClass());
	}

	@Test
	public void testCreateContextual() throws JsonMappingException {
		DateTimeSerializerBaseTestClass newSerializer = serializer.createContextual(null, null);
		assertSame(serializer, newSerializer);

		BeanProperty property = mock(BeanProperty.class);
		newSerializer = serializer.createContextual(null, property);
		assertSame(serializer, newSerializer);

		Annotated annotated = mock(Annotated.class);
		property = mock(BeanProperty.class);
		serializer = new DateTimeSerializerBaseTestClass(DateTimeSerializerBase.class, false,
				new SimpleDateFormat());
		newSerializer = serializer.createContextual(null, property);
		assertSame(serializer, newSerializer);
	}

	@Test
	public void testIsEmpty() {
		Object value = new Object();
		Assert.assertTrue(serializer.isEmpty((DateTimeSerializerBaseTestClass) value));
		Assert.assertTrue(serializer.isEmpty(null));
		serializer = new DateTimeSerializerBaseTestClass(DateTimeSerializerBase.class, false, null);
		Assert.assertFalse(serializer.isEmpty((DateTimeSerializerBaseTestClass) value));
	}

	@Test
	public void testGetSchema() {
		JsonNode node = serializer.getSchema(null, null);
		assertEquals("string", node.get("type").asText());
		assertEquals(true, node.get("required").asBoolean());

		serializer = new DateTimeSerializerBaseTestClass(DateTimeSerializerBaseTestClass.class, true, null);
		node = serializer.getSchema(null, null);
		assertEquals("number", node.get("type").asText());
	}

	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
		serializer.acceptJsonFormatVisitor(visitor, null);
	}

	@Test
	public void testSerialize() throws IOException {
		JsonGenerator gen = mock(JsonGenerator.class);
		SerializerProvider provider = mock(SerializerProvider.class);
		serializer.serialize(null, gen, provider);
	}

	@Test
	public void testAsTimestamp() {
		SerializerProvider provider = mock(SerializerProvider.class);
		assertEquals(true, serializer._asTimestamp(provider));

		serializer = new DateTimeSerializerBaseTestClass(DateTimeSerializerBaseTestClass.class, null, null);
		try {
			serializer._asTimestamp(null);
			Assert.fail("IllegalArgumentException should be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Null SerializerProvider passed for com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBaseTestClass",
					e.getMessage());
		}

		serializer = new DateTimeSerializerBaseTestClass(DateTimeSerializerBaseTestClass.class, null, null);
		assertEquals(false, serializer._asTimestamp(null));

	}

	@Test
	public void testAccept()

	{
		JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
		JavaType typeHint = mock(JavaType.class);
		serializer._acceptJsonFormatVisitor(visitor, typeHint, true);
		serializer._acceptJsonFormatVisitor(visitor, typeHint, false);
	}

	private class DateTimeSerializerBaseTestClass extends DateTimeSerializerBase
			implements ContextualSerializer {
		protected DateTimeSerializerBaseTestClass(Class type, Boolean useTimestamp, DateFormat customFormat) {
			super(type, useTimestamp, customFormat);
			
		}

		@Override
		public DateTimeSerializerBaseWithContext createInstance() {
			return null;
		}

		@Override
		public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		}

		@Override
		protected long _timestamp(Object arg0) {
			return 0;
		}

		@Override
		public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper arg0, JavaType arg1) throws JsonMappingException {
		}
	}

	private class DateTimeSerializerBaseWithContext implements ContextualSerializer{

		@Override
		public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
				throws JsonMappingException {
			// TODO Auto-generated method stub
			return null;
		}
	}

}