package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<LocalDateTime> dateTimeSerializer;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        dateTimeSerializer = new DateTimeSerializerBase<LocalDateTime>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
        };
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDefaultInstance() {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>(
                LocalDateTime.class, false, null);
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testWithFormat() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer.withFormat(false, dateFormat);
        assertEquals(dateFormat, serializer._customFormat);
        assertFalse(serializer._useTimestamp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer.withFormat(false, null);
        serializer.createContextual(null, null);
    }

    @Test
    public void testSerializerCreationWithFormat() throws JsonProcessingException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer.withFormat(false, dateFormat);
        JsonSerializer<?> jsonSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertNotNull(jsonSerializer);
        assertTrue(jsonSerializer instanceof DateTimeSerializerBase);
        assertEquals(dateFormat, ((DateTimeSerializerBase<?>) jsonSerializer)._customFormat);
        assertFalse(((DateTimeSerializerBase<?>) jsonSerializer)._useTimestamp);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullValue() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer;
        serializer.serialize(null, generator, mapper.getSerializerProvider());
    }

    @Test
    public void testSerializationWithEmptyValue() throws JsonGenerationException, IOException {
        LocalDateTime emptyValue = null;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer;
        serializer.serialize(emptyValue, generator, mapper.getSerializerProvider());
        assertNull(generator.getValueAsString());
    }

    @Test
    public void testSerializationWithValue() throws JsonGenerationException, IOException {
        LocalDateTime value = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer;
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        String result = generator.getValueAsString();
        String expected = "2022-01-01T12:34:56";
        assertEquals(expected, result);
    }

    @Test
    public void testSerializationWithValueWithTimeZone() throws JsonGenerationException, IOException {
        LocalDateTime value = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        mapper.setTimeZone(timeZone);
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer;
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        String result = generator.getValueAsString();
        String expected = "2022-01-01T12:34:56Z";
        assertEquals(expected, result);
    }

    @Test
    public void testSerializationWithValueWithLocale() throws JsonGenerationException, IOException {
        LocalDateTime value = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
        Locale locale = Locale.US;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        mapper.setLocale(locale);
        DateTimeSerializerBase<LocalDateTime> serializer = dateTimeSerializer;
        serializer.serialize(value, generator, mapper.getSerializerProvider());
        String result = generator.getValueAsString();
        String expected = "2022-01-01T12:34:56-05:00";
        assertEquals(expected, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializationWithNullInput() throws IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader(""));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        deserializer.deserialize(parser, null, null);
    }

    @Test
    public void testDeserializationWithEmptyString() throws IOException, ParseException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader(""));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        LocalDateTime result = deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
        assertNull(result);
    }

    @Test
    public void testDeserializationWithValidString() throws IOException, ParseException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("2022-01-01T12:34:56"));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        LocalDateTime result = deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
        LocalDateTime expected = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationWithValidStringWithTimeZone() throws IOException, ParseException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("2022-01-01T12:34:56Z"));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        LocalDateTime result = deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
        LocalDateTime expected = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationWithValidStringWithLocale() throws IOException, ParseException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("2022-01-01T12:34:56-05:00"));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        LocalDateTime result = deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
        LocalDateTime expected = LocalDateTime.of(2022, 1, 1, 12, 34, 56);
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidString() throws IOException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("InvalidFormat"));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
    }

    @Test
    public void testDeserializationWithInvalidStringWithTimeZone() throws IOException, ParseException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("InvalidFormatZ"));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        LocalDateTime result = deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
        assertNull(result);
    }

    @Test
    public void testDeserializationWithInvalidStringWithLocale() throws IOException, ParseException {
        JsonParser parser = mapper.getFactory().createParser(new StringReader("InvalidFormat-05:00"));
        DateTimeSerializerBase<LocalDateTime> deserializer = dateTimeSerializer;
        LocalDateTime result = deserializer.deserialize(parser, Calendar.class, new TypeSerializer());
        assertNull(result);
    }
}