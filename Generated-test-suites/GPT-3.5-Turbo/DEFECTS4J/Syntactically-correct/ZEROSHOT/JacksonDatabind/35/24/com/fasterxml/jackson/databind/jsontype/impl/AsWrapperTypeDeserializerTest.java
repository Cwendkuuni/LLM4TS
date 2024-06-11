package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonParser.class, DeserializationContext.class, JsonDeserializer.class})
public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Arrange
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = Mockito.spy(new AsWrapperTypeDeserializer(null, null, null, false, null));
        
        // Act
        deserializer.deserializeTypedFromObject(jp, ctxt);
        
        // Assert
        // Add your assertions here
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Arrange
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = Mockito.spy(new AsWrapperTypeDeserializer(null, null, null, false, null));
        
        // Act
        deserializer.deserializeTypedFromArray(jp, ctxt);
        
        // Assert
        // Add your assertions here
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Arrange
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = Mockito.spy(new AsWrapperTypeDeserializer(null, null, null, false, null));
        
        // Act
        deserializer.deserializeTypedFromScalar(jp, ctxt);
        
        // Assert
        // Add your assertions here
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Arrange
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = Mockito.spy(new AsWrapperTypeDeserializer(null, null, null, false, null));
        
        // Act
        deserializer.deserializeTypedFromAny(jp, ctxt);
        
        // Assert
        // Add your assertions here
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        // Arrange
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = Mockito.spy(new AsWrapperTypeDeserializer(null, null, null, false, null));
        Object typeId = new Object();
        
        // Act
        deserializer._deserializeWithNativeTypeId(p, ctxt, typeId);
        
        // Assert
        // Add your assertions here
    }
    
    @Test
    public void testDeserialize() throws IOException {
        // Arrange
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = Mockito.spy(new AsWrapperTypeDeserializer(null, null, null, false, null));
        
        // Act
        deserializer._deserialize(p, ctxt);
        
        // Assert
        // Add your assertions here
    }

}