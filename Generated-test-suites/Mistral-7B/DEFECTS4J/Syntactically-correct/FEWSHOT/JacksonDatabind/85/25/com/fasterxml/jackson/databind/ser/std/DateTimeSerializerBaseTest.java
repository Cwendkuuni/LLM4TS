package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    static DateTimeSerializerBase<Object> createSerializer(Class<T> type, boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            protected long _timestamp(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }
        }.withHandledType(type).withFormat(useTimestamp, customFormat);
    }

    @Test
    public void testCreateContextualSerializer() {
        Object date = new Date(0L);
        Object serializer = createSerializer(Date.class, true, null);
        JsonFormat.Shape shape = JsonFormat.Value.shapeForSerialization(serializers -> serializers.findPropertyMetadata(new String[]{"foo"}));
        JsonSerializer<Object> serializerWithFormat = serializer.createContextual(null, null);
        assertEquals(JsonFormat.Shape.NUMBER, shape);
        assertEquals(serializerWithFormat.getClass(), DateTimeSerializerBase.class);
        assertEquals(Boolean.TRUE, ((DateTimeSerializerBase<Object>) serializerWithFormat)._useTimestamp);
        assertNull(((DateTimeSerializerBase<Object>) serializerWithFormat)._customFormat);

        JsonFormat<Object> jsonFormat = new JsonFormat.Value(JsonFormat.Value.Shape.STRING, null);
        JsonSerializer<Object> serializerWithFormatString = serializer.createContextual(null, new Object() {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            public JsonFormat<Object> getJsonFormat() {
                return jsonFormat;
            }
        });
        assertEquals(JsonFormat.Shape.STRING, shape);
        assertEquals(serializerWithFormatString.getClass(), DateTimeSerializerBase.class);
        assertEquals(Boolean.FALSE, ((DateTimeSerializerBase<Object>) serializerWithFormatString)._useTimestamp);
        assertEquals(jsonFormat, ((DateTimeSerializerBase<Object>) serializerWithFormatString)._customFormat);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Object date = new Date(0L);
        Object serializer = createSerializer(Date.class, true, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new Object() {}, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getFormatName());
        assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getValueFormat());

        Object customSerializer = createSerializer(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));
        serializer = createSerializer(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));
        visitor = new JsonFormatVisitorWrapper(new Object() {}, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("string", visitor.getFormatName());
        assertEquals(JsonParser.NumberType.TEXT, visitor.getNumberType());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getValueFormat());
        assertEquals("dd-MM-yyyy HH:mm:ss", ((SimpleDateFormat) ((DateTimeSerializerBase<Object>) serializer)._customFormat).toPattern());
    }

    @Test
    public void testSerialize() throws IOException {
        Object date = new Date(0L);
        Object serializer = createSerializer(Date.class, true, null);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getDefault());
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        gen.setTimeZone(TimeZone.getDefault());
        serializer.serialize(date, gen, new Object() {});
        assertEquals("0", gen.getOutputBuffer().readLine());
        assertEquals("0\n", gen.getOutputBuffer().readLine());
        assertEquals(0, gen.getOutputBuffer().size());

        gen.reset();
        serializer = createSerializer(Date.class, false, formatter);
        serializer.serialize(date, gen, new Object() {});
        assertEquals("\"24-01-2001 12:00:00\"", gen.getOutputBuffer().readLine());
        assertEquals("\"24-01-2001 12:00:00\"\n", gen.getOutputBuffer().readLine());
        assertEquals(35, gen.getOutputBuffer().size());
    }
}