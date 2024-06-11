package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdGenerator generator;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        generator = mock(ObjectIdGenerator.class);
        objectIdReader = new ObjectIdReader("idProperty", mapper.getTypeFactory().constructValueType(Long.TYPE, Long.class), null, null, generator, null);
    }

    @Test
    public void testNullId() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> nullValueProvider = mock(JsonDeserializer.class);

        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(1L);

        property = new ObjectIdValueProperty(property, valueDeserializer, nullValueProvider);

        Object instance = new TestClass();
        Object result = property.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertEquals(1L, result);
        assertEquals(1L, instance.getId());
        assertNull(instance.getName());

        property.set(instance, null);
        assertNull(instance.getId());
    }

    @Test
    public void testValidId() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> valueDeserializer = new FromStringDeserializer<>(Long.class);
        JsonDeserializer<Object> nullValueProvider = mock(JsonDeserializer.class);

        property = new ObjectIdValueProperty(property, valueDeserializer, nullValueProvider);

        Object instance = new TestClass(1L);
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "1");

        Object result = property.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance);
        assertEquals(1L, result);
        assertEquals(1L, instance.getId());
        assertEquals("1", instance.getName());

        property.set(instance, new TestClass("2"));
        assertEquals("2", instance.getName());
        assertEquals(2L, instance.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        property = new ObjectIdValueProperty(property, null);

        TestClass instance = new TestClass();
        instance.setId(1L);
        property.set(instance, new TestClass("2"));
    }

    @Test
    public void testIdPropertySet() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> nullValueProvider = mock(JsonDeserializer.class);
        PropertyName idProperty = new PropertyName("idProperty");

        when(valueDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(1L);

        property = new ObjectIdValueProperty(property, valueDeserializer, nullValueProvider);
        TestClass instance = new TestClass();
        ObjectIdValueProperty idPropertyValue = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), idProperty, valueDeserializer, nullValueProvider);

        property.set(instance, new TestClass("2"));
        assertEquals("2", instance.getName());
        assertEquals(1L, instance.getId());
        assertEquals(1L, instance.getIdProperty().longValue());

        idPropertyValue.set(instance, new TestClass("3"));
        assertEquals("3", instance.getName());
        assertEquals(1L, instance.getId());
        assertEquals(3L, instance.getIdProperty().longValue());
    }

    static class TestClass {
        private Long id;
        private String name;
        @JsonDeserialize(using = StdSerializer.class)
        private Long idProperty;

        public TestClass() {
        }

        public TestClass(Long id) {
            this.id = id;
        }

        public TestClass(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getIdProperty() {
            return idProperty;
        }

        public void setIdProperty(Long idProperty) {
            this.idProperty = idProperty;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}