package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for {@link JsonGeneratorImpl}
 */
public class JsonGeneratorImplTest {

    private IOContext ctx;
    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        ctx = new IOContext(null);
        generator = new JsonGeneratorImpl(ctx, 0, null);
    }

    @Test
    public void testDefaultConstruction() {
        assertNotNull(generator);
        assertTrue(generator instanceof JsonGeneratorImpl);
        assertEquals(0, generator.features());
        assertEquals(null, generator.getCodec());
        assertEquals(sOutputEscapes, generator._outputEscapes);
        assertEquals(127, generator.getHighestEscapedChar());
        assertNull(generator.getCharacterEscapes());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testConstructionWithFeatures() {
        int features = Feature.QUOTE_FIELD_NAMES.mask() | Feature.ESCAPE_NON_ASCII.mask();
        ObjectCodec codec = new ObjectMapper().getFactory().getCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctx, features, codec);
        assertEquals(features, jsonGenerator.features());
        assertEquals(codec, jsonGenerator.getCodec());
        assertTrue(Feature.ESCAPE_NON_ASCII.enabledIn(features));
        assertFalse(Feature.QUOTE_FIELD_NAMES.enabledIn(features));
    }

    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        generator.setHighestNonEscapedChar(123);
        assertEquals(123, generator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharNegative() throws IOException {
        generator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testSetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes(){
            @Override
            public String getEscapeSequence(int c) {
                return String.format("\\u%04x", c);
            }
        };
        generator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, generator.getCharacterEscapes());
        assertNotEquals(sOutputEscapes, generator._outputEscapes);
    }

    @Test
    public void testSetCharacterEscapesNull() throws IOException {
        generator.setCharacterEscapes(null);
        assertEquals(sOutputEscapes, generator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        SerializableString rootValueSeparator = new SerializableString("root");
        generator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, generator._rootValueSeparator);
    }

    @Test
    public void testEnableQuoteFieldNames() throws IOException {
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(generator._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() throws IOException {
        generator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testEnableEscapeNonAscii() throws IOException {
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testDisableEscapeNonAscii() throws IOException {
        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertEquals(0, generator.getHighestEscapedChar());
    }

    @Test
    public void testVersion() throws IOException {
        Version version = generator.version();
        assertNotNull(version);
        assertTrue(version.major() > 0);
        assertTrue(version.minor() > 0);
        assertTrue(version.subversion() > 0);
    }
}