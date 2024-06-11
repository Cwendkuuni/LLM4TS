package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

/**
 * Test cases for class {@link ExternalTypeHandler}.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() {
        // Test constructor
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        SettableBeanProperty property = new SimpleBeanProperty("testProperty");
        TypeDeserializer typeDeserializer = new StubTypeDeserializer(property.getType());
        properties[0] = new ExtTypedProperty(property, typeDeserializer);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        assertEquals(1, handler._properties.length);
        assertEquals(properties[0], handler._properties[0]);

        // Test start method
        ExternalTypeHandler startedHandler = handler.start();
        assertEquals(handler, startedHandler);

        // Test handleTypePropertyValue method
        JsonParser p = createMockJsonParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProperty";
        assertTrue(handler.handleTypePropertyValue(p, ctxt, propName, bean));
        assertNotNull(handler._typeIds);
        assertEquals(1, handler._typeIds.length);
        assertEquals("typeId", handler._typeIds[0]);

        // Test handlePropertyValue method
        handler._typeIds = null;
        handler._tokens = new TokenBuffer[1][];
        handler._tokens[0] = new TokenBuffer[0];
        assertTrue(handler.handlePropertyValue(p, ctxt, propName, bean));
        assertNotNull(handler._tokens[0]);

        // Test complete method
        Object result = handler.complete(p, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testHandleMissingTypeProperty() {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        SettableBeanProperty property = new SimpleBeanProperty("testProperty");
        TypeDeserializer typeDeserializer = new StubTypeDeserializer(property.getType());
        properties[0] = new ExtTypedProperty(property, typeDeserializer);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        JsonParser p = createMockJsonParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "missingTypeProperty";
        assertFalse(handler.handleTypePropertyValue(p, ctxt, propName, bean));
        assertFalse(handler.handlePropertyValue(p, ctxt, propName, bean));
    }

    @Test
    public void testHandleMissingProperty() {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        SettableBeanProperty property = new SimpleBeanProperty("testProperty");
        TypeDeserializer typeDeserializer = new StubTypeDeserializer(property.getType());
        properties[0] = new ExtTypedProperty(property, typeDeserializer);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        JsonParser p = createMockJsonParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProperty";
        handler._properties = null;
        assertFalse(handler.handleTypePropertyValue(p, ctxt, propName, bean));
        assertFalse(handler.handlePropertyValue(p, ctxt, propName, bean));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        SettableBeanProperty property = new SimpleBeanProperty("testProperty");
        TypeDeserializer typeDeserializer = new StubTypeDeserializer(property.getType());
        properties[0] = new ExtTypedProperty(property, typeDeserializer);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        JsonParser p = createMockJsonParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProperty";
        handler._properties = null;
        handler._typeIds = new String[1];
        handler._typeIds[0] = "typeId";
        handler._tokens = new TokenBuffer[1][];
        handler._tokens[0] = new TokenBuffer();
        handler._tokens[0].copyCurrentStructure(p);
        handler._deserializeAndSet(p, ctxt, bean, 0, "typeId");
        assertEquals("typeId", handler._properties[0].getProperty().get(bean));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        SettableBeanProperty property = new SimpleBeanProperty("testProperty");
        SettableBeanProperty creatorProperty = new SimpleBeanProperty("creatorProperty");
        TypeDeserializer typeDeserializer = new StubTypeDeserializer(property.getType());
        TypeDeserializer creatorTypeDeserializer = new StubTypeDeserializer(creatorProperty.getType());
        properties[0] = new ExtTypedProperty(property, typeDeserializer).linkTypeProperty(creatorProperty);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);
        JsonParser p = createMockJsonParser();
        DeserializationContext ctxt = createMockContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer(new SimpleBeanProperty("testBean"));
        PropertyBasedCreator creator = new SimpleCreator();
        Object bean = creator.buildInstance(ctxt, null);
        String propName = "testProperty";
        handler._properties = null;
        handler._typeIds = new String[1];
        handler._typeIds[0] = "typeId";
        handler._tokens = new TokenBuffer[1][];
        handler._tokens[0] = new TokenBuffer();
        handler._deserialize(p, ctxt, 0, "typeId");
        handler.complete(p, ctxt, buffer, creator);
        assertEquals("typeId", handler._properties[0].getProperty().get(bean));
        assertEquals("creatorProperty", handler._properties[0].getTypeProperty().get(bean));
    }

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return mock(DeserializationContext.class);
    }

    private static class StubTypeDeserializer extends TypeDeserializer {
        private final SettableBeanProperty _property;

        public StubTypeDeserializer(Class<?> type) {
            super(type);
            _property = new SimpleBeanProperty("testProperty");
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public SettableBeanProperty getPropertyName() {
            return _property;
        }

        @Override
        public String getPropertyName(DeserializationContext ctxt) throws IOException {
            return _property.getName();
        }

        @Override
        public Class<?> getValueInstantiator(DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class SimpleBeanProperty {
        private final String _name;

        public SimpleBeanProperty(String name) {
            _name = name;
        }

        public String getName() {
            return _name;
        }

        public void set(Object bean, Object value) {
            // Do nothing
        }

        public Object get(Object bean) {
            return null;
        }

        public void setValue(Object bean, Object value) {
            // Do nothing
        }
    }

    private static class SimpleCreator implements PropertyBasedCreator {
        @Override
        public Object buildInstance(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public void configure(DeserializationContext ctxt) throws IOException {
            // Do nothing
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public void configure(DeserializationContext ctxt, BeanDesc beanDesc) throws IOException {
            // Do nothing
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt, PropertyName propertyName) throws IOException {
            return new Object();
        }

        @Override
        public void configure(DeserializationContext ctxt, BeanFeature feature) throws IOException {
            // Do nothing
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt, JsonDeserializer<?> deserializer) throws IOException {
            return new Object();
        }

        @Override
        public void configure(DeserializationContext ctxt, AnnotatedClass annotatedClass) throws IOException {
            // Do nothing
        }

        @Override
        public void configure(DeserializationContext ctxt, AnnotatedMember am) throws IOException {
            // Do nothing
        }

        @Override
        public void configure(DeserializationContext ctxt, AnnotatedMethod am) throws IOException {
            // Do nothing
        }

        @Override
        public void configure(DeserializationContext ctxt, AnnotatedConstructor ac) throws IOException {
            // Do nothing
        }
    }
}