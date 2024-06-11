package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private ObjectCodec objectCodec;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
    }

    

    // Test constructor
    @Test
    public void testJsonGeneratorImplConstructor() {
        assertNotNull(jsonGenerator);
    }

    // Test getVersion
    @Test
    public void testGetVersion() {
        Version version = jsonGenerator.version();
        assertNotNull(version);
    }

    // Test writeFieldName
    @Test
    public void testWriteFieldName() throws IOException {
        jsonGenerator.writeFieldName("fieldName");
        assertEquals("fieldName", jsonGenerator.getOutputBuffer().toString());
    }

    @Test(expected = IOException.class)
    public void testWriteFieldNameWithNullFieldName() throws IOException {
        jsonGenerator.writeFieldName(null);
    }

    // Test writeString
    @Test
    public void testWriteString() throws IOException {
        jsonGenerator.writeString("hello");
        assertEquals("\"hello\"", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStringEmpty() throws IOException {
        jsonGenerator.writeString("");
        assertEquals("\"\"", jsonGenerator.getOutputBuffer().toString());
    }

    @Test(expected = IOException.class)
    public void testWriteStringWithNullString() throws IOException {
        jsonGenerator.writeString(null);
    }

    // Test enable
    @Test
    public void testEnable() {
        JsonGenerator jsonGenerator = jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testEnableTwice() {
        JsonGenerator jsonGenerator = jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        JsonGenerator jsonGenerator2 = jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertSame(jsonGenerator, jsonGenerator2);
    }

    // Test setHighestNonEscapedChar
    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetHighestNonEscapedCharToZero() {
        jsonGenerator.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    // Test setCharacterEscapes
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int c) {
                return Character.toString((char) c);
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeString("test");
        assertEquals("\\u0074\\u0065\\u0073\\u0074", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSetCharacterEscapesNull() {
        jsonGenerator.setCharacterEscapes(null);
        jsonGenerator.writeString("test");
        assertEquals("\"test\"", jsonGenerator.getOutputBuffer().toString());
    }

    // Test setRootValueSeparator
    @Test
    public void testSetRootValueSeparator() {
        jsonGenerator.setRootValueSeparator(new SerializableString("separator"));
        jsonGenerator.writeString("test");
        assertEquals("separator:\"test\"", jsonGenerator.getOutputBuffer().toString());
    }

    
}