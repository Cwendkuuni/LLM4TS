package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Test file for StdKeyDeserializer class.
 */
public class StdKeyDeserializerTest {

    @org.junit.Test
    public void testDeserializeKeyForBoolean() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "true");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertTrue((Boolean) key);
    }

    @org.junit.Test
    public void testDeserializeKeyForByte() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "10");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals((byte) 10, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForShort() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "100");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals((short) 100, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForChar() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "A");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals('A', key);
    }

    @org.junit.Test
    public void testDeserializeKeyForInt() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "1000");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(1000, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForLong() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "1000000");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(1000000L, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForFloat() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "3.14");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(3.14f, key, 0.001f);
    }

    @org.junit.Test
    public void testDeserializeKeyForDouble() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "3.14159265");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(3.14159265, key, 0.0000001);
    }

    @org.junit.Test
    public void testDeserializeKeyForLocale() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "en_US");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(Locale.US, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForDate() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "2022-01-01");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals("Fri Jan 01 00:00:00 CST 2022", key.toString());
    }

    @org.junit.Test
    public void testDeserializeKeyForCalendar() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "2022-01-01");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        Calendar calendar = (Calendar) key;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @org.junit.Test
    public void testDeserializeKeyForUUID() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "123e4567-e89b-12d3-a456-426655440000");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        UUID uuid = (UUID) key;
        assertEquals("123e4567-e89b-12d3-a456-426655440000", uuid.toString());
    }

    @org.junit.Test
    public void testDeserializeKeyForURI() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "https://example.com");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        URI uri = (URI) key;
        assertEquals("https://example.com", uri.toString());
    }

    @org.junit.Test
    public void testDeserializeKeyForURL() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "https://example.com");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        URL url = (URL) key;
        assertEquals("https://example.com", url.toString());
    }

    @org.junit.Test
    public void testDeserializeKeyForClass() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "java.lang.String");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(String.class, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForEnum() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "COLOR_RED");

        Enum enumType = MyEnum.class;
        Constructor<MyEnum> constructor = MyEnum.class.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.EnumKD(new EnumResolver() {
            @Override
            public Enum<?> findEnum(String name) {
                try {
                    return constructor.newInstance(name);
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to deserialize enum using constructor: " + e.getMessage());
                }
            }
        }, new AnnotatedMethod(constructor, MyEnum.class));

        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(MyEnum.COLOR_RED, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForEnumWithFactoryMethod() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "Color.RED");

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(MyEnum.Color.class.getDeclaredMethod("valueOf", String.class));
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(MyEnum.Color.RED, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForEnumWithStringConstructor() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "Color('RED')");

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(MyEnum.Color.class.getDeclaredConstructor(String.class));
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertEquals(MyEnum.Color.RED, key);
    }

    @org.junit.Test
    public void testDeserializeKeyForNull() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", null);

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKeyForUnknownType() throws IOException, JsonProcessingException {
        ObjectNode mapNode = ObjectMapperBuilder.json()
                .build()
                .valueFactory()
                .objectNode();
        mapNode.put("key", "MyUnknownClass");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = keyDeserializer.deserializeKey("key", new TestContext());
        assertNull(key);
    }

    /**
     * Test context for StdKeyDeserializer tests.
     */
    public static class TestContext extends DeserializationContextBase {
        @Override
        public ObjectFactory getObjectFactory() {
            return new StandardSerializers.BaseJsonSerializer.NullValueSerializer(ObjectMapperBuilder.json().build());
        }
    }

    private enum MyEnum {
        COLOR_RED,
        COLOR_GREEN,
        COLOR_BLUE;

        public static enum Color {
            RED("RED"),
            GREEN("GREEN"),
            BLUE("BLUE");

            private final String name;

            Color(String name) {
                this.name = name;
            }

            public static Color valueOf(String name) {
                return Arrays.stream(Color.values())
                        .filter(c -> c.name.equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null);
            }
        }
    }
}