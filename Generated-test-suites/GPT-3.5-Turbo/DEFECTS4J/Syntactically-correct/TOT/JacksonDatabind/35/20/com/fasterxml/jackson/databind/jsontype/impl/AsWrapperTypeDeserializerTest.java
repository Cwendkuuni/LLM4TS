package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws Exception {
        JavaType javaType = new ObjectMapper().getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = new CustomTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible,
                defaultImpl);
        deserializationContext = new ObjectMapper().getDeserializationContext();
        jsonParser = new JsonFactory().createParser("{\"type\": \"String\", \"value\": \"hello\"}");
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals("hello", result);
    }

    private static class CustomTypeIdResolver implements TypeIdResolver {
        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }
    }

}