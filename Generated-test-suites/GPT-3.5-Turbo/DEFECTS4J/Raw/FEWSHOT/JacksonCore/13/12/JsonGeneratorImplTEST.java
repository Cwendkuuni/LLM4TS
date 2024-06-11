package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonGeneratorImplTest {
    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        IOContext ioContext = new IOContext();
        int features = 0;
        ObjectCodec codec = null;
        generator = new JsonGeneratorImpl(ioContext, features, codec);
    }

    @Test
    public void testEnable() {
        generator.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT));
    }

    @Test
    public void testCheckStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        generator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        int charCode = 127;
        generator.setHighestNonEscapedChar(charCode);
        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = null;
        generator.setCharacterEscapes(esc);
        assertNull(generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializedString sep = new SerializedString(",");
        generator.setRootValueSeparator(sep);
        assertEquals(",", generator._rootValueSeparator.getValue());
    }

    @Test
    public void testVersion() {
        assertNotNull(generator.version());
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "name";
        String value = "John";
        generator.writeStringField(fieldName, value);
        assertEquals("name", generator.getCurrentName());
        assertEquals("John", generator.getText());
    }
}