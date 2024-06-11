package com.fasterxml.jackson.core.base; // make sure to import all necessary classes

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorBaseTest {

    

    @Test
    public void testIsEnabled() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generatorBase.isEnabled(Feature.WRITE_RAW));

        GeneratorBase generatorBase2 = new GeneratorBase(Feature.WRITE_RAW.getMask(), null);
        assertFalse(generatorBase2.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generatorBase2.isEnabled(Feature.WRITE_RAW));
    }

    @Test
    public void testEnable() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase.enable(Feature.WRITE_RAW);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_RAW));

        generatorBase.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generatorBase.disable(Feature.WRITE_RAW);
        assertFalse(generatorBase.isEnabled(Feature.WRITE_RAW));
    }

    @Test
    public void testVersion() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertEquals("2.12.0", generatorBase.version().toString());
    }

    @Test
    public void testGetCurrentValue() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        assertNull(generatorBase.getCurrentValue());

        generatorBase.setCurrentValue("test");
        assertEquals("test", generatorBase.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        GeneratorBase generatorBase = new GeneratorBase(0, null);
        generatorBase.setCurrentValue(123);
        assertEquals(123, generatorBase.getCurrentValue());

        generatorBase.setCurrentValue("test");
        assertEquals("test", generatorBase.getCurrentValue());
    }

    // Add test cases for other methods in the class...

    
}