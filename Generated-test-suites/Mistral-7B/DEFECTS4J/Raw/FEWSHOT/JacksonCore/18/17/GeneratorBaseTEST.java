package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb = new GeneratorBase(Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
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
    }

    @Test
    public void testEnable_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb = gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = new GeneratorBase(0, null).enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), gb.getFeatureMask());

        gb = new GeneratorBase(0, null).enable(Feature.WRITE_NUMBERS_AS_STRINGS).enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), gb.getFeatureMask());
    }

    @Test
    public void testDisable_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);

        gb = gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null).disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.ESCAPE_NON_ASCII.getMask(), gb.getFeatureMask());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFeatureMask_negative() {
        new GeneratorBase(0, null).setFeatureMask(-1);
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb = gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb = gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb = gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }
}