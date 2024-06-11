package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private static Object mapperSetup() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser jp = mapper.getFactory().createParser("{\"a\":\"StringType\",\"b\":{}}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "a", bean);
        assertEquals("StringType", handler._typeIds[0]);

        jp.nextToken(); // move past b

        handler.handleTypePropertyValue(jp, ctxt, "b.c", bean);
        assertNull(handler._typeIds[1]);

        jp.nextToken(); // move past b

        handler.handleTypePropertyValue(jp, ctxt, "d", bean);
        assertNull(handler._typeIds[2]);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser jp = mapper.getFactory().createParser("{\"prop1\":1,\"prop2\":[\"str1\",\"str2\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        assertNull(handler._typeIds[0]);

        handler.handlePropertyValue(jp, ctxt, "prop2[0]", bean);
        assertEquals("str1", handler._typeIds[1]);

        handler.handlePropertyValue(jp, ctxt, "prop2[1]", bean);
        assertEquals("str2", handler._typeIds[2]);
    }

    @Test
    public void testComplete() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser jp = mapper.getFactory().createParser("{\"prop1\":1,\"prop2\":[\"str1\",\"str2\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2[0]", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2[1]", bean);
        handler.complete(jp, ctxt, bean);

        assertEquals(3, handler._properties.length);
        ExtTypedProperty prop1 = handler._properties.get(0);
        ExtTypedProperty prop2Str1 = handler._properties.get(1);
        ExtTypedProperty prop2Str2 = handler._properties.get(2);

        assertNotNull(prop1.getProperty().getName());
        assertEquals("prop1", prop1.getProperty().getName());
        assertNull(prop1._typeIds);
        assertNull(prop1._tokens);

        assertNotNull(prop2Str1.getProperty().getName());
        assertEquals("prop2[0]", prop2Str1.getProperty().getName());
        assertEquals("str1", prop2Str1._typeIds[0]);
        assertNull(prop2Str1._tokens);

        assertNotNull(prop2Str2.getProperty().getName());
        assertEquals("prop2[1]", prop2Str2.getProperty().getName());
        assertEquals("str2", prop2Str2._typeIds[0]);
        assertNull(prop2Str2._tokens);
    }

    @Test
    public void testCompleteWithCreator() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        JsonParser jp = mapper.getFactory().createParser("{\"prop1\":1,\"prop2\":[\"str1\",\"str2\"]}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();

        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.handlePropertyValue(jp, ctxt, "prop2[0]", null);
        handler.handlePropertyValue(jp, ctxt, "prop2[1]", null);
        Object result = handler.complete(jp, ctxt, buffer, creator);

        assertEquals(3, handler._properties.length);
        ExtTypedProperty prop1 = handler._properties.get(0);
        ExtTypedProperty prop2Str1 = handler._properties.get(1);
        ExtTypedProperty prop2Str2 = handler._properties.get(2);

        verify(creator).build(ctxt, buffer);
        for (int i = 0; i < handler._properties.length; i++) {
            SettableBeanProperty prop = handler._properties[i].getProperty();
            if (creator.findCreatorProperty(prop.getName()) != null) {
                assertEquals(prop.getType().getName(), buffer.get(prop.getName()).getClass().getName());
            } else {
                assertEquals(prop.getType().getName(), handler._properties[i]._typeIds[0]);
            }
        }

        assertEquals(result.getClass().getName(), "ResultClass");
    }

    @Test
    public void testMissingTypeId() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser jp = mapper.getFactory().createParser("{\"prop1\":1}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);

        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (JsonMappingException ex) {
            assertEquals("Missing property 'prop2' for external type id 'null'", ex.getMessage());
        }
    }

    @Test
    public void testMissingProperty() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser jp = mapper.getFactory().createParser("{\"prop1\":1}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handleTypePropertyValue(jp, ctxt, "prop2", bean);

        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (JsonMappingException ex) {
            assertEquals("Missing property 'prop2' for external type id 'null'", ex.getMessage());
        }
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = mapperSetup();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser jp = mapper.getFactory().createParser("{\"prop1\":null,\"prop2\":null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);
        handler.complete(jp, ctxt, bean);

        assertEquals(2, handler._properties.length);
        ExtTypedProperty prop1 = handler._properties.get(0);
        ExtTypedProperty prop2 = handler._properties.get(1);

        assertNull(prop1._typeIds);
        assertNull(prop1._tokens);
        assertNull(prop2._typeIds);
        assertNull(prop2._tokens);
    }
}