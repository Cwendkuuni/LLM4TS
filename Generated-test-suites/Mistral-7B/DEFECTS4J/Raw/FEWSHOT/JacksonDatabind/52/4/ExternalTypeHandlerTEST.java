package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = createParser("{\"type\":\"String\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertTrue(handler.handleTypePropertyValue(p, ctxt, "type", bean));
        assertEquals("String", handler._typeIds[0]);
    }

    @Test
    public void testHandleTypePropertyValue_nullTypeId() throws IOException {
        JsonParser p = createParser("{\"type\":null}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertFalse(handler.handleTypePropertyValue(p, ctxt, "type", bean));
    }

    @Test
    public void testHandleTypePropertyValue_noMatchingProperty() throws IOException {
        JsonParser p = createParser("{\"type\":\"Integer\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertFalse(handler.handleTypePropertyValue(p, ctxt, "type", bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = createParser("{\"myProperty\":\"value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertTrue(handler.handlePropertyValue(p, ctxt, "myProperty", bean));
    }

    @Test
    public void testHandlePropertyValue_noMatchingProperty_nullValue() throws IOException {
        JsonParser p = createParser("{\"myProperty\":null}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertFalse(handler.handlePropertyValue(p, ctxt, "myProperty", bean));
    }

    @Test
    public void testHandlePropertyValue_noMatchingProperty_noTypeId() throws IOException {
        JsonParser p = createParser("{\"myProperty\":[1,2,3]}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertFalse(handler.handlePropertyValue(p, ctxt, "myProperty", bean));
    }

    @Test
    public void testHandlePropertyValue_matchingProperty() throws IOException {
        JsonParser p = createParser("{\"myProperty\":{\"type\":\"Integer\"}}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<Integer>(Integer.class), new IntDeserializer())},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertTrue(handler.handlePropertyValue(p, ctxt, "myProperty", bean));
        assertEquals(Integer.class, handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_matchingProperty_noTypeId() throws IOException {
        JsonParser p = createParser("{\"myProperty\":{\"type\":null}}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdSerializer<Integer>(Integer.class), new IntDeserializer())},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        assertFalse(handler.handlePropertyValue(p, ctxt, "myProperty", bean));
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = createParser("{\"typeProperty\":\"String\",\"property1\":null,\"property2\":\"value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        handler.handlePropertyValue(p, ctxt, "typeProperty", bean);
        handler.handlePropertyValue(p, ctxt, "property1", bean);
        handler.handlePropertyValue(p, ctxt, "property2", bean);
        assertEquals("value", handler.complete(p, ctxt, bean).getClass().getDeclaredField("property2").get(bean));
    }

    @Test
    public void testComplete_missingTypeId() throws IOException {
        JsonParser p = createParser("{\"typeProperty\":null,\"property1\":null,\"property2\":\"value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        handler.handlePropertyValue(p, ctxt, "typeProperty", bean);
        handler.handlePropertyValue(p, ctxt, "property1", bean);
        handler.handlePropertyValue(p, ctxt, "property2", bean);
        assertNotNull(handler.complete(p, ctxt, bean));
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_missingProperty() throws IOException {
        JsonParser p = createParser("{\"typeProperty\":\"String\",\"typeProperty2\":null,\"property2\":\"value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        handler.handlePropertyValue(p, ctxt, "typeProperty", bean);
        handler.handlePropertyValue(p, ctxt, "typeProperty2", bean);
        handler.handlePropertyValue(p, ctxt, "property2", bean);
        handler.complete(p, ctxt, bean);
    }

    @Test
    public void testComplete_nullBean() throws IOException {
        JsonParser p = createParser("{\"typeProperty\":\"String\",\"property1\":null,\"property2\":null}");
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        handler.handlePropertyValue(p, ctxt, "typeProperty", null);
        handler.handlePropertyValue(p, ctxt, "property1", null);
        handler.handlePropertyValue(p, ctxt, "property2", null);
        assertNull(handler.complete(p, ctxt, null));
    }

    @Test
    public void testComplete_noMatchingTypeId() throws IOException {
        JsonParser p = createParser("{\"typeProperty\":\"String\",\"property1\":{\"type\":\"Integer\"},\"property2\":\"value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        handler.handlePropertyValue(p, ctxt, "typeProperty", bean);
        handler.handlePropertyValue(p, ctxt, "property1", bean);
        handler.handlePropertyValue(p, ctxt, "property2", bean);
        assertNull(handler.complete(p, ctxt, bean));
    }

    @Test
    public void testComplete_creatorProperties() throws IOException {
        JsonParser p = createParser("{\"typeProperty\":null,\"creatorProperty\":null,\"property2\":\"value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        Object creator = new Object();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creatorFunc = new PropertyBasedCreator() {
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                return new Object();
            }
        };
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new StdSerializer<String>(String.class), null),
                new ExtTypedProperty(new StdSerializer<Integer>(Integer.class), new IntDeserializer()),
                new ExtTypedProperty(new StdSerializer<String>(String.class), null)},
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.start();
        handler.handlePropertyValue(p, ctxt, "typeProperty", bean);
        handler.handlePropertyValue(p, ctxt, "creatorProperty", bean);
        handler.handlePropertyValue(p, ctxt, "property2", bean);
        handler.complete(p, ctxt, buffer, creatorFunc);
        assertEquals(new Object(), handler.complete(p, ctxt, bean));
    }

    @Test
    public void testBuilder() throws IOException {
        JsonParser p = createParser("{\"property1\":\"value1\",\"property2\":\"value2\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new StdSerializer<String>(String.class), null)
                .addExternal(new StdSerializer<String>(String.class), null)
                .build());
        handler.start();
        handler.handlePropertyValue(p, ctxt, "property1", bean);
        handler.handlePropertyValue(p, ctxt, "property2", bean);
        assertEquals("value2", handler.complete(p, ctxt, bean).getClass().getDeclaredField("property2").get(bean));
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectReader reader = new ObjectMapper().readerFor(Object.class);
        return reader.readValueAsTree(json).asParser();
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        protected JsonProcessingException instantiateException(String msg) {
            return new JsonProcessingException(msg);
        }
    }
}