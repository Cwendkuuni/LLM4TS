package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":\"type1\",\"prop2\":{\"typeId\":\"type2\"}}");
        DeserializationContext ctxt = createContext();

        assertTrue(handler.handleTypePropertyValue(jsonParser, ctxt, "prop1", null));
        assertEquals("type1", handler.getTypeIds()[0]);

        assertTrue(handler.handleTypePropertyValue(jsonParser, ctxt, "prop2.typeId", null));
        assertEquals("type2", handler.getTypeIds()[1]);

        assertFalse(handler.handleTypePropertyValue(jsonParser, ctxt, "prop3", null));

        assertNull(handler.getTypeIds());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":\"val1\",\"prop2\":{}}");
        DeserializationContext ctxt = createContext();

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2.prop1", null));
        assertNull(handler.getTypeIds());

        jsonParser = createParser("{\"prop1\":\"val1\",\"prop2\":{\"typeId\":\"type2\"}}");
        handler = newTestHandler();

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2.typeId", null));
        assertEquals("type2", handler.getTypeIds()[0]);

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2.prop1", null));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":\"val1\",\"prop2\":{\"typeId\":\"type2\"}}");
        DeserializationContext ctxt = createContext();

        Object bean = handler.complete(jsonParser, ctxt, null);

        assertEquals("val1", handler.getProperties()[0].getProperty().get(bean));
        assertEquals("type2", handler.getTypeIds()[0]);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        JsonParser jsonParser = createParser("{\"prop1\":\"val1\",\"prop2\":{\"typeId\":\"type2\"}}");
        DeserializationContext ctxt = createContext();
        PropertyBasedCreator creator = new PropertyBasedCreatorMock();

        Object bean = handler.complete(jsonParser, ctxt, buffer, creator);

        assertEquals("val1", handler.getProperties()[0].getProperty().get(bean));
        assertEquals("type2", handler.getTypeIds()[0]);
    }

    @Test
    public void testHandleTypePropertyValueWithNullBean() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":null,\"prop2\":{\"typeId\":\"type2\"}}");
        DeserializationContext ctxt = createContext();

        assertFalse(handler.handleTypePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handleTypePropertyValue(jsonParser, ctxt, "prop2.typeId", null));
        assertEquals("type2", handler.getTypeIds()[0]);
    }

    @Test
    public void testHandlePropertyValueWithNullBean() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":null,\"prop2\":{}}");
        DeserializationContext ctxt = createContext();

        assertFalse(handler.handlePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2", null));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testHandleTypePropertyValueWithMissingTypeId() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":null,\"prop2\":{}}");
        DeserializationContext ctxt = createContext();

        assertFalse(handler.handleTypePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2", null));
        assertNull(handler.getTypeIds());

        handler = new TestHandlerWithDefaultType();
        jsonParser = createParser("{\"prop1\":null,\"prop2\":{}}");

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop2", null));
        assertEquals("defaultType", handler.getTypeIds()[0]);
    }

    @Test
    public void testHandlePropertyValueWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":\"val1\",\"prop2.missingProp\":{}}");
        DeserializationContext ctxt = createContext();

        assertTrue(handler.handlePropertyValue(jsonParser, ctxt, "prop1", null));
        assertNull(handler.getTypeIds());

        assertFalse(handler.handlePropertyValue(jsonParser, ctxt, "prop2.missingProp", null));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":\"val1\",\"prop2\":{}}");
        DeserializationContext ctxt = createContext();

        Object bean = handler.complete(jsonParser, ctxt, null);

        assertEquals("val1", handler.getProperties()[0].getProperty().get(bean));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = newTestHandler();
        JsonParser jsonParser = createParser("{\"prop1\":\"val1\",\"prop2.missingProp\":{}}");
        DeserializationContext ctxt = createContext();

        Object bean = handler.complete(jsonParser, ctxt, null);

        assertEquals("val1", handler.getProperties()[0].getProperty().get(bean));
        assertNull(handler.getTypeIds());
    }

    private ExternalTypeHandler newTestHandler() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        ObjectReader reader = mapper.readerForUpdating(ExternalTypeHandler.class);
        return reader.readValue(factory.createParser(getClass().getResource("/test-data.json")), ExternalTypeHandler.class);
    }

    private ExternalTypeHandler newTestHandlerWithDefaultType() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        ObjectReader reader = mapper.readerForUpdating(TestHandlerWithDefaultType.class);
        return reader.readValue(factory.createParser(getClass().getResource("/test-data-default-type.json")), ExternalTypeHandler.class);
    }

    private JsonParser createParser(String content) throws IOException {
        JsonFactory factory = new ObjectMapper().getFactory();
        return factory.createParser(new ByteArrayInputStream(content.getBytes()));
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }

    private static class TestContext extends BaseTestContext {
        @Override
        public JsonFactory getFactory() {
            return new ObjectMapper().getFactory();
        }
    }

    private static class TestHandlerWithDefaultType {
        private final ExtTypedProperty[] _properties;
        private final HashMap<String, Integer> _nameToPropertyIndex;

        public TestHandlerWithDefaultType() {
            _properties = new ExtTypedProperty[1];
            _nameToPropertyIndex = new HashMap<>();

            _properties[0] = new ExtTypedProperty(new SettableBeanPropertyMock(), new DefaultTypeDeserializerMock());
            _nameToPropertyIndex.put("property1", 0);
        }

        public String[] getTypeIds() {
            return _typeIds;
        }

        public TokenBuffer[] getTokens() {
            return _tokens;
        }

        public ExtTypedProperty[] getProperties() {
            return _properties;
        }
    }

    private static class DefaultTypeDeserializerMock extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
            return "defaultType";
        }

        @Override
        public String getPropertyName() {
            return "defaultPropertyName";
        }
    }

    private static class ExtTypedProperty {
        private final SettableBeanProperty _property;
        private final TypeDeserializer _typeDeserializer;
        private final String _typePropertyName;
        private String _typeId;
        private TokenBuffer _tokens;

        public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser) {
            _property = property;
            _typeDeserializer = typeDeser;
            _typePropertyName = typeDeser.getPropertyName();
        }

        public boolean hasTypePropertyName(String n) {
            return n.equals(_typePropertyName);
        }

        public boolean hasDefaultType() {
            return _typeDeserializer.getDefaultImpl() != null;
        }

        public String getDefaultTypeId() {
            Class<?> defaultType = _typeDeserializer.getDefaultImpl();
            if (defaultType == null) {
                return null;
            }
            return _typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
        }

        public String getTypePropertyName() {
            return _typePropertyName;
        }

        public String getTypeId() {
            return _typeId;
        }

        public void setTypeId(String typeId) {
            _typeId = typeId;
        }

        public SettableBeanProperty getProperty() {
            return _property;
        }

        public TokenBuffer getTokens() {
            return _tokens;
        }

        public void setTokens(TokenBuffer tokens) {
            _tokens = tokens;
        }
    }

    private static class PropertyBasedCreatorMock implements PropertyBasedCreator {
        @Override
        public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
            return pvb.getCreatorProperties().get(0).getValue();
        }

        @Override
        public Map<String, SettableBeanProperty> findCreatorProperties(Object param) {
            Map<String, SettableBeanProperty> properties = new HashMap<>();
            properties.put("property1", new SettableBeanPropertyMock());
            return properties;
        }
    }
}