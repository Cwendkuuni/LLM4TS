package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MethodPropertyTest {

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        JsonParser jsonParser = createJsonParser(JsonToken.VALUE_NULL);

        DeserializationContext deserializationContext = createDeserializationContext();

        Object instance = new Object();
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSetWithValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        JsonParser jsonParser = createJsonParser(JsonToken.VALUE_STRING);
        DeserializationContext deserializationContext = createDeserializationContext();
        Object instance = new Object();
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertEquals("testValue", instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        JsonParser jsonParser = createJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext deserializationContext = createDeserializationContext();
        Object instance = new Object();
        methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertNull(methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testDeserializeSetAndReturnWithValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        JsonParser jsonParser = createJsonParser(JsonToken.VALUE_STRING);
        DeserializationContext deserializationContext = createDeserializationContext();
        Object instance = new Object();
        methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals("testValue", methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testSetWithNullValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        Object instance = new Object();
        methodProperty.set(instance, null);

        assertNull(instance);
    }

    @Test
    public void testSetWithValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        Object instance = new Object();
        methodProperty.set(instance, "testValue");

        assertEquals("testValue", instance);
    }

    @Test
    public void testSetAndReturnWithNullValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        Object instance = new Object();
        methodProperty.setAndReturn(instance, null);

        assertNull(methodProperty.setAndReturn(instance, null));
    }

    @Test
    public void testSetAndReturnWithValue() throws Exception {
        MethodProperty methodProperty = createMethodProperty();
        Object instance = new Object();
        methodProperty.setAndReturn(instance, "testValue");

        assertEquals("testValue", methodProperty.setAndReturn(instance, "testValue"));
    }

    private MethodProperty createMethodProperty() throws Exception {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        return new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    private JsonParser createJsonParser(JsonToken jsonToken) {
        return new JsonParser() {
            @Override
            public JsonToken nextToken() {
                return jsonToken;
            }

            @Override
            public String getText() {
                if (jsonToken.equals(JsonToken.VALUE_STRING)) {
                    return "testValue";
                }
                return null;
            }
        };
    }

    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext(null, null, null);
    }
    
}