package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    private static final String propertyName = "objectId";

    @Test
    public void testConstruction() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertSame(objectIdReader, property._objectIdReader);
        assertNull(property._valueDeserializer);
        assertNull(_nullProvider);
        assertEquals(propertyName, property.getName());
        assertEquals(Object.class, property.getType());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertSame(property, newProperty);
        assertEquals("newName", newProperty.getName().asString());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty newProperty = property.withNullProvider(nullValueProvider);
        assertSame(property, newProperty);
        assertSame(nullValueProvider, newProperty._nullProvider);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withValueDeserializer(valueDeserializer);
        assertSame(property, newProperty);
        assertSame(valueDeserializer, newProperty._valueDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        JsonId jsonIdAnnotation = mock(JsonId.class);
        ObjectNode testNode = mock(ObjectNode.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        when(objectMapper.findValueToDeserialize(testNode, JsonToken.FIELD_NAME)).thenReturn("objectId");
        when(objectMapper.findValueToDeserialize(testNode, JsonToken.VALUE_NULL)).thenReturn(null);
        when(objectMapper.findValueToDeserialize(testNode, JsonToken.START_OBJECT)).thenReturn(jsonIdAnnotation);
        when(AnnotationIntrospector.findAnnotation(property, JsonId.class)).thenReturn(jsonIdAnnotation);
        when(jsonIdAnnotation.value()).thenReturn("someId");

        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(AnnotationIntrospector.findMember(property)).thenReturn(annotatedMember);

        Object id = property.getAnnotation(JsonId.class);
        assertSame(jsonIdAnnotation, id);
        verify(jsonIdAnnotation).value();
        verify(annotatedMember).getMemberName();
    }

    @Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        when(AnnotationIntrospector.findMember(property)).thenReturn(annotatedMember);

        assertSame(annotatedMember, property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object idValue = new Object();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        doReturn(idValue).when(objectIdReader).getValueId(instance, null);
        doReturn(null).when(objectIdReader).getIdType();
        doReturn(new JsonDeserializer.NullValueProvider()).when(objectIdReader).getNullValueProvider();

        Object id = property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertSame(idValue, id);
        verify(instance).setId(idValue);
        verify(objectIdReader).bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, idValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object idValue = new Object();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        doReturn(idValue).when(objectIdReader).getValueId(instance, null);
        doReturn(null).when(objectIdReader).getIdType();
        doReturn(new JsonDeserializer.NullValueProvider()).when(objectIdReader).getNullValueProvider();

        Object id = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertSame(idValue, id);
        verify(instance).setId(idValue);
        verify(objectIdReader).bindItem(instance);
        verify(objectIdReader.idProperty).setAndReturn(instance, idValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object idValue = new Object();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        PropertyName idPropertyName = new PropertyName("id");
        ObjectIdValueProperty idProperty = mock(ObjectIdValueProperty.class);
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        when(idProperty.getName()).thenReturn(idPropertyName);
        when(idProperty.getType()).thenReturn(Object.class);
        when(idProperty.setAndReturn(instance, idValue)).thenReturn(instance);
        doReturn(idProperty).when(objectIdReader).getIdProperty();

        Object id = property.setAndReturn(instance, idValue);
        assertSame(idValue, id);
        verify(instance).setId(idValue);
        verify(idProperty).setAndReturn(instance, idValue);
    }

    @Test
    public void testSet() throws IOException {
        Object idValue = new Object();
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        PropertyName idPropertyName = new PropertyName("id");
        ObjectIdValueProperty idProperty = mock(ObjectIdValueProperty.class);
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        when(idProperty.getName()).thenReturn(idPropertyName);
        when(idProperty.getType()).thenReturn(Object.class);
        when(idProperty.setAndReturn(instance, idValue)).thenReturn(instance);
        doReturn(idProperty).when(objectIdReader).getIdProperty();

        property.set(instance, idValue);
        verify(instance).setId(idValue);
        verify(idProperty).setAndReturn(instance, idValue);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object idValue = new Object();
        doReturn(idValue).when(objectIdReader).getValueId(instance, null);
        doReturn(new JsonSerializer.StdSerializer<>()).when(property).getSerializer();
        doReturn(new ObjectNode(new ObjectMapper())).when(property).createContextualValueContainer(JsonParser.Feature.WRITE_NULL_AS_EMPTY_JSON_ARRAY, null);
        doReturn(JsonToken.VALUE_STRING).when(mock(JsonParser.class)).nextToken();
        doReturn("someId").when(property).serializeValue(instance, null, null, null);

        ObjectNode resultNode = property.serializeValues(mock(JsonParser.class), null, null, null);
        assertSame(resultNode, property.createContextualValueContainer(JsonParser.Feature.WRITE_NULL_AS_EMPTY_JSON_ARRAY, null).getValueAsTree());
        verify(property).serializeValue(instance, null, null, null);
    }
}