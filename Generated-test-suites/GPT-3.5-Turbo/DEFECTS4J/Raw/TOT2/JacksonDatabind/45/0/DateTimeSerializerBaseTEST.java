package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatShape;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, null, null) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }
        };

        DateTimeSerializerBase<?> newSerializer = serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertTrue(newSerializer._useTimestamp);
        assertEquals("yyyy-MM-dd HH:mm:ss", ((SimpleDateFormat) newSerializer._customFormat).toPattern());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, null, null) {
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
        };

        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(null, null));
        assertFalse(serializer.isEmpty(null, new Object()));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, null, null) {
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return true;
            }

            @Override
            public JsonNode createSchemaNode(String type, boolean isNumber) {
                ObjectNode node = JsonNodeFactory.instance.objectNode();
                node.put("type", type);
                node.put("isNumber", isNumber);
                return node;
            }
        };

        ObjectNode schema = (ObjectNode) serializer.getSchema(null, null);
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("isNumber").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, null, null) {
            @Override
            protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType, JsonValueFormat numberFormat) throws JsonMappingException {
            }

            @Override
            protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat valueFormat) throws JsonMappingException {
            }

            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;
            }
	        };

	        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
	        serializer.acceptJsonFormatVisitor(visitor, null);
	        // Check that the visitor is visited correctly
	    }

	    @Test
	    public void testSerialize() throws IOException {
	        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, null, null) {
	            @Override
	            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
	            }
	        };

	        // Test serialization here
	    }
	}