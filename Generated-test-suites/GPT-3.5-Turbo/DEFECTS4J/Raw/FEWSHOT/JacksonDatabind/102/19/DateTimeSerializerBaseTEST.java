package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    private boolean useTimestamp;
    private DateFormat customFormat;
    private DateTimeSerializerBase<T> serializer;

    public DateTimeSerializerBaseTest() {
        this.useTimestamp = false;
        this.customFormat = null;
        this.serializer = new DateTimeSerializerBase<T>(null, null, null) {

            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                
            }
        };
    }

    @Test
    public void testGetSchema() {
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        JsonNode resultNode = serializer.getSchema(mockProvider, null);
        assertEquals("string", resultNode.get("type").asText());
        assertEquals(true, resultNode.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
    }

    @Test
    public void testSerializeAsString() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        SimpleDateFormat mockCustomFormat = mock(SimpleDateFormat.class);
        Date mockDate = mock(Date.class);

        when(mockCustomFormat.format(mockDate)).thenReturn("2018-03-01");

        AtomicReference<DateFormat> reusedCustomFormat = new AtomicReference<DateFormat>(mockCustomFormat);
        serializer._reusedCustomFormat = reusedCustomFormat;
        serializer._customFormat = mockCustomFormat;

        serializer._serializeAsString(mockDate, mockGenerator, mockProvider);

        // Verify that the correct format method was called and the value was written to the generator
        assertEquals("2018-03-01", mockGenerator.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeAsString_NullSerializerProvider() throws IOException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = null;
        SimpleDateFormat mockCustomFormat = mock(SimpleDateFormat.class);
        Date mockDate = mock(Date.class);

        when(mockCustomFormat.format(mockDate)).thenReturn("2018-03-01");

        AtomicReference<DateFormat> reusedCustomFormat = new AtomicReference<DateFormat>(mockCustomFormat);
        serializer._reusedCustomFormat = reusedCustomFormat;
        serializer._customFormat = mockCustomFormat;

        serializer._serializeAsString(mockDate, mockGenerator, mockProvider);
    }

}