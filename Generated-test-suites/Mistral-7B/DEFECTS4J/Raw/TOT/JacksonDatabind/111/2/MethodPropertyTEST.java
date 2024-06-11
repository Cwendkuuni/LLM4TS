package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.*;

public class MethodPropertyTest {

    private ObjectMapper objectMapper;
    private DeserializationContext context;
    private SettableBeanProperty methodProperty;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        context = objectMapper.getDeserializationContext();

        Class<?> testClass = getClass();
        Method setMethod = testClass.getDeclaredMethod("setStringValue", String.class);
        PropertyName propertyName = PropertyName.construct("stringValue");
        methodProperty = new MethodProperty(
                new BeanPropertyDefinition(propertyName, String.class),
                context.constructValueType(String.class),
                null,
                Annotations.findAnnotation(testClass, JsonProperty.class),
                AnnotatedMethod.construct(setMethod)
        );
    }

    @Test
    public void testNormalDeserialization() throws IOException {
        String inputJson = "{\"stringValue\": \"hello\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputJson);

        Object instance = new Object();
        methodProperty.deserializeAndSet(jsonParser, context, instance);

        assertEquals("hello", methodProperty.get(instance));
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        String inputJson = "{\"stringValue\": null}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputJson);

        Object instance = new Object();
        methodProperty.deserializeAndSet(jsonParser, context, instance);

        assertNull(methodProperty.get(instance));
    }

    @Test
    public void testValueDeserializer() throws IOException {
        String inputJson = "{\"stringValue\": 123}";
        JsonDeserializer<Integer> valueDeserializer = new StandardDeserializer<Integer>(Integer.class) {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        methodProperty = new MethodProperty(methodProperty, valueDeserializer);

        Object instance = new Object();
        ObjectNode node = objectMapper.readValue(inputJson, ObjectNode.class);

        methodProperty.deserializeAndSet(context.getParserForBean(instance), context, node);

        assertEquals(123, methodProperty.get(instance));
    }

    @Test(expected = IOException.class)
    public void testDeserializationFailure() throws IOException {
        String inputJson = "{\"stringValue\": true}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(inputJson);

        Object instance = new Object();
        methodProperty.deserializeAndSet(jsonParser, context, instance);

        fail("Expected IOException but got: " + methodProperty.get(instance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueFailure() throws IOException {
        Object instance = new Object();

        String setterName = "setStringValue";

        Method setter = MethodProperty.class.getDeclaredMethod(setterName, String.class);
        setter.setAccessible(true);

        try {
            setter.invoke(methodProperty, instance, 123);
            fail("Expected IllegalArgumentException but got: " + instance);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}