package com.example; // Replace with your package name

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new JsonMapper();
    }

    @Test
    public void testDateTimeSerializerBaseWithDefaultValues() throws IOException {
        // Create an instance of DateTimeSerializerBase with default values
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class);

        // Test serialization with default values
        Date date = new Date();
        String serializedDate = serializeAndGetSerializedForm(dateTimeSerializerBase, date);
        Assert.assertNotNull(serializedDate);
        assertEquals("2022-12-31T23:59:59.000Z", serializedDate);

        // Test deserialization with default values
        Date deserializedDate = deserializeAndGetDeserializedForm(dateTimeSerializerBase, serializedDate);
        Assert.assertNotNull(deserializedDate);
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws IOException {
        // Create an instance of DateTimeSerializerBase with custom format
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class)
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Test serialization with custom format
        Date date = new Date();
        String serializedDate = serializeAndGetSerializedForm(dateTimeSerializerBase, date);
        Assert.assertNotNull(serializedDate);
        assertEquals("2022-12-31 23:59:59", serializedDate);

        // Test deserialization with custom format
        Date deserializedDate = deserializeAndGetDeserializedForm(dateTimeSerializerBase, serializedDate);
        Assert.assertNotNull(deserializedDate);
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDateTimeSerializerBaseWithLocaleAndTimezone() throws IOException {
        // Create an instance of DateTimeSerializerBase with Locale and TimeZone
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class)
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        // Set Locale and TimeZone for the serializer provider
        Locale locale = new Locale("fr", "FR");
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        serializerProvider.setActiveLocale(locale);
        serializerProvider.setTimeZone(timeZone);

        // Test serialization with Locale and TimeZone
        Date date = new Date();
        String serializedDate = serializeAndGetSerializedForm(dateTimeSerializerBase, date);
        Assert.assertNotNull(serializedDate);
        assertEquals("31-12-2022 23:59:59", serializedDate); // French date format

        // Reset the serializer provider's Locale and TimeZone
        serializerProvider.setActiveLocale(Locale.getDefault());
        serializerProvider.setTimeZone(TimeZone.getDefault());

        // Test deserialization with Locale and TimeZone
        Date deserializedDate = deserializeAndGetDeserializedForm(dateTimeSerializerBase, serializedDate);
        Assert.assertNotNull(deserializedDate);
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDateTimeSerializerBaseWithJsonFormatAnnotation() throws IOException {
        // Create a JavaBean with @JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        static class MyDate {
            private Date date;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }

        // Create an instance of DateTimeSerializerBase for MyDate
        DateTimeSerializerBase<MyDate> dateTimeSerializerBase = objectMapper.findValueSerializer(MyDate.class);

        // Test serialization with @JsonFormat annotation
        MyDate myDate = new MyDate();
        myDate.setDate(new Date());
        String serializedMyDate = serializeAndGetSerializedForm(dateTimeSerializerBase, myDate);
        Assert.assertNotNull(serializedMyDate);
        assertEquals("2022-12-31 23:59:59", serializedMyDate);

        // Test deserialization with @JsonFormat annotation
        MyDate deserializedMyDate = deserializeAndGetDeserializedForm(dateTimeSerializerBase, serializedMyDate);
        Assert.assertNotNull(deserializedMyDate);
        assertEquals(myDate.getDate().getTime(), deserializedMyDate.getDate().getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseWithNullSerializerProvider() throws IOException {
        // Create an instance of DateTimeSerializerBase with null SerializerProvider
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class);
        dateTimeSerializerBase.createContextual(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDateTimeSerializerBaseWithNullProperty() throws IOException {
        // Create an instance of DateTimeSerializerBase with null property
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class);
        dateTimeSerializerBase.createContextual(new SerializerProvider(), null);
    }

    private String serializeAndGetSerializedForm(DateTimeSerializerBase<T> serializer, T value) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = serializer.getFactory().createGenerator(stringWriter);
        serializer.serialize(value, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
        return stringWriter.toString();
    }

    private T deserializeAndGetDeserializedForm(DateTimeSerializerBase<T> serializer, String jsonString) throws IOException {
        JsonParser jsonParser = serializer.getFactory().createParser(new StringReader(jsonString));
        DeserializationContext context = new DeserializationContext(new StandardDeserializationContext(), jsonParser);
        ObjectReader reader = serializer.getFactory().getValueFactory().getValueReader(context, serializer.handledType(), null);
        return (T) reader.readValue(context);
    }
}