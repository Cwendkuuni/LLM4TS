package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"type\":\"MyType\",\"name\":\"John\",\"age\":30}";
        JsonParser jp = new ObjectMapper().readTree(json).asParser();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        externalTypeHandler.handleTypePropertyValue(jp, ctxt, "type", bean);
        assertEquals("MyType", externalTypeHandler._typeIds[0]);
        externalTypeHandler.handleTypePropertyValue(jp, ctxt, "type", bean); // should not deserialize again
        assertNull(externalTypeHandler._typeIds[0]);

        externalTypeHandler = externalTypeHandler.start();
        externalTypeHandler.handleTypePropertyValue(jp, ctxt, "type", bean);
        assertEquals("MyType", externalTypeHandler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"type\":\"MyType\"}";
        JsonParser jp = new ObjectMapper().readTree(json).asParser();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        externalTypeHandler.handlePropertyValue(jp, ctxt, "name", bean);
        assertEquals("John", externalTypeHandler._properties[0].getProperty().getValue(bean));

        externalTypeHandler = externalTypeHandler.start();
        externalTypeHandler.handlePropertyValue(jp, ctxt, "type", bean);
        assertEquals("MyType", externalTypeHandler._typeIds[0]);
        externalTypeHandler.handlePropertyValue(jp, ctxt, "age", bean);
        assertEquals(30, externalTypeHandler._properties[1].getProperty().getValue(bean));
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"type\":\"MyType\"}";
        JsonParser jp = new ObjectMapper().readTree(json).asParser();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        externalTypeHandler.complete(jp, new TestDeserializationContext(), bean);
        assertEquals("MyType", externalTypeHandler._typeIds[0]);
        assertEquals("John", externalTypeHandler._properties[0].getProperty().getValue(bean));
        assertEquals(30, externalTypeHandler._properties[1].getProperty().getValue(bean));

        externalTypeHandler = externalTypeHandler.start();
        externalTypeHandler.handlePropertyValue(jp, new TestDeserializationContext(), "name", bean);
        externalTypeHandler.handlePropertyValue(jp, new TestDeserializationContext(), "type", bean);
        externalTypeHandler.handlePropertyValue(jp, new TestDeserializationContext(), "age", bean);
        externalTypeHandler.complete(jp, new TestDeserializationContext(), bean);
        assertEquals("MyType", externalTypeHandler._typeIds[0]);
        assertEquals("John", externalTypeHandler._properties[0].getProperty().getValue(bean));
        assertEquals(30, externalTypeHandler._properties[1].getProperty().getValue(bean));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String json = "{\"name\":{\"foo\":1,\"bar\":2},\"type\":\"MyType\"}";
        JsonParser jp = new ObjectMapper().readTree(json).asParser();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer)
                    throws IOException {
                return new MyClass(buffer.getAsObject(0));
            }
        };

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();
        externalTypeHandler.handlePropertyValue(jp, new TestDeserializationContext(), "name", bean);
        externalTypeHandler.handlePropertyValue(jp, new TestDeserializationContext(), "type", bean);
        externalTypeHandler.complete(jp, new TestDeserializationContext(), creator, bean);
        assertEquals("MyType", externalTypeHandler._typeIds[0]);
        assertEquals(new MyClass(new Object() {{
            foo = 1;
            bar = 2;
        }}), bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        String json = "{\"type\":\"MyType\"}";
        JsonParser jp = new ObjectMapper().readTree(json).asParser();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        externalTypeHandler.handleTypePropertyValue(jp, new TestDeserializationContext(), "name", bean);
        fail();
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeId() throws IOException {
        String json = "{\"name\":\"John\"}";
        JsonParser jp = new ObjectMapper().readTree(json).asParser();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        externalTypeHandler.handlePropertyValue(jp, new TestDeserializationContext(), "name", bean);
        fail();
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public ObjectFinder findValueDeserializer(Class<?> deserializerClass, BeanDescription beanDesc)
                throws JsonMappingException {
            return new SimpleObjectDeserializer();
        }
    }

    private static class SimpleObjectDeserializer extends StdDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class MyClass {
        int foo;
        int bar;

        MyClass(Object obj) {
            this.foo = ((Number) obj).intValue();
            this.bar = ((Number) obj).intValue() + 1;
        }
    }
}