package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;
    private JsonSerializer<Date> dateSerializer;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        dateSerializer = mapper.findValueSerializer(Date.class);
    }

    @Test
    public void testSerializeDateAsTimestamp() throws JsonGenerationException, IOException {
        Date date = new Date(System.currentTimeMillis());
        JsonGenerator gen = mapper.getPolymorphicGenerator(Date.class, null);
        dateSerializer.serialize(date, gen, null);
        assertEquals(mapper.writeValueAsBytes(date).toString(), gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDateWithCustomFormat() throws JsonGenerationException, IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        JsonSerializer<Date> customSerializer = dateSerializer.withFormat(Boolean.FALSE, customFormat);
        Date date = new Date(System.currentTimeMillis());
        JsonGenerator gen = mapper.getPolymorphicGenerator(Date.class, null);
        customSerializer.serialize(date, gen, null);
        assertEquals(customFormat.format(date), gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDateWithJsonFormat() throws JsonGenerationException, IOException {
        JsonFormat<Date> jsonFormat = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING)
                .withPattern("dd-MM-yyyy HH:mm:ss z");
        JsonSerializer<Date> jsonSerializer = dateSerializer.withFormat(Boolean.FALSE, jsonFormat);
        Date date = new Date(System.currentTimeMillis());
        JsonGenerator gen = mapper.getPolymorphicGenerator(Date.class, null);
        jsonSerializer.serialize(date, gen, null);
        assertEquals(jsonFormat.getPattern(), gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeNullDate() throws JsonGenerationException, IOException {
        JsonGenerator gen = mapper.getPolymorphicGenerator(Date.class, null);
        dateSerializer.serialize(null, gen, null);
        assertNull(mapper.writeValueAsBytes(null).toString());
        assertEquals("null", gen.getOutputBuffer().toString());
    }

    @Test
    public void testDeserializeDateAsTimestamp() throws IOException, JsonMappingException {
        String json = "1616341111611";
        Date date = mapper.readValue(json, Date.class);
        assertEquals(new Date(1616341111611L), date);
    }

    @Test
    public void testDeserializeDateWithCustomFormat() throws IOException, JsonMappingException, ParseException {
        String json = "25-06-2022 14:30:45 EDT";
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Date date = mapper.readValue(jsonParser, Date.class, customFormat);
        assertEquals(customFormat.parse(json), date);
    }

    @Test
    public void testDeserializeDateWithJsonFormat() throws IOException, JsonMappingException, ParseException {
        String json = "\"25-06-2022 14:30:45 z\"";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonStringFormatVisitor visitor = mapper.findValueSerializer(String.class).acceptJsonFormatVisitor(jsonParser, null);
        JsonFormat<Date> jsonFormat = visitor.getFormat();
        Date date = mapper.readValue(jsonParser, Date.class, jsonFormat);
        assertEquals(jsonFormat.getPattern(), json);
        assertEquals(visitor.getDateValue(), date);
    }

    @Test
    public void testDeserializeNullDate() throws IOException, JsonMappingException {
        String json = "null";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Date date = mapper.readValue(jsonParser, Date.class);
        assertNull(date);
    }
}