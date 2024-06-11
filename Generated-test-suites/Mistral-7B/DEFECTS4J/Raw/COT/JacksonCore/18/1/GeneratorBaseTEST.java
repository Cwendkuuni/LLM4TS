package com.fasterxml.jackson.core.base; // import statements go here

import org.junit.Test;
import static org.junit.Assert.*;

public class GeneratorBaseTest {

    // Basic test cases for public methods

    @Test
    public void testIsEnabled_Feature() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        GeneratorBase generatorBase1 = new GeneratorBase(0, null, null);
        assertFalse(generatorBase1.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertEquals(0, generatorBase.getFeatureMask());

        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generatorBase.getFeatureMask());
    }

    @Test
    public void testVersion() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNotNull(generatorBase.version());
    }

    @Test
    public void testGetCurrentValue() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getCurrentValue());

        generatorBase.setCurrentValue(new Object());
        assertEquals(new Object(), generatorBase.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setCurrentValue(new Object());
        assertEquals(new Object(), generatorBase.getCurrentValue());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testSetFeatureMask_NegativeMask() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setFeatureMask(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFeatureMask_MaskBiggerThanAvailableFeatures() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_NULL_AS_NULL.getMask() * 2);
    }

    @Test
    public void testEnable_DisabledFeature() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test(expected = IllegalStateException.class)
    public void testEnable_FeatureWithDerivedStateChanges_AlreadySet() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    @Test(expected = IllegalStateException.class)
    public void testDisable_FeatureWithDerivedStateChanges_NotSet() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getPrettyPrinter());
        generatorBase.useDefaultPrettyPrinter();
        assertNotNull(generatorBase.getPrettyPrinter());
    }

    @Test(expected = NullPointerException.class)
    public void testSetCodec_NullCodec() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setCodec(null);
    }

    @Test
    public void testWriteObject_NullValue() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.writeObject(null);
        assertTrue(generatorBase.isClosed());
    }
}