package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testGetFeatureMask() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertEquals(0, generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), generator.getFeatureMask());

        generator = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), null);
        assertEquals(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), generator.getFeatureMask());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        // Test null value
        generator.writeObject(null);

        Object obj = new Object();
        // Test with default ObjectCodec
        generator = new GeneratorBase(0, null);
        generator.writeObject(obj);

        // Test with custom ObjectCodec
        ObjectCodec objectCodec = new ObjectCodec() {
            @Override
            public void writeValue(JsonGenerator gen, Object value) throws IOException {
                gen.writeRawValue("Custom ObjectCodec");
            }
        };
        generator = new GeneratorBase(0, objectCodec);
        generator.writeObject(obj);
        assertEquals("Custom ObjectCodec", generator.getCurrentValue());
        generator.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isClosed());

        generator.close();
        assertTrue(generator.isClosed());
    }
}