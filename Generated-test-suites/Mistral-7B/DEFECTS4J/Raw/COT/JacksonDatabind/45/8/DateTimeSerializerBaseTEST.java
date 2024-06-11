package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for DateTimeSerializerBase
 */
public class DateTimeSerializerBaseTest {

    /**
     * Test method for {@link DateTimeSerializerBase#DateTimeSerializerBase(Class, Boolean, DateFormat)} constructor.
     */
    @Test
    public void testDateTimeSerializerBaseConstructor() {
        // Create a DateTimeSerializerBase instance with default arguments
        DateTimeSerializerBase<Long> dateTimeSerializerBase1 = new DateTimeSerializerBase<Long>(Long.class, null, null);

        // Create a DateTimeSerializerBase instance with useTimestamp set to true
        DateTimeSerializerBase<Long> dateTimeSerializerBase2 = new DateTimeSerializerBase<Long>(Long.class, true, null);

        // Create a DateTimeSerializerBase instance with customFormat
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Long> dateTimeSerializerBase3 = new DateTimeSerializerBase<Long>(Long.class, false, customFormat);
    }

    /**
     * Test method for {@link DateTimeSerializerBase#withFormat(Boolean, DateFormat)} method.
     */
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        DateTimeSerializerBase<Long> withTimestamp = dateTimeSerializer.withFormat(true, null);
        assertNotSame(dateTimeSerializer, withTimestamp);

        DateTimeFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Long> withCustomFormat = dateTimeSerializer.withFormat(false, customFormat);
        assertNotSame(dateTimeSerializer, withCustomFormat);
    }

    /**
     * Test method for {@link DateTimeSerializerBase#createContextual(SerializerProvider, BeanProperty)} method.
     */
    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        JavaType longType = mapper.getTypeFactory().constructType(Long.class);

        // Test case when JsonFormat is null
        BeanProperty property1 = null;
        JsonSerializer<?> serializer1 = new DateTimeSerializerBase<Long>(Long.class, false, null).createContextual(mapper.getSerializerProvider(), property1);
        assertEquals(new DateTimeSerializerBase<Long>(Long.class, false, null), serializer1);

        // Test case when JsonFormat hasShape NUMBER
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        static class TestClass1 {
            private Long date;
        }

        BeanProperty property2 = ReflectionTestUtils.findProperty(TestClass1.class, "date");
        JsonSerializer<?> serializer2 = new DateTimeSerializerBase<Long>(Long.class, false, null).createContextual(mapper.getSerializerProvider(), property2);
        assertTrue(serializer2 instanceof DateTimeSerializerBase);
        assertTrue(serializer2 instanceof StdScalarSerializer);
        assertTrue(serializer2 instanceof ContextualSerializer);
        assertTrue(serializer2 instanceof JsonSerializer);
        assertFalse(serializer2 instanceof StdDateSerializer);
        assertFalse(serializer2 instanceof StdSerializableSerializer);
        assertFalse(serializer2 instanceof StdValueSerializer);
        assertFalse(serializer2 instanceof StdBeanSerializerBase);
        assertEquals(longType, serializer2.getType(null));

        // Test case when JsonFormat hasShape STRING
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
        static class TestClass2 {
            private String date;
        }

        BeanProperty property3 = ReflectionTestUtils.findProperty(TestClass2.class, "date");
        JsonSerializer<?> serializer3 = new DateTimeSerializerBase<Long>(Long.class, false, null).createContextual(mapper.getSerializerProvider(), property3);
        assertTrue(serializer3 instanceof DateTimeSerializerBase);
        assertTrue(serializer3 instanceof StdScalarSerializer);
        assertTrue(serializer3 instanceof ContextualSerializer);
        assertTrue(serializer3 instanceof JsonSerializer);
        assertTrue(serializer3 instanceof StdDateSerializer);
        assertTrue(serializer3 instanceof StdSerializableSerializer);
        assertTrue(serializer3 instanceof StdValueSerializer);
        assertFalse(serializer3 instanceof StdBeanSerializerBase);
        assertEquals(stringType, serializer3.getType(null));

        // Test case when JsonFormat has a custom DateFormat
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        static class TestClass3 {
            private String date;
        }

        BeanProperty property4 = ReflectionTestUtils.findProperty(TestClass3.class, "date");
        JsonSerializer<?> serializer4 = new DateTimeSerializerBase<Long>(Long.class, false, null).createContextual(mapper.getSerializerProvider(), property4);
        assertTrue(serializer4 instanceof DateTimeSerializerBase);
        assertTrue(serializer4 instanceof StdScalarSerializer);
        assertTrue(serializer4 instanceof ContextualSerializer);
        assertTrue(serializer4 instanceof JsonSerializer);
        assertTrue(serializer4 instanceof StdDateSerializer);
        assertTrue(serializer4 instanceof StdSerializableSerializer);
        assertTrue(serializer4 instanceof StdValueSerializer);
        assertFalse(serializer4 instanceof StdBeanSerializerBase);
        assertEquals(stringType, serializer4.getType(null));

        // Test case when JsonFormat has a custom TimeZone
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "America/Los_Angeles")
        static class TestClass4 {
            private String date;
        }

        BeanProperty property5 = ReflectionTestUtils.findProperty(TestClass4.class, "date");
        JsonSerializer<?> serializer5 = new DateTimeSerializerBase<Long>(Long.class, false, null).createContextual(mapper.getSerializerProvider(), property5);
        assertTrue(serializer5 instanceof DateTimeSerializerBase);
        assertTrue(serializer5 instanceof StdScalarSerializer);
        assertTrue(serializer5 instanceof ContextualSerializer);
        assertTrue(serializer5 instanceof JsonSerializer);
        assertTrue(serializer5 instanceof StdDateSerializer);
        assertTrue(serializer5 instanceof StdSerializableSerializer);
        assertTrue(serializer5 instanceof StdValueSerializer);
        assertFalse(serializer5 instanceof StdBeanSerializerBase);
        assertEquals(stringType, serializer5.getType(null));

        // Test case when JsonFormat has a custom Locale
        @JsonFormat(shape = JsonFormat.Shape.STRING, locale = Locale.ITALY)
        static class TestClass5 {
            private String date;
        }

        BeanProperty property6 = ReflectionTestUtils.findProperty(TestClass5.class, "date");
        JsonSerializer<?> serializer6 = new DateTimeSerializerBase<Long>(Long.class, false, null).createContextual(mapper.getSerializerProvider(), property6);
        assertTrue(serializer6 instanceof DateTimeSerializerBase);
        assertTrue(serializer6 instanceof StdScalarSerializer);
        assertTrue(serializer6 instanceof ContextualSerializer);
        assertTrue(serializer6 instanceof JsonSerializer);
        assertTrue(serializer6 instanceof StdDateSerializer);
        assertTrue(serializer6 instanceof StdSerializableSerializer);
        assertTrue(serializer6 instanceof StdValueSerializer);
        assertFalse(serializer6 instanceof StdBeanSerializerBase);
        assertEquals(stringType, serializer6.getType(null));
    }

    /**
     * Test method for {@link DateTimeSerializerBase#serialize(Object, JsonGenerator, SerializerProvider)} method.
     */
    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        // Test case when serializing a null value
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        serializer.serialize(null, gen, mapper.getSerializerProvider());
        assertEquals("null", gen.getOutputBuffer().toString());

        // Test case when serializing a Long value as a numeric timestamp
        Long value1 = System.currentTimeMillis();
        serializer.serialize(value1, gen, mapper.getSerializerProvider());
        assertEquals(value1, gen.getOutputBuffer().readLong());

        // Test case when serializing a Long value as a string
        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        Long value2 = System.currentTimeMillis();
        StringFormat format = new StringFormat("%1$tY-%1$tm-%1$td %1$tH:%1$tm:%1$ts %1$tz");
        DateFormat customFormat = new SimpleDateFormat(format.toPattern(), Locale.ENGLISH);
        DateTimeSerializerBase<String> customFormatSerializer = new DateTimeSerializerBase<String>(String.class, false, customFormat);
        serializer.serialize(value2, gen, mapper.getSerializerProvider());
        assertEquals("\"\"", gen.getOutputBuffer().toString());
        stringSerializer.serialize(value2, gen, mapper.getSerializerProvider());
        String serializedValue = gen.getOutputBuffer().toString();
        String expectedValue = customFormat.format(new Date(value2));
        assertEquals(expectedValue, serializedValue.substring(1, serializedValue.length() - 1));
        customFormatSerializer.serialize(value2, gen, mapper.getSerializerProvider());
        assertEquals(expectedValue, gen.getOutputBuffer().toString());

        // Test case when serializing a Long value as a numeric timestamp with useTimestamp set to true
        DateTimeSerializerBase<Long> useTimestampSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        serializer.serialize(value1, gen, mapper.getSerializerProvider());
        assertEquals(value1, gen.getOutputBuffer().readLong());
        useTimestampSerializer.serialize(value1, gen, mapper.getSerializerProvider());
        assertEquals(value1, gen.getOutputBuffer().readLong());
    }

    /**
     * Test method for helper methods {@link DateTimeSerializerBase#_asTimestamp(SerializerProvider)} and {@link DateTimeSerializerBase#_acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType, boolean)}.
     */
    @Test
    public void testHelperMethods() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSerializerProvider().getJsonFormatVisitorProvider().findSerializerProvider().getSerializerProvider().getFactory().createJsonFormatVisitor(null, null);
        JavaType longType = mapper.getTypeFactory().constructType(Long.class);

        // Test case when _asTimestamp returns false
        assertFalse(new DateTimeSerializerBase<Long>(Long.class, false, null)._asTimestamp(null));

        // Test case when _asTimestamp returns true
        assertTrue(new DateTimeSerializerBase<Long>(Long.class, true, null)._asTimestamp(null));

        // Test case when _acceptJsonFormatVisitor is called with asNumber set to true
        new DateTimeSerializerBase<Long>(Long.class, false, null)._acceptJsonFormatVisitor(visitor, longType, true);
        assertEquals("number", visitor.getFormatType());
        assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getSerializableFormat());

        // Test case when _acceptJsonFormatVisitor is called with asNumber set to false
        new DateTimeSerializerBase<String>(String.class, false, null)._acceptJsonFormatVisitor(visitor, longType, false);
        assertEquals("string", visitor.getFormatType());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getSerializableFormat());
    }
}