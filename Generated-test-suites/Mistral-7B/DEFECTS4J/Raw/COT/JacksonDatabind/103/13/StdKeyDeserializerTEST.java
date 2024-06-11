package com.example; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    @org.junit.BeforeClass
    public static void setUp() {
        // Initialize ObjectMapper for deserialization
        ObjectMapper mapper = new ObjectMapper();
    }

    

    @org.junit.Test
    public void testDeserializeKey_Boolean() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("true", ctxt);
        assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", ctxt);
        assertEquals(Boolean.FALSE, key);

        key = serializer.deserializeKey("invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Byte() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("127", ctxt);
        assertEquals((byte) 127, key);

        try {
            key = serializer.deserializeKey("128", ctxt);
            assertThrows(IllegalArgumentException.class, () -> key);
        } catch (IllegalArgumentException e) {
            assertEquals("overflow, value cannot be represented as 8-bit value", e.getMessage());
        }

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Short() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("32767", ctxt);
        assertEquals((short) 32767, key);

        try {
            key = serializer.deserializeKey("32768", ctxt);
            assertThrows(IllegalArgumentException.class, () -> key);
        } catch (IllegalArgumentException e) {
            assertEquals("overflow, value cannot be represented as 16-bit value", e.getMessage());
        }

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Character() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("A", ctxt);
        assertEquals((char) 'A', key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Int() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("42", ctxt);
        assertEquals(42, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Long() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("9223372036854775807", ctxt);
        assertEquals(Long.MAX_VALUE, key);

        try {
            key = serializer.deserializeKey("9223372036854775808", ctxt);
            assertThrows(IllegalArgumentException.class, () -> key);
        } catch (IllegalArgumentException e) {
            assertEquals("overflow, value cannot be represented as 64-bit value", e.getMessage());
        }

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Float() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer._parse("3.14159", ctxt);
        assertEquals(3.14159f, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Double() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer._parse("3.14159265358979323846264338327950288416", ctxt);
        assertEquals(3.14159265358979323846264338327950288416, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Locale() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer._parse("en_US", ctxt);
        assertEquals(Locale.US, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Date() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer._parse("2023-03-01", ctxt);
        assertEquals(new Date(1678400000000L), key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Calendar() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer._parse("2023-03-01", ctxt);
        GregorianCalendar expectedCalendar = new GregorianCalendar(2023, 2, 1);
        assertEquals(expectedCalendar, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_Enum() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("RED", ctxt);
        assertEquals(MyEnum.RED, key);

        try {
            key = serializer.deserializeKey("GREEN", ctxt);
            assertThrows(IllegalArgumentException.class, () -> key);
        } catch (IllegalArgumentException e) {
            assertEquals("not one of values excepted for Enum class: [RED, BLUE]", e.getMessage());
        }

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_UUID() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("123e4567-e89b-12d3-a456-426655440000", ctxt);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426655440000"), key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_URL() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("https://www.example.com", ctxt);
        assertEquals(new URL("https://www.example.com"), key);

        try {
            key = serializer.deserializeKey("Invalid", ctxt);
            assertThrows(MalformedURLException.class, () -> key);
        } catch (MalformedURLException e) {
            assertEquals("Malformed URL", e.getMessage());
        }
    }

    @org.junit.Test
    public void testDeserializeKey_Class() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyClass.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("com.example.MyClass", ctxt);
        assertEquals(MyClass.class, key.getClass());

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_NullValueProvider() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, new NullValueProvider.Base() {});

        Object key = serializer.deserializeKey("null", ctxt);
        assertEquals(null, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    @org.junit.Test
    public void testDeserializeKey_EnumDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null, null);

        Object key = serializer.deserializeKey("MyEnumValue", ctxt);
        assertEquals(MyEnum.MyEnumValue, key);

        key = serializer.deserializeKey("Invalid", ctxt);
        assertNull(key);
    }

    
}

enum MyEnum {
    RED,
    BLUE;
}

class MyClass {
}