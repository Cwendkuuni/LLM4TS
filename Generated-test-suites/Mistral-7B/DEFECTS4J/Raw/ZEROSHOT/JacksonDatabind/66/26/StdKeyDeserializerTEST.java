package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializerTest;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
public class StdKeyDeserializerTest extends StdKeyDeserializerTest {

    @Test
    public void testDefaultSerialization() throws Exception {
        // Test default serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object key = keyDeserializer.deserializeKey("test", objectMapper);
        assertEquals("test", key);
    }

    @Test
    public void testBooleanSerialization() throws Exception {
        // Test boolean serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", objectMapper);
        assertTrue((Boolean) key);

        keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        key = keyDeserializer.deserializeKey("false", objectMapper);
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteSerialization() throws Exception {
        // Test byte serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("127", objectMapper);
        assertEquals((byte) 127, key);

        try {
            keyDeserializer = StdKeyDeserializer.forType(Byte.class);
            key = keyDeserializer.deserializeKey("128", objectMapper);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortSerialization() throws Exception {
        // Test short serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("32767", objectMapper);
        assertEquals((short) 32767, key);

        try {
            keyDeserializer = StdKeyDeserializer.forType(Short.class);
            key = keyDeserializer.deserializeKey("32768", objectMapper);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharSerialization() throws Exception {
        // Test char serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("A", objectMapper);
        assertEquals('A', key);

        try {
            keyDeserializer = StdKeyDeserializer.forType(Character.class);
            key = keyDeserializer.deserializeKey("B", objectMapper);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntSerialization() throws Exception {
        // Test int serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("42", objectMapper);
        assertEquals(42, key);
    }

    @Test
    public void testLongSerialization() throws Exception {
        // Test long serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("9223372036854775807", objectMapper);
        assertEquals(9223372036854775807L, key);

        try {
            keyDeserializer = StdKeyDeserializer.forType(Long.class);
            key = keyDeserializer.deserializeKey("9223372036854775808", objectMapper);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testFloatSerialization() throws Exception {
        // Test float serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("3.141592653589793", objectMapper);
        assertEquals(3.141592653589793f, key, 0.0000001f);
    }

    @Test
    public void testDoubleSerialization() throws Exception {
        // Test double serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("6.283185307179586e+01", objectMapper);
        assertEquals(6.283185307179586, key, 0.0000001);
    }

    @Test
    public void testLocaleSerialization() throws Exception {
        // Test locale serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = keyDeserializer.deserializeKey("en_US", objectMapper);
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateSerialization() throws Exception {
        // Test date serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object key = keyDeserializer.deserializeKey("2022-03-01", objectMapper);
        assertEquals(new Date(1646352000000L), key);
    }

    @Test
    public void testCalendarSerialization() throws Exception {
        // Test calendar serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = keyDeserializer.deserializeKey("2022-03-01", objectMapper);
        assertEquals(Calendar.getInstance(), key);
    }

    @Test
    public void testUUIDSerialization() throws Exception {
        // Test UUID serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = keyDeserializer.deserializeKey("110e66bf-7ebb-4c22-945d-826e1f6d2e71", objectMapper);
        assertEquals(new UUID("110e66bf-7ebb-4c22-945d-826e1f6d2e71"), key);
    }

    @Test
    public void testURISerialization() throws Exception {
        // Test URI serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("http://localhost:8080", objectMapper);
        assertEquals(new URI("http://localhost:8080"), key);
    }

    @Test
    public void testURLSerialization() throws Exception {
        // Test URL serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("http://localhost:8080", objectMapper);
        assertEquals(new URL("http://localhost:8080"), key);
    }

    @Test
    public void testClassSerialization() throws Exception {
        // Test class serialization behavior
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        Object key = keyDeserializer.deserializeKey("[Ljava.lang.String;", objectMapper);
        assertEquals(new String[]{"test"}, key);
    }

    @Test
    public void testEnumSerialization() throws Exception {
        // Test enum serialization behavior
        EnumResolver enumResolver = new EnumResolver();
        AnnotatedClass enumClass = objectMapper.getTypeFactory().constructType(MyEnum.class);
        AnnotatedMethod factoryMethod = enumClass.getDeclaredMethod("valueOf", String.class);

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.EnumKD(enumResolver, factoryMethod);
        Object key = keyDeserializer.deserializeKey("MY_ENUM_VALUE", objectMapper);
        assertEquals(MyEnum.MY_ENUM_VALUE, key);
    }

    @Test
    public void testConstructorSerialization() throws Exception {
        // Test constructor serialization behavior
        MyClass myClass = new MyClass();
        Constructor<MyClass> constructor = myClass.getClass().getDeclaredConstructor(String.class);

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        Object key = keyDeserializer.deserializeKey("MyClassTest", objectMapper);
        assertEquals(myClass, key);
    }

    @Test
    public void testFactoryMethodSerialization() throws Exception {
        // Test factory method serialization behavior
        MyClass myClass = new MyClass();
        Method factoryMethod = MyClass.class.getDeclaredMethod("createInstance", String.class);

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);
        Object key = keyDeserializer.deserializeKey("MyClassTest", objectMapper);
        assertEquals(myClass, key);
    }

    private static class MyEnum {
        public static final MyEnum MY_ENUM_VALUE = new MyEnum();

        private MyEnum() {
        }

        public static MyEnum valueOf(String name) {
            return MY_ENUM_VALUE;
        }
    }

    private static class MyClass {
        private MyClass() {
        }

        public static MyClass createInstance(String name) {
            return new MyClass();
        }
    }
}