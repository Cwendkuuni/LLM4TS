package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for {@link StdKeyDeserializer}
 */
@JacksonAnnotations.JacksonTest
public class StdKeyDeserializerTest {

    /**
     * Test constructor for default cases.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testStdKeyDeserializerDefaultConstruction() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertNotNull(deserializer);
        assertEquals(StdKeyDeserializer.TYPE_STRING, deserializer.getType());
    }

    /**
     * Test construction with specific type.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testStdKeyDeserializerWithSpecificType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertNotNull(deserializer);
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, deserializer.getType());
    }

    /**
     * Test parsing a boolean key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseBooleanKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("true", new TestContext());
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.TRUE);

        result = deserializer.deserializeKey("false", new TestContext());
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.FALSE);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a byte key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseByteKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = deserializer.deserializeKey("127", new TestContext());
        assertTrue(result instanceof Byte);
        assertEquals((Byte) result, (byte) 127);

        result = deserializer.deserializeKey("-128", new TestContext());
        assertTrue(result instanceof Byte);
        assertEquals((Byte) result, (byte) -128);

        result = deserializer.deserializeKey("256", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a short key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseShortKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object result = deserializer.deserializeKey("32767", new TestContext());
        assertTrue(result instanceof Short);
        assertEquals((Short) result, (short) 32767);

        result = deserializer.deserializeKey("-32768", new TestContext());
        assertTrue(result instanceof Short);
        assertEquals((Short) result, (short) -32768);

        result = deserializer.deserializeKey("65536", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a char key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseCharKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object result = deserializer.deserializeKey("'A'", new TestContext());
        assertTrue(result instanceof Character);
        assertEquals((Character) result, 'A');

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing an int key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseIntKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = deserializer.deserializeKey("2147483647", new TestContext());
        assertTrue(result instanceof Integer);
        assertEquals((Integer) result, Integer.MAX_VALUE);

        result = deserializer.deserializeKey("-2147483648", new TestContext());
        assertTrue(result instanceof Integer);
        assertEquals((Integer) result, Integer.MIN_VALUE);

        result = deserializer.deserializeKey("4294967296", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a long key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseLongKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object result = deserializer.deserializeKey("9223372036854775807", new TestContext());
        assertTrue(result instanceof Long);
        assertEquals((Long) result, Long.MAX_VALUE);

        result = deserializer.deserializeKey("-9223372036854775808", new TestContext());
        assertTrue(result instanceof Long);
        assertEquals((Long) result, Long.MIN_VALUE);

        result = deserializer.deserializeKey("18446744073709551616", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a float key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseFloatKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object result = deserializer.deserializeKey("3.1415926535897932384626433832795", new TestContext());
        assertTrue(result instanceof Float);
        assertEquals((Float) result, Float.parseFloat("3.1415926535897932384626433832795"));

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a double key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseDoubleKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object result = deserializer.deserializeKey("1.79769313486231570e308", new TestContext());
        assertTrue(result instanceof Double);
        assertEquals((Double) result, Double.parseDouble("1.79769313486231570e308"));

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a locale key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseLocaleKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = deserializer.deserializeKey("en_US", new TestContext());
        assertTrue(result instanceof Locale);
        assertEquals((Locale) result, Locale.US);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a date key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseDateKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object result = deserializer.deserializeKey("2021-08-31", new TestContext());
        assertTrue(result instanceof Date);
        assertNotNull(result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a calendar key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseCalendarKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = deserializer.deserializeKey("2021-08-31", new TestContext());
        assertTrue(result instanceof Calendar);
        assertNotNull(result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a UUID key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseUuidKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object result = deserializer.deserializeKey("7bf81d12-1b1d-4b56-bcc3-2b9e616b9b93", new TestContext());
        assertTrue(result instanceof UUID);
        assertEquals((UUID) result, new UUID("7bf81d12-1b1d-4b56-bcc3-2b9e616b9b93"));

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a URI key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseUriKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object result = deserializer.deserializeKey("http://example.com", new TestContext());
        assertTrue(result instanceof URI);
        assertEquals((URI) result, new URI("http://example.com"));

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a URL key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseUrlKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object result = deserializer.deserializeKey("http://example.com", new TestContext());
        assertTrue(result instanceof URL);
        assertEquals((URL) result, new URL("http://example.com"));

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a class key.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseClassKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object result = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertTrue(result instanceof Class);
        assertEquals((Class) result, String.class);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing an enum key using name.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseEnumKeyByName() throws Exception {
        enum TestEnum {
            A,
            B,
            C
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        Object result = deserializer.deserializeKey("A", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.A);

        result = deserializer.deserializeKey("B", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.B);

        result = deserializer.deserializeKey("C", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.C);

        result = deserializer.deserializeKey("D", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing an enum key using toString.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseEnumKeyByToString() throws Exception {
        enum TestEnum {
            A,
            B,
            C
        }

        StdKeyDeserializer deserializer = new StdEnumKeyDeserializer(TestEnum.class);
        Object result = deserializer.deserializeKey("A.toString()", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.A);

        result = deserializer.deserializeKey("B.toString()", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.B);

        result = deserializer.deserializeKey("C.toString()", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals((TestEnum) result, TestEnum.C);

        result = deserializer.deserializeKey("D.toString()", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a key using string constructor.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseKeyUsingConstructor() throws Exception {
        class TestStringConstructor {
            TestStringConstructor(String s) {
            }
        }

        StdKeyDeserializer deserializer = new StringCtorKeyDeserializer(TestStringConstructor.class);
        Object result = deserializer.deserializeKey("test", new TestContext());
        assertTrue(result instanceof TestStringConstructor);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    /**
     * Test parsing a key using static factory method.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testParseKeyUsingFactoryMethod() throws Exception {
        class TestFactoryMethod {
            static TestFactoryMethod createInstance(String s) {
                return new TestFactoryMethod();
            }
        }

        StdKeyDeserializer deserializer = new StringFactoryKeyDeserializer(TestFactoryMethod.class, "createInstance");
        Object result = deserializer.deserializeKey("test", new TestContext());
        assertTrue(result instanceof TestFactoryMethod);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    private static class TestContext extends TestBaseContext {
    }

    private static class TestBaseContext extends DeserializationContext {
        @Override
        public JsonFactory _JsonFactory() {
            return null;
        }

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }
    }
}