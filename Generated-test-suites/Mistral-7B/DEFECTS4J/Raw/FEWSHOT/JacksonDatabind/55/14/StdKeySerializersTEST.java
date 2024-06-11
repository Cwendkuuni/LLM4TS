package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null or Object type
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertNull(serializer);

        // Test for String type
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test for primitive types and Number
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, double.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for Class type
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.getDefault(), serializer);

        // Test for Date type
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(new Default(Default.TYPE_DATE, Date.class), serializer);

        // Test for Calendar type
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);

        // Test for UUID type
        serializer = StdKeySerializers.getStdKeySerializer(config, UUID.class, false);
        assertEquals(new Default(Default.TYPE_TO_STRING, UUID.class), serializer);

        // Test for other JDK types
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        // Test for useDefault as true
        serializer = StdKeySerializers.getStdKeySerializer(config, java.lang.Boolean.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // Test for null type
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        // Test for Enum type
        Class<MyEnum> enumType = MyEnum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, enumType);
        assertEquals(new Default(Default.TYPE_ENUM, enumType), serializer);

        // Test for String type
        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test for int type
        serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test for custom type
        Class<MyCustomClass> customType = MyCustomClass.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, customType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        // Test for String type
        String key1 = "testKey1";
        JsonNode node1 = mapper.readValue("{\"value\": " + key1 + "}", JsonNode.class);
        assertEquals(key1, node1.get("value"));

        // Test for custom type
        MyCustomClass customObject = new MyCustomClass();
        String key2 = "testKey2";
        JsonNode node2 = mapper.readValue("{\"value\": " + key2 + "}", JsonNode.class);
        node2.put("value", customObject);

        JsonNode rootNode = mapper.readTree("{\"value\": " + key2 + "}");
        rootNode.set("value", mapper.valueToTree(customObject));

        JsonNode result = mapper.treeToValue(rootNode, PropertySerializerMap.Dynamic.class);
        assertEquals(customObject, result);
        assertEquals(key2, result.getClass().getDeclaredField("_key").get(result));
    }

    // Add more test cases for other methods in the StdKeySerializers class if necessary
}