package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonGeneratorImplTest {
    
    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertEquals(false, generator._cfgUnqNames);
    }
    
    @Test
    public void test_checkStdFeatureChanges() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator._checkStdFeatureChanges(0, 0);
        assertEquals(true, generator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator._maximumNonEscapedChar);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(0, generator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());
    }
    
    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);
    }
    
    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.version();
        assertEquals(null, generator.version());
    }
    
    @Test
    public void testWriteStringField() throws Exception {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        generator.writeStringField("fieldName", "value");
        // validation needed here
    }
}