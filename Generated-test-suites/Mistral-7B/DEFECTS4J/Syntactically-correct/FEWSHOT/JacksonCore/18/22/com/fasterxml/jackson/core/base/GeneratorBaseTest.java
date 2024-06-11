package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
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
        int enabledFeatures = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase generatorEnabled = new GeneratorBase(enabledFeatures, null, null);

        assertTrue(generatorEnabled.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorEnabled.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generatorEnabled.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeature() {
        GeneratorBase generator = new GeneratorBase(0, null);

        // Test enabling features
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test disabling features
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        int features = generator.getFeatureMask();
        assertEquals(0, features);

        int enabledFeatures = Feature.WRITE_NUMBERS_AS_STRINGS.getMask() |
                Feature.ESCAPE_NON_ASCII.getMask() |
                Feature.STRICT_DUPLICATE_DETECTION.getMask();
        GeneratorBase generatorEnabled = new GeneratorBase(enabledFeatures, null, null);
        int mask = generatorEnabled.getFeatureMask();
        assertEquals(enabledFeatures, mask);
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generator = new GeneratorBase(0, null);
        PrettyPrinter defaultPrettyPrinter = generator.useDefaultPrettyPrinter();
        assertNotNull(defaultPrettyPrinter);
        assertTrue(defaultPrettyPrinter instanceof DefaultPrettyPrinter);
    }

    @Test(expected = IllegalStateException.class)
    public void testUseDefaultPrettyPrinterAlreadyAssigned() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setPrettyPrinter(new DefaultPrettyPrinter());
        generator.useDefaultPrettyPrinter();
    }

    @Test
    public void testSetCodec() {
        GeneratorBase generator = new GeneratorBase(0, null);
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        generator.setCodec(objectCodec);
        assertEquals(objectCodec, generator.getCodec());
    }
}