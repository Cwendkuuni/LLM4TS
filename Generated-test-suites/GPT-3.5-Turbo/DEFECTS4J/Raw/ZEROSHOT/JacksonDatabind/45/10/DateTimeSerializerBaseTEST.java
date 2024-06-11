package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

	@Test
	public void testWithFormat() {
		Boolean useTimestamp = true;
		DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		assertTrue(serializer.withFormat(useTimestamp, customFormat) == null);
	}

	@Test
	public void testCreateContextual() {
		SerializerProvider serializers = null;
		BeanProperty property = null;
		JsonFormat.Value format = null;
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		try {
			assertTrue(serializer.createContextual(serializers, property) == serializer);
		} catch (JsonMappingException e) {
			fail();
		}
	}

	@Test
	public void testIsEmpty1() {
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		Date value = null;
		assertTrue(serializer.isEmpty(value) == true);
	}

	@Test
	public void testIsEmpty2() {
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		Date value = new Date();
		assertTrue(serializer.isEmpty(value) == false);
	}

	@Test
	public void testIsEmpty3() {
		SerializerProvider serializers = null;
		Date value = null;
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		assertTrue(serializer.isEmpty(serializers, value) == true);
	}

	@Test
	public void testIsEmpty4() {
		SerializerProvider serializers = null;
		Date value = new Date();
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		assertTrue(serializer.isEmpty(serializers, value) == false);
	}

	@Test
	public void testGetSchema() {
		SerializerProvider serializers = null;
		Type typeHint = null;
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		assertTrue(serializer.getSchema(serializers, typeHint) != null);
	}

	@Test
	public void testAcceptJsonFormatVisitor() {
		JsonFormatVisitorWrapper visitor = null;
		JavaType typeHint = null;
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		try {
			serializer.acceptJsonFormatVisitor(visitor, typeHint);
		} catch (JsonMappingException e) {
			fail();
		}
	}

	@Test
	public void testSerialize() {
		JsonGenerator gen = null;
		SerializerProvider serializers = null;
		DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
			@Override
			public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(Date value) {
				return 0;
			}

			@Override
			public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				
			}
		};
		Date value = null;
		try {
			serializer.serialize(value, gen, serializers);
		} catch (IOException e) {
			fail();
		}
	}

}