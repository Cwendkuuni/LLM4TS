
package com.fasterxml.jackson.core.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setUp() throws Exception {
        generator = new JsonGeneratorImpl(null, 0, null) {
            @Override
            public void writeFieldName(String name) throws IOException {

            }

            @Override
            public void writeString(String text) throws IOException {

            }
        };
    }

    @After
    public void tearDown() throws Exception {
        generator = null;
    }

    @Test
    public void enable() {
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);

        generator.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertEquals(127, generator._maximumNonEscapedChar);
    }

    @Test
    public void _checkStdFeatureChanges() {
        generator._checkStdFeatureChanges(0, 0);
        assertFalse(generator._cfgUnqNames);

        generator._checkStdFeatureChanges(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), 0);
        assertTrue(generator._cfgUnqNames);

        generator._checkStdFeatureChanges(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(),
                JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        assertTrue(generator._cfgUnqNames);
    }

    @Test
    public void setHighestNonEscapedChar() {
        generator.setHighestNonEscapedChar(200);
        assertEquals(200, generator._maximumNonEscapedChar);

        generator.setHighestNonEscapedChar(-100);
        assertEquals(0, generator._maximumNonEscapedChar);
    }

    @Test
    public void getHighestEscapedChar() {
        generator.setHighestNonEscapedChar(200);
        assertEquals(200, generator.getHighestEscapedChar());
    }

    @Test
    public void setCharacterEscapes() {
        generator.setCharacterEscapes(null);
        assertArrayEquals(generator.sOutputEscapes, generator._outputEscapes);

        CharacterEscapes customCharacterEscapes = new CharacterEscapes();
        generator.setCharacterEscapes(customCharacterEscapes);
        assertArrayEquals(customCharacterEscapes.getEscapeCodesForAscii(), generator._outputEscapes);
    }

    @Test
    public void getCharacterEscapes() {
        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());

        CharacterEscapes customCharacterEscapes = new CharacterEscapes();
        generator.setCharacterEscapes(customCharacterEscapes);
        assertEquals(customCharacterEscapes, generator.getCharacterEscapes());
    }

    @Test
    public void setRootValueSeparator() {
        SerializableString sep = null;
        generator.setRootValueSeparator(sep);
        assertNotNull(generator._rootValueSeparator);

        sep = new com.fasterxml.jackson.core.util.SerializableString() {
            @Override
            public String getValue() {
                return null;
            }

            @Override
            public char[] asQuotedChars() {
                return new char[0];
            }

            @Override
            public byte[] asUnquotedUTF8() {
                return new byte[0];
            }

            @Override
            public int appendQuotedUTF8(byte[] outputBuffer, int outputOffset) {
                return 0;
            }

            @Override
            public int appendQuoted(char[] outputBuffer, int outputOffset) {
                return 0;
            }
        };
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void version() {
        assertNotNull(generator.version());
    }

    @Test
    public void writeStringField() {
        assertThrows(IOException.class, () -> generator.writeStringField("fieldName", "value"));
    }
}
