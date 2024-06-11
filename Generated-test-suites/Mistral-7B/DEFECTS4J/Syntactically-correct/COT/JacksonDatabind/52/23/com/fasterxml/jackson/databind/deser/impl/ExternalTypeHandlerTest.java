package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test cases for ExternalTypeHandler class
 */
public class ExternalTypeHandlerTest {

    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private ObjectNode mockObjectNode;
    private ExternalTypeHandler handler;
    private ExternalTypeHandler.Builder builder;

    @Before
    public void setup() {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockObjectNode = mock(ObjectNode.class);
        builder = new ExternalTypeHandler.Builder();
    }

    @Test
    public void testStart() throws IOException {
        Object bean = new Object();
        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        assertNotNull(handler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String typeId = "StringType";
        when(mockParser.getText()).thenReturn(typeId);
        when(mockContext.findDeserializer(String.class)).thenReturn(mock(StringDeserializer.class));
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        Object bean = new Object();
        when(property.getType()).thenReturn(String.class);
        when(property.getName()).thenReturn("typePropertyName");
        when(property.getPropertyName()).thenReturn("typePropertyName");

        boolean result = handler.handleTypePropertyValue(mockParser, mockContext, "typePropertyName", bean);
        assertEquals(true, result);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TypeDeserializer> typeDeserializerCaptor = ArgumentCaptor.forClass(TypeDeserializer.class);
        ArgumentCaptor<JsonParser> parserCaptor = ArgumentCaptor.forClass(JsonParser.class);
        ArgumentCaptor<DeserializationContext> contextCaptor = ArgumentCaptor.forClass(DeserializationContext.class);
        ArgumentCaptor<Object> objectCaptor = ArgumentCaptor.forClass(Object.class);

        handler.handleTypePropertyValue(mockParser, mockContext, "typePropertyName", bean);
        TypeDeserializer typeDeserializer = typeDeserializerCaptor.getValue();
        JsonParser parser = parserCaptor.getValue();
        Object object = objectCaptor.getValue();
        ObjectNode node = mockObjectNode;
        when(node.elements()).thenReturn(Collections.emptyList());
        when(typeDeserializer.createContext(mockContext, null, node)).thenReturn(mockContext);
        when(typeDeserializer.deserialize(mockParser, mockContext)).thenReturn("StringValue");

        TypeDeserializer deserializer = handler.getHandleTypePropertyValueHandler();
        deserializer.handlePropertyValue(mockParser, mockContext, "typePropertyName", bean);

        verify(property).set(bean, "StringValue");
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String propertyName = "propertyName";
        String typeId = "StringType";
        when(mockParser.getText()).thenReturn(typeId);
        when(mockContext.findDeserializer(String.class)).thenReturn(mock(StringDeserializer.class));
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        Object bean = new Object();
        when(property.getType()).thenReturn(String.class);
        when(property.getName()).thenReturn(propertyName);
        when(property.getPropertyName()).thenReturn("propertyName");

        boolean result = handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);
        assertEquals(true, result);

        ArgumentCaptor<JsonParser> parserCaptor = ArgumentCaptor.forClass(JsonParser.class);
        ArgumentCaptor<DeserializationContext> contextCaptor = ArgumentCaptor.forClass(DeserializationContext.class);
        ArgumentCaptor<Object> objectCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<String> typeIdCaptor = ArgumentCaptor.forClass(String.class);

        handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);
        handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);

        verify(property).set(bean, "StringType");
    }

    @Test
    public void testComplete() throws IOException {
        Object bean = new Object();
        String propertyName = "propertyName";
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn(propertyName);
        when(property.getType()).thenReturn(String.class);
        when(property.getPropertyName()).thenReturn("propertyName");

        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);
        handler.handleTypePropertyValue(mockParser, mockContext, propertyName, bean);

        handler.complete(mockParser, mockContext, bean);
        verify(property).set(bean, "StringType");
    }

    @Test
    public void testComplete_PropertyBasedCreator() throws IOException {
        Object bean = new Object();
        Object propertyBasedCreator = mock(PropertyBasedCreator.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);

        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, "propertyName", bean);
        handler.handlePropertyValue(mockParser, mockContext, "propertyName", bean);

        when(propertyBasedCreator.build(mockContext, buffer)).thenReturn(new Object());

        handler.complete(mockParser, mockContext, buffer, propertyBasedCreator);
        Object result = handler.getHandlePropertyValueHandler().getProperty().get(bean);
        assertEquals("StringType", result);
    }

    @Test
    public void testComplete_NullTypeId() throws IOException {
        Object bean = new Object();
        when(mockParser.getText()).thenReturn(null);
        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, "propertyName", bean);
        handler.handlePropertyValue(mockParser, mockContext, "propertyName", bean);

        handler.complete(mockParser, mockContext, bean);
        // No exception should be thrown
    }

    @Test
    public void testComplete_MissingProperty() throws IOException {
        Object bean = new Object();
        String propertyName = "propertyName";
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn(propertyName);
        when(property.getType()).thenReturn(String.class);
        when(property.getPropertyName()).thenReturn("propertyName");

        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, propertyName + "_missed", bean);
        handler.handleTypePropertyValue(mockParser, mockContext, propertyName, bean);
        handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);

        handler.complete(mockParser, mockContext, bean);
        verify(property, never()).set(bean, any(String.class));
    }

    @Test(expected = IOException.class)
    public void testComplete_NullBean() throws IOException {
        Object bean = null;
        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, "propertyName", bean);
        handler.handlePropertyValue(mockParser, mockContext, "propertyName", bean);

        handler.complete(mockParser, mockContext, bean);
    }

    @Test(expected = IOException.class)
    public void testComplete_NullTypeIdAndMissingProperty() throws IOException {
        Object bean = null;
        String propertyName = "propertyName";
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn(propertyName);
        when(property.getType()).thenReturn(String.class);
        when(property.getPropertyName()).thenReturn("propertyName");

        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, propertyName + "_missed", bean);
        handler.handleTypePropertyValue(mockParser, mockContext, propertyName, bean);
        handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);

        handler.complete(mockParser, mockContext, bean);
        verify(property, never()).set(bean, any(String.class));
    }

    @Test(expected = IOException.class)
    public void testComplete_NullTypeIdAndNullBean() throws IOException {
        Object bean = null;
        String propertyName = "propertyName";
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn(propertyName);
        when(property.getType()).thenReturn(String.class);
        when(property.getPropertyName()).thenReturn("propertyName");

        handler = new ExternalTypeHandler(builder.build(), null, null, null).start();
        handler.handlePropertyValue(mockParser, mockContext, propertyName + "_missed", bean);
        handler.handleTypePropertyValue(mockParser, mockContext, propertyName, bean);
        handler.handlePropertyValue(mockParser, mockContext, propertyName, bean);

        handler.complete(mockParser, mockContext, bean);
    }
}