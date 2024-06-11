package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }
        };
    }

    @Test
    public void testCreateContextual_ShapeIsNumeric_ReturnsWithFormatTrueAndNullCustomFormat() throws JsonMappingException {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.NUMBER, null, null);

        // act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // assert
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.TRUE, dateTimeSerializerBase._useTimestamp);
        assertNull(dateTimeSerializerBase._customFormat);
    }

    @Test
    public void testCreateContextual_ShapeIsString_ReturnsWithFormatFalseAndNonNullCustomFormat() throws JsonMappingException {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, null, null);

        // act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // assert
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        assertEquals(Boolean.FALSE, dateTimeSerializerBase._useTimestamp);
    }
    
    @Test
    public void testCreateContextual_FormatHasPattern_ReturnsCustomFormatWithPattern() throws Exception {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, null, "yyyy-MM-dd'T'HH:mm:ss");
        
        // act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        // assert
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        assertNotNull(dateTimeSerializerBase._customFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", ((SimpleDateFormat)dateTimeSerializerBase._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual_FormatHasLocale_ReturnsCustomFormatWithLocale() throws Exception {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, null, null, null, Locale.US, null);
        
        // act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        // assert
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        assertNotNull(dateTimeSerializerBase._customFormat);
        assertEquals(Locale.US, ((SimpleDateFormat)dateTimeSerializerBase._customFormat).getLocale());
    }

    @Test
    public void testCreateContextual_FormatHasTimeZone_ReturnsCustomFormatWithTimeZone() throws Exception {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, null, null, TimeZone.getTimeZone("UTC"), null, null);
        
        // act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        // assert
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        assertNotNull(dateTimeSerializerBase._customFormat);
        assertEquals(TimeZone.getTimeZone("UTC"), ((SimpleDateFormat)dateTimeSerializerBase._customFormat).getTimeZone());
    }

    @Test
    public void testCreateContextual_FormatIsNull_ReturnsOriginalInstance() throws JsonMappingException {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = null;

        // act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // assert
        assertNotNull(result);
        assertEquals(serializer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_PropertyIsNull_ThrowsIllegalArgumentException() throws JsonMappingException {
        // arrange
        SerializerProvider serializers = null;
        BeanProperty property = null;
        boolean isWriteAsTimestamp = true;
        DateFormat format = null;

        // act
        serializer.createContextual(serializers, property);

        // assert
        // exception expected
    }

    @Test
    public void testGetSchema_ReturnsJsonSchemaNodeWithNumberTypeIfAsTimestampIsTrue() throws JsonMappingException {
        // arrange
        SerializerProvider serializers = null;
        Class<?> typeHint = null;
        serializer._useTimestamp = true;

        // act
        Object result = serializer.getSchema(serializers, typeHint);

        // assert
        assertNotNull(result);
        assertTrue(result instanceof com.fasterxml.jackson.databind.node.ObjectNode);
        com.fasterxml.jackson.databind.node.ObjectNode objectNode = (com.fasterxml.jackson.databind.node.ObjectNode) result;
        assertNotNull(objectNode.get("type"));
        assertEquals("number", objectNode.get("type").asText());
    }

    @Test
    public void testGetSchema_ReturnsJsonSchemaNodeWithStringTypeIfAsTimestampIsFalse() throws JsonMappingException {
        // arrange
        SerializerProvider serializers = null;
        Class<?> typeHint = null;
        serializer._useTimestamp = false;

        // act
        Object result = serializer.getSchema(serializers, typeHint);

        // assert
        assertNotNull(result);
        assertTrue(result instanceof com.fasterxml.jackson.databind.node.ObjectNode);
        com.fasterxml.jackson.databind.node.ObjectNode objectNode = (com.fasterxml.jackson.databind.node.ObjectNode) result;
        assertNotNull(objectNode.get("type"));
        assertEquals("string", objectNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber_VisitsIntFormat() throws JsonMappingException {
        // arrange
        JsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        boolean asNumber = true;

        // act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // assert
        assertEquals(JsonParser.NumberType.LONG, ((MockJsonFormatVisitorWrapper) visitor).numberType);
        assertEquals(JsonValueFormat.UTC_MILLISEC, ((MockJsonFormatVisitorWrapper) visitor).format);
    }

    @Test
    public void testAcceptJsonFormatVisitor_NotAsNumber_VisitsStringFormat() throws JsonMappingException {
        // arrange
        JsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        boolean asNumber = false;

        // act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // assert
        assertNull(((MockJsonFormatVisitorWrapper) visitor).numberType);
        assertEquals(JsonValueFormat.DATE_TIME, ((MockJsonFormatVisitorWrapper) visitor).format);
    }

    class MockJsonFormatVisitorWrapper implements JsonFormatVisitorWrapper {

        public JsonParser.NumberType numberType;
        public JsonValueFormat format;

        @Override
        public SerializerProvider getProvider() {
            return null;
        }

        @Override
        public void setProvider(SerializerProvider provider) {

        }

        @Override
        public JsonFormatVisitorWrapper setVisitorBase(JsonFormatVisitorWrapper.Base base) {
            return null;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            return null;
        }
        
        @Override
        public void expectAnyFormat(JavaType type) throws JsonMappingException {
            
        }

        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonMapFormatVisitor expectMapFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
            return null;
        }

		@Override
		public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
			return null;
		}

		@Override
		public JsonObjectFormatVisitor expectObjectFormat(Class<?> type) throws JsonMappingException {
			return null;
		}

		@Override
		public JsonStringFormatVisitor expectStringFormat(Class<?> type) throws JsonMappingException {
			return null;
		}
    }

}