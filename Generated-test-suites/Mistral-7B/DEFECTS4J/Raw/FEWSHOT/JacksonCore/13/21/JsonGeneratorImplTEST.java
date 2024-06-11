package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test cases for abstract class JsonGeneratorImpl
 */
public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testFeatureConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.ESCAPE_NON_ASCII.asInt(), null);
        assertTrue(jsonGenerator._cfgUnqNames);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes customEscapes = new CustomCharacterEscapes();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setCharacterEscapes(customEscapes);
        assertEquals(customEscapes, jsonGenerator.getCharacterEscapes());
        assertNotEquals(sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString(":");
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testSetUnqNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setQuoteFieldNames(false);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setQuoteFieldNames(true);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetHighestNonEscapedCharNegative() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testSetRootValueSeparatorNull() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setRootValueSeparator(null);
    }
}

class CustomCharacterEscapes extends CharacterEscapes {
    @Override
    public int[] getEscapeCodesForAscii() {
        int[] outputEscapes = new int[sOutputEscapes.length];
        for (int i = 0; i < sOutputEscapes.length; i++) {
            outputEscapes[i] = i + 100; // Custom escape codes
        }
        return outputEscapes;
    }
}