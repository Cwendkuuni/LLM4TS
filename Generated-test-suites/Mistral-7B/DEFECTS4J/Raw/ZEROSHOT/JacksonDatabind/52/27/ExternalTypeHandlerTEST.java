package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static class TestTypeDeserializer extends TypeDeserializer {
        private static final String PROP_NAME = "testProp";
        private static final String TYPE_ID = "testTypeId";

        public TestTypeDeserializer() {
            super(String.class);
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd)
                throws IOException {
            return p.getText();
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText();
        }

        @Override
        public String getPropertyName() {
            return PROP_NAME;
        }

        @Override
        public String getTypeId() {
            return TYPE_ID;
        }

        @Override
        public String getTypeIdResolverVersion() {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestHandler extends ExternalTypeHandler {
        private TestTypeDeserializer typeDeserializer;

        public TestHandler(SettableBeanProperty property) {
            super(new ExtTypedProperty(property, typeDeserializer = new TestTypeDeserializer()));
        }

        @Override
        public Object complete(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
            return super.complete(p, ctxt, bean);
        }

        @Override
        public Object complete(JsonParser p, DeserializationContext ctxt, PropertyValueBuffer buffer,
                PropertyBasedCreator creator) throws IOException {
            return super.complete(p, ctxt, buffer, creator);
        }

        @Override
        protected Object _deserialize(JsonParser p, DeserializationContext ctxt, int index, String typeId)
                throws IOException {
            return super._deserialize(p, ctxt, index, typeId);
        }

        @Override
        protected void _deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, int index,
                String typeId) throws IOException {
            super._deserializeAndSet(p, ctxt, bean, index, typeId);
        }
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TestHandler handler = new TestHandler(property);

        when(p.getText()).thenReturn("testTypeId");
        when(property.getName()).thenReturn("testPropertyName");

        handler.handleTypePropertyValue(p, ctxt, "testPropertyName", bean);

        assertEquals("testTypeId", handler.getTypeIds()[0]);
        assertNull(handler.getTokens()[0]);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TestHandler handler = new TestHandler(property);

        when(p.getText()).thenReturn("testTypeId");
        when(property.getName()).thenReturn("testPropertyName");

        handler.handlePropertyValue(p, ctxt, "testPropertyName", bean);

        assertEquals("testTypeId", handler.getTypeIds()[0]);
        assertNull(handler.getTokens()[0]);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testComplete() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TestHandler handler = new TestHandler(property);

        when(p.getText()).thenReturn("testTypeId");

        handler.handlePropertyValue(p, ctxt, "testPropertyName", bean);
        handler.handlePropertyValue(p, ctxt, "testPropertyName", bean);

        Object result = handler.complete(p, ctxt, bean);

        assertEquals(bean.getClass(), result.getClass());
        assertEquals("testTypeId", property.get(result));
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testCompleteWithBufferAndCreator() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TestHandler handler = new TestHandler(property);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        when(p.getText()).thenReturn("testTypeId");

        handler.handlePropertyValue(p, ctxt, "testPropertyName", bean);
        handler.handlePropertyValue(p, ctxt, "testPropertyName", bean);

        Object result = handler.complete(p, ctxt, buffer, creator);

        assertEquals(bean.getClass(), result.getClass());
        assertEquals("testTypeId", property.get(result));
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TestHandler handler = new TestHandler(null);

        when(p.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getText()).thenReturn("typeId");
        when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn("testTypeId");
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = handler.complete(p, ctxt, null);

        assertEquals("testTypeId", result);
    }

    @SuppressWarnings("resource")
    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        TestHandler handler = new TestHandler(mock(SettableBeanProperty.class));

        when(p.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getText()).thenReturn("typeId");
        when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn("testTypeId");
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        handler.handlePropertyValue(p, ctxt, "typeId", bean);

        assertEquals("testTypeId", handler.getTypeIds()[0]);
        assertEquals("testTypeId", handler.getProperty().get(bean));
    }

    @org.junit.Test
    public void testBuilder() {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        List<ExtTypedProperty> properties = new ArrayList<>();
        Map<String, Integer> nameToPropertyIndex = new HashMap<>();

        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TestTypeDeserializer typeDeserializer = new TestTypeDeserializer();

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();

        builder.addExternal(property, typeDeserializer);

        ExternalTypeHandler handler = builder.build();

        assertEquals(1, handler.getProperties().length);
        assertEquals(property, handler.getProperties()[0]);
        assertEquals(1, handler.getNameToPropertyIndex().size());
        assertEquals(property.getName(), handler.getNameToPropertyIndex().get("testProp"));
        assertEquals(TYPE_ID, handler.getNameToPropertyIndex().get(TYPE_ID));
    }
}