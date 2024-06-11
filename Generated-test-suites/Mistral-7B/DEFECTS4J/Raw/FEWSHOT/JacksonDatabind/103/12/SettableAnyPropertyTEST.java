package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {

    @Test
    public void testSettableAnyPropertyCreation() {
        AnnotatedField fieldAnnotated = mapper().findFieldAnnotation(SettableAnyProperty.class, HashMap.class, "testProperty");
        AnnotatedMethod methodAnnotated = mapper().findMethodAnnotation(SettableAnyProperty.class, HashMap.class.getDeclaredMethod("put", Object.class, Object.class));
        JavaType javaType = mapper().constructType(HashMap.class);
        KeyDeserializer keyDeserializer = context().keyDeserializerInstance(String.class);
        JsonDeserializer<Object> valueDeserializer = mapper().findValueDeserializer(Object.class, String.class);
        TypeDeserializer valueTypeDeserializer = null; // not used in this test

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(fieldAnnotated),
                fieldAnnotated,
                javaType,
                keyDeserializer,
                valueDeserializer,
                valueTypeDeserializer
        );

        assertEquals(fieldAnnotated, settableAnyProperty.getProperty());
        assertEquals(fieldAnnotated, settableAnyProperty.getSetter());
        assertEquals(javaType, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertTrue(settableAnyProperty.isSetterIsField());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        AnnotatedField fieldAnnotated = mapper().findFieldAnnotation(SettableAnyProperty.class, HashMap.class, "testProperty");
        AnnotatedMember setterAnnotated = mapper().findMemberAnnotation(SettableAnyProperty.class, HashMap.class, "put");
        JavaType javaType = mapper().constructType(HashMap.class);
        KeyDeserializer keyDeserializer = context().keyDeserializerInstance(String.class);
        JsonDeserializer<Object> valueDeserializer = mapper().findValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null; // not used in this test

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(fieldAnnotated),
                setterAnnotated,
                javaType,
                keyDeserializer,
                valueDeserializer,
                valueTypeDeserializer
        );

        assertEquals(fieldAnnotated, settableAnyProperty.getProperty());
        assertEquals(setterAnnotated, settableAnyProperty.getSetter());
        assertEquals(javaType, settableAnyProperty.getType());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);

        assertEquals(settableAnyProperty.getProperty(), settableAnyPropertyWithValueDeserializer.getProperty());
        assertEquals(settableAnyProperty.getSetter(), settableAnyPropertyWithValueDeserializer.getSetter());
        assertEquals(settableAnyProperty.getType(), settableAnyPropertyWithValueDeserializer.getType());
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer.getValueDeserializer());
        assertNull(settableAnyPropertyWithValueDeserializer.getValueTypeDeserializer());
        assertNull(settableAnyPropertyWithValueDeserializer.getKeyDeserializer());
        assertFalse(settableAnyPropertyWithValueDeserializer.isSetterIsField());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        AnnotatedField fieldAnnotated = mapper().findFieldAnnotation(SettableAnyProperty.class, HashMap.class, "testProperty");
        AnnotatedMember setterAnnotated = mapper().findMemberAnnotation(SettableAnyProperty.class, HashMap.class, "put");
        JavaType javaType = mapper().constructType(HashMap.class);
        KeyDeserializer keyDeserializer = context().keyDeserializerInstance(String.class);
        JsonDeserializer<Object> valueDeserializer = mapper().findValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = null; // not used in this test

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(fieldAnnotated),
                setterAnnotated,
                javaType,
                keyDeserializer,
                valueDeserializer,
                valueTypeDeserializer
        );

        ObjectMapper objectMapper = mapper();
        Object instance = new HashMap<>();
        String propertyName = "testProperty";

        settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser(objectMapper.writeValueAsString("{\"key\": \"value\"}")), objectMapper.getDeserializationContext(), instance, propertyName);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertEquals("value", map.get(propertyName));
    }

    @Test
    public void testDeserializeWithValueTypeDeserializer() throws IOException {
        AnnotatedField fieldAnnotated = mapper().findFieldAnnotation(SettableAnyProperty.class, HashMap.class, "testProperty");
        AnnotatedMember setterAnnotated = mapper().findMemberAnnotation(SettableAnyProperty.class, HashMap.class, "put");
        JavaType javaType = mapper().constructType(HashMap.class);
        KeyDeserializer keyDeserializer = context().keyDeserializerInstance(String.class);
        JsonDeserializer<Object> valueDeserializer = mapper().findValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper().findValueDeserializer(Integer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(fieldAnnotated),
                setterAnnotated,
                javaType,
                keyDeserializer,
                valueDeserializer,
                valueTypeDeserializer
        );

        ObjectMapper objectMapper = mapper();
        Object instance = new HashMap<>();
        String propertyName = "testProperty";

        settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser(objectMapper.writeValueAsString("{\"key\": 1}")), objectMapper.getDeserializationContext(), instance, propertyName);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertEquals(1, map.get(propertyName).intValue());
    }

    @Test
    public void testDeserializeWithUnresolvedForwardReference() throws IOException {
        AnnotatedField fieldAnnotated = mapper().findFieldAnnotation(SettableAnyProperty.class, HashMap.class, "testProperty");
        AnnotatedMember setterAnnotated = mapper().findMemberAnnotation(SettableAnyProperty.class, HashMap.class, "put");
        JavaType javaType = mapper().constructType(HashMap.class);
        KeyDeserializer keyDeserializer = context().keyDeserializerInstance(String.class);
        JsonDeserializer<Object> valueDeserializer = mapper().findValueDeserializer(Object.class);
        TypeDeserializer valueTypeDeserializer = null; // not used in this test

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(
                new BeanProperty(fieldAnnotated),
                setterAnnotated,
                javaType,
                keyDeserializer,
                valueDeserializer,
                valueTypeDeserializer
        );

        ObjectMapper objectMapper = mapper();
        Object instance = new HashMap<>();
        String propertyName = "testProperty";

        JsonParser parser = objectMapper.getFactory().createParser(objectMapper.writeValueAsString("{\"key\": \"$ref\"}"));
        assertThrows(JsonMappingException.class, () -> settableAnyProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), instance, propertyName));
    }
}