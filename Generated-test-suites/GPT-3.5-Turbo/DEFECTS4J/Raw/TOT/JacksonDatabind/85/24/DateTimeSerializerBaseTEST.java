
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<DateTime> serializer;

    @Test
    public void testIsEmpty_NullValue_ReturnsTrue() {
        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_NotNullValue_ReturnsFalse() {
        DateTime dateTime = new DateTime();
        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        assertFalse(serializer.isEmpty(dateTime));
    }

    @Test
    public void testGetSchema_TimestampFormat_ReturnsSchemaNodeWithNumberFormat() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        JsonNode schema = serializer.getSchema(serializers, DateTime.class);

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_DateTimeFormat_ReturnsSchemaNodeWithStringFormat() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };
        JsonNode schema = serializer.getSchema(serializers, DateTime.class);

        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsTimestamp_VisitsIntFormatWithLongTypeAndUTCFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };
        serializer.acceptJsonFormatVisitor(visitor, DateTime.class);

        verify(visitor, times(1)).visitIntFormat(any(), eq(DateTime.class), eq(JsonParser.NumberType.LONG),
                eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testAcceptJsonFormatVisitor_NotAsTimestamp_VisitsStringFormatWithDateTimeFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        serializer.acceptJsonFormatVisitor(visitor, DateTime.class);

        verify(visitor, times(1)).visitStringFormat(any(), eq(DateTime.class), eq(JsonValueFormat.DATE_TIME));
    }
        
    @Test
    public void testCreateContextual_PropertyIsNull_ReturnsThis() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = null;

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        ContextualSerializer result = serializer.createContextual(serializers, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_PropertyFormatIsNull_ReturnsThis() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.findFormatOverrides(any(), any())).thenReturn(null);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        ContextualSerializer result = serializer.createContextual(serializers, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_PropertyFormatIsNumeric_ReturnsWithFormatTrue() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER);
        when(property.findFormatOverrides(any(), any())).thenReturn(format);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        ContextualSerializer result = serializer.createContextual(serializers, property);

        assertEquals(Boolean.TRUE, result.withFormat(Boolean.TRUE, null)._useTimestamp);
    }

    @Test
    public void testCreateContextual_PropertyFormatHasPattern_ReturnsWithFormatFalseAndSpecifiedPattern() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING).withPattern("yyyy-MM-dd");
        when(property.findFormatOverrides(any(), any())).thenReturn(format);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        ContextualSerializer result = serializer.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, result.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd")));
    }

    @Test
    public void testCreateContextual_PropertyFormatHasTimeZone_ReturnsWithFormatFalseAndSpecifiedTimeZone() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        TimeZone timezone = TimeZone.getTimeZone("PST");
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING).withTimeZone(timezone);
        when(property.findFormatOverrides(any(), any())).thenReturn(format);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        ContextualSerializer result = serializer.createContextual(serializers, property);

        SimpleDateFormat sdf = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601);
        sdf.setTimeZone(timezone);

        assertEquals(Boolean.FALSE, result.withFormat(Boolean.FALSE, sdf));
    }

    @Test
    public void testCreateContextual_PropertyFormatHasLocale_ReturnsWithFormatFalseAndSpecifiedLocale() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Locale locale = Locale.US;
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING).withLocale(locale);
        when(property.findFormatOverrides(any(), any())).thenReturn(format);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };

        ContextualSerializer result = serializer.createContextual(serializers, property);

        SimpleDateFormat sdf = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601, locale);

        assertEquals(Boolean.FALSE, result.withFormat(Boolean.FALSE, sdf));
    }

    @Test
    public void testSerialize_ValueIsNull_DoesNothing() throws IOException {
        DateTime dateTime = null;
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

        };
        serializer.serialize(dateTime, jsonGenerator, serializers);

        verify(jsonGenerator, never()).writeDate(any());
    }

    // Add more tests for other methods

}

