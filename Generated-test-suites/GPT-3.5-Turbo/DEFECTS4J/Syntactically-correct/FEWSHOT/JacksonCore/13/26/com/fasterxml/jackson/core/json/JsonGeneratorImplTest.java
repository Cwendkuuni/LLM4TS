
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonGeneratorImplTest {
    
    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        assertEquals(features, generator._features);
        assertEquals(codec, generator._objectCodec);
        assertEquals(ctxt, generator._ioContext);
        assertNull(generator._characterEscapes);
        assertEquals(JsonGeneratorImpl.sOutputEscapes, generator._outputEscapes);
        assertFalse(generator._cfgUnqNames);
    }
    
    @Test
    public void testEnableQuoteFieldNames() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        
        assertFalse(generator._cfgUnqNames);
    }
    
    @Test
    public void testEnableEscapeNonAscii() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        generator.enable(Feature.ESCAPE_NON_ASCII);
        
        assertEquals(127, generator._maximumNonEscapedChar);
    }
    
    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        SerializableString sep = new DefaultPrettyPrinter().getRootValueSeparator();
        generator.setRootValueSeparator(sep);
        
        assertEquals(sep, generator._rootValueSeparator);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        int charCode = 127;
        generator.setHighestNonEscapedChar(charCode);
        
        assertEquals(charCode, generator._maximumNonEscapedChar);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        int charCode = 127;
        generator.setHighestNonEscapedChar(charCode);
        
        assertEquals(charCode, generator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        CharacterEscapes esc = new CharacterEscapes();
        generator.setCharacterEscapes(esc);
        
        assertEquals(esc, generator._characterEscapes);
        assertEquals(esc.getEscapeCodesForAscii(), generator._outputEscapes);
    }
    
    @Test
    public void testGetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        CharacterEscapes esc = new CharacterEscapes();
        generator.setCharacterEscapes(esc);
        
        assertEquals(esc, generator.getCharacterEscapes());
    }
    
    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        Version version = VersionUtil.versionFor(JsonGeneratorImpl.class);
        
        assertEquals(version, generator.version());
    }
    
    @Test
    public void testWriteStringField() throws IOException {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        
        String fieldName = "fieldName";
        String value = "value";
        generator.writeStringField(fieldName, value);
        
        assertEquals(fieldName, generator.currentFieldName());
        assertEquals(value, generator.currentSegment());
    }
}
