package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {
    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private ObjectIdValueProperty objectIdValueProperty;
    private NonSerializable nonSerializable;

    @Before
    public void setup() {
        objectMapper = JsonMapper.builder().build();
        deserializationContext = objectMapper.getDeserializationContext();
        jsonParser = objectReader.getFactory().createParser("{\"id\": 1}");
        objectIdValueProperty = new ObjectIdValueProperty(createObjectIdReader(), createPropertyMetadata());
        nonSerializable = new NonSerializable();
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = ClassUtil.createInstance(NonSerializable.class, false);
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertNotNull(instance);
        assertNull(nonSerializable.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = ClassUtil.createInstance(NonSerializable.class, false);
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertNotNull(result);
        assertTrue(result instanceof NonSerializable);
        assertNull(nonSerializable.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoIdProperty() throws Exception {
        Object instance = ClassUtil.createInstance(NonSerializable.class, false);
        objectIdValueProperty.setAndReturn(nonSerializable, "test");
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws Exception {
        Object instance = ClassUtil.createInstance(NonSerializable.class, false);
        objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        Object result = objectIdValueProperty.setAndReturn(nonSerializable, "test");
        assertNotNull(result);
        assertTrue(result instanceof NonSerializable);
        assertEquals("test", nonSerializable.getId());
    }

    private ObjectIdReader createObjectIdReader() {
        return new ObjectIdReader(objectMapper, "id", objectMapper.constructType(Long.class), new ObjectIdGenerator.IdKey(NonSerializable.class, "_id", objectMapper.constructType(Object.class)), new SimpleObjectIdResolver());
    }

    private PropertyMetadata createPropertyMetadata() {
        return PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
    }

    private static class NonSerializable {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}