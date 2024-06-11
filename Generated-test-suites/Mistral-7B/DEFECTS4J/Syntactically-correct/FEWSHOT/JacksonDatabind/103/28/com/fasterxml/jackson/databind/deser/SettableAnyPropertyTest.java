package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstruction() {
        BeanProperty beanProperty = new BeanProperty();
        AnnotatedMember annotatedMember = new AnnotatedMethod();
        JavaType javaType = new JavaType();
        KeyDeserializer keyDeserializer = new KeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<>();
        TypeDeserializer valueTypeDeserializer = new TypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyProperty2 = new SettableAnyProperty(beanProperty, annotatedMember, javaType, valueDeserializer, valueTypeDeserializer);

        assertEquals(beanProperty, settableAnyProperty.getProperty());
        assertEquals(annotatedMember, settableAnyProperty.getSetter());
        assertEquals(javaType, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty.getValueTypeDeserializer());

        assertEquals(beanProperty, settableAnyProperty2.getProperty());
        assertEquals(annotatedMember, settableAnyProperty2.getSetter());
        assertEquals(javaType, settableAnyProperty2.getType());
        assertNull(settableAnyProperty2.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty2.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty2.getValueTypeDeserializer());

        SettableAnyProperty settableAnyProperty3 = settableAnyProperty.withValueDeserializer(new JsonDeserializer<>());

        assertEquals(beanProperty, settableAnyProperty3.getProperty());
        assertEquals(annotatedMember, settableAnyProperty3.getSetter());
        assertEquals(javaType, settableAnyProperty3.getType());
        assertNull(settableAnyProperty3.getKeyDeserializer());
        assertNotSame(valueDeserializer, settableAnyProperty3.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty3.getValueTypeDeserializer());
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        BeanProperty beanProperty = new BeanProperty();
        AnnotatedMember annotatedMember = new AnnotatedMethod();
        JavaType javaType = new JavaType();
        KeyDeserializer keyDeserializer = new KeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<>();
        TypeDeserializer valueTypeDeserializer = new TypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        Object instance = new Object();

        String propName = "test";

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"test\":1}");

        settableAnyProperty.deserializeAndSet(jsonParser, null, instance, propName);

        // Check if the value was set correctly
        JsonParser jsonParser2 = objectMapper.getFactory().createParser("{\"test\":\"test\"}");
        Object value = settableAnyProperty.deserialize(jsonParser2, null);
        assertEquals("test", value);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) annotatedMember).getValue(instance);
        assertEquals(value, map.get(propName));
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetNull() throws IOException {
        BeanProperty beanProperty = new BeanProperty();
        AnnotatedMember annotatedMember = new AnnotatedMethod();
        JavaType javaType = new JavaType();
        KeyDeserializer keyDeserializer = new KeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<>();
        TypeDeserializer valueTypeDeserializer = new TypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        Object instance = new Object();

        String propName = "test";

        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");

        settableAnyProperty.deserializeAndSet(jsonParser, null, instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) annotatedMember).getValue(instance);
        assertNull(map.get(propName));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetUnresolvedForwardReference() throws IOException {
        BeanProperty beanProperty = new BeanProperty();
        AnnotatedMember annotatedMember = new AnnotatedMethod();
        JavaType javaType = new JavaType();
        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<>();
        TypeDeserializer valueTypeDeserializer = new TypeDeserializer();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, null, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        Object instance = new Object();

        String propName = "test";

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"test\":$ref}");

        settableAnyProperty.deserializeAndSet(jsonParser, null, instance, propName);
    }

    @Test
    public void testSettableAnyPropertyToString() {
        BeanProperty beanProperty = new BeanProperty();
        AnnotatedMember annotatedMember = new AnnotatedMethod();
        JavaType javaType = new JavaType();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, null, null, null);

        assertEquals("[any property on class " + AnnotatedMethod.class.getName() + "]", settableAnyProperty.toString());
    }
}