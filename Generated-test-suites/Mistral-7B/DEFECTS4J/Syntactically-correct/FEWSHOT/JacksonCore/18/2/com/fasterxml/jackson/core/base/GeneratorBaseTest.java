package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        gen = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gen = new GeneratorBase(Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertFalse(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase gen = new GeneratorBase(0, null);
        assertEquals(0, gen.getFeatureMask());

        gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertEquals(0x0000001F, gen.getFeatureMask());
    }

    @Test
    public void testEnable_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.setFeatures(Feature.WRITE_NUMBERS_AS_Strings.getMask());
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testEnable_ESCAPE_NON_ASCII() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        gen.setFeatures(Feature.ESCAPE_NON_ASCII.getMask());
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testEnable_STRICT_DUPLICATE_DETECTION() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gen.setFeatures(Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertFalse(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisable_WRITE_NUMBERS_AS_STRINGS() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.setFeatures(0);
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testDisable_ESCAPE_NON_ASCII() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.ESCAPE_NON_ASCII.getMask(), null);
        gen.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(gen.isEnabled(Feature.ESCAPE_NON_ASCII));

        gen.setFeatures(0);
        assertTrue(gen.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testDisable_STRICT_DUPLICATE_DETECTION() throws IOException {
        GeneratorBase gen = new GeneratorBase(Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        gen.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        gen.setFeatures(0);
        assertTrue(gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.setFeatureMask(0);
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testOverrideStdFeatures() throws IOException {
        GeneratorBase gen = new GeneratorBase(0, null);
        gen.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertFalse(gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }
}