package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws Exception {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.writeStringField("fieldName", "value");

        // Verify that the field name was written correctly
        assertEquals("fieldName", generator.getCurrentName());

        // Verify that the string value was written correctly
        assertEquals("value", generator.getText());
    }

    @Test
    public void testEnableQuoteFieldNamesFeature() throws Exception {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), null);
        generator.writeFieldName("fieldName");

        // Verify that the field name was written with quotes
        assertEquals("\"fieldName\"", generator.getText());
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setHighestNonEscapedChar(128);
        
        // Verify that the maximum non-escaped character was set correctly
        assertEquals(128, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        CharacterEscapes customEscapes = new CustomCharacterEscapes();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setCharacterEscapes(customEscapes);
        
        // Verify that the character escapes were set correctly
        assertEquals(customEscapes, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        SerializableString separator = new CustomSerializableString();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setRootValueSeparator(separator);
        
        // Verify that the root value separator was set correctly
        assertEquals(separator, generator.getRootValueSeparator());
    }

    private static class CustomCharacterEscapes extends CharacterEscapes {
        @Override
        public int[] getEscapeCodesForAscii() {
            return new int[]{1, 2, 3, 4};
        }

        @Override
        public SerializableString getEscapeSequence(int ch) {
            return null;
        }
    }

    private static class CustomSerializableString implements SerializableString {
        @Override
        public int charLength() {
            return 0;
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
    }
}