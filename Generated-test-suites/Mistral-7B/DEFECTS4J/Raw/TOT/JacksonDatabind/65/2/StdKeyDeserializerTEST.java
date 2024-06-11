package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeBooleanKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", new TestContext(Boolean.class));
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", new TestContext(Boolean.class));
        assertEquals(Boolean.FALSE, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(Boolean.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeByteKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("0", new TestContext(Byte.class));
        assertEquals((Byte) 0, key);

        key = keyDeserializer.deserializeKey("127", new TestContext(Byte.class));
        assertEquals((Byte) 127, key);

        key = keyDeserializer.deserializeKey("-128", new TestContext(Byte.class));
        assertEquals((Byte) -128, key);

        key = keyDeserializer.deserializeKey("256", new TestContext(Byte.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeShortKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("0", new TestContext(Short.class));
        assertEquals((Short) 0, key);

        key = keyDeserializer.deserializeKey("32767", new TestContext(Short.class));
        assertEquals((Short) 32767, key);

        key = keyDeserializer.deserializeKey("-32768", new TestContext(Short.class));
        assertEquals((Short) -32768, key);

        key = keyDeserializer.deserializeKey("65536", new TestContext(Short.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeCharKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("A", new TestContext(Character.class));
        assertEquals((Character) 'A', key);

        key = keyDeserializer.deserializeKey("\\u00E9", new TestContext(Character.class));
        assertEquals((Character) '\u00E9', key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(Character.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeIntKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("0", new TestContext(Integer.class));
        assertEquals((Integer) 0, key);

        key = keyDeserializer.deserializeKey("12345", new TestContext(Integer.class));
        assertEquals((Integer) 12345, key);

        key = keyDeserializer.deserializeKey("-12345", new TestContext(Integer.class));
        assertEquals((Integer) -12345, key);

        key = keyDeserializer.deserializeKey("Integer.MAX_VALUE", new TestContext(Integer.class));
        assertEquals(Integer.MAX_VALUE, key);

        key = keyDeserializer.deserializeKey("Integer.MIN_VALUE", new TestContext(Integer.class));
        assertEquals(Integer.MIN_VALUE, key);

        key = keyDeserializer.deserializeKey("2147483648", new TestContext(Integer.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeLongKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("0", new TestContext(Long.class));
        assertEquals((Long) 0, key);

        key = keyDeserializer.deserializeKey("123456789012345", new TestContext(Long.class));
        assertEquals((Long) 123456789012345L, key);

        key = keyDeserializer.deserializeKey("-123456789012345", new TestContext(Long.class));
        assertEquals((Long) -123456789012345L, key);

        key = keyDeserializer.deserializeKey("Long.MAX_VALUE", new TestContext(Long.class));
        assertEquals(Long.MAX_VALUE, key);

        key = keyDeserializer.deserializeKey("Long.MIN_VALUE", new TestContext(Long.class));
        assertEquals(Long.MIN_VALUE, key);

        key = keyDeserializer.deserializeKey("9223372036854775808", new TestContext(Long.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeFloatKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("0.0", new TestContext(Float.class));
        assertEquals((Float) 0f, key);

        key = keyDeserializer.deserializeKey("123.45", new TestContext(Float.class));
        assertEquals((Float) 123.45f, key);

        key = keyDeserializer.deserializeKey("NaN", new TestContext(Float.class));
        assertEquals((Float) Float.NaN, key);

        key = keyDeserializer.deserializeKey("INF", new TestContext(Float.class));
        assertEquals((Float) Float.POSITIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("-INF", new TestContext(Float.class));
        assertEquals((Float) Float.NEGATIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(Float.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeDoubleKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("0.0", new TestContext(Double.class));
        assertEquals((Double) 0d, key);

        key = keyDeserializer.deserializeKey("123.45", new TestContext(Double.class));
        assertEquals((Double) 123.45d, key);

        key = keyDeserializer.deserializeKey("NaN", new TestContext(Double.class));
        assertEquals((Double) Double.NaN, key);

        key = keyDeserializer.deserializeKey("INF", new TestContext(Double.class));
        assertEquals((Double) Double.POSITIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("-INF", new TestContext(Double.class));
        assertEquals((Double) Double.NEGATIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(Double.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeLocaleKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = keyDeserializer.deserializeKey("en_US", new TestContext(Locale.class));
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("es_ES", new TestContext(Locale.class));
        assertEquals(Locale.SPAIN, key);

        key = keyDeserializer.deserializeKey("fr_CH", new TestContext(Locale.class));
        assertEquals(Locale.SWITZERLAND, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(Locale.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeURIKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("http://example.com", new TestContext(URI.class));
        assertEquals(new URI("http://example.com"), key);

        key = keyDeserializer.deserializeKey("https://example.com", new TestContext(URI.class));
        assertEquals(new URI("https://example.com"), key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(URI.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeURLKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("http://example.com", new TestContext(URL.class));
        assertEquals(new URL("http://example.com"), key);

        key = keyDeserializer.deserializeKey("https://example.com", new TestContext(URL.class));
        assertEquals(new URL("https://example.com"), key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext(URL.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeClassKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        String className = String.class.getName();
        Object key = keyDeserializer.deserializeKey(className, new TestContext(String.class));
        assertEquals(className, key);

        className = "java.util.ArrayList".replaceAll("\\.", "/");
        key = keyDeserializer.deserializeKey(className, new TestContext(String.class));
        assertEquals(Class.forName(className), key);

        key = keyDeserializer.deserializeKey("java.util.List", new TestContext(String.class));
        assertEquals(List.class, key);

        key = keyDeserializer.deserializeKey("NonExistentClass", new TestContext(String.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeNullKey() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = keyDeserializer.deserializeKey(null, new TestContext(String.class));
        assertNull(key);
    }

    @Test
    public void testDeserializeUnknownEnumKey() throws Exception {
        Enum keyEnum = Enum.valueOf(UnknownEnum.class, "UNKNOWN_VALUE");
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UnknownEnum.class);
        Object key = keyDeserializer.deserializeKey(keyEnum.name(), new TestContext(UnknownEnum.class));
        assertEquals(keyEnum, key);
    }

    @Test
    public void testDeserializeUnknownEnumKeyWithUnknownValue() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UnknownEnum.class);
        Object key = keyDeserializer.deserializeKey("UNKNOWN_VALUE", new TestContext(UnknownEnum.class));
        assertNull(key);
    }

    @JsonDeserialize(using = StdKeyDeserializer.StringFactoryKeyDeserializer.class)
    static class MyStringFactoryKey {
        public MyStringFactoryKey() {}

        public static MyStringFactoryKey createInstance(String key) {
            return new MyStringFactoryKey();
        }
    }

    @Test
    public void testDeserializeKeyUsingStringFactory() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyStringFactoryKey.class);
        Object key = keyDeserializer.deserializeKey("MyStringFactoryKey", new TestContext(MyStringFactoryKey.class));
        assertEquals(MyStringFactoryKey.class, key.getClass());
    }

    @Test
    public void testDeserializeKeyUsingStringConstructor() throws Exception {
        Constructor<MyStringConstructorKey> constructor = MyStringConstructorKey.class.getDeclaredConstructor(String.class);
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyStringConstructorKey.class);
        Object key = keyDeserializer.deserializeKey("MyStringConstructorKey", new TestContext(MyStringConstructorKey.class));
        assertEquals(constructor.newInstance("MyStringConstructorKey"), key);
    }

    static class TestContext extends DeserializationContext {
        private final Class<?> _type;

        TestContext(Class<?> type) {
            super();
            _type = type;
        }

        @Override
        public Class<?> getClassInstance() {
            return _type;
        }
    }
}