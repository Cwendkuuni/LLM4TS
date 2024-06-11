package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"name\":\"Prop\",\"type\":\"StringType\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), HashMap.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        assertEquals(true, handler.handleTypePropertyValue(jp, ctxt, "name", root));
        assertEquals("StringType", ((Map) root).get("type"));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"name\":\"Prop\",\"value\":\"Test\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), HashMap.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        assertEquals(true, handler.handlePropertyValue(jp, ctxt, "name", root));
        assertEquals("Test", ((Map) root).get("value"));
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"Prop1\":null,\"Prop2\":null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), HashMap.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        handler.complete(jp, ctxt, root);
        assertEquals(null, root);
    }

    @Test
    public void testCompleteWithValues() throws IOException {
        String json = "{\"Prop1\":{\"type\":\"StringType\",\"value\":\"Test1\"},\"Prop2\":{\"type\":\"IntType\",\"value\":42}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop1"), new StringTypeDeserializer()),
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop2"), new IntTypeDeserializer())},
                new HashMap<>(), new String[]{"StringType", "IntType"}, new TokenBuffer[2]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), SimplePojo.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        handler.handleTypePropertyValue(jp, ctxt, "Prop1.type", root);
        handler.handlePropertyValue(jp, ctxt, "Prop1.value", root);
        handler.handleTypePropertyValue(jp, ctxt, "Prop2.type", root);
        handler.handlePropertyValue(jp, ctxt, "Prop2.value", root);
        handler.start();
        handler.complete(jp, ctxt, root);
        SimplePojo simplePojo = (SimplePojo) root;
        assertEquals("Test1", simplePojo.prop1);
        assertEquals(42, simplePojo.prop2);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String json = "{\"creator\":{\"Prop1\":null,\"Prop2\":null}}";
        SimplePojoCreator creator = new SimplePojoCreator();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop1"), new StringTypeDeserializer()),
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop2"), new IntTypeDeserializer())},
                new HashMap<>(), new String[]{"StringType", "IntType"}, new TokenBuffer[2]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), PropertyValueBuffer.class).getRootAsObject();
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        handler.handleTypePropertyValue(jp, ctxt, "creator.Prop1.type", root);
        handler.handlePropertyValue(jp, ctxt, "creator.Prop1.value", root);
        handler.handleTypePropertyValue(jp, ctxt, "creator.Prop2.type", root);
        handler.handlePropertyValue(jp, ctxt, "creator.Prop2.value", root);
        handler.start();
        SimplePojo simplePojo = handler.complete(jp, ctxt, root, creator);
        assertEquals(null, simplePojo.prop1);
        assertEquals(0, simplePojo.prop2);
    }

    @Test(expected = MappingException.class)
    public void testMissingProperty() throws IOException {
        String json = "{\"Prop1\":null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop1"), new StringTypeDeserializer()),
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop2"), new IntTypeDeserializer())},
                new HashMap<>(), new String[]{"StringType", "IntType"}, new TokenBuffer[2]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), SimplePojo.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        handler.handlePropertyValue(jp, ctxt, "Prop1.value", root);
        handler.start();
        handler.complete(jp, ctxt, root);
    }

    @Test(expected = MappingException.class)
    public void testMissingTypeIdProperty() throws IOException {
        String json = "{\"Prop1\":\"Test\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop1"), new StringTypeDeserializer()),
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop2"), new IntTypeDeserializer())},
                new HashMap<>(), new String[]{"StringType", "IntType"}, new TokenBuffer[2]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), SimplePojo.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        handler.handleTypePropertyValue(jp, ctxt, "Prop1.type", root);
        handler.start();
        handler.complete(jp, ctxt, root);
    }

    @Test
    public void testDefaultType() throws IOException {
        String json = "{\"Prop1\":null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop1"), new StringTypeDeserializer()),
                new ExtTypedProperty(new SimplePojo().getClass().getDeclaredField("prop2"), new IntTypeDeserializer())},
                new HashMap<>(), new String[]{"StringType", "IntType"}, new TokenBuffer[2]);
        Object root = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), SimplePojo.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonParser jp = objectMapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        handler.handlePropertyValue(jp, ctxt, "Prop1.value", root);
        handler.start();
        handler.complete(jp, ctxt, root);
        SimplePojo simplePojo = (SimplePojo) root;
        assertEquals(null, simplePojo.prop1);
        assertEquals(0, simplePojo.prop2);
        SimplePojo simplePojo2 = handler.complete(jp, ctxt, root, null);
        assertEquals(null, simplePojo2.prop1);
        assertEquals(0, simplePojo2.prop2);
    }

    static class SimplePojo {
        String prop1;
        int prop2;

        @JsonCreator
        public SimplePojo(@JsonProperty("prop1") String prop1, @JsonProperty("prop2") int prop2) {
            this.prop1 = prop1;
            this.prop2 = prop2;
        }
    }

    static class SimplePojoCreator {
        public SimplePojo create(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
            return new SimplePojo(buffer.getParameterValue("prop1"), buffer.getParameterValue("prop2").asInt());
        }
    }

    static class StringTypeDeserializer extends StdScalarTypeDeserializer<String> {
        public StringTypeDeserializer() {
            super(String.class);
        }

        @Override
        public String getPropertyName() {
            return "type";
        }
    }

    static class IntTypeDeserializer extends StdScalarTypeDeserializer<Integer> {
        public IntTypeDeserializer() {
            super(Integer.class);
        }

        @Override
        public String getPropertyName() {
            return "type";
        }
    }
}