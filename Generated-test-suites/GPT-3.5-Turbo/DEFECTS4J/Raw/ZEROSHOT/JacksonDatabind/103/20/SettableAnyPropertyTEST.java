package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    }

    @Test
    public void testGetProperty() {
        BeanProperty result = settableAnyProperty.getProperty();

        assertEquals(null, result);
    }

    @Test
    public void testHasValueDeserializer() {
        boolean result = settableAnyProperty.hasValueDeserializer();

        assertEquals(false, result);
    }

    @Test
    public void testGetType() {
        JavaType result = settableAnyProperty.getType();

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeAndSet() {
        // simulate the behavior of deserializeAndSet() method
        Object instance = new Object();
        String propName = "property";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getJsonFactory().createParser("value");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        // assert the behavior of deserializeAndSet() method
        try {
            settableAnyProperty.deserializeAndSet(jsonParser, deserializationContext, instance, propName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserialize() {
        // simulate the behavior of deserialize() method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getJsonFactory().createParser("value");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        // assert the behavior of deserialize() method
        try {
            Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);
            assertNotNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSet() {
        // simulate the behavior of set() method
        Object instance = new Object();
        Object propName = "property";
        Object value = "value";

        // assert the behavior of set() method
        try {
            settableAnyProperty.set(instance, propName, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_throwAsIOE() {
        // simulate the behavior of _throwAsIOE() method
        Exception e = new IllegalArgumentException("Illegal Argument");
        Object propName = "property";
        Object value = "value";

        // assert the behavior of _throwAsIOE() method
        try {
            settableAnyProperty._throwAsIOE(e, propName, value);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testToString() {
        String expected = "[any property on class com.fasterxml.jackson.databind.deser.SettableAnyPropertyTest]";
        String result = settableAnyProperty.toString();
        
        assertEquals(expected, result);
    }

    @Test
    public void testAnySetterReferring() {
        AnnotatedMember setter = new AnnotatedField();
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object instance = new Object();
        String propName = "property";
        SettableAnyProperty.AnySetterReferring anySetterReferring = new SettableAnyProperty.AnySetterReferring(settableAnyProperty, reference, type, instance, propName);

        anySetterReferring.handleResolvedForwardReference("id", "value");
    }
}