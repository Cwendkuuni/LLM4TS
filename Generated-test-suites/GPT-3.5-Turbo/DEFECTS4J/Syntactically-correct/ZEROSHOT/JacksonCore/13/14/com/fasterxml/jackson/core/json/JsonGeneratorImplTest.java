package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
    }

    @Test
    public void testEnable() {
        JsonGeneratorImpl result = jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(result._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        jsonGenerator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void test_setHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(200);
        assertEquals(200, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void test_getHighestEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(200);
        assertEquals(200, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void test_setCharacterEscapes() {
        CharacterEscapes esc = new CharacterEscapes();
        jsonGenerator.setCharacterEscapes(esc);
        assertNotNull(jsonGenerator._characterEscapes);
        assertNotNull(jsonGenerator._outputEscapes);
    }

    @Test
    public void test_getCharacterEscapes() {
        assertEquals(null, jsonGenerator.getCharacterEscapes());
    }

    @Test
    public void test_setRootValueSeparator() {
        SerializableString sep = new DefaultPrettyPrinter();
        JsonGeneratorImpl result = jsonGenerator.setRootValueSeparator(sep);
        assertEquals(sep, result._rootValueSeparator);
    }

    @Test
    public void test_version() {
        assertNotNull(jsonGenerator.version());
    }

    @Test
    public void test_writeStringField() {
        try {
            jsonGenerator.writeStringField("fieldName", "value");
        } catch (IOException e) {
            fail("An exception occurred");
        }
    }
}