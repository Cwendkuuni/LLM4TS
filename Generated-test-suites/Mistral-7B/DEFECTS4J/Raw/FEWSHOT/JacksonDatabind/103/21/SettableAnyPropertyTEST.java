package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyCreation() {
        AnnotatedMethod setter = Mockito.mock(AnnotatedMethod.class);
        AnnotatedField field = Mockito.mock(AnnotatedField.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyProperty2 = settableAnyProperty.withValueDeserializer(valueDeserializer);

        assertNotNull(settableAnyProperty);
        assertNotNull(settableAnyProperty2);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty2.getValueDeserializer());
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "foo";

        settableAnyProperty.deserializeAndSet(parser, null, instance, propName);

        Object value = mapper.readValue(parser, Object.class);
        assertNotNull(value);
        assertEquals(1, value.hashCode());
        // This test depends on the actual implementation of the set method
        // You can write another test method to check the state of the instance after the set method is called
    }

    @Test
    public void testSettableAnyPropertyDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object value = settableAnyProperty.deserialize(parser, null);

        Object expectedValue = mapper.readValue(parser, Object.class);
        assertEquals(expectedValue, value);
    }

    @Test
    public void testSettableAnyPropertySet() throws IOException {
        Object instance = new Object();
        Object propName = "foo";
        Object value = new Object();

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        settableAnyProperty.set(instance, propName, value);

        // This test depends on the actual implementation of the set method
        // You can write another test method to check the state of the instance after the set method is called
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetWithNullInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = null;
        String propName = "foo";

        settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetWithNullValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "foo";

        settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetWithNullTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = null;

        settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": null}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "foo";

        settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetWithNullPropName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = null;

        settableAnyProperty.deserializeAndSet(parser, null, instance, propName);
    }

    @Test
    public void testSettableAnyPropertyToString() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        String expectedValue = "[any property on class " + SettableAnyPropertyTest.class.getName() + "]";

        assertEquals(expectedValue, settableAnyProperty.toString());
    }
}