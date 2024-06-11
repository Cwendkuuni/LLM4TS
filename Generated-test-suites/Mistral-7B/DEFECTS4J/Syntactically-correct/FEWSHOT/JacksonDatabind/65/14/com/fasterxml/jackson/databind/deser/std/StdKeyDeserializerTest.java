package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
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

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = serializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = serializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = serializer.deserializeKey("someOtherValue", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertEquals((Byte) 1, result);

        result = serializer.deserializeKey("-127", new TestContext());
        assertEquals((Byte) -127, result);

        result = serializer.deserializeKey("128", new TestContext());
        assertNull(result);

        result = serializer.deserializeKey("256", new TestContext());
        assertNull(result);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertEquals((Short) 1, result);

        result = serializer.deserializeKey("-32768", new TestContext());
        assertEquals((Short) -32768, result);

        result = serializer.deserializeKey("32768", new TestContext());
        assertNull(result);

        result = serializer.deserializeKey("65536", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        Object result = serializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', result);

        result = serializer.deserializeKey("'\u0061'", new TestContext());
        assertEquals((Character) 'a', result);

        result = serializer.deserializeKey("foo", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertEquals((Integer) 1, result);

        result = serializer.deserializeKey("-2147483648", new TestContext());
        assertEquals((Integer) -2147483648, result);

        result = serializer.deserializeKey("2147483647", new TestContext());
        assertNull(result);

        result = serializer.deserializeKey("4294967296", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertEquals((Long) 1L, result);

        result = serializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals((Long) -9223372036854775808L, result);

        result = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertNull(result);

        result = serializer.deserializeKey("18446744073709551616", new TestContext());
        assertNull(result);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        Object result = serializer.deserializeKey("1.0", new TestContext());
        assertEquals((Float) 1.0f, result);

        result = serializer.deserializeKey("-3.14", new TestContext());
        assertEquals((Float) -3.14f, result);

        result = serializer.deserializeKey("NaN", new TestContext());
        assertNull(result);

        result = serializer.deserializeKey("Infinity", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        Object result = serializer.deserializeKey("1.0", new TestContext());
        assertEquals((Double) 1.0, result);

        result = serializer.deserializeKey("-3.14", new TestContext());
        assertEquals((Double) -3.14, result);

        result = serializer.deserializeKey("NaN", new TestContext());
        assertNull(result);

        result = serializer.deserializeKey("Infinity", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object result = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = serializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, result);

        result = serializer.deserializeKey("ko_KR", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);

        Object result = serializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);

        result = serializer.deserializeKey("1970-01-01T00:00:00Z", new TestContext());
        assertNotNull(result);

        result = serializer.deserializeKey("2022-02-29", new TestContext()); // not a leap year
        assertNull(result);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = serializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);

        result = serializer.deserializeKey("1970-01-01T00:00:00Z", new TestContext());
        assertNotNull(result);

        result = serializer.deserializeKey("2022-02-29", new TestContext()); // not a leap year
        assertNull(result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);

        Object result = serializer.deserializeKey("123e4567-789b-1234-a1b2-3456789abcde", new TestContext());
        assertEquals(UUID.fromString("123e4567-789b-1234-a1b2-3456789abcde"), result);

        result = serializer.deserializeKey("Some-random-UUID", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        Object result = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), result);

        result = serializer.deserializeKey("https://example.com/path/to/file.txt", new TestContext());
        assertEquals(new URI("https://example.com/path/to/file.txt"), result);

        result = serializer.deserializeKey("http://example.com?foo=bar", new TestContext());
        assertEquals(new URI("http://example.com?foo=bar"), result);

        result = serializer.deserializeKey("https://example.com/path/to/file.txt#fragment", new TestContext());
        assertEquals(new URI("https://example.com/path/to/file.txt#fragment"), result);

        result = serializer.deserializeKey("mailto:user@example.com", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        Object result = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), result);

        result = serializer.deserializeKey("https://example.com/path/to/file.txt", new TestContext());
        assertEquals(new URL("https://example.com/path/to/file.txt"), result);

        result = serializer.deserializeKey("http://example.com?foo=bar", new TestContext());
        assertNull(result); //URLs don't support query parameters as keys
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        StdKeyDeserializer classSerializer = StdKeyDeserializer.forType(MyClass.class);

        Object result = serializer.deserializeKey("java.util.Map", new TestContext());
        assertEquals(Map.class, result);

        result = classSerializer.deserializeKey("com.example.MyClass", new TestContext());
        assertEquals(MyClass.class, result);

        result = classSerializer.deserializeKey("java.lang.String", new TestContext());
        assertNull(result);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        EnumResolver enumResolver = EnumResolver.constructEnumResolver(MyEnum.class);
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class, enumResolver);

        Object result = serializer.deserializeKey("VALUE_ONE", new TestContext());
        assertEquals(MyEnum.VALUE_ONE, result);

        result = serializer.deserializeKey("VALUE_TWO", new TestContext());
        assertEquals(MyEnum.VALUE_TWO, result);

        result = serializer.deserializeKey("INVALID_VALUE", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        JsonDeserializer<Object> delegate = new ObjectMapper().findValueDeserializer(String.class, null);
        StdKeyDeserializer serializer = new DelegatingKD(Object.class, delegate);

        Object result = serializer.deserializeKey("\"Hello World\"", new TestContext());
        assertEquals("Hello World", result);

        result = serializer.deserializeKey("123", new TestContext());
        assertEquals(123, result);

        result = serializer.deserializeKey("true", new TestContext());
        assertEquals(true, result);

        result = serializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader _getRootValueFactory() {
            return null;
        }

        @Override
        public ObjectReader _findRootValue(Class<?> valueType) {
            return null;
        }

        @Override
        public ObjectMapper _getAnnotationIntrospector() {
            return null;
        }

        @Override
        public ObjectFactory _getFactory() {
            return null;
        }

        @Override
        public ObjectReader _findValueDeserializer(Class<?> valueType) {
            return null;
        }

        @Override
        public ObjectReader _findValueDeserializer(Class<?> valueType, JsonDeserializer.None p1) {
            return null;
        }

        @Override
        public JsonDeserializer<?> _findBeanDeserializer(Class<?> beanType) {
            return null;
        }

        @Override
        public JsonDeserializer<Object> _findRootValueDeserializer() {
            return null;
        }
    }
}