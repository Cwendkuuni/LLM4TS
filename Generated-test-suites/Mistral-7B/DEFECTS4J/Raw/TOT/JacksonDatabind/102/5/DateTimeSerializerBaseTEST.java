import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() throws IOException, ParseException {
        // Test default serialization
        JsonMapper mapper = new JsonMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        Date date = new Date();
        dateTimeSerializerBase.serialize(date, gen, null);
        verifyDateFormat(gen, "2022-03-24T14:11:23.123Z", null);

        // Test custom serialization with format and locale
        JsonFormat jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setLocale(Locale.FRENCH);
        jsonFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        jsonFormat.setPattern("dd-MM-yyyy HH:mm:ss.SSS z");
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DateTimeSerializerBase<Date> customSerializer = mapper.findValueSerializer(Date.class);
        customSerializer = customSerializer.withFormat(Boolean.FALSE, new StdDateFormat(jsonFormat));
        Date dateCustom = new Date();
        JsonParser parser = mapper.getFactory().createParser("\"25-06-2022 15:30:45.678 CEST\"");
        Object readValue = mapper.readValue(parser, Date.class);
        Date expectedDate = dateCustom;
        expectedDate.setTime(parser.nextValue().asLong());
        assertEquals(expectedDate, readValue);
        String serializedCustom = customSerializer.serializeValuesAsStrings(dateCustom, null);
        assertEquals("25-06-2022 15:30:45.678 CEST", serializedCustom);

        // Test custom serialization with format and timezone
        JsonFormat jsonFormatTimezone = new JsonFormat.Value();
        jsonFormatTimezone.setShape(JsonFormat.Shape.STRING);
        jsonFormatTimezone.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DateTimeSerializerBase<Date> customSerializerTimezone = mapper.findValueSerializer(Date.class);
        customSerializerTimezone = customSerializerTimezone.withFormat(Boolean.FALSE, new StdDateFormat(jsonFormatTimezone));
        Date dateTimezone = new Date();
        JsonParser parserTimezone = mapper.getFactory().createParser("\"2022-03-24T14:11:23.123Z\"");
        Object readValueTimezone = mapper.readValue(parserTimezone, Date.class);
        Date expectedDateTimezone = dateTimezone;
        expectedDateTimezone.setTime(parserTimezone.nextValue().asLong());
        assertEquals(expectedDateTimezone, readValueTimezone);
        String serializedTimezone = customSerializerTimezone.serializeValuesAsStrings(dateTimezone, null);
        assertEquals("2022-03-24T14:11:23.123Z", serializedTimezone);

        // Test empty object serialization
        assertNull(dateTimeSerializerBase.serialize(null, gen, null));

        // Test serialization with useTimestamp flag
        DateTimeSerializerBase<Date> useTimestampSerializer = mapper.findValueSerializer(Date.class);
        useTimestampSerializer = useTimestampSerializer.withFormat(Boolean.TRUE, null);
        long timestamp = date.getTime();
        assertEquals(timestamp, useTimestampSerializer.serializeValuesAsLong(date, null));

        // Test serialization with null customFormat
        DateTimeSerializerBase<Date> nullCustomFormatSerializer = mapper.findValueSerializer(Date.class);
        assertNull(nullCustomFormatSerializer._customFormat);
        assertEquals(timestamp, nullCustomFormatSerializer.serializeValuesAsLong(date, null));
    }

    private void verifyDateFormat(JsonGenerator gen, String expectedDateString, DateFormat customFormat) throws IOException {
        if (customFormat != null) {
            assertEquals(customFormat.getClass(), gen.getSerializerProvider().findValueSerializer(DateFormat.class));
            DateFormat formatter = (DateFormat) gen.getSerializerProvider().findValueSerializer(DateFormat.class);
            formatter.setLenient(false);
            try {
                Date date = formatter.parse(expectedDateString);
                assertEquals(expectedDateString, formatter.format(date));
            } catch (ParseException e) {
                fail("Expected parse success but failed: " + e.getMessage());
            }
            assertEquals(expectedDateString, formatter.format(new Date()));
        }
        String serialized = gen.getOutputFactory().writeValueAsString(new Date());
        assertEquals(expectedDateString, serialized);
    }
}