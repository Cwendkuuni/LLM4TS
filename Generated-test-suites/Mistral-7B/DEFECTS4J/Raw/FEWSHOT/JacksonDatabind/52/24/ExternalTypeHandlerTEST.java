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
        JsonParser p = createParser("{\"type\":\"Foo\", \"name\":\"John\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "type", bean));
        assertEquals("Foo", handler.getTypeIds()[0]);

        p.nextToken(); // move to next token

        assertFalse(handler.handleTypePropertyValue(p, ctxt, "type", bean));
    }

    @Test
    public void testHandleTypePropertyValue_nullBean() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        assertTrue(handler.handleTypePropertyValue(p, ctxt, "type", null));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        assertTrue(handler.handlePropertyValue(p, ctxt, "name", bean));
        assertNull(handler.getTypeIds());

        p.nextToken(); // move to next token

        assertTrue(handler.handlePropertyValue(p, ctxt, "type", bean));
        assertNull(handler.getTypeIds());

        handler.start();
        String typeId = handler.getTypeIds()[0];
        assertEquals("Foo", typeId);
    }

    @Test
    public void testHandlePropertyValue_nullBeanAndTokens() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        assertTrue(handler.handlePropertyValue(p, ctxt, "type", null));
        assertNull(handler.getTypeIds());

        p.nextToken(); // move to next token

        handler.start();
        String typeId = handler.getTypeIds()[0];
        assertEquals("Foo", typeId);
    }

    @Test
    public void testHandlePropertyValue_noTypeId() throws IOException {
        JsonParser p = createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.start();
        SettableBeanProperty prop = handler.getProperty(0);
        String propName = prop.getName();

        assertTrue(handler.handlePropertyValue(p, ctxt, propName, bean));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testHandlePropertyValue_typeIdAlreadySet() throws IOException {
        JsonParser p = createParser("{\"name\":\"John\", \"type\":\"Foo\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.start();
        String typeId = handler.getTypeIds()[0];
        assertEquals("Foo", typeId);

        assertTrue(handler.handlePropertyValue(p, ctxt, "name", bean));
        assertEquals("Foo", handler.getTypeIds()[0]);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = createParser("{\"type\":\"Foo\", \"name\":\"John\"}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.start();
        handler.complete(p, ctxt, bean);

        SettableBeanProperty prop = handler.getProperty(0);
        assertEquals("John", prop.get(bean));
        assertEquals("Foo", handler.getTypeIds()[0]);
    }

    @Test
    public void testComplete_propertyValueBufferAndPropertyBasedCreator() throws IOException {
        JsonParser p = createParser("{" +
                "  \"type\": { " +
                "    \"type\": \"Foo\", " +
                "    \"name\": \"John\"" +
                "  }, " +
                "  \"name\": \"Jane\"" +
                "}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                return new Object() {{
                    prop1 = buffer.get(0);
                    prop2 = buffer.get(1);
                }};
            };
        };

        handler.start();
        handler.complete(p, ctxt, buffer, creator);

        Object bean = handler.getProperty(0).get(handler.getProperty(1).get(buffer.getObject()));
        assertEquals("John", bean.prop1);
        assertEquals("Foo", handler.getTypeIds()[0]);
        assertEquals("Jane", handler.getProperty(1).get(bean).prop1);
    }

    @Test
    public void testComplete_noTypeId() throws IOException {
        JsonParser p = createParser("{" +
                "  \"type\": null, " +
                "  \"name\": \"John\"" +
                "}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.start();
        handler.complete(p, ctxt, bean);

        SettableBeanProperty prop = handler.getProperty(0);
        assertEquals("John", prop.get(bean));
        assertNull(handler.getTypeIds());
    }

    @Test
    public void testComplete_missingTypeIdProperty() throws IOException {
        JsonParser p = createParser("{" +
                "  \"name\": \"John\"" +
                "}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.start();
        handler.complete(p, ctxt, bean);

        SettableBeanProperty prop = handler.getProperty(0);
        assertEquals("John", prop.get(bean));
        assertNull(handler.getTypeIds());

        String exceptionMessage = "";
        try {
            handler.getTypeIds()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("missing external type id property 'type'", exceptionMessage);
    }

    @Test
    public void testComplete_missingProperty() throws IOException {
        JsonParser p = createParser("{" +
                "  \"type\": \"Foo\"" +
                "}");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler.start();
        handler.complete(p, ctxt, bean);

        String exceptionMessage = "";
        try {
            handler.getProperty(1).get(bean);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Missing property 'name' for external type id 'type'", exceptionMessage);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = createParser("\"Foo\"");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        String result = handler._deserialize(p, ctxt, 0, "Foo");
        assertEquals("Foo", result);
    }

    @Test
    public void testDeserialize_nullValue() throws IOException {
        JsonParser p = createParser("null");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        Object result = handler._deserialize(p, ctxt, 0, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = createParser("\"Foo\"");
        DeserializationContext ctxt = new DeserializationContext(p, new StandardDeserializationContext());
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object bean = new Object();

        handler._deserializeAndSet(p, ctxt, bean, 0, "Foo");

        SettableBeanProperty prop = handler.getProperty(0);
        assertEquals("Foo", prop.get(bean));
    }

    private JsonParser createParser(String json) throws IOException {
        StringReader reader = new StringReader(json);
        JsonFactory f = new JsonFactory();
        return f.createParser(reader);
    }
}