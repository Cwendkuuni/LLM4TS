package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertEquals(0, gb.getFeatureMask());

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), gb.getFeatureMask());

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), gb.getFeatureMask());

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), gb.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = new GeneratorBase(0, null);
        gb.enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(127, gb.getHighestNonEscapedChar());

        gb = new GeneratorBase(0, null);
        gb.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertSame(DupDetector.rootDetector(gb), gb.getWriteContext().getDupDetector());
    }

    @Test
    public void testDisable() {
        GeneratorBase gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertEquals(0, gb.getHighestNonEscapedChar());

        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertNull(gb.getWriteContext().getDupDetector());
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertSame(DupDetector.rootDetector(gb), gb.getWriteContext().getDupDetector());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertNull(gb.getPrettyPrinter());
        gb.useDefaultPrettyPrinter();
        assertNotNull(gb.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertNull(gb.getCodec());
        gb.setCodec(new ObjectCodec() {});
        assertNotNull(gb.getCodec());
    }

    @Test
    public void testGetOutputContext() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertNull(gb.getOutputContext());

        gb = new GeneratorBase(0, null, new JsonWriteContext());
        assertSame(gb.getOutputContext(), gb.getWriteContext());
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeString("foo");
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeRawValue("foo");
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testWriteRawValue_charArray() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeRawValue("foo".toCharArray(), 0, 3);
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testWriteRawValue_string() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeRawValue("foo");
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testWriteRawValue_SerializableString() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeRawValue(new SerializableString("foo"));
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeObject("foo");
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeTree(new Object());
        assertEquals(null, gb.getCurrentValue());

        gb = new GeneratorBase(0, null);
        gb.setCodec(new ObjectCodec() {
            @Override
            public void writeValue(JsonGenerator gen, Object value) throws IOException {
                gen.writeString("foo");
            }
        });
        gb.writeTree(new Object());
        assertEquals("foo", gb.getCurrentValue());
    }

    @Test
    public void testFlush() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.flush();
    }

    @Test
    public void testClose() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.close();
        assertTrue(gb.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteAfterClose() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.close();
        gb.writeString("foo");
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteBoolean() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        // Not implemented at this level
    }

    @Test
    public void testWriteNull() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeNull();
        assertEquals(null, gb.getCurrentValue());
    }

    @Test
    public void testWriteNumber_int() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeNumber(42);
        assertEquals(Integer.toString(42), gb.getCurrentValue());
    }

    @Test
    public void testWriteNumber_long() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeNumber(42L);
        assertEquals(Long.toString(42L), gb.getCurrentValue());
    }

    @Test
    public void testWriteNumber_double() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeNumber(3.14159265358979323846);
        assertEquals(Double.toString(3.14159265358979323846), gb.getCurrentValue());
    }

    @Test
    public void testWriteNumber_float() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeNumber(3.14159265f);
        assertEquals(Float.toString(3.14159265f), gb.getCurrentValue());
    }

    @Test
    public void testWriteNumber_BigDecimal() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.writeNumber(new BigDecimal("3.14159265358979323846"));
        assertEquals("3.14159265358979323846", gb.getCurrentValue().toString());
    }
}