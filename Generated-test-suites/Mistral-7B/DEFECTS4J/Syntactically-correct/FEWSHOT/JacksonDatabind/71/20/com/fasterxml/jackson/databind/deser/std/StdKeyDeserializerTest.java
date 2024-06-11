package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test cases for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws Exception {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(String.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Boolean() throws Exception {
        Class<?> raw = Boolean.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Boolean.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Byte() throws Exception {
        Class<?> raw = Byte.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Byte.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Short() throws Exception {
        Class<?> raw = Short.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Short.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Char() throws Exception {
        Class<?> raw = Character.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Character.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Int() throws Exception {
        Class<?> raw = Integer.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Integer.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Long() throws Exception {
        Class<?> raw = Long.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Long.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Float() throws Exception {
        Class<?> raw = Float.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Float.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Double() throws Exception {
        Class<?> raw = Double.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Double.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Locale() throws Exception {
        Class<?> raw = Locale.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Locale.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Date() throws Exception {
        Class<?> raw = Date.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Date.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Calendar() throws Exception {
        Class<?> raw = Calendar.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Calendar.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_UUID() throws Exception {
        Class<?> raw = UUID.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(UUID.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_URI() throws Exception {
        Class<?> raw = URI.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(URI.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_URL() throws Exception {
        Class<?> raw = URL.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(URL.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Class() throws Exception {
        Class<?> raw = Class.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Class.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testForType_Currency() throws Exception {
        Class<?> raw = Currency.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Currency.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testDeserializeKey_String() throws Exception {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "foo";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        Class<?> raw = Boolean.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue((Boolean) result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertFalse((Boolean) result);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        Class<?> raw = Byte.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "5";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals((byte) 5, result);
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        Class<?> raw = Short.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "10";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals((short) 10, result);
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        Class<?> raw = Character.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "A";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals((char) 'A', result);
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        Class<?> raw = Integer.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "123";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        Class<?> raw = Long.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "12345678901234567890";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(12345678901234567890L, result);
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        Class<?> raw = Locale.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "en_US";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        Class<?> raw = Date.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "2022-01-01";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertInstanceOf(Date.class, result);
        assertEquals("Mon Jan 03 00:12:34 GMT 2022", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format((Date) result));
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        Class<?> raw = Calendar.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "2022-01-01";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertInstanceOf(Calendar.class, result);
        assertEquals(2022, ((Calendar) result).get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, ((Calendar) result).get(Calendar.MONTH));
        assertEquals(1, ((Calendar) result).get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        Class<?> raw = UUID.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "123e4567-e89b-12d3-a456-426655440000";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(UUID.fromString(key), result);
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        Class<?> raw = URI.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "http://www.example.com";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(new URI(key), result);
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        Class<?> raw = URL.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "http://www.example.com";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(new URL(key), result);
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        Class<?> raw = String.class;
        Class<?> expectedType = Integer.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "java.lang.Integer";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(expectedType, result.getClass());
    }

    @Test
    public void testDeserializeKey_Currency() throws Exception {
        Class<?> raw = Currency.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "USD";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializeKey_UnknownClass() throws Exception {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        String key = "Xyz";
        assertThrows(IOException.class, () -> keyDeserializer.deserializeKey(key, new TestDeserializationContext()));
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findClass(String name) throws IOException, JsonProcessingException {
            if ("java.lang.String".equals(name)) {
                return String.class;
            }
            throw new JsonMappingException("Unrecognized class name: " + name);
        }
    }
}