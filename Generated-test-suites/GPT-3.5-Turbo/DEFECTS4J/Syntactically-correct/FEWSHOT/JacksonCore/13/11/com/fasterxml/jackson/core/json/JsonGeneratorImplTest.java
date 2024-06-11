package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {
    JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        generator = mock(JsonGeneratorImpl.class);
    }

    @Test
    public void testEnable() {
        generator.enable(Feature.AUTO_CLOSE_JSON_CONTENT);
        generator.enable(Feature.AUTO_CLOSE_TARGET);
        generator.enable(Feature.AUTO_CLOSE_SOURCE);
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        generator._checkStdFeatureChanges(1, 2);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        generator.setHighestNonEscapedChar(100);
        generator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testGetHighestEscapedChar() {
        assertEquals(0, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = mock(CharacterEscapes.class);
        generator.setCharacterEscapes(esc);
        generator.setCharacterEscapes(null);
    }

    @Test
    public void testGetCharacterEscapes() {
        assertNull(generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = mock(SerializableString.class);
        generator.setRootValueSeparator(sep);
    }

    @Test
    public void testVersion() {
        assertEquals(2.1, generator.version().getMajorVersion(), 0);
        assertEquals(0, generator.version().getMinorVersion(), 0);
    }

    @Test
    public void testWriteStringField() throws IOException {
        generator.writeStringField("fieldName", "value");
    }
}