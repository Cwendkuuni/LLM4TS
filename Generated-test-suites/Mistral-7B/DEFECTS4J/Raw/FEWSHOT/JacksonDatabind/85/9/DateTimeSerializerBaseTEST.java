package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    private static class TestDateTimeSerializerBase<T> extends DateTimeSerializerBase<T> {
        public TestDateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializerBase<>(handledType(), timestamp, customFormat);
        }
    }

    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<?> dateTimeSerializerBase = new DateTimeSerializerBase<>(String.class, false, null);
        assertNull(dateTimeSerializerBase._useTimestamp);
        assertNull(dateTimeSerializerBase._customFormat);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<?> dateTimeSerializerBaseUseTimestamp = new DateTimeSerializerBase<>(String.class, true, null);
        assertTrue(dateTimeSerializerBaseUseTimestamp._useTimestamp.booleanValue());
        assertNull(dateTimeSerializerBaseUseTimestamp._customFormat);

        // Test constructor with useTimestamp = false and customFormat
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Date.class);
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        DateTimeSerializerBase<Date> dateTimeSerializerBaseCustomFormat = new DateTimeSerializerBase<>(javaType, false, customFormat);
        assertFalse(dateTimeSerializerBaseCustomFormat._useTimestamp.booleanValue());
        assertEquals(customFormat, dateTimeSerializerBaseCustomFormat._customFormat);

        // Test isEmpty method
        Date date = new Date();
        assertFalse(dateTimeSerializerBaseCustomFormat.isEmpty(date));
        assertFalse(dateTimeSerializerBaseCustomFormat.isEmpty(ObjectMapper.getDefaultInstance().readValue(new ObjectMapper().writeValueAsString(date), javaType)));

        // Test getSchema method
        JsonNode schema = dateTimeSerializerBaseCustomFormat.getSchema(null, javaType);
        assertEquals("string", schema.getClass().getSimpleName());

        // Test createContextual method
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        AnnotatedClass annotatedClass = objectMapper.getTypeFactory().constructType(TestDateTimeSerializerBase.class);
        JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
        jsonFormatValue.setShape(JsonFormat.Shape.STRING);
        jsonFormatValue.setPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
        jsonFormatValue.setLocale(Locale.US);
        jsonFormatValue.setTimeZone(TimeZone.getTimeZone("GMT"));
        AnnotatedField annotatedField = annotatedClass.findAnnotation(JsonFormat.class.getName());
        if (annotatedField != null) {
            annotatedField.setValue(jsonFormatValue);
        }
        TestDateTimeSerializerBase<TestDateTimeSerializerBase<?>> testDateTimeSerializerBase = objectMapper.findValueTreeReader(new ObjectMapper().writeValueAsString(new TestDateTimeSerializerBase<>(String.class, false, null)), TestDateTimeSerializerBase.class).findValueAsTree().traverse();
        TestDateTimeSerializerBase<TestDateTimeSerializerBase<?>> contextualSerializer = testDateTimeSerializerBase.as(TestDateTimeSerializerBase.class);
        JsonSerializer<?> jsonSerializer = contextualSerializer.createContextual(null, null);
        assertTrue(jsonSerializer instanceof DateTimeSerializerBase);
        assertEquals(JsonFormatTypes.DATE_TIME, jsonSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig().getDefaultSerializerProvider()), null).getFormatType());

        // Test serialize method
        try {
            JsonGenerator jsonGenerator = ObjectMapper.getDefaultInstance().getFactory().createGenerator(new ByteArrayOutputStream(), null);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
            DateTimeSerializerBase<Date> dateTimeSerializerBaseDateFormat = new DateTimeSerializerBase<>(javaType, false, dateFormat);
            dateTimeSerializerBaseDateFormat.serialize(new Date(), jsonGenerator, null);
            String serializedDate = jsonGenerator.getOutputStream().toString();
            Instant instant = Instant.parse(serializedDate);
            assertEquals(instant.getEpochSecond() * 1000 + instant.getNano() / 1000000, _timestamp(new Date(instant.toEpochMilli())), 0.01);
            jsonGenerator.close();
        } catch (JsonGenerationException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}