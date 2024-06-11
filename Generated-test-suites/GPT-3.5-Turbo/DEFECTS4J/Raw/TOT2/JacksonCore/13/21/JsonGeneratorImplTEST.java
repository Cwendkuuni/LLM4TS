package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
    
    private JsonGeneratorImpl jsonGeneratorImpl;
    
    @Before
    public void setup() {
        jsonGeneratorImpl = new JsonGeneratorImpl(new IOContext(), 0, null);
    }
    
    @Test
    public void testEnable() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGeneratorImpl.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes customEscapes = new CharacterEscapes();
        jsonGeneratorImpl.setCharacterEscapes(customEscapes);
        assertEquals(customEscapes, jsonGeneratorImpl.getCharacterEscapes());
    }
    
    @Test
    public void testSetRootValueSeparator() {
        SerializableString separator = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        jsonGeneratorImpl.setRootValueSeparator(separator);
        assertEquals(separator, jsonGeneratorImpl._rootValueSeparator);
    }
    
    @Test
    public void testWriteStringField() {
        String fieldName = "testField";
        String value = "testValue";
        try {
            jsonGeneratorImpl.writeStringField(fieldName, value);
            assertEquals("testField", jsonGeneratorImpl.getCurrentName());
            assertEquals("testValue", jsonGeneratorImpl.getText());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}