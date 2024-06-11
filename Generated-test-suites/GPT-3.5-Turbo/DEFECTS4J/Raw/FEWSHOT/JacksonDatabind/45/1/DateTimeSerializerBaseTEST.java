
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private JsonGenerator mockJsonGenerator;

    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSerialize_AsTimestamp() throws IOException {
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // test code
            }
        };

        dateTimeSerializer.serialize(new Date(), mockJsonGenerator, mockSerializerProvider);

        verify(mockSerializerProvider).isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        verify(mockSerializerProvider, never()).getTimeZone();
    }

    @Test
    public void testSerialize_AsString_DefaultFormat() throws IOException {
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // test code
            }
        };

        dateTimeSerializer.serialize(new Date(), mockJsonGenerator, mockSerializerProvider);

        verify(mockSerializerProvider).isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        verify(mockSerializerProvider).getLocale();
        verify(mockSerializerProvider).getTimeZone();
    }

    @Test
    public void testSerialize_AsString_CustomFormat() throws IOException {
        final String pattern = "yyyy-MM-dd";
        final Locale locale = Locale.ENGLISH;
        final TimeZone timeZone = TimeZone.getTimeZone("UTC");
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        dateFormat.setTimeZone(timeZone);

        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, dateFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0L;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // test code
            }
        };

        dateTimeSerializer.serialize(new Date(), mockJsonGenerator, mockSerializerProvider);

        verify(mockSerializerProvider).isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        verify(mockSerializerProvider, never()).getLocale();
        verify(mockSerializerProvider).getTimeZone();
    }
}
