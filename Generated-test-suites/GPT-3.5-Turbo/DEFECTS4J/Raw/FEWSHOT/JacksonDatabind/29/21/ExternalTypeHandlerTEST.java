package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class ExternalTypeHandlerTest {
    
    private ExternalTypeHandler externalTypeHandler;
    
    @Before
    public void setup() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property1 = null; //initialize property1
        TypeDeserializer typeDeserializer1 = null; //initialize typeDeserializer1
        builder.addExternal(property1, typeDeserializer1);
        SettableBeanProperty property2 = null; //initialize property2
        TypeDeserializer typeDeserializer2 = null; //initialize typeDeserializer2
        builder.addExternal(property2, typeDeserializer2);
        
        externalTypeHandler = builder.build();
    }
    
    @Test
    public void testHandleTypePropertyValue() {
        JsonParser jp = null; //initialize jp
        DeserializationContext ctxt = null; //initialize ctxt
        String propName = null; //initialize propName
        Object bean = null; //initialize bean
        
        assertFalse(externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean));
    }
    
    @Test
    public void testHandlePropertyValue() {
        JsonParser jp = null; //initialize jp
        DeserializationContext ctxt = null; //initialize ctxt
        String propName = null; //initialize propName
        Object bean = null; //initialize bean
        
        assertFalse(externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean));
    }
    
    @Test
    public void testComplete() {
        JsonParser p = null; //initialize p
        DeserializationContext ctxt = null; //initialize ctxt
        Object bean = null; //initialize bean
        
        assertNotNull(externalTypeHandler.complete(p, ctxt, bean));
    }
    
    @Test
    public void testCompleteWithCreator() {
        JsonParser jp = null; //initialize jp
        DeserializationContext ctxt = null; //initialize ctxt
        PropertyValueBuffer buffer = null; //initialize buffer
        PropertyBasedCreator creator = null; //initialize creator
        
        assertNotNull(externalTypeHandler.complete(jp, ctxt, buffer, creator));
    }
}