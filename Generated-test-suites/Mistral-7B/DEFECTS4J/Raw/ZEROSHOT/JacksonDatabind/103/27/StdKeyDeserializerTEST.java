package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);

        key = deserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("0", new TestContext());
        assertEquals((Byte) 0, key);

        key = deserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, key);

        key = deserializer.deserializeKey("128", new TestContext());
        assertNull(key);
    }

    @Test
    public void testShortType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("0", new TestContext());
        assertEquals((Short) 0, key);

        key = deserializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, key);

        key = deserializer.deserializeKey("32768", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCharType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("a", new TestContext());
        assertEquals((Character) 'a', key);

        key = deserializer.deserializeKey("z", new TestContext());
        assertEquals((Character) 'z', key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testIntType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("0", new TestContext());
        assertEquals((Integer) 0, key);

        key = deserializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) 2147483647, key);

        key = deserializer.deserializeKey("2147483648", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLongType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("0", new TestContext());
        assertEquals((Long) 0, key);

        key = deserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, key);

        key = deserializer.deserializeKey("9223372036854775808", new TestContext());
        assertNull(key);
    }

    @Test
    public void testFloatType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("0.0", new TestContext());
        assertEquals((Float) 0.0f, key);

        key = deserializer.deserializeKey("3.14159265359", new TestContext());
        assertEquals((Float) 3.14159265359f, key, 0.0001f);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDoubleType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("0.0", new TestContext());
        assertEquals((Double) 0.0, key);

        key = deserializer.deserializeKey("6.28318530718", new TestContext());
        assertEquals((Double) 6.28318530718, key, 0.000001);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLocaleType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);

        key = deserializer.deserializeKey("es_ES", new TestContext());
        assertEquals(Locale.SPAIN, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDateType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object key = deserializer.deserializeKey("2022-12-31", new TestContext());
        assertEquals(new Date(1671321600000L), key);

        key = deserializer.deserializeKey("2022-11-30", new TestContext());
        assertEquals(new Date(1668732800000L), key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCalendarType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = deserializer.deserializeKey("2022-12-31", new TestContext());
        GregorianCalendar calendar = new GregorianCalendar(2022, 11, 31);
        assertEquals(calendar, key);

        key = deserializer.deserializeKey("2022-11-30", new TestContext());
        calendar = new GregorianCalendar(2022, 10, 30);
        assertEquals(calendar, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testUriType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("http://example.com", new TestContext());
        URI uri = new URI("http", "example.com", null);
        assertEquals(uri, key);

        key = deserializer.deserializeKey("https://example.com", new TestContext());
        uri = new URI("https", "example.com", null);
        assertEquals(uri, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testUrlType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("http://example.com", new TestContext());
        URL url = new URL("http", "example.com", 80, "/path/to/resource");
        assertEquals(url, key);

        key = deserializer.deserializeKey("https://example.com", new TestContext());
        url = new URL("https", "example.com", 443, "/path/to/resource");
        assertEquals(url, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testClassType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);

        key = deserializer.deserializeKey("java.util.ArrayList", new TestContext());
        assertEquals(ArrayList.class, key);

        key = deserializer.deserializeKey("java.lang.Object", new TestContext());
        assertEquals(Object.class, key);

        key = deserializer.deserializeKey("java.util.HashMap", new TestContext());
        assertEquals(HashMap.class, key);

        key = deserializer.deserializeKey("java.util.HashSet", new TestContext());
        assertEquals(HashSet.class, key);

        key = deserializer.deserializeKey("com.example.MyClass", new TestContext());
        Class<?> clazz = Class.forName("com.example.MyClass");
        assertEquals(clazz, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Color.class);
        Object key = deserializer.deserializeKey("RED", new TestContext());
        assertEquals(Color.RED, key);

        key = deserializer.deserializeKey("GREEN", new TestContext());
        assertEquals(Color.GREEN, key);

        key = deserializer.deserializeKey("BLUE", new TestContext());
        assertEquals(Color.BLUE, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteArrayType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(byte[].class);
        Object key = deserializer.deserializeKey("Rg==", new TestContext());
        byte[] bytes = Base64.decodeBase64("Rg==");
        assertEquals(bytes, key);

        key = deserializer.deserializeKey("QWx//6z+", new TestContext());
        bytes = Base64.decodeBase64("QWx//6z+");
        assertEquals(bytes, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectInstanceFinder defaultInstanceFinder() {
            return new AlwaysReturnDefaultInstanceFinder();
        }

        @Override
        public BeanDescription findBeanDescription(Class<?> potentialBeanType, String propertyName) {
            return null;
        }

        @Override
        public AnnotationIntrospector introspectAnnotation(Class<?> annType) {
            return null;
        }
    }
}