package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
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
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext ctxt;
    private Object bean;

    @Before
    public void setUp() throws IOException {
        parser = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        bean = mock(Object.class);
        List<ExtTypedProperty> properties = new ArrayList<>();
        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                nameToPropertyIndex, null, null).start();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "prop1";
        String typeId = "type1";
        String jsonString = "{\"prop1\":\"type1\"}";

        when(parser.getText()).thenReturn(typeId);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        handler.handleTypePropertyValue(parser, ctxt, propName, bean);

        assertNotNull(handler._typeIds);
        assertNotNull(handler._tokens);
        assertEquals(1, handler._typeIds.length);
        assertEquals(1, handler._tokens.length);
        assertEquals(typeId, handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String propName = "prop1";
        String jsonString = "{\"prop1\":\"value1\"}";

        when(parser.getText()).thenReturn("value1");
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("value1");
        when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        handler.handlePropertyValue(parser, ctxt, propName, bean);

        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @Test
    public void testComplete() throws IOException {
        String propName1 = "prop1";
        String typeId1 = "type1";
        String propName2 = "prop2";
        String typeId2 = "type2";

        ExtTypedProperty property1 = mock(ExtTypedProperty.class);
        ExtTypedProperty property2 = mock(ExtTypedProperty.class);

        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn(propName1);
        when(property.getType()).thenReturn(String.class);

        SettableBeanProperty typeProperty1 = mock(SettableBeanProperty.class);
        SettableBeanProperty typeProperty2 = mock(SettableBeanProperty.class);
        when(typeProperty1.getName()).thenReturn(typeId1);
        when(typeProperty2.getName()).thenReturn(typeId2);

        when(property.getPropertyName()).thenReturn(propName1);
        when(property.getType()).thenReturn(Boolean.class);

        when(property1.getProperty()).thenReturn(property);
        when(property1.getTypeProperty()).thenReturn(typeProperty1);

        when(property2.getProperty()).thenReturn(property);
        when(property2.getTypeProperty()).thenReturn(typeProperty2);

        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(propName1, 0);
        nameToPropertyIndex.put(typeId1, 0);
        nameToPropertyIndex.put(propName2, 1);
        nameToPropertyIndex.put(typeId2, 1);

        TypeDeserializer typeDeserializer1 = mock(TypeDeserializer.class);
        TypeDeserializer typeDeserializer2 = mock(TypeDeserializer.class);

        when(typeDeserializer1.getPropertyName()).thenReturn(typeId1);
        when(typeDeserializer2.getPropertyName()).thenReturn(typeId2);
        when(typeDeserializer1.getDefaultImpl()).thenReturn(Boolean.class);
        when(typeDeserializer2.getDefaultImpl()).thenReturn(Integer.class);

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                nameToPropertyIndex, new String[] {typeId1, typeId2}, new TokenBuffer[2]).start();

        handler.handleTypePropertyValue(parser, ctxt, propName1, bean);
        handler.handlePropertyValue(parser, ctxt, propName1, bean);
        handler.handlePropertyValue(parser, ctxt, propName2, bean);

        when(parser.getText()).thenReturn(typeId1);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn(propName1);

        handler.complete(parser, ctxt, bean);

        SettableBeanProperty beanProperty1 = property;
        Object beanValue1 = Boolean.TRUE;
        beanProperty1.set(bean, beanValue1);

        when(parser.getText()).thenReturn(typeId2);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn(propName2);

        handler.complete(parser, ctxt, bean);

        SettableBeanProperty beanProperty2 = property;
        Object beanValue2 = Integer.valueOf(42);
        beanProperty2.set(bean, beanValue2);

        assertNotNull(beanValue1);
        assertNotNull(beanValue2);
        assertEquals(Boolean.TRUE, beanValue1);
        assertEquals(Integer.valueOf(42), beanValue2);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        String propName1 = "prop1";
        String typeId1 = "type1";
        String propName2 = "prop2";
        String typeId2 = "type2";

        ExtTypedProperty property1 = mock(ExtTypedProperty.class);
        ExtTypedProperty property2 = mock(ExtTypedProperty.class);

        SettableBeanProperty property = mock(SettableBeanProperty.class);
        SettableBeanProperty typeProperty1 = mock(SettableBeanProperty.class);
        SettableBeanProperty typeProperty2 = mock(SettableBeanProperty.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);

        when(property.getName()).thenReturn(propName1);
        when(property.getType()).thenReturn(String.class);

        when(property1.getProperty()).thenReturn(property);
        when(property1.getTypeProperty()).thenReturn(typeProperty1);

        when(property2.getProperty()).thenReturn(property);
        when(property2.getTypeProperty()).thenReturn(typeProperty2);

        when(typeProperty1.getName()).thenReturn(typeId1);
        when(typeProperty2.getName()).thenReturn(typeId2);

        when(property.getPropertyName()).thenReturn(propName1);
        when(property.getType()).thenReturn(Boolean.class);

        when(typeProperty1.getProperty()).thenReturn(property);
        when(typeProperty1.getTypeProperty()).thenReturn(typeProperty1);

        when(typeProperty2.getProperty()).thenReturn(property);
        when(typeProperty2.getTypeProperty()).thenReturn(typeProperty2);

        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put(propName1, 0);
        nameToPropertyIndex.put(typeId1, 0);
        nameToPropertyIndex.put(propName2, 1);
        nameToPropertyIndex.put(typeId2, 1);

        TypeDeserializer typeDeserializer1 = mock(TypeDeserializer.class);
        TypeDeserializer typeDeserializer2 = mock(TypeDeserializer.class);

        when(typeDeserializer1.getPropertyName()).thenReturn(typeId1);
        when(typeDeserializer2.getPropertyName()).thenReturn(typeId2);
        when(typeDeserializer1.getDefaultImpl()).thenReturn(Boolean.class);
        when(typeDeserializer2.getDefaultImpl()).thenReturn(Integer.class);

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                nameToPropertyIndex, null, null).start();

        handler.handleTypePropertyValue(parser, ctxt, propName1, bean);
        handler.handlePropertyValue(parser, ctxt, propName1, bean);
        handler.handlePropertyValue(parser, ctxt, propName2, bean);

        when(parser.getText()).thenReturn(typeId1);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn(propName1);

        handler.complete(parser, ctxt, bean, buffer, typeDeserializer1);

        Object beanValue1 = Boolean.TRUE;
        buffer.assignParameter(property, beanValue1);

        when(parser.getText()).thenReturn(typeId2);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn(propName2);

        handler.complete(parser, ctxt, bean, buffer, typeDeserializer2);

        Object beanValue2 = Integer.valueOf(42);
        buffer.assignParameter(typeProperty2, beanValue2);

        assertNotNull(beanValue1);
        assertNotNull(beanValue2);
        assertEquals(Boolean.TRUE, beanValue1);
        assertEquals(Integer.valueOf(42), beanValue2);
    }
}