package com.example.junit.test.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;
    private JsonDeserializer<ObjectIdNode> objectIdDeserializer;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        objectIdDeserializer = mock(JsonDeserializer.class);
        deserializationContext = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);

        when(objectIdReader.getIdType()).thenReturn(ObjectIdNode.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.idProperty).thenReturn(new StubSettableBeanProperty());
        when(objectIdReader.generator).thenReturn(new StubObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new StubObjectIdResolver());

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.setValueDeserializer(objectIdDeserializer);
    }

    @Test
    public void testEmptyDeserialization() throws IOException {
        // When deserialization is empty
        when(jsonParser.nextToken()).thenReturn(null);

        // Then deserializeAndSet method returns null
        Object result = objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, null);
        assertNull(result);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        // When deserialization returns null
        when(objectIdDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(null);

        // Then deserializeAndSet method returns null
        Object result = objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, null);
        assertNull(result);
    }

    @Test
    public void testDeserializationWithObjectId() throws IOException {
        // Given deserialization returns ObjectIdNode
        ObjectIdNode objectIdNode = ObjectIdNode.parser().parse("123");
        when(objectIdDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(objectIdNode);

        // When deserialization is performed
        Object result = objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, null);

        // Then result should be the same as ObjectIdNode
        assertEquals(objectIdNode, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethod() throws IOException {
        // Given instance to be set
        Object instance = new Object();

        // When set method is called
        objectIdValueProperty.set(instance, "test");

        // Then exception should be thrown
    }

    @Test
    public void testSetAndReturnMethod() throws IOException {
        // Given instance to be set and return value
        Object instance = new Object();
        Object returnValue = new Object();
        SettableBeanProperty idProp = new StubSettableBeanProperty();

        when(idProp.setAndReturn(instance, "test")).thenReturn(returnValue);

        // When setAndReturn method is called
        Object result = objectIdValueProperty.setAndReturn(instance, "test");

        // Then result should be the same as returnValue
        assertEquals(returnValue, result);
    }

    static class StubObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectId generateId(SerializerProvider serializers, BeanDescription beanDesc, JsonGenerator gen)
                throws IOException {
            return null;
        }

        @Override
        public void serializeId(JsonGenerator gen, SerializerProvider serializers, Object id) throws IOException {
            gen.writeObject(id);
        }
    }

    static class StubObjectIdResolver extends JsonDeserializer<ObjectId> {
        @Override
        public Object idFromTree(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    static class StubSettableBeanProperty extends SettableBeanProperty {
        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return value;
        }
    }
}