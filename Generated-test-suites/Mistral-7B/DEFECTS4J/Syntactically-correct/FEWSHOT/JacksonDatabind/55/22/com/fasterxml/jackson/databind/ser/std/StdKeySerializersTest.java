package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> keySerializer;

        // Test for null rawKeyType
        keySerializer = getStdKeySerializer(config, null, true);
        assertInstanceOf(Dynamic.class, keySerializer);

        // Test for String rawKeyType
        keySerializer = getStdKeySerializer(config, String.class, true);
        assertEquals(StringKeySerializer.class, keySerializer.getClass());

        // Test for primitive types and Number
        keySerializer = getStdKeySerializer(config, int.class, true);
        assertEquals(DEFAULT_KEY_SERIALIZER.getClass(), keySerializer.getClass());
        keySerializer = getStdKeySerializer(config, double.class, true);
        assertEquals(DEFAULT_KEY_SERIALIZER.getClass(), keySerializer.getClass());

        // Test for Class rawKeyType
        keySerializer = getStdKeySerializer(config, Class.class, true);
        assertEquals(Default.class, keySerializer.getClass());

        // Test for Date rawKeyType
        keySerializer = getStdKeySerializer(config, Date.class, true);
        assertEquals(Default.class, keySerializer.getClass());
        keySerializer = getStdKeySerializer(config, Date.class, false);
        assertNull(keySerializer);

        // Test for Calendar rawKeyType
        keySerializer = getStdKeySerializer(config, Calendar.class, true);
        assertEquals(Default.class, keySerializer.getClass());
        keySerializer = getStdKeySerializer(config, Calendar.class, false);
        assertNull(keySerializer);

        // Test for java.util.UUID rawKeyType
        keySerializer = getStdKeySerializer(config, java.util.UUID.class, true);
        assertEquals(Default.class, keySerializer.getClass());
        keySerializer = getStdKeySerializer(config, java.util.UUID.class, false);
        assertNull(keySerializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> keySerializer;

        keySerializer = getFallbackKeySerializer(config, null);
        assertInstanceOf(Dynamic.class, keySerializer);

        keySerializer = getFallbackKeySerializer(config, String.class);
        assertEquals(StringKeySerializer.class, keySerializer.getClass());

        keySerializer = getFallbackKeySerializer(config, int.class);
        assertEquals(DEFAULT_KEY_SERIALIZER.getClass(), keySerializer.getClass());

        keySerializer = getFallbackKeySerializer(config, java.util.UUID.class);
        assertEquals(Default.class, keySerializer.getClass());

        keySerializer = getFallbackKeySerializer(config, Enum.class);
        assertInstanceOf(Dynamic.class, keySerializer);
    }

    @Test
    public void testDynamicSerialization() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> keySerializer = getStdKeySerializer(config, Enum.class, true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationConfig(config);

        MyEnum myEnum = MyEnum.VALUE_1;
        String json = mapper.writeValueAsString(new Object() {
            @JsonIdentityInfo(stereotypes = {MyEnum.class})
            public Enum getKey() {
                return myEnum;
            }
        });

        Object readValue = mapper.readValue(json, Object.class);
        assertEquals(myEnum, readValue.getKey());
    }
}

class MyEnum {
    public static final MyEnum VALUE_1 = new MyEnum("VALUE_1");
    private final String name;

    MyEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}