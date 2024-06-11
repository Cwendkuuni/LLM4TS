package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectId;
import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static com.fasterxml.jackson.databind.DeserializationTest.createParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        AnnotationIntrospector introspector = new AnnotationIntrospector();
        ObjectNode testNode = createParser().readValueAsTree("/null");

        Object instance = new Object();

        ObjectIdValuePropertyTest.deserializeAndSet(objectIdProperty, createContext(), instance, testNode);

        Object id = introspector.findAnnotation(instance.getClass(), JsonId.class).id();
        assertEquals("expectedId", id);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        Object id = "expectedId";

        Object result = objectIdProperty.setAndReturn(instance, id);

        Object idProp = introspector.findProperty(instance.getClass(), "id");
        assertEquals(id, idProp.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturnWithNullId() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        assertThrows(IllegalArgumentException.class, () -> {
            objectIdProperty.setAndReturn(instance, null);
        });
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        assertNull(objectIdProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        assertNull(objectIdProperty.getMember());
    }

    @Test
    public void testWithName() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);
        ObjectIdValueProperty newObjectIdProperty = objectIdProperty.withName(PropertyName.construct("newId"));

        assertEquals("newId", newObjectIdProperty.getName().asString());
        assertEquals(objectIdProperty, newObjectIdProperty.getBaseProperty());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public JsonTypeId idType(AnnotationIntrospector valueIntrospector, BeanPropertyDefinition propertyDefinition, JsonParser p, DeserializationContext ctxt) {
                return null;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new AlwaysGenerateObjectId();
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new AlwaysResolveObjectId();
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new DummyIdProperty();
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, null);

        JsonDeserializer<Object> jsonDeserializer = new StubJsonDeserializer<>();
        ObjectIdValueProperty newObjectIdProperty = objectIdProperty.withValueDeserializer(jsonDeserializer);

        assertEquals(jsonDeserializer, newObjectIdProperty.getValueDeserializer());
        assertEquals(objectIdProperty, newObjectIdProperty.getBaseProperty());
    }

    private static void deserializeAndSet(ObjectIdProperty property, DeserializationContext ctxt, Object instance, ObjectNode testNode) throws IOException {
        ObjectIdValueProperty objectIdProperty = (ObjectIdValueProperty) property;
        ObjectIdReader objectIdReader = objectIdProperty.getObjectIdReader();
        ObjectIdGenerator idGenerator = objectIdReader.idGenerator();
        ObjectIdResolver idResolver = objectIdReader.idResolver();

        Object id = idGenerator.generateId(ctxt, null);
        testNode.put("id", id);

        JsonParser parser = createParser(testNode);
        property.deserializeAndSet(parser, ctxt, instance);

        Object idProp = ctxt.findBeanProperty("id", Object.class);
        assertEquals(id, idProp.get(instance));
    }

    static class DummyIdProperty extends SettableBeanProperty {
        public DummyIdProperty() {
            super("id", null, null, null, null);
        }

        @Override
        public void setAndReturn(Object instance, Object value) throws IOException {
            throw new UnsupportedOperationException("Should not call set() on DummyIdProperty");
        }
    }

    static class AlwaysGenerateObjectId implements ObjectIdGenerator {
        @Override
        public Object generateId(DeserializationContext ctxt, Object idFromXML) {
            return convertObjectId("generatedId");
        }
    }

    static class AlwaysResolveObjectId implements ObjectIdResolver {
        @Override
        public Object resolveId(DeserializationContext ctxt, ObjectId id) {
            return id;
        }
    }

    static class StubJsonDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}