package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdReader.Generator;
import com.fasterxml.jackson.databind.node.ObjectIdReader.Resolver;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    // test dependencies
    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;
    private BeanPropertyDefinition propertyDefinition;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public ObjectIdReader.Generator generator() {
                return new ObjectIdReader.Generator() {
                    @Override
                    public ObjectIdValue idFromString(String idStr) {
                        return new ObjectIdValue(idStr);
                    }
                };
            }

            @Override
            public ObjectIdReader.Resolver resolver() {
                return new ObjectIdReader.Resolver() {
                    @Override
                    public ObjectIdValue idFromObject(Object idObj) {
                        return new ObjectIdValue(idObj.toString());
                    }
                };
            }
        };
        propertyDefinition = new BeanPropertyDefinition("id", Long.class);
        jsonParser = new BaseJsonParser();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyDefinition);
    }

    // Test for constructor
    @Test
    public void testObjectIdValuePropertyConstructor() {
        ObjectIdValueProperty obj = new ObjectIdValueProperty(objectIdReader, propertyDefinition);
        assertNotNull(obj);
    }

    // Test for getter methods
    @Test
    public void testGetters() {
        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(Long.class, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getAnnotation(PropertyAnnotation.class));
        assertNull(objectIdValueProperty.getMember());
    }

    // Test for deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        JsonNode node = objectMapper.readTree("{\"id\":1}");
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
        assertEquals(1, ((Number) objectIdValueProperty.get(instance)).longValue());
    }

    // Test for deserializeSetAndReturn method with null value
    @Test
    public void testDeserializeSetAndReturnNullValue() throws JsonProcessingException {
        JsonParser p = jsonParser.getParser();
        p.nextToken(); // consume [null]
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(p, objectMapper.getDeserializationContext(), instance);
        assertNull(result);
    }

    // Test for deserializeSetAndReturn method with valid value
    @Test
    public void testDeserializeSetAndReturnValidValue() throws JsonProcessingException {
        JsonNode node = objectMapper.readTree("{\"id\":1}");
        Object instance = new Object();
        Object id = objectMapper.readTree("{\"id\":1}").get("id").asLong();
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
        assertEquals(id, result);
    }

    // Test for set and setAndReturn methods
    @Test
    public void testSetAndReturn() throws JsonProcessingException {
        Object instance = new Object();
        Object id = new Long(1);
        objectIdValueProperty.setAndReturn(instance, id);
        assertEquals(id, objectIdValueProperty.get(instance));
    }

    // Test for exceptions when set method is called
    @Test(expected = UnsupportedOperationException.class)
    public void testExceptionWhenSetIsCalled() throws IOException {
        Object instance = new Object();
        objectIdValueProperty.set(instance, new Object());
    }
}