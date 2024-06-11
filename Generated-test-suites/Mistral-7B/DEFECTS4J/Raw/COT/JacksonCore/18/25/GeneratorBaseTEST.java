package com.fasterxml.jackson.core.base; // make sure to import all required classes

import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.core.base.GeneratorBase.*;

/**
 * Test class for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    // Test instance of GeneratorBase
    private GeneratorBase generatorBase;

    @Test
    public void testConstructor() {
        // Test constructor with default feature mask and ObjectCodec
        generatorBase = new GeneratorBase(DEFAULT_FEATURES_MASK, null);

        // Test constructor with specified feature mask and ObjectCodec
        generatorBase = new GeneratorBase(FEATURES_WITH_WRITE_NUMBERS_AS_STRINGS, new ObjectCodec() {});
    }

    @Test
    public void testIsEnabled_Feature() {
        generatorBase = new GeneratorBase(DEFAULT_FEATURES_MASK, null);

        Assert.assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        Assert.assertTrue(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
        Assert.assertTrue(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generatorBase = new GeneratorBase(NO_WRITE_NUMBERS_AS_STRINGS_FEATURES, null);

        Assert.assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        Assert.assertTrue(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
        Assert.assertTrue(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testEnable_Feature() {
        // Test enabling WRITE_NUMBERS_AS_STRINGS feature
        generatorBase = new GeneratorBase(NO_WRITE_NUMBERS_AS_STRINGS_FEATURES, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        Assert.assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test disabling WRITE_NUMBERS_AS_STRINGS feature
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        Assert.assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test enabling ESCAPE_NON_ASCII feature
        generatorBase = new GeneratorBase(NO_ESCAPE_NON_ASCII_FEATURES, null);
        generatorBase.enable(Feature.ESCAPE_NON_ASCII);
        Assert.assertTrue(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));

        // Test disabling ESCAPE_NON_ASCII feature
        generatorBase.disable(Feature.ESCAPE_NON_ASCII);
        Assert.assertFalse(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));

        // Test enabling STRICT_DUPLICATE_DETECTION feature
        generatorBase = new GeneratorBase(NO_STRICT_DUPLICATE_DETECTION_FEATURES, null);
        generatorBase.enable(Feature.STRICT_DUPLICATE_DETECTION);
        Assert.assertTrue(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test disabling STRICT_DUPLICATE_DETECTION feature
        generatorBase.disable(Feature.STRICT_DUPLICATE_DETECTION);
        Assert.assertFalse(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFeatureMask_IllegalStateException() {
        generatorBase = new GeneratorBase(DEFAULT_FEATURES_MASK, null);
        generatorBase.setFeatureMask(0xFFFFFFFF); // invalid feature mask
    }

    @Test
    public void testVersion() {
        generatorBase = new GeneratorBase(DEFAULT_FEATURES_MASK, null);
        Assert.assertNotNull(generatorBase.version());
    }

    // Add test cases for other methods in GeneratorBase class
}