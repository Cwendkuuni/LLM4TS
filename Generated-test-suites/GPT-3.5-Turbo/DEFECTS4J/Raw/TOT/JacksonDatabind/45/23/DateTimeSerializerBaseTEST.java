import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        serializerProvider = mock(SerializerProvider.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        when(serializerProvider.getLocale()).thenReturn(Locale.getDefault());
        when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getDefault());
    }

    @Test
    public void testWithFormat() {
        DateFormat customFormat = mock(DateFormat.class);
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, customFormat) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        DateTimeSerializerBase<?> newSerializer = serializer.withFormat(false, customFormat);

        assertEquals(false, newSerializer._useTimestamp);
        assertEquals(customFormat, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        BeanProperty property = null;
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonSerializer<?> newSerializer = serializer.createContextual(serializerProvider, property);

        assertEquals(serializer, newSerializer);
    }

    @Test
    public void testCreateContextualWithNonNullPropertyAndNullFormat() {
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(serializerProvider.getAnnotationIntrospector().findFormat(annotated)).thenReturn(null);

        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonSerializer<?> newSerializer = serializer.createContextual(serializerProvider, property);

        assertEquals(serializer, newSerializer);
    }

    @Test
    public void testCreateContextualWithNumericFormat() {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(serializerProvider.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);

        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonSerializer<?> newSerializer = serializer.createContextual(serializerProvider, property);

        assertEquals(true, newSerializer._useTimestamp);
        assertEquals(null, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithStringFormat() {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.getLocale()).thenReturn(Locale.getDefault());
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        when(serializerProvider.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);

        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonSerializer<?> newSerializer = serializer.createContextual(serializerProvider, property);

        assertEquals(false, newSerializer._useTimestamp);
        assertEquals(format.getTimeZone(), ((SimpleDateFormat) newSerializer._customFormat).getTimeZone());
        assertEquals(format.getPattern(), ((SimpleDateFormat) newSerializer._customFormat).toPattern());
    }

    // Add more test cases for other methods in DateTimeSerializerBase

}