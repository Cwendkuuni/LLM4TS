import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, new SimpleDateFormat("yyyy-MM-dd")) {
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

        DateTimeSerializerBase<Object> result = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        assertEquals(false, result._asTimestamp(null));
        assertEquals("yyyy-MM-dd HH:mm:ss", ((SimpleDateFormat) result._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, new SimpleDateFormat("yyyy-MM-dd")) {
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

        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonParser.NumberType numberType = JsonParser.NumberType.LONG;
        JsonValueFormat valueFormat = JsonValueFormat.UTC_MILLISEC;

        serializer.createContextual(serializers, property);
        verify(serializers, times(0)).findFormatOverrides(property, Object.class);
        verify(property, times(0)).getType();
        verify(serializers, times(0)).getTimeZone();
        verify(serializers, times(0)).getLocale();

        when(property.getType()).thenReturn(Object.class);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(serializers.getLocale()).thenReturn(Locale.getDefault());

        serializer.createContextual(serializers, property);
        verify(serializers, times(1)).findFormatOverrides(property, Object.class);
        verify(property, times(1)).getType();
        verify(serializers, times(1)).getTimeZone();
        verify(serializers, times(1)).getLocale();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        when(property.getType()).thenReturn(Object.class);
        when(serializers.findFormatOverrides(property, Object.class)).thenReturn(null);
        when(serializers.getLocale()).thenReturn(Locale.ENGLISH);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        serializer.createContextual(serializers, property);
        verify(serializers, times(2)).findFormatOverrides(property, Object.class);
        verify(property, times(2)).getType();
        verify(serializers, times(2)).getTimeZone();
        verify(serializers, times(2)).getLocale();
        assertEquals(false, serializer._asTimestamp(null));
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat) serializer._customFormat).toPattern());

        serializer.createContextual(serializers, property);
        verify(serializers, times(3)).findFormatOverrides(property, Object.class);
        verify(property, times(3)).getType();
        verify(serializers, times(3)).getTimeZone();
        verify(serializers, times(3)).getLocale();
        assertEquals(false, serializer._asTimestamp(null));
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat) serializer._customFormat).toPattern());

        when(property.getType()).thenReturn(Object.class);
        when(serializers.findFormatOverrides(property, Object.class)).thenReturn(mock(JsonFormat.Value.class));

        serializer.createContextual(serializers, property);
        verify(serializers, times(4)).findFormatOverrides(property, Object.class);
        verify(property, times(4)).getType();
        verify(serializers, times(4)).getTimeZone();
        verify(serializers, times(4)).getLocale();
        assertEquals(false, serializer._asTimestamp(null));
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat) serializer._customFormat).toPattern());

        when(property.getType()).thenReturn(Object.class);
        when(serializers.findFormatOverrides(property, Object.class)).thenReturn(mock(JsonFormat.Value.class));
        when(serializers.isEnabled(MapperFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        serializer.createContextual(serializers, property);
        verify(serializers, times(5)).findFormatOverrides(property, Object.class);
        verify(property, times(5)).getType();
        verify(serializers, times(5)).getTimeZone();
        verify(serializers, times(5)).getLocale();
        assertEquals(true, serializer._asTimestamp(null));
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat) serializer._customFormat).toPattern());

        serializer.createContextual(serializers, property);
        verify(serializers, times(6)).findFormatOverrides(property, Object.class);
        verify(property, times(6)).getType();
        verify(serializers, times(6)).getTimeZone();
        verify(serializers, times(6)).getLocale();
        assertEquals(true, serializer._asTimestamp(null));
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat) serializer._customFormat).toPattern());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, new SimpleDateFormat("yyyy-MM-dd")) {
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

        assertEquals(true, serializer.isEmpty(null));
        assertEquals(true, serializer.isEmpty(mock(SerializerProvider.class), null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, new SimpleDateFormat("yyyy-MM-dd")) {
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

        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode result = serializer.getSchema(serializers, typeHint);

        assertEquals("number", result.get("type").asText());
        assertEquals(true, result.get("format").asBoolean());

        serializer._useTimestamp = false;
        result = serializer.getSchema(serializers, typeHint);

        assertEquals("string", result.get("type").asText());
        assertEquals(true, result.get("format").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, new SimpleDateFormat("yyyy-MM-dd")) {
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

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).getProvider();
        verify(typeHint, times(1)).isDateType();
        verify(visitor, times(1)).expectNumberFormat(typeHint);
        verify(visitor, times(0)).expectStringFormat(typeHint);
        verify(visitor, times(1)).getProvider();
        verify(typeHint, times(1)).isDateType();
        verify(visitor, times(1)).expectNumberFormat(typeHint);
        verify(visitor, times(0)).expectStringFormat(typeHint);

        when(typeHint.isDateType()).thenReturn(true);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(2)).getProvider();
        verify(typeHint, times(2)).isDateType();
        verify(visitor, times(2)).expectNumberFormat(typeHint);
        verify(visitor, times(1)).expectStringFormat(typeHint);

        when(typeHint.isDateType()).thenReturn(false);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(3)).getProvider();
        verify(typeHint, times(3)).isDateType();
        verify(visitor, times(2)).expectNumberFormat(typeHint);
        verify(visitor, times(1)).expectStringFormat(typeHint);
    }

}