package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ValuePropertyAnnotation;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader mockObjectIdReader() {
        ObjectIdReader reader = mock(ObjectIdReader.class);
        when(reader.propertyName).thenReturn(new PropertyName("id"));
        when(reader.getIdType()).thenReturn(Integer.class);
        when(reader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(reader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(reader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        return reader;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("id");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(parser.getValueAsInt()).thenReturn(123);

        Object instance = new Object();
        Object id = property.deserializeAndSet(parser, mock(DeserializationContext.class), instance);

        assertEquals(123, property.get(instance));
        assertEquals(123, property.getMetadata().getSerializedValue(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentName()).thenReturn("id");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(parser.getValueAsInt()).thenReturn(123);

        Object instance = new Object();
        Object id = property.deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);

        assertEquals(123, id);
        assertEquals(123, property.get(instance));
        assertEquals(123, property.getMetadata().getSerializedValue(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        Object instance = new Object();
        Object id = new Integer(123);

        when(property._valueDeserializer).thenReturn(deserializer);
        when(property._nullProvider).thenReturn(nullValueProvider);

        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        when(idProp.setAndReturn(instance, id)).thenReturn(instance);
        when(idProp.getName()).thenReturn("id");

        property.set(instance, id);

        assertEquals(instance, property.get(instance));
        assertEquals(instance, property.getMetadata().getSerializedValue(instance));
        assertEquals(id, property.getMetadata().getSerializedValue(instance, "id"));
        assertEquals("id", property.getName());
    }

    @Test
    public void testAnnotationGet() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(property._valueDeserializer).thenReturn(deserializer);

        JsonId jsonId = new JsonId();
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        ValuePropertyAnnotation valuePropertyAnnotation = mock(ValuePropertyAnnotation.class);
        when(beanPropertyDefinition.getAnnotation(JsonId.class)).thenReturn(jsonId);
        when(beanPropertyDefinition.getValueAnnotation()).thenReturn(valuePropertyAnnotation);

        when(property.getMember()).thenReturn(beanPropertyDefinition);

        assertEquals(jsonId, property.getAnnotation(JsonId.class));
        assertEquals(valuePropertyAnnotation, property.getMember().getValueAnnotation());
    }

    @Test
    public void testNullValue() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        Object id = property.deserializeAndSet(parser, mock(DeserializationContext.class), instance);

        assertNull(id);
        assertNull(property.get(instance));
        assertNull(property.getMetadata().getSerializedValue(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetOnPropertyWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object id = new Integer(123);

        property.set(instance, id);
    }
}