package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private JsonDeserializer.KeyDeserializer deserializer;

    @Before
    public void setUp() {
        context = new TestDeserializationContext();
        JavaType javaType = context.constructType(StdKeyDeserializer.class);
        deserializer = context.findKeyDeserializer(javaType, StdKeyDeserializer.class);
    }

    @Test
    public void testBooleanDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("true", context);
        Assert.assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", context);
        Assert.assertEquals(Boolean.FALSE, key);

        try {
            deserializer.deserializeKey("1", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation: value '1' can not be represented as java.lang.Boolean", e.getMessage());
        }
    }

    @Test
    public void testByteDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("1", context);
        Assert.assertEquals((Byte) 1, key);

        try {
            deserializer.deserializeKey("256", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("123", context);
        Assert.assertEquals((Short) 123, key);

        try {
            deserializer.deserializeKey("32767", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation: overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("A", context);
        Assert.assertEquals((Character) 'A', key);

        try {
            deserializer.deserializeKey("ABC", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("12345", context);
        Assert.assertEquals(12345, key);
    }

    @Test
    public void testLongDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("123456789012345", context);
        Assert.assertEquals(123456789012345L, key);
    }

    @Test
    public void testFloatDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("1.23", context);
        Assert.assertEquals(1.23f, key);
    }

    @Test
    public void testDoubleDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("1.234", context);
        Assert.assertEquals(1.234d, key);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("en_US", context);
        Assert.assertEquals(Locale.US, key);
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("USD", context);
        Assert.assertEquals(Currency.getInstance("USD"), key);
    }

    @Test
    public void testDateDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("2022-01-01", context);
        Assert.assertEquals(new Date(0), key); // Test with default Date constructor.

        key = deserializer.deserializeKey("Mon Jan 01 00:00:00 CET 2022", context);
        Assert.assertEquals(new Date(System.currentTimeMillis() - (3600 * 1000 * 7 * 24 * 30 * 12 * 2)), key); // Test with specific Date constructor.
    }

    @Test
    public void testCalendarDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("2022-01-01", context);
        Calendar calendar = (Calendar) key;
        Assert.assertEquals(2022, calendar.get(Calendar.YEAR));
        Assert.assertEquals(1, calendar.get(Calendar.MONTH));
        Assert.assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUUIDDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context);
        Assert.assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), key);
    }

    @Test
    public void testURIDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("http://example.com", context);
        Assert.assertEquals(new URI("http://example.com"), key);
    }

    @Test
    public void testURLDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("http://example.com", context);
        Assert.assertEquals(new URL("http://example.com"), key);

        try {
            deserializer.deserializeKey("https://example.com:8080/path?query=123#fragment", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("unable to parse key as URL", e.getMessage());
        }
    }

    @Test
    public void testClassDeserializer() throws IOException {
        Object key = deserializer.deserializeKey("java.lang.String", context);
        Assert.assertEquals(String.class, key);

        try {
            deserializer.deserializeKey("com.example.MyClass", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("unable to parse key as Class", e.getMessage());
        }
    }

    @Test
    public void testEnumDeserializer() throws IOException {
        JavaType enumType = context.constructType(EnumSet.class);
        Object key = deserializer.deserializeKey("values()", context);
        Assert.assertEquals(EnumSet.allOf(EnumSet.class), key);

        key = deserializer.deserializeKey("valueOf(EAST)", context);
        EnumSet enumSet = (EnumSet) key;
        EnumSet expected = EnumSet.of(EnumSet.EAST);
        Assert.assertArrayEquals(expected.toArray(), enumSet.toArray());

        try {
            deserializer.deserializeKey("INVALID", context);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("not one of values excepted for Enum class: [EAST, SOUTH, NORTH, WEST]", e.getMessage());
        }
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        JavaType javaType = context.constructType(String.class);
        Object keyDeserializer = deserializer.deserializeKey("StringDeserializer", context);
        JsonDeserializer<?> jsonDeserializer = context.findDeserializer(javaType, JsonDeserializer.class);
        JsonDeserializer.KeyDeserializer delegateKeyDeserializer = (JsonDeserializer.KeyDeserializer) keyDeserializer;
        JsonDeserializer.KeyDeserializer expectedKeyDeserializer = new StdKeyDeserializer.StringKD(javaType);
        Assert.assertEquals(expectedKeyDeserializer.getClass(), delegateKeyDeserializer.getClass());

        // Test deserialization.
        Object key = delegateKeyDeserializer.deserializeKey("test", context);
        Assert.assertEquals("test", key);
    }
}