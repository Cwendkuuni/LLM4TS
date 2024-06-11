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
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGenerator);
    }

    // Test for enable method
    @Test
    public void testEnableFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    // Test for setHighestNonEscapedChar method
    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());
    }

    // Test for setCharacterEscapes method
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    // Test for setRootValueSeparator method
    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        SerializableString rootValueSeparator = new SerializableString("rootValueSeparator");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }

    // Test for writeFieldName method with quoted field names
    @Test
    public void testWriteFieldNameQuoted() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("\"fieldName\"");
        jsonGenerator.writeValue("value");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContent().toString();
        assertTrue(result.contains("\"fieldName\":"));
    }

    // Test for writeFieldName method without quoted field names
    @Test
    public void testWriteFieldNameUnquoted() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.disabledIn(0), null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("fieldName");
        jsonGenerator.writeValue("value");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContent().toString();
        assertTrue(result.contains("fieldName:"));
    }

    // Test for writeString method with standard escapes
    @Test
    public void testWriteStringStandardEscapes() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("fieldName");
        jsonGenerator.writeString("string with \\" + "escape sequences: \\b \\f \\n \\r \\t \\\" \\' \\\\");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContent().toString();
        assertTrue(result.contains("\"fieldName\":\"string with \\b\\\\\\" + "escape sequences: \\b\\f\\n\\r\\t\\\"\\'\\\\\""));
    }

    // Test for writeString method with custom escapes
    @Test
    public void testWriteStringCustomEscapes() throws IOException {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int c) {
                switch (c) {
                    case '\b': return "b";
                    case '\f': return "f";
                    case '\n': return "n";
                    case '\r': return "r";
                    case '\t': return "t";
                    case '\"': return "\\\"";
                    case '\'': return "\\'";
                    case '\\': return "\\\\";
                    default: return super.getEscapeSequence(c);
                }
            }
        };
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("fieldName");
        jsonGenerator.writeString("string with escape sequences: \\b \\f \\n \\r \\t \\\" \\' \\\\");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputContent().toString();
        assertTrue(result.contains("\"fieldName\":\"string with escape sequences: b f n r t \\\" \' \\\\\""));
    }
}