package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

        objectIdReader = new SimpleObjectIdReader();
    }

    @Test
    public void testSimpleDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyName.construct("id"));
        BeanPropertyDefinition def = new BeanPropertyDefinition(PropertyName.construct("id"), Object.class, new None(), PropertyMetadata.empty());
        PropertyName idPropertyName = PropertyName.construct("idValue");
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(property, idPropertyName);
        BeanDeserializerBase idDeserializer = new StdScalaBeanDeserializer(Object.class, mapper.getTypeFactory());
        ObjectIdValueProperty propertyUnderTest = new ObjectIdValueProperty(property, idProperty, idDeserializer);

        AnnotatedClass ac = mapper.getAnnotatedClass(Object.class);
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
        JsonId idAnnotation = ai.findAnnotation(ac.getAnnotatedMembers()[0], JsonId.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("id", idAnnotation.value());

        Object instance = mapper.readValue(node.toString(), Object.class);
        assertNull(instance.getClass().getDeclaredField("idValue").get(instance));
        assertSame(idAnnotation.value(), instance.getClass().getDeclaredField("id").get(instance));

        ObjectNode resultNode = mapper.writeValueAsNode(propertyUnderTest.getBean(instance));
        ObjectNode result = mapper.readTree(resultNode.toString());
        assertNull(result.get("idValue"));
        assertNotNull(result.get("id"));

        Object deserializedInstance = mapper.readValue(resultNode.toString(), Object.class);
        assertSame(instance, deserializedInstance);
    }

    @Test
    public void testNullIdValueDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyName.construct("id"));
        BeanPropertyDefinition def = new BeanPropertyDefinition(PropertyName.construct("id"), Object.class, new None(), PropertyMetadata.empty());
        PropertyName idPropertyName = PropertyName.construct("idValue");
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(property, idPropertyName);
        ObjectIdValueProperty propertyUnderTest = new ObjectIdValueProperty(property, idProperty, null);

        ObjectNode node = mapper.createObjectNode();

        Object instance = mapper.readValue(node.toString(), Object.class);
        assertNull(instance.getClass().getDeclaredField("idValue").get(instance));

        ObjectNode resultNode = mapper.writeValueAsNode(propertyUnderTest.getBean(instance));
        ObjectNode result = mapper.readTree(resultNode.toString());
        assertNull(result.get("idValue"));

        Object deserializedInstance = mapper.readValue(resultNode.toString(), Object.class);
        assertSame(instance, deserializedInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyName.construct("id"));
        ObjectIdValueProperty propertyUnderTest = new ObjectIdValueProperty(property, null);

        Object instance = mapper.readValue(mapper.createObjectNode().toString(), Object.class);

        Object idValue = new Object();
        propertyUnderTest.set(instance, idValue);
    }

    static class SimpleObjectIdReader implements ObjectIdReader {
        private final PropertyName propertyName = PropertyName.construct("id");
        private final Class<Object> idType = Object.class;

        @Override
        public PropertyName propertyName() {
            return propertyName;
        }

        @Override
        public Class<?> idType() {
            return idType;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new StdObjectIdGenerator();
        }

        @Override
        public ObjectIdResolver idResolver() {
            return new StdObjectIdResolver();
        }
    }

    static class StdObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectId generateId(Object id) {
            return new ReadableObjectId() {
                @Override
                public ObjectIdType idType() {
                    return ObjectIdType.ID_STRING;
                }

                @Override
                public String idValue() {
                    return id.toString();
                }
            };
        }
    }

    static class StdObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idId(Object id, ObjectIdGenerator generator) {
            return generator.generateId(id);
        }
    }

}