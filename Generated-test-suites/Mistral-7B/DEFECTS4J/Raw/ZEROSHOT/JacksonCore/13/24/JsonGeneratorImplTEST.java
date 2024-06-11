package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
    }

    @Test
    public void testDefaultConfiguration() {
        // Test default configuration
        assertEquals(JsonGenerator.Feature.CAN_OVERWRITE_ARRAY_ITEMS, jsonGenerator.isEnabled(JsonGenerator.Feature.CAN_OVERWRITE_ARRAY_ITEMS));
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII, jsonGenerator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES, jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
        assertNull(jsonGenerator.getCharacterEscapes());
        assertNull(jsonGenerator.getRootValueSeparator());
    }

    @Test
    public void testFeatureToggle() {
        jsonGenerator = new JsonGeneratorImpl(ioContext, JsonGenerator.Feature.QUOTE_FIELD_NAMES.enableMask(), null);

        // Test feature is enabled
        assertFalse(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));

        jsonGenerator = jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);

        // Test feature is now disabled
        assertTrue(jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator = new JsonGeneratorImpl(ioContext, JsonGenerator.Feature.ESCAPE_NON_ASCII.enableMask(), null);

        // Test default highest escapes character
        assertEquals(127, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = jsonGenerator.setHighestNonEscapedChar(128);

        // Test highest escapes character is set to 128
        assertEquals(128, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = jsonGenerator.setHighestNonEscapedChar(0);

        // Test highest escapes character is set to 0 (all characters can be represented without escaping)
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes escapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { '\u0001' };
            }
        };

        jsonGenerator = new JsonGeneratorImpl(ioContext, JsonGenerator.Feature.ESCAPE_NON_ASCII.enableMask(), null);

        // Test default character escapes
        assertNull(jsonGenerator.getCharacterEscapes());

        jsonGenerator = jsonGenerator.setCharacterEscapes(escapes);

        // Test custom character escapes are set
        assertEquals(escapes, jsonGenerator.getCharacterEscapes());

        // Test custom escapes are used
        int[] outputEscapesBefore = jsonGenerator._outputEscapes;
        jsonGenerator.writeString("\\u0001");
        int[] outputEscapesAfter = jsonGenerator._outputEscapes;
        assertArrayEquals(new int[] { '\u0001' }, outputEscapesBefore);
        assertArrayEquals(new int[] { '\u0001', 0x7F }, outputEscapesAfter);

        jsonGenerator = jsonGenerator.setCharacterEscapes(null);

        // Test standard character escapes are used again
        assertNull(jsonGenerator.getCharacterEscapes());
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("test_separator");

        jsonGenerator = new JsonGeneratorImpl(ioContext, JsonGenerator.Feature.QUOTE_FIELD_NAMES.enableMask(), null);

        // Test default root value separator
        assertNull(jsonGenerator.getRootValueSeparator());

        jsonGenerator = jsonGenerator.setRootValueSeparator(rootValueSeparator);

        // Test root value separator is set
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }
}