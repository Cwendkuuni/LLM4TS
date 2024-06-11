package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertEquals(ctxt, generator._ioContext);
        assertNull(generator._characterEscapes);
        assertNull(generator._rootValueSeparator);
        assertEquals(127, generator._maximumNonEscapedChar);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator._checkStdFeatureChanges(0, Feature.QUOTE_FIELD_NAMES.getMask());
        assertFalse(generator._cfgUnqNames);
        generator._checkStdFeatureChanges(Feature.QUOTE_FIELD_NAMES.getMask(), 0);
        assertTrue(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator.getHighestEscapedChar());

        generator.setHighestNonEscapedChar(-1);
        assertEquals(0, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());

        CharacterEscapes esc = new CharacterEscapes();
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);

        SerializableString sep = new SerializableString();
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), generator.version());
    }

    @Test
    public void testWriteStringField() {
        IOContext ctxt = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.writeStringField("fieldName", "fieldValue");
        // Verify the fields are written correctly by directly accessing the private methods

        // writeFieldName() is called by writeStringField()
        assertEquals("fieldName", generator._lastWriteFieldName);
        // writeString() is called by writeStringField()
        assertEquals("fieldValue", generator._lastWriteString);
    }
}