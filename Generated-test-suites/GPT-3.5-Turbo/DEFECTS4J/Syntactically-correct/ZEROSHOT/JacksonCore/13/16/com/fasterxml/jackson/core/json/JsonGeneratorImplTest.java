package com.fasterxml.jackson.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.*;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        IOContext ioContext = null;
        int features = 0;
        ObjectCodec codec = null;
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, features, codec);
    }

    @Test
    public void testEnable() {
        Feature f = null;
        JsonGenerator result = jsonGeneratorImpl.enable(f);
        assertNotNull(result);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        jsonGeneratorImpl._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        int charCode = 0;
        JsonGenerator result = jsonGeneratorImpl.setHighestNonEscapedChar(charCode);
        assertNotNull(result);
    }

    @Test
    public void testGetHighestEscapedChar() {
        int result = jsonGeneratorImpl.getHighestEscapedChar();
        assertNotNull(result);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = null;
        JsonGenerator result = jsonGeneratorImpl.setCharacterEscapes(esc);
        assertNotNull(result);
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes result = jsonGeneratorImpl.getCharacterEscapes();
        assertNotNull(result);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = null;
        JsonGenerator result = jsonGeneratorImpl.setRootValueSeparator(sep);
        assertNotNull(result);
    }

    @Test
    public void testVersion() {
        Version result = jsonGeneratorImpl.version();
        assertNotNull(result);
    }

    @Test
    public void testWriteStringField() {
        String fieldName = null;
        String value = null;
        try {
            jsonGeneratorImpl.writeStringField(fieldName, value);
        } catch(IOException e) {
            fail();
        }
    }
}