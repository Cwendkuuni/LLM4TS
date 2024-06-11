package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateTimeSerializerBaseBuilder;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        Locale locale = Locale.ENGLISH;
        TimeZone timeZone = TimeZone.getDefault();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
        customFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBaseBuilder<Date>()
                .withUseTimestamp(false)
                .withCustomFormat(customFormat)
                .build();

        JsonSerializer<Date> jsonSerializer = mapper.serializerByType(Date.class);
        assertNotNull(jsonSerializer);
        assertEquals(ValueType.DATE_TIME, jsonSerializer.getValueType());

        JsonFormat<Date> jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setTimeZone(timeZone);
        jsonFormat.setLocale(locale);
        jsonFormat.setPattern("dd-MM-yyyy HH:mm:ss");
        jsonFormat.setSerializableWith(Date.class);

        SerializerProvider serializers = mapper.getSerializerProvider();
        Annotated property = mapper.introspect(JsonSerialize.class.getDeclaredField("value"));
        JsonFormat<Date> propertyFormat = serializers.getAnnotationIntrospector().findFormat(property);

        // test serialization with custom format
        if (propertyFormat != null && propertyFormat.getShape() == JsonFormat.Shape.STRING) {
            jsonSerializer = dateSerializerBase.createContextual(serializers, null);
            assertNotNull(jsonSerializer);
            assertEquals(ValueType.DATE_TIME, jsonSerializer.getValueType());
            JsonGenerator gen = mapper.getPolymorphicSerializer(jsonSerializer);
            gen.setSerializer(jsonSerializer);
            String result = mapper.writeValueAsString(date);
            assertEquals("31-12-2022 12:00:00", result);

            // test deserialization with custom format
            JsonParser parser = mapper.getFactory().createParser(result);
            Date deserialized = mapper.readValue(parser, Date.class);
            assertEquals(date, deserialized);
        }

        // test serialization as timestamp
        jsonSerializer = mapper.serializerByType(Date.class);
        assertNotNull(jsonSerializer);
        assertEquals(ValueType.NUMBER, jsonSerializer.getValueType());

        jsonSerializer = dateSerializerBase.createContextual(serializers, null);
        assertNotNull(jsonSerializer);
        assertEquals(ValueType.NUMBER, jsonSerializer.getValueType());
        JsonGenerator gen = mapper.getPolymorphicSerializer(jsonSerializer);
        gen.setSerializer(jsonSerializer);
        long timestamp = date.getTime();
        String result = mapper.writeValueAsString(timestamp);
        assertEquals("3422322223232", result);

        // test deserialization as timestamp
        JsonParser parser = mapper.getFactory().createParser(result);
        Date deserialized = mapper.readValue(parser, Date.class);
        assertEquals(date, deserialized);

        // test serialization with default format
        jsonSerializer = mapper.serializerByType(Date.class);
        assertNotNull(jsonSerializer);
        assertEquals(ValueType.DATE_TIME, jsonSerializer.getValueType());

        jsonSerializer = mapper.serializerByType(Date.class);
        jsonSerializer = mapper.serializerByType(Date.class).withContext(new ToStringSerializer(Date.class));

        String defaultResult = mapper.writeValueAsString(date);
        assertEquals("2022-12-31T12:00:00.000Z", defaultResult);

        // test deserialization with default format
        JsonParser defaultParser = mapper.getFactory().createParser(defaultResult);
        Date deserializedDefault = mapper.readValue(defaultParser, Date.class);
        assertEquals(date, deserializedDefault);

    }
}