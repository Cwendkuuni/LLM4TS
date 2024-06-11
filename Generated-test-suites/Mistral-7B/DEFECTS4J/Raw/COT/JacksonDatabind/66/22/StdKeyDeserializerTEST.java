package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringKD;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper mapper;
    private StdKeyDeserializer stringKD;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
        stringKD = StringKD.forType(String.class);
    }

    @Test
    public void testDeserializeKey_String() throws Exception {
        String key = "someString";
        Object result = stringKD.deserializeKey(key, context);
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Null() throws Exception {
        Object result = stringKD.deserializeKey(null, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_Invalid() throws Exception {
        String key = "invalidKey";
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testParse_Boolean() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)._parse("true", context);
        assertEquals(Boolean.TRUE, result);

        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)._parse("false", context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidBoolean() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)._parse("invalid", context);
    }

    @Test
    public void testParse_Byte() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class)._parse("127", context);
        assertEquals((byte) 127, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidByte() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class)._parse("-129", context);
    }

    @Test
    public void testParse_Short() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class)._parse("32767", context);
        assertEquals((short) 32767, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidShort() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class)._parse("32769", context);
    }

    @Test
    public void testParse_Character() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class)._parse("'A'", context);
        assertEquals('A', result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidCharacter() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class)._parse(" invalid", context);
    }

    @Test
    public void testParse_Int() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class)._parse("123456", context);
        assertEquals(123456, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidInt() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class)._parse("123457", context);
    }

    @Test
    public void testParse_Long() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class)._parse("12345678901234567890", context);
        assertEquals(12345678901234567890L, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidLong() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class)._parse("12345678901234567891", context);
    }

    @Test
    public void testParse_Float() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class)._parse("123.45", context);
        assertEquals(123.45f, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidFloat() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class)._parse("123.46", context);
    }

    @Test
    public void testParse_Double() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class)._parse("123.456", context);
        assertEquals(123.456, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidDouble() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class)._parse("123.457", context);
    }

    @Test
    public void testParse_Locale() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, new LocaleDeserializer())._parse("en_US", context);
        assertEquals(Locale.US, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidLocale() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, new LocaleDeserializer())._parse("invalid", context);
    }

    @Test
    public void testParse_Enum() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, MyEnum.class, new MyEnumDeserializer())._parse("VALUE_1", context);
        assertEquals(MyEnum.VALUE_1, result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidEnum() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, MyEnum.class, new MyEnumDeserializer())._parse("INVALID_VALUE", context);
    }

    @Test
    public void testParse_URL() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class)._parse("http://www.example.com", context);
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test(expected = MalformedURLException.class)
    public void testParse_InvalidURL() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class)._parse("http://www.example.com/invalid", context);
    }

    @Test
    public void testParse_URI() throws Exception {
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class)._parse("http://www.example.com", context);
        assertEquals(new URI("http://www.example.com"), result);
    }

    @Test(expected = IOException.class)
    public void testParse_InvalidURI() throws Exception {
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class)._parse("http://www.example.com/invalid", context);
    }

    @Test
    public void testParse_Class() throws Exception {
        Class<?> clazz = Class.forName("java.lang.String");
        Object result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, clazz)._parse("java.lang.String", context);
        assertEquals(clazz, result.getClass());
    }

    @Test(expected = Exception.class)
    public void testParse_InvalidClass() throws Exception {
        Class<?> clazz = Class.forName("InvalidClass");
        new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, clazz)._parse("java.lang.String", context);
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue("{\"key\":\"VALUE_1\"}", MyPojo.class);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
        assertEquals(MyEnum.VALUE_1, deserializer.deserializeKey("VALUE_1", context));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue("{\"key\":\"INVALID_VALUE\"}", MyPojo.class);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
        deserializer.deserializeKey("INVALID_VALUE", context);
    }

    private static class MyEnum {
        public static final MyEnum VALUE_1 = new MyEnum();
        public static final MyEnum VALUE_2 = new MyEnum();

        private MyEnum() {
        }
    }

    private static class MyEnumDeserializer extends StdKeyDeserializer.EnumKD {
        public MyEnumDeserializer() {
            super(new EnumResolver() {
                @Override
                public Enum<?> findEnum(String name) {
                    if ("VALUE_1".equals(name)) {
                        return MyEnum.VALUE_1;
                    }
                    if ("VALUE_2".equals(name)) {
                        return MyEnum.VALUE_2;
                    }
                    return null;
                }
            }, null);
        }
    }

    private static class MyPojo {
        private MyEnum key;

        public MyEnum getKey() {
            return key;
        }

        public void setKey(MyEnum key) {
            this.key = key;
        }
    }

    @Test
    public void testDeserializeKey_NullValueProvider() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        deserializer.setNullValueProvider(NullValueProvider.NULL_VALUE);
        Object result = deserializer.deserializeKey(null, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_NullValueHandler() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        deserializer.setNullValueHandler(new StdKeyDeserializer.NullKeyDeserializer());
        Object result = deserializer.deserializeKey(null, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_EmptyString() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        deserializer.deserializeKey("", context);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_Invalid() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        deserializer.deserializeKey("invalid", context);
    }

    @Test
    public void testDeserializeKey_Constructor() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class,
                new StringCtorKeyDeserializer(Integer.class));
        Object result = deserializer.deserializeKey("123", context);
        assertEquals(123, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_Constructor_Invalid() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class,
                new StringCtorKeyDeserializer(Integer.class));
        deserializer.deserializeKey("1234", context);
    }

    @Test
    public void testDeserializeKey_FactoryMethod() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class,
                new StringFactoryKeyDeserializer(Integer.class, "createIntKey"));
        Object result = deserializer.deserializeKey("123", context);
        assertEquals(123, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_FactoryMethod_Invalid() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class,
                new StringFactoryKeyDeserializer(Integer.class, "createIntKey"));
        deserializer.deserializeKey("1234", context);
    }
}