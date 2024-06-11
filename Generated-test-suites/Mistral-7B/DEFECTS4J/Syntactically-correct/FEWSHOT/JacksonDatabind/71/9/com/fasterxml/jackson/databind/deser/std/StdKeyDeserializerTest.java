package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?>[] types = {String.class, Object.class, UUID.class, Integer.class, Long.class, Date.class, Calendar.class,
                Boolean.class, Byte.class, Character.class, Short.class, Float.class, Double.class, URI.class, URL.class,
                Class.class, Locale.class, Currency.class};

        for (Class<?> type : types) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertNotNull(keyDeserializer);
            assertEquals(type, keyDeserializer.getKeyClass());
        }
    }

    @Test
    public void testDeserializeKeyStringInt() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer intKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "7";
        Object deserializedKey = intKeyDeserializer.deserializeKey(key, new TestContext());
        assertEquals(7, deserializedKey);
    }

    @Test
    public void testDeserializeKeyStringLong() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer longKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "12345678901234567890";
        Object deserializedKey = longKeyDeserializer.deserializeKey(key, new TestContext());
        assertEquals(12345678901234567890L, deserializedKey);
    }

    @Test
    public void testDeserializeKeyStringBoolean() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer booleanKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String keyTrue = "true";
        Object deserializedKeyTrue = booleanKeyDeserializer.deserializeKey(keyTrue, new TestContext());
        assertTrue((Boolean) deserializedKeyTrue);

        String keyFalse = "false";
        Object deserializedKeyFalse = booleanKeyDeserializer.deserializeKey(keyFalse, new TestContext());
        assertFalse((Boolean) deserializedKeyFalse);
    }

    @Test
    public void testDeserializeKeyStringDate() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer dateKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        String key = "2023-03-15";
        Object deserializedKey = dateKeyDeserializer.deserializeKey(key, new TestContext());
        Date date = (Date) deserializedKey;
        assertEquals(2023, date.getYear());
        assertEquals(3, date.getMonth());
        assertEquals(15, date.getDate());
    }

    @Test
    public void testDeserializeKeyStringLocale() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer localeKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        String key = "en_US";
        Object deserializedKey = localeKeyDeserializer.deserializeKey(key, new TestContext());
        Locale locale = (Locale) deserializedKey;
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeKeyStringNull() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer stringKeyDeserializer = new StdKeyDeserializer(-1, String.class);
        String key = null;
        Object deserializedKey = stringKeyDeserializer.deserializeKey(key, new TestContext());
        assertNull(deserializedKey);
    }

    // Add more test cases for other types and edge cases as needed
}

class TestContext extends DeserializationContext {
    // Implement the necessary methods to make the test pass
}