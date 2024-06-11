package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectReader reader = objectMapper.readerFor(rootNode);
        objectIdReader = new SimpleObjectIdReader(reader.findValueParser(ObjectNode.class), null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object id = "123";
        JsonParser parser = objectMapper.getFactory().createParser(Objects.toString(id));
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object instance = new Object();
        property.deserializeAndSet(parser, ctxt, instance);

        ObjectReader idReader = objectMapper.readerFor(Object.class);
        Object idValue = idReader.readValue(parser);
        assertEquals(id, idValue);

        Object id2 = "456";
        parser = objectMapper.getFactory().createParser(Objects.toString(id2));
        ctxt = objectMapper.getDeserializationContext();

        instance = new Object();
        property.deserializeAndSet(parser, ctxt, instance);

        BeanPropertyDefinition def = AnnotationIntrospector.findProperty(Object.class, "id");
        TypeDeserializer typeDeserializer = objectMapper.findValueDeserializer(def.getType(), def.getName(), null);

        Object id3 = typeDeserializer.deserialize(parser, ctxt, Object.class);
        assertEquals(id2, id3);
    }

    @Test
    public void testNullId() throws IOException, JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        JsonParser parser = objectMapper.getFactory().createParser("null");

        property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), instance);
        assertNull(instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testIdProperty() throws IOException, JsonProcessingException {
        class TestObject {
            @JsonDeserialize(using = ObjectIdValueProperty.class)
            private Long id;
            private String name;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }
        }

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        TestObject testObject = new TestObject();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("id", "123");
        ObjectReader reader = mapper.readerFor(TestObject.class);

        Object id = "123";
        JsonParser parser = mapper.getFactory().createParser(Objects.toString(id));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TestObject testInstance = reader.readValue(parser);
        assertEquals(123L, testInstance.getId());

        TestObject testInstance2 = new TestObject();
        property.deserializeAndSet(parser, ctxt, testInstance2);
        assertEquals(123L, testInstance2.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        property.set(instance, "123");
    }

    static class SimpleObjectIdReader implements ObjectIdReader {
        private JsonParser _parser;
        private ObjectIdGenerator _generator;
        private ObjectIdResolver _resolver;

        public SimpleObjectIdReader(JsonParser parser, ObjectIdResolver resolver) {
            _parser = parser;
            _resolver = resolver;
        }

        public JsonParser propertyNameParser() {
            return _parser;
        }

        public ObjectIdGenerator generator() {
            return _generator;
        }

        public ObjectIdResolver resolver() {
            return _resolver;
        }

        public ObjectIdReader withGenerator(ObjectIdGenerator generator) {
            return new SimpleObjectIdReader(_parser, generator);
        }

        public ObjectIdReader withResolver(ObjectIdResolver resolver) {
            return new SimpleObjectIdReader(_parser, resolver);
        }
    }
}