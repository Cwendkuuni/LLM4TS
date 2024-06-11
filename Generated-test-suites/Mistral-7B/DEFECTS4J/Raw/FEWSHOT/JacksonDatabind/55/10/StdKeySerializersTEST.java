package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);

        rawKeyType = String.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);

        rawKeyType = UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(serializer);

        rawKeyType = Double.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.getStdKeySerializer(config, rawKeyType, true).getClass(), serializer.getClass());
    }

    @Test
    public void testDefaultKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertEquals(Serializers.Base.stringSerializer().getClass(), serializer.getClass());

        JsonNode node = mapper.writeValueAsTree("key", "value");
        assertEquals("value", node.get("key").asText());

        config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        node = mapper.writeValueAsTree("key", null);
        assertNull(node.get("key"));
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        Class<?> clazz = new Object(){}.getClass();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, clazz, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        Object obj = new Object(){};
        JsonNode node = mapper.writeValueAsTree(obj, new SimpleBeanPropertyFilter("key"));
        assertEquals("key", node.get("key").asText());

        clazz = String.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, clazz, false);
        assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());

        node = mapper.writeValueAsTree("key", "value");
        assertEquals("value", node.get("key").asText());
    }

    @Test
    public void testDefaultDateSerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        Date date = new Date();
        JsonNode node = mapper.writeValueAsTree(date, new SimpleBeanPropertyFilter("key"));
        assertEquals(date.getTime(), node.get("key").asLong());
    }

    @Test
    public void testDefaultCalendarSerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        Calendar calendar = Calendar.getInstance();
        JsonNode node = mapper.writeValueAsTree(calendar, new SimpleBeanPropertyFilter("key"));
        assertEquals(calendar.getTimeInMillis(), node.get("key").asLong());
    }

    @Test
    public void testToStringKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, UUID.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        UUID uuid = UUID.randomUUID();
        JsonNode node = mapper.writeValueAsTree(uuid, new SimpleBeanPropertyFilter("key"));
        assertEquals(uuid.toString(), node.get("key").asText());
    }
}