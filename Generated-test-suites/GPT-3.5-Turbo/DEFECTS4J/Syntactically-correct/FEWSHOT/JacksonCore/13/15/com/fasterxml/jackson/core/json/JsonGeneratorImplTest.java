package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        assertSame(ctxt, jsonGenerator._ioContext);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        assertSame(jsonGenerator, jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES));
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        jsonGenerator._checkStdFeatureChanges(2, 2);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        assertSame(jsonGenerator, jsonGenerator.setHighestNonEscapedChar(100));
        assertEquals(100, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        jsonGenerator.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        CharacterEscapes esc = new CharacterEscapes();
        assertSame(jsonGenerator, jsonGenerator.setCharacterEscapes(esc));
        assertSame(esc, jsonGenerator._characterEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        CharacterEscapes esc = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(esc);
        assertSame(esc, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        SerializableString sep = new SerializableString();
        assertSame(jsonGenerator, jsonGenerator.setRootValueSeparator(sep));
        assertSame(sep, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        assertSame(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGenerator.version());
    }

    @Test
    public void testWriteStringField() {
        // writeStringField() is a final method and thus cannot be directly tested
        // but since it calls writeFieldName and writeString, these methods can be tested indirectly
        IOContext ctxt = new IOContext(null, null, false);
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, 2, codec);
        try {
            jsonGenerator.writeStringField("fieldName", "value");
        } catch (IOException e) {
            fail("Unexpected IO Exception");
        }
    }
}