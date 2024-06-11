package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest extends TestBase {

    @InjectMocks
    private DateTimeSerializerBase<Long> longSerializer;
    @InjectMocks
    private DateTimeSerializerBase<String> stringSerializer;

    @Test
    public void testLongSerializerWithFormat() throws Exception {
        final JsonFormat.Shape shape = JsonFormat.Shape.NUMBER;
        final JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(shape);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        final DateTimeSerializerBase<Long> serialized = longSerializer.withFormat(true, new StdDateFormat(format));

        assertEquals(serializers().findValueSerializer(Long.class), serialized);
        assertEquals(shape, serialized.getShape());
        assertEquals(Locale.US, serialized.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), serialized.getTimeZone());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", serialized.getPattern());
    }

    @Test
    public void testLongSerializerWithoutFormat() throws Exception {
        final DateTimeSerializerBase<Long> serialized = longSerializer;

        assertEquals(serializers().findValueSerializer(Long.class), serialized);
        assertNull(serialized.getLocale());
        assertNull(serialized.getTimeZone());
        assertNull(serialized.getPattern());
    }

    @Test
    public void testStringSerializerWithFormat() throws Exception {
        final JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        final JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(shape);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        final DateTimeSerializerBase<String> serialized = stringSerializer.withFormat(false, new SimpleDateFormat(format.getPattern(), format.getLocale()));

        assertEquals(serializers().findValueSerializer(String.class), serialized);
        assertEquals(shape, serialized.getShape());
        assertEquals(Locale.US, serialized.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), serialized.getTimeZone());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", serialized.getPattern());
    }

    @Test
    public void testStringSerializerWithoutFormat() throws Exception {
        final DateTimeSerializerBase<String> serialized = stringSerializer;

        assertEquals(serializers().findValueSerializer(String.class), serialized);
        assertNull(serialized.getLocale());
        assertNull(serialized.getTimeZone());
        assertNull(serialized.getPattern());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithLongFormat() throws Exception {
        final JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        final JsonParser serializers = mock(JsonParser.class);
        final SerializerProvider provider = new SerializerProvider();
        final ObjectCodec codec = new ObjectMapper().getSerializationConfig().getDefaultInstance();

        when(visitor.getSerializerProvider()).thenReturn(provider);
        when(provider.findValueSerializer(Long.class)).thenReturn(longSerializer);

        longSerializer._acceptJsonFormatVisitor(visitor, null, _asTimestamp(serializers));

        verify(visitor).visitIntFormat(any(JsonFormatVisitorWrapper.class).isA(isA(JsonFormatVisitorWrapper.class)), any(JavaType.class), anyInt(), anyInt());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringFormat() throws Exception {
        final JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        final JsonParser serializers = mock(JsonParser.class);
        final SerializerProvider provider = new SerializerProvider();
        final ObjectCodec codec = new ObjectMapper().getSerializationConfig().getDefaultInstance();

        when(visitor.getSerializerProvider()).thenReturn(provider);
        when(provider.findValueSerializer(String.class)).thenReturn(stringSerializer);

        stringSerializer._acceptJsonFormatVisitor(visitor, null, _asTimestamp(serializers));

        verify(visitor).visitStringFormat(any(JsonFormatVisitorWrapper.class).isA(isA(JsonFormatVisitorWrapper.class)), any(JavaType.class), any());
    }

    @Test
    public void testSerializeLongValue() throws IOException {
        final Long longValue = 1516332800000L;
        final JsonGenerator gen = createMock(JsonGenerator.class);
        final SerializerProvider serializers = new SerializerProvider();

        when(gen.writeNumber(anyLong())).then(invocation -> gen.writeString(new StdDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601).format(new Date(longValue))));

        longSerializer.serialize(longValue, gen, serializers);

        verify(gen).writeString("2018-01-01T00:00:00.000Z");
    }

    @Test
    public void testSerializeStringValue() throws IOException {
        final String stringValue = "2018-01-01T00:00:00.000Z";
        final JsonGenerator gen = createMock(JsonGenerator.class);
        final SerializerProvider serializers = new SerializerProvider();

        when(gen.writeString(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        stringSerializer.serialize(stringValue, gen, serializers);

        verify(gen).writeString(stringValue);
    }

    @Test
    public void testIsEmpty() throws IOException {
        assertFalse(longSerializer.isEmpty(null));
        assertTrue(longSerializer.isEmpty(null));
        assertFalse(longSerializer.isEmpty(1516332800000L));
        assertFalse(longSerializer.isEmpty(new Date(0)));
    }

    @Test
    public void testGetSchema() throws IOException {
        final JsonNode schema = longSerializer.getSchema(null, Long.class);

        assertEquals("number", schema.getType().getClass().getSimpleName());
    }

    @Test
    public void testCreateContextual() throws IOException {
        final ObjectCodec codec = new ObjectMapper().getSerializationConfig().getDefaultInstance();
        final JsonParser serializers = new JsonParser();
        final BeanProperty property = Mockito.mock(BeanProperty.class);

        when(property.getName()).thenReturn("testProperty");

        final JsonFormat.Value format = new JsonFormat.Value();
        final JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        format.setShape(shape);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        when(codec.findFormatOverrides(eq(serializers), any(BeanProperty.class), any(JavaType.class)))
                .thenReturn(format);

        final DateTimeSerializerBase<String> serialized = stringSerializer.createContextual(null, property);

        assertEquals(stringSerializer, serialized);
        assertEquals(shape, serialized.getShape());
        assertEquals(Locale.US, serialized.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), serialized.getTimeZone());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", serialized.getPattern());
    }

    private boolean _asTimestamp(JsonParser serializers) {
        return longSerializer._asTimestamp(serializers);
    }

}