package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test cases for {@link JsonGeneratorImpl} class
 */
public class JsonGeneratorImplTest {

    private JsonGenerator _generator;
    private JsonFactory _factory;
    private IOContext _ioContext;
    private CharacterEscapes _characterEscapes;

    @Before
    public void setUp() throws IOException {
        _factory = new JsonFactory();
        _ioContext = _factory.getCreationContext();
        _generator = _factory.createGenerator(_ioContext);
        _characterEscapes = _generator.getCharacterEscapes();
    }

    // Test for constructor
    @Test
    public void testJsonGeneratorImplConstructor() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(_ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    // Test for setHighestNonEscapedChar() method
    @Test
    public void testSetHighestNonEscapedChar() {
        _generator.setHighestNonEscapedChar(128);
        int highestNonEscapedChar = _generator.getHighestNonEscapedChar();
        assertTrue(highestNonEscapedChar >= 128);
    }

    // Test for setCharacterEscapes() method
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 255 };
            }
        };
        _generator.setCharacterEscapes(customEscapes);
        int[] outputEscapes = _generator.getOutputEscapes();
        assertTrue(outputEscapes[255] != -1);
    }

    // Test for writeFieldName() method
    @Test
    public void testWriteFieldName() throws IOException {
        _generator.writeFieldName("fieldName");
        String fieldName = _generator.getOutputBuffer().toString();
        assertTrue(fieldName.equals("\"fieldName\""));
    }

    // Test for writeString() method
    @Test
    public void testWriteString() throws IOException {
        _generator.writeString("stringValue");
        String stringValue = _generator.getOutputBuffer().toString();
        assertTrue(stringValue.equals("\"stringValue\""));
    }

    // Test for writeStringField() method
    @Test
    public void testWriteStringField() throws IOException {
        _generator.writeStringField("fieldName", "stringValue");
        String expected = "\"fieldName\":\"stringValue\"";
        String actual = _generator.getOutputBuffer().toString();
        assertTrue(actual.equals(expected));
    }

    // Test for enable() method
    @Test
    public void testEnableFeatureQuoteFieldNames() throws IOException {
        _generator.enable(Feature.QUOTE_FIELD_NAMES);
        String fieldName = "fieldName";
        _generator.writeFieldName(fieldName);
        String actual = _generator.getOutputBuffer().toString();
        assertFalse(actual.startsWith("\""));
        _generator.disable(Feature.QUOTE_FIELD_NAMES);
        _generator.writeFieldName(fieldName);
        actual = _generator.getOutputBuffer().toString();
        assertTrue(actual.startsWith("\""));
    }

    // Test for setRootValueSeparator() method
    @Test
    public void testSetRootValueSeparator() throws IOException {
        _generator.setRootValueSeparator(new SerializableString("rootValueSeparator"));
        String expected = "\"rootValueSeparator\"";
        String actual = _generator.getOutputBuffer().toString();
        assertTrue(actual.equals(expected));
    }

    // Test for version() method
    @Test
    public void testVersion() {
        Version version = _generator.version();
        assertNotNull(version);
    }

    // Test for _checkStdFeatureChanges() method
    @Test(expected = IllegalStateException.class)
    public void testCheckStdFeatureChanges() {
        _generator.enable(Feature.QUOTE_FIELD_NAMES);
        _generator.disable(Feature.QUOTE_FIELD_NAMES);
        _generator.enable(Feature.QUOTE_FIELD_NAMES);
    }

    // Test for edge cases: writeFieldName with empty string and null value
    @Test
    public void testWriteFieldNameWithEmptyStringAndNull() throws IOException {
        _generator.writeFieldName("");
        String emptyString = _generator.getOutputBuffer().toString();
        assertTrue(emptyString.equals("\"\""));

        _generator.writeNull();
        _generator.writeFieldName(null);
        String nullValue = _generator.getOutputBuffer().toString();
        assertTrue(nullValue.equals("null"));
    }

    // Test for exception handling: IOException when writeString() called with null value
    @Test(expected = IOException.class)
    public void testIOExceptionWhenWriteStringWithNull() throws IOException {
        _generator.writeString(null);
    }

    // Test for exception handling: IllegalStateException when enableFeature() called with unsupported feature
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenEnableUnsupportedFeature() {
        _generator.enable(Feature.IGNORE_UNDEFINED_PROPERTY);
    }
}