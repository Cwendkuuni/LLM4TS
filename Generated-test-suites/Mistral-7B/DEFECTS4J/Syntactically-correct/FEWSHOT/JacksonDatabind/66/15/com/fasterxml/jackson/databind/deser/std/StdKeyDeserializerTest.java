package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Test for boolean type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", MAPPER.getDeserializationContext()));
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for byte type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testByteKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 0, keyDeserializer.deserializeKey("0", MAPPER.getDeserializationContext()));
        assertEquals((byte) 127, keyDeserializer.deserializeKey("127", MAPPER.getDeserializationContext()));
        assertEquals((byte) -128, keyDeserializer.deserializeKey("-128", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for short type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testShortKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 0, keyDeserializer.deserializeKey("0", MAPPER.getDeserializationContext()));
        assertEquals((short) Short.MAX_VALUE, keyDeserializer.deserializeKey("32767", MAPPER.getDeserializationContext()));
        assertEquals((short) Short.MIN_VALUE, keyDeserializer.deserializeKey("-32768", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for char type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testCharKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('a', keyDeserializer.deserializeKey("a", MAPPER.getDeserializationContext()));
        assertEquals('\\u00FF', keyDeserializer.deserializeKey("\u00FF", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for int type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testIntKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(0, keyDeserializer.deserializeKey("0", MAPPER.getDeserializationContext()));
        assertEquals(Integer.MAX_VALUE, keyDeserializer.deserializeKey("2147483647", MAPPER.getDeserializationContext()));
        assertEquals(Integer.MIN_VALUE, keyDeserializer.deserializeKey("-2147483648", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for long type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testLongKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(0L, keyDeserializer.deserializeKey("0", MAPPER.getDeserializationContext()));
        assertEquals(Long.MAX_VALUE, keyDeserializer.deserializeKey("9223372036854775807", MAPPER.getDeserializationContext()));
        assertEquals(Long.MIN_VALUE, keyDeserializer.deserializeKey("-9223372036854775808", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for float type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testFloatKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(0.0f, keyDeserializer.deserializeKey("0.0", MAPPER.getDeserializationContext()), 0.001f);
        assertEquals(Float.MAX_VALUE, keyDeserializer.deserializeKey("3.4028235E38", MAPPER.getDeserializationContext()));
        assertEquals(Float.MIN_VALUE, keyDeserializer.deserializeKey("-3.4028235E38", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for double type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(0.0, keyDeserializer.deserializeKey("0.0", MAPPER.getDeserializationContext()), 0.001);
        assertEquals(Double.MAX_VALUE, keyDeserializer.deserializeKey("1.79769313486231E308", MAPPER.getDeserializationContext()));
        assertEquals(Double.MIN_VALUE, keyDeserializer.deserializeKey("-1.79769313486231E308", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for locale type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.US, keyDeserializer.deserializeKey("en_US", MAPPER.getDeserializationContext()));
        assertEquals(Locale.UK, keyDeserializer.deserializeKey("en_GB", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));

        // Test with custom Locale deserializer
        AnnotatedClass annotatedClass = MAPPER.findTypeDeserializer(Locale.class);
        BeanDescription beanDescription = annotatedClass.findBeanDescription();
        AnnotatedMethod annotatedMethod = beanDescription.findFactoryMethodNamed("fromString");
        StdKeyDeserializer localeKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, (FromStringDeserializer<?>) annotatedMethod.getAnnotatedExecutable().getAnnotatedConstructor(String.class).getAnnotatedInstance());
        assertEquals(Locale.US, localeKeyDeserializer.deserializeKey("en_US", MAPPER.getDeserializationContext()));
        assertEquals(Locale.UK, localeKeyDeserializer.deserializeKey("en_GB", MAPPER.getDeserializationContext()));
        assertNull(localeKeyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for URI type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testURIKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals(new URI("http://example.com"), keyDeserializer.deserializeKey("http://example.com", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for URL type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testURLKeyDeserializer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL("http://example.com"), keyDeserializer.deserializeKey("http://example.com", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for enum type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testEnumKeyDeserializer() throws IOException {
        enum TestEnum {
            ONE, TWO, THREE
        }

        BeanDescription beanDescription = MAPPER.findBeanDescription(TestEnum.class);
        AnnotatedMethod annotatedMethod = beanDescription.findFactoryMethodNamed("valueOf");
        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, TestEnum.class, (FromStringDeserializer<?>) annotatedMethod.getAnnotatedExecutable().getAnnotatedConstructor(String.class).getAnnotatedInstance());

        assertEquals(TestEnum.ONE, enumKeyDeserializer.deserializeKey("ONE", MAPPER.getDeserializationContext()));
        assertEquals(TestEnum.TWO, enumKeyDeserializer.deserializeKey("TWO", MAPPER.getDeserializationContext()));
        assertEquals(TestEnum.THREE, enumKeyDeserializer.deserializeKey("THREE", MAPPER.getDeserializationContext()));
        assertNull(enumKeyDeserializer.deserializeKey("FOO", MAPPER.getDeserializationContext()));
    }

    /**
     * Test for custom class type key deserializer.
     *
     * @throws IOException if deserialization fails.
     */
    @Test
    public void testCustomClassKeyDeserializer() throws IOException {
        static class CustomClass {
            private final int id;

            public CustomClass(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }

            public static CustomClass fromString(String str) {
                String[] parts = str.split(":");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid string representation: " + str);
                }
                int id = Integer.parseInt(parts[0]);
                return new CustomClass(id);
            }
        }

        StdKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(CustomClass.class);
        assertEquals(new CustomClass(1), keyDeserializer.deserializeKey("1:CustomClass", MAPPER.getDeserializationContext()));
        assertNull(keyDeserializer.deserializeKey("foo", MAPPER.getDeserializationContext()));
    }
}