package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.*;
import java.util.*;

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static ExternalTypeHandler createHandler(ExtTypedProperty[] properties,
            HashMap<String, Integer> nameToPropertyIndex) {
        String[] typeIds = new String[properties.length];
        TokenBuffer[] tokens = new TokenBuffer[properties.length];
        return new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testHandleTypePropertyValueSuccess() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"property1\":\"type1\"}");
        assertTrue(handler.handleTypePropertyValue(parser, context, "property1", null));
        assertEquals("type1", handler._typeIds[0]);
        assertEquals(property1, handler._properties[0]);
    }

    @org.junit.Test
    public void testHandleTypePropertyValueFailure() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"property3\":\"type1\"}");
        assertFalse(handler.handleTypePropertyValue(parser, context, "property3", null));
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testHandlePropertyValueSuccess() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"property1\":\"value1\"}");
        assertTrue(handler.handlePropertyValue(parser, context, "property1", null));
        assertEquals("value1", handler._tokens[0].toString());
        assertEquals(property1, handler._properties[0]);
    }

    @org.junit.Test
    public void testHandlePropertyValueFailure() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"property3\":\"value1\"}");
        assertFalse(handler.handlePropertyValue(parser, context, "property3", null));
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testCompleteSuccess() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{" +
                "\"property1\":\"type1\"," +
                "\"property2\":\"value2\"" +
                "}");
        Object result = handler.complete(parser, context, null);
        assertEquals(2, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertEquals("value2", handler._tokens[1].getStringValue());
        assertEquals(property1, handler._properties[0]);
        assertEquals(property2, handler._properties[1]);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testCompleteMissingProperty() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{" +
                "\"property2\":\"value2\"" +
                "}");
        handler.complete(parser, context, null);
        fail("Expected JsonMappingException");
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testCompleteMissingTypeId() throws IOException {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty[] properties = {property1, property2};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        ObjectBeanContext context = new ObjectMapper().getFactory().createContext();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{" +
                "\"property1\":\"type1\"" +
                "}");
        handler.complete(parser, context, null);
        fail("Expected JsonMappingException");
    }

    @org.junit.Test
    public void testBuilder() {
        ExtTypedProperty property1 = new ExtTypedProperty(new StdHandler(), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new StdHandler(), null);
        TypeDeserializer typeDeserializer = new StdDeserializer();
        SettableBeanProperty beanProperty1 = new StdSettableBeanProperty();
        SettableBeanProperty beanProperty2 = new StdSettableBeanProperty();
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(beanProperty1, typeDeserializer);
        builder.addExternal(beanProperty2, typeDeserializer);
        ExternalTypeHandler handler = builder.build();

        assertEquals(2, handler._properties.length);
        assertEquals(property1, handler._properties[0]);
        assertEquals(property2, handler._properties[1]);
    }
}