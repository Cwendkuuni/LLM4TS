package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {
    
    private JsonGeneratorImpl jsonGenerator;
    
    @Before
    public void setUp() {
        jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
    }
    
    @Test
    public void testEnableFeature() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator._maximumNonEscapedChar);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(esc);
        assertEquals(esc, jsonGenerator._characterEscapes);
    }
    
    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = new SerializableString();
        jsonGenerator.setRootValueSeparator(sep);
        assertEquals(sep, jsonGenerator._rootValueSeparator);
    }
    
    @Test
    public void testVersion() {
        Version version = jsonGenerator.version();
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), version);
    }
    
    @Test
    public void testWriteStringField() {
        try {
            jsonGenerator.writeStringField("name", "value");
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

}