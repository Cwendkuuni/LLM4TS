package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNotNull(jsonGenerator);
        assertNull(jsonGenerator._characterEscapes);
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getDefaultValue(), jsonGenerator._cfgUnqNames);
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        assertEquals(JsonGenerator.Feature.CAN_OVERWRITE_OUTPUT.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.CAN_OVERWRITE_OUTPUT));
        assertEquals(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN_TEXT.getDefaultValue(), jsonGenerator.isEnabled(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN_TEXT));
    }

    @Test
    public void testFeatureConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0x201, null);
        assertNotNull(jsonGenerator);
        assertNull(jsonGenerator._characterEscapes);
        assertEquals(false, jsonGenerator._cfgUnqNames);
        assertEquals(true, jsonGenerator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertEquals(false, jsonGenerator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(true, jsonGenerator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
        assertEquals(true, jsonGenerator.isEnabled(JsonGenerator.Feature.CAN_OVERWRITE_OUTPUT));
        assertEquals(false, jsonGenerator.isEnabled(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN_TEXT));
    }

    @Test
    public void testFeatureChanges() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertEquals(true, jsonGenerator._cfgUnqNames);
        jsonGenerator.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertEquals(false, jsonGenerator._cfgUnqNames);
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator.getHighestEscapedChar());
        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{ 1, 2, 3 };
            }

            @Override
            public int[] getEscapeCodesForUnicode() {
                return new int[0];
            }
        });
        assertEquals(new int[]{ 1, 2, 3 }, jsonGenerator._outputEscapes);
    }
}