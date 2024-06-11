package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    @Override
                    public ObjectId generateId(SerializerProvider serializers)
                        throws IOException {
                        return new ObjectId("testId");;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId resolveId(Serializable id, DeserializationContext ctxt)
                        throws IOException {
                        return new ObjectId("testIdResolved");
                    }
                };
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt)
                        throws IOException {
                        return null;
                    }
                };
            }

            @Override
            public boolean isEnabled(DeserializationConfig config) {
                return true;
            }
        };
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testConstructor() {
        assertSame(objectIdReader, objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object testInstance = new Object();
        JsonParser jsonParser = new JsonFactory().createParser("{\"id\": 1}");
        objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), testInstance);
        ObjectId expectedObjectId = new ObjectId("1");
        assertSame(expectedObjectId, testInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object testInstance = new Object();
        JsonParser jsonParser = new JsonFactory().createParser("{\"id\": 1}");
        Object returnedObject = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), testInstance);
        assertSame(testInstance, returnedObject);
    }

    @Test
    public void testSet() throws IOException {
        Object testInstance = new Object();
        Object value = new Object();
        objectIdValueProperty.set(testInstance, value);
        assertNull(testInstance); // ObjectIdValueProperty does not set instance value
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsUnsupportedOperationException() throws IOException {
        Object testInstance = new Object();
        Object value = new Object();
        objectIdValueProperty.setAndReturn(testInstance, value);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(NullSerializer.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectIdGenerator defaultIdGenerator() {
        return new ObjectIdGenerator() {
            @Override
            public ObjectId generateId(SerializerProvider serializers)
                throws IOException {
                return new ObjectId("defaultId");
            }
        };
    }

    @Override
    public ObjectIdResolver defaultIdResolver() {
        return new ObjectIdResolver() {
            @Override
            public ObjectId resolveId(Serializable id, DeserializationContext ctxt)
                throws IOException {
                return new ObjectId("defaultIdResolved");
            }
        };
    }

    @Override
    public JsonDeserializer<Object> findBeanDeserializer(BeanDescription beanDesc,
            JsonDeserializer<?> valueDeserializer) throws IOException {
        return null;
    }

    @Override
    public void handleUnknownProperty(String name, Object value,
            JsonParser p, DeserializationContext ctxt) throws IOException {
    }
}

class ObjectId {
    private final String id;

    public ObjectId(String id) {
        this.id = id;
    }
}