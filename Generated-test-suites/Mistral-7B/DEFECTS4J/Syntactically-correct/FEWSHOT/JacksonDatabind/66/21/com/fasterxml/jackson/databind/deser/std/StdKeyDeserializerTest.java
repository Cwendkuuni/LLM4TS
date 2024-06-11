package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test cases for StdKeyDeserializer class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws Exception {
        Class<?> rawType = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Boolean.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Byte.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_BYTE, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Short.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_SHORT, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Character.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_CHAR, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Long.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_LONG, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Float.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_FLOAT, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Double.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_DOUBLE, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = URI.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_URI, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = URL.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_URL, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Locale.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_LOCALE, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Currency.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_CURRENCY, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Class.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_CLASS, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = UUID.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_UUID, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Calendar.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_CALENDAR, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Date.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(StdKeyDeserializer.TYPE_DATE, keyDeserializer._kind);
        assertEquals(rawType, keyDeserializer.getKeyClass());

        rawType = Enum.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(-1, keyDeserializer._kind);
        assertNull(keyDeserializer._deser);

        rawType = Object.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(-1, keyDeserializer._kind);
        assertNull(keyDeserializer._deser);

        rawType = String.class;
        keyDeserializer = StdKeyDeserializer.forType(rawType);
        assertEquals(-1, keyDeserializer._kind);
        assertNull(keyDeserializer._deser);
    }

    @Test
    public void testDeserializeKey() throws Exception {
        Class<?> keyClass = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(keyClass);

        String key = "true";
        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey(key, null));

        key = "false";
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey(key, null));

        key = "123";
        assertEquals(Integer.valueOf(123), keyDeserializer.deserializeKey(key, null));

        key = "1234567890";
        assertEquals(Long.valueOf(1234567890L), keyDeserializer.deserializeKey(key, null));

        key = "3.14159265358979323846";
        assertEquals(Double.valueOf(3.14159265358979323846), keyDeserializer.deserializeKey(key, null));

        key = "2010-03-01";
        assertEquals(new Date(1262304000000L), keyDeserializer.deserializeKey(key, null));

        key = "EN";
        assertEquals(Locale.ENGLISH, keyDeserializer.deserializeKey(key, null));

        key = "USD";
        assertEquals(Currency.getInstance(Locale.US), keyDeserializer.deserializeKey(key, null));

        key = "java.util.Date";
        assertEquals(Date.class, keyDeserializer.deserializeKey(key, null));

        key = "java.util.Calendar";
        Calendar calendar = GregorianCalendar.getInstance();
        assertEquals(calendar.getClass(), keyDeserializer.deserializeKey(key, null));

        key = "java.net.URI";
        assertEquals(URI.class, keyDeserializer.deserializeKey(key, null));

        key = "java.net.URL";
        assertEquals(URL.class, keyDeserializer.deserializeKey(key, null));

        key = "java.lang.Class";
        assertEquals(Class.class, keyDeserializer.deserializeKey(key, null));

        key = "java.util.UUID";
        assertEquals(UUID.class, keyDeserializer.deserializeKey(key, null));

        key = "java.util.Date,123";
        assertEquals(new Date(123), keyDeserializer.deserializeKey(key, null));

        key = "java.util.Locale.US";
        assertEquals(Locale.US, keyDeserializer.deserializeKey(key, null));

        key = "java.util.Currency,USD";
        assertEquals(Currency.getInstance(Locale.US), keyDeserializer.deserializeKey(key, null));

        key = "java.lang.Class<java.util.Date>";
        assertEquals(Date.class, keyDeserializer.deserializeKey(key, null));

        key = "java.lang.Class<java.util.Calendar>";
        assertEquals(Calendar.class, keyDeserializer.deserializeKey(key, null));

        key = "java.lang.Class<java.net.URI>";
        assertEquals(URI.class, keyDeserializer.deserializeKey(key, null));

        key = "java.lang.Class<java.net.URL>";
        assertEquals(URL.class, keyDeserializer.deserializeKey(key, null));

        key = "java.lang.Class<java.util.UUID>";
        assertEquals(UUID.class, keyDeserializer.deserializeKey(key, null));

        key = "java.util.Date[1, 2]";
        Object[] dateArray = new Object[2];
        dateArray[0] = new Date(0);
        dateArray[1] = new Date(1);
        assertEquals(dateArray, keyDeserializer.deserializeKey(key, null));

        key = "java.util.List<java.lang.String>";
        List<String> stringList = Arrays.asList("foo", "bar");
        assertEquals(stringList.getClass(), keyDeserializer.deserializeKey(key, null));

        key = "java.util.Set<java.lang.String>";
        Set<String> stringSet = new HashSet<>(Arrays.asList("foo", "bar"));
        assertEquals(stringSet.getClass(), keyDeserializer.deserializeKey(key, null));

        key = "java.util.Map<java.lang.String, java.lang.Integer>";
        Map<String, Integer> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", 2);
        assertEquals(map.getClass(), keyDeserializer.deserializeKey(key, null));

        key = "java.util.EnumSet<java.lang.Enum>";
        EnumSet<Enum> enumSet = EnumSet.allOf(Enum.class);
        assertEquals(enumSet.getClass(), keyDeserializer.deserializeKey(key, null));

        key = "java.lang.reflect.Method[java.lang.String, java.lang.String]";
        Method method = String.class.getMethod("concat", String.class);
        assertEquals(method.getClass(), keyDeserializer.deserializeKey(key, null));

        key = "java.lang.reflect.Constructor<java.lang.Object[]>";
        Constructor<Object[]> constructor = Object[].class.getConstructor(int.class);
        assertEquals(constructor.getClass(), keyDeserializer.deserializeKey(key, null));

        AnnotatedClass ac = AnnotatedClass.construct(String[].class, null);
        AnnotatedMethod am = ac.getDeclaredMethod("valueOf", String[].class);
        key = am.getAnnotatedName().getText();
        assertEquals(String[].class, keyDeserializer.deserializeKey(key, null));

        key = "java.net.URI[1, java.lang.String]";
        Object[] uriComponents = new Object[2];
        uriComponents[0] = new URI("http://example.com");
        uriComponents[1] = "foo";
        assertEquals(URI[].class, keyDeserializer.deserializeKey(key, null));
        assertEquals(uriComponents, keyDeserializer.deserializeKey(key, null));

        key = "java.net.URL[1, java.lang.String]";
        URL[] urls = new URL[2];
        urls[0] = new URL("http://example.com");
        urls[1] = new URL("http://example.org");
        assertEquals(URL[].class, keyDeserializer.deserializeKey(key, null));
        assertEquals(urls, keyDeserializer.deserializeKey(key, null));

        key = "java.util.Map<java.lang.String, java.lang.Integer[]>";
        Map<String, Integer[]> map2 = new HashMap<>();
        Integer[] intArray1 = new Integer[2];
        intArray1[0] = 1;
        intArray1[1] = 2;
        Integer[] intArray2 = new Integer[3];
        intArray2[0] = 3;
        intArray2[1] = 4;
        intArray2[2] = 5;
        map2.put("foo", intArray1);
        map2.put("bar", intArray2);
        assertEquals(map2.getClass(), keyDeserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKeyWithCustomDeserializer() throws Exception {
        Class<?> keyClass = String.class;
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, keyClass);
        keyDeserializer._deser = new FromStringDeserializer<>(String.class);

        String key = "MyCustomClass[1, \"foo\"]";
        AnnotatedClass annotatedClass = AnnotatedClass.construct(Class.forName("com.example.MyCustomClass"), null);
        AnnotatedMethod constructorMethod = annotatedClass.getDeclaredConstructor(Integer.class, String.class);
        assertEquals(annotatedClass, keyDeserializer.deserializeKey(key, null));

        key = "MyCustomClass[1, 1, \"foo\", 2, \"bar\"]";
        Object[] constructorArguments = {1, 1, "foo", 2, "bar"};
        assertEquals(annotatedClass, keyDeserializer.deserializeKey(key, null));
        assertEquals(constructorArguments, keyDeserializer.deserializeKey(key, null));
    }

    @Test
    public void testDeserializeKeyWithInvalidInput() throws Exception {
        Class<?> keyClass = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(keyClass);

        String key = "InvalidInput";
        assertNull(keyDeserializer.deserializeKey(key, null));
    }
}