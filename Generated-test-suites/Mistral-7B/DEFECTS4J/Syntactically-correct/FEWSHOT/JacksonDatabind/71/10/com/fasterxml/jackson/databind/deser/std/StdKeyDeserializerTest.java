package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test cases for {@link StdKeyDeserializer} class.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyForBasicTypes() throws Exception {
        // Test deserialization for basic types
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        serializer = StdKeyDeserializer.forType(Byte.class);
        key = serializer.deserializeKey("1", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) 1, key);

        serializer = StdKeyDeserializer.forType(Short.class);
        key = serializer.deserializeKey("2147483647", new TestContext()); // maximum short value
        assertTrue(key instanceof Short);
        assertEquals((short) 32767, key);

        serializer = StdKeyDeserializer.forType(Character.class);
        key = serializer.deserializeKey("A", new TestContext());
        assertTrue(key instanceof Character);
        assertEquals('A', key);

        serializer = StdKeyDeserializer.forType(Integer.class);
        key = serializer.deserializeKey("12345", new TestContext());
        assertTrue(key instanceof Integer);
        assertEquals(12345, key);

        serializer = StdKeyDeserializer.forType(Long.class);
        key = serializer.deserializeKey("-123456789012345", new TestContext()); // maximum long value
        assertTrue(key instanceof Long);
        assertEquals(-123456789012345L, key);

        serializer = StdKeyDeserializer.forType(Float.class);
        key = serializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertTrue(key instanceof Float);
        assertEquals(3.14159265358979323846f, key, 0.001f);

        serializer = StdKeyDeserializer.forType(Double.class);
        key = serializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertTrue(key instanceof Double);
        assertEquals(3.14159265358979323846, key, 0.000001);

        serializer = StdKeyDeserializer.forType(Locale.class);
        Object localeKey = serializer.deserializeKey("en_US", new TestContext());
        assertTrue(localeKey instanceof Locale);
        assertEquals(Locale.US, localeKey);

        serializer = StdKeyDeserializer.forType(Date.class);
        Object dateKey = serializer.deserializeKey("2023-03-01", new TestContext());
        assertTrue(dateKey instanceof Date);
        assertEquals("Thu Mar 02 00:00:00 EST 2023", dateKey.toString());

        serializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendarKey = (Calendar) serializer.deserializeKey("2023-03-01", new TestContext());
        assertEquals(2023, calendarKey.get(Calendar.YEAR));
        assertEquals(3, calendarKey.get(Calendar.MONTH) + 1); // month is 0-indexed in Calendar
        assertEquals(1, calendarKey.get(Calendar.DAY_OF_MONTH));

        serializer = StdKeyDeserializer.forType(URI.class);
        Object uriKey = serializer.deserializeKey("http://www.example.com", new TestContext());
        assertTrue(uriKey instanceof URI);
        assertEquals("http://www.example.com", uriKey.toString());

        serializer = StdKeyDeserializer.forType(URL.class);
        Object urlKey = serializer.deserializeKey("http://www.example.com", new TestContext());
        assertTrue(urlKey instanceof URL);
        assertEquals("http://www.example.com", urlKey.toString());
    }

    @Test
    public void testDeserializeKeyForCustomClass() throws Exception {
        // Test deserialization for a custom class
        class MyCustomClass {
            private final int value;

            @JsonCreator
            public MyCustomClass(@JsonValue int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyCustomClass.class);
        Object key = serializer.deserializeKey("123", new TestContext());
        assertTrue(key instanceof MyCustomClass);
        assertEquals(123, ((MyCustomClass) key).getValue());
    }

    @Test
    public void testDeserializeKeyForEnum() throws Exception {
        // Test deserialization for an enum
        enum MyEnum {
            VALUE_1,
            VALUE_2,
            @JsonValue
            VALUE_3
        }

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        Object key = serializer.deserializeKey("VALUE_2", new TestContext());
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.VALUE_2, key);

        key = serializer.deserializeKey("VALUE_X", new TestContext()); // unknown value
        assertNull(key);
    }

    @Test
    public void testDeserializeKeyForClassByString() throws Exception {
        // Test deserialization for a class by its string representation
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey("java.lang.String", new TestContext());
        assertTrue(key instanceof String);
        assertEquals("java.lang.String", key.getClass().getName());
    }

    @Test
    public void testDeserializeKeyForClassByConstructor() throws Exception {
        // Test deserialization for a class using a constructor
        class MyClassWithCustomConstructor {
            private final int value;

            public MyClassWithCustomConstructor(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        Constructor<MyClassWithCustomConstructor> constructor = MyClassWithCustomConstructor.class.getDeclaredConstructor(Integer.TYPE);
        constructor.setAccessible(true);

        StdKeyDeserializer serializer = new StringCtorKeyDeserializer(constructor);
        Object key = serializer.deserializeKey("42", new TestContext());
        assertTrue(key instanceof MyClassWithCustomConstructor);
        assertEquals(42, ((MyClassWithCustomConstructor) key).getValue());
    }

    @Test
    public void testDeserializeKeyForClassByFactoryMethod() throws Exception {
        // Test deserialization for a class using a static factory method
        class MyClassWithFactoryMethod {
            private final int value;

            @JsonCreator
            public static MyClassWithFactoryMethod create(int value) {
                return new MyClassWithFactoryMethod(value);
            }

            private MyClassWithFactoryMethod(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        Method factoryMethod = MyClassWithFactoryMethod.class.getDeclaredMethod("create", Integer.TYPE);
        factoryMethod.setAccessible(true);

        StdKeyDeserializer serializer = new StringFactoryKeyDeserializer(factoryMethod);
        Object key = serializer.deserializeKey("42", new TestContext());
        assertTrue(key instanceof MyClassWithFactoryMethod);
        assertEquals(42, ((MyClassWithFactoryMethod) key).getValue());
    }

    @Test
    public void testDeserializeKeyForClassWithDelegatingDeserializer() throws Exception {
        // Test deserialization for a class using a delegating deserializer
        class MyDelegateDeserializer extends KeyDeserializer {
            private final JsonDeserializer<?> _delegate;

            public MyDelegateDeserializer(JsonDeserializer<?> delegate) {
                _delegate = delegate;
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return _delegate.deserialize(ctxt.getParser(), ctxt);
            }
        }

        class MyClassWithDelegateDeserializer {
            private final String _value;

            @JsonDeserialize(using = MyDelegateDeserializer.class)
            public String getValue() {
                return _value;
            }

            public MyClassWithDelegateDeserializer(@JsonValue String value) {
                _value = value;
            }
        }

        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<>(String.class);
        StdKeyDeserializer serializer = new DelegatingKD(MyClassWithDelegateDeserializer.class, stringDeserializer);
        Object key = serializer.deserializeKey("Hello World", new TestContext());
        assertTrue(key instanceof MyClassWithDelegateDeserializer);
        assertEquals("Hello World", ((MyClassWithDelegateDeserializer) key).getValue());
    }

    @Test
    public void testDeserializeKeyForCustomEnumDeserializer() throws Exception {
        // Test deserialization for a custom enum deserializer
        class MyEnumDeserializer extends KeyDeserializer {
            private final EnumResolver _enumResolver;

            public MyEnumDeserializer(EnumResolver enumResolver) {
                _enumResolver = enumResolver;
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return _enumResolver.findEnumValue(ctxt.findContextualType(ctxt.getCurrentNameContext(), MyEnum.class), key);
            }
        }

        class MyEnum {
            @JsonValue
            public static MyEnum fromString(String key) {
                return EnumSet.allOf(MyEnum.class).stream()
                        .filter(e -> e.name().equalsIgnoreCase(key))
                        .findFirst()
                        .orElse(null);
            }
        }

        EnumResolver enumResolver = new EnumResolver(MyEnum.class);
        StdKeyDeserializer serializer = new EnumKD(enumResolver, AnnotatedMethod.noOp());
        Object key = serializer.deserializeKey("VALUE_2", new TestContext());
        assertTrue(key instanceof MyEnum);
        assertEquals(MyEnum.VALUE_2, key);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object findValueIfPresent(String name, Class<?> valueType) {
            return null;
        }

        @Override
        public Object findValueIfPresent(AnnotatedMember am) {
            return null;
        }

        @Override
        public Object findValueIfPresent(AnnotatedField af) {
            return null;
        }

        @Override
        public Object findValueIfPresent(AnnotatedMethod am) {
            return null;
        }

        @Override
        public Object findValueIfPresent(AnnotatedConstructor ac) {
            return null;
        }

        @Override
        public Object findClass(String className) throws IOException, InstantiationException, IllegalAccessException {
            return Class.forName(className).newInstance();
        }
    }
}