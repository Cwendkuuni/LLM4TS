package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for JsonGeneratorImpl
 */
public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() throws Exception {
        ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
    }

    // Test for constructor
    @Test
    public void testJsonGeneratorImplConstructor() {
        assertNotNull(jsonGenerator);
    }

    // Test for method enable
    @Test
    public void testEnableFeatureQuoteFieldNames() throws Exception {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    // Test for method enable with Feature.QUOTE_FIELD_NAMES disabled
    @Test
    public void testEnableFeatureQuoteFieldNamesDisabled() throws Exception {
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    // Test for method setHighestNonEscapedChar
    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());
    }

    // Test for method setHighestNonEscapedChar with charCode<0
    @Test(expected = IllegalArgumentException.class)
    public void testSetHighestNonEscapedCharNegativeCharCode() throws Exception {
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    // Test for method setCharacterEscapes
    @Test
    public void testSetCharacterEscapes() throws Exception {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int c) {
                return String.valueOf((char) c);
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
    }

    // Test for method setCharacterEscapes with null
    @Test(expected = IllegalArgumentException.class)
    public void testSetCharacterEscapesNull() throws Exception {
        jsonGenerator.setCharacterEscapes(null);
    }

    // Test for method setRootValueSeparator
    @Test
    public void testSetRootValueSeparator() throws Exception {
        SerializableString rootValueSeparator = new SerializableString("_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    // Test for method version
    @Test
    public void testVersion() throws Exception {
        assertEquals(VersionUtil. Version.VERSION_2_11_2, jsonGenerator.version());
    }

    // Test for method writeFieldName with unquoted property names
    @Test
    public void testWriteFieldNameUnquotedPropertyNames() throws Exception {
        jsonGenerator._cfgUnqNames = true;
        jsonGenerator.writeFieldName("test");
        String result = jsonGenerator.getOutputStr();
        assertEquals("test", result);
    }

    // Test for method writeFieldName with quoted property names
    @Test
    public void testWriteFieldNameQuotedPropertyNames() throws Exception {
        jsonGenerator._cfgUnqNames = false;
        jsonGenerator.writeFieldName("\"test\"");
        String result = jsonGenerator.getOutputStr();
        assertEquals("\"test\"", result);
    }

    // Test for method writeStringField with unescaped characters
    @Test
    public void testWriteStringFieldUnescapedCharacters() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeStringField("test", "test");
        String result = jsonGenerator.getOutputStr();
        assertEquals("{\"test\":\"test\"}", result);
    }

    // Test for method writeStringField with escaped characters
    @Test
    public void testWriteStringFieldEscapedCharacters() throws Exception {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSequence(int c) {
                return String.valueOf((char) (c + 128));
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeStringField("test", "test");
        String result = jsonGenerator.getOutputStr();
        assertEquals("{\"test\":\"test\\129test\\129\"}", result);
    }

    // Test for method writeStringField with unquoted JSON number
    @Test
    public void testWriteStringFieldUnquotedJsonNumber() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", 123.45);
        String result = jsonGenerator.getOutputStr();
        assertEquals("{\"test\":123.45}", result);
    }

    // Test for method writeStringField with quoted JSON number
    @Test
    public void testWriteStringFieldQuotedJsonNumber() throws Exception {
        jsonGenerator._cfgUnqNames = false;
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", 123.45);
        String result = jsonGenerator.getOutputStr();
        assertEquals("{\"test\":\"123.45\"}", result);
    }

    // Test for method writeStringField with empty String
    @Test
    public void testWriteStringFieldEmptyString() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeStringField("test", "");
        String result = jsonGenerator.getOutputStr();
        assertEquals("{\"test\":\"\"}", result);
    }

    // Test for method writeStringField with null String
    @Test
    public void testWriteStringFieldNullString() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeStringField("test", null);
        String result = jsonGenerator.getOutputStr();
        assertEquals("{\"test\":null}", result);
    }

    // Test for method writeRawValue with unquoted value
    @Test
    public void testWriteRawValueUnquotedValue() throws Exception {
        jsonGenerator.writeRawValue("test");
        String result = jsonGenerator.getOutputStr();
        assertEquals("test", result);
    }

    // Test for method writeRawValue with quoted value
    @Test
    public void testWriteRawValueQuotedValue() throws Exception {
        jsonGenerator._cfgUnqNames = false;
        jsonGenerator.writeRawValue("\"test\"");
        String result = jsonGenerator.getOutputStr();
        assertEquals("\"test\"", result);
    }

    // Test for method writeRawValue with empty String
    @Test
    public void testWriteRawValueEmptyString() throws Exception {
        jsonGenerator.writeRawValue("");
        String result = jsonGenerator.getOutputStr();
        assertEquals("", result);
    }

    // Test for method writeRawValue with null value
    @Test
    public void testWriteRawValueNullValue() throws Exception {
        jsonGenerator.writeRawValue(null);
        String result = jsonGenerator.getOutputStr();
        assertEquals(null, result);
    }

    // Test for method writeRawValue with raw JSON number
    @Test
    public void testWriteRawValueRawJsonNumber() throws Exception {
        jsonGenerator.writeRawValue("123.45");
        String result = jsonGenerator.getOutputStr();
        assertEquals("123.45", result);
    }

    // Test for method writeRawValue with raw JSON boolean
    @Test
    public void testWriteRawValueRawJsonBoolean() throws Exception {
        jsonGenerator.writeRawValue("true");
        String result = jsonGenerator.getOutputStr();
        assertEquals(true, result.booleanValue());
    }

    // Test for method writeRawValue with raw JSON null
    @Test
    public void testWriteRawValueRawJsonNull() throws Exception {
        jsonGenerator.writeRawValue("null");
        String result = jsonGenerator.getOutputStr();
        assertEquals(null, result);
    }

    // Test for method writeNumberField with number less than zero
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNumberFieldNumberLessThanZero() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", -123);
    }

    // Test for method writeNumberField with Long.MAX_VALUE
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNumberFieldLongMaxValue() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", Long.MAX_VALUE);
    }

    // Test for method writeNumberField with Long.MIN_VALUE
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNumberFieldLongMinValue() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", Long.MIN_VALUE);
    }

    // Test for method writeNumberField with double NaN
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNumberFieldDoubleNaN() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", Double.NaN);
    }

    // Test for method writeNumberField with double infinity
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNumberFieldDoubleInfinity() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", Double.POSITIVE_INFINITY);
    }

    // Test for method writeNumberField with double negative infinity
    @Test(expected = IllegalArgumentException.class)
    public void testWriteNumberFieldDoubleNegativeInfinity() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeNumberField("test", Double.NEGATIVE_INFINITY);
    }

    // Test for method writeRawValue with null character
    @Test(expected = IOException.class)
    public void testWriteRawValueNullCharacter() throws Exception {
        jsonGenerator.writeFieldName("test");
        jsonGenerator.writeRawValue(String.valueOf((char) 0));
    }
}