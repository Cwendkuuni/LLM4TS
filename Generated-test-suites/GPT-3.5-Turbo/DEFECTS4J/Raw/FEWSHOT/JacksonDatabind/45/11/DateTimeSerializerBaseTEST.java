package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testWithContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(JsonFormat.Value.forShape(JsonFormat.Shape.STRING));
        when(serializers.getLocale()).thenReturn(Locale.getDefault());
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonSerializer<?> contextualSerializer = serializer.createContextual(serializers, property);

        verify(serializers.getAnnotationIntrospector(), times(1)).findFormat(annotated);
        verify(serializers, times(1)).getLocale();
        verify(serializers, times(1)).getTimeZone();
    }

    @Test
    public void testCreateSchemaNode() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonNode jsonNode = serializer.createSchemaNode("number", true);

        assertEquals("number", jsonNode.getNodeType().name());
        assertEquals(true, jsonNode.isValueNode());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).getProvider();
    }

    @Test
    public void testIsNotEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        boolean isEmpty = serializer.isEmpty(null);
        assertEquals(true, isEmpty);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
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

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        serializer.serialize("value", gen, serializers);

        verify(gen, times(1)).writeString(anyString());
    }

    @Test
    public void testAsTimestamp() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        boolean asTimestamp = serializer._asTimestamp(null);

        assertEquals(false, asTimestamp);
    }
}