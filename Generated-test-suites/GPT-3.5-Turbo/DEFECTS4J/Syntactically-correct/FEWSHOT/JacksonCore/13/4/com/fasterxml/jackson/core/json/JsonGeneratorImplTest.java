package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Test;

import java.io.IOException;

public class JsonGeneratorImplTest {
    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        JsonGeneratorImpl result = generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(result._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator._checkStdFeatureChanges(0, 0);
        generator._checkStdFeatureChanges(1, 1);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        JsonGeneratorImpl result = generator.setHighestNonEscapedChar(100);
        assertEquals(100, result.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        CharacterEscapes esc = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[0];
            }
        };
        JsonGeneratorImpl result = generator.setCharacterEscapes(esc);
        assertEquals(esc, result.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        SerializableString sep = new DefaultPrettyPrinter().getRootValueSeparator();
        JsonGeneratorImpl result = generator.setRootValueSeparator(sep);
        assertEquals(sep, result._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        VersionUtil.Version version = generator.version();
        assertEquals(VersionUtil.versionFor(GeneratorBase.class), version);
    }

    @Test
    public void testWriteStringField() throws IOException {
        IOContext ctxt = new IOContext(new CharTypes(), false, null);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.writeFieldName("fieldName");
        generator.writeString("value");
    }
}