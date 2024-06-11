package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testUseTimestamp() throws Exception {
        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        assertEquals("1633043200000", timestampSerializer.serialize(1633043200000L, new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializerProvider()));
    }

    @Test
    public void testUseCustomFormat() throws Exception {
        DateTimeSerializerBase<String> customFormatSerializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US));
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("\"01-01-2022 12:00:00\"");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);
        assertEquals("2022-01-01T12:00:00.000Z", rootNode.toString());
        assertEquals("01-01-2022 12:00:00", customFormatSerializer.serialize(new Date(0), new JsonGenerator(new ByteArrayOutputStream()), new ObjectMapper().getSerializerProvider()).getValueAsString());
    }

    @Test
    public void testCreateContextualWithNullProvider() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        assertNotNull(serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithFormat() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        JsonFormat formatAnnotation = new JsonFormat();
        formatAnnotation.setShape(JsonFormat.Shape.STRING);
        formatAnnotation.setPattern("dd-MM-yyyy HH:mm:ss");
        JsonSerializer<String> serializerWithFormat = serializer.withFormat(false, formatAnnotation);
        assertNotNull(serializerWithFormat);
        assertEquals("dd-MM-yyyy HH:mm:ss", serializerWithFormat.getClass().getDeclaredField("_customFormat").get(serializerWithFormat).getPattern());
    }

    @Test
    public void testCreateContextualWithTimestamp() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.NUMBER);
        JsonSerializer<String> serializerWithFormat = serializer.withFormat(true, format);
        assertNotNull(serializerWithFormat);
        assertTrue(serializerWithFormat.getClass().isAssignableFrom(DateTimeSerializerBase.class));
        assertFalse(serializerWithFormat.getClass().getDeclaredField("_customFormat").isAccessible());
    }

    @Test
    public void testCreateContextualWithFormatAndCustomFormat() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        JsonSerializer<String> serializerWithFormat = serializer.withFormat(false, format);

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        JsonSerializer<String> serializerWithCustomFormat = serializerWithFormat.withFormat(false, customFormat);
        assertNotNull(serializerWithCustomFormat);
        assertEquals("dd-MM-yyyy HH:mm:ss", serializerWithCustomFormat.getClass().getDeclaredField("_customFormat").get(serializerWithCustomFormat).getPattern());
    }
}