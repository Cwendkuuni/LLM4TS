package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.json.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test cases for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    /**
     * Test for constructor with default features and codec.
     */
    @Test
    public void testGeneratorBaseDefaultConstructor() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        assertEquals(Feature.AUTO_CLOSE.getMask(), generator.getFeatureMask());
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE));
        assertNull(generator.getCodec());
    }

    /**
     * Test for constructor with specified features and codec.
     */
    @Test
    public void testGeneratorBaseCustomConstructor() {
        int customFeatures = Feature.AUTO_CLOSE.getMask() | Feature.ESCAPE_NON_ASCII.getMask();
        ObjectCodec customCodec = new ObjectMapper.DefaultObjectMapper();
        GeneratorBase generator = new GeneratorBase(customFeatures, customCodec);
        assertEquals(customFeatures, generator.getFeatureMask());
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(customCodec, generator.getCodec());
    }

    /**
     * Test for constructor with specified features, codec, and context.
     */
    @Test
    public void testGeneratorBaseCustomConstructorWithContext() {
        int customFeatures = Feature.AUTO_CLOSE.getMask() | Feature.ESCAPE_NON_ASCII.getMask();
        ObjectCodec customCodec = new ObjectMapper.DefaultObjectMapper();
        JsonWriteContext context = new JsonWriteContext.Builder().build();
        GeneratorBase generator = new GeneratorBase(customFeatures, customCodec, context);
        assertEquals(customFeatures, generator.getFeatureMask());
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertEquals(customCodec, generator.getCodec());
        assertEquals(context, generator.getOutputContext());
    }

    /**
     * Test for version method.
     */
    @Test
    public void testVersion() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        Version version = generator.version();
        assertNotNull(version);
        String expectedVersion = "com.fasterxml.jackson.core:jackson-core:2.14.0";
        assertEquals(expectedVersion, version.toString());
    }

    /**
     * Test for isEnabled method.
     */
    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE));
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_INTS));
    }

    /**
     * Test for getFeatureMask method.
     */
    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask();
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask, actualMask);
    }

    /**
     * Test for enable method with a feature that is already enabled.
     */
    @Test
    public void testEnableAlreadyEnabledFeature() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask, actualMask);
    }

    /**
     * Test for enable method with a feature that is not enabled.
     */
    @Test
    public void testEnableDisabledFeature() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask();
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), actualMask);
    }

    /**
     * Test for disable method with a feature that is already disabled.
     */
    @Test
    public void testDisableAlreadyDisabledFeature() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask() & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask() & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask, actualMask);
    }

    /**
     * Test for disable method with a feature that is enabled.
     */
    @Test
    public void testDisableEnabledFeature() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask();
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), actualMask);
    }

    /**
     * Test for setFeatureMask method.
     */
    @Test
    public void testSetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.setFeatureMask(expectedMask);
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask, actualMask);
    }

    /**
     * Test for overrideStdFeatures method.
     */
    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        int expectedMask = Feature.AUTO_CLOSE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        int actualMask = generator.getFeatureMask();
        assertEquals(expectedMask, actualMask);

        int expectedMaskDisabled = Feature.AUTO_CLOSE.getMask() & ~Feature.WRITE_NUMBERS_AS_STRINGS.getMask();
        generator.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), 0);
        int actualMaskDisabled = generator.getFeatureMask();
        assertEquals(expectedMaskDisabled, actualMaskDisabled);
    }

    /**
     * Test for useDefaultPrettyPrinter method.
     */
    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        PrettyPrinter defaultPrettyPrinter = generator.useDefaultPrettyPrinter();
        assertNotNull(defaultPrettyPrinter);
        assertTrue(defaultPrettyPrinter instanceof DefaultPrettyPrinter);
    }

    /**
     * Test for setCodec method.
     */
    @Test
    public void testSetCodec() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        ObjectCodec customCodec = new ObjectMapper.DefaultObjectMapper();
        generator.setCodec(customCodec);
        assertEquals(customCodec, generator.getCodec());
    }

    /**
     * Test for getOutputContext method.
     */
    @Test
    public void testGetOutputContext() {
        GeneratorBase generator = new GeneratorBase(Feature.AUTO_CLOSE.getMask(), new ObjectMapper.DefaultObjectMapper());
        JsonWriteContext outputContext = generator.getOutputContext();
        assertNotNull(outputContext);
    }
}