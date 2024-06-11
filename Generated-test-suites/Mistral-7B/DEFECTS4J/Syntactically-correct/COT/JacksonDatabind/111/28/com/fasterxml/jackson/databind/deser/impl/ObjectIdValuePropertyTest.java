package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ObjectIdValueProperty.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() throws Exception {
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return java.lang.Long.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                // no-op
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                // no-op
                return null;
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return null;
            }
        };
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata(null));
    }

    @Test
    public void testConstructor() {
        assertSame(objectIdReader.propertyName(), objectIdValueProperty.getName());
    }

    @Test
    public void testWithName() throws Exception {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
        assertSame(new PropertyName("newName"), newObjectIdValueProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        TypeDeserializer deserializer = new StandardTypeDeserializer();
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deserializer);
        assertSame(deserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(nullValueProvider);
        assertSame(nullValueProvider, newObjectIdValueProperty.getNullProvider());
    }

    @Test
    public void testGetAnnotation() throws Exception {
        assertNull(objectIdValueProperty.getAnnotation(ObjectId.class));
    }

    @Test
    public void testGetMember() throws Exception {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeAndSetNullInstance() throws Exception {
        Object parser = new Object();
        objectIdValueProperty.deserializeAndSet(new JsonFactory().createParser(new ObjectNode(ObjectMapper.getDefaultInstance()).setNull()), new MinimalDeserializationContext(), parser);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetEmptyString() throws Exception {
        JsonParser parser = new JsonFactory().createParser(new ObjectNode(ObjectMapper.getDefaultInstance()).setTextValue(""));
        objectIdValueProperty.deserializeAndSet(parser, new MinimalDeserializationContext(), new Object());
    }

    @Test
    public void testDeserializeAndSetLong() throws Exception {
        Object id = 1L;
        JsonParser parser = new JsonFactory().createParser(new ObjectNode(ObjectMapper.getDefaultInstance()).numberValue(id));
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeAndSet(parser, new MinimalDeserializationContext(), instance);
        assertSame(instance, result);
        assertSame(id, ((Object) instance).getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeAndSetNullId() throws Exception {
        JsonParser parser = new JsonFactory().createParser(new ObjectNode(ObjectMapper.getDefaultInstance()).nullValue());
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeAndSet(parser, new MinimalDeserializationContext(), instance);
        assertNull(result);
        assertNull(((Object) instance).getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSerializeSetAndReturnLong() throws Exception {
        Object id = 1L;
        Object instance = new Object();
        ObjectIdValueProperty.setAndReturn(instance, id);
        assertEquals(id, ((Object) instance).getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSerializeSetAndReturnNull() throws Exception {
        Object instance = new Object();
        ObjectIdValueProperty.setAndReturn(instance, null);
        assertNull(((Object) instance).getClass().getDeclaredField("id").get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNullInstance() throws Exception {
        Object parser = new Object();
        objectIdValueProperty.set(parser, null);
    }

    @Test
    public void testSetLongInstance() throws Exception {
        Object id = 1L;
        Object instance = new Object();
        objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(id, ((Object) instance).getClass().getDeclaredField("id").get(instance));
    }

}