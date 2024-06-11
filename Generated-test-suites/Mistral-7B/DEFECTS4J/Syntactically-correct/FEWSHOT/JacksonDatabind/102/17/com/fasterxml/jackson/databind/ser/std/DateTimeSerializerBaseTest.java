package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Date> DATE_SERIALIZER = new DateTimeSerializerBase<Date>(Date.class, false, null);

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> TIMESTAMP_SERIALIZER = new DateTimeSerializerBase<Long>(Long.class, true, null);

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Date> CUSTOM_DATE_FORMAT_SERIALIZER = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"));

    @Test
    public void testDateTimeSerializerBase() {
        assertNotNull(DATE_SERIALIZER);
        assertNotNull(TIMESTAMP_SERIALIZER);
        assertNotNull(CUSTOM_DATE_FORMAT_SERIALIZER);
    }

    @Test
    public void testSerializationWithFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().getGenerator();

        Date date = new Date();
        Long timestamp = date.getTime();

        // Test serialization with default format
        JsonWriter writer = mapper.getSerializerProvider().getWriterFor(Date.class);
        writer.writeValue(new JsonStreamWriter(generator), date);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date), generator.getOutputBuffer().toString());

        // Test serialization with timestamp format
        DateTimeSerializerBase<Long> timestampSerializer = TIMESTAMP_SERIALIZER.withFormat(Boolean.TRUE, null);
        writer = mapper.getSerializerProvider().getWriterFor(Long.class);
        writer.writeValue(new JsonStreamWriter(generator), timestamp);
        assertEquals(Long.toString(timestamp), generator.getOutputBuffer().toString());

        // Test serialization with custom date format
        writer = mapper.getSerializerProvider().getWriterFor(Date.class);
        writer.writeValue(new JsonStreamWriter(generator), date, CUSTOM_DATE_FORMAT_SERIALIZER);
        assertEquals("25-12-2022 03:14:56 PM", generator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializationWithCustomLocalesAndTimeZones() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().getGenerator();

        Date date = new Date();

        // Test serialization with custom locale
        DateTimeSerializerBase<Date> customLocaleSerializer = DATE_SERIALIZER.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.CHINESE));
        JsonWriter writer = mapper.getSerializerProvider().getWriterFor(Date.class);
        writer.writeValue(new JsonStreamWriter(generator), date, customLocaleSerializer);
        assertEquals("25-12-2022 三 下午 03:14:56", generator.getOutputBuffer().toString());

        // Test serialization with custom time zone
        DateTimeSerializerBase<Date> customTimeZoneSerializer = DATE_SERIALIZER.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH), TimeZone.getTimeZone("Asia/Calcutta"));
        writer = mapper.getSerializerProvider().getWriterFor(Date.class);
        writer.writeValue(new JsonStreamWriter(generator), date, customTimeZoneSerializer);
        assertEquals("25-12-2022 08:44:56 IST", generator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializationWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getPolymorphicSerializerProvider().getGenerator();

        JsonWriter writer = mapper.getSerializerProvider().getWriterFor(Date.class);
        writer.writeNull(new JsonStreamWriter(generator));
        assertNull(generator.getOutputBuffer().toString());
    }

    @Test
    public void testDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"2022-12-25T03:14:56.123Z\"");
        JsonDeserializer<Date> dateDeserializer = DATE_SERIALIZER;

        Date date = mapper.readValue(parser, Date.class, dateDeserializer);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2022-12-25T03:14:56.123Z"), date);

        // Test deserialization with custom date format
        JsonParser customDateFormatParser = mapper.getFactory().createParser("\"25-12-2022 03:14:56 PM\"");
        JsonDeserializer<Date> customDateFormatDeserializer = CUSTOM_DATE_FORMAT_SERIALIZER;
        Date customDateFormatDate = mapper.readValue(customDateFormatParser, Date.class, customDateFormatDeserializer);
        assertEquals(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").parse("25-12-2022 03:14:56 PM"), customDateFormatDate);
    }

    @Test
    public void testDeserializationWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        JsonDeserializer<Date> dateDeserializer = DATE_SERIALIZER;

        Date date = mapper.readValue(parser, Date.class, dateDeserializer);
        assertNull(date);
    }

    @Test
    public void testDeserializationWithInvalidFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"invalid\"");
        JsonDeserializer<Date> dateDeserializer = DATE_SERIALIZER;

        try {
            mapper.readValue(parser, Date.class, dateDeserializer);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Can not instantiate value of type [simple type, class java.util.Date] from JSON String \"invalid\": Unrecognized token 'i': was expecting ('null', 'false')", e.getMessage());
        }
    }
}