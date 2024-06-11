package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<java.util.Date> dateSerializer;

    @Before
    public void setUp() {
        longSerializer = new StdDateSerializer(Long.TYPE, true, null);
        dateSerializer = new StdDateSerializer(Date.class, false, null);
    }

    @Test
    public void testLongSerializerWithTimestamp() throws JsonGenerationException, IOException {
        Long testValue = 1640995200000L; // Wed, 12 Jan 2022 00:00:00 GMT
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);

        longSerializer.serialize(testValue, jsonGenerator, null);
        jsonGenerator.flush();

        String expected = "1640995200000";
        Assert.assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testLongSerializerWithCustomFormat() throws JsonGenerationException, IOException {
        Long testValue = 1640995200000L;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);

        DateTimeSerializerBase<Long> customSerializer = longSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));
        customSerializer.serialize(testValue, jsonGenerator, null);
        jsonGenerator.flush();

        String expected = "12-01-2022 00:00:00 GMT";
        Assert.assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testDateSerializerWithTimestamp() throws JsonGenerationException, IOException {
        java.util.Date testValue = new GregorianCalendar(2022, 0, 12).getTime();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);

        dateSerializer.serialize(testValue, jsonGenerator, null);
        jsonGenerator.flush();

        String expected = "\"2022-01-12T00:00:00.000Z\"";
        Assert.assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testDateSerializerWithCustomFormat() throws JsonGenerationException, IOException {
        java.util.Date testValue = new GregorianCalendar(2022, 0, 12).getTime();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);

        DateTimeSerializerBase<java.util.Date> customSerializer = dateSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));
        customSerializer.serialize(testValue, jsonGenerator, null);
        jsonGenerator.flush();

        String expected = "\"12-01-2022 00:00:00 GMT\"";
        Assert.assertEquals(expected, stringWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() throws JsonGenerationException, IOException {
        longSerializer.createContextual(null, null);
    }

    @Test
    public void testFindFormatOverrides() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = mapper.findFormatOverrides(null, null, Long.class);
        Assert.assertNull(format);
    }
}