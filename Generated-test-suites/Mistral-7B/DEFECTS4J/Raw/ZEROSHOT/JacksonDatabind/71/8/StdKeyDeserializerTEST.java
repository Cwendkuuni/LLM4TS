package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer booleanStdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        Object key = booleanStdKeyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertTrue((boolean) key);

        key = booleanStdKeyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertFalse((boolean) key);
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer byteStdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);

        Object key = byteStdKeyDeserializer.deserializeKey("5", objectMapper.getDeserializationContext());
        assertEquals((byte) 5, key);
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer shortStdKeyDeserializer = StdKeyDeserializer.forType(Short.class);

        Object key = shortStdKeyDeserializer.deserializeKey("10", objectMapper.getDeserializationContext());
        assertEquals((short) 10, key);
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer charStdKeyDeserializer = StdKeyDeserializer.forType(Character.class);

        Object key = charStdKeyDeserializer.deserializeKey("A", objectMapper.getDeserializationContext());
        assertEquals('A', key);
    }

    @Test
    public void testIntKeyDeserialization() throws Exception {
        StdKeyDeserializer intStdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);

        Object key = intStdKeyDeserializer.deserializeKey("15", objectMapper.getDeserializationContext());
        assertEquals(15, key);
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer longStdKeyDeserializer = StdKeyDeserializer.forType(Long.class);

        Object key = longStdKeyDeserializer.deserializeKey("20", objectMapper.getDeserializationContext());
        assertEquals(20L, key);
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer floatStdKeyDeserializer = StdKeyDeserializer.forType(Float.class);

        Object key = floatStdKeyDeserializer.deserializeKey("3.14", objectMapper.getDeserializationContext());
        assertEquals(3.14f, key, 0.01f);
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer doubleStdKeyDeserializer = StdKeyDeserializer.forType(Double.class);

        Object key = doubleStdKeyDeserializer.deserializeKey("6.28", objectMapper.getDeserializationContext());
        assertEquals(6.28, key, 0.01);
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        StdKeyDeserializer localeStdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);

        Object key = localeStdKeyDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer dateStdKeyDeserializer = StdKeyDeserializer.forType(Date.class);

        Object key = dateStdKeyDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
        assertEquals("Sun Dec 31 00:00:00 CET 2022", key.toString());
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer calendarStdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        Object key = calendarStdKeyDeserializer.deserializeKey("2022-12-31", objectMapper.getDeserializationContext());
        assertEquals(Calendar.getInstance().getTime(), key.getTime());
    }

    @Test
    public void testUUIDKeyDeserialization() throws Exception {
        StdKeyDeserializer uuidStdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);

        Object key = uuidStdKeyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426655440000", objectMapper.getDeserializationContext());
        assertEquals("123e4567-e89b-12d3-a456-426655440000", key.toString());
    }

    @Test
    public void testURIKeyDeserialization() throws Exception {
        StdKeyDeserializer uriStdKeyDeserializer = StdKeyDeserializer.forType(URI.class);

        Object key = uriStdKeyDeserializer.deserializeKey("https://example.com", objectMapper.getDeserializationContext());
        assertEquals("https://example.com", key.toString());
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        StdKeyDeserializer urlStdKeyDeserializer = StdKeyDeserializer.forType(URL.class);

        Object key = urlStdKeyDeserializer.deserializeKey("https://example.com", objectMapper.getDeserializationContext());
        assertEquals("https://example.com", key.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeKeyDeserialization() throws Exception {
        StdKeyDeserializer unknownTypeStdKeyDeserializer = StdKeyDeserializer.forType(String[].class);

        unknownTypeStdKeyDeserializer.deserializeKey("key", objectMapper.getDeserializationContext());
    }
}