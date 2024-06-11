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

    @SuppressWarnings("unchecked")
    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        //Test for null and Object.class
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for String.class
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        //Test for primitive types and Number.class
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for Class.class
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), serializer);

        //Test for Date.class
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        //Test for Calendar.class
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        //Test for java.util.UUID.class
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        //Test for boolean.class
        serializer = StdKeySerializers.getStdKeySerializer(config, boolean.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for custom class
        class CustomClass {
        }
        serializer = StdKeySerializers.getStdKeySerializer(config, CustomClass.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for enum class
        enum MyEnum {
            ONE, TWO, THREE
        }
        serializer = StdKeySerializers.getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class), serializer);

        //Test for null and useDefault = true
        serializer = StdKeySerializers.getStdKeySerializer(config, null, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for Object.class and useDefault = true
        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        //Test for null
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        //Test for Class.class
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for custom class
        class CustomClass {
        }
        serializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        //Test for enum class
        enum MyEnum {
            ONE, TWO, THREE
        }
        serializer = StdKeySerializers.getFallbackKeySerializer(config, MyEnum.class);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class), serializer);
    }

    @Test
    public void testDynamicKeySerializer() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        //Test for String
        String key1 = "test1";
        Object value1 = "testValue1";
        String json1 = mapper.writeValueAsString(new Object() {
            String key = key1;
            Object value = value1;
        });
        JsonParser parser1 = mapper.getFactory().createParser(json1);
        Map<String, Object> map1 = mapper.readValue(parser1, Map.class);
        assertEquals(value1, map1.get(key1));

        //Test for Integer
        int key2 = 1;
        Object value2 = "testValue2";
        String json2 = mapper.writeValueAsString(new Object() {
            int key = key2;
            Object value = value2;
        });
        JsonParser parser2 = mapper.getFactory().createParser(json2);
        Map<String, Object> map2 = mapper.readValue(parser2, Map.class);
        assertEquals(value2, map2.get(Integer.toString(key2)));

        //Test for custom class
        class CustomClass {
            int id;
        }
        CustomClass customObj = new CustomClass();
        customObj.id = 42;
        String json3 = mapper.writeValueAsString(new Object() {
            int key = customObj.id;
            Object value = customObj;
        });
        JsonParser parser3 = mapper.getFactory().createParser(json3);
        Map<String, Object> map3 = mapper.readValue(parser3, Map.class);
        assertEquals(customObj, map3.get(Integer.toString(customObj.id)));
    }
}