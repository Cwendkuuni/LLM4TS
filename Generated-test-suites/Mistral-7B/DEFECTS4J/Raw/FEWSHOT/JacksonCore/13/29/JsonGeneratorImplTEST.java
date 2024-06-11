package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        // Default initialization
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertNull(jsonGenerator._characterEscapes);
        assertNull(jsonGenerator._rootValueSeparator);
        assertFalse(jsonGenerator._cfgUnqNames);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);

        // Set features
        int features = Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.QUOTE_FIELD_NAMES.getMask();
        JsonGeneratorImpl jsonGeneratorWithFeatures = new JsonGeneratorImpl(null, features, null);
        assertTrue(jsonGeneratorWithFeatures.isEnabled(Feature.QUOTE_FIELD_NAMES));
        assertFalse(jsonGeneratorWithFeatures._cfgUnqNames);
        assertEquals(127, jsonGeneratorWithFeatures._maximumNonEscapedChar);

        // Set character escapes
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{123, 456};
            }
        };
        JsonGeneratorImpl jsonGeneratorWithCustomEscapes = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorWithCustomEscapes.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[]{123, 456}, jsonGeneratorWithCustomEscapes._outputEscapes);

        // Set root value separator
        SerializableString rootValueSeparator = new SerializableString(":");
        JsonGeneratorImpl jsonGeneratorWithRootValueSeparator = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorWithRootValueSeparator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorWithRootValueSeparator._rootValueSeparator);

        // Set highest non-escaped char
        JsonGeneratorImpl jsonGeneratorWithHighestNonEscapedChar = new JsonGeneratorImpl(null, 0, null);
        jsonGeneratorWithHighestNonEscapedChar.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGeneratorWithHighestNonEscapedChar._maximumNonEscapedChar);
    }

    @Test
    public void testSetFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
        assertFalse(jsonGenerator._cfgUnqNames);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));

        JsonGeneratorImpl jsonGeneratorWithFeatures = new JsonGeneratorImpl(null, Feature.QUOTE_FIELD_NAMES.getMask() | Feature.AUTO_CLOSE_SOURCE.getMask(), null);
        assertTrue(jsonGeneratorWithFeatures.isEnabled(Feature.QUOTE_FIELD_NAMES));
        assertTrue(jsonGeneratorWithFeatures.isEnabled(Feature.AUTO_CLOSE_SOURCE));

        JsonGeneratorImpl jsonGeneratorWithDisabledFeatures = new JsonGeneratorImpl(null, Feature.QUOTE_FIELD_NAMES.getMask() & ~Feature.AUTO_CLOSE_SOURCE.getMask(), null);
        assertFalse(jsonGeneratorWithDisabledFeatures.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertTrue(jsonGeneratorWithDisabledFeatures.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(127, jsonGenerator.getHighestNonEscapedChar());
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator.getHighestNonEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{123, 456};
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[]{123, 456}, jsonGenerator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes characterEscapes = jsonGenerator.getCharacterEscapes();
        assertNull(characterEscapes);
        CharacterEscapes customCharacterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{123, 456};
            }
        };
        jsonGenerator.setCharacterEscapes(customCharacterEscapes);
        assertEquals(customCharacterEscapes, jsonGenerator.getCharacterEscapes());
    }
}