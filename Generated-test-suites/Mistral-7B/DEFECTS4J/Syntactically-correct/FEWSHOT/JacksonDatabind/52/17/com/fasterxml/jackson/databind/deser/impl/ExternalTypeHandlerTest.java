package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.*;
import java.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = createHandlerWithProperties();
        Object bean = new Object();
        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        String propertyName = "typeProperty";
        String typeId = "TypeId";
        handler.handleTypePropertyValue(p, ctxt, propertyName, bean);

        assertEquals(typeId, handler.getTypeIds()[0]);
        assertNull(handler.getTokens()[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = createHandlerWithProperties();
        Object bean = new Object();
        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        String propertyName = "property";
        String typeId = "TypeId";
        handler.handlePropertyValue(p, ctxt, propertyName, bean);

        assertEquals(typeId, handler.getTypeIds()[0]);
        assertNull(handler.getTokens()[0]);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = createHandlerWithProperties();
        Object bean = new Object();
        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        handler.complete(p, ctxt, bean);

        for (int i = 0; i < handler.getProperties().length; i++) {
            SettableBeanProperty prop = handler.getProperties()[i].getProperty();
            assertNotNull(prop.get(bean));
        }
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        ExternalTypeHandler handler = createHandlerWithPropertiesAndCreator();
        Object bean = new Object();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                return new Object();
            }
        };

        handler.complete(p, ctxt, buffer, creator);

        for (int i = 0; i < handler.getProperties().length; i++) {
            SettableBeanProperty prop = handler.getProperties()[i].getProperty();
            assertNotNull(prop.get(bean));
        }
    }

    @Test
    public void testMissingProperty() throws IOException {
        ExternalTypeHandler handler = createHandlerWithProperties();
        Object bean = new Object();
        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        String propertyName = "missingProperty";

        handler.handlePropertyValue(p, ctxt, propertyName, bean);
        handler.complete(p, ctxt, bean);

        assertFalse(handler.getTypeIds()[0] != null);
        assertNull(handler.getTokens()[0]);
    }

    @Test
    public void testMissingTypeId() throws IOException {
        ExternalTypeHandler handler = createHandlerWithProperties();
        Object bean = new Object();
        JsonParser p = createParser();
        DeserializationContext ctxt = createContext();

        String propertyName = "typeProperty";
        handler.handleTypePropertyValue(p, ctxt, propertyName, bean);
        handler.complete(p, ctxt, bean);

        assertNull(handler.getTypeIds()[0]);
        assertNull(handler.getTokens()[0]);
    }

    private ExternalTypeHandler createHandlerWithProperties() {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder() {
            @Override
            public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser) {
                if (property.getName().equals("typeProperty")) {
                    property.addAnnotation(new JsonTypeInfo.As(JsonTypeInfo.As.EXTERNAL_PROPERTY));
                } else if (property.getName().equals("property")) {
                    property.addAnnotation(new JsonTypeInfo.As(JsonTypeInfo.As.EXTERNAL_PROPERTY));
                }
                super.addExternal(property, typeDeser);
            }
        }.build());
        return handler;
    }

    private ExternalTypeHandler createHandlerWithPropertiesAndCreator() {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder() {
            @Override
            public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser) {
                if (property.getName().equals("typeProperty")) {
                    property.addAnnotation(new JsonTypeInfo.As(JsonTypeInfo.As.EXTERNAL_PROPERTY));
                    property.addAnnotation(JsonTypeInfo.As.CREATOR_PROPERTY);
                } else if (property.getName().equals("property")) {
                    property.addAnnotation(new JsonTypeInfo.As(JsonTypeInfo.As.EXTERNAL_PROPERTY));
                }
                super.addExternal(property, typeDeser);
            }

            @Override
            public ExternalTypeHandler build() {
                ExternalTypeHandler builder = super.build();
                SettableBeanProperty typeProp = findProperty("typeProperty", builder.getProperties());
                SettableBeanProperty creatorProp = findProperty("creatorProperty", builder.getProperties());
                builder.getProperties()[0].linkTypeProperty(creatorProp);
                builder.getProperties()[1].linkTypeProperty(typeProp);
                return builder;
            }
        }.build());
        return handler;
    }

    private JsonParser createParser() throws IOException {
        String json = "[{\"typeProperty\":\"TypeId1\",\"property\":\"Value1\"},{\"typeProperty\":\"TypeId2\",\"property\":\"Value2\"}]";
        ObjectReader reader = new ObjectMapper().readerForListOf(Object.class);
        JsonParser p = reader.readValue(new StringReader(json));
        return p;
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getContext();
    }

    private SettableBeanProperty findProperty(String propertyName, ExtTypedProperty[] properties) {
        for (ExtTypedProperty property : properties) {
            if (property.getProperty().getName().equals(propertyName)) {
                return (SettableBeanProperty) property.getProperty();
            }
        }
        return null;
    }
}