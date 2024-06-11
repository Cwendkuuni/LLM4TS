package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {
    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS); // already enabled, so no effect
        generator.enable(Feature.ESCAPE_NON_ASCII); // already enabled, so no effect
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION); // already enabled, so no effect
    }

    @Test
    public void testDisable() {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS); // already disabled, so no effect
        generator.disable(Feature.ESCAPE_NON_ASCII); // already disabled, so no effect
        generator.disable(Feature.STRICT_DUPLICATE_DETECTION); // already disabled, so no effect
    }

    @Test
    public void testSetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.setFeatureMask(Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.setFeatureMask(0);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.overrideStdFeatures(Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), 0);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }
}