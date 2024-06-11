package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Integer.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, UUID.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, BigDecimal.class, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Integer.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, UUID.class);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer._typeId);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, BigDecimal.class);
        assertNull(serializer);
    }

    @Test
    public void testDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNull(serializer.getParameterNames());
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        JsonNode node = mapper.writeValueAsTree("foo");
        assertEquals("foo", node.get(0).asText());
    }

    @Test
    public void testDefaultStringSerializer() {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
        assertNull(serializer.getParameterNames());
        assertEquals(StdKeySerializers.STRING_KEY_SERIALIZER, serializer);

        JsonNode node = mapper.writeValueAsTree("foo");
        assertEquals("foo", node.get(0).asText());
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        Object key = new Object() {
            public String toString() {
                return "key";
            }
        };

        JsonNode node = mapper.writeValueAsTree(key);
        assertEquals("key", node.get(0).asText());
    }

    @Test
    public void testDefaultSerializerForDate() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        Date date = new Date();

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

        JsonNode node = mapper.writeValueAsTree(date);
        assertEquals("2023-02-26T18:03:38.311+01:00", node.get(0).asText());
    }

    @Test
    public void testDefaultSerializerForCalendar() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        Calendar calendar = Calendar.getInstance();

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);

        JsonNode node = mapper.writeValueAsTree(calendar);
        assertEquals("2023-02-26T18:03:38.311+01:00", node.get(0).asText());
    }

    @Test
    public void testDefaultSerializerForEnum() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        MyEnum enumValue = MyEnum.VALUE_1;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        JsonNode node = mapper.writeValueAsTree(enumValue);
        assertEquals("VALUE_1", node.get(0).asText());
    }

    @Test
    public void testDefaultSerializerForClass() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        Class<?> clazz = MyClass.class;

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, clazz, false);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer._typeId);

        JsonNode node = mapper.writeValueAsTree(clazz);
        assertEquals("com.example.MyClass", node.get(0).asText());
    }

    @Test
    public void testDefaultSerializerForToString() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        String string = "foo";

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        JsonNode node = mapper.writeValueAsTree(string);
        assertEquals("foo", node.get(0).asText());
    }
}

class MyEnum {
    public static enum MyEnum {
        VALUE_1, VALUE_2, VALUE_3;
    }
}

class MyClass {
}