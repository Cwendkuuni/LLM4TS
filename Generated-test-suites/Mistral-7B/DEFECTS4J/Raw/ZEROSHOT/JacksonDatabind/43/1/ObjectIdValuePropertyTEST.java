package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("objectId"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdGenerator.class);
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Mockito.any(Annotation.class)));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException, IOException {
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        ObjectIdReader objectIdReaderForTest = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return ObjectIdGenerator.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return objectIdGenerator;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return objectIdResolver;
            }

            @Override
            public String getPropertyName() {
                return "objectId";
            }
        };
        StdSerializer<Object> objectDeserializer = mock(StdSerializer.class);
        ObjectIdValueProperty objectIdValuePropertyForTest = new ObjectIdValueProperty(objectIdReaderForTest, PropertyMetadata.empty(), objectDeserializer);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(objectIdGenerator.generateId()).thenReturn("testId");
        when(objectIdResolver.findId(instance)).thenReturn(null);

        doAnswer(invocation -> {
            Object id = invocation.getArgument(0).deserialize(jsonParser, deserializationContext);
            return id;
        }).when(objectDeserializer).deserialize(jsonParser, deserializationContext);

        objectIdValuePropertyForTest.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(objectIdResolver).findId(instance);
        verify(objectIdGenerator).generateId();
        verify(objectDeserializer).deserialize(jsonParser, deserializationContext);

        ObjectId id = mock(ObjectId.class);
        when(id.bindItem(instance)).thenReturn(instance);
        when(objectIdReaderForTest.getGenerator()).thenReturn(id);
        when(objectIdReaderForTest.idProperty).thenReturn(mock(SettableBeanProperty.class));

        objectIdValuePropertyForTest.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(objectIdReaderForTest.getGenerator()).times(2).bindItem(instance);
        verify(objectIdReaderForTest.idProperty).setAndReturn(instance, "testId");
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException, IOException {
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        ObjectIdReader objectIdReaderForTest = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return ObjectIdGenerator.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return objectIdGenerator;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return objectIdResolver;
            }

            @Override
            public String getPropertyName() {
                return "objectId";
            }
        };
        StdSerializer<Object> objectDeserializer = mock(StdSerializer.class);
        ObjectIdValueProperty objectIdValuePropertyForTest = new ObjectIdValueProperty(objectIdReaderForTest, PropertyMetadata.empty(), objectDeserializer);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(objectIdGenerator.generateId()).thenReturn("testId");
        when(objectIdResolver.findId(instance)).thenReturn(null);

        doAnswer(invocation -> {
            Object id = invocation.getArgument(0).deserialize(jsonParser, deserializationContext);
            return id;
        }).when(objectDeserializer).deserialize(jsonParser, deserializationContext);

        Object result = objectIdValuePropertyForTest.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(objectIdResolver).findId(instance);
        verify(objectIdGenerator).generateId();
        verify(objectDeserializer).deserialize(jsonParser, deserializationContext);

        ObjectId id = mock(ObjectId.class);
        when(id.bindItem(instance)).thenReturn(instance);
        when(objectIdReaderForTest.getGenerator()).thenReturn(id);
        when(objectIdReaderForTest.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectId idResult = objectIdValuePropertyForTest.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(objectIdReaderForTest.getGenerator()).times(2).bindItem(instance);
        verify(objectIdReaderForTest.idProperty).setAndReturn(instance, "testId");
        assertEquals(idResult, instance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        ObjectIdReader objectIdReaderForTest = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return ObjectIdGenerator.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return objectIdGenerator;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return objectIdResolver;
            }

            @Override
            public String getPropertyName() {
                return "objectId";
            }
        };
        ObjectIdValueProperty objectIdValuePropertyForTest = new ObjectIdValueProperty(objectIdReaderForTest, PropertyMetadata.empty());

        Object instance = new Object();
        Object id = new Object();

        doThrow(new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty"))
                .when(objectIdValuePropertyForTest).setAndReturn(instance, null);

        objectIdValuePropertyForTest.set(instance, id);

        verify(objectIdValuePropertyForTest).setAndReturn(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsUnsupportedOperationException() throws IOException {
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        ObjectIdReader objectIdReaderForTest = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return ObjectIdGenerator.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return objectIdGenerator;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return objectIdResolver;
            }

            @Override
            public String getPropertyName() {
                return "objectId";
            }
        };
        ObjectIdValueProperty objectIdValuePropertyForTest = new ObjectIdValueProperty(objectIdReaderForTest, PropertyMetadata.empty());

        Object instance = new Object();
        Object id = new Object();

        doThrow(new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty"))
                .when(objectIdValuePropertyForTest).setAndReturn(instance, id);

        objectIdValuePropertyForTest.set(instance, id);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        ObjectIdReader objectIdReaderForTest = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return ObjectIdGenerator.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return objectIdGenerator;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return objectIdResolver;
            }

            @Override
            public String getPropertyName() {
                return "objectId";
            }
        };
        SettableBeanProperty settableBeanPropertyMock = mock(SettableBeanProperty.class);
        ObjectIdValueProperty objectIdValuePropertyForTest = new ObjectIdValueProperty(objectIdReaderForTest, PropertyMetadata.empty(), settableBeanPropertyMock);

        Object instance = new Object();
        Object id = new Object();

        when(settableBeanPropertyMock.setAndReturn(instance, id)).thenReturn(instance);

        Object result = objectIdValuePropertyForTest.setAndReturn(instance, id);

        verify(settableBeanPropertyMock).setAndReturn(instance, id);
        assertEquals(result, instance);
    }
}