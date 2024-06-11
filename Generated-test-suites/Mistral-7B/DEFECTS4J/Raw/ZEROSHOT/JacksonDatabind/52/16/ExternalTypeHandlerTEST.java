package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {}, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("{\"typeId\": \"String\", \"prop\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "typeId", bean));
        assertEquals("String", handler._typeIds[0]);

        handler.complete(p, ctxt, bean);
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {}, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("{\"prop\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        assertTrue(handler.handlePropertyValue(p, ctxt, "prop", bean));

        handler.complete(p, ctxt, bean);
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandleMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {}, new TokenBuffer[0]);

        JsonParser p = mapper.getFactory().createParser("{\"missingProp\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        handler.handlePropertyValue(p, ctxt, "missingProp", bean);

        handler.complete(p, ctxt, bean);
        assertNull(bean);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {"String"}, new TokenBuffer[1]);

        JsonParser p = mapper.getFactory().createParser("{\"prop\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        handler.handlePropertyValue(p, ctxt, "prop", bean);

        handler.complete(p, ctxt, bean);
        assertNull(bean);
    }

    @Test
    public void testHandleMissingTypeIdAndProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {"String"}, new TokenBuffer[1]);

        JsonParser p = mapper.getFactory().createParser("{\"prop\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        handler.handlePropertyValue(p, ctxt, "prop", bean);

        handler.complete(p, ctxt, bean);
        assertNull(bean);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {"String"}, new TokenBuffer[1]);

        JsonParser p = mapper.getFactory().createParser("{\"typeId\": \"String\", \"prop\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "typeId", bean));

        handler.complete(p, ctxt, bean);

        assertEquals("value", handler._properties[0].getProperty().get(bean));
        assertEquals("String", handler._typeIds[0]);

        handler.complete(p, ctxt, bean);
        assertNull(bean);
    }

    @Test(expected = IOException.class)
    public void testMissingTypeIdProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(mapper), new HashMap<>(),
                new String[] {"String"}, new TokenBuffer[1]);

        JsonParser p = mapper.getFactory().createParser("{\"prop\": \"value\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.start();

        handler.handlePropertyValue(p, ctxt, "prop", bean);

        handler.complete(p, ctxt, bean);
    }

    @Test
    public void testCompleteWithCreatorProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFactory jsonFactory = mapper.getFactory();
        ObjectReader reader = mapper.readerForMap(Map.class);

        ExternalTypeHandler handler = new ExternalTypeHandler(createCreatorProperties(mapper), new HashMap<>(),
                new String[] {"String"}, new TokenBuffer[1]);

        JsonParser p = jsonFactory.createParser("{" +
                "\"typeId\": \"String\"," +
                "\"creator\": {" +
                "\"creatorProp\": \"creatorValue\"," +
                "\"typeIdProp\": \"StringType\"}" +
                "}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.start();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "typeId", bean));

        handler.complete(p, ctxt, bean);

        assertEquals("creatorValue", handler._properties[0].getProperty().get("creator"));
        assertEquals("String", handler._properties[1].getProperty().get("typeIdProp"));
        assertEquals("StringType", handler._typeIds[1]);

        handler.complete(p, ctxt, bean);
        assertSame(bean, handler._properties[2].getProperty().get("bean"));

        handler.complete(p, ctxt, bean);
        assertNull(bean);
    }

    private ExtTypedProperty[] createProperties(ObjectMapper mapper) {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(new SettableBeanProperty(mapper.findWrapperType(Map.class), "prop"),
                new StringDeserializer(mapper.getTypeFactory().constructType(String.class))));
        properties.add(new ExtTypedProperty(new SettableBeanProperty(mapper.findWrapperType(Map.class), "typeId"),
                new StringDeserializer(mapper.getTypeFactory().constructType(String.class))));
        return properties.toArray(new ExtTypedProperty[properties.size()]);
    }

    private ExtTypedProperty[] createCreatorProperties(ObjectMapper mapper) {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(new SettableBeanProperty(mapper.findWrapperType(Map.class), "creatorProp"),
                new StringDeserializer(mapper.getTypeFactory().constructType(String.class))));
        properties.add(new ExtTypedProperty(new SettableBeanProperty(mapper.findWrapperType(Map.class), "typeIdProp"),
                new StringDeserializer(mapper.getTypeFactory().constructType(String.class))));
        properties.add(new ExtTypedProperty(new SettableBeanProperty(mapper.findWrapperType(Map.class), "bean"),
                new MapDeserializer(mapper)));
        return properties.toArray(new ExtTypedProperty[properties.size()]);
    }
}