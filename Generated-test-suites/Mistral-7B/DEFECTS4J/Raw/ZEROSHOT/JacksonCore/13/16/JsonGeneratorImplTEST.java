package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private CharacterEscapes characterEscapes;
    private ObjectCodec objectCodec;

    @Before
    public void setUp() throws Exception {
        ioContext = Mockito.mock(IOContext.class);
        characterEscapes = Mockito.mock(CharacterEscapes.class);
        objectCodec = Mockito.mock(ObjectCodec.class);

        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec) {
            @Override
            protected CharacterEscapes _characterEscapes = characterEscapes;
        };
    }

    @Test
    public void testDefaultConstruction() {
        JsonGenerationSchema schema = jsonGenerator.getSerializationConfig().getDefaultSerializationSchema();
        assertEquals("com.fasterxml.jackson.core.json.JsonGeneratorImpl", schema.getClassName());
    }

    @Test
    public void testConstructorWithIOContext() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, objectCodec);
        assertEquals(ioContext, jsonGenerator._ioContext);
    }

    @Test
    public void testConstructorWithFeaturesAndCodec() {
        int features = JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES.mask();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, features, objectCodec);
        assertEquals(features, jsonGenerator.getFeatures());
    }

    @Test
    public void testSetCharacterEscapes() throws IOException {
        when(characterEscapes.getEscapeCodesForAscii()).thenReturn(new int[0]);

        jsonGenerator.setCharacterEscapes(null);
        assertEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);

        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes.getEscapeCodesForAscii(), jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator._maximumNonEscapedChar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharNegativeValue() throws IOException {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharValueGreaterThan65535() throws IOException {
        jsonGenerator.setHighestNonEscapedChar(65536);
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        String separator = "test";
        jsonGenerator.setRootValueSeparator(new UTF8String("test"));
        assertEquals(separator, jsonGenerator._rootValueSeparator.getValue());
    }

    @Test
    public void testEnableFeatureQuoteFieldNames() throws IOException {
        jsonGenerator.enable(JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testDisableFeatureQuoteFieldNames() throws IOException {
        jsonGenerator.enable(JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.disable(JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() throws IOException {
        int newFeatureFlags = JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES.mask() | JsonGenerationSchema.Feature.ESCAPE_NON_ASCII.mask();
        jsonGenerator._checkStdFeatureChanges(newFeatureFlags, 0);
        assertFalse(jsonGenerator._cfgUnqNames);
        assertTrue(jsonGenerator.isEnabled(JsonGenerationSchema.Feature.ESCAPE_NON_ASCII));
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckStdFeatureChangesTwice() throws IOException {
        jsonGenerator._checkStdFeatureChanges(0, JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES.mask());
        jsonGenerator._checkStdFeatureChanges(0, JsonGenerationSchema.Feature.QUOTE_FIELD_NAMES.mask());
    }

    @Test
    public void testVersion() {
        Version version = jsonGenerator.version();
        assertNotNull(version);
    }
}