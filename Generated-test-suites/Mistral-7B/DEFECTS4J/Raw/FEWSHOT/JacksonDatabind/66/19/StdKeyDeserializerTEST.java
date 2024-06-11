package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.NumberInput;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);
        key = serializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, key);
        try {
            key = serializer.deserializeKey("-128", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, key);
        try {
            key = serializer.deserializeKey("-32768", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', key);
        try {
            key = serializer.deserializeKey("AB", new TestContext());
            fail();
        } catch (IOException e) {
            assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("12345", new TestContext());
        assertEquals((Integer) 12345, key);
    }

    @Test
    public void testLongKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("1234567890123456789", new TestContext());
        assertEquals((Long) 1234567890123456789L, key);
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("1.2345", new TestContext());
        assertEquals((Float) 1.2345f, key);
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("1.23456789", new TestContext());
        assertEquals((Double) 1.23456789, key);
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testCurrencyKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Currency.class);
        Object key = serializer.deserializeKey("USD", new TestContext());
        assertEquals(Currency.getInstance("USD"), key);
    }

    @Test
    public void testDateKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);
        Object key = serializer.deserializeKey("2019-12-31", new TestContext());
        assertEquals(new java.util.Date("Wed Dec 31 00:00:00 CET 2019"), key);
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        Object key = serializer.deserializeKey("2019-12-31", new TestContext());
        assertEquals(java.util.GregorianCalendar.getInstance(2019, 11, 31), key);
    }

    @Test
    public void testUUIDKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object key = serializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), key);
    }

    @Test
    public void testURIKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://www.example.com", new TestContext());
        assertEquals(new URI("http://www.example.com"), key);
    }

    @Test
    public void testURLKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://www.example.com", new TestContext());
        assertEquals(new URL("http://www.example.com"), key);
    }

    @Test
    public void testClassKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);
    }

    @Test
    public void testEnumKeyDeserializerByName() throws IOException, JsonProcessingException {
        TestEnum testEnum = new TestEnum("FOO");
        KeyDeserializer serializer = StdKeyDeserializer.forType(TestEnum.class);
        Object key = serializer.deserializeKey("FOO", new TestContext());
        assertEquals(testEnum.FOO, key);
    }

    @Test
    public void testEnumKeyDeserializerByToString() throws IOException, JsonProcessingException {
        TestEnum testEnum = new TestEnum("BAR");
        KeyDeserializer serializer = StdKeyDeserializer.forType(TestEnum.class);
        Object key = serializer.deserializeKey("BAR", new TestContext());
        assertEquals(testEnum.BAR, key);
    }

    @Test
    public void testDelegateKeyDeserializer() throws IOException, JsonProcessingException {
        TestDelegate delegate = new TestDelegate();
        KeyDeserializer serializer = new DelegatingKD(String.class, delegate);
        Object key = serializer.deserializeKey("test", new TestContext());
        assertEquals(delegate.deserializeKey("test", new TestContext()), key);
    }

    @Test
    public void testStringKeyDeserializer() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StringKD.forType(String.class);
        Object key = serializer.deserializeKey("test", new TestContext());
        assertEquals("test", key);
    }

    @Test
    public void testStringKeyDeserializerObject() throws IOException, JsonProcessingException {
        KeyDeserializer serializer = StringKD.forType(Object.class);
        Object key = serializer.deserializeKey("test", new TestContext());
        assertEquals("test", key);
    }

    @Test
    public void testEnumResolver() throws IOException, JsonProcessingException {
        TestEnum testEnum = new TestEnum("BAZ");
        EnumResolver enumResolver = EnumResolver.construct(TestEnum.class);
        Object key = enumResolver.findEnum("BAZ");
        assertEquals(testEnum.BAZ, key);
    }

    private static class TestContext extends TestBaseContext {
    }

    private static class TestEnum {
        static final TestEnum FOO = new TestEnum("FOO");
        static final TestEnum BAR = new TestEnum("BAR");
        static final TestEnum BAZ = new TestEnum("BAZ");

        private final String name;

        TestEnum(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }

    private static class TestDelegate implements KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return "test";
        }

        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return new TextNode("test");
        }
    }
}

class TestBaseContext extends BaseTestContext {
    protected TestBaseContext() {
        super(new ObjectMapper());
    }
}