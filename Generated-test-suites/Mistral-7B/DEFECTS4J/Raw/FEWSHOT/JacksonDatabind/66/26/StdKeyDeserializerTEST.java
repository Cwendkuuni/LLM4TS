package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws IOException {
        Class<?> intType = Integer.TYPE;
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(intType);
        assertEquals(intType, serializer.getKeyClass());
        assertEquals(TypeDescription.TypeId.INT, serializer.getTypeId());
    }

    @Test
    public void testDeserializeKey_Boolean() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.TYPE);
        assertEquals(Boolean.TRUE, serializer.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, serializer.deserializeKey("false", null));
        try {
            serializer.deserializeKey("foobar", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Byte() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.TYPE);
        assertEquals((byte) 0, serializer.deserializeKey("0", null));
        assertEquals((byte) 127, serializer.deserializeKey("127", null));
        assertEquals((byte) 128, serializer.deserializeKey("128", null)); // should fail
        try {
            serializer.deserializeKey("256", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Char() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.TYPE);
        assertEquals((char) 'a', serializer.deserializeKey("a", null));
        assertEquals((char) 0, serializer.deserializeKey("", null));
        try {
            serializer.deserializeKey("\u0001", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Short() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.TYPE);
        assertEquals((short) 0, serializer.deserializeKey("0", null));
        assertEquals((short) Short.MAX_VALUE, serializer.deserializeKey(String.valueOf(Short.MAX_VALUE), null));
        try {
            serializer.deserializeKey("32768", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Int() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.TYPE);
        assertEquals(0, serializer.deserializeKey("0", null));
        assertEquals(Integer.MAX_VALUE, serializer.deserializeKey(String.valueOf(Integer.MAX_VALUE), null));
        try {
            serializer.deserializeKey("2147483648", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Long() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.TYPE);
        assertEquals(0L, serializer.deserializeKey("0", null));
        assertEquals(Long.MAX_VALUE, serializer.deserializeKey(String.valueOf(Long.MAX_VALUE), null));
        try {
            serializer.deserializeKey("9223372036854775808", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Float() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.TYPE);
        assertEquals(0.0f, serializer.deserializeKey("0.0", null), 0.001f);
        assertEquals(Float.MAX_VALUE, serializer.deserializeKey(String.valueOf(Float.MAX_VALUE), null), 0.001f);
        try {
            serializer.deserializeKey("3.4E38F", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Double() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.TYPE);
        assertEquals(0.0, serializer.deserializeKey("0.0", null), 0.001);
        assertEquals(Double.MAX_VALUE, serializer.deserializeKey(String.valueOf(Double.MAX_VALUE), null), 0.001);
        try {
            serializer.deserializeKey("1.8E308", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Locale() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        assertEquals(Locale.US, serializer.deserializeKey("en_US", null));
        assertEquals(Locale.JAPAN, serializer.deserializeKey("ja_JP", null));
        try {
            serializer.deserializeKey("invalidLocale", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Date() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertEquals(new Date(), serializer.deserializeKey("2022-01-01", null));
        try {
            serializer.deserializeKey("invalidDate", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Calendar() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        GregorianCalendar calendar = new GregorianCalendar(2022, 0, 1);
        assertEquals(calendar, serializer.deserializeKey(calendar.toString(), null));
        try {
            serializer.deserializeKey("invalidCalendar", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_UUID() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, serializer.deserializeKey(uuid.toString(), null));
        try {
            serializer.deserializeKey("invalidUUID", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_URI() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        URI uri = new URI("http://example.com");
        assertEquals(uri, serializer.deserializeKey(uri.toString(), null));
        try {
            serializer.deserializeKey("invalidURI", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_URL() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        URL url = new URL("http://example.com");
        assertEquals(url, serializer.deserializeKey(url.toString(), null));
        try {
            serializer.deserializeKey("invalidURL", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeKey_Class() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        assertEquals(String.class, serializer.deserializeKey("java.lang.String", null));
        try {
            serializer.deserializeKey("java.util.ArrayList", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}