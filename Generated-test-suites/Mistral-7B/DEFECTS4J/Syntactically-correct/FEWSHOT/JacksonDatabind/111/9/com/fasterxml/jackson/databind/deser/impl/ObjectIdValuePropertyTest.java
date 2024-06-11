package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BaseParseTestCase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest extends BaseParseTestCase {

    private ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public IdType getIdType() {
            return IdType.STRING;
        }

        @Override
        public String getPropertyName() {
            return "id";
        }

        @Override
        public String getIdPropertyName() {
            return "objectId";
        }

        @Override
        public ObjectIdGenerator getIdGenerator() {
            return new ObjectIdGenerator() {
                long nextId = 0;

                @Override
                public ObjectId generateId(DeserializationContext ctxt, Object idValue) {
                    return new ObjectId(nextId++, this);
                }
            };
        }

        @Override
        public ObjectIdResolver getIdResolver() {
            return new ObjectIdResolver() {
                ObjectId idCache = null;

                @Override
                public ObjectId resolveId(DeserializationContext ctxt, Object idValue) {
                    ObjectId id = idCache;
                    if (id == null) {
                        id = new ObjectId(System.currentTimeMillis(), this);
                        idCache = id;
                    }
                    return id;
                }
            };
        }
    };

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        JsonParser p = mapper.getFactory().createParser("123");
        Object instance = new Object();
        property.deserializeAndSet(p, null, instance);
        assertEquals("123", property.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        JsonParser p = mapper.getFactory().createParser("123");
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, null, instance);
        assertEquals("123", result);
        assertEquals("123", property.get(instance));
    }

    @Test
    public void testNullDeserialization() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);

        JsonParser p = mapper.getFactory().createParser("null");
        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(p, null, instance);
        assertNull(result);
        assertNull(property.get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        Object instance = new Object();

        property.setAndReturn(instance, "123");
        assertEquals("123", property.get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws Exception {
        ObjectMapper mapper = new JsonMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ) {
            @Override
            public SettableBeanProperty idProperty;

            @Override
            public SettableBeanProperty setAndReturn(Object instance, Object value) throws IOException {
                return super.setAndReturn(instance, value);
            }
        };

        Object instance = new Object();
        property.setAndReturn(instance, "123");
        fail("Should not reach here");
    }

    @Test
    public void testAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, PropertyMetadata.STD_OBJ);
        assertNull(property.getAnnotation(AnnotatedClass.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, PropertyMetadata.STD_OBJ);
        assertNull(property.getMember());
    }

    static class AnnotatedClass extends Annotation {}

    static class Object {
        private long id;

        public Object() {}

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}