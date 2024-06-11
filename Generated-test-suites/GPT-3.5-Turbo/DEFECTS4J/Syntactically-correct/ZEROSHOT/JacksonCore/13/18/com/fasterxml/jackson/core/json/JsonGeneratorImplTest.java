package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;

    @Before
    public void setUp() {
        jsonGenerator = mock(JsonGeneratorImpl.class);
    }

    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertEquals(jsonGenerator, generator);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        jsonGenerator._checkStdFeatureChanges(1, 2);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(jsonGenerator, generator);
    }

    @Test
    public void testGetHighestEscapedChar() {
        jsonGenerator._maximumNonEscapedChar = 127;
        int result = jsonGenerator.getHighestEscapedChar();
        assertEquals(127, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes escapes = mock(CharacterEscapes.class);
        when(escapes.getEscapeCodesForAscii()).thenReturn(new int[]{});
        JsonGeneratorImpl generator = jsonGenerator.setCharacterEscapes(escapes);
        assertEquals(jsonGenerator, generator);
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes escapes = mock(CharacterEscapes.class);
        jsonGenerator._characterEscapes = escapes;
        CharacterEscapes result = jsonGenerator.getCharacterEscapes();
        assertEquals(escapes, result);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = mock(SerializableString.class);
        JsonGeneratorImpl generator = jsonGenerator.setRootValueSeparator(sep);
        assertEquals(jsonGenerator, generator);
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";
        jsonGenerator.writeFieldName(fieldName);
        jsonGenerator.writeString(value);
    }
}