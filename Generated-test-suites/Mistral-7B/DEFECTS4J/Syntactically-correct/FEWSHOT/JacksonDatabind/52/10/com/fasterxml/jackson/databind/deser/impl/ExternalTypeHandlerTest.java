package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();
        String propName = "testProperty";
        String typeId = "testType";
        handler.handleTypePropertyValue(p, new TestContext(), propName, bean);
        assertEquals(typeId, handler._typeIds[0]);
        assertEquals(bean, handler._properties[0].getProperty().get(handler._tokens[0].asParser(p).nextToken(), handler));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();
        String propName = "testProperty";
        String typeId = "testType";
        handler.handlePropertyValue(p, new TestContext(), propName, bean);
        assertEquals(typeId, handler._typeIds[0]);
        assertEquals(bean, handler._properties[0].getProperty().get(handler._tokens[0].asParser(p).nextToken(), handler));
    }

    @Test
    public void testComplete() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();

        handler.handleTypePropertyValue(p, new TestContext(), "testProperty1", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty2", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty3", bean);
        handler.complete(p, new TestContext(), bean);

        for (int i = 0; i < handler._properties.length; i++) {
            assertEquals(bean, handler._properties[i].getProperty().get(handler._tokens[i].asParser(p).nextToken(), handler));
        }
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        Object bean = new Object();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                Object result = buffer.getParameter(new SettableBeanProperty("creatorProperty1"));
                Object typeId = buffer.getParameter(new SettableBeanProperty("creatorProperty2"));
                return new Object() {
                    String property1 = (String) result;
                    String typeId1 = (String) typeId;
                };
            }
        };
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();

        handler.handleTypePropertyValue(p, new TestContext(), "testProperty1", bean);
        handler.handlePropertyValue(p, new TestContext(), "creatorProperty1", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty2", bean);
        handler.handlePropertyValue(p, new TestContext(), "creatorProperty2", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty3", bean);
        handler.complete(p, new TestContext(), bean, creator);

        assertEquals("testPropertyValue1", handler._properties[0].getProperty().get(handler._tokens[0].asParser(p).nextToken(), handler));
        assertEquals("testPropertyValue2", handler._properties[1].getProperty().get(handler._tokens[1].asParser(p).nextToken(), handler));
        assertEquals("testPropertyValue3", handler._properties[2].getProperty().get(handler._tokens[2].asParser(p).nextToken(), handler));
        assertEquals("creatorPropertyValue1", handler._properties[3].getProperty().get(handler._tokens[3].asParser(p).nextToken(), handler));
        assertEquals("testType1", handler._properties[3].getTypeProperty().get(handler._tokens[3].asParser(p).nextToken(), handler));

        Object result = handler._properties[4].getProperty().get(handler._tokens[4].asParser(p).nextToken(), handler);
        assertEquals("testPropertyValue1", result.getClass().getField("property1").get(result));
        assertEquals("testType1", result.getClass().getField("typeId1").get(result));
    }

    @Test
    public void testCompleteWithNullValues() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();

        handler.handlePropertyValue(p, new TestContext(), "testProperty1", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty2", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty3", bean);
        handler.complete(p, new TestContext(), bean);

        for (int i = 0; i < handler._properties.length; i++) {
            if (i > 0) {
                assertNull(handler._properties[i].getProperty().get(handler._tokens[i].asParser(p).nextToken(), handler));
            }
        }
    }

    @Test
    public void testCompleteMissingProperties() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();

        handler.handleTypePropertyValue(p, new TestContext(), "testProperty1", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty2", bean);
        handler.complete(p, new TestContext(), bean);

        for (int i = 2; i < handler._properties.length; i++) {
            SettableBeanProperty prop = handler._properties[i].getProperty();
            String message = String.format("Missing property '%s' for external type id '%s'", prop.getName(), handler._properties[i].getTypePropertyName());
            try {
                handler._deserializeAndSet(p, new TestContext(), bean, i, handler._properties[i].getTypePropertyName());
                fail();
            } catch (IOException e) {
                assertEquals(message, e.getMessage());
            }
        }
    }

    @Test
    public void testCompleteMissingTypeIds() throws IOException {
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        JsonParser p = createTestParser();

        handler.handlePropertyValue(p, new TestContext(), "testProperty1", bean);
        handler.handlePropertyValue(p, new TestContext(), "testProperty2", bean);
        handler.complete(p, new TestContext(), bean);

        for (int i = 0; i < handler._properties.length; i++) {
            if (handler._typeIds[i] == null) {
                SettableBeanProperty prop = handler._properties[i].getProperty();
                String message = String.format("Missing external type id property '%s'", prop.getName());
                try {
                    handler._deserializeAndSet(p, new TestContext(), bean, i, null);
                    fail();
                } catch (IOException e) {
                    assertEquals(message, e.getMessage());
                }
            }
        }
    }

    private JsonParser createTestParser() throws IOException {
        String json = "{\"testProperty1\":\"testValue1\",\"testProperty2\":\"testValue2\",\"testProperty3\":null}";
        JsonFactory factory = new JsonFactory();
        return factory.createParser(new ByteArrayInputStream(json.getBytes()));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public void reportMappingException(String msg) throws IOException {
            fail(msg);
        }

        @Override
        public void reportMappingException(String msgTemplate, Object... args) throws IOException {
            fail(String.format(msgTemplate, args));
        }
    }
}