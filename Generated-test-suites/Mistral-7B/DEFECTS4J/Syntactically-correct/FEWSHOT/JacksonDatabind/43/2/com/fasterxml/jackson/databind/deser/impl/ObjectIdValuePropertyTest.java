package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public Class<?> idType() {
            return Long.class;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return ObjectIdGenerators.IntSequenceGenerator.instance;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return new SimpleObjectIdResolver();
        }

        @Override
        public SettableBeanProperty idProperty() {
            return new SettableBeanProperty("idProperty", Long.class, null, null);
        }
    };

    @Test
    public void testObjectIdValuePropertyConstruction() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        assertNotNull(property);
        assertEquals("id", property.getName());
        assertEquals(Long.class, property.getType());
        assertNull(property.getMember());
        assertNull(property.getValueDeserializer());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyCopy() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        ObjectIdValueProperty copy = new ObjectIdValueProperty(src, null);
        ObjectIdValueProperty copyWithName = new ObjectIdValueProperty(src, PropertyName.construct("newName"));
        ObjectIdValueProperty copyWithDeserializer = new ObjectIdValueProperty(src, new StdDeserializer<>(String.class));

        assertEquals(src, copy);
        assertEquals(src.getName(), copy.getName());
        assertEquals(src.getType(), copy.getType());
        assertEquals(src._objectIdReader, copy._objectIdReader);
        assertNotSame(src, copyWithName);
        assertNotSame(src, copyWithDeserializer);
        assertNotSame(src._objectIdReader, copyWithName._objectIdReader);
        assertNotSame(src._objectIdReader, copyWithDeserializer._objectIdReader);

        assertEquals(src.getName(), copyWithName.getName());
        assertEquals(src.getType(), copyWithName.getType());
        assertNotSame(src, copyWithName);
        assertEquals(src._objectIdReader, copyWithName._objectIdReader);

        assertEquals(src.getName(), copyWithDeserializer.getName());
        assertEquals(src.getType(), copyWithDeserializer.getType());
        assertEquals(src._objectIdReader, copyWithDeserializer._objectIdReader);
        assertNotSame(src, copyWithDeserializer);
        assertNotNull(copyWithDeserializer.getValueDeserializer());
    }

    @Test
    public void testObjectIdValuePropertySetter() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        Object id = 1L;

        Object instance = new Object();
        Object idProperty = new SettableBeanProperty("idProperty", Long.class, null, null);
        BeanUtil.setAccessible(idProperty, true);

        property.setValue(instance, id);
        assertEquals(id, idProperty.get(instance));

        property.setAndReturn(instance, id);
        assertEquals(instance, property.getBean());
        assertEquals(id, property.getBeanId(instance));
    }

    @Test
    public void testObjectIdValuePropertyDeserialization() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        JsonParser p = constructParser("{ \"id\": 1 }");
        Object id = 1L;

        Object instance = new Object();
        Object idProperty = new SettableBeanProperty("idProperty", Long.class, null, null);
        BeanUtil.setAccessible(idProperty, true);

        property.deserializeAndSet(p, new TestDeserializationContext(), instance);
        assertEquals(id, idProperty.get(instance));

        Object deserialized = property.deserializeSetAndReturn(p, new TestDeserializationContext(), instance);
        assertEquals(instance, deserialized);

        property.set(instance, id);
        Object deserializedSetter = property.setAndReturn(instance, id);
        assertEquals(instance, deserializedSetter);
    }

    private static class SimpleObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId findId(Object id, ObjectIdGenerator generator) {
            return new ReadableObjectId(id.toString(), id.getClass());
        }
    }

    private static class ReadableObjectId implements ReadableObjectId {
        private String id;
        private Class<?> cls;

        public ReadableObjectId(String id, Class<?> cls) {
            this.id = id;
            this.cls = cls;
        }

        @Override
        public String idString() {
            return id;
        }

        @Override
        public Class<?> idType() {
            return cls;
        }

        @Override
        public ObjectId getIdForEmptyString() {
            return this;
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public ObjectIdResolver idResolver() {
            return new SimpleObjectIdResolver();
        }
    }
}