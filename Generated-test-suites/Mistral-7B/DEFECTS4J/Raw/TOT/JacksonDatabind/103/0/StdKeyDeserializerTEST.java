package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("true", context);
        Assert.assertEquals(Boolean.TRUE, result);
        result = keyDeserializer.deserializeKey("false", context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test invalid key deserialization
        Assert.assertEquals("not a valid representation", keyDeserializer.deserializeKey("invalidKey", context));

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("0", context);
        Assert.assertEquals((byte) 0, result);
        result = keyDeserializer.deserializeKey("127", context);
        Assert.assertEquals((byte) 127, result);

        // Test invalid key deserialization
        Assert.assertEquals("not a valid representation, problem: java.lang.NumberFormatException: For input string: \"invalidKey\"",
                keyDeserializer.deserializeKey("invalidKey", context));

        // Test out of bounds key
        Assert.assertEquals("overflow, value cannot be represented as 8-bit value",
                keyDeserializer.deserializeKey("256", context));

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("0", context);
        Assert.assertEquals((short) 0, result);
        result = keyDeserializer.deserializeKey("32767", context);
        Assert.assertEquals((short) 32767, result);

        // Test invalid key deserialization
        Assert.assertEquals("not a valid representation, problem: java.lang.NumberFormatException: For input string: \"invalidKey\"",
                keyDeserializer.deserializeKey("invalidKey", context));

        // Test out of bounds key
        Assert.assertEquals("overflow, value cannot be represented as 16-bit value",
                keyDeserializer.deserializeKey("32768", context));

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("A", context);
        Assert.assertEquals('A', result);

        // Test invalid key deserialization
        Assert.assertEquals("can only convert 1-character Strings",
                keyDeserializer.deserializeKey("invalidKey", context));

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("0", context);
        Assert.assertEquals(0, result);

        // Test invalid key deserialization
        Assert.assertEquals("not a valid representation, problem: java.lang.NumberFormatException: For input string: \"invalidKey\"",
                keyDeserializer.deserializeKey("invalidKey", context));

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("0", context);
        Assert.assertEquals(0L, result);
        result = keyDeserializer.deserializeKey("9223372036854775807", context);
        Assert.assertEquals(Long.MAX_VALUE, result);

        // Test invalid key deserialization
        Assert.assertEquals("not a valid representation, problem: java.lang.NumberFormatException: For input string: \"invalidKey\"",
                keyDeserializer.deserializeKey("invalidKey", context));

        // Test out of bounds key
        Assert.assertEquals("overflow, value cannot be represented as 64-bit value",
                keyDeserializer.deserializeKey("9223372036854775808", context));

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("0.0", context);
        Assert.assertEquals(0.0f, result);
        result = keyDeserializer.deserializeKey("123.45", context);
        Assert.assertEquals(123.45f, result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("0.0", context);
        Assert.assertEquals(0.0, result, 0.001);
        result = keyDeserializer.deserializeKey("123.45", context);
        Assert.assertEquals(123.45, result, 0.001);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("en_US", context);
        Assert.assertEquals(Locale.US, result);
        result = keyDeserializer.deserializeKey("fr_FR", context);
        Assert.assertEquals(Locale.FRANCE, result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(java.util.Date.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("2022-12-31T23:59:59.000Z", context);
        Assert.assertEquals(new Date(System.currentTimeMillis() - 16724800000L), result); // One month ago

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.DECEMBER, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Object result = keyDeserializer.deserializeKey(calendar.toString(), context);
        Assert.assertEquals(calendar, result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testURIKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("https://www.example.com", context);
        Assert.assertEquals(new URI("https://www.example.com"), result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testURLKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("https://www.example.com", context);
        Assert.assertEquals(new URL("https://www.example.com"), result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("java.lang.String", context);
        Assert.assertEquals(String.class, result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testUUIDKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("123e4567-123e-4567-aef0-123e456784f0", context);
        Assert.assertEquals(new UUID("123e4567-123e-4567-aef0-123e456784f0"), result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testEnumKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(EnumSet.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("Of", context);
        Assert.assertEquals(EnumSet.of(EnumSet.AllElements), result);
        result = keyDeserializer.deserializeKey("A, B, C", context);
        Assert.assertEquals(EnumSet.of(EnumA.A, EnumB.B, EnumC.C), result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testByteArrayKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte[].class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        byte[] expectedKey = new byte[]{1, 2, 3};
        String base64EncodedKey = Base64.getEncoder().encodeToString(expectedKey);
        Object result = keyDeserializer.deserializeKey(base64EncodedKey, context);
        Assert.assertArrayEquals(expectedKey, (byte[]) result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testStringKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Object result = keyDeserializer.deserializeKey("key1", context);
        Assert.assertEquals("key1", result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testObjectKeyDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Object.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DeserializationConfig());

        // Test valid key deserialization
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        Object key = map.entrySet().iterator().next().getKey();
        Object result = keyDeserializer.deserializeKey(key.toString(), context);
        Assert.assertEquals(key, result);

        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }
}