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
 * Test class for {@link JsonGeneratorImpl}
 */
public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setup() {
        ioContext = new IOContext(null, new byte[0], null, null, null, null, null, null, null, null, null);
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, new SimpleObjectCodec()) {
            @Override
            protected void _checkStdFeatureChanges(int newFeatureFlags, int changedFeatures) {
                super._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
            }
        };
    }

    @Test
    public void testConstructor() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, new SimpleObjectCodec());
        assertNotNull(jsonGenerator);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(256);
        assertEquals(256, jsonGenerator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharNegativeValue() {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{-1, -1, -1};
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test(expected = NullPointerException.class)
    public void testSetCharacterEscapesNull() {
        jsonGenerator.setCharacterEscapes(null);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("root");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }

    @Test
    public void testVersion() {
        assertEquals(VersionUtil.Version.UNKNOWN, jsonGenerator.version());
    }
}