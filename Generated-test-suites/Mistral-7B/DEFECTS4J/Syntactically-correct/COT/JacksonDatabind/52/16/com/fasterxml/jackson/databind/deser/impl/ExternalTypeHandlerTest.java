package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.TypeDeserializers.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test cases for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    

    @org.junit.BeforeClass
    public static void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
    }

    @org.junit.AfterClass
    public static void tearDown() throws Exception {
        // No cleanup needed
    }

    /**
     * Test for {@link ExternalTypeHandler#start()} method.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testStart() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        ExternalTypeHandler startHandler = externalTypeHandler.start();
        assertNotSame(externalTypeHandler, startHandler);
        // No further assertions needed as this method doesn't return a value
    }

    /**
     * Test for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * method with valid input.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testHandleTypePropertyValueValid() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        ExternalTypeHandler handler = externalTypeHandler.start();
        String propName = "test";
        String typeId = "StringType";
        jsonParser.nextToken(); // skip [START_OBJECT]
        jsonParser.nextToken(); // skip fieldName: test
        jsonParser.nextValue(); // read StringType as value
        handler.handleTypePropertyValue(jsonParser, context, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._properties.length - 1]);
    }

    /**
     * Test for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * method with invalid input.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testHandleTypePropertyValueInvalid() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        ExternalTypeHandler handler = externalTypeHandler.start();
        String propName = "test";
        jsonParser.nextToken(); // skip [START_OBJECT]
        jsonParser.nextToken(); // skip fieldName: test1
        jsonParser.nextValue(); // read null as value
        try {
            handler.handleTypePropertyValue(jsonParser, context, propName, bean);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        jsonParser.nextToken(); // skip fieldName: test2
        jsonParser.nextValue(); // read StringType as value
        handler.handleTypePropertyValue(jsonParser, context, propName, bean);
        assertEquals("StringType", handler._typeIds[handler._properties.length - 1]);
    }

    /**
     * Test for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * method with valid input.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testHandlePropertyValueValid() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        ExternalTypeHandler handler = externalTypeHandler.start();
        String propName = "test";
        String typeId = "StringType";
        jsonParser.nextToken(); // skip [START_OBJECT]
        jsonParser.nextToken(); // skip fieldName: test
        jsonParser.nextValue(); // read null as value
        handler.handlePropertyValue(jsonParser, context, propName, bean);
        assertNull(handler._typeIds[handler._properties.length - 1]);
        jsonParser.nextToken(); // skip fieldName: test
        jsonParser.nextValue(); // read StringType as value
        handler.handlePropertyValue(jsonParser, context, propName, bean);
        assertEquals("StringType", handler._typeIds[handler._properties.length - 1]);
    }

    /**
     * Test for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)}
     * method with invalid input.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testHandlePropertyValueInvalid() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        ExternalTypeHandler handler = externalTypeHandler.start();
        String propName = "test";
        jsonParser.nextToken(); // skip [START_OBJECT]
        jsonParser.nextToken(); // skip fieldName: test1
        jsonParser.nextValue(); // read null as value
        try {
            handler.handlePropertyValue(jsonParser, context, propName, bean);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        jsonParser.nextToken(); // skip fieldName: test
        jsonParser.nextValue(); // read StringType as value
        handler.handlePropertyValue(jsonParser, context, propName, bean);
        assertEquals("StringType", handler._typeIds[handler._properties.length - 1]);
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
     * method with valid input.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testCompleteValid() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        ExternalTypeHandler handler = externalTypeHandler.start();
        String typeId = "StringType";
        handler.handleTypePropertyValue(jsonParser, context, "test", bean);
        handler.handlePropertyValue(jsonParser, context, "test", bean);
        Object result = handler.complete(jsonParser, context, bean);
        assertEquals(bean.getClass(), result.getClass());
        SettableBeanProperty property = settableBeanProperty;
        property.set(result, typeId);
        assertEquals(typeId, property.get(result));
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)}
     * method with missing type id.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testCompleteMissingTypeId() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        ExternalTypeHandler handler = externalTypeHandler.start();
        String propertyName = "test";
        handler.handlePropertyValue(jsonParser, context, propertyName, bean);
        try {
            handler.complete(jsonParser, context, bean);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object, PropertyValueBuffer, PropertyBasedCreator)}
     * method with valid input.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testCompleteValidWithCreator() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                return new Object();
            }
        };
        ExternalTypeHandler handler = externalTypeHandler.start();
        String typeId = "StringType";
        handler.handleTypePropertyValue(jsonParser, context, "test", bean);
        handler.handlePropertyValue(jsonParser, context, "test", bean);
        handler.complete(jsonParser, context, null, creator);
        Object result = creator.build(context, null);
        assertEquals(bean.getClass(), result.getClass());
        SettableBeanProperty property = settableBeanProperty;
        property.set(result, typeId);
        assertEquals(typeId, property.get(result));
    }

    /**
     * Test for {@link ExternalTypeHandler#_deserialize(JsonParser, DeserializationContext, int, String)}
     * method.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object result = externalTypeHandler._deserialize(jsonParser, context, 0, "StringType");
        assertEquals(String.class, result.getClass());
    }

    /**
     * Test for {@link ExternalTypeHandler#_deserializeAndSet(JsonParser, DeserializationContext, Object, int, String)}
     * method.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = createTestParser();
        DeserializationContext context = createTestContext();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder(), new HashMap<String, Integer>(), null, null).build();
        Object bean = new Object();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(PropertyName.construct("test"), StringDeserializer.instance, bean.getClass());
        externalTypeHandler._deserializeAndSet(jsonParser, context, bean, 0, "StringType");
        assertEquals("StringType", settableBeanProperty.get(bean));
    }

    /**
     * Helper method to create a test parser.
     * @return A JsonParser instance to be used for testing
     * @throws IOException if an I/O error occurs
     */
    private JsonParser createTestParser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser("{}");
    }

    /**
     * Helper method to create a test context.
     * @return A DeserializationContext instance to be used for testing
     */
    private DeserializationContext createTestContext() {
        return new TestDeserializationContext();
    }

    /**
     * A custom DeserializationContext implementation for testing.
     */
    private static class TestDeserializationContext extends DeserializationContextBase {
        @Override
        public Object instantiateValue(Class<?> valueType, JsonParser p, DynamicTypeResolver dynamicTypeResolver)
                throws IOException, InstantiationException {
            return new Object();
        }

        @Override
        public Object instantiateValue(Class<?> valueType, JsonParser p, BeanDescription beanDesc, DynamicTypeResolver dynamicTypeResolver)
                throws IOException, InstantiationException {
            return new Object();
        }
    }

    
}