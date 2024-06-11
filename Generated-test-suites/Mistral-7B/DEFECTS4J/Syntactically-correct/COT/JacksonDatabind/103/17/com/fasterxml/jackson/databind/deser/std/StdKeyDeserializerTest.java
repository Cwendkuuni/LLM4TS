package com.example.test.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "true";
        Object result = null;

        try {
            result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(result instanceof Boolean);
        assertEquals(true, ((Boolean) result).booleanValue());
    }

    @Test
    public void testNullWhenKeyIsNull() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = null;

        Object result = serializer.deserializeKey(key, context);

        assertNull(result);
    }

    @Test
    public void testByteTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "127";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Byte.class, result);
            assertEquals((byte) 127, ((Byte) result).byteValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testByteTypeDeserializerWithNegativeValue() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "-128";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testShortTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "32767";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Short.class, result);
            assertEquals((short) 32767, ((Short) result).shortValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShortTypeDeserializerWithNegativeValue() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "-32768";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCharTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "A";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Character.class, result);
            assertEquals('A', ((Character) result).charValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharTypeDeserializerWithMultiCharKey() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "AB";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testIntTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "123456";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Integer.class, result);
            assertEquals(123456, ((Integer) result).intValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testLongTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "1234567890123456";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Long.class, result);
            assertEquals(1234567890123456L, ((Long) result).longValue());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testFloatTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "123.45";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Float.class, result);
            assertEquals(123.45f, ((Float) result).floatValue(), 0.001f);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testDoubleTypeDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "123.45";

        try {
            Object result = serializer.deserializeKey(key, context);
            assertInstanceOf(Double.class, result);
            assertEquals(123.45, ((Double) result).doubleValue(), 0.001);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testLocaleTypeDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "en_US";

        Object result = serializer.deserializeKey(key, context);
        assertInstanceOf(Locale.class, result);
        assertEquals("en_US", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocaleTypeDeserializerWithInvalidLocaleString() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "en_US_ invalid";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUriTypeDeserializerWithInvalidUriString() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "invalid:/uri";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testUrlTypeDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "http://example.com";

        Object result = serializer.deserializeKey(key, context);
        assertInstanceOf(URL.class, result);
        assertEquals("http://example.com", result.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testUrlTypeDeserializerWithInvalidUrlString() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "invalid://example.com";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected MalformedURLException");
    }

    @Test
    public void testClassTypeDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "java.lang.String";

        Object result = serializer.deserializeKey(key, context);
        assertInstanceOf(Class.class, result);
        assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassTypeDeserializerWithInvalidClassName() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String key = "com.example.NonExistentClass";

        try {
            Object result = serializer.deserializeKey(key, context);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        fail("Expected ClassNotFoundException");
    }
}