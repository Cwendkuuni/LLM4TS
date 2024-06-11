package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {
    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(0, null);

        // Test with disabled features
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test with enabled features
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase generatorWithFeatures = new GeneratorBase(features, null);
        assertTrue(generatorWithFeatures.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorWithFeatures.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generatorWithFeatures.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        int expectedMask = 0;
        assertEquals(expectedMask, generator.getFeatureMask());

        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase generatorWithFeatures = new GeneratorBase(features, null);
        int expectedFeaturesMask = features;
        assertEquals(expectedFeaturesMask, generatorWithFeatures.getFeatureMask());
    }

    @Test
    public void testEnableFeature() {
        GeneratorBase generator = new GeneratorBase(0, null);

        // Test enabling WRITE_NUMBERS_AS_STRINGS feature
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test enabling ESCAPE_NON_ASCII feature
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        // Test enabling STRICT_DUPLICATE_DETECTION feature
        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisableFeature() {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        // Test disabling WRITE_NUMBERS_AS_STRINGS feature
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling ESCAPE_NON_ASCII feature
        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        // Test disabling STRICT_DUPLICATE_DETECTION feature
        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);

        int newMask = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask();
        generator.setFeatureMask(newMask);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        int oldMask = newMask;
        newMask = oldMask ^ (Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask());
        generator.setFeatureMask(newMask);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(0, null);

        int newState = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask();
        generator.overrideStdFeatures(newState, newState);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        int oldState = newState;
        newState = oldState ^ (Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask());
        generator.overrideStdFeatures(newState, newState);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }
}