package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();
        String json = writer.writeValueAsString(true);

        KeyDeserializer keyDeserializer = mapper.getKeyDeserializerFactory().findKeyDeserializer(Boolean.class);
        assertInstanceOf(StdKeyDeserializer.class, keyDeserializer);
        StdKeyDeserializer stdKeyDeserializer = (StdKeyDeserializer) keyDeserializer;
        assertEquals(Boolean.TRUE, stdKeyDeserializer.deserializeKey("true", mapper.getDeserializationContext()));

        json = writer.writeValueAsString(false);
        assertEquals(Boolean.FALSE, stdKeyDeserializer.deserializeKey("false", mapper.getDeserializationContext()));

        // Invalid value
        try {
            stdKeyDeserializer.deserializeKey("invalid", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            assertEquals("not a valid representation: invalid", e.getMessage());
        }
    }

    @Test
    public void testByteDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (byte value = Byte.MIN_VALUE; value <= 255; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Byte.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }

        // Invalid value
        try {
            deserializer.deserializeKey("256", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            assertEquals("not a valid representation: overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (short value = Short.MIN_VALUE; value <= Short.MAX_VALUE; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Short.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }

        // Invalid value
        try {
            deserializer.deserializeKey("32768", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            assertEquals("not a valid representation: overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (char value = '\u0000'; value <= '\uFFFF'; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Character.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }

        // Invalid value
        try {
            deserializer.deserializeKey("", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (int value = Integer.MIN_VALUE; value <= Integer.MAX_VALUE; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Integer.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }
    }

    @Test
    public void testLongDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (long value = Long.MIN_VALUE; value <= Long.MAX_VALUE; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Long.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }
    }

    @Test
    public void testFloatDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (float value = Float.MIN_VALUE; value <= Float.MAX_VALUE; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Float.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }
    }

    @Test
    public void testDoubleDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter writer = mapper.writer();

        for (double value = Double.MIN_VALUE; value <= Double.MAX_VALUE; value++) {
            String json = writer.writeValueAsString(value);
            assertEquals(Double.valueOf(value), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
        }
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = (StdKeyDeserializer) mapper.getKeyDeserializerFactory().findKeyDeserializer(Locale.class);
        String json = "en_US";

        assertEquals(Locale.US, deserializer.deserializeKey(json, mapper.getDeserializationContext()));

        json = "de_DE";
        assertEquals(Locale.GERMANY, deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testUriDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        String json = "http://example.com";

        assertEquals(new URI(json), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testUrlDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        String json = "http://example.com";

        assertEquals(new URL(json), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testClassDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        String json = "[\"Hello\", \"World\"]";

        assertEquals(new String[2].getClass(), deserializer.deserializeKey(json, mapper.getDeserializationContext()).getClass());
        assertArrayEquals(new String[]{"Hello", "World"}, (String[]) deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = (StdKeyDeserializer) mapper.getKeyDeserializerFactory().findKeyDeserializer(Currency.class);
        String json = "$12.34";

        assertEquals(Currency.getInstance("USD"), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testDateDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        String json = "2022-12-31";

        assertEquals(new Date(1671142399000L), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testCalendarDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        String json = "2022-12-31";

        java.util.Date date = mapper.getDeserializationContext().parseDate(json);
        assertEquals(java.util.Calendar.getInstance().setTime(date), deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }

    @Test
    public void testEnumDeserialization() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        Enum e = Enum.values()[0];
        StdKeyDeserializer deserializer = (StdKeyDeserializer) mapper.getKeyDeserializerFactory().findKeyDeserializer(e.getClass());
        String json = e.name();

        assertEquals(e, deserializer.deserializeKey(json, mapper.getDeserializationContext()));
    }
}