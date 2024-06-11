package com.example.junit;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    // Test methods for constructor

    @Test
    public void testDateTimeSerializerBaseConstructorWithUseTimestampAndCustomFormatNull() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        assertNull(dateTimeSerializerBase._customFormat);
        assertFalse(dateTimeSerializerBase._useTimestamp);
    }

    @Test
    public void testDateTimeSerializerBaseConstructorWithUseTimestampTrueAndCustomFormatNull() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, true, null);
        assertTrue(dateTimeSerializerBase._useTimestamp);
        assertNull(dateTimeSerializerBase._customFormat);
    }

    @Test
    public void testDateTimeSerializerBaseConstructorWithUseTimestampFalseAndCustomFormatInstance() {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(timeZone);
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        assertNotNull(dateTimeSerializerBase._customFormat);
        assertFalse(dateTimeSerializerBase._useTimestamp);
    }

    // Test methods for abstract methods

    @Test(expected = AssertionError.class)
    public void testAbstractMethodSerializeThrowsError() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        dateTimeSerializerBase.serialize(new Date(), new JsonGenerator(), null);
    }

    // Test methods for public methods

    @Test
    public void testIsEmptyMethodReturnsFalse() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        assertFalse(dateTimeSerializerBase.isEmpty(null, new Date()));
    }

    @Test
    public void testGetSchemaMethodReturnsExpectedJsonNode() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonNode schema = dateTimeSerializerBase.getSchema(null, null);
        assertEquals("string", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() {
        SerializerProvider serializers = new SerializerProvider();
        serializers.setSerializerInstance(DateTimeSerializerBase.class);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, ObjectType.valueOf("java.util.Date"));

        assertTrue(visitor.hasSerializable());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getSerializableFormat());
    }

    // Test methods for helper methods

    @Test
    public void testAsTimestampHelperMethodReturnsFalse() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        assertFalse(dateTimeSerializerBase._asTimestamp(null));
    }

    @Test
    public void testAsTimestampHelperMethodReturnsTrue() {
        SerializerProvider serializers = new SerializerProvider();
        serializers.setSerializationConfig(new SerializerConfig());
        serializers.getSerializationConfig().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        assertTrue(dateTimeSerializerBase._asTimestamp(serializers));
    }

    // Test methods for _acceptJsonFormatVisitor method

    @Test
    public void testAcceptJsonFormatVisitorMethodWithAsNumberTrue() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);

        dateTimeSerializerBase._acceptJsonFormatVisitor(visitor, ObjectType.valueOf("java.util.Date"), true);

        assertTrue(visitor.hasSerializable());
        assertEquals(JsonParser.NumberType.LONG, visitor.getSerializableFormat().getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getSerializableFormat().getFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorMethodWithAsNumberFalse() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);

        dateTimeSerializerBase._acceptJsonFormatVisitor(visitor, ObjectType.valueOf("java.util.Date"), false);

        assertTrue(visitor.hasSerializable());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getSerializableFormat());
    }

    // Test methods for _serializeAsString method

    @Test
    public void testSerializeAsStringMethodWithNullFormat() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());

        try {
            dateTimeSerializerBase._serializeAsString(new Date(), generator, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null DateFormat passed for serialization. Cannot serialize to String format.", e.getMessage());
        }
    }

    @Test
    public void testSerializeAsStringMethodWithValidFormat() throws ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(timeZone);
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());

        Date date = customFormat.parse("25-12-2021 15:30:45");
        dateTimeSerializerBase._serializeAsString(date, generator, null);
        String serializedDate = generator.getOutputString();

        assertEquals("25-12-2021 15:30:45", serializedDate);
    }

    // Test methods for withFormat method

    @Test
    public void testWithFormatMethodWithUseTimestampTrueAndCustomFormatNull() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, true, null);
        DateTimeSerializerBase<Date> withTimestampOnlySerializer = dateTimeSerializerBase.withFormat(true, null);

        assertTrue(withTimestampOnlySerializer._useTimestamp);
        assertNull(withTimestampOnlySerializer._customFormat);
    }

    @Test
    public void testWithFormatMethodWithUseTimestampFalseAndCustomFormatInstance() {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(timeZone);
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
        DateTimeSerializerBase<Date> withCustomFormatSerializer = dateTimeSerializerBase.withFormat(false, customFormat);

        assertFalse(withCustomFormatSerializer._useTimestamp);
        assertNotNull(withCustomFormatSerializer._customFormat);
    }
}