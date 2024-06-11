package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    private static class TestObject {
        private Object collection;

        public Object getCollection() {
            return collection;
        }

        public void setCollection(Object value) {
            this.collection = value;
        }
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        property.deserializeAndSet(parser, null, instance);
        assertEquals("[1,2,3]", instance.getCollection());
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        JsonParser parser = mapper.getFactory().createParser(null);
        property.deserializeAndSet(parser, null, instance);
        assertNull(instance.getCollection());
    }

    @Test
    public void testTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(List.class);
        property.setValueDeserializer(mapper.findValueDeserializer(List.class, null));
        property.setValueTypeDeserializer(typeDeserializer);

        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        property.deserializeAndSet(parser, null, instance);
        assertEquals("[1,2,3]", instance.getCollection());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        Object value = new Object();
        property.set(instance, value);
        fail("Should not call `set()` on setterless property");
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        Object value = new Object();
        assertEquals(instance, property.setAndReturn(instance, value));
        assertNotSame(value, instance.getCollection());
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        assertEquals(null, property.getAnnotation(JsonInclude.class));
        assertEquals(JsonInclude.Value.NON_EMPTY, property.getAnnotation(JsonInclude.class).value());
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        property.fixAccess(mapper.getConfig());
        Method getter = TestObject.class.getMethod("getCollection");
        getter.setAccessible(true);
        assertEquals(Modifier.PUBLIC, getter.getModifiers());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        Object result = property.deserializeSetAndReturn(parser, null, instance);
        assertEquals("[1,2,3]", instance.getCollection());
        assertEquals(instance, result);
    }

    @Test
    public void testWithName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        String newName = "newName";
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        SetterlessProperty newProperty = property.withName(PropertyName.construct(newName));
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        SetterlessProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestObject instance = new TestObject();
        NullValueProvider nva = mock(NullValueProvider.class);
        SetterlessProperty property = mapper.findProperty("collection", TestObject.class);

        SetterlessProperty newProperty = property.withNullProvider(nva);
        assertEquals(nva, newProperty._nullProvider);
    }
}