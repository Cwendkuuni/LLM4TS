package com.example.test;

import com.fasterxml.jackson.core.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImplConstructor() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectMapper().getFactory();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, features, codec);
        assertNotNull(jsonGenerator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplConstructorWithNullIOContext() {
        IOContext ctxt = null;
        int features = 0;
        ObjectCodec codec = new ObjectMapper().getFactory();
        assertThrows(IllegalArgumentException.class, () -> new JsonGeneratorImpl(ctxt, features, codec));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplConstructorWithInvalidFeatures() {
        IOContext ctxt = new IOContext();
        int features = 0xDEAD;
        ObjectCodec codec = new ObjectMapper().getFactory();
        assertThrows(IllegalArgumentException.class, () -> new JsonGeneratorImpl(ctxt, features, codec));
    }

    @Test
    public void testJsonGeneratorImplSetHighestNonEscapedCharWithNegativeValue() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertEquals(-1, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testJsonGeneratorImplGetHighestEscapedCharWithValidCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[0];
            }
        });
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplSetCharacterEscapesWithNull() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setCharacterEscapes(null);
        assertThrows(IllegalArgumentException.class, () -> jsonGenerator.setCharacterEscapes(null));
    }

    @Test
    public void testJsonGeneratorImplEnableFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplEnableInvalidFeature() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.enable(Feature.INVALID_FEATURE);
        assertThrows(IllegalArgumentException.class, () -> jsonGenerator.enable(Feature.INVALID_FEATURE));
    }

    @Test
    public void testJsonGeneratorImplSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplSetHighestNonEscapedCharWithNegativeValue() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setHighestNonEscapedChar(-1);
        assertThrows(IllegalArgumentException.class, () -> jsonGenerator.setHighestNonEscapedChar(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplSetHighestNonEscapedCharWithNonASCIIValue() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setHighestNonEscapedChar(0x10000);
        assertThrows(IllegalArgumentException.class, () -> jsonGenerator.setHighestNonEscapedChar(0x10000));
    }

    @Test
    public void testJsonGeneratorImplGetHighestEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testJsonGeneratorImplSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{0x1A};
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(0x1A, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testJsonGeneratorImplSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        SerializableString rootValueSeparator = "rootValueSeparator";
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplSetRootValueSeparatorWithNull() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        jsonGenerator.setRootValueSeparator(null);
        assertThrows(IllegalArgumentException.class, () -> jsonGenerator.setRootValueSeparator(null));
    }

    @Test
    public void testJsonGeneratorImplGetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory());
        SerializableString rootValueSeparator = "rootValueSeparator";
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }
}