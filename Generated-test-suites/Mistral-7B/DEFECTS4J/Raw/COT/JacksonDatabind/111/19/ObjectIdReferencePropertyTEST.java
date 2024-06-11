package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanPropertyTest;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private SettableBeanProperty settableBeanPropertyMock;
    private ObjectIdInfo objectIdInfoMock;
    private JsonDeserializer<?> jsonDeserializerMock;
    private UnresolvedForwardReference unresolvedForwardReferenceMock;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        settableBeanPropertyMock = mock(SettableBeanProperty.class);
        objectIdInfoMock = mock(ObjectIdInfo.class);
        jsonDeserializerMock = mock(JsonDeserializer.class);
        unresolvedForwardReferenceMock = mock(UnresolvedForwardReference.class);
        objectMapper = new ObjectMapper();

        MockitoAnnotations.initMocks(this);

        objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
        objectIdReferenceProperty.withValueDeserializer(jsonDeserializerMock);
    }

    @Test
    public void testConstructorWithSettableBeanProperty() {
        Object instance = mock(Object.class);
        assertEquals(settableBeanPropertyMock, objectIdReferenceProperty.getBackRef());
        assertEquals(objectIdInfoMock, objectIdReferenceProperty.getObjectIdInfo());
        assertEquals(jsonDeserializerMock, objectIdReferenceProperty.getValueDeserializer());
        assertEquals(settableBeanPropertyMock, objectIdReferenceProperty.getPropertyName());
        assertEquals(instance.getClass(), objectIdReferenceProperty.getType());
    }

    @Test
    public void testConstructorWithObjectIdReferenceProperty() {
        ObjectIdReferenceProperty src = mock(ObjectIdReferenceProperty.class);
        Object instance = mock(Object.class);

        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(src, PropertyName.getInstance("newName"));

        assertEquals(src, objectIdReferenceProperty.getBackRef());
        assertEquals(src._forward, objectIdReferenceProperty._forward);
        assertEquals(src._objectIdInfo, objectIdReferenceProperty._objectIdInfo);
        assertEquals(src._valueDeserializer, objectIdReferenceProperty._valueDeserializer);
        assertEquals(PropertyName.getInstance("newName"), objectIdReferenceProperty.getPropertyName());
        assertEquals(instance.getClass(), objectIdReferenceProperty.getType());
    }

    @Test
    public void testConstructorWithJsonDeserializerNullValueProvider() {
        Object instance = mock(Object.class);

        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, jsonDeserializerMock, null);

        assertEquals(jsonDeserializerMock, objectIdReferenceProperty._valueDeserializer);
        assertNull(objectIdReferenceProperty._nullProvider);
        assertEquals(instance.getClass(), objectIdReferenceProperty.getType());
    }

    @Test
    public void testConstructorWithPropertyName() {
        Object instance = mock(Object.class);
        PropertyName newName = PropertyName.getInstance("newName");

        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, newName);

        assertEquals(newName, objectIdReferenceProperty.getPropertyName());
        assertNull(objectIdReferenceProperty._forward);
        assertNull(objectIdReferenceProperty._objectIdInfo);
        assertNull(objectIdReferenceProperty._valueDeserializer);
        assertEquals(instance.getClass(), objectIdReferenceProperty.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullInstance() throws IOException {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
        JsonParser jsonParserMock = mock(JsonParser.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);

        when(jsonParserMock.getValueAsToken()).thenReturn(null);

        objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueDeserializer() throws IOException {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
        JsonParser jsonParserMock = mock(JsonParser.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(jsonParserMock.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock, instance.getClass())).thenThrow(IOException.class);

        objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance);
    }

    @Test
    public void testDeserializeAndSetWithNonNullValueDeserializer() throws IOException {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
        JsonParser jsonParserMock = mock(JsonParser.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        when(jsonParserMock.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock, instance.getClass())).thenReturn(value);

        when(settableBeanPropertyMock.setAndReturn(instance, value)).thenReturn(value);

        assertEquals(value, objectIdReferenceProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException, JsonProcessingException {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanPropertyMock, objectIdInfoMock);
        JsonParser jsonParserMock = mock(JsonParser.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        UnresolvedForwardReference unresolvedForwardReference = mock(UnresolvedForwardReference.class);
        when(jsonParserMock.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock, instance.getClass())).thenThrow(UnresolvedForwardReference.class);

        Object id = mock(Object.class);
        when(unresolvedForwardReference.getRoid()).thenReturn(id);

        objectIdReferenceProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instance);
    }
}