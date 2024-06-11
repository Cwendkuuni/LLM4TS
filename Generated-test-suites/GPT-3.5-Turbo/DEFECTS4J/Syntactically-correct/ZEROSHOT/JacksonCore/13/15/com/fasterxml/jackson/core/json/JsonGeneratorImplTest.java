package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonGeneratorImplTest {
    
    @Mock
    private IOContext mockContext;
    
    @Mock
    private ObjectCodec mockCodec;
    
    @Mock
    private SerializableString mockSerializableString;
    
    private JsonGeneratorImpl jsonGeneratorImpl;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jsonGeneratorImpl = new JsonGeneratorImpl(mockContext, 0, mockCodec);
    }
    
    @Test
    public void testEnable() {
        JsonGenerator generator = jsonGeneratorImpl.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        verify(mockCodec, atLeastOnce()).disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        verify(mockCodec, atLeastOnce()).enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
    }
    
    @Test
    public void testCheckStdFeatureChanges() {
        jsonGeneratorImpl._checkStdFeatureChanges(0, 0);
        verify(mockCodec, atLeastOnce()).disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        verify(mockCodec, atLeastOnce()).enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        verify(mockCodec, atLeastOnce()).setHighestNonEscapedChar(128);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        jsonGeneratorImpl.getHighestEscapedChar();
        verify(mockCodec, atLeastOnce()).getHighestEscapedChar();
    }
    
    @Test
    public void testSetCharacterEscapes() {
        jsonGeneratorImpl.setCharacterEscapes(mockSerializableString);
        verify(mockCodec, atLeastOnce()).setCharacterEscapes(mockSerializableString);
    }
    
    @Test
    public void testGetCharacterEscapes() {
        jsonGeneratorImpl.getCharacterEscapes();
        verify(mockCodec, atLeastOnce()).getCharacterEscapes();
    }
    
    @Test
    public void testSetRootValueSeparator() {
        jsonGeneratorImpl.setRootValueSeparator(mockSerializableString);
        verify(mockCodec, atLeastOnce()).setRootValueSeparator(mockSerializableString);
    }
    
    @Test
    public void testVersion() {
        jsonGeneratorImpl.version();
        verify(mockCodec, atLeastOnce()).version();
    }
    
    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "value";
        
        jsonGeneratorImpl.writeFieldName(fieldName);
        jsonGeneratorImpl.writeString(value);
        
        verify(mockCodec, atLeastOnce()).writeStringField(fieldName, value);
        verify(mockCodec, atLeastOnce()).writeString(value);
    }
}