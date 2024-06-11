package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ExternalTypeHandlerTest {

    private static class TestBean {
        private String prop1;
        private String prop2;
        private ExternalType externalType;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        public String getProp2() {
            return prop2;
        }

        public void setProp2(String prop2) {
            this.prop2 = prop2;
        }

        public ExternalType getExternalType() {
            return externalType;
        }

        public void setExternalType(ExternalType externalType) {
            this.externalType = externalType;
        }
    }

    private static class ExternalType {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private static class ExternalTypeDeserializer extends StdDeserializer<ExternalType> {
        @Override
        public ExternalType deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            if (p.nextToken() != JsonToken.START_OBJECT) {
                throw ctxt.mappingException("Expected START_OBJECT but found " + p.getCurrentToken());
            }
            String id = p.nextTextValue();
            if (p.nextToken() != JsonToken.END_OBJECT) {
                throw ctxt.mappingException("Expected END_OBJECT but found " + p.getCurrentToken());
            }
            ExternalType extType = new ExternalType();
            extType.setId(id);
            return extType;
        }

        public String getPropertyName() {
            return "externalType";
        }

        public Class<?> getValueInstantiationHandler() {
            return null;
        }
    }

    private ExternalTypeHandler handler;
    private ObjectMapper mapper;
    private DeserializationContext context;
    private JsonParser parser;
    private TestBean bean;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        context = Mockito.mock(DeserializationContext.class);
        parser = Mockito.mock(JsonParser.class);
        bean = new TestBean();

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(Mockito.mock(SettableBeanProperty.class),
                        new ExternalTypeDeserializer())
                .addExternal(Mockito.mock(SettableBeanProperty.class),
                        Mockito.mock(TypeDeserializer.class))
                .build());
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        String json = "{\"prop1\":\"value1\",\"externalType\":\"type1\"}";

        Mockito.when(parser.getText()).thenReturn("type1");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(parser.nextTextValue()).thenReturn("externalType");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.nextTextValue()).thenReturn("type1");

        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        assertNull(bean.getProp1());

        handler.handleTypePropertyValue(parser, context, "externalType", bean);
        assertEquals("type1", bean.getExternalType().getId());

        Mockito.verify(parser, Mockito.times(1)).nextToken();
        Mockito.verify(parser, Mockito.times(1)).getText();
        Mockito.verifyNoMoreInteractions(parser);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        String json = "{\"prop1\":\"value1\",\"externalType\":[\"type1\"]}";

        Mockito.when(parser.getText()).thenReturn("type1");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.nextTextValue()).thenReturn("type1");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        handler.handlePropertyValue(parser, context, "prop1", bean);
        assertNull(bean.getProp1());

        handler.handlePropertyValue(parser, context, "externalType", bean);
        assertEquals("type1", bean.getExternalType().getId());

        Mockito.verify(parser, Mockito.times(2)).nextToken();
        Mockito.verify(parser, Mockito.times(1)).getText();
        Mockito.verifyNoMoreInteractions(parser);
    }

    @Test
    public void testComplete() throws Exception {
        String json = "{\"prop1\":\"value1\",\"externalType\":[\"type1\"]}";

        Mockito.when(parser.getText()).thenReturn("type1");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.nextTextValue()).thenReturn("type1");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "externalType", bean);
        handler.complete(parser, context, bean);

        assertEquals("value1", bean.getProp1());
        assertEquals("type1", bean.getExternalType().getId());

        Mockito.verify(parser, Mockito.times(4)).nextToken();
        Mockito.verifyNoMoreInteractions(parser);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws Exception {
        String json = "[{\"name\":\"TestBean\",\"handler\":\"handler1\",\"properties\":[{" +
                "\"name\":\"prop1\",\"type\":\"String\"}," +
                "{\"name\":\"externalType\",\"type\":\"ExternalType\"}]}," +
                "{{\"name\":\"TestBean\",\"handler\":\"handler2\",\"properties\":[]}}]";

        Map<String, PropertyName> propertyNames = new HashMap<>();
        propertyNames.put("prop1", PropertyName.construct("prop1"));
        propertyNames.put("externalType", PropertyName.construct("externalType"));

        ObjectMapper mapper = new ObjectMapper();
       JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        BeanDeserializerFactory beanDeserializerFactory = new BeanDeserializerFactory(introspector, mapper.getFactory(), mapper.getTypeFactory());
        JsonNode node = mapper.readTree(json);
        TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {};

        List<Map<String, Object>> creators = mapper.readValue(json, typeRef);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);

        for (Map<String, Object> creatorMap : creators) {
            String handlerName = (String) creatorMap.get("handler");
            if (handlerName.equals("handler1")) {
                Mockito.when(creator.build(context, buffer)).thenReturn(new TestBean());
                break;
            }
        }

        handler.complete(parser, context, buffer, creator);
        assertEquals("value1", bean.getProp1());
        assertEquals("type1", bean.getExternalType().getId());

        Mockito.verify(parser, Mockito.times(1)).nextToken();
        Mockito.verifyNoMoreInteractions(parser);
    }
}