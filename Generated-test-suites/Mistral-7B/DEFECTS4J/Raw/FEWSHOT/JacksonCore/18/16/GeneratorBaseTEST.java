package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        // Test disabled by default
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabled
        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);
        int expected = 0;
        assertEquals(expected, gb.getFeatureMask());

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        expected = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        assertEquals(expected, gb.getFeatureMask());
    }

    @Test
    public void testEnableFeature() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        // Test disabled by default
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test enabling
        gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test disabling
        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisableFeature() throws Exception {
        GeneratorBase gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        // Test enabled
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test disabling
        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_as_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_as_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        // Test initial state
        int expected = 0;
        assertEquals(expected, gb.getFeatureMask());

        // Test setting feature mask
        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        expected = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        assertEquals(expected, gb.getFeatureMask());

        // Test setting to same mask
        gb.setFeatureMask(expected);
        assertEquals(expected, gb.getFeatureMask());

        // Test setting to different mask
        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        expected = Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        assertEquals(expected, gb.getFeatureMask());

        // Test setting negative mask
        gb.setFeatureMask(-1);
        expected = 0;
        assertEquals(expected, gb.getFeatureMask());
    }

    @Test
    public void testOverrideStdFeatures() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        // Test initial state
        int expected = 0;
        assertEquals(expected, gb.getFeatureMask());

        // Test overriding standard features
        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        expected = Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        assertEquals(expected, gb.getFeatureMask());

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.ESCAPE_NON_ASCII.getMask());
        expected = Feature.ESCAPE_NON_ASCII.getMask();
        assertEquals(expected, gb.getFeatureMask());

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        expected = Feature.STRICT_DUPLICATE_DETECTION.getMask();
        assertEquals(expected, gb.getFeatureMask());

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), 0);
        expected = 0;
        assertEquals(expected, gb.getFeatureMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        // Test null pretty printer
        assertNull(gb.getPrettyPrinter());

        // Test setting and getting default pretty printer
        gb.useDefaultPrettyPrinter();
        DefaultPrettyPrinter pp = (DefaultPrettyPrinter) gb.getPrettyPrinter();
        assertEquals(DefaultPrettyPrinter.class, pp.getClass());

        // Test setting and getting custom pretty printer
        DefaultPrettyPrinter customPP = new DefaultPrettyPrinter();
        gb.setPrettyPrinter(customPP);
        assertEquals(customPP, gb.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);
        ObjectCodec oc = null;

        // Test null codec
        assertNull(gb.getCodec());

        // Test setting and getting codec
        oc = new ObjectMapper().getFactory().createObjectCodec();
        gb.setCodec(oc);
        assertEquals(oc, gb.getCodec());
    }

    @Test
    public void testGetOutputContext() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);
        JsonWriteContext ctx = gb.getOutputContext();

        assertNull(ctx.getCurrentValue());
        assertFalse(ctx.isFlushed());
        assertFalse(ctx.isClosed());
    }

    @Test
    public void testWriteString() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        String text = "Hello, World!";
        gb.writeString(text);

        String actual = gb.getCurrentValue().toString();
        assertEquals(text, actual);
    }

    @Test
    public void testWriteRawValue() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        String text = "Hello, World!";
        gb.writeRawValue(text);

        String actual = gb.getCurrentValue().toString();
        assertEquals(text, actual);
    }

    @Test
    public void testWriteRawValueWithLength() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        String text = "Hello, World!";
        int offset = 0;
        int length = text.length();
        gb.writeRawValue(text, offset, length);

        String actual = gb.getCurrentValue().toString();
        assertEquals(text, actual);
    }

    @Test
    public void testWriteRawValueWithInvalidLength() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        String text = "Hello, World!";
        int offset = 0;
        int length = text.length() + 1;
        try {
            gb.writeRawValue(text, offset, length);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testWriteNull() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.writeNull();

        assertNull(gb.getCurrentValue());
    }

    @Test
    public void testWriteBoolean() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        boolean bool = true;
        gb.writeBoolean(bool);

        String actual = gb.getCurrentValue().toString();
        assertEquals(bool, Boolean.parseBoolean(actual));
    }

    @Test
    public void testWriteNumber() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        int number = 123;
        gb.writeNumber(number);

        String actual = gb.getCurrentValue().toString();
        assertEquals(number, Integer.parseInt(actual));
    }

    @Test
    public void testWriteNumberWithDoubleType() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        double number = 123.45;
        gb.writeNumber(number);

        String actual = gb.getCurrentValue().toString();
        assertEquals(number, Double.parseDouble(actual));
    }

    @Test
    public void testWriteNumberWithBigDecimalType() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        BigDecimal number = new BigDecimal("123.45");
        gb.writeNumber(number);

        String actual = gb.getCurrentValue().toString();
        assertEquals(number.toString(), actual);
    }

    @Test
    public void testFlush() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.writeString("Hello, World!");
        gb.flush();

        String actual = gb.getCurrentValue().toString();
        assertEquals("Hello, World!", actual);
        assertTrue(gb.isFlushed());
    }

    @Test
    public void testClose() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.writeString("Hello, World!");
        gb.close();

        String actual = gb.getCurrentValue().toString();
        assertEquals("Hello, World!", actual);
        assertTrue(gb.isClosed());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws Exception {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.writeString("Hello, World!");
        gb.close();

        gb.writeString("Hello, again!");
    }
}