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
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    private IOContext _ioContext;
    private JsonGeneratorImpl _generator;
    private CharacterEscapes _characterEscapes;

    @Before
    public void setUp() {
        _ioContext = new IOContext();
        _characterEscapes = new CharacterEscapes();
        _generator = new JsonGeneratorImpl(_ioContext, 0, null) {
            @Override
            protected CharacterEscapes getCharacterEscapes() {
                return _characterEscapes;
            }
        };
    }

    // Test methods for JsonGeneratorImpl constructor

    @Test
    public void testJsonGeneratorImplConstructor() {
        assertNotNull(_generator);
        assertEquals(_ioContext, _generator._ioContext);
        assertEquals(0, _generator.features());
        assertNull(_generator.codec());
    }

    // Test methods for setCharacterEscapes()

    @Test
    public void testSetCharacterEscapesWithNullEscapes() {
        _generator.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, _generator._outputEscapes);
    }

    @Test
    public void testSetCharacterEscapesWithCustomEscapes() {
        CharacterEscapes customEscapes = new CharacterEscapes();
        _generator.setCharacterEscapes(customEscapes);
        assertSame(customEscapes, _generator._characterEscapes);
        assertArrayNotEquals(JsonGeneratorImpl.sOutputEscapes, _generator._outputEscapes);
    }

    // Test methods for getCharacterEscapes()

    @Test
    public void testGetCharacterEscapes() {
        assertSame(_characterEscapes, _generator.getCharacterEscapes());
    }

    // Test methods for setRootValueSeparator()

    @Test
    public void testSetRootValueSeparatorWithNullSeparator() {
        _generator.setRootValueSeparator(null);
        assertNull(_generator._rootValueSeparator);
    }

    @Test
    public void testSetRootValueSeparatorWithSerializableStringSeparator() {
        SerializableString separator = new SerializableString(":");
        _generator.setRootValueSeparator(separator);
        assertSame(separator, _generator._rootValueSeparator);
    }

    // Test methods for getRootValueSeparator()

    @Test
    public void testGetRootValueSeparator() {
        assertNull(_generator._rootValueSeparator);
    }

    // Test methods for writeString()

    @Test(expected = IOException.class)
    public void testWriteStringWithNullValueThrowsIOException() throws IOException {
        _generator.writeString(null);
    }

    @Test
    public void testWriteStringWithEmptyValue() throws IOException {
        _generator.writeString("");
        assertEquals(0, _generator.getOutputSize());
    }

    @Test
    public void testWriteStringWithSingleCharacterValue() throws IOException {
        _generator.writeString("A");
        assertEquals(2, _generator.getOutputSize());
    }

    @Test
    public void testWriteStringWithMultiCharacterValue() throws IOException {
        _generator.writeString("Hello, World!");
        assertEquals(14, _generator.getOutputSize());
    }

    // Test methods for writeFieldName() and writeString()

    @Test
    public void testWriteStringField() throws IOException {
        _generator.writeStartObject();
        _generator.writeFieldName("name");
        _generator.writeString("John Doe");
        _generator.writeEndObject();
        String expected = "{\"name\":\"John Doe\"}";
        assertEquals(expected, _generator.getOutput().toString());
    }
}