
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setup() {
        jsonGenerator = new JsonGeneratorImpl(null, 0, null) {};
    }

    @Test
    public void testEnableFeature() {
        assertFalse(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void test_setHighestNonEscapedChar_withPositiveChar() {
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void test_setHighestNonEscapedChar_withNegativeChar() {
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void test_setCharacterEscapes_withNullEscapes() {
        jsonGenerator.setCharacterEscapes(null);
        assertNull(jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void test_setCharacterEscapes_withNonEmptyEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 1, 2, 3 };
            }

            @Override
            public SerializableString getEscapeSequence(int ch) {
                return null;
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        assertNull(jsonGenerator._rootValueSeparator);
        jsonGenerator.setRootValueSeparator(null);
        assertNull(jsonGenerator._rootValueSeparator);
    }
}
