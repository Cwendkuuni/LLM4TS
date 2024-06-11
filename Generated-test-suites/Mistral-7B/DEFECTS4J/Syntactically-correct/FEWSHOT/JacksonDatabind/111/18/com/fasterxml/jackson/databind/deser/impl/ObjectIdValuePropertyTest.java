package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        String propertyName = "id";
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        Object id = new Object();
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("123");

        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(objectIdReader, times(1)).getIdType();
        verify(objectIdReader, times(1)).propertyName;
        verify(deserializationContext, times(1)).findObjectId(eq(id), any(), any());
        verify(instance, times(1)).setId(id);

        Object idProperty = verify(objectIdReader, times(1)).idProperty;
        assertEquals(idProperty, property.getIdProperty());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        Object id = new Object();
        Object deserializedValue = new Object();

        when(objectIdReader.propertyName).thenReturn("id");
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.LONG);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
        when(property.getValueDeserializer()).thenReturn(new BeanDeserializerBase() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return deserializedValue;
            }
        });

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER);
        when(jsonParser.getValueAsLong()).thenReturn(123L);

        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(objectIdReader, times(1)).getIdType();
        verify(objectIdReader, times(1)).propertyName;
        verify(deserializationContext, times(1)).findObjectId(eq(id), any(), any());
        verify(instance, times(1)).setId(id);
        verify(objectIdReader, times(1)).idProperty;
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object id = new Object();

        when(objectIdReader.propertyName).thenReturn("id");
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        when(instance.setId(any())).thenReturn(instance);

        Object result = property.setAndReturn(instance, id);

        verify(instance, times(1)).setId(id);
        verify(objectIdReader, times(1)).idProperty;
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object id = new Object();

        when(objectIdReader.propertyName).thenReturn("id");
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        property.set(instance, id);

        verify(instance, times(1)).setId(id);
        verify(objectIdReader, times(1)).idProperty;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();
        Object id = new Object();

        when(objectIdReader.propertyName).thenReturn("id");
        when(objectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.STRING);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(null);

        property.set(instance, id);

        fail("Should throw UnsupportedOperationException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeAndSetWithNullValue() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object instance = new Object();

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        fail("Should throw JsonProcessingException");
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        PropertyName newName = mock(PropertyName.class);

        ObjectIdValueProperty result = property.withName(newName);

        assertEquals(newName, result.getName());
        assertEquals(property, result.getBackReference());
        assertEquals(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdValueProperty result = property.withValueDeserializer(deserializer, nva);

        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(nva, result._nullProvider);
        assertEquals(objectIdReader, result._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdValueProperty result = property.withNullProvider(nva);

        assertEquals(nva, result._nullProvider);
        assertEquals(objectIdReader, result._objectIdReader);
    }
}