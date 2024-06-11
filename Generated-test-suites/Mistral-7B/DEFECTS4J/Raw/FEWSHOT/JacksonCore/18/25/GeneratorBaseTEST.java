package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generatorBase.isEnabled(Feature.WRITE_BOOLEAN_VALUE_AS_true));

        GeneratorBase generatorBaseWithWriteBoolean = new GeneratorBase(Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask(), null);
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_BOOLEAN_VALUE_AS_true));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertEquals(0, generatorBase.getFeatureMask());

        GeneratorBase generatorBaseWithWriteBoolean = new GeneratorBase(Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask(), null);
        assertEquals(Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask(), generatorBaseWithWriteBoolean.getFeatureMask());
    }

    @Test
    public void testEnable() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generatorBaseWithWriteBoolean = new GeneratorBase(0, null);
        generatorBaseWithWriteBoolean.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        generatorBaseWithWriteBoolean.enable(Feature.WRITE_NUMBERS_AS_STRINGS); // should not change state
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generatorBaseWithWriteBooleanAndNumbersAsStrings = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask(), null);
        assertTrue(generatorBaseWithWriteBooleanAndNumbersAsStrings.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorBaseWithWriteBooleanAndNumbersAsStrings.isEnabled(Feature.WRITE_BOOLEAN_VALUE_AS_true));
    }

    @Test
    public void testDisable() {
        GeneratorBase generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generatorBaseWithWriteBoolean = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask(), null);
        generatorBaseWithWriteBoolean.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_BOOLEAN_VALUE_AS_true));
        assertFalse(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testSetFeatureMask() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generatorBaseWithWriteBoolean = new GeneratorBase(0, null);
        generatorBaseWithWriteBoolean.setFeatureMask(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask());
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_BOOLEAN_VALUE_AS_true));
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        GeneratorBase generatorBaseWithWriteBoolean = new GeneratorBase(0, null);
        generatorBaseWithWriteBoolean.overrideStdFeatures(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), Feature.WRITE_BOOLEAN_VALUE_AS_true.getMask());
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorBaseWithWriteBoolean.isEnabled(Feature.WRITE_BOOLEAN_VALUE_AS_true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedUseDefaultPrettyPrinter() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.useDefaultPrettyPrinter();
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        PrettyPrinter defaultPrettyPrinter = generatorBase.useDefaultPrettyPrinter();
        assertNotNull(defaultPrettyPrinter);
    }

    @Test
    public void testSetCodec() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        generatorBase.setCodec(objectCodec);
        assertEquals(objectCodec, generatorBase.getCodec());
    }
}