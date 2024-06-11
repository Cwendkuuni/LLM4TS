package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() throws IOException {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);

        // Typical use case
        generator.writeStringField("fieldName", "fieldValue");
        
        // Edge case: fieldName is null
        assertThrows(NullPointerException.class, () -> generator.writeStringField(null, "fieldValue"));

        // Edge case: value is null
        generator.writeStringField("fieldName", null);
    }
    
    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        
        // Typical use case
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(127, generator.getHighestEscapedChar());
        
        // Edge case: enabling same feature twice
        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertEquals(127, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        
        // Typical use case
        CharacterEscapes esc = new CharacterEscapes();
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator.getCharacterEscapes());

        // Edge case: setting null character escapes
        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        
        // Typical use case
        generator.setRootValueSeparator(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);
        
        // Edge case: setting null separator
        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);
    }

}