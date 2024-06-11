package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class ObjectIdValuePropertyTest extends BaseTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return null; }

            @Override
            public Class<?> idType() { return null; }
        };
        PropertyMetadata metadata = null;
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        assertNull(objectIdValueProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(null, null);
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return new ObjectIdGenerator() {
                @Override
                public ObjectId generateId(JsonParser p) {
                    return new ObjectId(1L, "1");
                }
            }; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdGenerator objectIdGenerator = objectIdValueProperty._objectIdReader.generator;
        ObjectId id = objectIdGenerator.generateId(mapper().getFactory().createParser("1"));

        ObjectIdValueProperty spy = spy(objectIdValueProperty);
        Object instance = new Object();
        JsonParser jp = mapper().getFactory().createParser("{\"id\":1}");
        objectIdValueProperty.deserializeAndSet(jp, mapper().getDeserializationContext(), instance);
        assertEquals(id.id, instance.getClass().getDeclaredField("id").get(instance));
        verify(spy).deserializeSetAndReturn(jp, mapper().getDeserializationContext(), instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return new ObjectIdGenerator() {
                @Override
                public ObjectId generateId(JsonParser p) {
                    return new ObjectId(1L, "1");
                }
            }; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdGenerator objectIdGenerator = objectIdValueProperty._objectIdReader.generator;
        ObjectId id = objectIdGenerator.generateId(mapper().getFactory().createParser("1"));

        ObjectIdValueProperty spy = spy(objectIdValueProperty);
        Object instance = new Object();
        JsonParser jp = mapper().getFactory().createParser("{\"id\":1}");
        Object result = objectIdValueProperty.deserializeSetAndReturn(jp, mapper().getDeserializationContext(), instance);
        assertEquals(id.id, result);
        verify(spy).deserializeAndSet(jp, mapper().getDeserializationContext(), instance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdGenerator objectIdGenerator = objectIdValueProperty._objectIdReader.generator;
        ObjectId id = objectIdGenerator.generateId(mapper().getFactory().createParser("1"));
        Object instance = new Object();
        Object value = id.id;

        objectIdValueProperty.set(instance, value);
        assertEquals(value, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetThrowsException() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.set(instance, value);
    }

    @Test
    public void testSetWithIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdGenerator objectIdGenerator = objectIdValueProperty._objectIdReader.generator;
        ObjectId id = objectIdGenerator.generateId(mapper().getFactory().createParser("1"));
        ObjectId idProperty = new ObjectId(2L, "2");
        Object instance = new Object() {{ setId(idProperty); }};
        Object value = id.id;

        SettableBeanProperty idPropertySpy = spy(instance.getClass().getDeclaredField("id").get(instance));
        ObjectIdValueProperty spy = spy(objectIdValueProperty);
        objectIdValueProperty.set(instance, value);
        verify(idPropertySpy).setAndReturn(instance, value);
        verify(spy).deserializeAndSet(mapper().getFactory().createParser("1"), mapper().getDeserializationContext(), instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();

        objectIdValueProperty.set(instance, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNonLongValue() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();

        objectIdValueProperty.set(instance, new TextNode("1"));
    }

    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("oldName"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, null);
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
        assertEquals(newName, result.getName());
        assertEquals(objectIdValueProperty, result.getPropertyBase());
        assertEquals(objectIdValueProperty._objectIdReader, result._objectIdReader);
        assertEquals(objectIdValueProperty._valueDeserializer, result._valueDeserializer);
        assertEquals(objectIdValueProperty._metadata, result._metadata);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<ObjectId> idDeserializer = new StdSerializer<ObjectId>(ObjectId.class) {};
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        ObjectIdValueProperty result = objectIdValueProperty.withValueDeserializer(idDeserializer);
        assertEquals(idDeserializer, result._valueDeserializer);
        assertNotSame(objectIdValueProperty, result);
    }

    @Test
    public void testToString() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public ObjectIdGenerator generator() { return null; }

            @Override
            public ObjectIdResolver resolver() { return null; }

            @Override
            public PropertyName propertyName() { return new PropertyName("id"); }

            @Override
            public Class<?> idType() { return Long.class; }
        };
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        String result = objectIdValueProperty.toString();
        assertTrue(result.contains("ObjectIdValueProperty"));
        assertTrue(result.contains("id"));
        assertTrue(result.contains("ObjectIdReader"));
    }
}