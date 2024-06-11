
package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {
    @Mock
    private BeanProperty _property;
    
    @Mock
    private AnnotatedMember _setter;
    
    @Mock
    private JavaType _type;
    
    @Mock
    private JsonDeserializer<Object> _valueDeserializer;
    
    @Mock
    private TypeDeserializer _valueTypeDeserializer;
    
    @Mock
    private KeyDeserializer _keyDeserializer;
    
    private SettableAnyProperty settableAnyProperty;
    
    public SettableAnyPropertyTest() {
        MockitoAnnotations.initMocks(this);
        settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, _keyDeserializer, _valueDeserializer, _valueTypeDeserializer);
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        
        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        
        verify(p, times(1)).getCurrentToken();
        verify(_keyDeserializer, times(1)).deserializeKey(propName, ctxt);
        verify(_valueDeserializer, times(1)).deserialize(p, ctxt);
        verifyNoMoreInteractions(p);
        verifyNoMoreInteractions(_keyDeserializer);
        verifyNoMoreInteractions(_valueDeserializer);
    }
    
    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        settableAnyProperty.deserialize(p, ctxt);
        
        verify(p, times(1)).getCurrentToken();
        verify(_valueDeserializer, times(1)).deserialize(p, ctxt);
        verifyNoMoreInteractions(p);
        verifyNoMoreInteractions(_valueDeserializer);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        settableAnyProperty.deserializeWithType(p, ctxt);
        
        verify(p, times(1)).getCurrentToken();
        verify(_valueDeserializer, times(1)).deserializeWithType(p, ctxt, _valueTypeDeserializer);
        verifyNoMoreInteractions(p);
        verifyNoMoreInteractions(_valueDeserializer);
    }
    
    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();
        
        settableAnyProperty.set(instance, propName, value);
        
        verify(_setter, times(1)).callOnWith(instance, propName, value);
        verifyNoMoreInteractions(_setter);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testSetWithException() throws Exception {
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();
        
        doThrow(new Exception()).when(_setter).callOnWith(instance, propName, value);
        
        settableAnyProperty.set(instance, propName, value);
    }
    
    @Test
    public void testFixAccess() throws Exception {
        DeserializationConfig config = mock(DeserializationConfig.class);
        
        settableAnyProperty.fixAccess(config);
        
        verify(_setter, times(1)).fixAccess(anyBoolean());
        verifyNoMoreInteractions(_setter);
    }
}
