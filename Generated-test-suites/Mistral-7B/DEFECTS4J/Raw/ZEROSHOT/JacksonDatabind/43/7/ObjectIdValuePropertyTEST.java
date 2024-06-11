package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.test.BaseTest;

/**
 * JUnit 4 test file for ObjectIdValueProperty class.
 */
public class ObjectIdValuePropertyTest extends BaseTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public String getPropertyName() {
            return "objectId";
        }

        @Override
        public Class<?> getIdType() {
            return Long.class;
        }

        @Override
        public JsonDeserializer<?> getIdDeserializer() {
            return new StandardDeserializer<>(Long.class);
        }

        @Override
        public JsonDeserializer<?> getIdPropertyDeserializer() {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }
    };

    private ObjectIdValueProperty objectIdValueProperty;
    private TestClass testClass;

    @Before
    public void setUp() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Long.class, new StandardDeserializer<>(Long.class));
        jsonFactory.registerModule(module);
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        ObjectIdReader idReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "objectId";
            }

            @Override
            public Class<?> getIdType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> getIdDeserializer() {
                return new StandardDeserializer<>(Long.class);
            }

            @Override
            public JsonDeserializer<?> getIdPropertyDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };

        testClass = new TestClass();
        AnnotatedMember member = new AnnotatedMember(ReflectionTestUtils.findField(TestClass.class, "objectId"), null);
        objectIdValueProperty = new ObjectIdValueProperty(idReader, new PropertyMetadata(PropertyMetadata.STD_OBJ_ID_PROPERTY_METADATA, member, null, null));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("{\"objectId\":1}");
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);
        assertEquals(1, ReflectionTestUtils.getFieldValue(instance, "objectId"));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("{\"objectId\":1}");
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance);
        assertEquals(instance, result);
        assertEquals(1, ReflectionTestUtils.getFieldValue(instance, "objectId"));
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        ObjectId id = new ObjectId(1);
        objectIdValueProperty.set(instance, id);
        assertEquals(id.getId(), ReflectionTestUtils.getFieldValue(instance, "objectId"));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        ObjectId id = new ObjectId(1);
        Object result = objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(instance, result);
        assertEquals(id.getId(), ReflectionTestUtils.getFieldValue(instance, "objectId"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() throws Exception {
        Object instance = new Object();
        objectIdValueProperty.set(instance, null);
    }

    @Test
    public void testWithName() throws Exception {
        String newName = "newName";
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newObjectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<>(String.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(stringDeserializer);
        assertEquals(stringDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithNameAndValueDeserializer() throws Exception {
        String newName = "newName";
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<>(String.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName).withValueDeserializer(stringDeserializer);
        assertEquals(newName, newObjectIdValueProperty.getName());
        assertEquals(stringDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testToString() throws Exception {
        String expected = String.format("ObjectIdValueProperty(%s, %s)", objectIdValueProperty.getName(), objectIdValueProperty.getClass().getName());
        assertEquals(expected, objectIdValueProperty.toString());
    }

    public static class ObjectId {
        private final long id;

        public ObjectId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    public static class TestClass {
        @JsonId
        private Long objectId;

        public Long getObjectId() {
            return objectId;
        }

        public void setObjectId(Long objectId) {
            this.objectId = objectId;
        }
    }
}