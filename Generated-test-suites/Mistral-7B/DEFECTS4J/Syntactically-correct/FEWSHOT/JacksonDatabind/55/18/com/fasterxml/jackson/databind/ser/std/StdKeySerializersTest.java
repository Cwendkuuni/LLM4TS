package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StdKeySerializersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = null;

        // Test with null rawKeyType
        serializer = getStdKeySerializer(config, null, false);
        assertNull(serializer);

        // Test with String class
        serializer = getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test with primitive types
        serializer = getStdKeySerializer(config, boolean.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Number class
        serializer = getStdKeySerializer(config, Integer.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Class class
        serializer = getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Date class
        serializer = getStdKeySerializer(config, Date.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        // Test with Calendar class
        serializer = getStdKeySerializer(config, Calendar.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        // Test with UUID class
        serializer = getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        // Test with custom class
        class MyCustomClass {
        }
        serializer = getStdKeySerializer(config, MyCustomClass.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Object class
        serializer = getStdKeySerializer(config, Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Enum class
        enum MyEnum {
            VALUE_1, VALUE_2, VALUE_3
        }
        serializer = getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class), serializer);

        // Test with custom enum class
        enum MyCustomEnum {
            VALUE_1, VALUE_2, VALUE_3
        }
        serializer = getStdKeySerializer(config, MyCustomEnum.class, false);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyCustomEnum.class), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = null;

        // Test with null rawKeyType
        serializer = getFallbackKeySerializer(config, null);
        assertNull(serializer);

        // Test with String class
        serializer = getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test with custom class
        class MyCustomClass {
        }
        serializer = getFallbackKeySerializer(config, MyCustomClass.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Enum class
        enum MyEnum {
            VALUE_1, VALUE_2, VALUE_3
        }
        serializer = getFallbackKeySerializer(config, MyEnum.class);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class), serializer);

        // Test with custom enum class
        enum MyCustomEnum {
            VALUE_1, VALUE_2, VALUE_3
        }
        serializer = getFallbackKeySerializer(config, MyCustomEnum.class);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyCustomEnum.class), serializer);
    }

    @Test
    public void testDynamicKeySerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = null;
        PropertySerializerMap map = new PropertySerializerMap();

        // Test serialization with primitive types
        int intValue = 1;
        serializer = getStdKeySerializer(config, intValue.getClass(), false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = _findAndAddDynamic(map, intValue.getClass(), config.getDefaultSerializerProvider());
        JsonSerializer<Object> dynamicSerializer = map.serializerFor(intValue.getClass());
        assertEquals(StdKeySerializers.Default.class, dynamicSerializer.getClass());

        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        g.setCodec(new ObjectMapper());
        serializer.serialize(intValue, g, config.getDefaultSerializerProvider());
        g.flush();
        assertStringEquals("1", g.getOutputContent().toString());

        g.reset();
        dynamicSerializer.serialize(intValue, g, config.getDefaultSerializerProvider());
        g.flush();
        assertStringEquals("1", g.getOutputContent().toString());

        // Test serialization with custom class
        class MyCustomClass {
            int value;

            public MyCustomClass(int value) {
                this.value = value;
            }
        }
        MyCustomClass customClass = new MyCustomClass(2);

        serializer = getStdKeySerializer(config, customClass.getClass(), false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = _findAndAddDynamic(map, customClass.getClass(), config.getDefaultSerializerProvider());
        dynamicSerializer = map.serializerFor(customClass.getClass());
        assertEquals(StdKeySerializers.Default.class, dynamicSerializer.getClass());

        g.reset();
        serializer.serialize(customClass, g, config.getDefaultSerializerProvider());
        g.flush();
        assertStringEquals("MyCustomClass{value=2}", g.getOutputContent().toString());

        g.reset();
        dynamicSerializer.serialize(customClass, g, config.getDefaultSerializerProvider());
        g.flush();
        assertStringEquals("MyCustomClass{value=2}", g.getOutputContent().toString());
    }

    @Test
    public void testStringKeySerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.STRING_KEY_SERIALIZER;

        String key = "test";
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        g.setCodec(new ObjectMapper());

        serializer.serialize(key, g, new DefaultSerializerProvider());
        g.flush();
        assertStringEquals("test", g.getOutputContent().toString());
    }

    @Test
    public void testEnumKeySerializer() {
        JsonSerializer<Object> serializer = null;
        SerializationConfig config = new SerializationConfig();

        // Test with default Enum serialization
        enum MyEnum {
            VALUE_1, VALUE_2, VALUE_3
        }
        serializer = getStdKeySerializer(config, MyEnum.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());

        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        g.setCodec(new ObjectMapper());
        MyEnum enumValue = MyEnum.VALUE_1;
        serializer.serialize(enumValue, g, new DefaultSerializerProvider());
        g.flush();
        assertStringEquals("VALUE_1", g.getOutputContent().toString());

        g.reset();
        serializer = getFallbackKeySerializer(config, MyEnum.class);
        serializer.serialize(enumValue, g, new DefaultSerializerProvider());
        g.flush();
        assertStringEquals("VALUE_1", g.getOutputContent().toString());

        // Test with custom Enum serialization
        g.reset();
        SerializerFactory serializerFactory = new SimpleSerializerProvider.BaseSerializerFactory();
        serializer = serializerFactory.findValueSerializer(MyEnum.class, null);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        serializer.serialize(enumValue, g, new DefaultSerializerProvider());
        g.flush();
        assertStringEquals("VALUE_1", g.getOutputContent().toString());

        g.reset();
        dynamicSerializer = _findAndAddDynamic(new PropertySerializerMap(), MyEnum.class, new DefaultSerializerProvider());
        dynamicSerializer.serialize(enumValue, g, new DefaultSerializerProvider());
        g.flush();
        assertStringEquals("VALUE_1", g.getOutputContent().toString());
    }

    private JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map,
            Class<?> type, SerializerProvider provider) throws JsonMappingException {
        PropertySerializerMap.SerializerAndMapResult result =
                map.findAndAddKeySerializer(type, provider, null);
        return result.serializer;
    }

    private void assertStringEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }

    private static StringWriter newStringWriter() {
        return new StringWriter();
    }
}