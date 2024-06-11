package com.example.junit; // replace with your test package name

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private DeserializationContext context;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testBasicBooleanDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", context);
        Assert.assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", context);
        Assert.assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testBasicByteDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("127", context);
        Assert.assertEquals((byte) 127, key);

        try {
            key = deserializer.deserializeKey("-128", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value cannot be represented as 8-bit value", e.getMessage());
        }

        try {
            key = deserializer.deserializeKey("256", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value cannot be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testBasicShortDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("32767", context);
        Assert.assertEquals((short) 32767, key);

        try {
            key = deserializer.deserializeKey("-32768", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value cannot be represented as 16-bit value", e.getMessage());
        }

        try {
            key = deserializer.deserializeKey("65536", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value cannot be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testBasicCharDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", context);
        Assert.assertEquals('A', key);

        try {
            key = deserializer.deserializeKey("", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testBasicIntDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("2147483647", context);
        Assert.assertEquals(2147483647, key);

        try {
            key = deserializer.deserializeKey("-2147483648", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value cannot be represented as 32-bit value", e.getMessage());
        }
    }

    @Test
    public void testBasicLongDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("9223372036854775807", context);
        Assert.assertEquals(9223372036854775807L, key);

        try {
            key = deserializer.deserializeKey("-9223372036854775808", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value cannot be represented as 64-bit value", e.getMessage());
        }
    }

    @Test
    public void testBasicFloatDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("3.14159265358979323846", context);
        Assert.assertEquals(3.14159265358979323846f, key, 0.00001f);
    }

    @Test
    public void testBasicDoubleDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("6.28318530718", context);
        Assert.assertEquals(6.28318530718, key, 0.00001);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Object key = deserializer.deserializeKey("en_US", context);
        Assert.assertEquals(Locale.US, key);

        Object keyFromStringFactory = deserializer.deserializeKey("fr_CA", context);
        Assert.assertEquals(new Locale("fr", "CA"), keyFromStringFactory);

        try {
            key = deserializer.deserializeKey("invalidLocale", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testDateDeserialization() throws IOException, URISyntaxException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        Object key = deserializer.deserializeKey("2022-12-31", context);
        Assert.assertEquals(new Date(1671139200000L), key);

        Object keyFromParser = deserializer.deserializeKey("2023-01-01T00:00:00Z", context);
        Assert.assertEquals(new Date(1672492800000L), keyFromParser);

        try {
            key = deserializer.deserializeKey("invalidDateFormat", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testEnumDeserialization() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
        Object key = deserializer.deserializeKey("FIRST", context);
        Assert.assertEquals(MyEnum.FIRST, key);

        Object keyFromFactory = deserializer.deserializeKey("SECOND", context);
        Assert.assertEquals(MyEnum.SECOND, keyFromFactory);

        try {
            key = deserializer.deserializeKey("THIRD", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("not one of values excepted for Enum class: [FIRST, SECOND]", e.getMessage());
        }
    }

    @Test
    public void testExceptionHandling() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        try {
            deserializer.deserializeKey(null, context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation", e.getMessage());
        }

        try {
            deserializer.deserializeKey("invalidEnumValue", context);
            Assert.fail("Expected exception");
        } catch (IOException e) {
            Assert.assertEquals("not one of values excepted for Enum class: [null]", e.getMessage());
        }
    }
}

enum MyEnum {
    FIRST,
    SECOND,
    // Add other enum values as needed
}