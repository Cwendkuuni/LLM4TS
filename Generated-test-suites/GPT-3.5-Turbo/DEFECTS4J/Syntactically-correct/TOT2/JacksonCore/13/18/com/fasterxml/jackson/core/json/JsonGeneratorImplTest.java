package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
    }

    @Test
    public void testEnableQuoteFieldNames() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(200);
        assertEquals(200, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(150);
        assertEquals(150, jsonGenerator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(esc);
        assertEquals(esc, jsonGenerator._characterEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        jsonGenerator.setRootValueSeparator(sep);
        assertEquals(sep, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        assertNotNull(jsonGenerator.version());
    }

    @Test
    public void testWriteStringField() {
        try {
            jsonGenerator.writeStringField("fieldName", "value");
        } catch (Exception e) {
            fail("Exception thrown while testing writeStringField method");
        }
    }
}