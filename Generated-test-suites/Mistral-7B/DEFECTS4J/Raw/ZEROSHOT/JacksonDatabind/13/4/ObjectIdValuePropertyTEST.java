package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private JsonDeserializer<ObjectIdNode> valueDeserializer;
    private AnnotatedMember annotatedMember;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        valueDeserializer = mock(JsonDeserializer.class);
        annotatedMember = mock(AnnotatedMember.class);
        when(objectIdReader.idProperty).thenReturn(new StubSettableBeanProperty());

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
            private static final long serialVersionUID = 1L;

            @Override
            public JsonDeserializer<?> getValueDeserializer() {
                return valueDeserializer;
            }
        };
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(ObjectIdProperty.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdNode idNode = mock(ObjectIdNode.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(idNode);
        when(objectIdReader.generator.fromString("id")).thenReturn(new ObjectIdGenerator.ObjectIdGeneratorBase() {
            private static final long serialVersionUID = 1L;

            @Override
            public ObjectId generateId(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "id";
            }
        });
        when(objectIdReader.resolver.findPropertyId("idProperty")).thenReturn("idProperty");

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(annotatedMember).set(argumentCaptor.capture(instance), argumentCaptor.capture(idNode));

        assertEquals("id", argumentCaptor.getAllValues().get(1).toString());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdNode idNode = mock(ObjectIdNode.class);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(idNode);

        Object returnValue = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, returnValue);
    }

    @Test
    public void testSet() throws Exception {
        Object id = new Object();
        Object instance = new Object();

        objectIdValueProperty.set(instance, id);

        verify(annotatedMember).set(instance, id);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object id = new Object();
        Object instance = new Object();

        Object returnValue = objectIdValueProperty.setAndReturn(instance, id);

        assertEquals(instance, returnValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetShouldNotCallSet() throws Exception {
        Object instance = new Object();

        objectIdValueProperty.set(instance, new Object());
    }

    private static class StubSettableBeanProperty extends SettableBeanProperty {
        private static final long serialVersionUID = 1L;

        StubSettableBeanProperty() {
            super("idProperty", Object.class, null, null);
        }

        @Override
        public ObjectIdValueProperty withName(PropertyName newName) {
            throw new UnsupportedOperationException("Should not call withName() on StubSettableBeanProperty");
        }

        @Override
        public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
            throw new UnsupportedOperationException("Should not call withValueDeserializer() on StubSettableBeanProperty");
        }

        @Override
        public void setAndReturn(Object instance, Object value) throws IOException {
            throw new UnsupportedOperationException("Should not call setAndReturn() on StubSettableBeanProperty");
        }
    }
}