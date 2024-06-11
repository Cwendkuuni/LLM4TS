package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase gb = new GeneratorBase(0, null);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
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
    public void testEnable() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS); // already enabled
        assertNull(gb.getWriteContext().getDupDetector());

        gb = new GeneratorBase(0, null, null);
        gb.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        gb.enable(Feature.ESCAPE_NON_ASCII);
        gb.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), gb.getFeatureMask());
        assertNotNull(gb.getWriteContext().getDupDetector());
    }

    @Test
    public void testDisable() throws IOException {
        GeneratorBase gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS); // already disabled
        assertNull(gb.getWriteContext().getDupDetector());

        gb = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null, null);
        gb.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        gb.disable(Feature.ESCAPE_NON_ASCII);
        gb.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertEquals(0, gb.getFeatureMask());
        assertNull(gb.getWriteContext().getDupDetector());
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.setFeatureMask(Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.setFeatureMask(Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase gb = new GeneratorBase(0, null);

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gb.overrideStdFeatures(Feature.ESCAPE_NON_ASCII.getMask(), Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));

        gb.overrideStdFeatures(Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertFalse(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gb.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(gb.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(gb.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(gb.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }
}