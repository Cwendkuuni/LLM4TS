package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider.None;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public IdType getIdType() {
                return IdType.INT;
            }

            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public BeanDeserializer.NullValueProvider _nullProvider = None.INSTANCE;

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
        };
    }

    @Test
    public void testDeserializeWithValidId() throws JsonMappingException, IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("id", 1L);
        Object actual = mapper.readValue(jsonNode, mapper.constructType(Object.class));
        Object id = property.getName().get(actual);
        Assert.assertEquals(1L, id);
    }

    @Test
    public void testDeserializeWithNullId() throws JsonMappingException, IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.putNull("id");
        Object actual = mapper.readValue(jsonNode, mapper.constructType(Object.class));
        Object id = property.getName().get(actual);
        Assert.assertNull(id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object actual = new Object();
        property.set(actual, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithNonSettableProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object actual = new Object();
        property.setAndReturn(actual, new Object());
    }

    @Test
    public void testSetAndGet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object actual = new Object();
        Object expected = new Object();
        property.setAndReturn(actual, expected);
        Object id = property.getName().get(actual);
        Assert.assertEquals(expected, id);
    }

    @Test
    public void testSetAndGetWithValueDeserializer() throws JsonMappingException, IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, mapper.readValue(mapper.createObjectNode().put("MyCustomDeserializer", new MyCustomDeserializer()), mapper.constructType(JsonDeserializer.class)));
        Object actual = new Object();
        Object expected = mapper.readTree("123").get(0).asLong();
        property.setAndReturn(actual, expected);
        Object id = property.getName().get(actual);
        Assert.assertEquals(expected, id);
    }
}

class MyCustomDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsLong();
    }
}