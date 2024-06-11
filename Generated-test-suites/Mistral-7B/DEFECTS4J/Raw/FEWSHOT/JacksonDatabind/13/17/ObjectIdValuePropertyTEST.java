package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader mockObjectIdReader = new MockObjectIdReader();

    @Test
    public void testConstructors() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata());
        assertEquals(mockObjectIdReader.propertyName, property.getName());
        assertEquals(mockObjectIdReader.getIdType(), property.getType());
        assertNull(property.getAnnotation(JsonId.class));
        assertNotNull(property.getDeserializer());

        ObjectIdValueProperty copy = new ObjectIdValueProperty(property, new JsonDeserializer<>());
        assertEquals(property, copy);

        ObjectIdValueProperty deprecated1 = new ObjectIdValueProperty(property, new PropertyName("newName"));
        assertEquals("newName", deprecated1.getName().getName());
        ObjectIdValueProperty deprecated2 = new ObjectIdValueProperty(property, "newName");
        assertEquals("newName", deprecated2.getName().getName());

        ObjectIdValueProperty renamed = new ObjectIdValueProperty(property, new PropertyName("newName"));
        assertEquals("newName", renamed.getName().getName());
    }

    @Test
    public void testGetters() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonId.class));
        assertNull(property.getMember());

        ObjectNode node = JsonFactory.instance.createObjectNode();
        node.put("name", "newName");
        AnnotatedMember member = AnnotationIntrospector.findProperty(node, new SimpleBeanPropertyFilter(), null);
        ObjectIdValueProperty annotatedProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), member);
        assertEquals(member, annotatedProperty.getMember());
        assertEquals(JsonId.class, annotatedProperty.getAnnotationClass());
        assertEquals(JsonId.class, annotatedProperty.getAnnotationType());
        assertEquals(mockObjectIdReader.propertyName, annotatedProperty.getName());
        assertEquals(mockObjectIdReader.getIdType(), annotatedProperty.getType());
        assertEquals(mockObjectIdReader.getDeserializer(), annotatedProperty.getDeserializer());
    }

    @Test
    public void testDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata());
        Object id = new Object();
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer<>();
        property.setDeserializer(deserializer);

        JsonParser mockParser = new MockJsonParser();
        Object instance = new Object();

        Object readValue = property.deserializeAndSet(mockParser, new DeserializationContext(), instance);
        assertEquals(readValue, id);
        assertEquals(id, instance);

        Object deserializedValue = property.deserializeSetAndReturn(mockParser, new DeserializationContext(), instance);
        assertEquals(deserializedValue, id);
        assertEquals(instance, deserializedValue);

        property.set(instance, null);
        assertNull(instance.getClass().getDeclaredField("id").get(instance));

        property.set(instance, new Object());
        Object idFieldValue = instance.getClass().getDeclaredField("id").get(instance);
        assertNotNull(idFieldValue);

        Object idProp = property.getObjectIdReader().getIdProperty();
        assertEquals(idFieldValue, idProp.get(instance));
    }
}

class MockObjectIdReader implements ObjectIdReader {
    final String propertyName = "name";
    final Class<?> idType = String.class;
    final JsonDeserializer<?> getDeserializer = new StubJsonDeserializer<>();
    final BeanPropertyDefinition idProperty = new BeanPropertyDefinition(new PropertyName("id"), JsonType.STRING_TYPE, null, null, null, null, null);

    ObjectIdGenerator generator = new ObjectIdGenerator() {
        @Override
        public String generateIdInternal() {
            return "generatedId";
        }
    };

    ObjectIdResolver resolver = new ObjectIdResolver() {
        @Override
        public ObjectId findExistingId(ObjectId id) throws IOException {
            return new ReadableObjectId() {
                @Override
                public Object getIdValue() {
                    return id;
                }

                @Override
                public void bindItem(Object item) {
                    // no-op
                }
            };
        }

        @Override
        public ObjectId findIdFor(Object id) throws IOException {
            return new ReadableObjectId() {
                @Override
                public Object getIdValue() {
                    return id;
                }
            };
        }
    };
}

class StubJsonDeserializer<T> extends JsonDeserializer<T> {
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Object();
    }
}

class MockJsonParser extends JsonParser {
    @Override
    public boolean nextToken() throws IOException {
        return true;
    }

    @Override
    public Object readValueAs(Class<Object> valueType) throws IOException {
        return null;
    }
}