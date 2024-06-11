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
    private ObjectCodec objectCodec;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        objectCodec = new ObjectMapper.DefaultObjectCodecAlwaysCanCreateTreeTo(ioContext);
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
    }

    @Test
    public void testConstructor() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertNotNull(jsonGenerator);
    }

    @Test
    public void testEnableFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int c) {
                return Integer.toString(c);
            }
        };
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertArrayEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);

        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCharacterEscapesWithNull() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setCharacterEscapes(null);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializedString(":");
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }
}