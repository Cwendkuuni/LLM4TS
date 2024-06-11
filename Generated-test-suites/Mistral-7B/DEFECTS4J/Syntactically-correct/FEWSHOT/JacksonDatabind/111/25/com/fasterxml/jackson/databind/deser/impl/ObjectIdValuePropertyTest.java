package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName getPropertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public Class<Object> getIdType() {
            return Object.class;
        }

        @Override
        public JsonDeserializer<Object> getIdDeserializer() {
            return new StdDeserializer<Object>(Object.class) {
                @Override
                public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return p.getValueAsToken().asInt();
                }
            };
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }

        @Override
        public SettableBeanProperty idProperty() {
            return null;
        }
    };

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals("id", property.getName());
        assertNull(property.getAnnotation(JsonProperty.class));
        assertNull(property.getMember());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property._objectIdReader);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(PropertyName.construct("newName"));
        assertEquals("newName", newProperty.getName());
        assertSame(property, newProperty._objectIdReader);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withValueDeserializer(new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        });
        assertSame(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertNotSame(property, newProperty);
        assertSame(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withNullProvider(new NullValueProviderDefault());
        assertSame(property.getNullProvider(), newProperty.getNullProvider());
        assertNotSame(property, newProperty);
        assertSame(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectNode instanceNode = ObjectMapper.getDefaultInstance().createObjectNode();
        JsonParser p = ObjectMapper.getDefaultInstance().parserForObject(instanceNode);
        DeserializationContext context = ObjectMapper.getDefaultInstance().unbinding();
        Object instance = context.constructValue("instance");

        p.nextToken(); // consume [START_OBJECT]
        p.nextValue("id", JsonToken.VALUE_NUMBER_INT, 1);
        property.deserializeAndSet(p, context, instance);

        Object id = context.getValueValidator().validateId(instance, "id", Object.class);
        assertEquals(1, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectNode instanceNode = ObjectMapper.getDefaultInstance().createObjectNode();
        JsonParser p = ObjectMapper.getDefaultInstance().parserForObject(instanceNode);
        DeserializationContext context = ObjectMapper.getDefaultInstance().unbinding();

        p.nextToken(); // consume [START_OBJECT]
        p.nextValue("id", JsonToken.VALUE_NUMBER_INT, 1);

        Object id = property.deserializeSetAndReturn(p, context, new Object());
        assertEquals(1, id);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = 1;

        property.set(instance, id);

        Object idFromInstance = property.get(instance);
        assertEquals(id, idFromInstance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object id = 1;

        Object returnValue = property.setAndReturn(instance, id);
        assertEquals(instance, returnValue);
    }
}