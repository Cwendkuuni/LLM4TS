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
 * Unit tests for JsonGeneratorImpl class
 */
public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGenerator;
    private IOContext ioContext;
    private CharacterEscapes characterEscapes;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        characterEscapes = new CharacterEscapes();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null) {
            @Override
            protected CharacterEscapes _characterEscapes() {
                return characterEscapes;
            }
        };
    }

    /**
     * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)}
     * @throws IOException
     */
    @Test
    public void testJsonGeneratorImplConstructor() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGenerator);
    }

    /**
     * Test method for {@link JsonGeneratorImpl#JsonGeneratorImpl(IOContext, int, ObjectCodec)} with null ObjectCodec
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplConstructorWithNullObjectCodec() throws IOException {
        new JsonGeneratorImpl(ioContext, 0, null);
    }

    /**
     * Test method for {@link JsonGeneratorImpl#setHighestNonEscapedChar(int)}
     */
    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator.getHighestEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    /**
     * Test method for {@link JsonGeneratorImpl#setCharacterEscapes(CharacterEscapes)}
     */
    @Test
    public void testSetCharacterEscapes() {
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());

        jsonGenerator.setCharacterEscapes(null);
        assertNull(jsonGenerator.getCharacterEscapes());
    }

    /**
     * Test method for {@link JsonGeneratorImpl#setRootValueSeparator(SerializableString)}
     */
    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("test");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }
}