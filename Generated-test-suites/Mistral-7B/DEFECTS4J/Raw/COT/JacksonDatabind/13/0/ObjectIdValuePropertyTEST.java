package com.example.objectidvalueproperty; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedCreator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long nextId = 1;
                    @Override
                    public ObjectId generateId(SerializerProvider serializers) {
                        return new ObjectId(nextId++);
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) {
                        return new ObjectId(Long.parseLong(idStr));
                    }
                };
            }

            @Override
            public String getIdValue(ObjectId id) {
                return String.valueOf(id.id);
            }
        };
    }

    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String json = "{\"id\": 1}";
        Object idInstance = new Object();
        Object idValue = objectMapper.readValue(json, String.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        assertEquals(idValue, objectIdValueProperty.getValue(idInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String json = "{\"id\": 1}";
        Object idInstance = new Object();
        Object idValue = objectMapper.readValue(json, String.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object result = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        assertEquals(idValue, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        Object idInstance = new Object();
        Object idValue = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.set(idInstance, idValue);

        fail("Should throw UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        Object idInstance = new Object();
        Object idValue = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object result = objectIdValueProperty.setAndReturn(idInstance, idValue);

        fail("Should throw UnsupportedOperationException");
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullId() throws Exception {
        String json = "{}";
        Object idInstance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        fail("Should throw IOException");
    }

    @Test(expected = NullPointerException.class)
    public void testDeserializeSetAndReturnWithNullInstance() throws Exception {
        String json = "{\"id\": 1}";
        Object idValue = objectMapper.readValue(json, String.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), null);

        fail("Should throw NullPointerException");
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithEmptyIdString() throws Exception {
        String json = "{\"id\": \"\"}";
        Object idInstance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        fail("Should throw IOException");
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithInvalidIdType() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().getParser(new ObjectMapper().readTree("{\"id\": [1, 2, 3]}"));
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);

        try {
            Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), new Object());
            fail("Should throw IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullId() throws Exception {
        Object idInstance = new Object();
        Object idValue = null;

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.set(idInstance, idValue);

        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAndReturnWithNullId() throws Exception {
        Object idInstance = new Object();
        Object idValue = null;

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object result = objectIdValueProperty.setAndReturn(idInstance, idValue);

        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithInvalidId() throws Exception {
        String json = "{\"id\": \"abc\"}";
        Object idInstance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        fail("Should throw IOException");
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithInvalidId() throws Exception {
        String json = "{\"id\": \"abc\"}";
        Object idInstance = new Object();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object result = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        fail("Should throw IOException");
    }

    @Test
    public void testSetWithValidId() throws Exception {
        Object idInstance = new IdEntity();
        Object idValue = new ObjectId(1L);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        objectIdValueProperty.set(idInstance, idValue);

        ObjectId actualId = (ObjectId) objectIdValueProperty.getValue(idInstance);
        assertEquals(idValue, actualId);
    }

    @Test
    public void testSetAndReturnWithValidId() throws Exception {
        Object idInstance = new IdEntity();
        Object idValue = new ObjectId(1L);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        Object result = objectIdValueProperty.setAndReturn(idInstance, idValue);

        ObjectId actualId = (ObjectId) result;
        assertEquals(idValue, actualId);
        assertEquals(idInstance, result);
    }

    @Test
    public void testDeserializeAndSetWithIdProperty() throws Exception {
        String json = "{\"id\": 1, \"idProperty\": {}}";
        Object idInstance = new IdEntity();
        Object idValue = new ObjectId(1L);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdWriter idWriter = new ObjectIdWriter();
        objectMapper.registerModule(new SimpleModule() {{
            addSerializer(IdEntity.class, idWriter);
            addSerializer(ObjectId.class, new Std(ObjectId.class));
        }});

        objectIdValueProperty.setIdProperty(new ObjectIdValueProperty.Std(idWriter, null));

        Object result = objectIdValueProperty.deserializeAndSet(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        ObjectId actualId = (ObjectId) objectIdValueProperty.getValue(idInstance);
        assertEquals(idValue, actualId);
        assertEquals(idInstance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithIdProperty() throws Exception {
        String json = "{\"id\": 1, \"idProperty\": {}}";
        Object idInstance = new IdEntity();
        Object idValue = new ObjectId(1L);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdWriter idWriter = new ObjectIdWriter();
        objectMapper.registerModule(new SimpleModule() {{
            addSerializer(IdEntity.class, idWriter);
            addSerializer(ObjectId.class, new Std(ObjectId.class));
        }});

        objectIdValueProperty.setIdProperty(new ObjectIdValueProperty.Std(idWriter, null));

        Object result = objectIdValueProperty.deserializeSetAndReturn(objectMapper.getFactory().getParser(json), objectMapper.getDeserializationContext(), idInstance);

        ObjectId actualId = (ObjectId) objectIdValueProperty.getValue(idInstance);
        assertEquals(idValue, actualId);
        assertEquals(idInstance, result);
    }

    private static class IdEntity {
        private ObjectId id;
        private ObjectIdProperty idProperty;

        public IdEntity() {
        }

        public ObjectId getId() {
            return id;
        }

        public void setId(ObjectId id) {
            this.id = id;
        }

        public ObjectIdProperty getIdProperty() {
            return idProperty;
        }

        public void setIdProperty(ObjectIdProperty idProperty) {
            this.idProperty = idProperty;
        }
    }

    private static class ObjectIdWriter extends BeanPropertyWriter {
        @Override
        public void writeValue(JsonGenerator jg, Object value) throws IOException {
            if (value == null) {
                jg.writeNull();
                return;
            }

            ObjectId objectId = (ObjectId) value;
            jg.writeString(String.valueOf(objectId.getId()));
        }
    }
}