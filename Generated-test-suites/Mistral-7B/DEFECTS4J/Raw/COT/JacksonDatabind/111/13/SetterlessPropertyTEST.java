package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SetterlessProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new SimpleType(String.class);
        TypeDeserializer typeDeser = new StandardTypeDeserializer();
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedMethod method = new AnnotatedMethod(SetterlessProperty.class.getMethod("constructor"), new Class[]{BeanPropertyDefinition.class, JavaType.class, TypeDeserializer.class, Annotations.class, AnnotatedMethod.class});
        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(setterlessProperty);
    }

    @Test
    public void testWithName() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
        PropertyName newName = new PropertyName("testProperty");
        SetterlessProperty setterlessPropertyNewName = setterlessProperty.withName(newName);
        assertEquals(newName, setterlessPropertyNewName.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        NullValueProvider nva = new StandardSerializers.NullValueProvider();
        SetterlessProperty setterlessPropertyWithDeserializer = setterlessProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, setterlessPropertyWithDeserializer.getValueDeserializer());
        SetterlessProperty setterlessPropertyWithNullProvider = setterlessProperty.withNullProvider(nva);
        assertEquals(nva, setterlessPropertyWithNullProvider.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
        DeserializationConfig config = new DeserializationConfig(new ObjectMapper());
        setterlessProperty.fixAccess(config);
        assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethod() throws Exception {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        Object value = new Object();
        setterlessProperty.set(instance, value);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"test\"}");
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        setterlessProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance);
        String expectedName = "test";
        String actualName = (String) setterlessProperty.getMember().get(instance);
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\": \"test\"}");
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), instance);
        assertEquals(instance, result);
    }
}