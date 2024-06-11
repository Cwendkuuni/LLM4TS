package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdValueDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private int counter = 1;

                    @Override
                    public Object generateId() {
                        return counter++;
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String idStr) throws IOException {
                        return new ObjectNode(mapper.createObjectNode()).textValue(idStr);
                    }

                    @Override
                    public String idToString(ObjectId id) throws IOException {
                        return id.asText();
                    }
                };
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return new ObjectIdDeserializer();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSetterProperty("idProperty");
            }
        };
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = mapper.getFactory().createParser("[1]");
        Object instance = new Object();

        property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        Object id = property.get(_objectIdReader, mapper.getDeserializationContext(), instance);
        assertEquals(1, id.hashCode());
    }

    @Test
    public void testNullDeserialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = mapper.getFactory().createParser("null");
        Object instance = new Object();

        Object deserialized = property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        assertNull(deserialized);
        assertNull(property.get(_objectIdReader, mapper.getDeserializationContext(), instance));
    }

    @Test
    public void testErrorDeserialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = mapper.getFactory().createParser("invalid");
        Object instance = new Object();

        try {
            property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
            fail();
        } catch (JsonProcessingException e) {
            // pass
        }

        assertNull(property.get(_objectIdReader, mapper.getDeserializationContext(), instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();

        Object result = property.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, property.get(_objectIdReader, mapper.getDeserializationContext(), instance));
    }

    @Test
    public void testSetWithNullValue() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        property.set(instance, null);
        assertNull(property.get(_objectIdReader, mapper.getDeserializationContext(), instance));
    }

    @Test
    public void testSetWithIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        ObjectId id = mapper.valueToTree(1);

        property = new ObjectIdValueProperty(property, id.getClass(), null);
        Object result = property.setAndReturn(instance, id);
        assertEquals(id, result);
        assertEquals(id.asText(), property.get(_objectIdReader, mapper.getDeserializationContext(), instance).asText());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetUnsupported() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();

        property.set(instance, new Object());
    }
}