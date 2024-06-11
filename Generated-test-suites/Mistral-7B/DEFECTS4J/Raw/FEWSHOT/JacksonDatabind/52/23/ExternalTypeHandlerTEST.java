package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{\"typeId\": \"StringType\"}");
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);
        assertEquals("StringType", handler.getLastTypeId());
        p.close();

        handler.start();
        assertNull(handler.getLastTypeId());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{\"value\": 123}");
        handler.handlePropertyValue(p, new TestContext(), "value", bean);
        assertEquals(123, handler.getLastDeserializedValue());
        p.close();

        handler.start();
        assertNull(handler.getLastDeserializedValue());
    }

    @Test
    public void testComplete() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new SimpleTypeDeserializer())
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{\"typeId\": \"StringType\", \"value\": 123}");
        handler.handlePropertyValue(p, new TestContext(), "value", bean);
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);
        handler.complete(p, new TestContext(), bean);

        SimplePojo result = (SimplePojo) bean;
        assertEquals("StringType", result.getTypeId());
        assertEquals(123, result.getValue());
        p.close();

        handler.start();
        handler.complete(p, new TestContext(), bean);
        assertNull(bean);
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new SimpleTypeDeserializer())
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{" +
                "\"typeId\": \"NumberType\"," +
                "\"value\": 456" +
                "}");

        handler.handlePropertyValue(p, new TestContext(), "value", bean);
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);

        PropertyValueBuffer buffer = new PropertyValueBuffer();
        handler.complete(p, new TestContext(), buffer, new SimpleCreator());

        SimplePojo result = (SimplePojo) buffer.build();
        assertEquals("NumberType", result.getTypeId());
        assertEquals(456, result.getValue());
        p.close();

        handler.start();
        handler.complete(p, new TestContext(), buffer, new SimpleCreator());
        assertNull(bean);
    }

    @Test
    public void testMissingTypeId() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new SimpleTypeDeserializer())
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{" +
                "\"value\": 789" +
                "}");

        handler.handlePropertyValue(p, new TestContext(), "value", bean);
        handler.complete(p, new TestContext(), bean);

        SimplePojo result = (SimplePojo) bean;
        assertNull(result.getTypeId());
        assertEquals(null, result.getValue());
        p.close();

        handler.start();
        handler.complete(p, new TestContext(), bean);
        assertNull(bean);
    }

    @Test
    public void testMissingProperty() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new SimpleTypeDeserializer())
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{" +
                "\"typeId\": \"StringType\"" +
                "}");

        handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);
        handler.complete(p, new TestContext(), bean);

        SimplePojo result = (SimplePojo) bean;
        assertNull(result.getValue());
        p.close();

        handler.start();
        handler.complete(p, new TestContext(), bean);
        assertNull(bean);
    }

    @Test
    public void testNullValue() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeId"), new SimpleTypeDeserializer())
                .addExternal(new SimplePojo().getClass().getDeclaredField("value"), new SimpleTypeDeserializer())
                .build());

        JsonParser p = new JsonFactory().createParser("{" +
                "\"typeId\": \"StringType\"," +
                "\"value\": null" +
                "}");

        handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);
        handler.handlePropertyValue(p, new TestContext(), "value", bean);
        handler.complete(p, new TestContext(), bean);

        SimplePojo result = (SimplePojo) bean;
        assertNull(result.getValue());
        p.close();

        handler.start();
        handler.complete(p, new TestContext(), bean);
        assertNull(bean);
    }

    private static class SimplePojo {
        private String typeId;
        private Object value;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public static class SimpleCreator implements PropertyBasedCreator<Object> {
            @Override
            public Object createUsingGuess(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new SimplePojo();
            }
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
            JsonToken t = p.nextToken();
            if (t.isScalarValue()) {
                String typeId = p.getText();
                p.nextToken(); // consume ':' or ','
                return deserializeType(p, ctxt, vd.getType(ctxt.getTypeFactory().constructType(typeId)));
            }
            return vd.deserializeNull(p, ctxt);
        }

        @Override
        public String getPropertyName() {
            return "typeId";
        }
    }

    private static class TestContext extends JsonDeserializer.Context {
        private String lastTypeId;
        private Object lastDeserializedValue;

        public String getLastTypeId() {
            return lastTypeId;
        }

        public void setLastTypeId(String lastTypeId) {
            this.lastTypeId = lastTypeId;
        }

        public Object getLastDeserializedValue() {
            return lastDeserializedValue;
        }

        public void setLastDeserializedValue(Object lastDeserializedValue) {
            this.lastDeserializedValue = lastDeserializedValue;
        }
    }
}