package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test cases for ExternalTypeHandler class.
 */
public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static ExternalTypeHandler createHandler(ExtTypedProperty[] properties,
            HashMap<String, Integer> nameToPropertyIndex) {
        String[] typeIds = new String[properties.length];
        TokenBuffer[] tokens = new TokenBuffer[properties.length];
        return new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    /**
     * Test for handleTypePropertyValue method.
     */
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SimplePojo(), StringDeserializer.instance);
        properties[1] = new ExtTypedProperty(new SimplePojo(), NumberDeserializer.instance);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        Object bean = new SimplePojo();
        JsonParser jp = new JsonFactory().createParser("{\"typeProperty0\":\"StringTypeId\",\"property1\":1}");

        // First test handleTypePropertyValue call
        handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty0", bean);
        assertEquals("StringTypeId", handler.getTypeIds()[0]);
        handler.handleTypePropertyValue(jp, new TestContext(), "property1", bean);
        assertEquals("1", handler.getTypeIds()[1]);

        // Test handling same property multiple times
        jp.nextToken(); // Move past "}"
        handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty0", bean);
        handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty0", bean);
        assertEquals("StringTypeId", handler.getTypeIds()[0]);
        assertEquals("StringTypeId", handler.getTypeIds()[1]);

        // Test handling null property
        handler.handleTypePropertyValue(jp, new TestContext(), null, bean);
        assertNull(handler.getTypeIds());
    }

    /**
     * Test for handlePropertyValue method.
     */
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SimplePojo(), StringDeserializer.instance);
        properties[1] = new ExtTypedProperty(new SimplePojo(), NumberDeserializer.instance);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        Object bean = new SimplePojo();
        JsonParser jp = new JsonFactory().createParser("{\"property0\":\"StringValue\",\"property1\":1}");

        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        assertEquals("StringValue", handler.getTypeIds()[0]);
        handler.handlePropertyValue(jp, new TestContext(), "property1", bean);
        assertEquals("1", handler.getTypeIds()[1]);

        // Test handling same property multiple times
        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        assertEquals("StringValue", handler.getTypeIds()[0]);
        assertEquals("StringValue", handler.getTypeIds()[1]);

        // Test handling null property
        handler.handlePropertyValue(jp, new TestContext(), null, bean);
        assertNull(handler.getTypeIds());
    }

    /**
     * Test for complete method.
     */
    @Test
    public void testComplete() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SimplePojo(), StringDeserializer.instance);
        properties[1] = new ExtTypedProperty(new SimplePojo(), NumberDeserializer.instance);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        Object bean = new SimplePojo();
        JsonParser jp = new JsonFactory().createParser("{\"property0\":\"StringValue\",\"property1\":1}");
        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        handler.handlePropertyValue(jp, new TestContext(), "property1", bean);
        handler.complete(jp, new TestContext(), bean);

        assertEquals("StringValue", handler.getProperty(0).get(bean));
        assertEquals(1, handler.getProperty(1).get(bean));

        // Test handling missing type id property
        handler = createHandler(properties, nameToPropertyIndex);
        jp = new JsonFactory().createParser("{\"property0\":1}");
        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        handler.complete(jp, new TestContext(), bean);
        assertNull(handler.getTypeIds());

        // Test handling missing property
        handler = createHandler(properties, nameToPropertyIndex);
        jp = new JsonFactory().createParser("{\"TypeIdProperty\":\"StringTypeId\"}");
        handler.handleTypePropertyValue(jp, new TestContext(), "TypeIdProperty", bean);
        handler.complete(jp, new TestContext(), bean);
        assertNull(handler.getTypeIds());
    }

    /**
     * Test for complete method with PropertyValueBuffer and PropertyBasedCreator.
     */
    @Test
    public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SimplePojo(), StringDeserializer.instance);
        properties[1] = new ExtTypedProperty(new SimplePojo(), NumberDeserializer.instance);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = createHandler(properties, nameToPropertyIndex);

        Object bean = new SimplePojo();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new SimpleCreator();

        JsonParser jp = new JsonFactory().createParser("{\"property0\":\"StringValue\",\"property1\":1}");
        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        handler.handlePropertyValue(jp, new TestContext(), "property1", bean);
        handler.complete(jp, new TestContext(), buffer, creator);

        assertEquals("StringValue", handler.getProperty(0).get(bean));
        assertEquals(1, handler.getProperty(1).get(bean));

        // Test handling missing type id property
        handler = createHandler(properties, nameToPropertyIndex);
        jp = new JsonFactory().createParser("{\"property0\":1}");
        handler.handlePropertyValue(jp, new TestContext(), "property0", bean);
        handler.complete(jp, new TestContext(), buffer, creator);
        assertNull(handler.getTypeIds());

        // Test handling missing property
        handler = createHandler(properties, nameToPropertyIndex);
        jp = new JsonFactory().createParser("{\"TypeIdProperty\":\"StringTypeId\"}");
        handler.handleTypePropertyValue(jp, new TestContext(), "TypeIdProperty", bean);
        handler.complete(jp, new TestContext(), buffer, creator);
        assertNull(handler.getTypeIds());
    }

    /**
     * Test for _deserialize method.
     */
    @Test
    public void testDeserialize() throws IOException {
        StringDeserializer stringDeserializer = new StringDeserializer();
        NumberDeserializer numberDeserializer = new NumberDeserializer();
        SimplePojo pojo = new SimplePojo();

        JsonParser jp = new JsonFactory().createParser("{\"typeId\":\"StringTypeId\",\"value\":\"StringValue\"}");
        Object result = _deserialize(jp, new TestContext(), 0, "StringTypeId");
        assertEquals("StringValue", result);

        jp = new JsonFactory().createParser("{\"typeId\":\"NumberTypeId\",\"value\":1}");
        result = _deserialize(jp, new TestContext(), 1, "NumberTypeId");
        assertEquals(1, result);

        jp = new JsonFactory().createParser("{\"typeId\":\"NumberTypeId\",\"value\":null}");
        result = _deserialize(jp, new TestContext(), 1, "NumberTypeId");
        assertNull(result);
    }

    /**
     * Test for _deserializeAndSet method.
     */
    @Test
    public void testDeserializeAndSet() throws IOException {
        StringDeserializer stringDeserializer = new StringDeserializer();
        NumberDeserializer numberDesializer = new NumberDeserializer();
        SimplePojo pojo = new SimplePojo();

        JsonParser jp = new JsonFactory().createParser("{\"typeId\":\"StringTypeId\",\"value\":\"StringValue\"}");
        _deserializeAndSet(jp, new TestContext(), pojo, 0, "StringTypeId");
        assertEquals("StringValue", pojo.getStringProperty());

        jp = new JsonFactory().createParser("{\"typeId\":\"NumberTypeId\",\"value\":1}");
        _deserializeAndSet(jp, new TestContext(), pojo, 1, "NumberTypeId");
        assertEquals(1, pojo.getNumberProperty());

        jp = new JsonFactory().createParser("{\"typeId\":\"NumberTypeId\",\"value\":null}");
        _deserializeAndSet(jp, new TestContext(), pojo, 1, "NumberTypeId");
        assertNull(pojo.getNumberProperty());
    }

    /**
     * Private helper method for _deserialize test.
     */
    private Object _deserialize(JsonParser jp, DeserializationContext ctxt, int index, String typeId)
            throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.handleTypePropertyValue(jp, ctxt, "typeId", null);
        handler.handlePropertyValue(jp, ctxt, null, null);
        return handler._deserialize(jp, ctxt, index, typeId);
    }

    /**
     * Private helper method for _deserializeAndSet test.
     */
    private void _deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object bean, int index, String typeId) throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        handler.handleTypePropertyValue(jp, ctxt, "typeId", null);
        handler.handlePropertyValue(jp, ctxt, null, bean);
        handler._deserializeAndSet(jp, ctxt, bean, index, typeId);
    }

    private static class SimplePojo {
        private String stringProperty;
        private Number numberProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String value) {
            this.stringProperty = value;
        }

        public Number getNumberProperty() {
            return numberProperty;
        }

        public void setNumberProperty(Number value) {
            this.numberProperty = value;
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectInstanceFinder defaultInstanceFinder() {
            return null;
        }

        @Override
        public BeanDescription defaultBeanDescription() {
            return null;
        }

        @Override
        public ObjectMapper defaultObjectMapper() {
            return null;
        }

        @Override
        public ObjectFactory defaultObjectFactory() {
            return null;
        }

        @Override
        public JsonFactory defaultJsonFactory() {
            return new JsonFactory();
        }
    }

    private static class SimpleCreator extends BaseJsonDeserializer<SimplePojo> {
        @Override
        public SimplePojo createInstance(DeserializationContext ctxt) throws IOException {
            return new SimplePojo();
        }

        @Override
        public SimplePojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new SimplePojo();
        }
    }
}