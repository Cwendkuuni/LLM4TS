package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializerTest;

public class StdKeyDeserializerTest extends StdKeyDeserializerTest {

    private static final Object[] TEST_CASES = new Object[] {
            new Object() {
                String key = "true";
                Class<?> keyClass = Boolean.class;
                Object expected = Boolean.TRUE;
            },
            new Object() {
                String key = "false";
                Class<?> keyClass = Boolean.class;
                Object expected = Boolean.FALSE;
            },
            new Object() {
                String key = "123";
                Class<?> keyClass = Integer.class;
                Object expected = 123;
            },
            new Object() {
                String key = "1234567890";
                Class<?> keyClass = Long.class;
                Object expected = 1234567890L;
            },
            new Object() {
                String key = "123.5";
                Class<?> keyClass = Float.class;
                Object expected = 123.5f;
            },
            new Object() {
                String key = "123.56";
                Class<?> keyClass = Double.class;
                Object expected = 123.56;
            },
            new Object() {
                String key = "en_US";
                Class<?> keyClass = Locale.class;
                Object expected = Locale.US;
            },
            new Object() {
                String key = "Europe/London";
                Class<?> keyClass = ZoneId.class;
                Object expected = ZoneId.of("Europe/London");
            },
            new Object() {
                String key = "2022-02-28";
                Class<?> keyClass = LocalDate.class;
                Object expected = LocalDate.of(2022, 2, 28);
            },
            new Object() {
                String key = "2022-02-28T12:00:00.000Z";
                Class<?> keyClass = OffsetDateTime.class;
                Object expected = OffsetDateTime.of(2022, 2, 28, 12, 0, 0, 0, ZoneOffset.ZERO);
            },
            new Object() {
                String key = "2022-02-28T12:00:00.000+01:00";
                Class<?> keyClass = ZonedDateTime.class;
                Object expected = ZonedDateTime.of(2022, 2, 28, 13, 0, 0, 0, ZoneId.of("+01:00"));
            },
            new Object() {
                String key = "123.45";
                Class<?> keyClass = BigDecimal.class;
                Object expected = new BigDecimal("123.45");
            },
            new Object() {
                String key = "123.45E-2";
                Class<?> keyClass = BigDecimal.class;
                Object expected = new BigDecimal("123.45", new MathContext(2));
            },
            new Object() {
                String key = "java.util.Date";
                Class<?> keyClass = Date.class;
                Object expected = new Date(0L);
            },
            new Object() {
                String key = "java.util.Calendar";
                Class<?> keyClass = Calendar.class;
                Object expected = GregorianCalendar.getInstance();
            },
            new Object() {
                String key = "java.net.URI";
                Class<?> keyClass = URI.class;
                Object expected = new URI("http://example.com");
            },
            new Object() {
                String key = "java.net.URL";
                Class<?> keyClass = URL.class;
                Object expected = new URL("http://example.com");
            },
            new Object() {
                String key = "java.lang.String";
                Class<?> keyClass = String.class;
                Object expected = "test";
            },
            new Object() {
                String key = "java.util.UUID";
                Class<?> keyClass = UUID.class;
                Object expected = UUID.randomUUID();
            },
            new Object() {
                String key = "[1, 2, 3]";
                Class<?> keyClass = List.class;
                Object expected = Arrays.asList(1, 2, 3);
            },
            new Object() {
                String key = "{key1=value1, key2=value2}";
                Class<?> keyClass = Map.class;
                Object expected = new HashMap() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }};
            },
            new Object() {
                String key = "java.util.EnumSet[ELEMENT1, ELEMENT2]";
                Class<?> keyClass = EnumSet.class;
                Object expected = EnumSet.of(ELEMENT1, ELEMENT2);
            },
            new Object() {
                String key = "java.util.EnumMap[CLASS_NAME#ELEMENT1=VALUE1, CLASS_NAME#ELEMENT2=VALUE2]";
                Class<?> keyClass = EnumMap.class;
                Object expected = new EnumMap<>(CLASS_NAME.class) {{
                    put(ELEMENT1, VALUE1);
                    put(ELEMENT2, VALUE2);
                }};
            },
            new Object() {
                String key = "java.util.LinkedHashMap[KEY1=VALUE1, KEY2=VALUE2]";
                Class<?> keyClass = LinkedHashMap.class;
                Object expected = new LinkedHashMap() {{
                    put("KEY1", "VALUE1");
                    put("KEY2", "VALUE2");
                }};
            },
            new Object() {
                String key = "java.util.Hashtable[KEY1=VALUE1, KEY2=VALUE2]";
                Class<?> keyClass = Hashtable.class;
                Object expected = new Hashtable() {{
                    put("KEY1", "VALUE1");
                    put("KEY2", "VALUE2");
                }};
            },
            new Object() {
                String key = "java.util.Properties[property1=value1, property2=value2]";
                Class<?> keyClass = Properties.class;
                Object expected = new Properties() {{
                    setProperty("property1", "value1");
                    setProperty("property2", "value2");
                }};
            },
            new Object() {
                String key = "java.util.WeakHashMap[KEY1=VALUE1, KEY2=VALUE2]";
                Class<?> keyClass = WeakHashMap.class;
                Object expected = new WeakHashMap<Object, Object>() {{
                    put("KEY1", "VALUE1");
                    put("KEY2", "VALUE2");
                }};
            },
            new Object() {
                String key = "java.util.concurrent.ConcurrentHashMap[KEY1=VALUE1, KEY2=VALUE2]";
                Class<?> keyClass = ConcurrentHashMap.class;
                Object expected = new ConcurrentHashMap<Object, Object>() {{
                    put("KEY1", "VALUE1");
                    put("KEY2", "VALUE2");
                }};
            },
            new Object() {
                String key = "java.util.concurrent.CopyOnWriteArraySet[ELEMENT1, ELEMENT2]";
                Class<?> keyClass = CopyOnWriteArraySet.class;
                Object expected = new CopyOnWriteArraySet<Object>() {{
                    add(ELEMENT1);
                    add(ELEMENT2);
                }};
            },
            new Object() {
                String key = "java.util.EnumSet[ELEMENT1, ELEMENT2]";
                Class<?> keyClass = EnumSet.noneOf(EnumType.class);
                Object expected = EnumSet.noneOf(EnumType.class);
            },
            new Object() {
                String key = "java.util.EnumMap[CLASS_NAME#ELEMENT1=VALUE1, CLASS_NAME#ELEMENT2=VALUE2]";
                Class<?> keyClass = EnumMap.noneOf(EnumType.class);
                Object expected = EnumMap.noneOf(EnumType.class);
            },
            new Object() {
                String key = "java.lang.Class<java.util.Date>";
                Class<?> keyClass = Date.class;
                Object expected = Date.class;
            },
            new Object() {
                String key = "java.net.URI";
                Class<?> keyClass = URI.class;
                Object expected = URI.class;
            },
            new Object() {
                String key = "com.example.MyClass";
                Class<?> keyClass = MyClass.class;
                Object expected = MyClass.class;
            },
            new Object() {
                String key = "java.net.URL";
                Class<?> keyClass = URL.class;
                Object expected = URL.class;
            },
            new Object() {
                String key = "java.lang.String";
                Class<?> keyClass = String.class;
                Object expected = String.class;
            },
            new Object() {
                String key = "java.lang.String";
                Class<?> keyClass = Enum.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.lang.Enum<java.lang.String>";
                Class<?> keyClass = String.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.lang.Enum<java.lang.String>";
                Class<?> keyClass = Enum.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.EnumSet";
                Class<?> keyClass = EnumSet.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.EnumMap";
                Class<?> keyClass = EnumMap.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.HashMap";
                Class<?> keyClass = HashMap.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.LinkedHashMap";
                Class<?> keyClass = LinkedHashMap.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.Hashtable";
                Class<?> keyClass = Hashtable.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.Properties";
                Class<?> keyClass = Properties.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.WeakHashMap";
                Class<?> keyClass = WeakHashMap.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.ConcurrentHashMap";
                Class<?> keyClass = ConcurrentHashMap.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.CopyOnWriteArraySet";
                Class<?> keyClass = CopyOnWriteArraySet.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.EnumSet[ELEMENT1, ELEMENT2]";
                Class<?> keyClass = Object.class;
                Object expected = null;
            },
            new Object() {
                String key = "java.util.EnumMap[CLASS_NAME#ELEMENT1=VALUE1, CLASS_NAME#ELEMENT2=VALUE2]";
                Class<?> keyClass = Object.class;
                Object expected = null;
            }
    };

    @ org.junit.Test
    public void testDeserializeKey() throws Exception {
        for (Object testCase : TEST_CASES) {
            Object key = testCase;
            Class<?> keyClass = ((Class<?>) testCase.getClass().getDeclaredField("keyClass").get(testCase));
            Object expected = testCase.expected;
            StdKeyDeserializer deserializer = StdKeyDeserializer.forType(keyClass.getClassLoader().loadClass(keyClass.getName()));
            Object deserialized = deserializer.deserializeKey(String.valueOf(key), createTestContext());
            assertEquals(expected, deserialized);
        }
    }

    @ org.junit.Test
    public void testDeserializeKeyWithEnumFactoryMethod() throws Exception {
        Class<?> enumClass = EnumWithFactoryMethod.class;
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(enumClass);
        Object deserialized = deserializer.deserializeKey("EnumValue1", createTestContext());
        assertEquals(EnumValue1.class, deserialized.getClass());
    }

    @ org.junit.Test
    public void testDeserializeKeyWithEnumToStringResolver() throws Exception {
        Class<?> enumClass = EnumWithToStringResolver.class;
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(enumClass);
        Object deserialized = deserializer.deserializeKey("VALUE1", createTestContext());
        assertEquals(EnumValue1.class, deserialized.getClass());
    }

    @ org.junit.Test
    public void testDeserializeKeyWithStringFactoryMethod() throws Exception {
        Class<?> stringClass = String.class;
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(stringClass);
        Object deserialized = deserializer.deserializeKey("test", createTestContext());
        assertEquals("test", deserialized);
    }

    @ org.junit.Test
    public void testDeserializeKeyWithStringConstructor() throws Exception {
        Class<?> stringClass = String.class;
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(stringClass);
        Constructor<?> constructor = stringClass.getDeclaredConstructor(String.class);
        deserializer = new StringCtorKeyDeserializer(constructor);
        Object deserialized = deserializer.deserializeKey("test", createTestContext());
        assertEquals("test", deserialized);
    }

    @ org.junit.Test
    public void testDeserializeKeyWithNullKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object deserialized = deserializer.deserializeKey(null, createTestContext());
        assertNull(deserialized);
    }

    @ org.junit.Test
    public void testDeserializeKeyWithInvalidKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object deserialized = deserializer.deserializeKey("InvalidKey", createTestContext());
        assertNull(deserialized);
    }

    private DeserializationContext createTestContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }
}