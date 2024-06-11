package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {
    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        IOContext ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
    }

    @Test
    public void testDefaultConfiguration() {
        assertNotNull(jsonGenerator.getVersion());
        assertNull(jsonGenerator.getCharacterEscapes());
        assertNotNull(jsonGenerator._outputEscapes);
        assertNull(jsonGenerator._characterEscapes);
        assertNull(jsonGenerator._rootValueSeparator);
        assertFalse(jsonGenerator._cfgUnqNames);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{1, 2, 3};
            }
        };

        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertNotNull(jsonGenerator.getCharacterEscapes());
        assertNotSame(sOutputEscapes, jsonGenerator._outputEscapes);
        assertArrayEquals(new int[]{1, 2, 3}, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testEnableQuoteFieldNames() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }
}