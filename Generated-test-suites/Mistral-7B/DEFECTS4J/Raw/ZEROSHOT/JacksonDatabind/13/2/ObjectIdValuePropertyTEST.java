package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private JsonDeserializer<Object> idDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Mock
    private SettableBeanProperty idProperty;

    @InjectMocks
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdNode.class);
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        when(objectIdReader.idProperty).thenReturn(idProperty);
    }

    @Test
    public void testConstructors() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(ObjectIdNode.class, objectIdValueProperty.getType());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty.getMemberBinding());
    }

    @Test
    public void testGetAnnotation() {
        assertEquals(null, objectIdValueProperty.getAnnotation(JsonDeserialize.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(null, objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String idValue = "123";
        Object id = new ObjectIdNode(idValue);
        when(idDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        when(deserializationContext.findObjectId(id, mock(ObjectIdGenerator.class), mock(ObjectIdResolver.class))).thenReturn(new ReadableObjectId() {
            @Override
            public void bindItem(Object item) {
                // no-op
            }
        });

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertEquals(instance, objectIdValueProperty.getMemberBinding().getBindValue());
        assertEquals(idValue, objectIdProperty.get(instance).asText());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String idValue = "123";
        Object id = new ObjectIdNode(idValue);
        when(idDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(id);
        when(deserializationContext.findObjectId(id, mock(ObjectIdGenerator.class), mock(ObjectIdResolver.class))).thenReturn(new ReadableObjectId() {
            @Override
            public void bindItem(Object item) {
                // no-op
            }
        });

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(instance, result);
        assertEquals(idValue, objectIdProperty.get(instance).asText());
    }

    @Test
    public void testSet() throws IOException {
        String idValue = "123";
        ObjectIdNode idNode = new ObjectIdNode(idValue);
        when(idProperty.setAndReturn(instance, idNode)).thenReturn(instance);

        objectIdValueProperty.set(instance, idNode);
        assertEquals(instance, objectIdValueProperty.getMemberBinding().getBindValue());
        assertEquals(idValue, objectIdProperty.get(instance).asText());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String idValue = "123";
        ObjectIdNode idNode = new ObjectIdNode(idValue);
        when(idProperty.setAndReturn(instance, idNode)).thenReturn(instance);

        Object result = objectIdValueProperty.setAndReturn(instance, idNode);
        assertEquals(instance, result);
        assertEquals(idValue, objectIdProperty.get(instance).asText());
    }
}