package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringKD;
import com.fasterxml.jackson.databind.deser.std.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.EnumKD;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

/**
 * Test cases for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest {

    @JacksonStdImpl
    static class TestEnum {
        private final String name;

        TestEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class TestEnumResolver extends EnumResolver {
        private final Class<TestEnum> _enumClass;

        TestEnumResolver(Class<TestEnum> enumClass) {
            super(enumClass);
            _enumClass = enumClass;
        }

        @Override
        public TestEnum findEnum(String name) {
            return EnumSet.allOf(_enumClass).stream()
                    .filter(e -> e.name().equals(name))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public TestEnum getDefaultValue() {
            return EnumSet.valueOf(TestEnum.VALUE1);
        }
    }

    @Test
    public void testStringDeserializer() {
        KeyDeserializer deserializer = StringKD.forType(String.class);
        Object key = deserializer.deserializeKey("test", new TestContext());
        assertEquals("test", key);
    }

    @Test
    public void testObjectDeserializer() {
        KeyDeserializer deserializer = StringKD.forType(Object.class);
        Object key = deserializer.deserializeKey("test", new TestContext());
        assertSame(String.class, key.getClass());
        assertEquals("test", key.toString());
    }

    @Test
    public void testBooleanDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);
        key = deserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertEquals((byte) 1, key);

        try {
            key = deserializer.deserializeKey("256", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertEquals((short) 1, key);

        try {
            key = deserializer.deserializeKey("32768", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", new TestContext());
        assertEquals('A', key);
    }

    @Test
    public void testIntDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertEquals(1, key);
    }

    @Test
    public void testLongDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertEquals(1L, key);
    }

    @Test
    public void testFloatDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("1.0", new TestContext());
        assertEquals(1.0f, key);
    }

    @Test
    public void testDoubleDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("1.0", new TestContext());
        assertEquals(1.0, key);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object key = deserializer.deserializeKey("2023-03-01", new TestContext());
        assertEquals("Thu Mar 02 00:00:00 CET 2023", key.toString());
    }

    @Test
    public void testCalendarDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = deserializer.deserializeKey("2023-03-01", new TestContext());
        assertEquals(2023, ((Calendar) key).get(Calendar.YEAR));
        assertEquals(2, ((Calendar) key).get(Calendar.MONTH) + 1);
        assertEquals(1, ((Calendar) key).get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUUIDDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = deserializer.deserializeKey("123e4567-789b-12d3-a456-426614174000", new TestContext());
        assertEquals("123e4567-789b-12d3-a456-426614174000", key.toString());
    }

    @Test
    public void testURIDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("http://example.com/test", new TestContext());
        assertEquals("http://example.com/test", key.toString());
    }

    @Test
    public void testURIDeserializerWithMalformedURL() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        try {
            Object key = deserializer.deserializeKey("http://example.com/test%", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testURIDeserializerWithNull() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testURLDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("http://example.com/test", new TestContext());
        assertEquals("http://example.com/test", ((URL) key).toString());
    }

    @Test
    public void testURLDeserializerWithMalformedURL() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        try {
            Object key = deserializer.deserializeKey("http://example.com/test%", new TestContext());
            fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testURLDeserializerWithNull() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testClassDeserializer() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key.getClass());
    }

    @Test
    public void testClassDeserializerWithInvalidClass() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        try {
            Object key = deserializer.deserializeKey("java.lang.Integer", new TestContext());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testClassDeserializerWithNull() {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey(null, new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumDeserializer() throws IOException {
        Class<TestEnum> enumClass = TestEnum.class;
        KeyDeserializer deserializer = EnumKD.forType(enumClass);
        TestEnumResolver resolver = new TestEnumResolver(enumClass);

        Object key = deserializer.deserializeKey("VALUE1", new TestContext());
        assertEquals(TestEnum.VALUE1, key);

        key = deserializer.deserializeKey("VALUE2", new TestContext());
        assertEquals(TestEnum.VALUE2, key);

        key = deserializer.deserializeKey("VALUE3", new TestContext());
        assertEquals(TestEnum.VALUE3, key);

        try {
            key = deserializer.deserializeKey("INVALID", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        TestContext context = new TestContext() {
            @Override
            public EnumResolver getEnumResolver() {
                return resolver;
            }
        };
        try {
            key = deserializer.deserializeKey("VALUE4", context);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        TestEnum defaultValue = resolver.getDefaultValue();
        key = deserializer.deserializeKey(defaultValue.toString(), context);
        assertEquals(defaultValue, key);
    }

    @Test
    public void testByteArrayDeserializer() throws JsonProcessingException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(byte[].class);
        Object key = deserializer.deserializeKey("RGVjb2RlIGlzIG5vdCBzbyBpbiBhIG5vdCBzbyBpbiB0aGUtc2Vjb25kIGZvdWw=", new TestContext());
        byte[] expectedBytes = new byte[] { 72, 101, 108, 108, 101, 32, 76, 97, 111, 114, 101, 32, 67, 101, 115, 115, 116, 32, 66, 103, 111, 111, 117, 108, 100 };
        assertArrayEquals(expectedBytes, key);
    }

    @Test
    public void testConstructorDeserializer() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> enumClass = TestEnum.class;
        Constructor<TestEnum> constructor = enumClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        KeyDeserializer deserializer = new StringCtorKeyDeserializer(constructor);
        Object key = deserializer.deserializeKey("VALUE1", new TestContext());
        assertEquals(TestEnum.VALUE1, key);
    }

    @Test
    public void testStaticMethodDeserializer() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> enumClass = TestEnum.class;
        Method method = enumClass.getDeclaredMethod("getValueByString", String.class);
        method.setAccessible(true);

        KeyDeserializer deserializer = new StringFactoryKeyDeserializer(method);
        Object key = deserializer.deserializeKey("VALUE1", new TestContext());
        assertEquals(TestEnum.VALUE1, key);
    }

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectInstanceFinder _instanceFinder() {
            return super._instanceFinder();
        }

        @Override
        public ObjectMapper _rootValueInstance() {
            return super._rootValueInstance();
        }

        @Override
        public JsonParser _parser() {
            return super._parser();
        }

        @Override
        public Class<?> _activeType() {
            return super._activeType();
        }

        @Override
        public BeanDescription _beanDesc() {
            return super._beanDesc();
        }

        @Override
        public PropertyName _propertyName() {
            return super._propertyName();
        }

        @Override
        public JsonDeserializer<Object> _valueDeserializer() {
            return super._valueDeserializer();
        }

        @Override
        public AnnotationIntrospector _annotationIntrospector() {
            return super._annotationIntrospector();
        }

        @Override
        public EnumResolver getEnumResolver() {
            return null;
        }
    }
}