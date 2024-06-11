package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestObject(Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ByteArrayInputStream(
                "{\"foo\":\"bar\",\"baz\":{\"typeId\":\"BazType\",\"value\":123}}"
                        .getBytes()), clazz);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"MyType\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build());
        handler.start();

        assertTrue(handler.handleTypePropertyValue(parser, context, "typeId", null));
        assertEquals("MyType", handler._typeIds[0]);

        parser = mapper.getFactory().createParser("{\"typeId\":\"MyType\",\"foo\":\"bar\"}");
        handler.start();
        assertTrue(handler.handleTypePropertyValue(parser, context, "typeId", null));
        assertEquals("MyType", handler._typeIds[0]);
        assertEquals("bar", handler._properties[0].getProperty().get(handler._tokens[0].asParser(parser).nextValue()));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":\"bar\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .build()).start();

        assertTrue(handler.handlePropertyValue(parser, context, "foo", null));
        assertEquals("bar", handler._properties[0].getProperty().get(handler._tokens[0]));

        parser = mapper.getFactory().createParser("{\"foo\":\"bar\",\"baz\":{\"typeId\":\"BazType\"}}");
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        assertTrue(handler.handlePropertyValue(parser, context, "foo", null));
        assertEquals("bar", handler._properties[0].getProperty().get(handler._tokens[0]));
        assertEquals(null, handler._typeIds[0]);

        parser.nextToken(); // skip "baz"
        assertTrue(handler.handlePropertyValue(parser, context, "typeId", null));
        assertEquals("BazType", handler._typeIds[1]);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":\"bar\",\"baz\":{\"typeId\":\"BazType\"}}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        Object result = handler.complete(parser, context, null);
        assertThat(result, instanceOf(MyType.class));
        assertEquals("bar", handler._properties[0].getProperty().getValue(result));
        assertEquals("BazType", handler._typeIds[1]);
    }

    @Test
    public void testCompletePropertyValueBuffer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":[1,2,3],\"baz\":{\"typeId\":\"BazType\",\"value\":45}}");
        DeserializationContext context = mapper.getDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", List.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        handler.complete(parser, context, buffer, null);
        List<Object> result = (List<Object>) buffer.getBuffer();
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals("BazType", handler._typeIds[1]);
    }

    @Test
    public void testCompleteNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":\"bar\",\"baz\":null}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        Object result = handler.complete(parser, context, null);
        assertEquals(null, handler._typeIds[1]);
    }

    @Test
    public void testCompleteMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":\"bar\"}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        try {
            handler.complete(parser, context, null);
            fail();
        } catch (JsonMappingException expected) {
            assertEquals("Missing property 'baz' for external type id 'BazType'",
                    expected.getMessage());
        }
    }

    @Test
    public void testCompleteMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":\"bar\",\"baz\":123}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        try {
            handler.complete(parser, context, null);
            fail();
        } catch (JsonMappingException expected) {
            assertEquals("Missing external type id property 'typeId' for property 'baz'",
                    expected.getMessage());
        }
    }

    @Test
    public void testCompleteNullObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\":null,\"baz\":{\"typeId\":\"BazType\",\"value\":45}}");
        DeserializationContext context = mapper.getDeserializationContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("foo", String.class), null)
                .addExternal(new SettableBeanProperty("baz", new MyTypeDeserializer())).build()).start();

        Object result = handler.complete(parser, context, null);
        assertNull(result);
    }

    // Add more tests as needed...

    static class MyTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd)
                throws IOException {
            JsonParser p2 = p.getCoercedValueParser();
            String typeId = p2.getValueAsString();
            return new MyType(typeId);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonParser p2 = p.getCoercedValueParser();
            String typeId = p2.getValueAsString();
            return new MyType(typeId);
        }

        @Override
        public String getPropertyName() {
            return "typeId";
        }

        @Override
        public Class<?> getEmptyValueInstantiator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonTypeIdResolver getTypeIdResolver() {
            return new JsonTypeIdResolverBase() {
                @Override
                public String idFromValueAndType(String name, Class<?> valueType) {
                    return "BazType";
                }
            };
        }
    }

    static class MyType {
        private final String typeId;

        public MyType(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public String toString() {
            return "MyType{" + "typeId='" + typeId + '\'' + '}';
        }
    }
}