
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBaseTest.DateTimeSerializerBaseMock serializer;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBaseTest.DateTimeSerializerBaseMock(DateTime.class, true, DATE_FORMAT);
    }

    @Test
    public void testEmpty() {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertTrue(serializer.isEmpty(provider, null));
        assertFalse(serializer.isEmpty(provider, new DateTime()));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertNotNull(serializer.getSchema(provider, DateTime.class));
    }

    @Test
    public void testSerializeAsString() throws ParseException, IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        DateTime dateTime = new DateTime(DATE_FORMAT.parse("2021-01-01T00:00:00.000Z"));
        serializer.serialize(dateTime, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals("\"2021-01-01T00:00:00.000Z\"", jsonGenerator.toString());
    }

    private static class DateTimeSerializerBaseMock extends DateTimeSerializerBase<DateTime> {

        public DateTimeSerializerBaseMock(Class<DateTime> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        public boolean isEmpty(SerializerProvider serializers, DateTime value) {
            return false;
        }

        @Override
        protected long _timestamp(DateTime value) {
            return 0;
        }

        @Override
        public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

        }
    }

    private static class DateTime {

        private Date date;

        public DateTime(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }
    }
}
