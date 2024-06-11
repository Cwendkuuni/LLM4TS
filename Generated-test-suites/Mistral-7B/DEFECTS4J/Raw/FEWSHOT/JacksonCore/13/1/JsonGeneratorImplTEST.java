package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConfiguration() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNull(jsonGenerator._characterEscapes);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
        assertNull(jsonGenerator._rootValueSeparator);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testQuoteFieldNamesFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, Feature.QUOTE_FIELD_NAMES.mask(), null);
        assertTrue(jsonGenerator._cfgUnqNames);

        jsonGenerator = new JsonGeneratorImpl(null, ~Feature.QUOTE_FIELD_NAMES.mask(), null);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testEscapeNonAsciiFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, Feature.ESCAPE_NON_ASCII.mask(), null);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);

        jsonGenerator = new JsonGeneratorImpl(null, ~Feature.ESCAPE_NON_ASCII.mask(), null);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { '\u0001' };
            }
        };

        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(new int[] { '\u0001' }, jsonGenerator._outputEscapes);
    }

    @Test
    public void testRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        SerializableString rootValueSeparator = new BasicSerializableString("separator");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }
}