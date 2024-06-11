package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testConstructor() {
        // Test constructor with features and codec
        IOContext ctxt = new IOContext(null, null, false);
        int features = 1;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        assertEquals(ctxt, generator._ioContext);
        assertEquals(features, generator._features);
        assertEquals(codec, generator._objectCodec);
        assertFalse(generator._cfgUnqNames);

        // Test constructor without features and codec
        generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertEquals(ctxt, generator._ioContext);
        assertEquals(0, generator._features);
        assertNull(generator._objectCodec);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testEnable() {
        // Test enabling QUOTE_FIELD_NAMES feature
        IOContext ctxt = new IOContext(null, null, false);
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        assertEquals(0, generator._features);
        assertTrue(generator._cfgUnqNames);

        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertEquals(Feature.QUOTE_FIELD_NAMES.getMask(), generator._features);
        assertFalse(generator._cfgUnqNames);
        assertTrue(generator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testCheckStdFeatureChanges() {
        // Test changes in standard features
        IOContext ctxt = new IOContext(null, null, false);
        int newFeatureFlags = 0;
        int changedFeatures = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, newFeatureFlags, codec);
        assertEquals(newFeatureFlags, generator._features);
        assertTrue(generator._cfgUnqNames);

        generator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        assertEquals(0, generator._features);
        assertFalse(generator._cfgUnqNames);
        assertFalse(generator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        // Test setting highest non-escaped character
        IOContext ctxt = new IOContext(null, null, false);
        int charCode = 127;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        assertEquals(0, generator._maximumNonEscapedChar);

        generator.setHighestNonEscapedChar(charCode);
        assertEquals(charCode, generator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        // Test getting highest escaped character
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        generator._maximumNonEscapedChar = 127;

        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        // Test setting character escapes
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);

        // Test setting null character escapes
        CharacterEscapes esc = null;
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator._characterEscapes);
        assertEquals(JsonGeneratorImpl.sOutputEscapes, generator._outputEscapes);

        // Test setting non-null character escapes
        esc = new CharacterEscapes();
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator._characterEscapes);
        assertEquals(esc.getEscapeCodesForAscii(), generator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        // Test getting character escapes
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        CharacterEscapes esc = new CharacterEscapes();
        generator._characterEscapes = esc;

        assertEquals(esc, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        // Test setting root value separator
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        SerializableString sep = new SerializableString();

        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        // Test getting version
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);

        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), generator.version());
    }

    @Test
    public void testWriteStringField() throws IOException {
        // Test writing string field
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        String fieldName = "fieldName";
        String value = "fieldValue";

        generator.writeFieldName(fieldName);
        generator.writeString(value);
    }
}