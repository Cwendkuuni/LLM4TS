package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        
        Object result = deserializer.deserialize(parser, context);
        
        Assert.assertNull(result);
        Mockito.verify(parser).skipChildren();
    }
    
    @Test
    public void testDeserializeWithType_startArray() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        Assert.assertNull(result);
        Mockito.verify(typeDeserializer).deserializeTypedFromAny(parser, context);
    }
    
    @Test
    public void testDeserializeWithType_startObject() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        Assert.assertNull(result);
        Mockito.verify(typeDeserializer).deserializeTypedFromAny(parser, context);
    }
    
    @Test
    public void testDeserializeWithType_fieldName() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        Assert.assertNull(result);
        Mockito.verify(typeDeserializer).deserializeTypedFromAny(parser, context);
    }
    
    @Test
    public void testDeserializeWithType_defaultCase() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_OBJECT);
        
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        Assert.assertNull(result);
    }
}