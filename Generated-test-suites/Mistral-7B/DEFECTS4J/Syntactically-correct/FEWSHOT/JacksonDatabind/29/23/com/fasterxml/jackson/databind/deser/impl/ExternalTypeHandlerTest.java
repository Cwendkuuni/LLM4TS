package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Prepare test data
        String json = "{\"foo\":\"StringType\",\"bar\":{\"baz\":\"IntType\"}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo"), StringTypeDeserializer.instance())
                .addExternal(new SimplePojo().getClass().getDeclaredField("bar.baz"), IntTypeDeserializer.instance())
                .build());

        // Test
        SimplePojo bean = handler.handleTypePropertyValue(p, ctxt, "foo", null);
        assertEquals("StringType", bean.foo);
        bean = handler.handleTypePropertyValue(p, ctxt, "bar.baz", null);
        assertEquals(1, bean.bar.baz);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Prepare test data
        String json = "{\"foo\":\"StringValue\",\"bar\":{\"baz\":42}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo"), StringTypeDeserializer.instance())
                .addExternal(new SimplePojo().getClass().getDeclaredField("bar.baz"), IntTypeDeserializer.instance())
                .build());

        // Test
        SimplePojo bean = handler.handlePropertyValue(p, ctxt, "foo", null);
        assertEquals("StringValue", bean.foo);
        bean = handler.handlePropertyValue(p, ctxt, "bar.baz", null);
        assertEquals(42, bean.bar.baz);
    }

    @Test
    public void testComplete() throws IOException {
        // Prepare test data
        String json = "{\"foo\":\"StringValue\",\"bar\":{}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo"), StringTypeDeserializer.instance())
                .addExternal(new SimplePojo().getClass().getDeclaredField("bar.baz"), IntTypeDeserializer.instance())
                .build());

        // Test
        SimplePojo bean = handler.start().handlePropertyValue(p, ctxt, "foo", null);
        handler.handlePropertyValue(p, ctxt, "bar.baz", bean);
        bean = handler.complete(p, ctxt, bean);
        assertEquals("StringValue", bean.foo);
        assertEquals(0, bean.bar.baz);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        // Prepare test data
        String json = "{\"foo\":\"StringValue1\",\"bar\":{\"baz\":42,\"qux\":\"StringValue2\"}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        SimplePojoWithCreator creator = new SimplePojoWithCreator();

        // Test
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo"), StringTypeDeserializer.instance())
                .addExternal(new SimplePojo().getClass().getDeclaredField("bar.baz"), IntTypeDeserializer.instance())
                .build());
        handler.handlePropertyValue(p, ctxt, "foo", null);
        handler.handlePropertyValue(p, ctxt, "bar.baz", null);
        handler.handlePropertyValue(p, ctxt, "bar.qux", null);
        Object result = handler.complete(p, ctxt, buffer, creator);
        SimplePojo pojo = (SimplePojo) result;
        assertEquals("StringValue1", pojo.foo);
        assertEquals(42, pojo.bar.baz);
        assertEquals("StringValue2", pojo.bar.qux);
    }

    @Test
    public void testMissingExternalProperty() throws IOException {
        // Prepare test data
        String json = "{\"foo\":\"StringValue\",\"bar\":{}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo"), StringTypeDeserializer.instance())
                .build());

        // Test
        try {
            handler.complete(p, ctxt, null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'bar' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testMissingTypeId() throws IOException {
        // Prepare test data
        String json = "{\"foo\":{\"bar\":\"StringValue\"}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo.bar"), StringTypeDeserializer.instance())
                .build());

        // Test
        try {
            handler.complete(p, ctxt, null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing external type id property 'bar'", e.getMessage());
        }
    }

    @Test
    public void testNullExternalPropertyValue() throws IOException {
        // Prepare test data
        String json = "{\"foo\":null,\"bar\":{\"baz\":42}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo"), StringTypeDeserializer.instance())
                .addExternal(new SimplePojo().getClass().getDeclaredField("bar.baz"), IntTypeDeserializer.instance())
                .build());

        // Test
        SimplePojo bean = handler.handlePropertyValue(p, ctxt, "foo", null);
        handler.handlePropertyValue(p, ctxt, "bar.baz", bean);
        bean = handler.complete(p, ctxt, bean);
        assertEquals(null, bean.foo);
        assertEquals(42, bean.bar.baz);
    }

    @Test
    public void testNullExternalTypeId() throws IOException {
        // Prepare test data
        String json = "{\"foo\":{\"bar\":null}}";
        JsonParser p = new ObjectMapper().readTree(json);
        DeserializationContext ctxt = new TestDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("foo.bar"), StringTypeDeserializer.instance())
                .build());

        // Test
        try {
            handler.complete(p, ctxt, null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing external type id property 'bar'", e.getMessage());
        }
    }

    private static class SimplePojo {
        String foo;
        SimplePojoWithBaz bar = new SimplePojoWithBaz();

        // Getters and setters omitted for brevity
    }

    private static class SimplePojoWithBaz {
        int baz;

        // Getters and setters omitted for brevity
    }

    private static class SimplePojoWithCreator {
        SimplePojo pojo;

        @JsonCreator
        public SimplePojoWithCreator(@JsonProperty("foo") String foo, @JsonProperty("bar") SimplePojo bar) {
            this.pojo = new SimplePojo();
            this.pojo.foo = foo;
            this.pojo.bar = bar;
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public JsonParser _getCurrentLocation() {
            return null;
        }

        @Override
        public void _reportError(Throwable t) {
            fail("Unexpected error during deserialization: " + t.getMessage());
        }

        @Override
        public void _reportMappingException(MappingException me) {
            fail("Unexpected mapping exception during deserialization: " + me.getMessage());
        }

        @Override
        public void _constructInstance(Class<?> beanType) throws InstantiationException {
            // No-op
        }

        @Override
        public void _createInstance(Class<?> beanType, JsonDeserializer<?> deserializer)
                throws InstantiationException, IllegalAccessException {
            // No-op
        }

        @Override
        public Object _instanceFinished(Object instance) {
            return instance;
        }

        @Override
        public JavaType _handleTypeReference(TypeReference<?> typeRef) throws JsonMappingException {
            return null;
        }
    }

    private static class StringTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public TypeDeserializer forProperty(PropertyDescriptor prop) {
            return this;
        }

        @Override
        public void resolve(DeserializationContext ctxt, BeanProperty beanProp) throws JsonMappingException {
            // No-op
        }

        @Override
        public TypeDeserializer forProperty(PropertyName propName) {
            return this;
        }

        @Override
        public String getPropertyName() {
            return "StringType";
        }
    }

    private static class IntTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }

        @Override
        public TypeDeserializer forProperty(PropertyDescriptor prop) {
            return this;
        }

        @Override
        public void resolve(DeserializationContext ctxt, BeanProperty beanProp) throws JsonMappingException {
            // No-op
        }

        @Override
        public TypeDeserializer forProperty(PropertyName propName) {
            return this;
        }

        @Override
        public String getPropertyName() {
            return "IntType";
        }
    }
}