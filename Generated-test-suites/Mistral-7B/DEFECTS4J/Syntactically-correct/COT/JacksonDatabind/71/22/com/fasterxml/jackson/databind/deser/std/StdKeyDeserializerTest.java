package com.example.test;

import com.fasterxml.jackson.annotation.JacksonStdImpl;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumResolver;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;



public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Boolean.TRUE, result);
        key = "false";
        result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testByteDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();
        String key = "10";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((byte) 10, result);
    }

    @Test
    public void testShortDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();
        String key = "200";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals((short) 200, result);
    }

    @Test
    public void testCharDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = createContext();
        String key = "A";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Character.valueOf('A'), result);
    }

    @Test
    public void testIntDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        String key = "100";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Integer.valueOf(100), result);
    }

    @Test
    public void testLongDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        String key = "1000L";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Long.valueOf(1000L), result);
    }

    @Test
    public void testFloatDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = createContext();
        String key = "10.5";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Float.valueOf(10.5f), result);
    }

    @Test
    public void testDoubleDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = createContext();
        String key = "10.5";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Double.valueOf(10.5), result);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = createContext();
        String key = "en_US";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(Locale.forLanguageTag("en_US"), result);
    }

    @Test
    public void testDateDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = createContext();
        String key = "2022-12-31";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        assertEquals(new Date(1671136000000L), result);
    }

    @Test
    public void testCalendarDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = createContext();
        String key = "2022-12-31";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertNotNull(result);
        Calendar calendar = (Calendar) result;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(12, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUUIDDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = createContext();
        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);
    }

    @Test
    public void testURIDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = createContext();
        String key = "https://www.example.com/";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new URI(key), result);
    }

    @Test
    public void testURLDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();
        String key = "https://www.example.com/";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(new URL(key), result);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        DeserializationContext context = createContext();
        String key = "[Ljava.lang.String;";
        Object result = keyDeserializer.deserializeKey(key, context);
        assertEquals(String[].class, result.getClass());
        assertArrayEquals(new String[]{}, (Object) result);
    }

    @Test
    public void testEnumDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = createContext();
        AnnotatedMethod factoryMethod = Locale.class.getDeclaredMethod("forLanguageTag", String.class);
        KeyDeserializer enumKeyDeserializer = new StdKeyDeserializer.EnumKD(new EnumResolver() {
            @Override
            public Locale findEnum(String name) {
                return (Locale) factoryMethod.invoke(null, name);
            }

            @Override
            public Enum<?>[] getEnumIds() {
                return Locale.getAvailableLocales();
            }
        }, factoryMethod);
        String key = "en_US";
        Object result = enumKeyDeserializer.deserializeKey(key, context);
        assertEquals(Locale.forLanguageTag("en_US"), result);
    }

    @Test
    public void testNullDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = createContext();
        String key = null;
        Object result = keyDeserializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidBooleanDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidByteDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidShortDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCharDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidIntDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidLongDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFloatDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDoubleDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidLocaleDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDateDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCalendarDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidURIDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidURLDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidClassDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        keyDeserializer.deserializeKey(key, context);
    }

    private DeserializationContext createContext() {
        return objectMapper.getDeserializationContext();
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertEquals(null, actual);
        } else if (expected.getClass().isArray()) {
            Object[] expectedArray = (Object[]) expected;
            Object[] actualArray = (Object[]) actual;
            for (int i = 0; i < expectedArray.length; i++) {
                assertEquals(expectedArray[i], actualArray[i]);
            }
        } else {
            assertEquals(expected, actual);
        }
    }
}

