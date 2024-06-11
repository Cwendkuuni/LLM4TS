package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    @SuppressWarnings("rawtypes")
    private static class TestDateTimeSerializer extends DateTimeSerializerBase<Date> {
        TestDateTimeSerializer() {
            super(Date.class, Boolean.FALSE, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializer();
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // override to prevent actual serialization
        }
    }

    @Test
    public void testUseTimestamp() {
        DateTimeSerializerBase<Date> serializer = new TestDateTimeSerializer();
        assertTrue(serializer._asTimestamp(null));

        ObjectMapper mapper = new ObjectMapper();
        SimpleSerializerProvider serializers = new SimpleSerializerProvider();
        serializers.setProviderClass(TestDateTimeSerializer.class);
        mapper.setSerializerProvider(serializers);
        serializers.setSerializationConfig(mapper.getSerializationConfig());
        assertTrue(serializer._asTimestamp(serializers));
        assertFalse(serializers.getSerializationConfig().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Test
    public void testCustomFormat() throws IOException {
        DateTimeSerializerBase<Date> serializer = new TestDateTimeSerializer();

        ObjectMapper mapper = new ObjectMapper();
        SimpleSerializerProvider serializers = new SimpleSerializerProvider();
        serializers.setProviderClass(TestDateTimeSerializer.class);
        mapper.setSerializerProvider(serializers);
        SerializationConfig config = mapper.getSerializationConfig();

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        JsonFormat customJsonFormat = new JsonFormat();
        customJsonFormat.setShape(JsonFormat.Shape.STRING);
        customJsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        customJsonFormat.setLocale(Locale.US);
        customJsonFormat.setPattern(customFormat.toPattern());
        Annotated member = null;
        JsonFormat format = new JsonFormat(customJsonFormat);

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getFormatVisitor(JsonFormatVisitor.Std.DATE_TIME, config, member);
        TestDateTimeSerializer serializerWithFormat = serializer.withFormat(Boolean.FALSE, customFormat);
        serializerWithFormat._acceptJsonFormatVisitor(visitor, null, false);

        JsonNode schema = visitor.getSchema();
        assertEquals("string", schema.getClass().getSimpleName());
        assertEquals("date-time", schema.asText());
    }
}