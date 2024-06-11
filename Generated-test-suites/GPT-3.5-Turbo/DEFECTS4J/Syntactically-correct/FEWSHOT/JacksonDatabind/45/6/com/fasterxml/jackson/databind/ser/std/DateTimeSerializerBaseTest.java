package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Object> dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
    }

    @Test
    public void testCreateContextual_hasPropertyFormat_returnsDateTimeSerializerBase() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        BeanProperty property = mock(BeanProperty.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        format.setPattern("yyyy-MM-dd");
        format.setLocale(Locale.US);
        Mockito.when(property.getMember()).thenReturn(annotated);
        Mockito.when(provider.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);

        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(provider, property);

        Mockito.verify(provider.getAnnotationIntrospector()).findFormat(annotated);
        Mockito.verify(provider).getTimeZone();
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat)dateTimeSerializerBase._customFormat).toPattern());
        assertEquals(Locale.US, ((SimpleDateFormat)dateTimeSerializerBase._customFormat).getLocale());
        assertEquals(TimeZone.getTimeZone("GMT"), ((SimpleDateFormat)dateTimeSerializerBase._customFormat).getTimeZone());
        assertNull(result);
    }

    @Test
    public void testCreateContextual_noPropertyFormat_returnsDateTimeSerializerBase() throws JsonMappingException {
        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(null, null);

        assertNull(dateTimeSerializerBase._customFormat);
        assertNull(result);
    }

    @Test
    public void test_getSchema_withTimestamp_returnsJsonSchemaNode() {
        Type typeHint = mock(Type.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        JsonNode result = dateTimeSerializerBase.getSchema(serializers, typeHint);

        JsonNode expected = createSchemaNode("number", true);
        assertEquals(expected, result);
    }

    @Test
    public void test_getSchema_withoutTimestamp_returnsJsonSchemaNode() {
        Type typeHint = mock(Type.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        JsonNode result = dateTimeSerializerBase.getSchema(serializers, typeHint);

        JsonNode expected = createSchemaNode("string", true);
        assertEquals(expected, result);
    }

    @Test
    public void test_acceptJsonFormatVisitor_withTimestamp_callsVisitorVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Mockito.when(dateTimeSerializerBase._asTimestamp(provider)).thenReturn(true);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(dateTimeSerializerBase).visitIntFormat(visitor, typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
        Mockito.verify(dateTimeSerializerBase, Mockito.never()).visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
    }

    @Test
    public void test_acceptJsonFormatVisitor_withoutTimestamp_callsVisitorVisitStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Mockito.when(dateTimeSerializerBase._asTimestamp(provider)).thenReturn(false);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(dateTimeSerializerBase).visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
        Mockito.verify(dateTimeSerializerBase, Mockito.never()).visitIntFormat(visitor, typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void test_isEmpty_nullValue_returnsTrue() {
        boolean result = dateTimeSerializerBase.isEmpty(null);

        assertTrue(result);
    }

    @Test
    public void test_isEmpty_zeroTimestamp_returnsTrue() {
        Object value = new Object() {
            @SuppressWarnings("unused")
            public long getTimestamp() {
                return 0L;
            }
        };

        boolean result = dateTimeSerializerBase.isEmpty(value);

        assertTrue(result);
    }

    @Test
    public void test_isEmpty_nonZeroTimestamp_returnsFalse() {
        Object value = new Object() {
            @SuppressWarnings("unused")
            public long getTimestamp() {
                return 1L;
            }
        };

        boolean result = dateTimeSerializerBase.isEmpty(value);

        assertFalse(result);
    }

    @Test
    public void test_isEmpty_providerAndNullValue_returnsTrue() {
        SerializerProvider provider = mock(SerializerProvider.class);
        boolean result = dateTimeSerializerBase.isEmpty(provider, null);

        assertTrue(result);
    }

    @Test
    public void test_isEmpty_providerAndZeroTimestamp_returnsTrue() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object() {
            @SuppressWarnings("unused")
            public long getTimestamp() {
                return 0L;
            }
        };

        boolean result = dateTimeSerializerBase.isEmpty(provider, value);

        assertTrue(result);
    }

    @Test
    public void test_isEmpty_providerAndNonZeroTimestamp_returnsFalse() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object() {
            @SuppressWarnings("unused")
            public long getTimestamp() {
                return 1L;
            }
        };

        boolean result = dateTimeSerializerBase.isEmpty(provider, value);

        assertFalse(result);
    }

    private static JsonNode createSchemaNode(String type, boolean nullable) {
        final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("type", type);
        if (!nullable) {
            objectNode.put("optional", !nullable);
        }
        return objectNode;
    }
}