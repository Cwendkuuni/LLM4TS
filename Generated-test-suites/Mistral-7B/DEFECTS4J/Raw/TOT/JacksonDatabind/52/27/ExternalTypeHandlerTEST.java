package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

    private ObjectMapper objectMapper;
    private ExternalTypeHandler handler;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(new MyClassA(), new TypeDeserializer() {
            @Override
            public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
                return new MyClassB();
            }

            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public String getPropertyName() {
                return "typeId";
            }
        }).addExternal(new MyClassC(), new TypeDeserializer() {
            @Override
            public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
                return new MyClassD();
            }

            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public String getPropertyName() {
                return "typeId";
            }
        }).build());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"MyClassA\": \"ATypeId\"}";
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object bean = new MyClassE();

        handler.handleTypePropertyValue(p, ctxt, "MyClassA", bean);

        Assert.assertEquals("ATypeId", handler.getTypeIds()[0]);
        Assert.assertNull(handler.getTokens()[0]);
        Assert.assertNotNull(bean);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"MyClassA\": [1, \"BTypeId\"], \"MyClassC\": \"CTypeId\"}";
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object bean = new MyClassE();

        handler.handlePropertyValue(p, ctxt, "MyClassA", bean);

        Assert.assertEquals("BTypeId", handler.getTypeIds()[0]);
        Assert.assertNotNull(handler.getTokens()[0]);
        Assert.assertNotNull(bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeProperty() throws IOException {
        String json = "{\"MyClassA\": 1, \"MyClassC\": \"CTypeId\"}";
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object bean = new MyClassE();

        handler.handlePropertyValue(p, ctxt, "MyClassA", bean);

        Assert.assertEquals(1, handler.getTypeIds()[0].hashCode());
        Assert.assertNull(handler.getTokens()[0]);
        Assert.assertNotNull(bean);

        handler.complete(p, ctxt, bean);

        // This should throw an exception
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        String json = "{\"MyClassA\": \"ATypeId\"}";
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object bean = new MyClassE();

        handler.handleTypePropertyValue(p, ctxt, "MyClassB", bean);

        Assert.assertNull(handler.getTypeIds()[0]);
        Assert.assertNull(handler.getTokens()[0]);
        Assert.assertNotNull(bean);

        handler.complete(p, ctxt, bean);

        // This should throw an exception
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"MyClassA\": \"ATypeId1\", \"MyClassA\": [1, \"ATypeId2\"], \"MyClassC\": \"CTypeId\"}";
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object bean = new MyClassE();

        handler.handlePropertyValue(p, ctxt, "MyClassA", bean);
        handler.handlePropertyValue(p, ctxt, "MyClassA", bean);
        handler.handleTypePropertyValue(p, ctxt, "MyClassC", bean);

        handler.complete(p, ctxt, bean);

        Assert.assertEquals("ATypeId1", handler.getTypeIds()[0]);
        Assert.assertNull(handler.getTokens()[0]);
        Assert.assertEquals("ATypeId2", handler.getTypeIds()[1]);
        Assert.assertNull(handler.getTokens()[1]);
        Assert.assertEquals("CTypeId", handler.getTypeIds()[2]);
        Assert.assertNull(handler.getTokens()[2]);
        Assert.assertNotNull(bean);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        String json = "{\"MyClassA\": null, \"Creator\": {\"MyClassA\": null, \"MyClassA\": [1, \"ATypeId\"], \"MyClassC\": \"CTypeId\"}}";
        PropertyBasedCreator creator = context -> new MyClassE();
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();

        handler.handlePropertyValue(p, ctxt, "MyClassA", creator);
        handler.handlePropertyValue(p, ctxt, "MyClassA", creator);
        handler.handleTypePropertyValue(p, ctxt, "MyClassC", creator);

        handler.complete(p, ctxt, buffer, creator);

        Object bean = handler.getCompletedResult();

        Assert.assertEquals("ATypeId", handler.getTypeIds()[0]);
        Assert.assertNull(handler.getTokens()[0]);
        Assert.assertEquals("ATypeId", handler.getTypeIds()[1]);
        Assert.assertNull(handler.getTokens()[1]);
        Assert.assertEquals("CTypeId", handler.getTypeIds()[2]);
        Assert.assertNull(handler.getTokens()[2]);
        Assert.assertNotNull(bean);
    }

    @Test
    public void testCompleteWithNullValues() throws IOException {
        String json = "{\"MyClassA\": null, \"MyClassA\": null, \"MyClassC\": null}";
        JsonParser p = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object bean = new MyClassE();

        handler.handlePropertyValue(p, ctxt, "MyClassA", bean);
        handler.handlePropertyValue(p, ctxt, "MyClassA", bean);
        handler.handleTypePropertyValue(p, ctxt, "MyClassC", bean);

        handler.complete(p, ctxt, bean);

        Assert.assertNull(handler.getTypeIds()[0]);
        Assert.assertNull(handler.getTokens()[0]);
        Assert.assertNull(handler.getTypeIds()[1]);
        Assert.assertNull(handler.getTokens()[1]);
        Assert.assertNull(handler.getTypeIds()[2]);
        Assert.assertNull(handler.getTokens()[2]);
        Assert.assertNotNull(bean);
    }

    private class MyClassA {
        private String typeId;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }
    }

    private class MyClassB {
    }

    private class MyClassC {
    }

    private class MyClassD {
    }

    private class MyClassE {
        private List<MyClassA> myClassAs = new ArrayList<>();
        private MyClassC myClassC;

        public List<MyClassA> getMyClassAs() {
            return myClassAs;
        }

        public void setMyClassAs(List<MyClassA> myClassAs) {
            this.myClassAs = myClassAs;
        }

        public MyClassC getMyClassC() {
            return myClassC;
        }

        public void setMyClassC(MyClassC myClassC) {
            this.myClassC = myClassC;
        }
    }

    private static class PropertyValueBuffer {
        private final Map<String, Object> propertyValues = new HashMap<>();

        public void assignParameter(SettableBeanProperty property, Object value) {
            propertyValues.put(property.getName(), value);
        }

        public Object getCompletedResult() {
            List<Object> values = new ArrayList<>(propertyValues.values());
            return objectMapper.readValue(objectMapper.writeValueAsString(values), MyClassE.class);
        }
    }

    private static class PropertyBasedCreator extends BaseAnnotationIntrospector {
        @Override
        public Object createUsingDefaults(DeserializationContext ctxt) throws IOException {
            return new MyClassE();
        }

        @Override
        public Object createInstance(DeserializationContext ctxt) throws IOException {
            return new MyClassE();
        }
    }
}