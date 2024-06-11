package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriter.Feature;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testGetFeatureMask() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testEnable() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS); // should not have any effect
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());

        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS); // should not have any effect
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testDisable() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS); // should not have any effect
        assertEquals(0, generator.getFeatureMask());

        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS); // should not have any effect
        assertEquals(0, generator.getFeatureMask());
    }

    @Test
    public void testOverrideStdFeatures() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(0, null);

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.overrideStdFeatures(Feature.ESCAPE_NON_ASCII.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.overrideStdFeatures(Feature.STRICT_DUPLICATE_DETECTION.getMask(), 0);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        generator.overrideStdFeatures(0, Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getPrettyPrinter());

        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
        assertTrue(generator.getPrettyPrinter() instanceof DefaultPrettyPrinter);

        generator.useDefaultPrettyPrinter();
        assertSame(generator.getPrettyPrinter(), generator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() throws JsonProcessingException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getCodec());

        ObjectCodec codec = new ObjectMapper().getFactory().createObjectCodec();
        generator.setCodec(codec);
        assertEquals(codec, generator.getCodec());

        generator.setCodec(null);
        assertNull(generator.getCodec());
    }
}