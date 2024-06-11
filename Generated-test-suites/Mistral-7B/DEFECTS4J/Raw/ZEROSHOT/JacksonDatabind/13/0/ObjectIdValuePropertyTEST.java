package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyMetadata getMetadata(DeserializationConfig config, AnnotatedMember am,
                BeanPropertyPropertyEditor pe) {
            return new StandardPropertyMetadata();
        }

        @Override
        public PropertyName getPropertyName() {
            return new PropertyName("id");
        }

        @Override
        public Class<?> getIdType() {
            return Object.class;
        }

        @Override
        public JsonDeserializer<?> getIdDeserializer() {
            return new StdScalarDeserializer(ObjectIdNode.class);
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
        public SettableBeanProperty idProperty;
    };

    @SuppressWarnings("unchecked")
    private static final ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata());

    @org.junit.Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Null.class));
    }

    @org.junit.Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        Object target = new Object();
        JsonParser jp = createContext().createParser("{\"id\":1}");
        property.deserializeAndSet(jp, createContext(), target);
        assertEquals(1, target.getClass().getDeclaredField("id").get(target));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object target = new Object();
        JsonParser jp = createContext().createParser("{\"id\":1}");
        Object result = property.deserializeSetAndReturn(jp, createContext(), target);
        assertEquals(target, result);
        assertEquals(1, target.getClass().getDeclaredField("id").get(target));
    }

    @org.junit.Test
    public void testSet() throws IOException {
        Object target = new Object();
        property.set(target, new TextNode("1"));
        assertEquals("1", target.getClass().getDeclaredField("id").get(target));
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        Object target = new Object();
        Object result = property.setAndReturn(target, new TextNode("1"));
        assertEquals(target, result);
        assertEquals("1", target.getClass().getDeclaredField("id").get(target));
    }

    @org.junit.Test
    public void testWithName() {
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertEquals("newName", newProperty.getName().getSimpleName());
        assertEquals(property, newProperty.getBackReference());
    }

    @org.junit.Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(new StdScalarDeserializer(TextNode.class));
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property, newProperty.getBackReference());
    }

    @org.junit.Test
    public void testToString() {
        Object target = new Object();
        ObjectIdValueProperty newProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata()) {
            @Override
            public String getName() {
                return "name";
            }

            @Override
            public JsonDeserializer<?> getValueDeserializer() {
                return new ToStringSerializer(String.class);
            }
        };
        property.set(target, new ObjectIdNode(1L));
        assertEquals("name=1", newProperty.toString());
    }

    @org.junit.Test
    public void testDeserializeAndSet_NullId() throws IOException {
        Object target = new Object();
        JsonParser jp = createContext().createParser("{}");
        property.deserializeAndSet(jp, createContext(), target);
        assertNull(target.getClass().getDeclaredField("id").get(target));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn_NullId() throws IOException {
        Object target = new Object();
        JsonParser jp = createContext().createParser("{}");
        Object result = property.deserializeSetAndReturn(jp, createContext(), target);
        assertEquals(target, result);
        assertNull(target.getClass().getDeclaredField("id").get(target));
    }

    private DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id");
        mapper.setSerializerProvider(new SerializerProvider.Base());
        mapper.setBeanPropertyFilter(filter);
        return mapper.getDeserializationContext();
    }
}