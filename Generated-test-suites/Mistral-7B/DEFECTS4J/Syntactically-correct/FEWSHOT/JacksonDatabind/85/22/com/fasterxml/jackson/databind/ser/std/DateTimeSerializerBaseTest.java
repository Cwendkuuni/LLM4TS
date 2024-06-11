package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer.StandardSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonDateFormatVisitor;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseEmpty() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(new Long(0L)));
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, dateFormat) {
            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Long>(Long.TYPE, timestamp, customFormat) {
                    @Override
                    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        if (timestamp) {
                            gen.writeNumber(value.longValue());
                        } else {
                            gen.writeString(customFormat.format(Calendar.getInstance().getTime()));
                        }
                    }
                };
            }
        };

        String json = mapper.writeValueAsString(new Long(System.currentTimeMillis()));
        Long deserialized = mapper.readValue(json, Long.class);
        assertEquals(deserialized.longValue(), System.currentTimeMillis());

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Long.TYPE);
        assertEquals("string", schema.getClass().getSimpleName());

        SerializerProvider provider = mapper.getSerializerProvider();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(provider.getProviderInstance(),
                mapper.getTypeFactory().constructValueType(Long.class, "test"));
        serializer.acceptJsonFormatVisitor(visitor, Long.TYPE);
        JsonDateFormatVisitor formatVisitor = (JsonDateFormatVisitor) visitor.getFormatVisitor();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formatVisitor.getFormat().getPattern());
        assertEquals(JsonValueFormat.DATE_TIME, formatVisitor.getFormat().getShape());
        assertEquals(TimeZone.getTimeZone("UTC"), formatVisitor.getFormat().getTimeZone());
        assertNull(formatVisitor.getFormat().getLocale());
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && actual != null) {
            assertEquals(expected.toString(), actual.toString());
        } else {
            fail();
        }
    }
}