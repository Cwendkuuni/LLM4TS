package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testConfigConst() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertEquals(Version.unknownVersion(), jsonGenerator.version());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator.getRootValueSeparator());
        assertEquals(Feature.QuoteFieldNames.DEFAULT, jsonGenerator.isEnabled(Feature.QuoteFieldNames));
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        CharacterEscapes characterEscapes = new CharacterEscapes() {};
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
        CharacterEscapes standardEscapes = jsonGenerator.getCharacterEscapes();
        assertNotEquals(characterEscapes, standardEscapes);
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        SerializableString rootValueSeparator = new SerializableString("testSeparator");
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }

    @Test
    public void testHighestNonEscapedChar() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testQuoteFieldNames() throws Exception {
        JsonGeneratorImpl jsonGenerator1 = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator1.writeStringField("field", "value");
        assertTrue(jsonGenerator1.toString().contains("\"field\":\"value\""));

        JsonGeneratorImpl jsonGenerator2 = new JsonGeneratorImpl(null, Feature.QuoteFieldNames.valueOf(true), null);
        jsonGenerator2.writeStringField("field", "value");
        assertFalse(jsonGenerator2.toString().contains("\"field\":\"value\""));
        assertTrue(jsonGenerator2.toString().contains("field:value"));
    }
}