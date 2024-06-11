package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImpl() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertEquals(getDefaultPrettyPrinter(), jsonGeneratorImpl._rootValueSeparator);
        assertEquals(false, jsonGeneratorImpl._cfgUnqNames);
        assertNull(jsonGeneratorImpl._characterEscapes);
        assertEquals(CharTypes.get7BitOutputEscapes(), jsonGeneratorImpl._outputEscapes);
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ioContext = new IOContext();
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{-1, -2};
            }
        };
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        JsonGeneratorImpl jsonGeneratorImplWithCustomEscapes = jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGeneratorImplWithCustomEscapes.getCharacterEscapes());
        assertArrayEquals(new int[]{-1, -2}, jsonGeneratorImplWithCustomEscapes._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ioContext = new IOContext();
        SerializableString rootValueSeparator = new SerializableString("root");
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        JsonGeneratorImpl jsonGeneratorImplWithCustomSeparator = jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImplWithCustomSeparator._rootValueSeparator);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        JsonGeneratorImpl jsonGeneratorImplWithCustomNonEscapedChar = jsonGeneratorImpl.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGeneratorImplWithCustomNonEscapedChar.getHighestEscapedChar());
    }

    @Test
    public void testEnableQuoteFieldNames() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        JsonGeneratorImpl jsonGeneratorImplWithQuoteFieldNames = jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImplWithQuoteFieldNames._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        JsonGeneratorImpl jsonGeneratorImplWithUnquoteFieldNames = jsonGeneratorImpl.enable(Feature.UNQUOTED_FIELD_NAMES);
        assertFalse(jsonGeneratorImplWithUnquoteFieldNames._cfgUnqNames);
    }

    private static SerializableString getDefaultPrettyPrinter() {
        return DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
    }

    private static void assertArrayEquals(int[] expected, int[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}