package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        // no-op, since feature is already enabled
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.enable(Feature.ESCAPE_NON_ASCII);
        // no-op, since feature is already enabled
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        // no-op, since feature is already enabled
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testDisable() {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertEquals(0, generator.getFeatureMask());

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        // no-op, since feature is already disabled
        assertEquals(0, generator.getFeatureMask());

        generator.disable(Feature.ESCAPE_NON_ASCII);
        // no-op, since feature is already disabled
        assertEquals(0, generator.getFeatureMask());

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        // no-op, since feature is already disabled
        assertEquals(0, generator.getFeatureMask());
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue() | Feature.ESCAPE_NON_ASCII.getValue(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue() | Feature.STRICT_DUPLICATE_DETECTION.getValue(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue() | Feature.ESCAPE_NON_ASCII.getValue() | Feature.STRICT_DUPLICATE_DETECTION.getValue(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue(), 0);
        // no-op, since feature mask is already 0
        assertEquals(0, generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue() | Feature.ESCAPE_NON_ASCII.getValue(), 0);
        // no-op, since feature mask is already 0
        assertEquals(0, generator.getFeatureMask());

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getValue() | Feature.STRICT_DUPLICATE_DETECTION.getValue(), 0);
        // no-op, since feature mask is already 0
        assertEquals(0, generator.getFeatureMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        PrettyPrinter pp = generator.useDefaultPrettyPrinter();
        assertNotNull(pp);
        assertEquals(DefaultPrettyPrinter.class, pp.getClass());
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec oc = new ObjectMapper().getFactory().createEncoderDefaults();
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.setCodec(oc);
        assertEquals(oc, generator.getCodec());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCodecWithNull() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.setCodec(null);
    }
}