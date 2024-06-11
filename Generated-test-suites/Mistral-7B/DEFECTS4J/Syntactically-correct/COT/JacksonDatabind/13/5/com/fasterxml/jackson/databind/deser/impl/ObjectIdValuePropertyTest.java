package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdIdentifier;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.STRING) {
            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    @Override
                    public ObjectId generateId(SerializerProvider serializers) throws IOException {
                        return new ObjectIdIdentifier("testId");
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId findId(Object id, ObjectIdGenerator generator) throws IOException {
                        if (id instanceof ObjectIdIdentifier) {
                            return (ObjectIdIdentifier) id;
                        }
                        return null;
                    }
                };
            }
        };
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testEmptyConstructor() {
        assertNull(objectIdValueProperty.getMetadata());
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.getName());
    }

    @Test
    public void testConstructorWithMetadata() {
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertSame(metadata, property.getMetadata());
        assertNull(property.getValueDeserializer());
        assertSame(objectIdReader.propertyName, property.getName());
    }

    @Test
    public void testConstructorWithDeserializer() {
        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, deserializer);
        assertSame(deserializer, property.getValueDeserializer());
        assertSame(objectIdReader.propertyName, property.getName());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new ObjectIdIdentifier("testId");
        Object testInstance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.deserializeAndSet(mockContext.parser(), mockContext, testInstance);
        ObjectIdIdentifier deserializedId = (ObjectIdIdentifier) mockContext.parser().getValueAsToken();
        assertSame(id, deserializedId);
        ObjectId idObject = property.getObjectId(testInstance);
        assertSame(id, idObject);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new ObjectIdIdentifier("testId");
        Object testInstance = new Object();
        Object returnValue = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object result = property.deserializeSetAndReturn(mockContext.parser(), mockContext, testInstance);
        assertSame(testInstance, result);
        ObjectId idObject = property.getObjectId(testInstance);
        assertSame(id, idObject);
        Object deserializedId = mockContext.parser().getValueAsToken();
        assertSame(id, deserializedId);
        Object deserializedReturnValue = property.getValueDeserializer().deserialize(mockContext.parser(), mockContext);
        assertSame(returnValue, deserializedReturnValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullId() throws IOException {
        Object testInstance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.set(testInstance, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithIdPropertyNull() throws IOException {
        Object testInstance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdReader idReader = new ObjectIdReader("idPropertyName", ObjectIdReader.IdType.LONG) {
            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };
        ObjectIdValueProperty propertyWithIdReader = new ObjectIdValueProperty(idReader, property);
        propertyWithIdReader.set(testInstance, new ObjectIdIdentifier("testId"));
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        Object testInstance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser jp = mockContext.parser();
        jp.getValueAsToken(); // consume start object
        jp.nextToken(); // consume key
        jp.nextToken(); // consume value start object
        jp.nextToken(); // consume null
        jp.nextToken(); // consume end object
        property.deserializeAndSet(jp, mockContext, testInstance);
    }
}