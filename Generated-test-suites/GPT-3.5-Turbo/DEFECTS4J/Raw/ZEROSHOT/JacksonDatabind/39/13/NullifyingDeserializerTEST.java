package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private TypeDeserializer typeDeserializer;

    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        verify(jsonParser).skipChildren();
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_arrayOrObject() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        Object result1 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        Object result2 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        verify(typeDeserializer, times(2)).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals(null, result1);
        assertEquals(null, result2);
    }
    
    @Test
    public void testDeserializeWithType_fieldName() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeWithType_default() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_OBJECT);
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertEquals(null, result);
    }
}