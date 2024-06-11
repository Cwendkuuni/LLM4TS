package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testConfigure() {
        // Method is not implemented and marked as deprecated
    }

    @Test
    public void testEnable_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnable_ESCAPE_NON_ASCII() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testEnable_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(0, null, null);
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() {
        // Method is deprecated
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(0, null);
        int oldState = generator.getFeatureMask();
        int newState = oldState | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.overrideStdFeatures(newState, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }
}