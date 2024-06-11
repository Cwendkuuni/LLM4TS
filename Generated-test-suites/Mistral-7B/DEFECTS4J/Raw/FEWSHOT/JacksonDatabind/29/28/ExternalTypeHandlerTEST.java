package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.*;

import java.io.*;
import java.util.*;

/**
 * Test cases for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "typePropertyName");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), new String[0], null);
        JsonParser jp = createTestParser();
        String json = "{\"typePropertyName\":\"StringType\"}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);
        assertEquals("StringType", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler = handler.start();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);
        assertNull(handler._typeIds[0]);
        assertTrue(handler._tokens[0] != null);
        JsonToken token = handler._tokens[0].currentToken();
        assertEquals(JsonToken.START_OBJECT, token.id());
        assertEquals("StringType", handler._tokens[0].getText());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "propertyName");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), new String[0], null);
        JsonParser jp = createTestParser();
        String json = "{\"propertyName\":\"value\"}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "propertyName", bean);
        assertEquals("value", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler = handler.start();

        handler.handlePropertyValue(jp, ctxt, "propertyName", bean);
        assertNull(handler._typeIds[0]);
        assertTrue(handler._tokens[0] != null);
        JsonToken token = handler._tokens[0].currentToken();
        assertEquals(JsonToken.START_OBJECT, token.id());

        handler._deserializeAndSet(jp, ctxt, bean, 0, "StringType");
        assertEquals("value", bean.getClass().getDeclaredField("value").get(bean));
    }

    @Test
    public void testComplete() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "typePropertyName");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), new String[0], null);
        JsonParser jp = createTestParser();
        String json = "{\"typePropertyName\":\"StringType1\"}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        handler.complete(jp, ctxt, bean);
        assertEquals("StringType1", bean.getClass().getDeclaredField("type").get(bean));

        json = "{\"typePropertyName\":\"StringType1\"}";
        handler._typeIds = new String[1];
        handler._tokens = new TokenBuffer[1];
        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);
        handler._typeIds[0] = "StringType1";
        handler = handler.start();
        handler.complete(jp, ctxt, bean);
        assertEquals("StringType1", bean.getClass().getDeclaredField("type").get(bean));
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "typePropertyName1");
        properties[1] = new ExtTypedProperty(new StdSerializer<>(Integer.class), Integer.class, "typePropertyName2");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                Object result = handler.complete(ctxt, buffer);
                return new Object() {
                    String type1 = (String) result.getClass().getDeclaredField("type1").get(result);
                    int type2 = (Integer) result.getClass().getDeclaredField("type2").get(result);
                    public String getType1() { return type1; }
                    public int getType2() { return type2; }
                };
            }
        };
        JsonParser jp = createTestParser();
        String json = "{" +
                "  \"typePropertyName1\":\"StringType1\"," +
                "  \"typePropertyName2\":42" +
                "}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = creator.createUsingDefaultValues();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName1", bean);
        handler.handlePropertyValue(jp, ctxt, "typePropertyName2", bean);

        handler = handler.start();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName1", bean);
        handler.handlePropertyValue(jp, ctxt, "typePropertyName2", bean);

        Object result = creator.build(ctxt, new PropertyValueBuffer());
        assertEquals("StringType1", result.getClass().getDeclaredField("type1").get(result));
        assertEquals(42, result.getClass().getDeclaredField("type2").get(result));
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "typePropertyName");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
        JsonParser jp = createTestParser();
        String json = "{\"typePropertyName\":null}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        handler.complete(jp, ctxt, bean);
        assertNull(bean.getClass().getDeclaredField("type").get(bean));

        json = "{\"typePropertyName\":null}";
        handler._typeIds = new String[1];
        handler._tokens = new TokenBuffer[1];
        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);
        handler._typeIds[0] = null;
        handler = handler.start();
        handler.complete(jp, ctxt, bean);
        assertNull(bean.getClass().getDeclaredField("type").get(bean));
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "typePropertyName");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
        JsonParser jp = createTestParser();
        String json = "{}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'typePropertyName' for external type id 'null'", e.getMessage());
        }

        json = "";
        handler._typeIds = new String[1];
        handler._tokens = new TokenBuffer[1];
        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);
        handler._typeIds[0] = null;
        handler = handler.start();
        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'typePropertyName' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        properties[0] = new ExtTypedProperty(new StdSerializer<>(String.class), String.class, "typePropertyName");
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
        JsonParser jp = createTestParser();
        String json = "{\"propertyName\":\"value\"}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "propertyName", bean);

        handler = handler.start();

        handler.handlePropertyValue(jp, ctxt, "propertyName", bean);

        handler = handler.start();

        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'typePropertyName' for external type id 'null'", e.getMessage());
        }

        json = "{\"propertyName\":\"value\"}";
        handler._typeIds = new String[1];
        handler._tokens = new TokenBuffer[1];
        handler.handlePropertyValue(jp, ctxt, "propertyName", bean);
        handler._typeIds[0] = null;
        handler = handler.start();
        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'typePropertyName' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testBuilder() throws IOException {
        ExtTypedProperty[] properties = new ExternalTypeHandler.Builder().addExternal(new StdSerializer<>(String.class), String.class, "typePropertyName").build()._properties.toArray(new ExtTypedProperty[0]);
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, new HashMap<>(), null, null);
        JsonParser jp = createTestParser();
        String json = "{\"typePropertyName\":\"StringType\"}";
        DeserializationContext ctxt = new TestDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);
        assertEquals("StringType", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler = handler.start();

        handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", bean);

        handler = handler.start();

        handler.complete(jp, ctxt, bean);
        assertEquals("StringType", bean.getClass().getDeclaredField("type").get(bean));
    }

    private JsonParser createTestParser() throws IOException {
        StringReader reader = new StringReader("{}");
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createParser(reader);
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object instantiateValue(Class<?> valueType) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
            return valueType.newInstance();
        }
    }
}