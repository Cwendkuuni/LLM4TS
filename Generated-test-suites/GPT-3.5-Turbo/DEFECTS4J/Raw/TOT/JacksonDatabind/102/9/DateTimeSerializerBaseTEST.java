
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Mock
    private JsonSerializer<Object> defaultSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dateTimeSerializer = new DateTimeSerializer();
    }

    @Test
    public void testSerialize_WithDefaultFormat_ShouldSerializeAsString() throws IOException {
        // Arrange
        Date date = getDate("2020-01-01T00:00:00Z");
        StringWriter stringWriter = new StringWriter();
        when(serializerProvider.getConfig()).thenReturn(new MockSerializationConfig());
        when(serializerProvider.isEnabled(Mockito.any())).thenReturn(false);
        when(jsonGenerator.getOutputTarget()).thenReturn(stringWriter);

        // Act
        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.close();

        // Assert
        assertEquals("\"2020-01-01T00:00:00Z\"", stringWriter.toString());
    }

    @Test
    public void testSerialize_WithUseTimestampTrue_ShouldSerializeAsTimestamp() throws IOException {
        // Arrange
        Date date = getDate("2020-01-01T00:00:00Z");
        StringWriter stringWriter = new StringWriter();
        when(serializerProvider.getConfig()).thenReturn(new MockSerializationConfig());
        when(serializerProvider.isEnabled(Mockito.any())).thenReturn(true);
        when(jsonGenerator.getOutputTarget()).thenReturn(stringWriter);

        // Act
        dateTimeSerializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.close();

        // Assert
        assertEquals("1577836800000", stringWriter.toString());
    }

    @Test
    public void testSerialize_WithCustomFormat_ShouldSerializeUsingCustomFormat() throws IOException {
        // Arrange
        Date date = getDate("2020-01-01T00:00:00Z");
        StringWriter stringWriter = new StringWriter();
        when(serializerProvider.getConfig()).thenReturn(new MockSerializationConfig());
        when(serializerProvider.isEnabled(Mockito.any())).thenReturn(false);
        when(jsonGenerator.getOutputTarget()).thenReturn(stringWriter);

        DateTimeSerializerBase<Date> customDateFormatSerializer = new CustomDateFormatSerializer();

        // Act
        customDateFormatSerializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.close();

        // Assert
        assertEquals("\"2020-01-01T00:00:00\"", stringWriter.toString());
    }

    private Date getDate(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class DateTimeSerializer extends DateTimeSerializerBase<Date> {

        public DateTimeSerializer() {
            super(Date.class, null, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (_asTimestamp(serializers)) {
                gen.writeNumber(value.getTime());
            } else {
                _serializeAsString(value, gen, serializers);
            }
        }
    }

    private class CustomDateFormatSerializer extends DateTimeSerializerBase<Date> {

        private static final String CUSTOM_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

        public CustomDateFormatSerializer() {
            super(Date.class, null, new SimpleDateFormat(CUSTOM_DATE_FORMAT));
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            _serializeAsString(value, gen, serializers);
        }
    }

    private class MockSerializationConfig extends com.fasterxml.jackson.databind.SerializationConfig {

        @Override
        public DateFormat getDateFormat() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat;
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }

        @Override
        public JsonSerializer<?> getSerializerInstance(Class<?> cls, boolean hasConcrete) {
            return defaultSerializer;
        }
    }
}
