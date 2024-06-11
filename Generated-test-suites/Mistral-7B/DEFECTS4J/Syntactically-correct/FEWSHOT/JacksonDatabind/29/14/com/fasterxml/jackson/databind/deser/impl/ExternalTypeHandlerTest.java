package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))}, new HashMap<>(), new String[0], new TokenBuffer[0]);

        when(jp.getText()).thenReturn("typeId");

        String propName = "propertyName";
        Integer index = 0;
        ExtTypedProperty prop = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        when(prop.getTypePropertyName()).thenReturn(propName);

        handler._nameToPropertyIndex.put(propName, index);

        // True case
        assertTrue(handler.handleTypePropertyValue(jp, ctxt, propName, bean));

        // False case
        String invalidPropName = "invalidPropertyName";
        assertFalse(handler.handleTypePropertyValue(jp, ctxt, invalidPropName, bean));

        // Null bean
        assertNull(handler.handleTypePropertyValue(jp, ctxt, propName, null));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))}, new HashMap<>(), new String[0], new TokenBuffer[0]);

        String propName = "propertyName";
        Integer index = 0;
        SettableBeanProperty prop = mock(SettableBeanProperty.class);
        when(prop.getName()).thenReturn(propName);
        when(prop.getType()).thenReturn(String.class);
        when(prop.deserialize).thenReturn("deserializedValue");

        handler._nameToPropertyIndex.put(propName, index);

        TokenBuffer tokens = mock(TokenBuffer.class);
        when(jp.getParserConfig()).thenReturn(new StandardParserConfig());
        when(tokens.asParser(jp)).thenReturn(tokens);

        handler._tokens[index] = tokens;

        String typeId = "typeId";
        when(jp.getText()).thenReturn(typeId);

        handler.handlePropertyValue(jp, ctxt, propName, bean);

        assertEquals("deserializedValue", bean.getClass().getField("propertyName").get(bean));
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))}, new HashMap<>(), new String[0], new TokenBuffer[0]);

        String propName = "propertyName";
        Integer index = 0;
        SettableBeanProperty prop = mock(SettableBeanProperty.class);
        when(prop.getName()).thenReturn(propName);
        when(prop.getType()).thenReturn(String.class);

        handler._nameToPropertyIndex.put(propName, index);

        String typeId = "typeId";

        TokenBuffer tokens = mock(TokenBuffer.class);
        when(tokens.firstToken()).thenReturn(JsonToken.START_OBJECT);
        when(tokens.asParser(jp)).thenReturn(tokens);

        handler._tokens[index] = tokens;

        handler.complete(jp, ctxt, bean);

        assertEquals("deserializedValue", bean.getClass().getField("propertyName").get(bean));
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))}, new HashMap<>(), new String[0], new TokenBuffer[0]);

        String propName = "propertyName";
        Integer index = 0;
        SettableBeanProperty prop = mock(SettableBeanProperty.class);
        when(prop.getName()).thenReturn(propName);
        when(prop.getType()).thenReturn(String.class);
        when(prop.deserialize).thenReturn("deserializedValue");

        handler._nameToPropertyIndex.put(propName, index);

        String typeId = "typeId";

        TokenBuffer tokens = mock(TokenBuffer.class);
        when(tokens.firstToken()).thenReturn(JsonToken.START_OBJECT);
        when(tokens.asParser(jp)).thenReturn(tokens);

        handler._tokens[index] = tokens;

        when(creator.build(ctxt, buffer)).thenReturn(bean);

        handler.complete(jp, ctxt, buffer, creator);

        assertEquals("deserializedValue", bean.getClass().getField("propertyName").get(bean));
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        int index = 0;
        String typeId = "typeId";

        ExtTypedProperty prop = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        when(settableBeanProperty.getName()).thenReturn("propertyName");
        when(prop.getProperty()).thenReturn(settableBeanProperty);

        JsonParser p2 = mock(JsonParser.class);
        JsonToken t = mock(JsonToken.class);
        when(p2.nextToken()).thenReturn(t);

        TokenBuffer merged = mock(TokenBuffer.class);
        when(merged.asParser(jp)).thenReturn(p2);

        handler = new ExternalTypeHandler(new ExtTypedProperty[]{prop}, new HashMap<>(), new String[0], new TokenBuffer[0]);

        handler._properties[index] = prop;

        handler._deserializeAndSet(jp, ctxt, bean, index, typeId);

        assertEquals("deserializedValue", bean.getClass().getField("propertyName").get(bean));
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        builder.addExternal(property, typeDeserializer);

        ExternalTypeHandler handler = builder.build();

        assertEquals(1, handler._properties.size());
        assertEquals(property, handler._properties.get(0));
    }
}