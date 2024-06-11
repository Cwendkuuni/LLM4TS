package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;

@RunWith(JUnit4.class)
public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_withTypeIds() throws IOException {
        // Create JSON with type id
        String json = "{\"_type\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializerTest$SomeClass\", \"foo\":\"bar\"}";

        // Create Jackson ObjectMapper and configure it to use AsPropertyTypeDeserializer
        ObjectMapper objectMapper = new ObjectMapper();
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null,
                objectMapper.getTypeFactory().constructType(SomeClass.class),
                new CustomTypeIdResolver(),
                "_type",
                true,
                null,
                As.PROPERTY);
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext(objectMapper);
        typeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), deserializationContext);
    }

    @Test
    public void testDeserializeTypedFromObject_withDefaultImplAndNoTypeIds() throws IOException {
        // Create JSON without type id
        String json = "{\"foo\":\"bar\"}";

        // Create Jackson ObjectMapper and configure it to use AsPropertyTypeDeserializer with default implementation
        ObjectMapper objectMapper = new ObjectMapper();
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null,
                objectMapper.getTypeFactory().constructType(SomeClass.class),
                new CustomTypeIdResolver(),
                "_type",
                true,
                objectMapper.getTypeFactory().constructType(DefaultClass.class),
                As.PROPERTY);
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext(objectMapper);
        typeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), deserializationContext);
    }

    @Test
    public void testDeserializeTypedFromObject_withDefaultImplAndTypeIds() throws IOException {
        // Create JSON with type id
        String json = "{\"_type\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializerTest$SomeClass\", \"foo\":\"bar\"}";

        // Create Jackson ObjectMapper and configure it to use AsPropertyTypeDeserializer with default implementation
        ObjectMapper objectMapper = new ObjectMapper();
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null,
                objectMapper.getTypeFactory().constructType(SomeClass.class),
                new CustomTypeIdResolver(),
                "_type",
                true,
                objectMapper.getTypeFactory().constructType(DefaultClass.class),
                As.PROPERTY);
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext(objectMapper);
        typeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), deserializationContext);
    }

    // Custom test classes for testing purposes
    public static class SomeClass {
        private String foo;

        public SomeClass() {}

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }
    }

    public static class DefaultClass {
        private String foo;

        public DefaultClass() {}

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }
    }

    public static class CustomTypeIdResolver implements com.fasterxml.jackson.databind.jsontype.TypeIdResolver {

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
        public String idFromBaseType() {
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
    }
}