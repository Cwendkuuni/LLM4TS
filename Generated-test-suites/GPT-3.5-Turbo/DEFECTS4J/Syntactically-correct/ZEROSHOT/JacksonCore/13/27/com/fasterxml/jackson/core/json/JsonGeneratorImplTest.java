package com.fasterxml.jackson.core.json;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
    private JsonGeneratorImpl generatorImpl;
    
    @Before
    public void setUp() {
        generatorImpl = new JsonGeneratorImpl(null, 0, null) {};
    }
    
    @Test
    public void testEnable() {
        JsonGeneratorImpl.Feature feature = JsonGeneratorImpl.Feature.QUOTE_FIELD_NAMES;

        JsonGenerator result = generatorImpl.enable(feature);
        
        assertTrue(result instanceof JsonGeneratorImpl);
        assertTrue(generatorImpl._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;

        generatorImpl._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        
        assertTrue(generatorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        int charCode = 127;

        JsonGenerator result = generatorImpl.setHighestNonEscapedChar(charCode);
        
        assertEquals(generatorImpl, result);
        assertEquals(charCode, generatorImpl._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        int expectedValue = 127;

        generatorImpl._maximumNonEscapedChar = expectedValue;
        int result = generatorImpl.getHighestEscapedChar();
        
        assertEquals(expectedValue, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = null;

        JsonGenerator result = generatorImpl.setCharacterEscapes(esc);
        
        assertEquals(generatorImpl, result);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, generatorImpl._outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes expected = null;

        generatorImpl._characterEscapes = expected;
        CharacterEscapes result = generatorImpl.getCharacterEscapes();
        
        assertEquals(expected, result);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = null;

        JsonGenerator result = generatorImpl.setRootValueSeparator(sep);
        
        assertEquals(generatorImpl, result);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generatorImpl._rootValueSeparator);
    }
    
    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";

        generatorImpl.writeStringField(fieldName, value);
        
        // assertions
    }
}