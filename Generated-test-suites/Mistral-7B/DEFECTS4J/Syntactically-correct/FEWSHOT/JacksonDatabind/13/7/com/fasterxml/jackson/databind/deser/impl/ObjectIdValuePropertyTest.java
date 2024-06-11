package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {
    private ObjectIdReader _objectIdReader;
    private ObjectIdValueProperty _property;

    @Before
    public void setup() {
        _objectIdReader = new ObjectIdReader() {
            @Override
            public String idFromString(String id) {
                return id;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    private int counter = 1;

                    @Override
                    public Object generateId(Serializable id) {
                        return Integer.toString(counter++).toString();
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public Object resolveId(String id, JavaType idType) {
                        return new Object() {
                            private String id = id;
                        };
                    }
                };
            }

            @JsonId
            private final String propertyName = "id";
            private final Class<?> idType = Integer.class;
            private final JsonDeserializer<Object> _deserializer = new StdSerializer<Object>(Object.class) {
                @Override
                public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return p.getValueAsString();
                }
            };
        };
        _property = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(_property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        assertNull(_property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object testInstance = new Object();

        JsonParser jp = createContext().parser();
        jp.setValue("123");

        _property.deserializeAndSet(jp, createContext(), testInstance);

        Object id = (Object) testInstance.getClass().getDeclaredField("id").get(testInstance);
        assertEquals("123", id.toString());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object testInstance = new Object();

        JsonParser jp = createContext().parser();
        jp.setValue("123");

        Object result = _property.deserializeSetAndReturn(jp, createContext(), testInstance);

        Object id = (Object) testInstance.getClass().getDeclaredField("id").get(testInstance);
        assertEquals("123", id.toString());
        assertEquals(testInstance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object testInstance = new Object();

        _property.set(testInstance, "123");

        Object id = (Object) testInstance.getClass().getDeclaredField("id").get(testInstance);
        assertEquals("123", id.toString());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object testInstance = new Object();

        Object result = _property.setAndReturn(testInstance, "123");

        Object id = (Object) testInstance.getClass().getDeclaredField("id").get(testInstance);
        assertEquals("123", id.toString());
        assertEquals(testInstance, result);
    }
}