package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long idCounter = 0;

                    @Override
                    public Object generateId(DeserializationContext ctxt) {
                        return idCounter++;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public Object resolveId(DeserializationContext ctxt, Object id) {
                        return id;
                    }
                };
            }
        };
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object id = Long.valueOf(1L);

        JsonParser jsonParser = jsonFactory.getParser("{\"id\": " + id + "}");
        Object instance = new Object();

        Object deserializedObject = property.deserializeAndSet(jsonParser, null, instance);
        assertNotNull(deserializedObject);
        assertEquals(id, objectMapper.readerForUpdating(instance).readValueAs(Long.class, "id"));
    }

    @Test
    public void testDeserializeSetAndReturnMissingId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        JsonParser jsonParser = jsonFactory.getParser("{\"name\": \"John\"}");
        Object deserializedObject = property.deserializeAndSet(jsonParser, null, instance);
        assertNull(deserializedObject);
        assertEquals(null, objectMapper.readerForUpdating(instance).readValueAs(Long.class, "id"));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = Long.valueOf(1L);

        property.setAndReturn(instance, id);
        assertEquals(id, objectMapper.readerForUpdating(instance).readValueAs(Long.class, "id"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        SettableBeanProperty idProp = new JsonTestBean().getClass().getDeclaredField("id").getAnnotation(JsonId.class).value();
        property = new ObjectIdValueProperty(objectIdReader, idProp);
        property.setAndReturn(instance, "invalidId");
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws IOException, JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return "id";
            }
        };
        property = new ObjectIdValueProperty(property, stringDeserializer);

        Object instance = new Object();

        JsonParser jsonParser = jsonFactory.getParser("{\"id\": \"idString\"}");
        Object deserializedObject = property.deserializeAndSet(jsonParser, null, instance);
        assertNotNull(deserializedObject);
        assertEquals("id", objectMapper.readerForUpdating(instance).readValueAs(String.class, "id"));
    }

    static class JsonTestBean {
        @JsonId("id")
        private Long id;
    }
}