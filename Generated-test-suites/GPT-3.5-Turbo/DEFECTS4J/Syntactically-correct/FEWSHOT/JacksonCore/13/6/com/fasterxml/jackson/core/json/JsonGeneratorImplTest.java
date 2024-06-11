package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JsonGeneratorImplTest {
    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setup() {
        IOContext ioContext = new IOContext();
        int features = 0;
        ObjectCodec objectCodec = new ObjectCodec();

        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, features, objectCodec);
    }

    @Test
    public void testEnable() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;

        jsonGeneratorImpl._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        int charCode = 100;

        jsonGeneratorImpl.setHighestNonEscapedChar(charCode);

        assertEquals(charCode, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes();

        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);

        assertEquals(characterEscapes, jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = new DefaultPrettyPrinter().createRootValueSeparator();

        jsonGeneratorImpl.setRootValueSeparator(sep);

        assertEquals(sep, jsonGeneratorImpl._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        Version version = VersionUtil.versionFor(JsonGeneratorImpl.class);

        assertEquals(version, jsonGeneratorImpl.version());
    }
}