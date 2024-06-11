package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Boolean.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("true", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Boolean);
        assertEquals(Boolean.TRUE, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("false", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Boolean);
        assertEquals(Boolean.FALSE, deserializedKey);
    }

    @Test
    public void testByteTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Byte.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("123", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Byte);
        assertEquals((byte) 123, deserializedKey);
    }

    @Test
    public void testShortTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Short.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("12345", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Short);
        assertEquals((short) 12345, deserializedKey);
    }

    @Test
    public void testCharTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Character.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("A", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Character);
        assertEquals('A', deserializedKey);
    }

    @Test
    public void testIntTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Integer.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("123456", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(123456, deserializedKey);
    }

    @Test
    public void testLongTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Long.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("12345678901234567890", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Long);
        assertEquals(12345678901234567890L, deserializedKey);
    }

    @Test
    public void testFloatTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Float.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("123.45", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Float);
        assertEquals(123.45f, deserializedKey);
    }

    @Test
    public void testDoubleTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Double.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("123.456.789.01234567890", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Double);
        assertEquals(123.45678901234567890D, deserializedKey);
    }

    @Test
    public void testLocaleTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Locale.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("en_US", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Locale);
        assertEquals(Locale.US, deserializedKey);
    }

    @Test
    public void testDateTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Date.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("2023-03-27", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Date);
        assertEquals("Tue Mar 28 01:54:33 MSK 2023", new SimpleDateFormat("EEE MMM d 'at' HH:mm:ss z yyyy").format((Date) deserializedKey));
    }

    @Test
    public void testCalendarTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Calendar.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("2023-03-27", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Calendar);
        assertEquals("Tue Mar 28 01:54:33 MSK 2023", ((GregorianCalendar) deserializedKey).getDisplayName());
    }

    @Test
    public void testUUIDTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(UUID.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof UUID);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", deserializedKey.toString());
    }

    @Test
    public void testURITypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(URI.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("https://example.com/path", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof URI);
        assertEquals("https://example.com/path", deserializedKey.toString());
    }

    @Test
    public void testURLTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(URL.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("https://example.com/path", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof URL);
        assertEquals("https://example.com/path", deserializedKey.toString());
    }

    @Test
    public void testClassTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(String[].class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("[Ljava.lang.String;@12345678", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof String[]);
        assertEquals(3, deserializedKey.length);
        assertEquals("first", deserializedKey[0]);
        assertEquals("second", deserializedKey[1]);
        assertEquals("third", deserializedKey[2]);
    }

    @Test
    public void testCurrencyTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(Currency.class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("USD", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof Currency);
        assertEquals("USD", deserializedKey.toString());
    }

    @Test
    public void testByteArrayTypeKeyDeserializer() throws Exception {
        StdKeyDeserializer ser = StdKeyDeserializer.forType(byte[].class);
        KeyDeserializer keyDeserializer = ser.getKeyDeserializer();
        Object deserializedKey = keyDeserializer.deserializeKey("ZXhhbXBsZSBmaWxlIEZvciBzd29yZA==", new DeserializationContext(new MockParser()));
        assertTrue(deserializedKey instanceof byte[]);
        byte[] byteArray = (byte[]) deserializedKey;
        assertEquals(5, byteArray.length);
        assertEquals((byte) 0x5A, byteArray[0]);
        assertEquals((byte) 0x48, byteArray[1]);
        assertEquals((byte) 0x68, byteArray[2]);
        assertEquals((byte) 0x65, byteArray[3]);
        assertEquals((byte) 0x63, byteArray[4]);
    }
}