package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    @Test
    public void testVersion() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        assertEquals("2.12.0", gen.version().toString());
    }

    @Test
    public void testIsEnabled() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        JsonGenerator gen2 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(gen2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen2.isEnabled(Feature.ESCAPE_NON_ASCII));

        JsonGenerator gen3 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(gen3.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gen3.isEnabled(Feature.ESCAPE_NON_ASCII));

        JsonGenerator gen4 = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK & ~Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertFalse(gen4.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gen4.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testSetEnabled() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);

        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        gen.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        gen.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetOutputContext() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        assertNotNull(gen.getOutputContext());
    }

    @Test
    public void testWriteString() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);

        gen.writeString("test");
        assertEquals("test", gen.getCurrentValue());

        gen.writeString("test \u00E1\u00E9\u2603");
        assertEquals("test \u00E1\u00E9\u2603", gen.getCurrentValue());

        gen.writeString("test \uD800\uDC00\uD800\uDCCC");
        assertEquals("test \uD800\uDC00\uD800\uDCCC", gen.getCurrentValue());
    }

    @Test
    public void testWriteRawValue() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);

        char[] test = {'t', 'e', 's', 't'};
        gen.writeRawValue(test, 0, 4);
        assertEquals(new String(test), gen.getCurrentValue().toString());

        gen.writeRawValue("test \u00E1\u00E9\u2603");
        assertEquals("test \u00E1\u00E9\u2603", gen.getCurrentValue().toString());

        gen.writeRawValue("\uD800\uDC00\uD800\uDCCC");
        assertEquals("\uD800\uDC00\uD800\uDCCC", gen.getCurrentValue().toString());

        gen.writeRawValue("\uD800\uDC01\uD800\uDCCC");
        // Surrogate pair not valid
        assertThrows(JsonGenerationException.class, () -> gen.getCurrentValue().toString());
    }

    @Test
    public void testWriteNumber() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);

        gen.writeNumber(123);
        assertEquals("123", gen.getCurrentValue().toString());

        gen.writeNumber(123.45);
        assertEquals("123.45", gen.getCurrentValue().toString());

        gen.writeNumber(123_000_000);
        assertEquals("123000000", gen.getCurrentValue().toString());

        gen.writeNumber(new BigDecimal("123.45"));
        assertEquals("123.45", gen.getCurrentValue().toString());

        gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);

        gen.writeNumber(123);
        assertEquals(123, gen.getCurrentValue());

        gen.writeNumber(123.45);
        assertEquals(123.45, gen.getCurrentValue());

        gen.writeNumber(123_000_000L);
        assertEquals(123000000L, gen.getCurrentValue());

        gen.writeNumber(new BigDecimal("123.45"));
        assertEquals(new BigDecimal("123.45"), gen.getCurrentValue());
    }

    @Test
    public void testWriteBoolean() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);

        gen.writeBoolean(true);
        assertEquals(true, gen.getCurrentValue());

        gen.writeBoolean(false);
        assertEquals(false, gen.getCurrentValue());
    }

    @Test
    public void testWriteNull() throws JsonProcessingException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);

        gen.writeNull();
        assertNull(gen.getCurrentValue());

        gen.enable(Feature.WRITE_NULL_AS_FIELD_NAME);
        gen.writeNull();
        assertEquals("null", gen.getCurrentValue().toString());
    }

    @Test
    public void testWriteObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":30,\"cars\":[{\"color\":\"red\",\"model\":\"Ford\"},{\"color\":\"blue\",\"model\":\"Mazda\"}]}";
        Object tree = mapper.readValue(json, Object.class);

        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        gen.writeObject(tree);
        assertEquals(json, gen.getOutputContext().toString());
    }

    @Test
    public void testFlush() throws JsonProcessingException, IOException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        gen.writeString("test");
        gen.flush();
        assertTrue(gen.isClosed());
    }

    @Test
    public void testClose() throws JsonProcessingException, IOException {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        gen.writeString("test");
        gen.close();
        assertTrue(gen.isClosed());
    }

    @Test
    public void testWriteStartArray() {
        // This method is not present in the provided code snippet
    }

    @Test
    public void testWriteEndArray() {
        // This method is not present in the provided code snippet
    }

    @Test
    public void testWriteStartObject() {
        // This method is not present in the provided code snippet
    }

    @Test
    public void testWriteEndObject() {
        // This method is not present in the provided code snippet
    }

    private void assertThrows(Class<? extends Throwable> expectedType, Runnable action) {
        try {
            action.run();
            fail("Expected exception of type " + expectedType.getName());
        } catch (Throwable e) {
            if (expectedType.isInstance(e)) {
                return;
            }
            fail("Unexpected exception: " + e.getClass().getName());
        }
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        JsonGenerator gen = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
        gen.useDefaultPrettyPrinter();
        assertSame(DefaultPrettyPrinter.class, gen.getPrettyPrinter().getClass());
    }
}