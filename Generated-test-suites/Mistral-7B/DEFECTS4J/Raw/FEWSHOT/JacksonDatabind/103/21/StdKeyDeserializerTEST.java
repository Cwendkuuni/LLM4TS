package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.io.NumberInput;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.StringKD.forType(String.class);
        String key = "test";
        assertEquals(key, kd.deserializeKey(key, null));
    }

    @Test
    public void testObjectKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.StringKD.forType(Object.class);
        String key = "test";
        assertEquals(key, kd.deserializeKey(key, null));
    }

    @Test
    public void testBooleanKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, kd.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, kd.deserializeKey("false", null));
        assertNull(kd.deserializeKey("falseish", null));
        assertNull(kd.deserializeKey("truesome", null));
    }

    @Test
    public void testByteKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 1, kd.deserializeKey("1", null));
        assertEquals((byte) 127, kd.deserializeKey("127", null));
        assertEquals((byte) -128, kd.deserializeKey("-128", null));
        assertEquals((byte) 255, kd.deserializeKey("255", null));
        assertNull(kd.deserializeKey("256", null));
    }

    @Test
    public void testShortKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 1, kd.deserializeKey("1", null));
        assertEquals((short) Short.MIN_VALUE, kd.deserializeKey("-32768", null));
        assertEquals((short) Short.MAX_VALUE, kd.deserializeKey("32767", null));
        assertNull(kd.deserializeKey("32768", null));
    }

    @Test
    public void testCharKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
        assertEquals('a', kd.deserializeKey("a", null));
        assertEquals('\'', kd.deserializeKey("'", null));
        assertNull(kd.deserializeKey("", null));
        assertNull(kd.deserializeKey("abc", null));
    }

    @Test
    public void testIntKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, kd.deserializeKey("1", null));
        assertEquals(-1, kd.deserializeKey("-1", null));
        assertEquals((int) Long.MAX_VALUE, kd.deserializeKey("2147483647", null));
        assertNull(kd.deserializeKey("2147483648", null));
    }

    @Test
    public void testLongKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, kd.deserializeKey("1", null));
        assertEquals(-1L, kd.deserializeKey("-1", null));
        assertEquals(Long.MAX_VALUE, kd.deserializeKey("9223372036854775807", null));
        assertNull(kd.deserializeKey("9223372036854775808", null));
    }

    @Test
    public void testFloatKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
        assertEquals(1.0f, kd.deserializeKey("1.0", null), 0.01f);
        assertEquals(Float.NaN, kd.deserializeKey("NaN", null));
        assertEquals(Float.POSITIVE_INFINITY, kd.deserializeKey("Infinity", null));
        assertEquals(Float.NEGATIVE_INFINITY, kd.deserializeKey("-Infinity", null));
    }

    @Test
    public void testDoubleKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, kd.deserializeKey("1.0", null), 0.01);
        assertEquals(Double.NaN, kd.deserializeKey("NaN", null));
        assertEquals(Double.POSITIVE_INFINITY, kd.deserializeKey("Infinity", null));
        assertEquals(Double.NEGATIVE_INFINITY, kd.deserializeKey("-Infinity", null));
    }

    @Test
    public void testLocaleKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
        Locale expected = new Locale("en", "US");
        assertEquals(expected, kd.deserializeKey("en_US", null));
    }

    @Test
    public void testDateKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
        Date expected = new Date(1234567890000L);
        assertEquals(expected, kd.deserializeKey("2009-12-31T23:59:56.890Z", null));
    }

    @Test
    public void testCalendarKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
        Calendar expected = Calendar.getInstance();
        expected.set(Calendar.YEAR, 2009);
        expected.set(Calendar.MONTH, Calendar.DECEMBER);
        expected.set(Calendar.DAY_OF_MONTH, 31);
        expected.set(Calendar.HOUR_OF_DAY, 23);
        expected.set(Calendar.MINUTE, 59);
        expected.set(Calendar.SECOND, 56);
        assertEquals(expected, kd.deserializeKey("2009-12-31T23:59:56Z", null));
    }

    @Test
    public void testUUIDKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);
        UUID expected = UUID.fromString("123e4567-89ab-cdef-1234-567890abcde");
        assertEquals(expected, kd.deserializeKey("123e4567-89ab-cdef-1234-567890abcde", null));
    }

    @Test
    public void testURIPKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
        URI expected = new URI("http://example.com/test?foo=bar");
        assertEquals(expected, kd.deserializeKey("http://example.com/test?foo=bar", null));
    }

    @Test
    public void testURLPKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
        URL expected = new URL("http://example.com/test?foo=bar");
        assertEquals(expected, kd.deserializeKey("http://example.com/test?foo=bar", null));
    }

    @Test
    public void testClassPKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, kd.deserializeKey("java.lang.String", null));
    }

    @Test
    public void testByteArrayPKD() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        KeyDeserializer kd = StdKeyDeserializer.forType(byte[].class);
        String encoded = "VG8gZXhpcyBpcyBhIGlzIG1lc3NhZ2UgYSBmaWxlIGlucHV0IGlucHV0IHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] expected = mapper.getConfig().getBase64Variant().decode(encoded);
        assertEquals(expected, kd.deserializeKey(encoded, null));
    }

    @Test
    public void testEnumKD() throws Exception {
        EnumResolver enumResolver = EnumResolver.construct(MyEnum.class, null);
        KeyDeserializer kd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class, enumResolver);
        assertEquals(MyEnum.ONE, kd.deserializeKey("ONE", null));
        assertEquals(MyEnum.TWO, kd.deserializeKey("TWO", null));
    }

    @Test
    public void testDelegatingKD() throws Exception {
        JsonDeserializer<MyBean> beanDeserializer = new MyBeanDeserializer();
        KeyDeserializer kd = new StdKeyDeserializer.DelegatingKD(MyBean.class, beanDeserializer);
        String key = "test";
        assertSame(beanDeserializer, kd.getDelegate());
        assertEquals(new MyBean(), kd.deserializeKey(key, null));
    }

    @Test
    public void testEnumFromStringKD() throws Exception {
        EnumResolver enumResolver = EnumResolver.construct(MyEnum.class, null);
        KeyDeserializer kd = new StdKeyDeserializer.EnumKD(enumResolver, null);
        assertEquals(MyEnum.ONE, kd.deserializeKey("ONE", null));
        assertEquals(MyEnum.TWO, kd.deserializeKey("TWO", null));
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Constructor<MyBean> constructor = MyBean.class.getDeclaredConstructor(String.class);
        KeyDeserializer kd = new StdKeyDeserializer.StringFactoryKeyDeserializer(constructor);
        String key = "test";
        assertEquals(new MyBean(key), kd.deserializeKey(key, null));
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Constructor<MyBean> constructor = MyBean.class.getDeclaredConstructor(String.class);
        KeyDeserializer kd = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        String key = "test";
        assertEquals(new MyBean(key), kd.deserializeKey(key, null));
    }

    private static class MyBean {
        private final String name;

        public MyBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static class MyBeanDeserializer extends JsonDeserializer<MyBean> {
            @Override
            public MyBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String name = p.getValueAsString();
                return new MyBean(name);
            }
        }
    }

    private static enum MyEnum {
        ONE,
        TWO
    }
}