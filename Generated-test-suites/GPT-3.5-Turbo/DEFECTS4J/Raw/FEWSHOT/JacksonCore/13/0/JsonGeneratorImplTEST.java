package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testConstructor() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(null, generator._ioContext);
        assertEquals(0, generator.getFeatureMask());
        assertEquals(null, generator.getCodec());
        assertEquals(JsonGeneratorImpl.sOutputEscapes, generator._outputEscapes);
        assertEquals(127, generator._maximumNonEscapedChar);
        assertNull(generator._characterEscapes);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);
        assertEquals(false, generator._cfgUnqNames);
    }

    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertEquals(false, generator._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator._checkStdFeatureChanges(0, 0);
        assertEquals(false, generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setHighestNonEscapedChar(150);
        assertEquals(150, generator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes escapes = new CharacterEscapes();
        generator.setCharacterEscapes(escapes);
        assertEquals(escapes, generator._characterEscapes);
        assertEquals(escapes.getEscapeCodesForAscii(), generator._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertNull(generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        SerializableString sep = new SerializableString();
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), generator.version());
    }

    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.writeStringField("name", "value");
        assertEquals("name", generator.currentName());
        assertEquals("value", generator.currentValue());
    }
}