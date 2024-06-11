package com.fasterxml.jackson.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonGeneratorImplTest {

    // test constructor
    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.ESCAPE_NON_ASCII.getMask();
        ObjectCodec codec = new ObjectCodec();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        assertNotNull(generator);
    }

    // test enable
    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.enable(Feature.QUOTE_FIELD_NAMES);

        assertTrue(generator._cfgUnqNames);
    }

    // test _checkStdFeatureChanges
    @Test
    public void testCheckStdFeatureChanges() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator._checkStdFeatureChanges(features, features);

        assertTrue(generator._cfgUnqNames);
    }

    // test setHighestNonEscapedChar
    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.ESCAPE_NON_ASCII.getMask();
        ObjectCodec codec = new ObjectCodec();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.setHighestNonEscapedChar(100);

        assertEquals(100, generator._maximumNonEscapedChar);
    }

    // test getHighestEscapedChar
    @Test
    public void testGetHighestEscapedChar() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.ESCAPE_NON_ASCII.getMask();
        ObjectCodec codec = new ObjectCodec();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.setHighestNonEscapedChar(100);

        assertEquals(100, generator.getHighestEscapedChar());
    }

    // test setCharacterEscapes
    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();
        CharacterEscapes escapes = new CharacterEscapes();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.setCharacterEscapes(escapes);

        assertEquals(escapes.getEscapeCodesForAscii(), generator._outputEscapes);
    }

    // test getCharacterEscapes
    @Test
    public void testGetCharacterEscapes() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();
        CharacterEscapes escapes = new CharacterEscapes();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.setCharacterEscapes(escapes);

        assertEquals(escapes, generator.getCharacterEscapes());
    }

    // test setRootValueSeparator
    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();
        SerializableString sep = new DefaultPrettyPrinter().DEFAULT_ROOT_VALUE_SEPARATOR;

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.setRootValueSeparator(sep);

        assertEquals(sep, generator._rootValueSeparator);
    }

    // test version
    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);

        assertNotNull(generator.version());
    }

    // test writeStringField
    @Test
    public void testWriteStringField() throws IOException {
        IOContext ctxt = new IOContext(null, null, false);
        int features = Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = new ObjectCodec();
        String fieldName = "fieldName";
        String value = "value";

        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator.writeStringField(fieldName, value);

        assertNull(generator.getCurrentValue());
    }

}