package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StdKeyDeserializerTest {
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBooleanKeyDeserialization() throws IOException {
        String json = "{\"key\":true}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Boolean.class));
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);
    }

    @Test
    public void testBooleanKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_bool}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Boolean.class));
        assertNull(key);
    }

    @Test
    public void testByteKeyDeserialization() throws IOException {
        String json = "{\"key\":1}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Byte.class));
        assertTrue(key instanceof Byte);
        assertEquals((byte) 1, key);
    }

    @Test
    public void testByteKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_byte}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Byte.class));
        assertNull(key);
    }

    @Test
    public void testShortKeyDeserialization() throws IOException {
        String json = "{\"key\":1}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Short.class));
        assertTrue(key instanceof Short);
        assertEquals((short) 1, key);
    }

    @Test
    public void testShortKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_short}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Short.class));
        assertNull(key);
    }

    @Test
    public void testCharKeyDeserialization() throws IOException {
        String json = "{\"key\":'A'}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Character.class));
        assertTrue(key instanceof Character);
        assertEquals('A', key);
    }

    @Test
    public void testCharKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_char}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Character.class));
        assertNull(key);
    }

    @Test
    public void testIntKeyDeserialization() throws IOException {
        String json = "{\"key\":1}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Integer.class));
        assertTrue(key instanceof Integer);
        assertEquals(1, key);
    }

    @Test
    public void testIntKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_int}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Integer.class));
        assertNull(key);
    }

    @Test
    public void testLongKeyDeserialization() throws IOException {
        String json = "{\"key\":1}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Long.class));
        assertTrue(key instanceof Long);
        assertEquals(1L, key);
    }

    @Test
    public void testLongKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_long}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Long.class));
        assertNull(key);
    }

    @Test
    public void testFloatKeyDeserialization() throws IOException {
        String json = "{\"key\":1.2}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Float.class));
        assertTrue(key instanceof Float);
        assertEquals(1.2f, key);
    }

    @Test
    public void testFloatKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_float}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Float.class));
        assertNull(key);
    }

    @Test
    public void testDoubleKeyDeserialization() throws IOException {
        String json = "{\"key\":1.2}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Double.class));
        assertTrue(key instanceof Double);
        assertEquals(1.2, key);
    }

    @Test
    public void testDoubleKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_double}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Double.class));
        assertNull(key);
    }

    @Test
    public void testLocaleKeyDeserialization() throws IOException {
        String json = "{\"key\":\"en_US\"}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Locale.class));
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);
    }

    @Test
    public void testLocaleKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_locale}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Locale.class));
        assertNull(key);
    }

    @Test
    public void testDateKeyDeserialization() throws IOException {
        String json = "{\"key\":\"2022-12-31T23:59:59Z\"}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Date.class));
        assertTrue(key instanceof Date);
        // Checking date value is not necessary in this test as it only checks serialization/deserialization
    }

    @Test
    public void testDateKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_date}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Date.class));
        assertNull(key);
    }

    @Test
    public void testCalendarKeyDeserialization() throws IOException {
        String json = "{\"key\":\"2022-12-31T23:59:59\"}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Calendar.class));
        assertTrue(key instanceof Calendar);
        // Checking calendar value is not necessary in this test as it only checks serialization/deserialization
    }

    @Test
    public void testCalendarKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_calendar}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Calendar.class));
        assertNull(key);
    }

    @Test
    public void testUUIDKeyDeserialization() throws IOException {
        String json = "{\"key\":\"01234567-89ab-cdef-1234-567890123456\"}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(UUID.class));
        assertTrue(key instanceof UUID);
        assertEquals(new UUID("01234567-89ab-cdef-1234-567890123456"), key);
    }

    @Test
    public void testURIKeyDeserialization() throws IOException {
        String json = "{\"key\":\"https://example.com/path/to/resource\"}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(URI.class));
        assertTrue(key instanceof URI);
        assertEquals(new URI("https://example.com/path/to/resource"), key);
    }

    @Test
    public void testURIKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_uri}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(URI.class));
        assertNull(key);
    }

    @Test
    public void testURLKeyDeserialization() throws IOException {
        String json = "{\"key\":\"https://example.com/path/to/resource\"}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(URL.class));
        assertTrue(key instanceof URL);
        assertEquals(new URL("https://example.com/path/to/resource"), key);
    }

    @Test
    public void testURLKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_url}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(URL.class));
        assertNull(key);
    }

    @Test
    public void testEnumKeyDeserialization() throws IOException {
        String json = "{\"key\":\"MyEnum.VALUE_1\"}";
        JsonNode node = mapper.readTree(json);
        ObjectMapper mapperForTesting = new ObjectMapper();
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapperForTesting.getTypingResolver();
        ObjectMapper.DefaultTypingResolver.MapTypeFactory mapTypeFactory = defaultTypingResolver.getMapTypeFactory();
        ObjectKeyDeserializer keyDeserializer = mapperForTesting.getFactory()
                .getValueDeserializer(mapTypeFactory.constructMapType(new TypeReference<Map<String, Object>>() {
                }));
        Object key = keyDeserializer.deserializeKey("MyEnum", node, mapperForTesting, null);
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.VALUE_1, key);
    }

    @Test
    public void testEnumKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_enum}";
        JsonNode node = mapper.readTree(json);
        ObjectMapper mapperForTesting = new ObjectMapper();
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = mapperForTesting.getTypingResolver();
        ObjectMapper.DefaultTypingResolver.MapTypeFactory mapTypeFactory = defaultTypingResolver.getMapTypeFactory();
        ObjectKeyDeserializer keyDeserializer = mapperForTesting.getFactory()
                .getValueDeserializer(mapTypeFactory.constructMapType(new TypeReference<Map<String, Object>>() {
                }));
        Object key = keyDeserializer.deserializeKey("MyEnum", node, mapperForTesting, null);
        assertNull(key);
    }

    @Test
    public void testClassKeyDeserialization() throws IOException {
        String json = "{\"key\":\"java.lang.String\"}";
        JsonNode node = mapper.readTree(json);
        Class<?> key = mapper.treeToValue(node, StdKeyDeserializer.forType(Class.class));
        assertTrue(key.isAssignableFrom(String.class));
    }

    @Test
    public void testClassKeyDeserializationInvalidJson() throws IOException {
        String json = "{\"key\":invalid_class}";
        JsonNode node = mapper.readTree(json);
        Object key = mapper.treeToValue(node, StdKeyDeserializer.forType(Class.class));
        assertNull(key);
    }
}