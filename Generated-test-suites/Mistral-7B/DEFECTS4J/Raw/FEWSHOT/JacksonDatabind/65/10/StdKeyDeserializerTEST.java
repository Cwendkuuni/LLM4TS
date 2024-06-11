package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.EnumResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?>[] types = new Class<?>[] { String.class, Object.class, UUID.class, Integer.class, Long.class, Date.class,
                Calendar.class, Boolean.class, Byte.class, Character.class, Short.class, Float.class, Double.class, URI.class,
                URL.class, Class.class, Locale.class, Currency.class };
        StdKeyDeserializer keyDeserializer;
        for (Class<?> type : types) {
            keyDeserializer = StdKeyDeserializer.forType(type);
            assertEquals(type, keyDeserializer.getKeyClass());
        }

        // Unsupported types
        assertNull(StdKeyDeserializer.forType(int[].class));
        assertNull(StdKeyDeserializer.forType(Map.class));
        assertNull(StdKeyDeserializer.forType(List.class));
        assertNull(StdKeyDeserializer.forType(ArrayList.class));
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        beanDeserializerTest(objectMapper, String.class);
        beanDeserializerTest(objectMapper, Integer.class);
        beanDeserializerTest(objectMapper, Long.class);
        beanDeserializerTest(objectMapper, Date.class);
        beanDeserializerTest(objectMapper, UUID.class);
        beanDeserializerTest(objectMapper, URI.class);
        beanDeserializerTest(objectMapper, URL.class);
        beanDeserializerTest(objectMapper, Boolean.class);
        beanDeserializerTest(objectMapper, Character.class);
        beanDeserializerTest(objectMapper, Locale.class);
        beanDeserializerTest(objectMapper, Currency.class);
    }

    private void beanDeserializerTest(ObjectMapper objectMapper, Class<?> keyClass) throws IOException, JsonProcessingException {
        String key = "key";
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object deserializedKey = ctxt.deserializeKey(key, ctxt);
        assertEquals(keyClass, deserializedKey.getClass());

        // Test null key
        assertNull(ctxt.deserializeKey(null, ctxt));

        // Test invalid key
        String invalidKey = "invalidKey";
        JsonMappingException mappingException = assertThrows(JsonMappingException.class, () -> ctxt.deserializeKey(invalidKey, ctxt));
        String errorMessage = mappingException.getMessage();
        if (keyClass.isEnum()) {
            String enumClass = keyClass.getCanonicalName();
            assertContains(errorMessage, enumClass + " enum value not found: " + invalidKey);
        } else {
            assertContains(errorMessage, "not a valid representation");
        }
    }

    @Test
    public void testParse() throws IOException, JsonProcessingException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object key;
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        StdKeyDeserializer stdKeyDeserializer;

        // Boolean
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        key = stdKeyDeserializer._parse("true", ctxt);
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);
        key = stdKeyDeserializer._parse("false", ctxt);
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);

        // Byte
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        key = stdKeyDeserializer._parse("1", ctxt);
        assertTrue(key instanceof Byte);
        assertEquals((byte) 1, key);
        key = stdKeyDeserializer._parse("-127", ctxt);
        assertTrue(key instanceof Byte);
        assertEquals((byte) -127, key);
        key = stdKeyDeserializer._parse("128", ctxt);
        assertNull(key);

        // Short
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        key = stdKeyDeserializer._parse("1", ctxt);
        assertTrue(key instanceof Short);
        assertEquals((short) 1, key);
        key = stdKeyDeserializer._parse("-32768", ctxt);
        assertTrue(key instanceof Short);
        assertEquals((short) -32768, key);
        key = stdKeyDeserializer._parse("32768", ctxt);
        assertNull(key);

        // Character
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        key = stdKeyDeserializer._parse("'a'", ctxt);
        assertTrue(key instanceof Character);
        assertEquals('a', key);

        // Int
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        key = stdKeyDeserializer._parse("1", ctxt);
        assertTrue(key instanceof Integer);
        assertEquals(1, key);

        // Long
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        key = stdKeyDeserializer._parse("1", ctxt);
        assertTrue(key instanceof Long);
        assertEquals(1L, key);
        key = stdKeyDeserializer._parse("-9223372036854775808", ctxt);
        assertTrue(key instanceof Long);
        assertEquals(-9223372036854775808L, key);
        key = stdKeyDeserializer._parse("9223372036854775807", ctxt);
        assertNull(key);

        // Float
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        key = stdKeyDeserializer._parse("1.0", ctxt);
        assertTrue(key instanceof Float);
        assertEquals(1.0f, key);

        // Double
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        key = stdKeyDeserializer._parse("1.0", ctxt);
        assertTrue(key instanceof Double);
        assertEquals(1.0, key);
        key = stdKeyDeserializer._parse("-1.79769313486231E308", ctxt);
        assertTrue(key instanceof Double);
        assertEquals(-1.79769313486231E308, key);
        key = stdKeyDeserializer._parse("1.79769313486231E308", ctxt);
        assertNull(key);

        // URI
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        key = stdKeyDeserializer._parse("http://example.com", ctxt);
        assertTrue(key instanceof URI);
        assertEquals(new URI("http://example.com"), key);

        // URL
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        key = stdKeyDeserializer._parse("http://example.com", ctxt);
        assertTrue(key instanceof URL);
        assertEquals(new URL("http://example.com"), key);

        // Class
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        key = stdKeyDeserializer._parse("java.lang.String", ctxt);
        assertTrue(key instanceof Class);
        assertEquals(String.class, key);

        // Locale
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        key = stdKeyDeserializer._parse("en_US", ctxt);
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);

        // Currency
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        key = stdKeyDeserializer._parse("USD", ctxt);
        assertTrue(key instanceof Currency);
        assertEquals(Currency.getInstance("USD"), key);

        // Unsupported types
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parse("2022-01-01", ctxt));
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parse("2022-01-01", ctxt));
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        assertThrows(IllegalArgumentException.class, () -> stdKeyDeserializer._parse("00000000-0000-0000-0000-000000000000", ctxt));
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertEquals(expected.getClass(), actual.getClass());
            assertEquals(expected, actual);
        }
    }

    private void assertContains(String message, String substring) {
        assertTrue(message.contains(substring));
    }
}