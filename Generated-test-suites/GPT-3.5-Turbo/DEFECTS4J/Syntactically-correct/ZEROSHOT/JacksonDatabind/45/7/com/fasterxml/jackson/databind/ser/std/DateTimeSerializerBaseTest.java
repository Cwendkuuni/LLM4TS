package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    DateTimeSerializerBase<String> dateTimeSerializerBase;

    @Before
    public void setup() {
        dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Annotated annotatedProperty = Mockito.mock(Annotated.class);
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        Locale locale = Mockito.mock(Locale.class);
        SimpleDateFormat simpleDateFormat = Mockito.mock(SimpleDateFormat.class);

        Mockito.when(serializers.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        Mockito.when(property.getMember()).thenReturn(annotatedProperty);
        Mockito.when(serializers.getLocale()).thenReturn(locale);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        Mockito.when(format.hasPattern()).thenReturn(true);
        Mockito.when(format.getPattern()).thenReturn("yyyy-MM-dd");
        Mockito.when(format.hasLocale()).thenReturn(true);
        Mockito.when(format.getLocale()).thenReturn(locale);

        Mockito.when(simpleDateFormat.getTimeZone()).thenReturn(null);
        Mockito.when(serializers.getTimeZone()).thenReturn(null);

        Mockito.when(new SimpleDateFormat("yyyy-MM-dd", locale)).thenReturn(simpleDateFormat);

        dateTimeSerializerBase.createContextual(serializers, property);

        Mockito.verify(serializers, Mockito.times(1)).getAnnotationIntrospector();
        Mockito.verify(property, Mockito.times(1)).getMember();
        Mockito.verify(simpleDateFormat, Mockito.times(1)).getTimeZone();
        Mockito.verify(format, Mockito.times(1)).getTimeZone();
        Mockito.verify(simpleDateFormat, Mockito.times(1)).setTimeZone(null);
    }

    @Test
    public void testIsEmpty() {
        String value = null;
        assertTrue(dateTimeSerializerBase.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        JsonNode jsonNode = dateTimeSerializerBase.getSchema(serializers, typeHint);

        assertEquals("string", jsonNode.get("type").asText());
        assertTrue(jsonNode.get("format").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor, Mockito.times(1)).visitStringFormat(typeHint, JsonValueFormat.DATE_TIME);
    }
}