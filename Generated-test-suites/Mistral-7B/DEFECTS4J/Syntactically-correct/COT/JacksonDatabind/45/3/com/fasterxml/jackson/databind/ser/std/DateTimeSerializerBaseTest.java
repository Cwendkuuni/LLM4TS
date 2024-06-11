package com.fasterxml.jackson.databind.ser.std.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

public class DateTimeSerializerBaseTest extends BaseTest {

    public void testDateTimeSerializerBaseConstructor() {
        // Test default constructor
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        assertNull(dateTimeSerializerBase._useTimestamp);
        assertNull(dateTimeSerializerBase._customFormat);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<Long> longSerializerBase = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        assertTrue(longSerializerBase._useTimestamp);
        assertNull(longSerializerBase._customFormat);

        // Test constructor with useTimestamp and customFormat
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, customFormat);
        assertTrue(dateSerializerBase._useTimestamp);
        assertEquals(customFormat, dateSerializerBase._customFormat);

        // Test constructor with null useTimestamp and customFormat
        DateTimeSerializerBase<Date> dateSerializerBase2 = new DateTimeSerializerBase<Date>(Date.class, null, customFormat);
        assertFalse(dateSerializerBase2._useTimestamp);
        assertEquals(customFormat, dateSerializerBase2._customFormat);
    }

    @SuppressWarnings("unchecked")
    public void testWithFormat() throws JsonMappingException, IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);
        DateTimeSerializerBase<Date> timestampSerializerBase = dateSerializerBase.withFormat(Boolean.TRUE, null);
        assertEquals(dateSerializerBase, timestampSerializerBase);

        DateTimeSerializerBase<Date> dateSerializerBase2 = new DateTimeSerializerBase<Date>(Date.class, false, null);
        DateTimeSerializerBase<Date> stringSerializerBase = dateSerializerBase2.withFormat(Boolean.FALSE, customFormat);
        assertNotSame(dateSerializerBase2, stringSerializerBase);
        assertEquals(customFormat, stringSerializerBase._customFormat);
    }

    public void testIsEmpty() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        assertFalse(dateSerializerBase.isEmpty(null));
        assertFalse(dateSerializerBase.isEmpty(mock(Date.class)));
        assertFalse(dateSerializerBase.isEmpty(mock(SerializerProvider.class), null));
        assertFalse(dateSerializerBase.isEmpty(mock(SerializerProvider.class), mock(Date.class)));
    }

    public void testGetSchema() throws IOException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        JsonNode schema = dateSerializerBase.getSchema(null, Long.TYPE);
        assertEquals("number", schema.getClass().getSimpleName());

        JsonNode schema2 = dateSerializerBase.getSchema(mock(SerializerProvider.class), Long.TYPE);
        assertEquals("number", schema2.getClass().getSimpleName());
    }

    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFactory().getJsonFormatVisitorWrapper();
        JavaType typeHint = mapper.getTypeFactory().constructType(Date.class);

        dateSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
    }

    public void testSerialize() throws JsonMappingException, JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        Date date = new Date();
        when(serializers.isEnabled(any(SerializationFeature.class))).thenReturn(false);
        when(serializers.findFormat((Annotated)any())).thenReturn(null);

        when(serializers.getLocale()).thenReturn(Locale.getDefault());
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());

        when(gen.writeNumber(anyLong())).thenAnswer(invocation -> invocation.getArgument(0).toString());
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        dateSerializerBase.serialize(date, gen, serializers);

        verify(gen).writeStartObject();
        verify(gen).writeNumber(date.getTime());
        verify(gen).writeEndObject();

        when(serializers.isEnabled(any(SerializationFeature.class))).thenReturn(true);

        dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);

        when(serializers.isEnabled(any(SerializationFeature.class))).thenReturn(false);
        when(serializers.getLocale()).thenReturn(Locale.getDefault());
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());

        when(gen.writeNumber(anyLong())).thenAnswer(invocation -> invocation.getArgument(0).toString());
        when(gen.writeString(anyString())).thenAnswer(invocation -> invocation.getArgument(0).toString());

        when(serializers.findFormat((Annotated)any())).thenReturn(null);

        dateSerializerBase.serialize(date, gen, serializers);

        verify(gen).writeStartObject();
        verify(gen).writeNumber(date.getTime());
        verify(gen).writeEndObject();

        when(serializers.isEnabled(any(SerializationFeature.class))).thenReturn(true);
        when(serializers.findFormat((Annotated)any()))
                .thenReturn(JsonFormat.Value.builder()
                        .shape(JsonFormat.Shape.STRING)
                        .timeZone(TimeZone.getTimeZone("UTC"))
                        .locale(Locale.US)
                        .build());

        dateSerializerBase.serialize(date, gen, serializers);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("@type");
        verify(gen).writeString("string");
        verify(gen).writeString("2023-03-29T14:25:35.456Z");
        verify(gen).writeEndObject();

        when(serializers.isEnabled(any(SerializationFeature.class)))
                .thenReturn(true);
        when(serializers.findFormat((Annotated)any()))
                .thenReturn(JsonFormat.Value.builder()
                        .shape(JsonFormat.Shape.STRING)
                        .timeZone(TimeZone.getTimeZone("UTC"))
                        .locale(Locale.US)
                        .pattern("dd-MM-yyyy HH:mm:ss.SSS")
                        .build());

        dateSerializerBase.serialize(date, gen, serializers);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("@type");
        verify(gen).writeString("string");
        verify(gen).writeFieldName("format");
        verify(gen).writeString("dd-MM-yyyy HH:mm:ss.SSS");
        verify(gen).writeFieldName("value");
        verify(gen).writeString("29-03-2023 14:25:35.456");
        verify(gen).writeEndObject();
    }

    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = JsonFormat.Value.builder()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .locale(Locale.US)
                .build();

        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);

        assertSame(dateSerializerBase, dateSerializerBase.createContextual(serializers, property));

        when(property.getMember()).thenReturn(new Date());

        DateTimeSerializerBase<Date> stringSerializerBase = (DateTimeSerializerBase<Date>) dateSerializerBase.createContextual(serializers, property);

        assertEquals(format, stringSerializerBase._customFormat);
    }
}