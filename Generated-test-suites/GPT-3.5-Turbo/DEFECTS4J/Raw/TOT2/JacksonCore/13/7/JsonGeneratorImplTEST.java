package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        try {
            generator.writeStringField("key", "value");
            // Test successful write of string field
        } catch (IOException e) {
            fail("Exception thrown while writing string field");
        }
    }

    @Test
    public void testEnableQuoteFieldNames() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        generator.setHighestNonEscapedChar(127);
        
        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        generator.setRootValueSeparator(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);
    }

    @Test
    public void testGetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        CharacterEscapes escapes = generator.getCharacterEscapes();
        
        assertNull(escapes);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(null, 0, null);
        
        assertNotNull(generator.version());
    }

}