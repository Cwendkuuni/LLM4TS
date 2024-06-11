package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new BaseProperty();
        Constructor<?> constructor = new Object() {}.getClass().getDeclaredConstructor(Object.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor);
        assertEquals(delegate, property._delegate);
        assertEquals(constructor, property._creator);
    }

    @Test
    public void testSerialization() throws Exception {
        SettableBeanProperty delegate = new BaseProperty();
        Constructor<?> constructor = new Object() {}.getClass().getDeclaredConstructor(Object.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(property);
        InnerClassProperty deserialized = mapper.readValue(json, InnerClassProperty.class);

        assertEquals(property, deserialized);
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(InnerClassProperty.class);
        JsonNode node = mapper.readTree("{\"className\":\"com.test.InnerClass\", \"constructorArgs\":[\"arg1\"]}");

        InnerClassProperty property = mapper.readerForUpdating(null).readValue(node);

        assertEquals("arg1", property.getDelegate().getName());
        assertNotNull(property.getCreator());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(InnerClassProperty.class);
        JsonNode node = mapper.readTree("{\"className\":\"com.test.InnerClass\", \"constructorArgs\":[\"arg1\"]}");

        InnerClassProperty property = mapper.readerForUpdating(null).readValue(node);
        Object instance = new Object();
        Object value = new Object();

        Object returnValue = property.setAndReturn(instance, value);

        assertEquals(value, returnValue);
    }

    @Test
    public void testSerializeAndDeserializeWithValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> valueDeserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return "deserialized value";
            }
        };

        SettableBeanProperty delegate = new BaseProperty();
        Constructor<?> constructor = new Object() {}.getClass().getDeclaredConstructor(Object.class);
        InnerClassProperty property = new InnerClassProperty(delegate, constructor, null, valueDeserializer);

        Object value = mapper.readValue(new ByteArrayInputStream("value from input stream".getBytes()), String.class);
        property.setValueDeserializer(valueDeserializer);

        String json = mapper.writeValueAsString(property);
        InnerClassProperty deserialized = mapper.readValue(json, InnerClassProperty.class);

        assertEquals(value, deserialized.getValue());
    }

    @Test
    public void testWithName() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        assertEquals(property, property.withName(new PropertyName("newName")));
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        assertEquals(property, property.withValueDeserializer(deserializer));
    }

    @Test
    public void testAssignIndex() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        property.assignIndex(1);
        assertEquals(1, property.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        assertEquals(0, property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        assertEquals(delegate.getAnnotation(Annotation.class), property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        assertEquals(delegate.getMember(), property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\":\"value from json\"}");

        InnerClassProperty property = new InnerClassProperty(null, null);
        property.setValueDeserializer(new StandardDeserializer<String>(String.class));

        property.deserializeAndSet(mapper.parserForObject(node), new DeserializationContext(mapper), new Object());

        String value = property.getValue();
        assertEquals("value from json", value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"value\":\"value from json\"}");

        InnerClassProperty property = new InnerClassProperty(null, null);
        property.setValueDeserializer(new StandardDeserializer<String>(String.class));

        Object instance = new Object();
        Object returnValue = property.deserializeSetAndReturn(mapper.parserForObject(node), new DeserializationContext(mapper), instance);

        String value = (String) property.getValue();
        assertEquals("value from json", value);
        assertEquals(instance, returnValue);
    }

    @Test
    public void testAssignIndexThrowsWhenNegative() {
        SettableBeanProperty delegate = new BaseProperty();
        InnerClassProperty property = new InnerClassProperty(delegate, null);
        try {
            property.assignIndex(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}