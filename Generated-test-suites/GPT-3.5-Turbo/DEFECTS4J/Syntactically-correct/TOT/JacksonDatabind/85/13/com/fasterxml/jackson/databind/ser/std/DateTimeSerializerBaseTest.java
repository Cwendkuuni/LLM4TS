
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;
    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, com.fasterxml.jackson.core.JsonGenerator gen,
                                  com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.getTime());
            }
        };
    }

    @Test
    public void testCreateContextual_NoFormat_ShouldReturnThis() throws Exception {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        JsonSerializer<?> actual = serializer.createContextual(serializers, property);

        assertEquals(serializer, actual);
    }

    @Test
    public void testCreateContextual_NumericShape_ShouldReturnWithFormatTrue() throws Exception {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        when(serializer.findFormatOverrides(serializers, property, Date.class)).thenReturn(format);

        JsonSerializer<?> actual = serializer.createContextual(serializers, property);

        DateTimeSerializerBase<Date> expected = serializer.withFormat(true, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateContextual_StringShape_ShouldReturnWithFormatFalse() throws Exception {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(false);
        when(serializer.findFormatOverrides(serializers, property, Date.class)).thenReturn(format);

        JsonSerializer<?> actual = serializer.createContextual(serializers, property);

        DateTimeSerializerBase<Date> expected = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSchema_UseTimestamp_ShouldReturnNumberSchema() throws Exception {
        SerializerProvider serializers = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        when(serializer._asTimestamp(serializers)).thenReturn(true);

        JsonNode actual = serializer.getSchema(serializers, typeHint);

        ObjectNode expected = mapper.createObjectNode();
        expected.put("type", "number");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSchema_UseCustomFormat_ShouldReturnStringSchema() throws Exception {
        SerializerProvider serializers = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        when(serializer._asTimestamp(serializers)).thenReturn(false);

        JsonNode actual = serializer.getSchema(serializers, typeHint);

        ObjectNode expected = mapper.createObjectNode();
        expected.put("type", "string");
        assertEquals(expected, actual);
    }

    @Test
    public void testSerialize_UseTimestamp_ShouldWriteNumericTimestamp() throws Exception {
        DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
        sp.setNullKeySerializer(serializer);

        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(serializers.getConfig()).thenReturn(mapper.getSerializationConfig());
        when(serializers.getActiveView()).thenReturn(null);
        when(serializers.getAttribute("SERIALIZE_DATE_KEY_AS_TIMESTAMP")).thenReturn(true);
        when(property.getMember()).thenReturn(null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createGenerator(out);
        Date date = new Date(1623954560000L); // 17 Jun 2021, 12:16:00 UTC

        serializer.serialize(date, gen, serializers);
        gen.flush();
        String actual = out.toString();

        assertEquals("\"1623954560000\"", actual);
    }

    @Test
    public void testSerialize_UseCustomFormat_ShouldWriteFormattedDate() throws Exception {
        DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
        sp.setNullKeySerializer(serializer);

        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(serializers.getConfig()).thenReturn(mapper.getSerializationConfig());
        when(serializers.getActiveView()).thenReturn(null);
        when(property.getMember()).thenReturn(null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createGenerator(out);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = customFormat.parse("2021-06-17");

        serializer.withFormat(false, customFormat).serialize(date, gen, serializers);
        gen.flush();
        String actual = out.toString();

        assertEquals("\"2021-06-17\"", actual);
    }
}
