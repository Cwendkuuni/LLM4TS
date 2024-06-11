package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        JsonGeneratorImpl result = generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        
        assertFalse(result._cfgUnqNames);
    }
    
    @Test
    public void testCheckStdFeatureChanges() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        generator._checkStdFeatureChanges(0, 0);
        
        assertFalse(generator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        JsonGeneratorImpl result = generator.setHighestNonEscapedChar(127);
        
        assertEquals(127, result._maximumNonEscapedChar);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        int result = generator.getHighestEscapedChar();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        JsonGeneratorImpl result = generator.setCharacterEscapes(null);
        
        assertEquals(JsonGeneratorImpl.sOutputEscapes, result._outputEscapes);
    }
    
    @Test
    public void testGetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        assertNull(generator.getCharacterEscapes());
    }
    
    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        String separator = "separator";
        JsonGeneratorImpl result = generator.setRootValueSeparator(new SerializableString(separator));
        
        assertEquals(separator, result._rootValueSeparator);
    }
}