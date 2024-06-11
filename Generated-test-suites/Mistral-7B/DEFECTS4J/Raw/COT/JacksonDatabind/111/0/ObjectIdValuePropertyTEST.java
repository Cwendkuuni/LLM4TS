package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<?> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        assertNotNull(objectIdValueProperty);
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(ObjectIdType.class, objectIdValueProperty.getIdType());
        assertNotNull(objectIdValueProperty._objectIdReader);
        assertEquals(deserializer, objectIdValueProperty.getDeserializer());
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<?> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        ObjectIdValueProperty copiedObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer, null);

        assertNotNull(copiedObjectIdValueProperty);
        assertEquals(objectIdValueProperty._objectIdReader, copiedObjectIdValueProperty._objectIdReader);
        assertEquals(objectIdValueProperty.getIdType(), copiedObjectIdValueProperty.getIdType());
        assertEquals(objectIdValueProperty.getName(), copiedObjectIdValueProperty.getName());
        assertEquals(deserializer, copiedObjectIdValueProperty.getDeserializer());
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<?> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        PropertyName newName = PropertyName.construct("newId");
        ObjectIdValueProperty withName = objectIdValueProperty.withName(newName);

        assertNotSame(objectIdValueProperty, withName);
        assertEquals(newName, withName.getName());
        assertEquals(objectIdValueProperty.getIdType(), withName.getIdType());
        assertEquals(objectIdValueProperty._objectIdReader, withName._objectIdReader);
        assertEquals(deserializer, withName.getDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<?> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        assertNull(objectIdValueProperty.getAnnotation(JsonProperty.class));
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<?> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<ObjectIdType> objectIdDeserializer = new StdDeserializer<ObjectIdType>(ObjectIdType.class) {};
        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), objectIdDeserializer);

        ObjectIdType expectedId = new ObjectIdType();
        String jsonString = "{\"id\":\"" + expectedId.toString() + "\"}";
        JsonParser jsonParser = new JsonFactory().createParser(jsonString);

        Object testInstance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), testInstance);

        ObjectIdType actualId = (ObjectIdType) testInstance;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        Object testInstance = new Object();

        JsonParser jsonParser = new JsonFactory().createParser(null);
        JsonToken jsonToken = jsonParser.nextToken();
        jsonParser.nextToken(JsonToken.VALUE_NULL);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), testInstance);

        assertNull(result);
        assertEquals(testInstance, testInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        Object testInstance = new Object();
        Object value = new Object();

        objectIdValueProperty.set(testInstance, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<ObjectIdType> getIdType() {
                return ObjectIdType.class;
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }
        };

        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), deserializer);

        Object testInstance = new Object();
        Object value = new Object();

        Object result = objectIdValueProperty.setAndReturn(testInstance, value);
    }
}