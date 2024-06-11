package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat_ValidInput_Success() {
        // Arrange
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Act
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, customFormat) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;  // not relevant for this test
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                // not relevant for this test
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };

        // Assert
        assertEquals(customFormat, serializer._customFormat);
    }
    
    @Test
    public void testEmpty_ValueIsNull_ReturnTrue() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;  // not relevant for this test
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                // not relevant for this test
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        String value = null;

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEmpty_ValueIsEmptyDate_ReturnTrue() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;  // not relevant for this test
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                // not relevant for this test
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        String value = "";

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testEmpty_ValueIsNotEmptyDate_ReturnFalse() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;  // not relevant for this test
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                // not relevant for this test
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        String value = "1990-01-01";

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetSchema_UseTimestamp_ReturnNumberSchemaNode() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.TRUE, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.createObjectNode().put("type", "number");
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                // not relevant for this test
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        SerializerProvider serializers = null;
        JavaType typeHint = null;
        
        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        JsonNode expected = new ObjectMapper().createObjectNode().put("type", "number");
        assertEquals(expected, result);
    }

    @Test
    public void testGetSchema_UseTimestamp_ReturnStringSchemaNode() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.createObjectNode().put("type", "string");
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                // not relevant for this test
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        SerializerProvider serializers = null;
        JavaType typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        JsonNode expected = new ObjectMapper().createObjectNode().put("type", "string");
        assertEquals(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsTimestamp_UseIntFormatMethod() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.TRUE, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;  // not relevant for this test
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                visitor.expectIntFormat(typeHint, JsonParser.NumberType.LONG);
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        // Act & Assert (no exception expected)
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString_UseStringFormatMethod() {
        // Arrange
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;  // not relevant for this test
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                // not relevant for this test
            }

            @Override
            protected long _timestamp(String value) {
                return 0;  // not relevant for this test
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;  // not relevant for this test
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonProcessingException {
                visitor.expectStringFormat(typeHint, JsonValueFormat.DATE_TIME);
            }
            
            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return false;  // not relevant for this test
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    boolean asNumber) throws JsonMappingException {
                // not relevant for this test
            }
        };
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        // Act & Assert (no exception expected)
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}