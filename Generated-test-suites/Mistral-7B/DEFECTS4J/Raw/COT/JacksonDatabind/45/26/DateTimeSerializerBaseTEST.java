package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> serializerUnderTest;
    @Mock
    private SerializerProvider serializers;
    @Mock
    private JsonFormatVisitorWrapper visitor;
    @Mock
    private JavaType typeHint;
    @Mock
    private JsonParser parser;

    @Before
    public void setUp() {
        serializerUnderTest = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            protected long _timestamp(Long value) {
                return value.longValue();
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<?>(){};
        assertEquals(true, serializer instanceof StdScalarSerializer);
        assertEquals(null, serializer._useTimestamp);
        assertEquals(null, serializer._customFormat);
    }

    @Test
    public void testWithFormat_useTimestamp_true() {
        DateTimeSerializerBase<Long> serializer = serializerUnderTest.withFormat(Boolean.TRUE, null);
        assertEquals(Boolean.TRUE, serializer._useTimestamp);
        assertEquals(null, serializer._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormat_useTimestamp_null() {
        DateTimeSerializerBase<Long> serializer = serializerUnderTest.withFormat(null, null);
    }

    @Test
    public void testWithFormat_customFormat_notNull() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeSerializerBase<Long> serializer = serializerUnderTest.withFormat(Boolean.FALSE, customFormat);
        assertEquals(Boolean.FALSE, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormat_customFormat_null() {
        DateTimeSerializerBase<Long> serializer = serializerUnderTest.withFormat(Boolean.FALSE, null);
    }

    @Test
    public void testIsEmpty_valueIsNull() {
        assertTrue(serializerUnderTest.isEmpty(null));
    }

    @Test
    public void testIsEmpty_valueNotNull() {
        Long value = 0L;
        assertTrue(serializerUnderTest.isEmpty(serializers, value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        String expected = "string";
        when(serializers.getLocale()).thenReturn(Locale.US);
        JsonNode result = serializerUnderTest.getSchema(serializers, Long.TYPE);
        assertEquals(expected, result.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializerUnderTest.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize_valueIsNull() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        serializerUnderTest.serialize(null, gen, serializers);
        verify(gen).writeNull();
    }

    @Test
    public void testSerialize_valueIsZero() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Long value = 0L;
        when(serializers._asTimestamp(serializers)).thenReturn(true);
        serializerUnderTest.serialize(value, gen, serializers);
        verify(gen).writeNumber(0);
    }

    @Test
    public void testSerialize_valueIsNotNull() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Long value = 1234567890123L;
        when(serializers._asTimestamp(serializers)).thenReturn(false);
        when(parser.getFormat(JsonFormat.Value.class)).thenReturn(mock(JsonFormat.Value.class));
        when(parser.getFormat(JsonFormat.Value.class).getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(parser.getFormat(JsonFormat.Value.class).getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        when(parser.getFormat(JsonFormat.Value.class).getLocale()).thenReturn(Locale.US);
        when(parser.getFormat(JsonFormat.Value.class).getTimeZone()).thenReturn(TimeZone.getDefault());
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        when(serializers._customFormat).thenReturn(customFormat);
        when(customFormat.format(new java.util.Date(value.longValue()))).thenReturn("2012-02-29T12:34:56.789Z");
        serializerUnderTest.serialize(value, gen, serializers);
        verify(gen).writeString("2012-02-29T12:34:56.789Z");
    }
}