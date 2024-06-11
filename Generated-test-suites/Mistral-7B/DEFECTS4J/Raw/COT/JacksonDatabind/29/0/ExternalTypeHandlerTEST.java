package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

/**
 * Test class for ExternalTypeHandler
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": \"type1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "prop1", bean));
        assertEquals("type1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_noTypeProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": \"value1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        assertFalse(handler.handlePropertyValue(jp, ctxt, "prop1", bean));
    }

    @Test
    public void testHandlePropertyValue_hasTypeProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"typeProperty\": \"type1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        assertTrue(handler.handlePropertyValue(jp, ctxt, "typeProperty", bean));
        assertNull(handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_missingProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"missingProperty\": \"value1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler.handlePropertyValue(jp, ctxt, "missingProperty", bean);
        fail("Expected IOException");
    }

    @Test
    public void testComplete_noMissingData() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": \"type1\", \"prop2\": 2}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler.complete(jp, ctxt, bean);
    }

    @Test(expected = IOException.class)
    public void testComplete_missingTypeId() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": \"type1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler.complete(jp, ctxt, bean);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testComplete_missingProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"typeId\": \"type1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler.complete(jp, ctxt, bean);
        fail("Expected IOException");
    }

    @Test
    public void testComplete_naturalType() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", Number.class), new NumberTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": 123}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler.complete(jp, ctxt, bean);
        assertEquals(123, handler._properties[0].getProperty().get(bean));
    }

    @Test
    public void testComplete_defaultType() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": null}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler.complete(jp, ctxt, bean);
        assertNull(handler._properties[0].getProperty().get(bean));
    }

    @Test
    public void testComplete_creatorProperties() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .addExternal(new SettableBeanProperty("prop2", Integer.class), new IntTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"prop1\": \"type1\", \"prop2\": 2}");
        DeserializationContext ctxt = new DeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                Object bean = new Object();
                SettableBeanProperty prop1 = handler._properties[0].getProperty();
                SettableBeanProperty prop2 = handler._properties[1].getProperty();
                prop1.set(bean, handler._properties[0].getProperty().deserialize(jp, ctxt));
                prop2.set(bean, handler._properties[1].getProperty().deserialize(jp, ctxt));
                return bean;
            }
        };
        Object bean = handler.complete(jp, ctxt, buffer, creator);
        assertEquals("type1", handler._properties[0].getProperty().get(bean));
        assertEquals(2, handler._properties[1].getProperty().get(bean));
    }

    @Test
    public void testDeserialize() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"typeId\": \"type1\", \"value\": \"value1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object result = handler._deserialize(jp, ctxt, 0, "type1");
        assertEquals("value1", result);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandlerTest.Builder()
                .addExternal(new SettableBeanProperty("prop1", String.class), new StringTypeDeserializer())
                .build();
        JsonParser jp = createParser("{\"typeId\": \"type1\", \"value\": \"value1\"}");
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        handler._deserializeAndSet(jp, ctxt, bean, 0, "type1");
        assertEquals("value1", handler._properties[0].getProperty().get(bean));
    }

    private JsonParser createParser(String json) throws IOException {
        StringReader reader = new StringReader(json);
        ObjectCodec oc = new ObjectMapper().getFactory().createObjectCodec();
        return oc.getParser(reader);
    }

    private static class StringTypeDeserializer extends StdScalarDeserializer {
        public StringTypeDeserializer() { super(String.class); }
        @Override public String getPropertyName() { return "stringProperty"; }
    }

    private static class IntTypeDeserializer extends StdScalarDeserializer {
        public IntTypeDeserializer() { super(Integer.class); }
        @Override public String getPropertyName() { return "integerProperty"; }
    }

    private static class NumberTypeDeserializer extends StdScalarDeserializer {
        public NumberTypeDeserializer() { super(Number.class); }
        @Override public String getPropertyName() { return "numberProperty"; }
    }
}

private static class PropertyBasedCreator {
    public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public PropertyValueBuffer getParameterValues(DeserializationContext ctxt) {
        throw new UnsupportedOperationException();
    }
    
    public boolean hasEmptyConstructor() {
        return false;
    }
    
    public boolean hasGetter() {
        return false;
    }
    
    public boolean hasSetter() {
        return false;
    }
    
    public void setValueInstantiator(ValueInstantiator instantiator) {}
}