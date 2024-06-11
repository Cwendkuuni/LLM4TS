package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.MethodName;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws Exception {
        StdKeyDeserializer ssd = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals("key", ssd.deserializeKey("key", null));
        assertEquals("key", ssd.deserializeKey("key", null));
        assertEquals("key", ssd.deserializeKey("key", null));
        assertEquals("key", ssd.deserializeKey("key", null));
    }

    @Test
    public void testStringKD_Object() throws Exception {
        StdKeyDeserializer ssd = StdKeyDeserializer.StringKD.forType(Object.class);
        assertEquals("key", ssd.deserializeKey("key", null));
        assertEquals("key", ssd.deserializeKey("key", null));
        assertEquals("key", ssd.deserializeKey("key", null));
        assertEquals("key", ssd.deserializeKey("key", null));
    }

    @Test
    public void testBooleanTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        Object result = ssd.deserializeKey("true", null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        result = ssd.deserializeKey("false", null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        result = ssd.deserializeKey("false-value", null);
        assertNull(result);
    }

    @Test
    public void testByteTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        Byte result = (Byte) ssd.deserializeKey("127", null);
        assertEquals((byte) 127, result);

        try {
            result = (Byte) ssd.deserializeKey("256", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        Short result = (Short) ssd.deserializeKey("32767", null);
        assertEquals((short) 32767, result);

        try {
            result = (Short) ssd.deserializeKey("32768", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        Character result = (Character) ssd.deserializeKey("'a'", null);
        assertEquals('a', result);

        try {
            result = (Character) ssd.deserializeKey("'b'c'", null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Integer result = (Integer) ssd.deserializeKey("12345", null);
        assertEquals(12345, result);
    }

    @Test
    public void testLongTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        Long result = (Long) ssd.deserializeKey("123456789012345", null);
        assertEquals(123456789012345L, result);
    }

    @Test
    public void testFloatTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        Float result = (Float) ssd.deserializeKey("123.45", null);
        assertEquals(123.45f, result);
    }

    @Test
    public void testDoubleTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        Double result = (Double) ssd.deserializeKey("123.45", null);
        assertEquals(123.45, result);
    }

    @Test
    public void testLocaleTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        Locale result = (Locale) ssd.deserializeKey("en_US", null);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCurrencyTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        Currency result = (Currency) ssd.deserializeKey("EUR", null);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDateTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        Date result = (Date) ssd.deserializeKey("2023-03-01", null);
        assertEquals("Thu Mar 01 00:00:00 EST 2023", result.toString());
    }

    @Test
    public void testCalendarTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        Calendar result = (Calendar) ssd.deserializeKey("2023-03-01", null);
        assertEquals(2023, result.get(Calendar.YEAR));
        assertEquals(3, result.get(Calendar.MONTH));
        assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUUIDTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        UUID result = (UUID) ssd.deserializeKey("123e4567-785b-11d1-80b4-00c0ap9f1cd5", null);
        assertEquals("123e4567-785b-11d1-80b4-00c0ap9f1cd5", result.toString());
    }

    @Test
    public void testURITypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        URI result = (URI) ssd.deserializeKey("https://example.com/path/to/resource", null);
        assertEquals("https://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testURLTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        URL result = (URL) ssd.deserializeKey("https://example.com/path/to/resource", null);
        assertEquals("https://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testClassTypeKD() throws Exception {
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        Class<?> result = (Class<?>) ssd.deserializeKey("java.lang.String", null);
        assertEquals(String.class, result);
    }

    @Test
    public void testEnumTypeKD() throws Exception {
        EnumResolver enumResolver = EnumResolver.constructUnsafe(MyEnum.class);
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class, enumResolver);

        Object result = ssd.deserializeKey("ENUM_VALUE_1", null);
        assertEquals(MyEnum.ENUM_VALUE_1, result);

        result = ssd.deserializeKey("ENUM_VALUE_2", null);
        assertEquals(MyEnum.ENUM_VALUE_2, result);

        result = ssd.deserializeKey("INVALID_VALUE", null);
        assertNull(result);
    }

    @Test
    public void testDelegateKD() throws Exception {
        DeserializationConfig config = new DeserializationConfig();
        JsonFactory factory = new JsonFactory(config);
        ObjectMapper mapper = new ObjectMapper(factory);

        JsonDeserializer<MyClass> delegate = new JsonDeserializer<MyClass>() {
            @Override
            public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new MyClass("DELEGATED_KEY");
            }
        };
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, MyClass.class, delegate);

        Object result = ssd.deserializeKey("DELEGATED_KEY", null);
        assertEquals(new MyClass("DELEGATED_KEY"), result);

        result = ssd.deserializeKey("INVALID_KEY", null);
        fail();
    }

    @Test
    public void testToStringDeserializerKD() throws Exception {
        EnumResolver enumResolver = EnumResolver.constructUnsafe(MyEnum.class);
        StdKeyDeserializer ssd = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class, enumResolver, true);

        Object result = ssd.deserializeKey("TO_STRING_ENUM_VALUE_1", null);
        assertEquals(MyEnum.TO_STRING_ENUM_VALUE_1, result);

        result = ssd.deserializeKey("TO_STRING_ENUM_VALUE_2", null);
        assertEquals(MyEnum.TO_STRING_ENUM_VALUE_2, result);

        result = ssd.deserializeKey("INVALID_VALUE", null);
        assertNull(result);
    }

    @Test
    public void testConstructorKeyDeserializer() throws Exception {
        Class<?> clazz = MyClassWithConstructor.class;
        Constructor<MyClassWithConstructor> constructor = clazz.getDeclaredConstructor(String.class);
        StdKeyDeserializer ssd = new StdKeyDeserializer(constructor);

        Object result = ssd.deserializeKey("CONSTRUCTOR_KEY", null);
        assertEquals(new MyClassWithConstructor("CONSTRUCTOR_KEY"), result);

        result = ssd.deserializeKey("INVALID_KEY", null);
        fail();
    }

    @Test
    public void testFactoryMethodKeyDeserializer() throws Exception {
        Class<?> clazz = MyClassWithFactoryMethod.class;
        MethodName factoryMethodName = new MethodName("createInstance");
        Method factoryMethod = clazz.getDeclaredMethod(factoryMethodName.getMethodName(), String.class);
        StdKeyDeserializer ssd = new StdKeyDeserializer(factoryMethod);

        Object result = ssd.deserializeKey("FACTORY_METHOD_KEY", null);
        assertEquals(new MyClassWithFactoryMethod("FACTORY_METHOD_KEY"), result);

        result = ssd.deserializeKey("INVALID_KEY", null);
        fail();
    }

    private static class MyClass {
        private final String key;

        public MyClass(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "key='" + key + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyClass that = (MyClass) o;
            return Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        public static final MyClass ENUM_VALUE_1 = new MyClass("ENUM_VALUE_1");
        public static final MyClass ENUM_VALUE_2 = new MyClass("ENUM_VALUE_2");
    }

    private static enum MyEnum {
        ENUM_VALUE_1,
        ENUM_VALUE_2,
        @JacksonStdImpl
        TO_STRING_ENUM_VALUE_1,
        @JacksonStdImpl
        TO_STRING_ENUM_VALUE_2
    }

    private static class MyClassWithConstructor {
        private final String key;

        MyClassWithConstructor(String key) {
            this.key = key;
        }

        public String toString() {
            return "MyClassWithConstructor{" +
                    "key='" + key + '\'' +
                    '}';
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyClassWithConstructor that = (MyClassWithConstructor) o;
            return Objects.equals(key, that.key);
        }

        public int hashCode() {
            return Objects.hash(key);
        }
    }

    private static class MyClassWithFactoryMethod {
        private final String key;

        MyClassWithFactoryMethod(String key) {
            this.key = key;
        }

        public String toString() {
            return "MyClassWithFactoryMethod{" +
                    "key='" + key + '\'' +
                    '}';
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyClassWithFactoryMethod that = (MyClassWithFactoryMethod) o;
            return Objects.equals(key, that.key);
        }

        public int hashCode() {
            return Objects.hash(key);
        }

        @JacksonStdImpl
        public static MyClassWithFactoryMethod createInstance(String key) {
            return new MyClassWithFactoryMethod(key);
        }
    }
}