package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, null, null);
        assertFalse(serializer._useTimestamp.booleanValue());
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializerBase<>(Date.class, true, null);
        assertTrue(timestampSerializer._useTimestamp.booleanValue());
        assertNull(timestampSerializer._customFormat);
        assertNull(timestampSerializer._reusedCustomFormat);

        // Test constructor with useTimestamp and customFormat
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        DateTimeSerializerBase<Date> customFormatSerializer = new DateTimeSerializerBase<>(Date.class, true, customFormat);
        assertTrue(customFormatSerializer._useTimestamp.booleanValue());
        assertEquals(customFormat, customFormatSerializer._customFormat);
        assertNull(customFormatSerializer._reusedCustomFormat);

        // Test constructor with null useTimestamp and customFormat
        DateTimeSerializerBase<Date> customFormatSerializer2 = new DateTimeSerializerBase<>(Date.class, null, customFormat);
        assertFalse(customFormatSerializer2._useTimestamp.booleanValue());
        assertEquals(customFormat, customFormatSerializer2._customFormat);
        assertNull(customFormatSerializer2._reusedCustomFormat);

        // Test constructor with useTimestamp, customFormat and reusedCustomFormat
        DateTimeSerializerBase<Date> reusedCustomFormatSerializer = new DateTimeSerializerBase<>(Date.class, true, customFormat);
        AtomicReference<DateFormat> atomicReference = new AtomicReference<>();
        atomicReference.set(reusedCustomFormatSerializer._reusedCustomFormat.getAndSet(null));
        assertEquals(customFormat, atomicReference.get());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Test createContextual with null property
        JsonSerializer<?> serializer1 = new DateTimeSerializerBase<>(Date.class, true, null).createContextual(null, null);
        assertTrue(serializer1 instanceof DateTimeSerializerBase);

        // Test createContextual with property having no format overrides
        JsonFormat.Shape shape1 = JsonFormat.Shape.STRING;
        JsonSerializer<?> serializer2 = new DateTimeSerializerBase<>(Date.class, true, null).createContextual(null, new Object() {
            @JsonFormat(shape = shape1)
            private Date date;
        }.getClass().getDeclaredField("date"));
        assertTrue(serializer2 instanceof DateTimeSerializerBase);

        // Test createContextual with property having numeric format override
        JsonFormat.Shape shape2 = JsonFormat.Shape.NUMBER;
        JsonSerializer<?> serializer3 = new DateTimeSerializerBase<>(Date.class, true, null).createContextual(null, new Object() {
            @JsonFormat(shape = shape2)
            private Date date;
        }.getClass().getDeclaredField("date"));
        assertTrue(serializer3 instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase) serializer3)._useTimestamp.booleanValue());

        // Test createContextual with property having custom format override
        JsonFormat.Shape shape3 = JsonFormat.Shape.STRING;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(shape3);
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSS");
        JsonSerializer<?> serializer4 = new DateTimeSerializerBase<>(Date.class, null, null).createContextual(null, new Object() {
            @JsonFormat(value = format)
            private Date date;
        }.getClass().getDeclaredField("date"));
        assertFalse(((DateTimeSerializerBase) serializer4)._useTimestamp.booleanValue());
        assertEquals(format, ((DateTimeSerializerBase) serializer4)._customFormat);
        assertNull(((DateTimeSerializerBase) serializer4)._reusedCustomFormat);

        // Test createContextual with property having custom format override and reused instance
        JsonFormat.Shape shape4 = JsonFormat.Shape.STRING;
        JsonFormat.Value format2 = new JsonFormat.Value();
        format2.setShape(shape4);
        format2.setPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTimeSerializerBase<Date> serializer5 = new DateTimeSerializerBase<>(Date.class, null, null);
        AtomicReference<DateFormat> reusedFormat = new AtomicReference<>();
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        customFormat.setLenient(false);
        reusedFormat.set(customFormat);
        DateFormat reusedCustomFormat = serializer5._reusedCustomFormat.getAndSet(null);
        assertEquals(customFormat, reusedCustomFormat);
        JsonSerializer<?> serializer6 = serializer5.createContextual(null, new Object() {
            @JsonFormat(value = format2)
            private Date date;
        }.getClass().getDeclaredField("date"));
        assertFalse(((DateTimeSerializerBase) serializer6)._useTimestamp.booleanValue());
        assertEquals(customFormat, ((DateTimeSerializerBase) serializer6)._customFormat);
        assertEquals(reusedFormat.get(), ((DateTimeSerializerBase) serializer6)._reusedCustomFormat.get());
    }

    @Test
    public void testSerialize() throws JsonMappingException, IOException {
        // Test serialization with useTimestamp=true
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializerBase<>(Date.class, true, null);
        JsonSerializer<Date> serializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
        serializer = serializer instanceof DateTimeSerializerBase ? (DateTimeSerializerBase<Date>) serializer : timestampSerializer;
        ObjectNode node = mapper.createObjectNode();
        node.put("date", new Date());
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, node);
        JsonParser parser = mapper.getFactory().createParser(new StringReader(writer.toString()));
        JsonToken token = parser.nextToken();
        long timestamp = parser.nextLongValue();
        assertEquals(System.currentTimeMillis(), timestamp);

        // Test serialization with useTimestamp=false and customFormat
        DateTimeSerializerBase<Date> customFormatSerializer = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        node = mapper.createObjectNode();
        node.put("date", new Date());
        writer = new StringWriter();
        mapper.writeValue(writer, node);
        parser = mapper.getFactory().createParser(new StringReader(writer.toString()));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertTrue(parser.getValueAsString().matches("([-\\+]?[\\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{3,}"));
    }
}