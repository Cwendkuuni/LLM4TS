package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
	
	@Test
	public void testWithFormat() {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		DateTimeSerializerBase result = serializer.withFormat(false, new SimpleDateFormat());
		
		assertFalse(result._asTimestamp(null));
	}
	
	@Test
	public void testCreateContextual() throws JsonMappingException {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		JsonSerializer<?> result = serializer.createContextual(null, null);
		
		assertEquals(serializer, result);
	}
	
	@Test
	public void testIsEmpty() {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		assertFalse(serializer.isEmpty(null, null));
	}
	
	@Test
	public void testGetSchema() {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		JsonNode result = serializer.getSchema(null, null);
		
		assertNotNull(result);
	}
	
	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		serializer.acceptJsonFormatVisitor(null, null);
	}
	
	@Test
	public void testSerialize() throws IOException {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		serializer.serialize(null, null, null);
	}
	
	@Test
	public void testSerializeAsString() throws IOException {
		DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
			public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}
			
			public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
			
			public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
			
			protected long _timestamp(T value) { return 0L; }
		};
		
		serializer._serializeAsString(null, null, null);
	}
	
}