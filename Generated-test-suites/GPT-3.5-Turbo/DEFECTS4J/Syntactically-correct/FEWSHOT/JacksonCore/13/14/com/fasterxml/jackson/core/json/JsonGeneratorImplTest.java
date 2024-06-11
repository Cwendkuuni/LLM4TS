package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.core.JsonGenerator;

public class JsonGeneratorImplTest {
    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
        generator.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void test_disable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), null);
        generator.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        generator.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testCheckStdFeatureChanges() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator._checkStdFeatureChanges(0, JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getMask());
        assertFalse(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
        generator._checkStdFeatureChanges(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT.getMask());
        assertTrue(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setHighestNonEscapedChar(127);
        assertEquals(127, generator.getHighestEscapedChar());
        generator.setHighestNonEscapedChar(256);
        assertEquals(256, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes_getCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertNull(generator.getCharacterEscapes());

        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());
        
        generator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[0];
            }
            @Override
            public SerializableString getEscapeSequence(int ch) {
                return null;
            }
        });
        assertEquals(new int[0], generator.getCharacterEscapes().getEscapeCodesForAscii());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);

        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);

        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);
    }
}