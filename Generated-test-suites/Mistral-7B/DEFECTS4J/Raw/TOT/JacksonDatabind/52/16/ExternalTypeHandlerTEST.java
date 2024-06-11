package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testSimpleHandling() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typeId\": \"StringType\", \"name\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        JsonParser p = mapper.getFactory().createParser(json);
        Object result = handler.handlePropertyValue(p, new DeserializationContext(), "typeId", null);
        assertEquals("StringType", result);
        result = handler.handlePropertyValue(p, new DeserializationContext(), "name", null);
        assertEquals("John", result);
    }

    @Test
    public void testMissingProperty() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        JsonParser p = mapper.getFactory().createParser(json);
        try {
            handler.handlePropertyValue(p, new DeserializationContext(), "typeId", null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing property 'typeId' for external type id 'typeId'", e.getMessage());
        }
    }

    @Test
    public void testMissingTypeId() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        JsonParser p = mapper.getFactory().createParser(json);
        try {
            handler.handlePropertyValue(p, new DeserializationContext(), "typeId", new Object());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing external type id property 'typeId'", e.getMessage());
        }
    }

    @Test
    public void testComplete() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typeId\": \"StringType\", \"name\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        Object result = handler.complete(mapper.getFactory().createParser(json), new DeserializationContext(), null);
        assertEquals("John", result);
    }

    @Test
    public void testCompleteWithNull() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typeId\": null, \"name\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        Object result = handler.complete(mapper.getFactory().createParser(json), new DeserializationContext(), null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithMissingTypeId() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        try {
            handler.complete(mapper.getFactory().createParser(json), new DeserializationContext(), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing external type id property 'typeId'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithMissingProperty() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{" +
                "\"typeId\": \"StringType\"," +
                "\"otherProperty\": \"John\"}";
        ExternalTypeHandler handler = mapper.getDeserializationConfig().findValueDeserializer(String.class, mapper);
        try {
            handler.complete(mapper.getFactory().createParser(json), new DeserializationContext(), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing property 'name' for external type id 'typeId'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithNestedProperties() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PropertyName propertyName = PropertyName.construct("my.nested.property");
        SettableBeanProperty property = new SettableBeanProperty(propertyName, String.class);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        TypeDeserializer nestedTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeserializer);
        builder.addExternal(new SettableBeanProperty(PropertyName.construct("nested.typeId"), String.class), nestedTypeDeserializer);
        ExternalTypeHandler handler = builder.build();
        String json = "{" +
                "\"my.nested.property\": {" +
                "\"nested.typeId\": \"StringType\"," +
                "\"value\": \"nested value\"" +
                "}," +
                "\"anotherProperty\": \"John\"" +
                "}";
        Object result = handler.complete(mapper.getFactory().createParser(json), new DeserializationContext(), null);
        assertEquals("nested value", property.get(result));
        assertEquals("John", result.getClass().getDeclaredField("anotherProperty").get(result));
    }

    @Test
    public void testCompleteWithCreatorProperties() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PropertyName propertyName = PropertyName.construct("my.nested.property");
        SettableBeanProperty property = new SettableBeanProperty(propertyName, String.class);
        SettableBeanProperty creatorProperty = new SettableBeanProperty(PropertyName.construct("creator.property"), Object.class);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        TypeDeserializer nestedTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeserializer);
        builder.addExternal(new SettableBeanProperty(PropertyName.construct("nested.typeId"), String.class), nestedTypeDeserializer);
        builder.addExternal(property, new CreatorForMyNestedProperty());
        Object result = builder.build().complete(mapper.getFactory().createParser(json), new DeserializationContext(), null, new PropertyValueBuffer(), new CreatorForMyNestedProperty());
        assertEquals("nested value", property.get(result));
    }

    static class CreatorForMyNestedProperty extends BeanDeserializerProvider.BeanDeserializerBase {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
            return new Object();
        }

        @Override
        public BeanDescription getBeanDescription() {
            return null;
        }
    }

    @Test
    public void testCompleteWithCreatorPropertiesAndTypeIds() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PropertyName propertyName = PropertyName.construct("my.nested.property");
        SettableBeanProperty property = new SettableBeanProperty(propertyName, String.class);
        SettableBeanProperty creatorProperty = new SettableBeanProperty(PropertyName.construct("creator.property"), Object.class);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        TypeDeserializer nestedTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeserializer);
        builder.addExternal(new SettableBeanProperty(PropertyName.construct("nested.typeId"), String.class), nestedTypeDeserializer);
        builder.addExternal(property, new CreatorForMyNestedProperty());
        Object result = builder.build().complete(mapper.getFactory().createParser(json), new DeserializationContext(), null, new PropertyValueBuffer(), new CreatorForMyNestedProperty());
        assertEquals("nested value", property.get(result));
    }
}