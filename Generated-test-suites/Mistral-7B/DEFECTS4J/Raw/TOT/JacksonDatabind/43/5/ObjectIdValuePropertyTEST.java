package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;
    private ObjectReader idReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setAnnotationIntrospector(new AnnotationIntrospector());

        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return String.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return null;
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

        idReader = objectMapper.readerForClass(String.class);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        String id = "123";
        Object instance = new Object();

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        ctxt.pushContext(new EmptyContextHandler());

        property.deserializeAndSet(idReader, ctxt, instance);

        Object idPropValue = property.get(instance);
        assertEquals(id, idPropValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.setValueDeserializer(idReader);

        String id = "123";
        Object instance = new Object();

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        ctxt.pushContext(new EmptyContextHandler());

        TypeDeserializer typeDeserializer = objectMapper.getTypeDeserializerForProperty(property, null, null, null);
        Object result = property.deserializeSetAndReturn(idReader, ctxt, instance, typeDeserializer);

        Object idPropValue = property.get(instance);
        assertEquals(id, idPropValue);
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        String id = "123";

        property.set(instance, id);

        Object idPropValue = property.get(instance);
        assertEquals(id, idPropValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetUnsupported() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property.setValueDeserializer(idReader);

        Object instance = new Object();
        String id = "123";

        property.set(instance, id);

        Object idProp = property.getValueDeserializer().getIdProperty();
        idProp.set(instance, id);

        fail("Should throw UnsupportedOperationException");
    }

    @Test
    public void testNullId() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        ctxt.pushContext(new EmptyContextHandler());

        property.deserializeAndSet(null, ctxt, instance);

        assertNull(property.get(instance));
    }
}

class EmptyContextHandler extends DeserializationContext.BaseContextHandler {
    @Override
    public void reportError(String msg) {
        // empty
    }

    @Override
    public void reportWarning(String msg) {
        // empty
    }
}