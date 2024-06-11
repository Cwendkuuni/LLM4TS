package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase gb = new GeneratorBase(0, null);

        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertEquals(0, gb.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisable() {
        GeneratorBase gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.setFeatureMask(gb.getFeatureMask() & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.setFeatureMask(gb.getFeatureMask() & ~Feature.ESCAPE_NON_ASCII.getMask());
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase gb = new GeneratorBase(0, null);
        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.overrideStdFeatures(gb.getFeatureMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.overrideStdFeatures(gb.getFeatureMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.overrideStdFeatures(gb.getFeatureMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.overrideStdFeatures(gb.getFeatureMask(), Feature.ESCAPE_NON_ASCII.getMask());
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertNull(gb.getPrettyPrinter());

        gb.useDefaultPrettyPrinter();
        assertNotNull(gb.getPrettyPrinter());
        assertTrue(gb.getPrettyPrinter() instanceof DefaultPrettyPrinter);
    }

    @Test
    public void testSetCodec() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertNull(gb.getCodec());

        ObjectCodec oc = new ObjectCodec();
        gb.setCodec(oc);
        assertEquals(oc, gb.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertNull(gb.getOutputContext());
    }
}