package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializationTest;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat.DateFormatBehavior;

public class DateTimeSerializerBaseTest extends SerializationTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<Calendar> calendarSerializer;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        longSerializer = new StdDateSerializer(Long.class, null);
        longSerializer = (DateTimeSerializerBase<Long>) longSerializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"));

        calendarSerializer = new StdDateSerializer(Calendar.class, false, null);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException, JsonProcessingException, IOException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss Z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        BeanProperty property = new BeanProperty("test", Calendar.class);
        JsonSerializer<Calendar> serializer = longSerializer.createContextual(null, property);

        assertEquals(Calendar.class, serializer.getClass());
        assertNotNull(serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullSerializerProvider() throws JsonMappingException, JsonProcessingException, IOException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss Z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        BeanProperty property = new BeanProperty("test", Calendar.class);
        JsonSerializer<Calendar> serializer = longSerializer.createContextual(null, property);
    }

    @Test
    public void testSerialization() throws JsonMappingException, JsonProcessingException, IOException {
        Calendar calendar = Calendar.getInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(calendar);

        JsonParser parser = mapper.getFactory().createParser(json);
        Calendar parsedCalendar = parser.readValueAs(Calendar.class);

        assertEquals(calendar.getTimeInMillis(), parsedCalendar.getTimeInMillis());
    }

    @Test
    public void testSerialization_useTimestamp() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        Long timestamp = 1646300800000L; // 2022-01-01 12:00:00 UTC

        String json = mapper.writeValueAsString(timestamp);
        assertEquals("1646300800000", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Long parsedTimestamp = parser.readValueAs(Long.class);
        assertEquals(timestamp, parsedTimestamp);
    }

    @Test
    public void testSerialization_customFormat() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        DateTimeSerializerBase<Long> customSerializer = (DateTimeSerializerBase<Long>) longSerializer.withFormat(false, customFormat);

        Calendar calendar = Calendar.getInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Long timestamp = calendar.getTimeInMillis();
        String json = mapper.writeValueAsString(timestamp);
        assertEquals("2022-01-01 12:30:00 +0000", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Long parsedTimestamp = parser.readValueAs(Long.class);
        assertEquals(timestamp, parsedTimestamp);
    }

    @Test
    public void testSerialization_customFormat_locale() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Long> customSerializer = (DateTimeSerializerBase<Long>) longSerializer.withFormat(false, customFormat);

        Calendar calendar = Calendar.getInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Long timestamp = calendar.getTimeInMillis();
        String json = mapper.writeValueAsString(timestamp);
        assertEquals("01-01-2022 12:30:00 EDT", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Long parsedTimestamp = parser.readValueAs(Long.class);
        assertEquals(timestamp, parsedTimestamp);
    }

    @Test
    public void testSerialization_customFormat_timeZone() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Long> customSerializer = (DateTimeSerializerBase<Long>) longSerializer.withFormat(false, customFormat);

        Calendar calendar = Calendar.getInstance(Locale.US, TimeZone.getTimeZone("Europe/Paris"));
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Long timestamp = calendar.getTimeInMillis();
        String json = mapper.writeValueAsString(timestamp);
        assertEquals("2022-01-01 13:30:00 CET", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Long parsedTimestamp = parser.readValueAs(Long.class);
        assertEquals(timestamp, parsedTimestamp);
    }

    @Test
    public void testEmptyValue() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString((Object) null);
        assertNull(mapper.readValue(json, Long.class));
    }

    @Test
    public void testJsonNodeGetSchema() throws JsonMappingException {
        JsonNode schema = longSerializer.getSchema(null, Long.class);
        assertEquals("string", schema.getClass().getSimpleName());
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss Z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<Long> serializer = (DateTimeSerializerBase<Long>) longSerializer;
        JavaType javaType = mapper.getTypeFactory().constructType(Long.class);

        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(createSerializerProvider(), javaType), javaType);

        assertEquals("string", serializer.acceptIntFormat(new JsonFormatVisitorWrapper(createSerializerProvider(), javaType), javaType, JsonParser.NumberType.LONG, JsonValueFormat.DATE_TIME).getName());
    }

    @Test
    public void testAcceptJsonFormatVisitor_useTimestamp() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss Z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<Long> serializer = (DateTimeSerializerBase<Long>) longSerializer;
        JavaType javaType = mapper.getTypeFactory().constructType(Long.class);

        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(createSerializerProvider(), javaType), javaType);

        assertEquals("number", serializer.acceptIntFormat(new JsonFormatVisitorWrapper(createSerializerProvider(), javaType), javaType, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC).getName());
    }

    @Test
    public void testAcceptJsonFormatVisitor_customFormat() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        DateTimeSerializerBase<Long> serializer = (DateTimeSerializerBase<Long>) longSerializer.withFormat(false, customFormat);

        JavaType javaType = mapper.getTypeFactory().constructType(Long.class);

        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(createSerializerProvider(), javaType), javaType);

        assertEquals("string", serializer.acceptStringFormat(new JsonFormatVisitorWrapper(createSerializerProvider(), javaType), javaType, JsonValueFormat.DATE_TIME).getName());
    }

    private JsonGenerator createSerializerProvider() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createGenerator();
    }
}