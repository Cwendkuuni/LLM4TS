package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static <T> StdKeyDeserializer<T> getStdKeyDeserializer(Class<T> keyType) {
        return (StdKeyDeserializer<T>) StdKeyDeserializer.forType(keyType);
    }

    @Test
    public void testBoolean() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Boolean> boolDeserializer = getStdKeyDeserializer(Boolean.class);

        Object key = boolDeserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, key);

        key = boolDeserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, key);

        try {
            key = boolDeserializer.deserializeKey("notABool", new TestContext());
            fail("Expected JacksonException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testByte() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Byte> byteDeserializer = getStdKeyDeserializer(Byte.class);

        Object key = byteDeserializer.deserializeKey("123", new TestContext());
        assertEquals((byte) 123, key);

        try {
            key = byteDeserializer.deserializeKey("notAByte", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testShort() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Short> shortDeserializer = getStdKeyDeserializer(Short.class);

        Object key = shortDeserializer.deserializeKey("12345", new TestContext());
        assertEquals((short) 12345, key);

        try {
            key = shortDeserializer.deserializeKey("notAShort", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testChar() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Character> charDeserializer = getStdKeyDeserializer(Character.class);

        Object key = charDeserializer.deserializeKey("a", new TestContext());
        assertEquals('a', key);

        try {
            key = charDeserializer.deserializeKey("notAChar", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testInt() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Integer> intDeserializer = getStdKeyDeserializer(Integer.class);

        Object key = intDeserializer.deserializeKey("123456", new TestContext());
        assertEquals(123456, key);

        try {
            key = intDeserializer.deserializeKey("notAnInt", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testLong() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Long> longDeserializer = getStdKeyDeserializer(Long.class);

        Object key = longDeserializer.deserializeKey("1234567890123456", new TestContext());
        assertEquals(1234567890123456L, key);

        try {
            key = longDeserializer.deserializeKey("notALong", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFloat() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Float> floatDeserializer = getStdKeyDeserializer(Float.class);

        Object key = floatDeserializer.deserializeKey("1.23", new TestContext());
        assertEquals(1.23f, key);

        try {
            key = floatDeserializer.deserializeKey("notAFloat", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDouble() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Double> doubleDeserializer = getStdKeyDeserializer(Double.class);

        Object key = doubleDeserializer.deserializeKey("1.23456", new TestContext());
        assertEquals(1.23456, key);

        try {
            key = doubleDeserializer.deserializeKey("notADouble", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testLocale() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Locale> localeDeserializer = getStdKeyDeserializer(Locale.class);

        Object key = localeDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);

        try {
            key = localeDeserializer.deserializeKey("notALocale", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDate() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Date> dateDeserializer = getStdKeyDeserializer(Date.class);

        Object key = dateDeserializer.deserializeKey("2022-03-01", new TestContext());
        assertEquals(new Date(1646304000000L), key);

        try {
            key = dateDeserializer.deserializeKey("notADate", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCalendar() throws IOException, JsonProcessingException {
        StdKeyDeserializer<Calendar> calendarDeserializer = getStdKeyDeserializer(Calendar.class);

        Object key = calendarDeserializer.deserializeKey("2022-03-01", new TestContext());
        assertEquals(Calendar.getInstance(), key);

        try {
            key = calendarDeserializer.deserializeKey("notACalendar", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testUUID() throws IOException, JsonProcessingException {
        StdKeyDeserializer<UUID> uuidDeserializer = getStdKeyDeserializer(UUID.class);

        Object key = uuidDeserializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(UUID.randomUUID(), key);

        try {
            key = uuidDeserializer.deserializeKey("notAUUID", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testURI() throws IOException, JsonProcessingException {
        StdKeyDeserializer<URI> uriDeserializer = getStdKeyDeserializer(URI.class);

        Object key = uriDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), key);

        try {
            key = uriDeserializer.deserializeKey("notAURI", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testURL() throws IOException, JsonProcessingException {
        StdKeyDeserializer<URL> urlDeserializer = getStdKeyDeserializer(URL.class);

        Object key = urlDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), key);

        try {
            key = urlDeserializer.deserializeKey("notAURL", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testClass() throws IOException, JsonProcessingException {
        StdKeyDeserializer<String> stringDeserializer = getStdKeyDeserializer(String.class);
        StdKeyDeserializer<Class<?>> classDeserializer = getStdKeyDeserializer(Class.class);

        Object key = stringDeserializer.deserializeKey("com.example.MyClass", new TestContext());
        assertEquals(String.class, key.getClass());
        assertEquals("com.example.MyClass", key.toString());

        key = classDeserializer.deserializeKey("com.example.MyClass", new TestContext());
        assertEquals(MyClass.class, key);
    }

    @Test
    public void testEnum() throws IOException, JsonProcessingException {
        enum MyEnum {
            VALUE_1, VALUE_2, VALUE_3
        }

        StdKeyDeserializer<MyEnum> enumDeserializer = getStdKeyDeserializer(MyEnum.class);

        Object key = enumDeserializer.deserializeKey("VALUE_2", new TestContext());
        assertEquals(MyEnum.VALUE_2, key);

        try {
            key = enumDeserializer.deserializeKey("notAnEnumValue", new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCustomKeyDeserializer() throws IOException, JsonProcessingException {
        class CustomKeyDeserializer extends KeyDeserializer {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(key);
            }
        }

        StdKeyDeserializer<Integer> customDeserializer = new CustomKeyDeserializer();

        Object key = customDeserializer.deserializeKey("42", new TestContext());
        assertEquals(42, key);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader _getRootValueFactory() {
            return null;
        }

        @Override
        public ObjectReader _configAndFactory() {
            return null;
        }

        @Override
        public JsonFactory _getFactory() {
            return null;
        }

        @Override
        public JsonParser _getParser() {
            return null;
        }

        @Override
        public BeanDescription _getConfig() {
            return null;
        }

        @Override
        public ObjectFactory _getObjectFactory() {
            return null;
        }

        @Override
        public AnnotationIntrospector _getAnnotationIntrospector() {
            return null;
        }

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }
    }
}