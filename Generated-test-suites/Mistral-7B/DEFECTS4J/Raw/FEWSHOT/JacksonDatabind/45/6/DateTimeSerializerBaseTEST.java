package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateFormatSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    static final DateTimeSerializerBase<Long> LONG_TIMESTAMP_SERIALIZER = new LongTimestampSerializer(Long.TYPE, true, null);
    static final DateTimeSerializerBase<String> DATE_TIME_STRING_SERIALIZER = new DateTimeSerializerBase<String>(String.class, false, null) {
        @Override
        protected long _timestamp(String value) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(value).getTime();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Test
    public void testLongTimestampSerializer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Long> serializer = LONG_TIMESTAMP_SERIALIZER;
        Long value = 1619459200000L;
        JsonNode node = mapper.writeValueAsTree(value, serializer);
        assertEquals(value, mapper.readValue(node.toString(), Long.class));
        assertEquals("1619459200000", node.asText());
    }

    @Test
    public void testDateFormatSerializer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = DATE_TIME_STRING_SERIALIZER;
        String value = "2023-03-15T12:30:45.678Z";
        JsonNode node = mapper.writeValueAsTree(value, serializer);
        assertEquals(value, mapper.readValue(node.toString(), String.class));
        assertEquals(value, node.asText());

        JsonParser parser = mapper.getFactory().createParser(node.toString());
        Object readValue = parser.readValueAs(String.class);
        assertEquals(value, readValue);

        JsonFormat<String> jsonFormat = new JsonFormat.Value() {
            @Override
            public JsonFormat.Shape shape() {
                return JsonFormat.Shape.STRING;
            }

            @Override
            public TimeZone timeZone() {
                return TimeZone.getDefault();
            }

            @Override
            public Locale locale() {
                return Locale.getDefault();
            }

            @Override
            public String pattern() {
                return "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            }
        };

        JsonNode schemaNode = DATE_TIME_STRING_SERIALIZER.getSchema(new SerializerProvider(), String.class.getType());
        assertEquals("string", schemaNode.asText());
        assertTrue(schemaNode.has("description"));
        assertTrue(schemaNode.has("type"));
        assertEquals("date-time", schemaNode.get("description").asText());
        assertEquals("string", schemaNode.get("type").asText());

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StdDateFormatSchema());
        DATE_TIME_STRING_SERIALIZER._acceptJsonFormatVisitor(visitor, String.class.getType(), false);
        assertEquals("date-time", visitor.getSchemaNode().get("description").asText());
        assertEquals("string", visitor.getSchemaNode().get("type").asText());
    }

    @Test
    public void testUseTimestamp() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null);
        String value = "2023-03-15T12:30:45.678Z";
        JsonNode node = mapper.writeValueAsTree(value, serializer);
        assertEquals("1678942245000", node.asText());

        String readValue = mapper.readValue(node.toString(), String.class);
        assertEquals("", readValue);

        JsonFormat<String> jsonFormat = new JsonFormat.Value() {
            @Override
            public JsonFormat.Shape shape() {
                return JsonFormat.Shape.NUMBER;
            }

            @Override
            public TimeZone timeZone() {
                return null;
            }

            @Override
            public Locale locale() {
                return null;
            }

            @Override
            public String pattern() {
                return null;
            }
        };

        JsonNode schemaNode = serializer.getSchema(new SerializerProvider(), String.class.getType());
        assertEquals("number", schemaNode.asText());
        assertTrue(schemaNode.has("description"));
        assertTrue(schemaNode.has("type"));
        assertEquals("date-time",
                schemaNode.get("description").asText());
        assertEquals("number", schemaNode.get("type").asText());

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StdDateFormatSchema());
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
        assertEquals("number", visitor.getSchemaNode().get("type").asText());
    }

    @Test
    public void testCustomFormat() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            protected DateFormat _customFormat() {
                return new SimpleDateFormat("dd-MM-yyyy");
            }
        };
        String value = "15-03-2023";
        JsonNode node = mapper.writeValueAsTree(value, serializer);
        assertEquals("15-03-2023", node.asText());

        String readValue = mapper.readValue(node.toString(), String.class);
        assertEquals(value, readValue);

        JsonFormat<String> jsonFormat = new JsonFormat.Value() {
            @Override
            public JsonFormat.Shape shape() {
                return JsonFormat.Shape.STRING;
            }

            @Override
            public TimeZone timeZone() {
                return null;
            }

            @Override
            public Locale locale() {
                return null;
            }

            @Override
            public String pattern() {
                return "dd-MM-yyyy";
            }
        };

        JsonNode schemaNode = serializer.getSchema(new SerializerProvider(), String.class.getType());
        assertEquals("string", schemaNode.asText());
        assertTrue(schemaNode.has("description"));
        assertTrue(schemaNode.has("type"));
        assertEquals("date", schemaNode.get("description").asText());
        assertEquals("string", schemaNode.get("type").asText());

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StdDateFormatSchema());
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
        assertEquals("date", visitor.getSchemaNode().get("description").asText());
        assertEquals("string", visitor.getSchemaNode().get("type").asText());
    }

    @Test
    public void testNull() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonNode node = mapper.writeValueAsTree(null, serializer);
        assertNull(node);

        String readValue = mapper.readValue(node.toString(), String.class);
        assertNull(readValue);
    }

    @Test
    public void testCustomFormatWithTimeZone() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            protected DateFormat _customFormat() {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                return df;
            }
        };
        String value = "15-03-2023 12:30:45";
        JsonNode node = mapper.writeValueAsTree(value, serializer);
        assertEquals("15-03-2023 12:30:45", node.asText());

        String readValue = mapper.readValue(node.toString(), String.class);
        assertEquals(value, readValue);

        JsonFormat<String> jsonFormat = new JsonFormat.Value() {
            @Override
            public JsonFormat.Shape shape() {
                return JsonFormat.Shape.STRING;
            }

            @Override
            public TimeZone timeZone() {
                return TimeZone.getTimeZone("UTC");
            }

            @Override
            public Locale locale() {
                return null;
            }

            @Override
            public String pattern() {
                return "dd-MM-yyyy HH:mm:ss";
            }
        };

        JsonNode schemaNode = serializer.getSchema(new SerializerProvider(), String.class.getType());
        assertEquals("string", schemaNode.asText());
        assertTrue(schemaNode.has("description"));
        assertTrue(schemaNode.has("type"));
        assertEquals("date-time", schemaNode.get("description").asText());
        assertEquals("string", schemaNode.get("type").asText());

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StdDateFormatSchema());
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
        assertEquals("date-time", visitor.getSchemaNode().get("description").asText());
        assertEquals("string", visitor.getSchemaNode().get("type").asText());
    }

    @Test
    public void testCustomFormatWithLocale() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            protected DateFormat _customFormat() {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                df.setLocale(Locale.GERMAN);
                return df;
            }
        };
        String value = "15-03-2023 12:30:45";
        JsonNode node = mapper.writeValueAsTree(value, serializer);
        assertEquals("15.03.2023 12:30:45", node.asText());

        String readValue = mapper.readValue(node.toString(), String.class);
        assertEquals(value, readValue);

        JsonFormat<String> jsonFormat = new JsonFormat.Value() {
            @Override
            public JsonFormat.Shape shape() {
                return JsonFormat.Shape.STRING;
            }

            @Override
            public TimeZone timeZone() {
                return null;
            }

            @Override
            public Locale locale() {
                return Locale.GERMAN;
            }

            @Override
            public String pattern() {
                return "dd-MM-yyyy HH:mm:ss";
            }
        };

        JsonNode schemaNode = serializer.getSchema(new SerializerProvider(), String.class.getType());
        assertEquals("string", schemaNode.asText());
        assertTrue(schemaNode.has("description"));
        assertTrue(schemaNode.has("type"));
        assertEquals("date-time", schemaNode.get("description").asText());
        assertEquals("string", schemaNode.get("type").asText());

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StdDateFormatSchema());
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
        assertEquals("date-time", visitor.getSchemaNode().get("description").asText());
        assertEquals("string", visitor.getSchemaNode().get("type").asText());
    }
}