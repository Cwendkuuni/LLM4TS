package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.StdDateFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Date> DATE_SERIALIZER = new DateTimeSerializerBase<Date>(Date.class, null, null) {
        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }

        @Override
        protected void _serializeAsString(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            super._serializeAsString(value, gen, serializers);
        }
    };

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> TIMESTAMP_SERIALIZER = new DateTimeSerializerBase<Long>(Long.class, true, null);

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    // Test for constructor
    @Test
    public void testConstructor() {
        DateTimeSerializerBase<Date> serializer1 = new DateTimeSerializerBase<>(Date.class, true, null);
        DateTimeSerializerBase<Date> serializer2 = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat(DEFAULT_PATTERN));

        assertEquals(serializer1.getClass(), DateTimeSerializerBase.class);
        assertEquals(serializer1.handledType(), Date.class);
        assertEquals(serializer1._useTimestamp, true);
        assertNull(serializer1._customFormat);
        assertNull(serializer1._reusedCustomFormat);

        assertEquals(serializer2.getClass(), DateTimeSerializerBase.class);
        assertEquals(serializer2.handledType(), Date.class);
        assertEquals(serializer2._useTimestamp, false);
        assertNotNull(serializer2._customFormat);
    }

    // Test for withFormat method
    @Test
    public void testWithFormat() throws JsonProcessingException {
        DateFormat customFormat = new SimpleDateFormat(DEFAULT_PATTERN);
        DateTimeSerializerBase<Date> serializer = DATE_SERIALIZER;

        DateTimeSerializerBase<Date> serializerWithFormat = serializer.withFormat(false, customFormat);

        assertEquals(serializerWithFormat.getClass(), DateTimeSerializerBase.class);
        assertEquals(serializerWithFormat.handledType(), Date.class);
        assertEquals(serializerWithFormat._useTimestamp, false);
        assertNotNull(serializerWithFormat._customFormat);
    }

    // Test for createContextual method
    @Test
    public void testCreateContextual() throws JsonMappingException, JsonProcessingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("US/Pacific"));

        when(serializers.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("US/Pacific"));

        DateTimeSerializerBase<Date> serializer = DATE_SERIALIZER;
        JsonSerializer<Date> serializedSerializer = serializer.createContextual(serializers, null);

        JsonFormat.Shape shape = serializedSerializer.getShape();
        assertEquals(shape.getSerializationShape(), JsonFormat.Shape.STRING);
        assertEquals(serializedSerializer.getClass(), DateTimeSerializerBase.class);
        assertEquals(serializedSerializer.handledType(), Date.class);
        assertNotNull(serializedSerializer._customFormat);
        assertEquals(serializedSerializer._customFormat.toPattern(), "dd-MM-yyyy HH:mm:ss");
        assertEquals(serializedSerializer._customFormat.getLocale(), Locale.US);
        assertEquals(serializedSerializer._customFormat.getTimeZone(), TimeZone.getTimeZone("US/Pacific"));
    }

    // Test for isEmpty method
    @Test
    public void testIsEmpty() {
        assertEquals(DATE_SERIALIZER.isEmpty(mock(SerializerProvider.class), null), false);
        assertEquals(TIMESTAMP_SERIALIZER.isEmpty(mock(SerializerProvider.class), null), false);
        assertEquals(TIMESTAMP_SERIALIZER.isEmpty(mock(SerializerProvider.class), new Date(0L)), false);
    }

    // Test for getSchema method
    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = DATE_SERIALIZER.getSchema(mock(SerializerProvider.class), null.getClass());
        assertEquals(schema.toString(), "{\"type\":\"string\",\"description\":\"Date value\",\"items\":null,\"minItems\":1,\"maxItems\":null,\"uniqueItems\":false,\"format\":\"date-time\"}");

        schema = TIMESTAMP_SERIALIZER.getSchema(mock(SerializerProvider.class), null.getClass());
        assertEquals(schema.toString(), "{\"type\":\"number\",\"description\":\"Date value\",\"minimum\":0,\"exclusiveMaximum\":null,\"format\":\"date-time\"}");
    }

    // Test for serialize method - string format
    @Test
    public void testSerializeStringFormat() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getDateFormat()).thenReturn(new SimpleDateFormat(DEFAULT_PATTERN));

        Date date = new Date();
        DATE_SERIALIZER.serialize(date, gen, serializers);

        String expectedDateString = new SimpleDateFormat(DEFAULT_PATTERN).format(date);
        verify(gen).writeString(expectedDateString);
    }

    // Test for serialize method - timestamp format
    @Test
    public void testSerializeTimestampFormat() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        long timestamp = System.currentTimeMillis();
        TIMESTAMP_SERIALIZER.serialize(timestamp, gen, serializers);

        verify(gen).writeNumber(timestamp);
    }

    // Test for _asTimestamp method
    @Test
    public void testAsTimestamp() throws JsonProcessingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertEquals(DATE_SERIALIZER._asTimestamp(serializers), false);
        assertEquals(TIMESTAMP_SERIALIZER._asTimestamp(serializers), true);
    }

    // Test for _acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        DATE_SERIALIZER._acceptJsonFormatVisitor(visitor, typeHint, false);
        verify(visitor).visitStringFormat(eq(typeHint), eq(JsonFormat.Shape.STRING), any(), anyString());

        TIMESTAMP_SERIALIZER._acceptJsonFormatVisitor(visitor, typeHint, true);
        verify(visitor).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.LONG), any(), any());
    }

    // Test for _serializeAsString method with reused custom format
    @Test
    public void testSerializeAsStringWithReusedCustomFormat() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateFormat customFormat = new SimpleDateFormat(DEFAULT_PATTERN);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        Date date = new Date();
        serializer.serialize(date, gen, serializers);

        String expectedDateString = new SimpleDateFormat(DEFAULT_PATTERN).format(date);
        verify(gen).writeString(expectedDateString);

        Date date2 = new Date();
        serializer.serialize(date2, gen, serializers);

        String expectedDateString2 = new SimpleDateFormat(DEFAULT_PATTERN).format(date2);
        verify(gen, times(2)).writeString(anyString());
        verify(serializer)._reusedCustomFormat.compareAndSet(nullable(DateFormat.class), any());
    }

    // Test for _serializeAsString method with new custom format instance
    @Test
    public void testSerializeAsStringWithNewCustomFormatInstance() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateFormat customFormat = new SimpleDateFormat(DEFAULT_PATTERN);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        Date date = new Date();
        serializer.serialize(date, gen, serializers);

        String expectedDateString = new SimpleDateFormat(DEFAULT_PATTERN).format(date);
        verify(gen).writeString(expectedDateString);

        Date date2 = new Date();
        serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        serializer.serialize(date2, gen, serializers);

        String expectedDateString2 = new SimpleDateFormat(DEFAULT_PATTERN).format(date2);
        verify(gen, times(2)).writeString(anyString());
        verify(customFormat, times(2)).format(any(Date.class));
    }

    // Test for _serializeAsString method with null custom format
    @Test(expected = JsonMappingException.class)
    public void testSerializeAsStringWithNullCustomFormat() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        serializer.serialize(new Date(), gen, serializers);

        verify(gen).writeNull();
    }

    // Test for _serializeAsString method with illegal custom format
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeAsStringWithIllegalCustomFormat() throws JsonProcessingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new StdDateFormat());
        serializer.serialize(new Date(), gen, serializers);

        verify(gen).writeNull();
    }
}