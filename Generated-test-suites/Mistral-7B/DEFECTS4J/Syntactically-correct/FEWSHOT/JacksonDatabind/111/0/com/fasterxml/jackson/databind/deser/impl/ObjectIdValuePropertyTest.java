package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader mockObjectIdReader() {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn("id");
        when(objectIdReader.getIdType()).thenReturn(Long.TYPE);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException {
                jgen.writeString(value);
            }
        }.withResolvedAnnotation(new JsonId()));
        return objectIdReader;
    }

    @Test
    public void testObjectIdValueProperty() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(Long.TYPE, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testWithName() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
                .withName(new PropertyName("newName"));
        assertEquals("newName", newObjectIdValueProperty.getName());
        assertEquals("id", objectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
                .withValueDeserializer(jsonDeserializer);
        assertEquals(jsonDeserializer, newObjectIdValueProperty._valueDeserializer);
        assertNull(objectIdValueProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty
                .withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newObjectIdValueProperty._nullProvider);
        assertNull(objectIdValueProperty._nullProvider);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().createObjectNode();
        jsonNode.put("id", "123");
        JsonParser jsonParser = constructParser(jsonNode);
        Object instance = new Object();
        Object id = objectIdValueProperty.deserializeAndSet(jsonParser, null, instance);
        assertEquals("123", id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().createObjectNode();
        jsonNode.put("id", "123");
        JsonParser jsonParser = constructParser(jsonNode);
        Object instance = new Object();
        Object id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, null, instance);
        assertEquals("123", id);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        Object instance = new Object();
        Object id = "123";
        objectIdValueProperty.set(instance, id);
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mockObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader,
                PropertyMetadata.empty());
        Object instance = new Object();
        Object id = "123";
        Object result = objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(instance, result);
    }
}