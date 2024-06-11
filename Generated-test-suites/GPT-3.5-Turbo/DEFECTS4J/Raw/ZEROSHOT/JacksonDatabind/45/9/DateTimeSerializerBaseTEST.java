package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

	@Test
	public void testWithFormat() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		DateTimeSerializerBase<String> newSerializer = serializerBase.withFormat(true, new SimpleDateFormat());
		assertTrue(newSerializer instanceof DateTimeSerializerBase);
	}

	@Test
	public void testCreateContextual() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializers = objectMapper.getSerializerProvider();
		BeanProperty property = new BeanProperty.Std(null, null, null);

		JsonFormat.Value format = new JsonFormat.Value();
		format.setShape(JsonFormat.Shape.STRING);

		DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializerBase
				.createContextual(serializers, property);
		assertTrue(newSerializer instanceof DateTimeSerializerBase);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateContextualWithNullProperty() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializers = objectMapper.getSerializerProvider();

		JsonFormat.Value format = new JsonFormat.Value();
		format.setShape(JsonFormat.Shape.STRING);

		DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializerBase
				.createContextual(serializers, null);
		assertTrue(newSerializer instanceof DateTimeSerializerBase);
	}

	@Test
	public void testIsEmpty() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		String value = "test";
		assertTrue(serializerBase.isEmpty(value));
	}

	@Test
	public void testIsEmptyWithSerializerProvider() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializers = objectMapper.getSerializerProvider();

		String value = "test";
		assertTrue(serializerBase.isEmpty(serializers, value));
	}

	@Test
	public void testGetSchema() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializers = objectMapper.getSerializerProvider();
		TypeFactory typeFactory = TypeFactory.defaultInstance();
		JavaType javaType = typeFactory.constructType(String.class);

		JsonNode schema = serializerBase.getSchema(serializers, javaType);
		assertNotNull(schema);
		assertEquals("string", schema.get("type").textValue());
		assertTrue(schema.get("format").booleanValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSchemaWithNullSerializerProvider() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		SerializerProvider serializers = null;
		TypeFactory typeFactory = TypeFactory.defaultInstance();
		JavaType javaType = typeFactory.constructType(String.class);

		JsonNode schema = serializerBase.getSchema(serializers, javaType);
		assertNotNull(schema);
		assertEquals("string", schema.get("type").textValue());
		assertTrue(schema.get("format").booleanValue());
	}

	@Test
	public void testAcceptJsonFormatVisitor() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						// Serialize method implementation
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// Serialize method implementation
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getVisitorWrapper();
		TypeFactory typeFactory = TypeFactory.defaultInstance();
		JavaType javaType = typeFactory.constructType(String.class);

		serializerBase.acceptJsonFormatVisitor(visitor, javaType);
	}

	@Test
	public void testSerialize() throws Exception {
		DateTimeSerializerBase<String> serializerBase = new DateTimeSerializerBase<String>(String.class, true,
				DateFormat.getDateInstance()) {

			@Override
			public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
				return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {

					@Override
					public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
							throws IOException {
						assertTrue(value.equals("test"));
					}

					@Override
					protected long _timestamp(String value) {
						return 0;
					}

				};
			}

			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				assertTrue(value.equals("test"));
			}

			@Override
			protected long _timestamp(String value) {
				return 0;
			}

		};

		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
		SerializerProvider serializers = objectMapper.getSerializerProvider();

		String value = "test";
		serializerBase.serialize(value, gen, serializers);
	}

}