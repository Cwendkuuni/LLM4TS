package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class JsonGeneratorImplTest {

    @Mock
    private IOContext ctxt;

    @Mock
    private ObjectCodec codec;

    @Test
    public void testConstructor() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        
        // Test enable() method after constructor
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
        
        // Test setHighestNonEscapedChar() method after constructor
        generator.setHighestNonEscapedChar(256);
        assertEquals(256, generator.getHighestEscapedChar());
        
        // Test setCharacterEscapes() method after constructor
        CharacterEscapes escapes = Mockito.mock(CharacterEscapes.class);
        generator.setCharacterEscapes(escapes);
        assertEquals(escapes, generator.getCharacterEscapes());
        
        // Test setRootValueSeparator() method after constructor
        SerializableString separator = Mockito.mock(SerializableString.class);
        generator.setRootValueSeparator(separator);
        assertEquals(separator, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        assertEquals(JsonGeneratorImpl.class.getPackage().getImplementationVersion(), generator.version().toString());
    }

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);

        // Test with non-null value
        SerializableString fieldName = Mockito.mock(SerializableString.class);
        String value = "test value";
        try {
            generator.writeStringField(fieldName.toString(), value);
        } catch (Exception e) {
            // handle exception
        }

        // Test with null value
        value = null;
        try {
            generator.writeStringField(fieldName.toString(), value);
        } catch (Exception e) {
            // handle exception
        }
    }
}