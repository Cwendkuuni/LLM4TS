package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\",\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "type", bean));
        assertEquals("Foo", handler._typeIds[0]);

        p.nextToken(); // consume "prop"
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = createParser("{\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertTrue(handler.handlePropertyValue(p, ctxt, "prop", bean));

        // no typeId set yet
        assertNull(handler._typeIds[0]);

        p.nextToken(); // consume "value"
    }

    @Test
    public void testHandleTypePropertyValueWithNullBean() throws IOException {
        JsonParser p = createParser("{\"type\":null}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        assertFalse(handler.handleTypePropertyValue(p, ctxt, "type", null));
    }

    @Test
    public void testHandleTypePropertyValueWithEmptyArray() throws IOException {
        JsonParser p = createParser("{\"type\":[]}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertFalse(handler.handleTypePropertyValue(p, ctxt, "type", bean));
    }

    @Test
    public void testHandleTypePropertyValueWithMultipleProperties() throws IOException {
        JsonParser p = createParser("{\"type1\":\"Foo\",\"type2\":\"Bar\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type1", bean);
        assertEquals("Foo", handler._typeIds[0]);

        handler.handleTypePropertyValue(p, ctxt, "type2", bean);
        assertEquals("Bar", handler._typeIds[1]);
    }

    @Test
    public void testHandleTypePropertyValueWithMissingProperty() throws IOException {
        JsonParser p = createParser("{\"missing\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertFalse(handler.handleTypePropertyValue(p, ctxt, "missing", bean));
    }

    @Test
    public void testHandlePropertyValueWithMissingTypeProperty() throws IOException {
        JsonParser p = createParser("{\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertTrue(handler.handlePropertyValue(p, ctxt, "prop", bean));

        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\",\"prop1\":\"value1\",\"prop2\":\"value2\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type", bean);
        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        handler.handlePropertyValue(p, ctxt, "prop2", bean);

        p.nextToken(); // consume "}"

        handler.complete(p, ctxt, bean);

        assertEquals("Foo", handler._typeIds[0]);
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        JsonParser p = createParser("{\"type\":null,\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type", bean);

        handler.complete(p, ctxt, bean);

        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        JsonParser p = createParser("{\"prop1\":\"value1\",\"prop2\":\"value2\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        handler.handlePropertyValue(p, ctxt, "prop2", bean);

        p.nextToken(); // consume "}"

        handler.complete(p, ctxt, bean);

        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        JsonParser p = createParser("{\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "prop", bean);

        p.nextToken(); // consume "}"

        try {
            handler.complete(p, ctxt, bean);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCompleteWithDeserializedValues() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\",\"prop\":[1,2,3]}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type", bean);
        handler.handlePropertyValue(p, ctxt, "prop", bean);

        p.nextToken(); // consume "}"

        handler.complete(p, ctxt, bean);

        assertEquals(3, handler._tokens[0].size());
    }

    @Test
    public void testCompleteWithCreatorProperties() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\",\"creator1\":\"value1\",\"creator2\":\"value2\"}");
        DeserializationContext ctxt = createContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        handler.handleTypePropertyValue(p, ctxt, "type", null);
        handler.handlePropertyValue(p, ctxt, "creator1", null);
        handler.handlePropertyValue(p, ctxt, "creator2", null);

        p.nextToken(); // consume "}"

        handler.complete(p, ctxt, buffer, new PropertyBasedCreator());

        assertEquals("value1", buffer.getParameterValue(0));
        assertEquals("value2", buffer.getParameterValue(1));
    }

    @Test
    public void testCompleteWithValueDeserialization() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\",\"prop\":null}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type", bean);
        handler.handlePropertyValue(p, ctxt, "prop", bean);

        p.nextToken(); // consume "}"

        handler.complete(p, ctxt, bean);

        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testCompleteWithTypeIdDeserialization() throws IOException {
        JsonParser p = createParser("{\"type\":null,\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type", bean);

        p.nextToken(); // consume "}"

        try {
            handler.complete(p, ctxt, bean);
            fail();
        } catch (IOException e) {
            // expected
        }

        assertEquals("value", handler._typeIds[0]);
    }

    @Test
    public void testCompleteWithMissingTypeIdProperty() throws IOException {
        JsonParser p = createParser("{\"prop\":\"value\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "prop", bean);

        p.nextToken(); // consume "}"

        try {
            handler.complete(p, ctxt, bean);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\"}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "type", bean);

        try {
            handler.complete(p, ctxt, bean);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCompleteWithMissingTypeIdAndProperty() throws IOException {
        JsonParser p = createParser("{}");
        DeserializationContext ctxt = createContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        try {
            handler.complete(p, ctxt, bean);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    private JsonParser createParser(String json) throws IOException {
        return new ObjectMapper().readTree(new ByteArrayInputStream(json.getBytes()));
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }
}

class TestContext extends DeserializationContext {
    @Override
    public JsonFactory _rootValueFactory() {
        return new SimpleModule().buildParser();
    }

    @Override
    public JsonFactory _valueFactory() {
        return new SimpleModule().buildParser();
    }
}