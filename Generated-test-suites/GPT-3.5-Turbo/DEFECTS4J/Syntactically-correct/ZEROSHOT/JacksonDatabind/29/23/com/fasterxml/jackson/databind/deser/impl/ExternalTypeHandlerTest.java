package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = new Object();

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = new Object();

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();

        Object result = externalTypeHandler.complete(p, ctxt, bean);

        assertSame(bean, result);
    }

    @Test
    public void testComplete2() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;

        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        assertNotNull(result);
    }

    @Test
    public void test_deserialize() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "typeId";

        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);

        assertNotNull(result);
    }

    @Test
    public void test_deserializeAndSet() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        int index = 0;
        String typeId = "typeId";

        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testExtTypedProperty_hasTypePropertyName_true() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        String n = "n";
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        boolean result = extTypedProperty.hasTypePropertyName(n);

        assertTrue(result);
    }

    @Test
    public void testExtTypedProperty_hasTypePropertyName_false() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        String n = "n";
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        boolean result = extTypedProperty.hasTypePropertyName("");

        assertFalse(result);
    }

    @Test
    public void testExtTypedProperty_hasDefaultType_true() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        boolean result = extTypedProperty.hasDefaultType();

        assertTrue(result);
    }

    @Test
    public void testExtTypedProperty_hasDefaultType_false() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        typeDeser.setDefaultImpl(null);
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        boolean result = extTypedProperty.hasDefaultType();

        assertFalse(result);
    }

    @Test
    public void testExtTypedProperty_getDefaultTypeId() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Class<?> defaultType = null;
        typeDeser.setDefaultImpl(defaultType);
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        String result = extTypedProperty.getDefaultTypeId();

        assertNull(result);
    }

    @Test
    public void testExtTypedProperty_getTypePropertyName() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        String result = extTypedProperty.getTypePropertyName();

        assertNotNull(result);
    }

    @Test
    public void testExtTypedProperty_getProperty() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);

        SettableBeanProperty result = extTypedProperty.getProperty();

        assertNotNull(result);
    }
}