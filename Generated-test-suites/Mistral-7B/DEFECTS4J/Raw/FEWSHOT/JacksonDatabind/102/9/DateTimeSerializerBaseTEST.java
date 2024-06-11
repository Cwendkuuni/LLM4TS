package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertNotNull(dateTimeSerializerBase);

        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithFormat = dateTimeSerializerBase.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
        assertNotNull(dateTimeSerializerBaseWithFormat);
        assertEquals(dateTimeSerializerBase, dateTimeSerializerBaseWithFormat.withFormat(false, null));
        assertNotEquals(dateTimeSerializerBase, dateTimeSerializerBaseWithFormat.withFormat(false, new SimpleDateFormat("dd-MM-yyyy")));

        assertNull(dateTimeSerializerBase._customFormat);
        assertNull(dateTimeSerializerBase._reusedCustomFormat);
        assertFalse(dateTimeSerializerBase._useTimestamp);

        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithUseTimestamp = new DateTimeSerializerBase<Date>(Date.class, true, null);
        assertTrue(dateTimeSerializerBaseWithUseTimestamp._useTimestamp);
        assertNull(dateTimeSerializerBaseWithUseTimestamp._customFormat);
        assertNull(dateTimeSerializerBaseWithUseTimestamp._reusedCustomFormat);

        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithCustomFormat = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
        assertTrue(dateTimeSerializerBaseWithCustomFormat._customFormat != null);
        assertNull(dateTimeSerializerBaseWithCustomFormat._reusedCustomFormat);

        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithReusedCustomFormat = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
        assertTrue(dateTimeSerializerBaseWithReusedCustomFormat._customFormat != null);
        assertNotNull(dateTimeSerializerBaseWithReusedCustomFormat._reusedCustomFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, null));
        objectMapper.registerModule(module);

        JsonSerializer<Date> serializer = objectMapper.findValueSerializer(Date.class);
        JsonSerializer<Date> serializerWithFormat = objectMapper.findValueSerializer(Date.class, new JsonFormat.Value() {
            {
                setShape(JsonFormat.Shape.STRING);
                setPattern("dd-MM-yyyy");
                setLocale(Locale.ENGLISH);
            }
        });

        assertEquals(serializer, serializer.withFormat(false, null));
        assertNotEquals(serializer, serializerWithFormat);

        JsonParser jsonParser = objectMapper.getFactory().createParser("2023-03-23");
        JsonNode rootNode = objectMapper.readValue(jsonParser, JsonNode.class);
        Date date = objectMapper.readValue(jsonParser, Date.class);
        assertEquals(date, rootNode.get("date"));

        JsonSerializer<Date> serializerWithFormatNull = objectMapper.findValueSerializer(Date.class);
        assertEquals(serializer, serializerWithFormatNull);

        JsonSerializer<Date> serializerWithFormatLocal = objectMapper.findValueSerializer(Date.class, new JsonFormat.Value() {
            {
                setShape(JsonFormat.Shape.STRING);
                setPattern("dd-MM-yyyy");
                setLocale(Locale.FRENCH);
            }
        });
        assertNotEquals(serializer, serializerWithFormatLocal);

        JsonSerializer<Date> serializerWithFormatTimeZone = objectMapper.findValueSerializer(Date.class, new JsonFormat.Value() {
            {
                setShape(JsonFormat.Shape.STRING);
                setPattern("dd-MM-yyyy HH:mm:ss z");
                setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
            }
        });
        assertNotEquals(serializer, serializerWithFormatTimeZone);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertFalse(dateTimeSerializerBase.isEmpty(null, null));

        DateTimeSerializerBase<Long> longSerializerBase = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertFalse(longSerializerBase.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, null));
        objectMapper.registerModule(module);

        JsonSerializer<Date> serializer = objectMapper.findValueSerializer(Date.class);
        JsonNode schemaNode = serializer.getSchema(null, null);
        assertEquals("number", schemaNode.getType());

        JsonSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        schemaNode = stringSerializer.getSchema(null, null);
        assertEquals("string", schemaNode.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null, null);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, null);

        assertTrue(visitor.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertFalse(visitor.getFormat().hasPattern());
        assertFalse(visitor.getFormat().hasLocale());
        assertFalse(visitor.getFormat().hasTimeZone());

        JsonFormatVisitorWrapper visitorWithFormat = new JsonFormatVisitorWrapper(null, null, null, null, null, new JsonFormat.Value() {
            {
                setShape(JsonFormat.Shape.STRING);
                setPattern("dd-MM-yyyy");
                setLocale(Locale.ENGLISH);
            }
        });

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitorWithFormat, null);

        assertFalse(visitorWithFormat.getProvider().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertTrue(visitorWithFormat.getFormat().hasPattern());
        assertTrue(visitorWithFormat.getFormat().hasLocale());
        assertFalse(visitorWithFormat.getFormat().hasTimeZone());
    }

    @Test
    public void testSerialize() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, null));
        objectMapper.registerModule(module);

        JsonSerializer<Date> serializer = objectMapper.findValueSerializer(Date.class);

        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(date, jsonGenerator, null);
        byte[] serializedData = jsonGenerator.getOutputBytes();
        String serializedDataString = new String(serializedData);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date deserializedDate = dateFormat.parse(serializedDataString);
        assertEquals(date, deserializedDate);

        JsonSerializer<Date> serializerWithFormat = objectMapper.findValueSerializer(Date.class, new JsonFormat.Value() {
            {
                setShape(JsonFormat.Shape.STRING);
                setPattern("dd-MM-yyyy");
                setLocale(Locale.ENGLISH);
            }
        });

        jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializerWithFormat.serialize(date, jsonGenerator, null);
        serializedData = jsonGenerator.getOutputBytes();
        serializedDataString = new String(serializedData);
        assertEquals("23-03-2023", serializedDataString);
    }

    @Test
    public void testSerializeAsString() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        dateTimeSerializerBase._serializeAsString(date, jsonGenerator, null);
        byte[] serializedData = jsonGenerator.getOutputBytes();
        String serializedDataString = new String(serializedData);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date deserializedDate = dateFormat.parse(serializedDataString);
        assertEquals(date, deserializedDate);

        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        AtomicReference<DateFormat> reusedCustomFormat = new AtomicReference<>();
        DateFormat formatter = reusedCustomFormat.getAndSet(null);
        if (formatter == null) {
            formatter = (DateFormat) dateTimeSerializerBase._customFormat.clone();
        }
        jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        dateTimeSerializerBase._serializeAsString(date, jsonGenerator, null);
        serializedData = jsonGenerator.getOutputBytes();
        serializedDataString = new String(serializedData);
        assertEquals("23-03-2023 15:30:45 EDT", serializedDataString);
    }
}