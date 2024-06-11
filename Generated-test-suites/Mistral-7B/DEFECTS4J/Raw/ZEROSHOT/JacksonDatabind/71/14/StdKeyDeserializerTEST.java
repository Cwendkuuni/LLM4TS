package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static Object createInstance(int kind, Class<?> cls) {
        if (cls == String.class || cls == Object.class) {
            return StringKD.forType(cls);
        } else if (cls == UUID.class) {
            return new StdKeyDeserializer(TYPE_UUID, cls);
        } else if (cls == Integer.class) {
            return new StdKeyDeserializer(TYPE_INT, cls);
        } else if (cls == Long.class) {
            return new StdKeyDeserializer(TYPE_LONG, cls);
        } else if (cls == Date.class) {
            return new StdKeyDeserializer(TYPE_DATE, cls);
        } else if (cls == Calendar.class) {
            return new StdKeyDeserializer(TYPE_CALENDAR, cls);
        } else {
            throw new IllegalArgumentException("Unsupported class: " + cls);
        }
    }

    @org.junit.Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        Object keyDeserializer = createInstance(TYPE_BOOLEAN, Boolean.class);

        // Test deserializeKey with Boolean key
        String key1 = "true";
        Object result1 = keyDeserializer.deserializeKey(key1, new TestContext());
        assertEquals(Boolean.TRUE, result1);

        String key2 = "false";
        Object result2 = keyDeserializer.deserializeKey(key2, new TestContext());
        assertEquals(Boolean.FALSE, result2);

        // Test deserializeKey with invalid Boolean key
        String key3 = "invalid";
        try {
            keyDeserializer.deserializeKey(key3, new TestContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        keyDeserializer = createInstance(TYPE_BYTE, Byte.class);

        // Test deserializeKey with Byte key
        String key4 = "10";
        Object result4 = keyDeserializer.deserializeKey(key4, new TestContext());
        assertEquals((byte) 10, result4);

        keyDeserializer = createInstance(TYPE_SHORT, Short.class);

        // Test deserializeKey with Short key
        String key5 = "20";
        Object result5 = keyDeserializer.deserializeKey(key5, new TestContext());
        assertEquals((short) 20, result5);

        keyDeserializer = createInstance(TYPE_CHAR, Character.class);

        // Test deserializeKey with Char key
        String key6 = "A";
        Object result6 = keyDeserializer.deserializeKey(key6, new TestContext());
        assertEquals('A', result6);

        keyDeserializer = createInstance(TYPE_INT, Integer.class);

        // Test deserializeKey with Integer key
        String key7 = "100";
        Object result7 = keyDeserializer.deserializeKey(key7, new TestContext());
        assertEquals(100, result7);

        keyDeserializer = createInstance(TYPE_LONG, Long.class);

        // Test deserializeKey with Long key
        String key8 = "1000L";
        Object result8 = keyDeserializer.deserializeKey(key8, new TestContext());
        assertEquals(1000L, result8);

        keyDeserializer = createInstance(TYPE_FLOAT, Float.class);

        // Test deserializeKey with Float key
        String key9 = "10.5";
        Object result9 = keyDeserializer.deserializeKey(key9, new TestContext());
        assertEquals(10.5f, result9, 0.001f);

        keyDeserializer = createInstance(TYPE_DOUBLE, Double.class);

        // Test deserializeKey with Double key
        String key10 = "10.5";
        Object result10 = keyDeserializer.deserializeKey(key10, new TestContext());
        assertEquals(10.5, result10, 0.001);

        keyDeserializer = createInstance(TYPE_LOCALE, Locale.class);

        // Test deserializeKey with Locale key
        String key11 = "en_US";
        Object result11 = keyDeserializer.deserializeKey(key11, new TestContext());
        assertEquals(new Locale("en", "US"), result11);

        keyDeserializer = createInstance(TYPE_URI, URI.class);

        // Test deserializeKey with URI key
        String key12 = "http://test.com";
        Object result12 = keyDeserializer.deserializeKey(key12, new TestContext());
        assertEquals(new URI("http", "//test.com", null), result12);

        keyDeserializer = createInstance(TYPE_URL, URL.class);

        // Test deserializeKey with URL key
        String key13 = "http://test.com";
        Object result13 = keyDeserializer.deserializeKey(key13, new TestContext());
        assertEquals(new URL("http", "//test.com", null), result13);

        keyDeserializer = createInstance(TYPE_CLASS, Class.class);

        // Test deserializeKey with Class key
        String key14 = "java.util.Date";
        Object result14 = keyDeserializer.deserializeKey(key14, new TestContext());
        assertEquals(Date.class, result14);

        keyDeserializer = createInstance(TYPE_DATE, Date.class);

        // Test deserializeKey with Date key
        String key15 = "2022-12-31";
        Object result15 = keyDeserializer.deserializeKey(key15, new TestContext());
        assertEquals(new Date(Long.parseLong("2022-12-31T00:00:00.000Z")), result15);

        keyDeserializer = createInstance(TYPE_CALENDAR, Calendar.class);

        // Test deserializeKey with Calendar key
        String key16 = "2022-12-31";
        Object result16 = keyDeserializer.deserializeKey(key16, new TestContext());
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date(Long.parseLong("2022-12-31T00:00:00.000Z")));
        assertEquals(calendar, result16);

        keyDeserializer = createInstance(TYPE_UUID, UUID.class);

        // Test deserializeKey with UUID key
        String key17 = "123e4567-123e-4567-aef0-123e45678465";
        Object result17 = keyDeserializer.deserializeKey(key17, new TestContext());
        assertEquals(new UUID("123e4567-123e-4567-aef0-123e45678465"), result17);
    }

    @org.junit.Test
    public void testGetKeyClass() throws IOException {
        Object keyDeserializer = createInstance(TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_BYTE, Byte.class);
        assertEquals(Byte.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_SHORT, Short.class);
        assertEquals(Short.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_CHAR, Character.class);
        assertEquals(Character.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_INT, Integer.class);
        assertEquals(Integer.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_LONG, Long.class);
        assertEquals(Long.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_FLOAT, Float.class);
        assertEquals(Float.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_DOUBLE, Double.class);
        assertEquals(Double.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_LOCALE, Locale.class);
        assertEquals(Locale.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_URI, URI.class);
        assertEquals(URI.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_URL, URL.class);
        assertEquals(URL.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_CLASS, Class.class);
        assertEquals(Class.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_DATE, Date.class);
        assertEquals(Date.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_CALENDAR, Calendar.class);
        assertEquals(Calendar.class, keyDeserializer.getKeyClass());

        keyDeserializer = createInstance(TYPE_UUID, UUID.class);
        assertEquals(UUID.class, keyDeserializer.getKeyClass());
    }

    private static class TestContext extends DeserializationContext {
        private TestContext() {
            super(null, null, new ObjectMapper());
        }

        @Override
        public ObjectMapper getFactory() {
            return new ObjectMapper();
        }
    }
}