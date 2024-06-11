package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class StdKeySerializersTest {

    private StdKeySerializers.Default DATE_DEFAULT;
    private StdKeySerializers.Default CALENDAR_DEFAULT;
    private StdKeySerializers.Default CLASS_DEFAULT;
    private StdKeySerializers.Default ENUM_DEFAULT;
    private StdKeySerializers.Default TOSTRING_DEFAULT;
    private StdKeySerializers.Dynamic DYNAMIC;
    private StdKeySerializers.StringKeySerializer STRINGKEYSERIALIZER;

    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        DATE_DEFAULT = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        CALENDAR_DEFAULT = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        CLASS_DEFAULT = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        ENUM_DEFAULT = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        TOSTRING_DEFAULT = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, UUID.class);
        DYNAMIC = new StdKeySerializers.Dynamic();
        STRINGKEYSERIALIZER = new StdKeySerializers.StringKeySerializer();

        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testDateDefault() throws IOException {
        Date date = new Date();

        DATE_DEFAULT.serialize(date, jsonGenerator, serializerProvider);

        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
    }

    @Test
    public void testCalendarDefault() throws IOException {
        Calendar calendar = Calendar.getInstance();

        CALENDAR_DEFAULT.serialize(calendar, jsonGenerator, serializerProvider);

        verify(serializerProvider).defaultSerializeDateKey(calendar.getTimeInMillis(), jsonGenerator);
    }

    @Test
    public void testClassDefault() throws IOException {
        Class<?> clazz = String.class;

        CLASS_DEFAULT.serialize(clazz, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(clazz.getName());
    }

    @Test
    public void testEnumDefault() throws IOException {
        Enum<?> enumValue = mock(Enum.class);
        when(enumValue.name()).thenReturn("enumName");

        ENUM_DEFAULT.serialize(enumValue, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName("enumName");
    }

    @Test
    public void testToStringDefault() throws IOException {
        UUID uuid = UUID.randomUUID();

        TOSTRING_DEFAULT.serialize(uuid, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(uuid.toString());
    }

    @Test
    public void testDynamic() throws IOException {
        String stringValue = "string";

        DYNAMIC.serialize(stringValue, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(stringValue);
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        String stringValue = "string";

        STRINGKEYSERIALIZER.serialize(stringValue, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(stringValue);
    } 
}