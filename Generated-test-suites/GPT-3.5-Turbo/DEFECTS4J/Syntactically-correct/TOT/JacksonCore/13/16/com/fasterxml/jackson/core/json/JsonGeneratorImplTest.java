
package com.fasterxml.jackson.core.json;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.writeStringField("fieldName", "value");

        // Assert
        // Check that the field name is written correctly
        assertTrue(jsonGenerator.getOutputContext().inObject());
        assertEquals("fieldName", jsonGenerator.getOutputContext().currentName());

        // Check that the value is written correctly
        assertEquals("\"value\"", jsonGenerator.getOutputContext().getCurrentValue());
    }

    @Test
    public void testEnable_QUOTE_FIELD_NAMES() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);

        // Assert
        assertTrue(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertFalse(jsonGenerator.isCfgUnqNames());
    }

    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(256);

        // Assert
        assertEquals(256, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[0];
            }

            @Override
            public SerializableString getEscapeSequence(int ch) {
                return null;
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);

        // Assert
        assertNotNull(jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setRootValueSeparator(new SerializableString() {
            @Override
            public String getValue() {
                return ",";
            }

            @Override
            public int charLength() {
                return 1;
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
            public byte[] asQuotedUTF8() {
                return new byte[0];
            }
        });

        // Assert
        assertEquals(",", jsonGenerator.getRootValueSeparator().getValue());
    }

}
