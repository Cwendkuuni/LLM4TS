package com.fasterxml.jackson.core.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testVersion() {
        GeneratorBase generator = new GeneratorBaseImpl(0, null);
        assertNotNull(generator.version());
    }

    @Test
    public void testGetCurrentValue() {
        GeneratorBase generator = new GeneratorBaseImpl(0, null);
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        GeneratorBase generator = new GeneratorBaseImpl(0, null);
        generator.setCurrentValue(null);
        assertNull(generator.getCurrentValue());
    }

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBaseImpl(0, null);
        assertFalse(generator.isEnabled(null));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBaseImpl(0, null);
        assertEquals(0, generator.getFeatureMask());
    }

    // TODO: Add more test cases for other methods in GeneratorBase

    private static class GeneratorBaseImpl extends GeneratorBase {

        public GeneratorBaseImpl(int features, ObjectCodec codec) {
            super(features, codec);
        }

        @Override
        protected void _releaseBuffers() {
            // TODO Auto-generated method stub

        }

        @Override
        protected void _verifyValueWrite(String typeMsg) throws IOException {
            // TODO Auto-generated method stub

        }
    }
}