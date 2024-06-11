package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonGeneratorImplTest {
    private JsonGeneratorImpl jsonGenerator;
    
    @Before
    public void setUp() {
        jsonGenerator = new JsonGeneratorImpl();
    }
    
    @Test
    public void testEnable() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        
        assertEquals(false, jsonGenerator._cfgUnqNames);
    }
    
    @Test
    public void test_setHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(127);
        
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
    }
    
    @Test
    public void test_getHighestEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(127);
        
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }
    
    @Test
    public void test_setCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        
        assertEquals(characterEscapes, jsonGenerator._characterEscapes);
    }
    
    @Test
    public void test_getCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }
    
    @Test
    public void test_setRootValueSeparator() {
        SerializableString sep = new SerializableString();
        jsonGenerator.setRootValueSeparator(sep);
        
        assertEquals(sep, jsonGenerator._rootValueSeparator);
    }
    
    @Test
    public void test_version() {
        Version version = new Version();
        assertEquals(version, jsonGenerator.version());
    }
    
    @Test
    public void test_writeStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";
        jsonGenerator.writeStringField(fieldName, value);
        
        assertEquals(fieldName, jsonGenerator.writeFieldName());
        assertEquals(value, jsonGenerator.writeString());
    }
}