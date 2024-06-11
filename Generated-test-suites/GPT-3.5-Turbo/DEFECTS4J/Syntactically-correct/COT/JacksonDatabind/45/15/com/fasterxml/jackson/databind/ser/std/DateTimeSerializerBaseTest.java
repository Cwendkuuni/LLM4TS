package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;   
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

	@Test
	public void testWithFormat() {
		// Test method signature: public abstract DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat)
		DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US)) {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Do nothing
			}
			@Override
			protected long _timestamp(String value) {
				// Do nothing
				return 0;
			}
		};
		DateTimeSerializerBase<String> newSerializer = serializer.withFormat(false, new SimpleDateFormat("MM/dd/yyyy", Locale.US));
		assertNotNull(newSerializer);
		assertEquals(false, newSerializer._useTimestamp);
		assertNotNull(newSerializer._customFormat);
		assertEquals("MM/dd/yyyy", ((SimpleDateFormat)newSerializer._customFormat).toPattern());
	}

	@Test
	public void testCreateContextual() {
		// Test method signature: public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException
		BeanProperty property = null;
		SerializerProvider serializers = new SerializerProvider();
		DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US)) {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Do nothing
			}
			@Override
			protected long _timestamp(String value) {
				// Do nothing
				return 0;
			}
		};

		// Case 1: format is null
		JsonFormat.Value format1 = new JsonFormat.Value();
		format1 = format1.withShape(JsonFormat.Shape.STRING);
		serializers.getAnnotationIntrospector().findFormat(property.getMember());
		JsonSerializer<?> newSerializer1 = serializer.createContextual(serializers, property);
		assertNotNull(newSerializer1);
		assertEquals(false, ((DateTimeSerializerBase<?>)newSerializer1)._useTimestamp);
		assertNotNull(((DateTimeSerializerBase<?>)newSerializer1)._customFormat);
		assertEquals(new SimpleDateFormat("yyyy-MM-dd", Locale.US).toPattern(), ((SimpleDateFormat)((DateTimeSerializerBase<?>)newSerializer1)._customFormat).toPattern());

		// Case 2: format shape is numeric
		JsonFormat.Value format2 = new JsonFormat.Value();
		format2 = format2.withShape(JsonFormat.Shape.NUMBER);
		JsonSerializer<?> newSerializer2 = serializer.createContextual(serializers, property);
		assertNotNull(newSerializer2);
		assertEquals(true, ((DateTimeSerializerBase<?>)newSerializer2)._useTimestamp);
		assertNull(((DateTimeSerializerBase<?>)newSerializer2)._customFormat);

		// Case 3: format shape is string
		JsonFormat.Value format3 = new JsonFormat.Value();
		format3 = format3.withShape(JsonFormat.Shape.STRING);
		format3 = format3.withPattern("MM/dd/yyyy");
		format3 = format3.withLocale(Locale.US);
		format3 = format3.withTimeZone(TimeZone.getTimeZone("PST"));
		JsonSerializer<?> newSerializer3 = serializer.createContextual(serializers, property);
		assertNotNull(newSerializer3);
		assertEquals(false, ((DateTimeSerializerBase<?>)newSerializer3)._useTimestamp);
		assertNotNull(((DateTimeSerializerBase<?>)newSerializer3)._customFormat);
		assertEquals("MM/dd/yyyy", ((SimpleDateFormat)((DateTimeSerializerBase<?>)newSerializer3)._customFormat).toPattern());
		assertEquals(TimeZone.getTimeZone("PST"), ((SimpleDateFormat)((DateTimeSerializerBase<?>)newSerializer3)._customFormat).getTimeZone());
		assertEquals(Locale.US, ((SimpleDateFormat)((DateTimeSerializerBase<?>)newSerializer3)._customFormat).getLocale());
	}

	@Test
	public void testIsEmpty() {
		// Test method signature: public boolean isEmpty(SerializerProvider serializers, T value)
		SerializerProvider serializers = new SerializerProvider();
		DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US)) {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Do nothing
			}
			@Override
			protected long _timestamp(String value) {
				return 0;
			}
		};

		// Case 1: value is null
		assertTrue(serializer.isEmpty(serializers, null));

		// Case 2: timestamp is 0
		assertTrue(serializer.isEmpty(serializers, "2021-01-01"));

		// Case 3: timestamp is non-zero
		assertFalse(serializer.isEmpty(serializers, "2021-05-21"));
	}

	@Test
	public void testGetSchema() {
		// Test method signature: public JsonNode getSchema(SerializerProvider serializers, Type typeHint)
		SerializerProvider serializers = new SerializerProvider();
		DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US)) {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Do nothing
			}
			@Override
			protected long _timestamp(String value) {
				return 0;
			}
		};

		JsonNode schemaAsNumber = serializer.getSchema(serializers, String.class);
		assertEquals("number", schemaAsNumber.get("type").asText());
		assertTrue(schemaAsNumber.get("required").asBoolean());

		JsonNode schemaAsString = serializer.getSchema(serializers, String.class);
		assertEquals("string", schemaAsString.get("type").asText());
		assertTrue(schemaAsString.get("required").asBoolean());
	}

	@Test
	public void testAcceptJsonFormatVisitor() {
		// Test method signature: public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
		JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
		JavaType typeHint = new JavaType();
		DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US)) {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Do nothing
			}
			@Override
			protected long _timestamp(String value) {
				return 0;
			}
		};

		serializer.acceptJsonFormatVisitor(visitor, typeHint);

		// Verify that the visitor is called correctly
		assertTrue(visitor.isVisitIntFormatCalled());
		assertFalse(visitor.isVisitStringFormatCalled());
	}

    @Test
    public void testSerialize() throws Exception {
        // Test method signature: public abstract void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
      
        // Test case: value is not null
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US)) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                assertEquals("2021-05-21", value);
            }
            @Override
            protected long _timestamp(String value) {
                return 0;
            }
        };
        serializer.serialize("2021-05-21", gen, serializers);

        // Test case: value is null
        serializer.serialize(null, gen, serializers);
    }
}