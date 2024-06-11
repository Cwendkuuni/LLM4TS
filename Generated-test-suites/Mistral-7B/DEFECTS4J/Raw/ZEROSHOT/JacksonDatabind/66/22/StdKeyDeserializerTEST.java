package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer booleanStdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();
        Object key = booleanStdKeyDeserializer.deserializeKey("true", context);
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        Object keyFalse = booleanStdKeyDeserializer.deserializeKey("false", context);
        assertTrue(keyFalse instanceof Boolean);
        assertEquals(Boolean.FALSE, keyFalse);
    }

    @Test
    public void testByteTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer byteStdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();
        Object key = byteStdKeyDeserializer.deserializeKey("127", context);
        assertTrue(key instanceof Byte);
        assertEquals((byte) 127, key);

        try {
            byteStdKeyDeserializer.deserializeKey("256", context);
            fail("Expected IOException for byte type with value greater than 127");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testShortTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer shortStdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();
        Object key = shortStdKeyDeserializer.deserializeKey("32767", context);
        assertTrue(key instanceof Short);
        assertEquals((short) 32767, key);

        try {
            shortStdKeyDeserializer.deserializeKey("32768", context);
            fail("Expected IOException for short type with value greater than 32767");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCharTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer charStdKeyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = createContext();
        Object key = charStdKeyDeserializer.deserializeKey("A", context);
        assertTrue(key instanceof Character);
        assertEquals('A', key);
    }

    @Test
    public void testIntTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer intStdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        Object key = intStdKeyDeserializer.deserializeKey("42", context);
        assertTrue(key instanceof Integer);
        assertEquals(42, key);
    }

    @Test
    public void testLongTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer longStdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        Object key = longStdKeyDeserializer.deserializeKey("4294967295", context);
        assertTrue(key instanceof Long);
        assertEquals(4294967295L, key);
    }

    @Test
    public void testFloatTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer floatStdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = createContext();
        Object key = floatStdKeyDeserializer.deserializeKey("3.14159265", context);
        assertTrue(key instanceof Float);
        assertEquals(3.14159265F, key);
    }

    @Test
    public void testDoubleTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer doubleStdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = createContext();
        Object key = doubleStdKeyDeserializer.deserializeKey("3.14159265358979323846", context);
        assertTrue(key instanceof Double);
        assertEquals(3.14159265358979323846, key);
    }

    @Test
    public void testLocaleTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer localeStdKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = createContext();

        Object key = localeStdKeyDeserializer.deserializeKey("en_US", context);
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);

        try {
            localeStdKeyDeserializer.deserializeKey("invalidLocale", context);
            fail("Expected IOException for invalid Locale key");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDateTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer dateStdKeyDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = createContext();
        Object key = dateStdKeyDeserializer.deserializeKey("2023-03-12", context);
        assertTrue(key instanceof Date);
    }

    @Test
    public void testCalendarTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer calendarStdKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = createContext();
        Object key = calendarStdKeyDeserializer.deserializeKey("2023-03-12", context);
        assertTrue(key instanceof Calendar);
    }

    @Test
    public void testUUIDTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer uuidStdKeyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = createContext();
        Object key = uuidStdKeyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426655440000", context);
        assertTrue(key instanceof UUID);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426655440000"), key);
    }

    @Test
    public void testURITypeKeyDeserialization() throws IOException {
        StdKeyDeserializer uriStdKeyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = createContext();
        Object key = uriStdKeyDeserializer.deserializeKey("https://www.example.com", context);
        assertTrue(key instanceof URI);
        assertEquals(new URI("https://www.example.com"), key);
    }

    @Test
    public void testURLTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer urlStdKeyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();
        Object key = urlStdKeyDeserializer.deserializeKey("https://www.example.com", context);
        assertTrue(key instanceof URL);
        assertEquals(new URL("https://www.example.com"), key);
    }

    @Test
    public void testClassTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer classStdKeyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = createContext();
        Object key = classStdKeyDeserializer.deserializeKey("java.lang.String", context);
        assertTrue(key instanceof Class);
        assertEquals(String.class, key);
    }

    @Test
    public void testEnumTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer enumStdKeyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);
        DeserializationContext context = createContext();

        Object key = enumStdKeyDeserializer.deserializeKey("MONDAY", context);
        assertTrue(key instanceof DayOfWeek);
        assertEquals(DayOfWeek.MONDAY, key);

        Object keyUnknown = enumStdKeyDeserializer.deserializeKey("THURSDAY13", context);
        assertTrue(keyUnknown instanceof String);
        assertEquals("THURSDAY13", keyUnknown);
    }

    @Test
    public void testInvalidTypeKeyDeserialization() throws IOException {
        StdKeyDeserializer invalidTypeKeyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();

        Object key = invalidTypeKeyDeserializer.deserializeKey("invalid", context);
        assertTrue(key instanceof String);
        assertEquals("not a valid representation", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeByteKeyDeserialization() throws IOException {
        StdKeyDeserializer byteStdKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();
        byteStdKeyDeserializer.deserializeKey("-1", context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeShortKeyDeserialization() throws IOException {
        StdKeyDeserializer shortStdKeyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();
        shortStdKeyDeserializer.deserializeKey("-1", context);
    }

    @Test
    public void testInvalidBooleanKeyDeserialization() throws IOException {
        StdKeyDeserializer booleanStdKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();
        Object key = booleanStdKeyDeserializer.deserializeKey("invalid", context);
        assertTrue(key instanceof String);
        assertEquals("not a valid representation", key);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidIntegerKeyDeserialization() throws IOException {
        StdKeyDeserializer intStdKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        intStdKeyDeserializer.deserializeKey("invalidInteger", context);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLongKeyDeserialization() throws IOException {
        StdKeyDeserializer longStdKeyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        longStdKeyDeserializer.deserializeKey("invalidLong", context);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidFloatKeyDeserialization() throws IOException {
        StdKeyDeserializer floatStdKeyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = createContext();
        floatStdKeyDeserializer.deserializeKey("invalidFloat", context);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidDoubleKeyDeserialization() throws IOException {
        StdKeyDeserializer doubleStdKeyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = createContext();
        doubleStdKeyDeserializer.deserializeKey("invalidDouble", context);
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getDeserializationConfig();
        return config.withRootName("root");
    }
}