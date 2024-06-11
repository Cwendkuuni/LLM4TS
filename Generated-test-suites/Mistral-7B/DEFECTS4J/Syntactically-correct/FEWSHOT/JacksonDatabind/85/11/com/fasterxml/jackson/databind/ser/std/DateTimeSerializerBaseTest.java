package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    DateTimeSerializerBase<T> serializer;

    public DateTimeSerializerBaseTest() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> jsonSerializer = mapper.findValueSerializer(Object.class);
        JsonFormat.Value format = JsonFormat.value().withShape(JsonFormat.Shape.NUMBER);
        JsonSerializer<Object> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(jsonSerializer, contextualSerializer);

        format = JsonFormat.value().withShape(JsonFormat.Shape.STRING);
        JsonSerializer<Object> formattedSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertNotNull(formattedSerializer);
        assertNotEquals(jsonSerializer, formattedSerializer);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFormatVisitor();
        JsonSerializer<Object> jsonSerializer = mapper.findValueSerializer(Object.class);

        JSONNumberFormat numberFormat = new JSONNumberFormat("number", JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
        JSONStringFormat stringFormat = new JSONStringFormat("string", JsonValueFormat.DATE_TIME);

        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType(), false);
        assertEquals(numberFormat, visitor.getFormat(JsonFormatVisitorWrapper.FORMAT_NUMBER));
        assertEquals(null, visitor.getFormat(JsonFormatVisitorWrapper.FORMAT_STRING));

        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType(), true);
        assertEquals(null, visitor.getFormat(JsonFormatVisitorWrapper.FORMAT_NUMBER));
        assertEquals(stringFormat, visitor.getFormat(JsonFormatVisitorWrapper.FORMAT_STRING));
    }

    @Test
    public void testSerializeWithDefaultFormat() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Instant now = Instant.now();
        Object value = now;
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        String result = gen.getOutputBuffer().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = dateFormat.parse(result);
        assertEquals(now.getEpochSecond(), date.getTime() / 1000L);
    }

    @Test
    public void testSerializeWithCustomFormat() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", locale);
        customFormat.setTimeZone(timeZone);
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, customFormat);

        Instant now = Instant.now();
        Object value = now;
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        String result = gen.getOutputBuffer().toString();
        assertEquals(customFormat.format(Date.from(now)), result);
    }

    @Test
    public void testSerializeWithTimestamp() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Instant now = Instant.now();
        Object value = now.getEpochSecond();
        serializer = new DateTimeSerializerBase<Object>(Object.class, true, null);
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        String result = gen.getOutputBuffer().toString();
        assertEquals(result, String.valueOf(value));
    }
}