package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest
{
    @Test
    public void testBoolean() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);

        // When
        Object key = kd.deserializeKey("true", null);

        // Then
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testByte() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);

        // When
        Object key = kd.deserializeKey("10", null);

        // Then
        assertTrue(key instanceof Byte);
        assertEquals((byte) 10, key);

        // When (invalid)
        key = kd.deserializeKey("-10", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testShort() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Short.class);

        // When
        Object key = kd.deserializeKey("100", null);

        // Then
        assertTrue(key instanceof Short);
        assertEquals((short) 100, key);

        // When (invalid)
        key = kd.deserializeKey("-200", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testChar() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Character.class);

        // When
        Object key = kd.deserializeKey("A", null);

        // Then
        assertTrue(key instanceof Character);
        assertEquals('A', key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testInt() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);

        // When
        Object key = kd.deserializeKey("1000", null);

        // Then
        assertTrue(key instanceof Integer);
        assertEquals(1000, key);

        // When (invalid)
        key = kd.deserializeKey("-2000", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testLong() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Long.class);

        // When
        Object key = kd.deserializeKey("1000000", null);

        // Then
        assertTrue(key instanceof Long);
        assertEquals(1000000L, key);

        // When (invalid)
        key = kd.deserializeKey("-2000000000L", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testFloat() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Float.class);

        // When
        Object key = kd.deserializeKey("1.0", null);

        // Then
        assertTrue(key instanceof Float);
        assertEquals(1.0f, key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testDouble() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Double.class);

        // When
        Object key = kd.deserializeKey("1.0", null);

        // Then
        assertTrue(key instanceof Double);
        assertEquals(1.0, key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testLocale() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);

        // When
        Object key = kd.deserializeKey("en_US", null);

        // Then
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testDate() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Date.class);

        // When
        Object key = kd.deserializeKey("2023-03-12", null);

        // Then
        assertTrue(key instanceof Date);
        assertEquals("Mon Mar 12 00:00:00 CET 2023", key.toString());

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testCalendar() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);

        // When
        Object key = kd.deserializeKey("2023-03-12", null);

        // Then
        assertTrue(key instanceof Calendar);
        assertEquals("Mon Mar 12 00:00:00 CET 2023", key.toString());

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testUUID() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);

        // When
        Object key = kd.deserializeKey("00000000-0000-0000-0000-000000000000", null);

        // Then
        assertTrue(key instanceof UUID);
        assertEquals("00000000-0000-0000-0000-000000000000", key.toString());

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testURI() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(URI.class);

        // When
        Object key = kd.deserializeKey("http://www.example.com", null);

        // Then
        assertTrue(key instanceof URI);
        assertEquals("http://www.example.com", key.toString());

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testURL() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(URL.class);

        // When
        Object key = kd.deserializeKey("http://www.example.com", null);

        // Then
        assertTrue(key instanceof URL);
        assertEquals("http://www.example.com", key.toString());

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testClass() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(String[].class);

        // When
        Object key = kd.deserializeKey("java.lang.String[Ljava.lang.String;", null);

        // Then
        assertTrue(key instanceof Class<?>);
        assertEquals(String[].class, key);

        // When (invalid)
        key = kd.deserializeKey("java.lang.InvalidClass", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testCurrency() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(Currency.class);

        // When
        Object key = kd.deserializeKey("EUR", null);

        // Then
        assertTrue(key instanceof Currency);
        assertEquals(Currency.getInstance("EUR"), key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testEnum() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(MyEnum.class);
        AnnotatedClass ac = reader.getAnnotations().forDeserializer();
        StdKeyDeserializer kd = AnnotationIntrospector.deannote(ac, StdKeyDeserializer.class);

        // When
        Object key = kd.deserializeKey("MyEnum.VALUE_1", null);

        // Then
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.VALUE_1, key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    @Test
    public void testToStringDeserializer() throws IOException, JsonProcessingException
    {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForType(MyToStringEnum.class);
        AnnotatedClass ac = reader.getAnnotations().forDeserializer();
        StdKeyDeserializer kd = AnnotationIntrospector.deannote(ac, StdKeyDeserializer.class);

        // When
        Object key = kd.deserializeKey("toStringValue", null);

        // Then
        assertTrue(key instanceof MyToStringEnum);
        assertEquals(MyToStringEnum.TO_STRING_VALUE, key);

        // When (invalid)
        key = kd.deserializeKey("invalid", null);

        // Then
        assertNull(key);
    }

    private enum MyEnum {
        VALUE_1, VALUE_2
    }

    private enum MyToStringEnum {
        TO_STRING_VALUE
    }
}