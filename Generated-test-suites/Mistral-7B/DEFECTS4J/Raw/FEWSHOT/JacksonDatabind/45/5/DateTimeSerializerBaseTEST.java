package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        Class<Date> dateType = Date.class;

        // Test with default settings
        DateTimeSerializerBase<Date> serializer1 = new DateTimeSerializerBase<>(dateType, Boolean.FALSE, null);
        JsonSerializer<Date> serializer2 = serializer1.createContextual(null, null);

        Date date = new Date();

        JsonParser parser = new JsonFactory().getParser();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);

        serializer2.serialize(date, generator, null);
        generator.flush();
        parser.close();
        generator.close();

        // Test with custom format as String
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> serializer3 = new DateTimeSerializerBase<>(dateType, Boolean.FALSE, customFormat);
        JsonSerializer<Date> serializer4 = serializer3.createContextual(null, null);

        parser = new JsonFactory().getParser(new StringReader("2023-03-24 12:30:45.678Z"));
        generator = new JsonFactory().createGenerator(System.out);

        JsonToken expectedToken = JsonToken.VALUE_STRING;
        JsonToken actualToken = parser.nextToken();
        assertEquals(expectedToken, actualToken);

        String expectedValue = parser.getValueAsString();
        serializer4.serialize(date, generator, null);
        String actualValue = generator.getOutputBuffer().toString();

        assertEquals(expectedValue, actualValue);
        parser.close();
        generator.close();

        // Test with custom format as numeric timestamp
        DateTimeSerializerBase<Date> serializer5 = new DateTimeSerializerBase<>(dateType, Boolean.TRUE, null);
        JsonSerializer<Date> serializer6 = serializer5.createContextual(null, null);

        parser = new JsonFactory().getParser(new StringReader("1679459245000"));
        generator = new JsonFactory().createGenerator(System.out);

        expectedToken = JsonToken.VALUE_NUMBER_INT;
        actualToken = parser.nextToken();
        assertEquals(expectedToken, actualToken);

        long expectedNumericValue = date.getTime();
        long actualNumericValue = parser.getValueAsLong();

        assertEquals(expectedNumericValue, actualNumericValue);
        parser.close();
        generator.close();
    }
}