package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Date> DATE_SERIALIZER = new StdDateSerializer(Date.class);

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> TIMESTAMP_SERIALIZER = new StdSerializableSerializer.DateBasedSerializer<>(Long.TYPE, null);

    @Test
    public void testDateTimeSerializerBaseConstruction() {
        // Test default constructor
        DateTimeSerializerBase<Date> ds1 = new DateTimeSerializerBase<>();
        assertTrue(ds1 instanceof DateTimeSerializerBase);
        assertFalse(ds1._useTimestamp);
        assertNull(ds1._customFormat);
        assertNull(ds1._reusedCustomFormat);

        // Test constructor with useTimestamp=false and customFormat=null
        DateTimeSerializerBase<Date> ds2 = new DateTimeSerializerBase<>(Date.class, false, null);
        assertSame(ds1, ds2);

        // Test constructor with useTimestamp=false and customFormat!=null
        DateTimeSerializerBase<Date> ds3 = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        assertFalse(ds3._useTimestamp);
        assertSame(ds3._customFormat, ds3._reusedCustomFormat.get());

        // Test constructor with useTimestamp=true and customFormat=null
        DateTimeSerializerBase<Date> ds4 = new DateTimeSerializerBase<>(Date.class, true, null);
        assertTrue(ds4._useTimestamp);
        assertNull(ds4._customFormat);
        assertNull(ds4._reusedCustomFormat);

        // Test constructor with useTimestamp=false and customFormat!=null
        DateTimeSerializerBase<Date> ds5 = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z"));
        assertFalse(ds5._useTimestamp);
        assertSame(ds5._customFormat, ds5._reusedCustomFormat.get());
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);

        // Test withFormat(Boolean, null)
        DateTimeSerializerBase<Date> ds1 = ds.withFormat(Boolean.TRUE, null);
        assertTrue(ds1._useTimestamp);
        assertNull(ds1._customFormat);
        assertNull(ds1._reusedCustomFormat);

        // Test withFormat(Boolean, SimpleDateFormat)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        DateTimeSerializerBase<Date> ds2 = ds.withFormat(Boolean.FALSE, sdf);
        assertFalse(ds2._useTimestamp);
        assertSame(sdf, ds2._customFormat);
        assertSame(sdf, ds2._reusedCustomFormat.get());

        // Test withFormat(Boolean, SimpleDateFormat) with reused instance
        DateTimeSerializerBase<Date> ds3 = ds.withFormat(Boolean.FALSE, sdf);
        assertSame(sdf, ds3._customFormat);
        assertSame(sdf, ds3._reusedCustomFormat.get());

        DateTimeSerializerBase<Date> ds4 = ds3.withFormat(Boolean.FALSE, sdf);
        assertSame(sdf, ds4._customFormat);
        assertSame(sdf, ds4._reusedCustomFormat.get());

        // Test withFormat(Boolean, SimpleDateFormat) with new instance
        DateTimeSerializerBase<Date> ds5 = ds3.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotSame(sdf, ds5._customFormat);
        assertNull(ds5._reusedCustomFormat.get());
        assertSame(ds5._customFormat, ds5._reusedCustomFormat.get());

        // Test withFormat(Boolean, DateFormat) with StdDateFormat
        DateTimeSerializerBase<Date> ds6 = ds.withFormat(Boolean.FALSE, new StdDateFormat("%d %b %Y %H:%M:%S %Z"));
        assertFalse(ds6._useTimestamp);
        assertSame(ds6._customFormat, ds6._reusedCustomFormat.get());

        // Test withFormat(Boolean, DateFormat) with custom DateFormat
        DateTimeSerializerBase<Date> ds7 = ds.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.ENGLISH, TimeZone.getTimeZone("UTC")));
        assertFalse(ds7._useTimestamp);
        assertSame(ds7._customFormat, ds7._reusedCustomFormat.get());
    }

    @Test
    public void testSerialize() throws JsonProcessingException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        // Test serialization as numeric timestamp
        Date date1 = new Date(1616153600000L);
        ds.serialize(date1, gen, new JsonFactory());
        gen.flush();
        assertEquals("1616153600000", gen.getOutputBuffer().toString());

        // Test serialization as string with custom format
        ds = new DateTimeSerializerBase<>(Date.class, false, sdf);
        Date date2 = new Date(1616153600000L);
        ds.serialize(date2, gen, new JsonFactory());
        gen.flush();
        assertEquals("2021-03-22 15:00:00.000Z", gen.getOutputBuffer().toString());

        // Test serialization as string with custom format and reused instance
        ds = new DateTimeSerializerBase<>(Date.class, false, sdf);
        Date date3 = new Date(1616153600000L);
        ds.serialize(date3, gen, new JsonFactory());
        gen.flush();
        assertEquals("2021-03-22 15:00:00.000Z", gen.getOutputBuffer().toString());

        ds = new DateTimeSerializerBase<>(Date.class, false, sdf);
        Date date4 = new Date(1616153600001L);
        ds.serialize(date4, gen, new JsonFactory());
        gen.flush();
        assertEquals("2021-03-22 15:00:00.001Z", gen.getOutputBuffer().toString());

        // Test serialization as string with custom format and new instance
        ds = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        Date date5 = new Date(1616153600000L);
        ds.serialize(date5, gen, new JsonFactory());
        gen.flush();
        assertEquals("2021-03-22", gen.getOutputBuffer().toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonSerializerFactory serializers = new JsonSerializerFactory();

        JsonFormatVisitorWrapper visitor = serializers.getConfig().getSchemaFactory()
                .findSchema(Date.class);

        // Test serialization as numeric timestamp
        ds.acceptJsonFormatVisitor(visitor, null);
        assertFalse(visitor.getFormat().hasPattern());

        // Test serialization as string with custom format
        ds = new DateTimeSerializerBase<>(Date.class, false, sdf);
        ds.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.getFormat().hasPattern());
        assertEquals("date-time", visitor.getFormat().getShape().toStringValue());
        assertEquals("yyyy-MM-dd HH:mm:ss.SSS z", visitor.getFormat().getPattern());
    }

    @Test
    public void testSerializeAsString() throws JsonProcessingException, IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        // Test serialization as string with custom format and reused instance
        ds = new DateTimeSerializerBase<>(Date.class, false, sdf);
        Date date = new Date();
        ds._serializeAsString(date, gen, new JsonFactory());
        gen.flush();
        assertEquals("2021-03-22 14:11:16.684+01:00", gen.getOutputBuffer().toString());

        // Test serialization as string with custom format and new instance
        ds = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        Date date2 = new Date();
        ds._serializeAsString(date2, gen, new JsonFactory());
        gen.flush();
        assertEquals("2021-03-22", gen.getOutputBuffer().toString());
    }

    @Test
    public void testWithFormat_Null() {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);
        DateTimeSerializerBase<Date> ds1 = ds.withFormat(Boolean.FALSE, null);
        assertSame(ds, ds1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormat_NullSerializerProvider() throws JsonMappingException {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);
        ds.withFormat(Boolean.FALSE, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testWithFormat_NullTypeHint() throws JsonMappingException {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonSerializerFactory serializerFactory = new JsonSerializerFactory();
        ds.withFormat(Boolean.FALSE, serializerFactory.constructValueSerializer(Long.TYPE));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);
        assertFalse(ds.isEmpty(null, null));
        assertFalse(ds.isEmpty(new JsonFactory(), new Date()));
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);
        assertEquals("number", ds.getSchema(null, null).toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Date> ds = new DateTimeSerializerBase<>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("dateField", Date.class, null, null, null);
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS z";
        Locale locale = Locale.ENGLISH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        // Test serialization as numeric timestamp
        JsonSerializer<Date> serializer = ds.createContextual(serializers, property);
        assertFalse(serializer.getSerializerProperties().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

        // Test serialization as string with custom pattern
        JsonFormat.Value format1 = new JsonFormat.Value();
        format1.setShape(shape);
        format1.setPattern(pattern);
        format1.setLocale(locale);
        format1.setTimeZone(timeZone);
        JsonSerializer<Date> serializer1 = ds.createContextual(serializers, property);
        assertTrue(serializer1.getSerializerProperties().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertEquals(format1, serializer1.getProvider().getBaseSerializer().getFormatOverrides());

        // Test serialization as string with existing StdDateFormat instance
        StdDateFormat stdDateFormat = new StdDateFormat("%d %b %Y %H:%M:%S %Z");
        JsonSerializer<Date> serializer2 = ds.createContextual(serializers, property);
        assertFalse(serializer2.getSerializerProperties().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertEquals(stdDateFormat, serializer2.getProvider().getBaseSerializer().getFormatOverrides());

        // Test serialization as string with custom DateFormat instance
        DateFormat customDateFormat = new SimpleDateFormat(pattern, locale, timeZone);
        JsonSerializer<Date> serializer3 = ds.createContextual(serializers, property);
        assertFalse(serializer3.getSerializerProperties().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertEquals(customDateFormat, serializer3.getProvider().getBaseSerializer().getFormatOverrides());

        // Test serialization as string with StdDateFormat instance and custom locale
        StdDateFormat stdDateFormatLocale = new StdDateFormat("%d %b %Y %H:%M:%S %Z", locale);
        JsonSerializer<Date> serializer4 = ds.createContextual(serializers, property);
        assertFalse(serializer4.getSerializerProperties().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertEquals(stdDateFormatLocale, serializer4.getProvider().getBaseSerializer().getFormatOverrides());

        // Test serialization as string with custom DateFormat instance and custom timezone
        DateFormat customDateFormatTz = new SimpleDateFormat(pattern, locale);
        customDateFormatTz.setTimeZone(timeZone);
        JsonSerializer<Date> serializer5 = ds.createContextual(serializers, property);
        assertFalse(serializer5.getSerializerProperties().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        assertEquals(customDateFormatTz, serializer5.getProvider().getBaseSerializer().getFormatOverrides());
    }
}