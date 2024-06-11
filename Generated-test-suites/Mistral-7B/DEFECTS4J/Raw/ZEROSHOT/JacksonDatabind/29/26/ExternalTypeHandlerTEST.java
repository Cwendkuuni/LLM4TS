package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ObjectMapper objectMapper;
    private ExternalTypeHandler handler;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        BeanDeserializerBuilder builder = objectMapper.getDeserializerFactory().findValueDeserializer(String.class, null);
        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(String[].class, builder);

        List<SettableBeanProperty> properties = new ArrayList<>();
        properties.add(new SettableBeanProperty("property1", String.class));
        properties.add(new SettableBeanProperty("property2", String[].class));

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(properties.get(0), null)
                .addExternal(properties.get(1), typeDeserializer)
                .build());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException, JsonProcessingException {
        String json = "{\"property1\":\"StringValue\",\"typeId\":\"StringType\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        assertTrue(handler.handleTypePropertyValue(parser, null, "property1", null));
        assertEquals("StringValue", handler._properties[0].getProperty().get(handler._tokens[0].asParser(parser).readValueAs(String.class)));
        assertEquals("StringType", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException, JsonProcessingException {
        String json = "{\"property1\":[\"StringValue1\",\"StringValue2\"],\"typeId\":\"StringType\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        assertTrue(handler.handlePropertyValue(parser, null, "property1", null));
        JsonNode node = parser.readValueAsTree();
        String[] values = (String[]) handler._properties[1].getProperty().get(handler._tokens[1].asParser(parser).readValueAs(Object.class));
        assertEquals(2, values.length);
        assertEquals("StringValue1", values[0]);
        assertEquals("StringValue2", values[1]);
        assertEquals("StringType", handler._typeIds[1]);
    }

    @Test
    public void testComplete() throws IOException, JsonProcessingException {
        String json = "{\"property1\":[\"StringValue1\",\"StringValue2\"],\"typeId\":\"StringType\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        Object result = handler.complete(parser, null, null);

        JsonNode node = objectMapper.readTree(parser);
        Map<String, Object> map = node.asMap();
        assertEquals(2, map.size());
        assertEquals(propertyFromName("property1"), map.keySet().iterator().next());
        assertEquals(String[].class, map.get("property1").getClass());
        assertEquals(new String[]{"StringValue1", "StringValue2"}, (String[]) map.get("property1"));
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException, JsonProcessingException {
        String json = "{\"property1\":[{\"property2\":\"Value1\"},{\"property2\":\"Value2\"}],\"typeId\":\"StringType\"}";
        JsonParser parser = objectMapper.getFactory().createParser(json);

        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @SuppressWarnings("unchecked")
            @Override
            public Object createUsingDefaults() {
                return new Object[2];
            }

            @Override
            public Property<?>[] getPropertyDescriptors() {
                return new Property[]{propertyFromName("property2")};
            }
        };

        Object result = handler.complete(parser, null, null, creator);

        Map<String, Object> map = (Map<String, Object>) result;
        assertEquals(2, map.size());
        assertEquals(propertyFromName("property1"), map.keySet().iterator().next());
        assertEquals(Object[].class, map.get("property1").getClass());
        Object[] values = (Object[]) map.get("property1");
        assertEquals(2, values.length);
        assertEquals("Value1", values[0]);
        assertEquals("Value2", values[1]);
    }

    private SettableBeanProperty propertyFromName(String name) {
        return new SettableBeanProperty(PropertyName.construct(name), null);
    }

}