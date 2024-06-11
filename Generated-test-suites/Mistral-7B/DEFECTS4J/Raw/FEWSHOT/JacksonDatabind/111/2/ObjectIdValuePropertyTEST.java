package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;

public class ObjectIdValuePropertyTest {

    private static ObjectIdReader createMockObjectIdReader() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        when(mockObjectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(mockObjectIdReader.getIdType()).thenReturn(ObjectIdReader.Type.LONG);
        when(mockObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(mockObjectIdReader.idProperty).thenReturn(null);
        when(mockObjectIdReader.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        return mockObjectIdReader;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = createMockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectInstance context = mock(DeserializationContext.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getValueAsLong()).thenReturn(1L);
        when(jsonParser.getValueAsString()).thenReturn("1");
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextFieldName()).thenReturn("id");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getValueAsLong()).thenReturn(1L);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object instance = new Object();
        when(objectIdValueProperty.deserializer.deserialize(jsonParser, context)).thenReturn(1L);

        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

        assertEquals(1L, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        ObjectIdReader objectIdReader = createMockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectInstance context = mock(DeserializationContext.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        objectIdValueProperty.deserializeAndSet(jsonParser, context, null);

        assertNull(nullInstance.getClass().getDeclaredField("id").get(nullInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = createMockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectInstance context = mock(DeserializationContext.class);
        JsonParser jsonParser = mock(JsonParser.class);
        Object instance = new Object();
        Object value = 1L;
        when(jsonParser.getValueAsLong()).thenReturn((Long) value);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        when(jsonParser.nextFieldName()).thenReturn("id");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getValueAsLong()).thenReturn((Long) value);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        when(objectIdValueProperty.deserialize(jsonParser, context)).thenReturn(value);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);

        assertEquals(value, result);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = createMockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object value = 1L;

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertEquals(value, result);
        assertEquals(value, instance.getClass().getDeclaredField("id").get(instance));
    }
}