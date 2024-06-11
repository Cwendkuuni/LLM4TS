package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator.enable(Feature.ESCAPE_NON_ASCII);
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        int mask = Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        assertEquals(mask, generator.getFeatureMask());
    }

    @Test
    public void testEnable_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnable_ESCAPE_NON_ASCII() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.setHighestNonEscapedChar(127);
    }

    @Test
    public void testEnable_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        DupDetector dupDetector = generator.getWriteContext().getDupDetector();
        assertNotNull(dupDetector);
    }

    @Test
    public void testDisable_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable_ESCAPE_NON_ASCII() {
        GeneratorBase generator = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.setHighestNonEscapedChar(0);
    }

    @Test
    public void testDisable_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase generator = new GeneratorBase(Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.getWriteContext().setDupDetector(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setFeatureMask(0xff);
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(0, null);
        int originalFeatures = generator.getFeatureMask();

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(Feature.ESCAPE_NON_ASCII.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(originalFeatures, generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.STRICT_DUPLICATE_DETECTION.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertEquals(originalFeatures, generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }
}