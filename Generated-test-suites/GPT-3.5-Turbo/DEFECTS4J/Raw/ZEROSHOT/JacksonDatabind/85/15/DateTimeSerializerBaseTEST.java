package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
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
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

	@Test
	public void testWithFormat() {
		Class<?> type = DateTimeSerializerBase.class;
		Boolean timestamp = true;
		DateFormat customFormat = new SimpleDateFormat();
		DateTimeSerializerBase<?> dateTimeSerializerBase = new DateTimeSerializerBase<String>(type, timestamp, customFormat) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {

			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		DateTimeSerializerBase<?> result = dateTimeSerializerBase.withFormat(timestamp, customFormat);
		assertEquals(null, result);
	}

	@Test
	public void testCreateContextual() throws JsonMappingException {
		Class<?> type = DateTimeSerializerBase.class;
		SerializerProvider serializers = null;
		BeanProperty property = null;
		DateTimeSerializerBase<?> dateTimeSerializerBase = new DateTimeSerializerBase<String>(type, true, new SimpleDateFormat()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {

			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializers, property);
		assertEquals(dateTimeSerializerBase, result);
	}

	@Test
	public void testIsEmpty() {
		Class<?> type = DateTimeSerializerBase.class;
		DateTimeSerializerBase<?> dateTimeSerializerBase = new DateTimeSerializerBase<String>(type, true, new SimpleDateFormat()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {

			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		assertEquals(true, dateTimeSerializerBase.isEmpty(null));
	}

	@Test
	public void testGetSchema() {
		Class<?> type = DateTimeSerializerBase.class;
		SerializerProvider serializers = null;
		Type typeHint = null;
		DateTimeSerializerBase<?> dateTimeSerializerBase = new DateTimeSerializerBase<String>(type, true, new SimpleDateFormat()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {

			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		JsonNode result = dateTimeSerializerBase.getSchema(serializers, typeHint);
		assertNotNull(result);
	}

	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		Class<?> type = DateTimeSerializerBase.class;
		JavaType typeHint = null;
		SerializerProvider serializers = null;
		DateTimeSerializerBase<?> dateTimeSerializerBase = new DateTimeSerializerBase<String>(type, true, new SimpleDateFormat()) {
			
			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {
				
			}
			
			@Override
			protected long _timestamp(String value) {
				return 0;
			}
			
		};
		
		JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapperBase(serializers) {
			
			@Override
			public SerializerProvider getProvider() {
				return null;
			}
			
			@Override
			public void setProvider(SerializerProvider p) {	}
			
			@Override
			public Object getVisitor() {
				return null;
			}
			
			@Override
			public void expectAnyFormat(JavaType type) throws JsonMappingException {	}
			
		};
		
		dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
	}

}