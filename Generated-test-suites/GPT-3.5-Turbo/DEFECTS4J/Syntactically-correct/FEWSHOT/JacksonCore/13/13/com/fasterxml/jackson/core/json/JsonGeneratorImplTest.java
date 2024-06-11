package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test that the IOContext object is set correctly
        assertNotNull(generator._ioContext);
        assertFalse(generator._ioContext.isResourceManaged());

        // Test that the features and codec are set correctly
        assertEquals(0, generator.getFeatureMask());
        assertNull(generator.getCodec());

        // Test that the maximumNonEscapedChar is set correctly
        assertEquals(0, generator._maximumNonEscapedChar);

        // Test that the characterEscapes object is set correctly
        assertNull(generator._characterEscapes);

        // Test that the outputEscapes array is set correctly
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes,
                generator._outputEscapes);

        // Test that the rootValueSeparator is set correctly
        assertNotNull(generator._rootValueSeparator);
    }

    @Test
    public void testEnable() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test enabling QUOTE_FIELD_NAMES feature
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);

        // Test enabling an unknown feature
        generator.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testCheckStdFeatureChanges() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test changing QUOTE_FIELD_NAMES feature
        generator._checkStdFeatureChanges(
                JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask(), 0);
        assertFalse(generator._cfgUnqNames);

        // Test changing an unknown feature
        generator._checkStdFeatureChanges(
                JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask(), 0);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test setting the highestNonEscapedChar to 0
        generator.setHighestNonEscapedChar(0);
        assertEquals(0, generator._maximumNonEscapedChar);

        // Test setting the highestNonEscapedChar to a negative value
        generator.setHighestNonEscapedChar(-1);
        assertEquals(0, generator._maximumNonEscapedChar);

        // Test setting the highestNonEscapedChar to a positive value
        generator.setHighestNonEscapedChar(255);
        assertEquals(255, generator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test getting the highestEscapedChar when it is 0
        assertEquals(0, generator.getHighestEscapedChar());

        // Test setting the highestNonEscapedChar to a positive value
        generator.setHighestNonEscapedChar(255);
        assertEquals(255, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test setting the characterEscapes to null
        generator.setCharacterEscapes(null);
        assertNull(generator._characterEscapes);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes,
                generator._outputEscapes);

        // Test setting the characterEscapes to a non-null value
        CharacterEscapes escapes = new CharacterEscapes();
        generator.setCharacterEscapes(escapes);
        assertSame(escapes, generator._characterEscapes);
        assertArrayEquals(escapes.getEscapeCodesForAscii(),
                generator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test getting the characterEscapes when it is null
        assertNull(generator.getCharacterEscapes());

        // Test setting the characterEscapes to a non-null value
        CharacterEscapes escapes = new CharacterEscapes();
        generator.setCharacterEscapes(escapes);
        assertSame(escapes, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        // Create a test JsonGeneratorImpl object
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(null,
                null, false), 0, null);

        // Test setting the rootValueSeparator to a non-null value
        SerializableString separator = new SerializableString();
        generator.setRootValueSeparator(separator);
        assertSame(separator, generator._rootValueSeparator);

        // Test setting the rootValueSeparator to null
        generator.setRootValueSeparator(null);
        assertNotNull(generator._rootValueSeparator);
    }
}