package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanPropertyDescriptor;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        List<BeanPropertyDescriptor> properties = new ArrayList<>();
        properties.add(new BeanPropertyDescriptor("prop1", PropertyName.construct("prop1"), String.class));
        properties.add(new BeanPropertyDescriptor("prop2", PropertyName.construct("prop2"), String.class));

        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("prop1", 0);
        nameToPropertyIndex.put("prop2", 1);

        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(properties.get(0), new StringTypeDeserializer())
                .addExternal(properties.get(1), new NumberTypeDeserializer())
                .build());

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"prop1\":\"StringType\", \"prop2\":null}");
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "prop1", bean);
        assertEquals("StringType", handler._typeIds[0]);
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);

        jp = new JsonFactory().createParser("{\"prop1\":null, \"prop2\":\"NumberType\"}");
        handler.handleTypePropertyValue(jp, ctxt, "prop2", bean);
        assertEquals(null, handler._typeIds[0]);
        assertEquals("NumberType", handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"prop1\":\"StringValue\", \"prop2\":null}");
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        assertEquals(null, handler._typeIds);
        assertEquals("StringValue", handler._tokens[0].textValue());
        assertNull(handler._tokens[1]);

        jp = new JsonFactory().createParser("{\"prop1\":null, \"prop2\":5}");
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);
        assertEquals(null, handler._typeIds);
        assertEquals(5, handler._tokens[0].intValue());
        assertNull(handler._tokens[1]);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"prop1\":\"StringValue\"}");
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop3", bean);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"prop1\":\"StringType\", \"prop2\":5}");
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.complete(jp, ctxt, bean);
        assertEquals("StringType", handler._typeIds[0]);
        assertEquals(5, handler._tokens[1].intValue());
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[0]);

        handler.reset();

        jp = new JsonFactory().createParser("{\"prop1\":null, \"prop2\":null}");
        handler.complete(jp, ctxt, bean);
        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object createUsingDefaults() {
                return new Object();
            }
        };

        JsonParser jp = new JsonFactory().createParser("{\"prop1\":null, \"prop2\":5}");
        DeserializationContext ctxt = new StdDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();

        handler.complete(jp, ctxt, buffer, creator);
        assertNotNull(buffer.getValue(0));
        assertNotNull(buffer.getValue(1));
    }

    @Test
    public void testSerialize() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonParser jp = new JsonFactory().createParser(objectMapper.writeValueAsString(new ExternalTypeHandlerTestData()));
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);

        handler.complete(jp, ctxt, bean);

        Object map = objectMapper.readValue(jp, Map.class);
        assertEquals(2, map.size());
        assertEquals("StringType", map.get("prop1"));
        assertEquals(5, map.get("prop2"));
    }

    @Test
    public void testReset() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"prop1\":\"StringType\"}");
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);

        handler.reset();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidJson() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{\"prop1\":\"StringType\"}");
        DeserializationContext ctxt = new StdDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
    }

    public static class ExternalTypeHandlerTestData {
        String prop1 = "StringType";
        int prop2 = 5;
    }

    public static class PropertyBasedCreator {
        public Object createUsingDefaults() {
            return new Object();
        }
    }

    public static class StringTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public BeanPropertyDescriptor findBeanProperty(String propertyName, int index) {
            return null;
        }

        @Override
        public PropertyName getPropertyName() {
            return null;
        }

        @Override
        public void serializeWithType(JsonGenerator jg, SerializerProvider sp, Object value, TypeSerializer typeSerializer)
                throws IOException {
            jg.writeStringValue((String) value);
        }
    }

    public static class NumberTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }

        @Override
        public BeanPropertyDescriptor findBeanProperty(String propertyName, int index) {
            return null;
        }

        @Override
        public PropertyName getPropertyName() {
            return null;
        }

        @Override
        public void serializeWithType(JsonGenerator jg, SerializerProvider sp, Object value, TypeSerializer typeSerializer)
                throws IOException {
            jg.writeNumber((Number) value);
        }
    }

    public static class PropertyValueBuffer {
        private Object[] values = new Object[16];

        public void assignParameter(BeanPropertyDescriptor prop, Object value) {
            values[prop.getIndex()] = value;
        }

        public Object getValue(int index) {
            return values[index];
        }
    }
}