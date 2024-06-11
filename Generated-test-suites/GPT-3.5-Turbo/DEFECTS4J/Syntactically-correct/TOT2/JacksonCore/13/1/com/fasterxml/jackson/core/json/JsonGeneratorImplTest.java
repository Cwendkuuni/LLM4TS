
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setup() {
        jsonGenerator = new JsonGeneratorImplMock();
    }

    @Test
    public void testEnableFeatureQuoteFieldNames() {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapesMock characterEscapes = new CharacterEscapesMock();
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }
}
