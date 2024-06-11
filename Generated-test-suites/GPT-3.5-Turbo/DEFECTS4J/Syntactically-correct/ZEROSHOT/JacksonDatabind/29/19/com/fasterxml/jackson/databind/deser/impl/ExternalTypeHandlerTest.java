package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

@RunWith(JUnit4.class)
public class ExternalTypeHandlerTest {

    // Test case for handleTypePropertyValue method
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = new JsonFactory().createParser("testString");
        DeserializationContext ctxt = new MappingJsonFactory().createContext(null, null);
        String propName = "testProp";
        Object bean = new Object();
        boolean result = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    // Test case for handlePropertyValue method
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = new JsonFactory().createParser("testString");
        DeserializationContext ctxt = new MappingJsonFactory().createContext(null, null);
        String propName = "testProp";
        Object bean = new Object();
        boolean result = handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    // Test case for complete method
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("testString");
        DeserializationContext ctxt = new MappingJsonFactory().createContext(null, null);
        Object bean = new Object();
        Object result = handler.complete(p, ctxt, bean);
        assertEquals(bean, result);
    }

    // Test case for complete method with call to build method
    @Test
    public void testCompleteWithBuild() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = new JsonFactory().createParser("testString");
        DeserializationContext ctxt = new MappingJsonFactory().createContext(null, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer(ctxt, null);
        PropertyBasedCreator creator = new PropertyBasedCreator(null);
        Object result = handler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }

    // Test case for _deserialize method
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("testString");
        DeserializationContext ctxt = new MappingJsonFactory().createContext(null, null);
        int index = 0;
        String typeId = "testType";
        Object result = handler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }

    // Test case for _deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("testString");
        DeserializationContext ctxt = new MappingJsonFactory().createContext(null, null);
        Object bean = new Object();
        int index = 0;
        String typeId = "testType";
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    // Test case for Builder class
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty(null, null, null, null, null, false, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null);
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        assertNotNull(handler);
    }

    // Test case for ExtTypedProperty class
    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = new SettableBeanProperty(null, null, null, null, null, false, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null);
        ExternalTypeHandler.ExtTypedProperty extTyProp = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertNotNull(extTyProp);
        boolean result = extTyProp.hasTypePropertyName("testProp");
        assertFalse(result);
        result = extTyProp.hasDefaultType();
        assertFalse(result);
        String defaultTypeId = extTyProp.getDefaultTypeId();
        assertNull(defaultTypeId);
        String typePropName = extTyProp.getTypePropertyName();
        assertNull(typePropName);
        SettableBeanProperty resultProperty = extTyProp.getProperty();
        assertEquals(property, resultProperty);
    }
}