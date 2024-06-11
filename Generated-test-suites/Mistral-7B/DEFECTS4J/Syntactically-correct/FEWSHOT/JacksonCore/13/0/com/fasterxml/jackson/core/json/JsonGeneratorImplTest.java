package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testConfigConstruction() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGenerator._ioContext);
        assertNull(jsonGenerator._characterEscapes);
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
        assertNull(jsonGenerator._rootValueSeparator);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testFeatureFlagConstruction() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.ESCAPE_NON_ASCII.asInt(), null);
        assertTrue(Feature.ESCAPE_NON_ASCII.enabledIn(jsonGenerator.getFeatures()));
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testCharacterEscapesConstruction() {
        IOContext ioContext = new IOContext();
        CharacterEscapes characterEscapes = CharacterEscapes.StableWhitespace.instance();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
    }

    @Test
    public void testRootValueSeparatorConstruction() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setRootValueSeparator(new SerializableString("root"));
        assertEquals(new SerializableString("root"), jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testQuoteFieldNamesConstruction() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator1 = new JsonGeneratorImpl(ioContext, 0, null);
        assertFalse(jsonGenerator1._cfgUnqNames);

        JsonGeneratorImpl jsonGenerator2 = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.asInt(), null);
        assertTrue(jsonGenerator2._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator._maximumNonEscapedChar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        Version version = jsonGenerator.version();
        assertNotNull(version);
        assertEquals("2.11.2", version.toString());
    }
}