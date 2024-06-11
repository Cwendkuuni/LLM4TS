package com.example.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext(null));
        assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", new TestContext(null));
        assertEquals(Boolean.FALSE, key);
        
        key = serializer.deserializeKey("invalid", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testByteDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        Object key = serializer.deserializeKey("127", new TestContext(null));
        assertEquals((byte) 127, key);

        key = serializer.deserializeKey("-128", new TestContext(null));
        assertEquals((byte) -128, key);

        key = serializer.deserializeKey("256", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testShortDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        Object key = serializer.deserializeKey("32767", new TestContext(null));
        assertEquals((short) 32767, key);

        key = serializer.deserializeKey("-32768", new TestContext(null));
        assertEquals((short) -32768, key);

        key = serializer.deserializeKey("65536", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testCharDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        Object key = serializer.deserializeKey("A", new TestContext(null));
        assertEquals('A', key);

        key = serializer.deserializeKey("Invalid", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testIntDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object key = serializer.deserializeKey("2147483647", new TestContext(null));
        assertEquals(2147483647, key);

        key = serializer.deserializeKey("-2147483648", new TestContext(null));
        assertEquals(-2147483648, key);

        key = serializer.deserializeKey("4611686018427387903", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testLongDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        Object key = serializer.deserializeKey("9223372036854775807", new TestContext(null));
        assertEquals(9223372036854775807L, key);

        key = serializer.deserializeKey("-9223372036854775808", new TestContext(null));
        assertEquals(-9223372036854775808L, key);

        key = serializer.deserializeKey("18446744073709551616", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testDoubleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        Object key = serializer.deserializeKey("1.79769313486231E308", new TestContext(null));
        assertEquals(1.79769313486231E308, key, 0.0);

        key = serializer.deserializeKey("-1.79769313486231E308", new TestContext(null));
        assertEquals(-1.79769313486231E308, key, 0.0);

        key = serializer.deserializeKey("4.94066e-324", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object key = serializer.deserializeKey("en_US", new TestContext(null));
        assertEquals(Locale.US, key);

        key = serializer.deserializeKey("fr_FR", new TestContext(null));
        assertEquals(Locale.FRANCE, key);

        key = serializer.deserializeKey("InvalidLocale", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testDateDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);

        Object key = serializer.deserializeKey("2022-12-31T23:59:59.999Z", new TestContext(null));
        assertNotNull(key);

        key = serializer.deserializeKey("InvalidDate", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testCalendarDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);

        Object key = serializer.deserializeKey("2022-12-31", new TestContext(null));
        assertNotNull(key);

        key = serializer.deserializeKey("InvalidDate", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        Object key = serializer.deserializeKey("http://example.com", new TestContext(null));
        assertEquals(new URI("http://example.com"), key);

        key = serializer.deserializeKey("InvalidURI", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        Object key = serializer.deserializeKey("http://example.com", new TestContext(null));
        assertEquals(new URL("http://example.com"), key);

        key = serializer.deserializeKey("InvalidURL", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class); // fallback to StringKD for testing

        Object key = serializer.deserializeKey("com.example.MyClass", new TestContext(null));
        assertNotNull(key);
        Class<?> cls = (Class<?>) key;
        assertEquals(MyClass.class, cls);

        key = serializer.deserializeKey("InvalidClass", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testNullKeyHandling() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object key = serializer.deserializeKey(null, new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testInvalidKeyHandling() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object key = serializer.deserializeKey("InvalidKey", new TestContext(null));
        assertNull(key);
    }

    @Test
    public void testUnknownTypeDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Object.class);

        Object key = serializer.deserializeKey("true", new TestContext(null));
        assertEquals(Boolean.TRUE, key);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownKeyType() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(-1, Object.class);
        serializer.deserializeKey("unknownKeyType", new TestContext(null));
    }
}

class TestContext extends DeserializationContext {
    public TestContext(DeserializerProvider provider) {
        super(provider);
    }

    @Override
    public Object findValueInMap(Map<String, Object> map, String key) {
        return map.getOrDefault(key, null);
    }
}