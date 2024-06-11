package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals("original", keyDeserializer.deserializeKey("original", new TestContext()));
    }

    @Test
    public void testStringKD_Object() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(Object.class);
        assertEquals("original", keyDeserializer.deserializeKey("original", new TestContext()));
    }

    @Test
    public void testBoolean() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        assertTrue(keyDeserializer.deserializeKey("true", new TestContext()));
        assertFalse(keyDeserializer.deserializeKey("false", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testByte() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 1, keyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((byte) 255, keyDeserializer.deserializeKey("255", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testShort() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 1, keyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((short) 32767, keyDeserializer.deserializeKey("32767", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testChar() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('\u0001', keyDeserializer.deserializeKey("\u0001", new TestContext()));
        assertEquals('\u0061', keyDeserializer.deserializeKey("a", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testInt() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, keyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals(12345, keyDeserializer.deserializeKey("12345", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testLong() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, keyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals(1234567890123L, keyDeserializer.deserializeKey("1234567890123", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testFloat() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(1.0f, keyDeserializer.deserializeKey("1.0", new TestContext()), 0.01f);
        assertEquals(3.14159265f, keyDeserializer.deserializeKey("3.14159265", new TestContext()), 0.001f);
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDouble() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, keyDeserializer.deserializeKey("1.0", new TestContext()), 0.001);
        assertEquals(3.14159265, keyDeserializer.deserializeKey("3.14159265", new TestContext()), 0.0001);
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testLocale() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Locale locale = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals("en_US", locale.toString());
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testDate() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Date date = keyDeserializer.deserializeKey("2023-03-27", new TestContext());
        assertEquals("Tue Mar 28 00:00:00 CET 2023", date.toString());
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testCalendar() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = keyDeserializer.deserializeKey("2023-03-27", new TestContext());
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
        assertEquals(27, calendar.get(Calendar.DAY_OF_MONTH));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testURI() throws IOException, JsonProcessingException, MalformedURLException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        URI uri = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals("http", uri.getScheme());
        assertEquals("example.com", uri.getHost());
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testURL() throws IOException, JsonProcessingException, MalformedURLException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        URL url = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals("http", url.getProtocol());
        assertEquals("example.com", url.getHost());
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testClass() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        assertEquals(String[].class, keyDeserializer.deserializeKey("[Ljava/lang/String;", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));

        keyDeserializer = StdKeyDeserializer.forType(List.class);
        assertEquals(ArrayList.class, keyDeserializer.deserializeKey("[Ljava/util/ArrayList;", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testEnum() throws IOException, JsonProcessingException {
        EnumResolver enumResolver = EnumResolver.construct(MyEnum.class, new TestContext().getAnnotationIntrospector());
        KeyDeserializer keyDeserializer = StdKeyDeserializer.EnumKD.forType(MyEnum.class, enumResolver, null);

        assertEquals(MyEnum.VALUE_1, keyDeserializer.deserializeKey("VALUE_1", new TestContext()));
        assertEquals(MyEnum.VALUE_2, keyDeserializer.deserializeKey("VALUE_2", new TestContext()));
        assertEquals(MyEnum.VALUE_3, keyDeserializer.deserializeKey("VALUE_3", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));

        keyDeserializer = StdKeyDeserializer.EnumKD.forType(MyEnum.class, enumResolver, enumResolver);
        assertEquals(MyEnum.VALUE_1, keyDeserializer.deserializeKey("VALUE_1", new TestContext()));
        assertEquals(MyEnum.VALUE_2, keyDeserializer.deserializeKey("VALUE_2", new TestContext()));
        assertEquals(MyEnum.VALUE_3, keyDeserializer.deserializeKey("VALUE_3", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testEnumToString() throws IOException, JsonProcessingException {
        EnumResolver enumResolver = EnumResolver.construct(MyEnum.class, new TestContext().getAnnotationIntrospector());
        KeyDeserializer keyDeserializer = StdKeyDeserializer.EnumKD.forType(MyEnum.class, enumResolver, null);

        assertEquals(MyEnum.VALUE_1, keyDeserializer.deserializeKey("VALUE_ONE", new TestContext()));
        assertEquals(MyEnum.VALUE_2, keyDeserializer.deserializeKey("VALUE_TWO", new TestContext()));
        assertEquals(MyEnum.VALUE_3, keyDeserializer.deserializeKey("VALUE_THREE", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));

        keyDeserializer = StdKeyDeserializer.EnumKD.forType(MyEnum.class, enumResolver, enumResolver);
        assertEquals(MyEnum.VALUE_1, keyDeserializer.deserializeKey("VALUE_ONE", new TestContext()));
        assertEquals(MyEnum.VALUE_2, keyDeserializer.deserializeKey("VALUE_TWO", new TestContext()));
        assertEquals(MyEnum.VALUE_3, keyDeserializer.deserializeKey("VALUE_THREE", new TestContext()));
        assertNull(keyDeserializer.deserializeKey("invalid", new TestContext()));
    }

    @Test
    public void testCustomDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = new CustomKeyDeserializer();
        assertEquals("customValue", keyDeserializer.deserializeKey("customKey", new TestContext()));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper _contextualObjectMapper() {
            return null;
        }

        @Override
        public AnnotationIntrospector _annotationIntrospector() {
            return null;
        }
    }

    private static class MyEnum {
        public static final MyEnum VALUE_1 = new MyEnum("VALUE_ONE");
        public static final MyEnum VALUE_2 = new MyEnum("VALUE_TWO");
        public static final MyEnum VALUE_3 = new MyEnum("VALUE_THREE");

        private final String value;

        MyEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static class CustomKeyDeserializer extends StdKeyDeserializer {
        CustomKeyDeserializer() {
            super(-1, String.class);
        }

        @Override
        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return "customValue";
        }
    }
}