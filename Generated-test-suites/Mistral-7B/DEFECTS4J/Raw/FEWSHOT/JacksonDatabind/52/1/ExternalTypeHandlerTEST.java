package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws IOException {
        // Test constructor
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
        assertNotNull(handler);

        // Test handleTypePropertyValue
        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();
        String propName = "name0";

        handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertEquals("testTypeId0", handler.getTypeIds()[0]);

        // Test handlePropertyValue
        handler.handlePropertyValue(p, ctxt, propName, bean);
        assertNull(handler.getTypeIds()[0]);
        assertNotNull(handler.getTokens()[0]);

        // Test complete
        handler.complete(p, ctxt, bean);
        assertEquals("testValue0", handler.getTokens()[0].getText());
        assertEquals("testTypeId0", handler.getTypeIds()[0]);
    }

    @Test
    public void testExternalTypeHandlerWithCreator() throws IOException {
        // Test constructor
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        // Test complete with PropertyValueBuffer and PropertyBasedCreator
        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = mockCreator();

        handler.complete(p, ctxt, buffer, creator);
        assertEquals("testValue0", buffer.getProperties()[0].getValue());
        assertEquals("testTypeId0", handler.getTypeIds()[0]);
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        // Test handleTypePropertyValue with missing property
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();
        String propName = "missingProperty";

        assertFalse(handler.handleTypePropertyValue(p, ctxt, propName, bean));
    }

    @Test
    public void testHandlePropertyValueMissingTypeId() throws IOException {
        // Test handlePropertyValue with missing type id
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();
        String propName = "name0";

        assertFalse(handler.handlePropertyValue(p, ctxt, propName, bean));
    }

    @Test
    public void testHandlePropertyValueMissingToken() throws IOException {
        // Test handlePropertyValue with missing token
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();
        String propName = "name0";

        // Clear token
        handler._tokens[0] = null;

        assertFalse(handler.handlePropertyValue(p, ctxt, propName, bean));
    }

    @Test
    public void testHandlePropertyValueNullBean() throws IOException {
        // Test handlePropertyValue with null bean
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        String propName = "name0";

        assertFalse(handler.handlePropertyValue(p, ctxt, propName, null));
    }

    @Test
    public void testCompleteMissingTypeId() throws IOException {
        // Test complete with missing type id
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();

        handler.complete(p, ctxt, bean);

        assertNull(handler.getTypeIds()[0]);
    }

    @Test
    public void testCompleteMissingProperty() throws IOException {
        // Test complete with missing property
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();
        Object bean = new Object();

        handler.complete(p, ctxt, bean);

        assertNull(handler.getTypeIds()[0]);
    }

    @Test
    public void testCompleteNullBean() throws IOException {
        // Test complete with null bean
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new TestProperty("name0"), new TestTypeDeserializer());

        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);

        JsonParser p = createMockParser();
        DeserializationContext ctxt = mockContext();

        handler.complete(p, ctxt, null);

        assertNull(handler.getTypeIds()[0]);
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }

    private PropertyBasedCreator mockCreator() {
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        when(creator.build(eq(ctxt), eq(buffer))).thenReturn(new Object());
        return creator;
    }

    private static class TestProperty {
        private final String name;

        public TestProperty(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class TestTypeDeserializer extends TypeDeserializer {
        private static final String PROPERTY_NAME = "testTypeProperty";
        private static final String TYPE_ID = "testTypeId0";

        public TestTypeDeserializer() {
            super(String.class, null);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public String getPropertyName() {
            return PROPERTY_NAME;
        }

        @Override
        public String getTypeId() {
            return TYPE_ID;
        }
    }
}