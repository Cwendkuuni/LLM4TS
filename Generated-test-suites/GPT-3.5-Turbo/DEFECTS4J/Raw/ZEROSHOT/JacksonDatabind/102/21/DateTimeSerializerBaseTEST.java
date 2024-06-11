
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    private DateTimeSerializerBase<TestDate> serializer;

    private DateFormat customFormat;

    private AtomicReference<DateFormat> reusedCustomFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        reusedCustomFormat = new AtomicReference<>();
        serializer = new DateTimeSerializerBase<TestDate>(TestDate.class, true, customFormat) {
            @Override
            public DateTimeSerializerBase<TestDate> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestDate value) {
                return 0;
            }

            @Override
            public void serialize(TestDate value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };
        serializer._reusedCustomFormat = reusedCustomFormat;
    }

    @Test
    public void testSerializeCustomFormat() throws Exception {
        Date date = new Date();
        serializer._serializeAsString(date, jsonGenerator, serializerProvider);

        DateFormat f = reusedCustomFormat.get();
        if (f == null) { // clone format instance if not reused
            f = (DateFormat) customFormat.clone();
        }
        
        verify(jsonGenerator).writeString(f.format(date));
    }

    private static class TestDate extends Date {
    }
}