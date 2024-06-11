package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGenerator;
    private CharacterEscapes characterEscapes;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        characterEscapes = new CharacterEscapes();
    }

    @Test
    public void testJsonGeneratorImplConstructor() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        assertNotNull(jsonGenerator);
    }

    @Test
    public void testJsonGeneratorImplEnableFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testJsonGeneratorImplSetHighestNonEscapedChar() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        jsonGenerator.setHighestNonEscapedChar(0xFFFF);
        assertEquals(0xFFFF, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testJsonGeneratorImplSetCharacterEscapes() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertSame(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void testJsonGeneratorImplSetRootValueSeparator() throws IOException {
        String rootValueSeparator = "rootValueSeparator";
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        jsonGenerator.setRootValueSeparator(new SerializableString(rootValueSeparator));
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator.getValue());
    }

    @Test
    public void testJsonGeneratorImplVersion() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, Feature.DEFAULT_USE_ANNOTATIONS.getMask(), null);
        Version version = jsonGenerator.version();
        assertNotNull(version);
    }

    @Test
    public void testJsonGeneratorImplWriteFieldName() throws IOException {
        String fieldName = "fieldName";
        jsonGenerator.writeFieldName(fieldName);
        assertNull(jsonGenerator._fieldNameStart);
        assertNull(jsonGenerator._fieldNameEnd);
    }

    @Test(expected = IOException.class)
    public void testJsonGeneratorImplWriteFieldNameWithNullFieldName() throws IOException {
        jsonGenerator.writeFieldName(null);
    }

    @Test
    public void testJsonGeneratorImplWriteString() throws IOException {
        String stringValue = "stringValue";
        jsonGenerator.writeString(stringValue);
        assertEquals(stringValue, jsonGenerator._outValue);
    }

    @Test(expected = IOException.class)
    public void testJsonGeneratorImplWriteStringWithNullStringValue() throws IOException {
        jsonGenerator.writeString(null);
    }

    @After
    public void tearDown() throws IOException {
        jsonGenerator.close();
    }
}