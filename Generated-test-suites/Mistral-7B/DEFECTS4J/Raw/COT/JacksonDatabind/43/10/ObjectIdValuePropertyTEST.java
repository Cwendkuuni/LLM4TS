package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.StdSerializableAnnotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdReader;

import org.junit.Test;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyMetadata getMetadata(AnnotatedMember member) {
            return new PropertyMetadata();
        }

        @Override
        public String getIdPropertyName() {
            return "idPropertyName";
        }

        @Override
        public Class<?> getIdType() {
            return Long.class;
        }

        @Override
        public JsonDeserializer<? extends ObjectId> getIdValueDeserializer() {
            return null;
        }

        @Override
        public ObjectIdGenerator getObjectIdGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver getObjectIdResolver() {
            return null;
        }

        @Override
        public String propertyName;
    };

    @Test
    public void testConstructor() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithName() {
        PropertyName newName = PropertyName.construct("testProperty");
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertNotNull(objectIdValueProperty);
        assertEquals("testProperty", objectIdValueProperty.getName().asString());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Long> deserializer = new StandardDeserializer<>(Long.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertNotNull(objectIdValueProperty);
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getAnnotation(JsonAutoDetect.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeAndSetThrowException() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty.class.getDeclaredMethod("deserializeAndSet")
                .setAccessible(true)
                .invoke(objectIdValueProperty, mock(JsonParser.class), mock(DeserializationContext.class), null);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = null;
        Object result = objectIdValueProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), null);
        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdGenerator objectIdGenerator = mock(ObjectIdGenerator.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        ObjectId id = new ObjectId();
        when(objectIdGenerator.generateId(any(Object.class)))
                .thenReturn(id);
        when(objectIdResolver.resolveId(eq(id), eq(objectIdReader.generator), eq(objectIdReader.resolver)))
                .thenReturn(id);
        ObjectParser objectParser = createObjectParser("{\"id\":1}", new JsonFactory());
        Object result = objectIdValueProperty.deserializeSetAndReturn(objectParser, mock(DeserializationContext.class), null);
        assertNotNull(result);
        assertNotNull(id.getId());
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = new ObjectId();
        objectIdValueProperty.set(instance, id.getId());
        assertNotNull(instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = new ObjectId();
        Object result = objectIdValueProperty.setAndReturn(instance, id.getId());
        assertNotNull(result);
        assertNotNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnThrowException() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = null;
        Object result = objectIdValueProperty.setAndReturn(instance, id);
    }

    private ObjectIdReader createObjectIdReader() {
        return new ObjectIdReader() {
            @Override
            public PropertyMetadata getMetadata(AnnotatedMember member) {
                return new PropertyMetadata();
            }

            @Override
            public String getIdPropertyName() {
                return "idPropertyName";
            }

            @Override
            public Class<?> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<? extends ObjectId> getIdValueDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator getObjectIdGenerator() {
                return mock(ObjectIdGenerator.class);
            }

            @Override
            public ObjectIdResolver getObjectIdResolver() {
                return mock(ObjectIdResolver.class);
            }
        };
    }

    private ObjectId createObjectId() {
        return new ObjectId() {
            @Override
            public long getId() {
                return 1L;
            }
        };
    }

    private JsonFactory createObjectParserFactory() {
        return new JsonFactory();
    }

    private ObjectParser createObjectParser(String content, JsonFactory factory) {
        return factory.createParser(new ByteArrayInputStream(content.getBytes()));
    }

    private <T> T mock(Class<T> clazz) {
        return Mockito.mock(clazz);
    }

    private static void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }

    private static void assertEquals(Object expected, Object actual) {
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }
}