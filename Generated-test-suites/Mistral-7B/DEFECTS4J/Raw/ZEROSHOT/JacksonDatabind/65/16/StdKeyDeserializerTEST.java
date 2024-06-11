package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest extends TestBase {

    @Test
    public void testSimpleStringKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("test", getContext());
        assertEquals("test", key);
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", getContext());
        assertTrue((Boolean) key);
        key = deserializer.deserializeKey("false", getContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("127", getContext());
        assertEquals((byte) 127, key);
        key = deserializer.deserializeKey("128", getContext());
        assertNull(key);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("32767", getContext());
        assertEquals((short) 32767, key);
        key = deserializer.deserializeKey("32768", getContext());
        assertNull(key);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", getContext());
        assertEquals('A', key);
        key = deserializer.deserializeKey("B", getContext());
        assertNull(key);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("12345", getContext());
        assertEquals(12345, key);
        key = deserializer.deserializeKey("12346", getContext());
        assertNull(key);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("1234567890123456789", getContext());
        assertEquals(1234567890123456789L, key);
        key = deserializer.deserializeKey("1234567890123456789L", getContext());
        assertNull(key);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("3.1415F", getContext());
        assertEquals(3.1415F, key);
        key = deserializer.deserializeKey("3.1415", getContext());
        assertNull(key);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("3.1415", getContext());
        assertEquals(3.1415, key);
        key = deserializer.deserializeKey("3.1415D", getContext());
        assertNull(key);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = deserializer.deserializeKey("en_US", getContext());
        assertEquals(Locale.US, key);
        key = deserializer.deserializeKey("es_ES", getContext());
        assertNull(key);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object key = deserializer.deserializeKey("2022-12-31", getContext());
        assertEquals(new Date(System.currentTimeMillis()), key); // uses current date for testing
        key = deserializer.deserializeKey("2022-12-32", getContext());
        assertNull(key);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = deserializer.deserializeKey("2022-12-31", getContext());
        assertEquals(Calendar.getInstance(), key); // uses current date for testing
        key = deserializer.deserializeKey("2022-12-32", getContext());
        assertNull(key);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = deserializer.deserializeKey("123e4567-789b-1234-5678-123456789012", getContext());
        assertEquals(new UUID("123e4567-789b-1234-5678-123456789012"), key);
        key = deserializer.deserializeKey("123e4567-789b-1234-5678-123456789013", getContext());
        assertNull(key);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("https://www.example.com", getContext());
        assertEquals(new URI("https", "www.example.com", null), key);
        key = deserializer.deserializeKey("https://www.example.com/path", getContext());
        assertNull(key);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("https://www.example.com", getContext());
        assertEquals(new URL("https", "www.example.com", -1, null), key);
        key = deserializer.deserializeKey("https://www.example.com/path", getContext());
        assertNull(key);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        Object key = deserializer.deserializeKey("[Ljava.lang.String;@789abcd1", getContext());
        assertArrayEquals(new String[] {"test"}, key);
        key = deserializer.deserializeKey("[Ljava.lang.String;@efghijkl", getContext());
        assertNull(key);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        EnumSet<Color> colors = EnumSet.allOf(Color.class);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Color.class);

        Object key = deserializer.deserializeKey("RED", getContext());
        assertEquals(Color.RED, key);
        key = deserializer.deserializeKey("BLUE", getContext());
        assertEquals(Color.BLUE, key);
        key = deserializer.deserializeKey("GREEN", getContext());
        assertEquals(Color.GREEN, key);
        key = deserializer.deserializeKey("YELLOW", getContext());
        assertEquals(Color.YELLOW, key);

        key = deserializer.deserializeKey("INVALID", getContext());
        assertNull(key);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        class MyKey {
            @JsonProperty
            private final String value;

            @JsonCreator
            public MyKey(@JsonProperty("value") String value) {
                this.value = value;
            }
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyKey.class);
        Object key = deserializer.deserializeKey("MyKey(\"test\")", getContext());
        assertEquals(new MyKey("test"), key);
        key = deserializer.deserializeKey("MyKey(\"another test\")", getContext());
        assertNull(key);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        class MyKey {
            @JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
            private final String value;

            public MyKey(String value) {
                this.value = value;
            }
        }

        AnnotationIntrospector introspector = getContext().getAnnotationIntrospector();
        Class<?> myKeyClass = MyKey.class;
        Constructor<?> constructor = myKeyClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, myKeyClass, null);

        Object key = deserializer.deserializeKey("MyKey(\"test\")", getContext());
        assertEquals(new MyKey("test"), key);
        key = deserializer.deserializeKey("MyKey(\"another test\")", getContext());
        assertNull(key);
    }
}