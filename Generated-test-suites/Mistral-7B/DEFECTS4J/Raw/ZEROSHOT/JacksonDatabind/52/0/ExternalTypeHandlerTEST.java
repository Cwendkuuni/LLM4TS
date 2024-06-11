package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{\"typeId\": \"StringClass\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "typeId", bean));
        assertEquals("StringClass", handler.getTypeIds()[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{\"propertyName\": \"typeId\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        assertTrue(handler.handlePropertyValue(p, ctxt, "propertyName", bean));
        assertNull(handler.getTypeIds());
        assertNotNull(handler.getTokens()[0]);
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{\"nonExistentProperty\": \"typeId\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        assertFalse(handler.handleTypePropertyValue(p, ctxt, "nonExistentProperty", bean));
    }

    @Test
    public void testHandlePropertyValueMissingPropertyAndTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{\"nonExistentProperty\": null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        assertFalse(handler.handlePropertyValue(p, ctxt, "nonExistentProperty", bean));
    }

    @Test
    public void testHandlePropertyValueWithoutTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{\"propertyName\": \"value\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        assertTrue(handler.handlePropertyValue(p, ctxt, "propertyName", bean));
        assertNull(handler.getTypeIds());
        assertNotNull(handler.getTokens()[0]);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"property1\": \"value1\","
                + "\"typeId1\": \"TypeClass1\","
                + "\"property2\": \"value2\","
                + "\"typeId2\": \"TypeClass2\""
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean);

        Map<String, String> map = (Map<String, String>) bean;
        assertEquals("value1", map.get("property1"));
        assertEquals("TypeClass1", map.get("typeId1"));
        assertEquals("value2", map.get("property2"));
        assertEquals("TypeClass2", map.get("typeId2"));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"property1\": \"value1\","
                + "\"property2\": \"value2\""
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean);

        fail("Expected JsonProcessingException but it didn't occur.");
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"typeId1\": \"TypeClass1\","
                + "\"typeId2\": \"TypeClass2\""
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean);

        fail("Expected JsonProcessingException but it didn't occur.");
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Creator creator = new Creator() {
            @Override
            public Object createInstance(DeserializationContext ctxt) throws IOException {
                return new HashMap<>();
            }
        };
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class), creator);
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"property1\": \"value1\","
                + "\"typeId1\": \"TypeClass1\","
                + "\"creatorProperty1\": \"CreatorValue1\","
                + "\"typeId2\": \"TypeClass2\","
                + "\"creatorProperty2\": \"CreatorValue2\""
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean, creator);

        Map<String, String> map = (Map<String, String>) bean;
        assertEquals("value1", map.get("property1"));
        assertEquals("TypeClass1", map.get("typeId1"));
        assertEquals("CreatorValue1", map.get("creatorProperty1"));
        assertEquals("TypeClass2", map.get("typeId2"));
        assertEquals("CreatorValue2", map.get("creatorProperty2"));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteWithMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Creator creator = new Creator() {
            @Override
            public Object createInstance(DeserializationContext ctxt) throws IOException {
                return new HashMap<>();
            }
        };
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class), creator);
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"property1\": \"value1\","
                + "\"creatorProperty1\": \"CreatorValue1\""
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean, creator);

        fail("Expected JsonProcessingException but it didn't occur.");
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteWithMissingCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Creator creator = new Creator() {
            @Override
            public Object createInstance(DeserializationContext ctxt) throws IOException {
                return new HashMap<>();
            }
        };
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class), creator);
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"typeId1\": \"TypeClass1\","
                + "\"creatorProperty1\": null"
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean, creator);

        fail("Expected JsonProcessingException but it didn't occur.");
    }

    @Test
    public void testCompleteWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Creator creator = new Creator() {
            @Override
            public Object createInstance(DeserializationContext ctxt) throws IOException {
                return new HashMap<>();
            }
        };
        ExternalTypeHandler handler = createHandler(mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class), creator);
        JsonParser p = mapper.getFactory().createParser("{"
                + "\"property1\": \"value1\","
                + "\"typeId1\": null,"
                + "\"creatorProperty1\": \"CreatorValue1\","
                + "\"typeId2\": null,"
                + "\"creatorProperty2\": \"CreatorValue2\""
                + "}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new HashMap<>();

        handler.complete(p, ctxt, bean, creator);

        Map<String, Object> map = (Map<String, Object>) bean;
        assertEquals(null, map.get("property1"));
        assertEquals(null, map.get("typeId1"));
        assertEquals("CreatorValue1", map.get("creatorProperty1"));
        assertEquals(null, map.get("typeId2"));
        assertEquals("CreatorValue2", map.get("creatorProperty2"));
    }

    private ExternalTypeHandler createHandler(Class<?> targetType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeser = mapper.getTypeFactory().constructValueTypeDeserializer(targetType, null);
        SettableBeanProperty property = new SimpleBeanPropertyReceiver().findProperty("property", targetType);
        SettableBeanProperty typeProperty = new SimpleBeanPropertyReceiver().findProperty("typeId", String.class);
        SettableBeanProperty creatorProperty = new SimpleBeanPropertyReceiver().findProperty("creatorProperty", String.class);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeser);
        builder.addExternal(typeProperty, mapper.getDeserializationConfig().getTypeIdResolver().findValueDeserializer(String.class));
        builder.addExternal(creatorProperty, mapper.getDeserializationConfig().getDefaultCreator());

        return builder.build();
    }

    private ExternalTypeHandler createHandler(Class<?> targetType, Creator creator) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeser = mapper.getTypeFactory().constructValueTypeDeserializer(targetType, null);
        SettableBeanProperty property = new SimpleBeanPropertyReceiver().findProperty("property", targetType);
        SettableBeanProperty typeProperty = new SimpleBeanPropertyReceiver().findProperty("typeId", String.class);
        SettableBeanProperty creatorProperty = new SimpleBeanPropertyReceiver().findProperty("creatorProperty", String.class);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeser);
        builder.addExternal(typeProperty, mapper.getDeserializationConfig().getTypeIdResolver().findValueDeserializer(String.class));
        builder.addExternal(creatorProperty, creator);

        return builder.build();
    }

    static class Creator implements BeanDeserializer {
        @Override
        public Object createInstance(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public BeanDescriptor getBeanDescription() {
            return null;
        }

        @Override
        public void setAllProperties(Object instance, DeserializationContext ctxt) throws IOException {
        }

        @Override
        public void findAndRegisterProperties(BeanPropertyDefinitionPropBuilder builder, BeanPropertyDefinitionBase propertyDefinition) {
        }

        @Override
        public void configure(BeanDescription beanDesc, DeserializationConfig config) throws JsonMappingException {
        }
    }

}