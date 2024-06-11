package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertSame(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);

        jsonGenerator.setCharacterEscapes(null);
        assertArrayEquals(sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        SerializableString rootValueSeparator = new SerializableString("_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testEnableQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);

        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testFeatureChanges() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        int newFeatureFlags = Feature.QUOTE_FIELD_NAMES.enabledInt();
        jsonGenerator._checkStdFeatureChanges(newFeatureFlags, Feature.QUOTE_FIELD_NAMES.mask());
        assertFalse(jsonGenerator._cfgUnqNames);
    }
}