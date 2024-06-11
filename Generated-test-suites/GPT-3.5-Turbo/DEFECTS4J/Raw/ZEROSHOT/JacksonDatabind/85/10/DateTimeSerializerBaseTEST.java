package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        // Setup
        Class<T> type = DateTimeSerializerBase.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Execute
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBase<TestObject>(type, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            protected long _timestamp(T value) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // TODO Auto-generated method stub
                
            }
        };
        
        // Verify
        assertNotNull(serializer);
        assertEquals(type, serializer.handledType());
        assertEquals(useTimestamp, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Setup
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
			@Override
			public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected long _timestamp(TestObject value) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {
				// TODO Auto-generated method stub
			}
		};
        SerializerProvider serializers = null;
        BeanProperty property = null;

        // Execute
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Verify
        assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        // Setup
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
			@Override
			public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected long _timestamp(TestObject value) {
				return 0;
			}

			@Override
			public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
        TestObject value = null;

        // Execute
        boolean result = serializer.isEmpty(value);

        // Verify
        assertTrue(result);
    }

    @Test
    public void testGetSchema() {
        // Setup
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
			@Override
			public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected long _timestamp(TestObject value) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Execute
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Verify
        assertNotNull(result);
        assertEquals("string", result.get("type").asText());
        assertTrue(result.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Setup
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
			@Override
			public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(TestObject value) {
				return 0;
			}

			@Override
			public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			}
		};
		JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        // Execute
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Verify
        // No exception thrown
    }

    @Test
    public void testSerialize() throws IOException {
        // Setup
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
			@Override
			public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(TestObject value) {
				return 0;
			}

			@Override
			public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeString(value.dateString());
			}
		};
        TestObject value = new TestObject("2019-01-01");

        // Setup mock JsonGenerator
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        // Execute
        serializer.serialize(value, gen, serializers);

        // Verify
        verify(gen).writeString("2019-01-01");
    }

    private static class TestObject {
        private String date;

        public TestObject(String date) {
            this.date = date;
        }

        public String dateString() {
            return date;
        }
    }
}