package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = keyDeserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Byte) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("128", new TestContext());
        assertEquals("overflow, value cannot be represented as 8-bit value", deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Short) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("32768", new TestContext());
        assertEquals("overflow, value cannot be represented as 16-bit value", deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = keyDeserializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("can only convert 1-character Strings", deserializedKey);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Integer) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) 2147483647, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("-2147483648", new TestContext());
        assertEquals("not a valid representation", deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Long) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals("not a valid representation", deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0.0", new TestContext());
        assertEquals((Float) 0.0f, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("3.14159265", new TestContext());
        assertEquals((Float) 3.14159265f, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0.0", new TestContext());
        assertEquals((Double) 0.0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals((Double) 3.14159265358979323846, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertEquals("not a valid representation", deserializedKey);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = keyDeserializer.deserializeKey("2022-12-31", new TestContext());
        assertEquals(new Date(1671152000000L), deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object deserializedKey = keyDeserializer.deserializeKey("2022-12-31", new TestContext());
        Calendar calendar = (Calendar) deserializedKey;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(12, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object deserializedKey = keyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", new TestContext());
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object deserializedKey = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        URI uri = (URI) deserializedKey;
        assertEquals("http", uri.getScheme());
        assertEquals("example.com", uri.getHost());

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        URL url = (URL) deserializedKey;
        assertEquals("http", url.getProtocol());
        assertEquals("example.com", url.getHost());

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        Object deserializedKey = keyDeserializer.deserializeKey("[Ljava.lang.String;@12345678", new TestContext());
        assertArrayEquals(new String[]{"one", "two", "three"}, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        enum TestEnum { ONE, TWO, THREE }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
        Object deserializedKey = keyDeserializer.deserializeKey("ONE", new TestContext());
        assertEquals(TestEnum.ONE, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("TWO", new TestContext());
        assertEquals(TestEnum.TWO, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("THREE", new TestContext());
        assertEquals(TestEnum.THREE, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("INVALID", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCustomKeyDeserializer() throws Exception {
        class CustomKey {
            private final int value;

            CustomKey(int value) {
                this.value = value;
            }

            @JsonDeserialize(using = CustomKeyDeserializer.class)
            public int getValue() {
                return value;
            }
        }

        class CustomKeyDeserializer extends KeyDeserializer {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return new CustomKey(Integer.parseInt(key));
            }

            @Override
            public KeyDeserializer.NullValue getNullKey() {
                return null;
            }
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(CustomKey.class);
        Object deserializedKey = keyDeserializer.deserializeKey("5", new TestContext());
        assertEquals(5, ((CustomKey) deserializedKey).getValue());

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }
}

class TestContext extends DeserializationContext {
    private static final long serialVersionUID = 1L;

    public TestContext() {
        super(new TestParser(), new DefaultSerializerProvider(), new JsonFactory());
    }
}