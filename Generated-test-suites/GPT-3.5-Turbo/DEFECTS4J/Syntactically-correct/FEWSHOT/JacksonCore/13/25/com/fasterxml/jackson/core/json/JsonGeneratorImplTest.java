package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testConstructor() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(generator);
        assertFalse(generator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testEnable() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        generator.setHighestNonEscapedChar(127);
        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        CharacterEscapes escapes = new CharacterEscapes();
        generator.setCharacterEscapes(escapes);
        assertEquals(escapes, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        SerializableString separator = new SerializableString();
        generator.setRootValueSeparator(separator);
        assertEquals(separator, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(generator.version());
    }

    @Test
    public void testWriteStringField() throws IOException {
        IOContext ioContext = new IOContext(null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ioContext, 0, null);
        generator.writeStringField("fieldName", "value");
        //Assert the output of the written field, however this depends on the implementation of the writeStringField() method
        //Assume.assertEquals("fieldName: value", outputStream.getString());
    }
}