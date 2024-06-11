
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(
                Date.class, true, null) {
            @Override
            protected long _timestamp(Date value) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(value);
                return calendar.getTimeInMillis();
            }

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

        };
    }

    @Test
    public void testWithFormat() {
        assertEquals(null, dateTimeSerializerBase.withFormat(true, null));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        dateTimeSerializerBase.serialize(null, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(0)).writeString(Mockito.anyString());
        Mockito.verify(serializerProvider, Mockito.times(0)).defaultSerializeDateValue(null, jsonGenerator);
    }

    @Test
    public void testIsEmpty() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        assertEquals(false, dateTimeSerializerBase.isEmpty(serializerProvider, new Date()));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);
        assertNull(dateTimeSerializerBase.getSchema(serializerProvider, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor, Mockito.times(1)).getProvider();
    }

    @Test
    public void testAsTimestamp() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        assertEquals(true, dateTimeSerializerBase._asTimestamp(serializerProvider));
    }

    @Test
    public void testAcceptJsonFormatVisitorWithNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        boolean asNumber = true;
        dateTimeSerializerBase._acceptJsonFormatVisitor(visitor, typeHint, asNumber);
        Mockito.verify(visitor, Mockito.times(1)).getIntFormat(Mockito.any(JsonParser.NumberType.class), Mockito.any(JsonValueFormat.class));
    }

    @Test
    public void testAcceptJsonFormatVisitorWithString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        boolean asNumber = false;
        dateTimeSerializerBase._acceptJsonFormatVisitor(visitor, typeHint, asNumber);
        Mockito.verify(visitor, Mockito.times(1)).getStringFormat(Mockito.any(JsonValueFormat.class));
    }

    @Test
    public void testSerializeAsStringWithCustomFormat() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat();
        Date value = new Date();
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        DateFormat df = new SimpleDateFormat(customFormat.toPattern(), customFormat.getLocale());
        TimeZone newTz = customFormat.getTimeZone();
        boolean changeTZ = (newTz != null) && !newTz.equals(df.getTimeZone());
        if (changeTZ) {
            df.setTimeZone(newTz);
        }

        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(
                Date.class, false, customFormat) {
            @Override
            protected long _timestamp(Date value) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(value);
                return calendar.getTimeInMillis();
            }

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (get_customFormat() == null) {
                    serializers.defaultSerializeDateValue(value, gen);
                    return;
                }
                gen.writeString(df.format(value));
            }

            public DateFormat get_customFormat() {
                return _customFormat;
            }

            public AtomicReference<DateFormat> get_reusedCustomFormat() {
                return _reusedCustomFormat;
            }

        };

        Mockito.doNothing().when(g).writeString(Mockito.anyString());
        dateTimeSerializerBase._serializeAsString(value, g, provider);

        Mockito.verify(g, Mockito.times(1)).writeString(Mockito.anyString());
    }

    @Test
    public void testSerializeAsStringWithDefaultFormat() throws IOException {
        Date value = new Date();
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Mockito.doNothing().when(g).writeString(Mockito.anyString());
        dateTimeSerializerBase._serializeAsString(value, g, provider);

        Mockito.verify(g, Mockito.times(0)).writeString(Mockito.anyString());
        Mockito.verify(provider, Mockito.times(1)).defaultSerializeDateValue(value, g);
    }

}
