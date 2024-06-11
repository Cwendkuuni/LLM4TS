package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        try {
            jsonGenerator.writeStringField("fieldName", "value");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testEnable() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void test_checkStdFeatureChanges() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator._checkStdFeatureChanges(0, 0);
        assertFalse(jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setCharacterEscapes(null);
        assertNotNull(jsonGenerator.getCharacterEscapes());
    }
    
    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setRootValueSeparator(null);
        assertNull(jsonGenerator._rootValueSeparator);
    }
    
    @Test
    public void testVersion() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNotNull(jsonGenerator.version());
    }

}