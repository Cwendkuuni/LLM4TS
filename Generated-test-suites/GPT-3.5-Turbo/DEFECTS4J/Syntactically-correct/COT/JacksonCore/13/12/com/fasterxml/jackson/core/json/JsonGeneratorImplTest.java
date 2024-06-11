
package com.fasterxml.jackson.core.json;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
    
    @Test
    public void testWriteStringField() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.writeStringField("fieldName", "value");
        // assert expected result
    }
    
    @Test
    public void testEnable() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        // assert expected result
        assertTrue(jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void test_checkStdFeatureChanges() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator._checkStdFeatureChanges(0, Feature.QUOTE_FIELD_NAMES.getMask());
        // assert expected result
        assertFalse(jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(255);
        // assert expected result
        assertEquals(255, jsonGenerator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setCharacterEscapes(null);
        // assert expected result
        // assertTrue(jsonGenerator.getCharacterEscapes());
    }
    
    @Test
    public void testGetCharacterEscapes() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setCharacterEscapes(null);
        // assert expected result
        // assertNotNull(jsonGenerator.getCharacterEscapes());
    }
    
    @Test
    public void testSetRootValueSeparator() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setRootValueSeparator(null);
        // assert expected result
        // assertEquals(null, jsonGenerator.getRootValueSeparator());
    }
    
    @Test
    public void testVersion() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        // assert expected result
        assertNotNull(jsonGenerator.version());
    }
}
