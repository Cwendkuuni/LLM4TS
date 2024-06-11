package com.fasterxml.jackson.core.json; // adjust the package if needed

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import org.junit.Test;

/**
 * JsonGeneratorImplTest
 */
public class JsonGeneratorImplTest {

    

    @Test
    public void testJsonGeneratorImplConstructor() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        assertNotNull(jsonGenerator);
    }

    @Test
    public void testEnableFeature() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator.getHighestNonEscapedChar());
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator.getHighestNonEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        CharacterEscapes characterEscapes = new SimpleCharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
        jsonGenerator.setCharacterEscapes(null);
        assertNull(jsonGenerator.getCharacterEscapes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCharacterEscapesWithNull() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.setCharacterEscapes(null);
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        SerializableString rootValueSeparator = "rootValueSeparator";
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRootValueSeparatorWithNull() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.setRootValueSeparator(null);
    }

    
}

class SimpleCharacterEscapes extends CharacterEscapes {
    @Override
    public int[] getEscapeCodesForAscii() {
        return new int[0];
    }
}