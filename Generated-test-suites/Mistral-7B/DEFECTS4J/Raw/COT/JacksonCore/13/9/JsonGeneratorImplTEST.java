package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test class for JsonGeneratorImpl
 */
public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
    }

    // Test for constructor
    @Test
    public void testJsonGeneratorImplConstructor() {
        assertNotNull(jsonGeneratorImpl);
    }

    // Test for enable method
    @Test
    public void testEnableFeature() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    // Test for setHighestNonEscapedChar method
    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());
    }

    // Test for setCharacterEscapes method
    @Test
    public void testSetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 0x10 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(new int[] { 0x10 }, jsonGeneratorImpl._outputEscapes);
    }

    // Test for getCharacterEscapes method
    @Test
    public void testGetCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = jsonGeneratorImpl.getCharacterEscapes();
        assertNull(characterEscapes);
    }

    // Test for setRootValueSeparator method
    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("rootValueSeparator");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertSame(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);
    }

    // Test for version method
    @Test
    public void testVersion() {
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGeneratorImpl.version());
    }

    // Test for writeFieldName method with quoted field name
    @Test
    public void testWriteFieldNameQuoted() throws IOException {
        jsonGeneratorImpl.writeFieldName("\"fieldName\"");
        String expected = "\"fieldName\":";
        assertEquals(expected, jsonGeneratorImpl.getOutputBuffer().toString());
    }

    // Test for writeFieldName method with unquoted field name
    @Test
    public void testWriteFieldNameUnquoted() throws IOException {
        jsonGeneratorImpl._cfgUnqNames = true;
        jsonGeneratorImpl.writeFieldName("fieldName");
        String expected = "fieldName:";
        assertEquals(expected, jsonGeneratorImpl.getOutputBuffer().toString());
    }

    // Test for writeString method with ASCII characters
    @Test
    public void testWriteStringAscii() throws IOException {
        jsonGeneratorImpl.writeString("abc");
        String expected = "\"abc\":";
        assertEquals(expected, jsonGeneratorImpl.getOutputBuffer().toString());
    }

    // Test for writeString method with non-ASCII characters
    @Test(expected = IOException.class)
    public void testWriteStringNonAscii() throws IOException {
        int maximumNonEscapedChar = 128;
        jsonGeneratorImpl.setHighestNonEscapedChar(maximumNonEscapedChar);
        jsonGeneratorImpl.writeString("\u00E9");
    }

    // Test for writeString method with custom character escapes
    @Test
    public void testWriteStringCustomCharacterEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 0x10, 0x11 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        jsonGeneratorImpl.writeString("abc\u00E9");
        String expected = "\"abc\\u0010\\u0011\":";
        assertEquals(expected, jsonGeneratorImpl.getOutputBuffer().toString());
    }
}