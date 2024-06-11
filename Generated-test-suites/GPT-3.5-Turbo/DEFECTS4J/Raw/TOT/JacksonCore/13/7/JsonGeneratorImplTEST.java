
package com.fasterxml.jackson.core.json;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        // Create a StringBuilder to store the JSON output
        StringBuilder json = new StringBuilder();

        // Create a mock IOContext and set the features
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec) {
            // Override the writeFieldName method to add the field name to the JSON output
            @Override
            public void writeFieldName(String name) throws IOException {
                json.append("\"" + name + "\": ");
            }

            // Override the writeString method to add the string value to the JSON output
            @Override
            public void writeString(String text) throws IOException {
                json.append("\"" + text + "\"");
            }
        };

        // Write a string field
        generator.writeStringField("name", "John");

        // Validate the JSON output
        assertEquals("\"name\": \"John\"", json.toString());
    }

    @Test
    public void testEnable() {
        // Create a mock IOContext and set the features
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);

        // Enable the QUOTE_FIELD_NAMES feature
        generator.enable(Feature.QUOTE_FIELD_NAMES);

        // Validate that the _cfgUnqNames field is set to false
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        // Create a mock IOContext and set the features
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);

        // Set the highest non-escaped character to 255
        generator.setHighestNonEscapedChar(255);

        // Validate that the _maximumNonEscapedChar field is set to 255
        assertEquals(255, generator._maximumNonEscapedChar);
    }

    @Test
    public void testSetCharacterEscapes() {
        // Create a mock IOContext and set the features
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);

        // Create a mock CharacterEscapes instance
        CharacterEscapes esc = new CharacterEscapes();

        // Set the custom character escapes
        generator.setCharacterEscapes(esc);

        // Validate that the _characterEscapes and _outputEscapes fields are updated accordingly
        assertEquals(esc, generator._characterEscapes);
        assertArrayEquals(esc.getEscapeCodesForAscii(), generator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        // Create a mock IOContext and set the features
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);

        // Create a mock SerializableString instance
        SerializableString sep = new SerializableString();

        // Set the root value separator
        generator.setRootValueSeparator(sep);

        // Validate that the _rootValueSeparator field is updated accordingly
        assertEquals(sep, generator._rootValueSeparator);
    }

}
