package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class StdKeyDeserializerTest {

    @Test
    public void testForTypeBoolean() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testForTypeByte() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) 1, key);

        try {
            key = deserializer.deserializeKey("256", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testForTypeShort() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertTrue(key instanceof Short);
        assertEquals((short) 1, key);

        try {
            key = deserializer.deserializeKey("32768", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testForTypeChar() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", new TestContext());
        assertTrue(key instanceof Character);
        assertEquals('A', key);
    }

    @Test
    public void testForTypeInt() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertEquals(1, key);
    }

    @Test
    public void testForTypeLong() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("1", new TestContext());
        assertEquals(1L, key);

        Object longMaxKey = deserializer.deserializeKey(Long.toString(Long.MAX_VALUE), new TestContext());
        assertEquals(Long.MAX_VALUE, longMaxKey);
    }

    @Test
    public void testForTypeFloat() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("1.0", new TestContext());
        assertEquals(1.0f, key);
    }

    @Test
    public void testForTypeDouble() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("1.0", new TestContext());
        assertEquals(1.0, key);
    }

    @Test
    public void testForTypeLocale() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testForTypeDate() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object key = deserializer.deserializeKey("2022-01-01", new TestContext());
        assertInstanceof(Date.class, key);
    }

    @Test
    public void testForTypeCalendar() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = deserializer.deserializeKey("2022-01-01", new TestContext());
        assertInstanceof(Calendar.class, key);
    }

    @Test
    public void testForTypeUUID() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", new TestContext());
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), key);
    }

    @Test
    public void testForTypeURI() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), key);
    }

    @Test
    public void testForTypeURL() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), key);
    }

    @Test
    public void testForTypeClass() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object key = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);

        try {
            key = deserializer.deserializeKey("java.util.ArrayList", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals("unable to parse key as Class", e.getMessage());
        }
    }

    @Test
    public void testForTypeEnum() throws IOException, JsonProcessingException {
        Enum e1 = MyEnum.VALUE1;
        Enum e2 = MyEnum.VALUE2;

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);

        Object key1 = deserializer.deserializeKey(e1.name(), new TestContext());
        assertEquals(e1, key1);

        Object key2 = deserializer.deserializeKey(e2.name(), new TestContext());
        assertEquals(e2, key2);
    }

    @Test
    public void testForTypeEnumWithUnknownValue() throws IOException, JsonProcessingException {
        Enum e1 = MyEnum.VALUE1;
        Enum e2 = MyEnum.VALUE2;
        Enum e3 = MyEnum.VALUE3;

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);

        Object key1 = deserializer.deserializeKey(e1.name(), new TestContext(true));
        assertEquals(e1, key1);

        Object key2 = deserializer.deserializeKey(e2.name(), new TestContext(true));
        assertEquals(e2, key2);

        Object key3 = deserializer.deserializeKey("UNKNOWN_VALUE", new TestContext(true));
        assertNull(key3);
    }

    private static class TestContext extends DeserializationContext {
        private TestContext() {
            super(null, null, null, null, null, null, null, null, null);
        }

        private TestContext(boolean allowUnknownEnumValues) {
            super(null, null, null, null, null, null, null, null, new JacksonAnnotationIntrospector());
            this.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, allowUnknownEnumValues);
        }
    }
}