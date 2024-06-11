package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"typeProperty\":\"String\",\"value\":\"foo\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SimpleBean().getClass().getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(), null, null);

        handler.start();

        assertTrue(handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty", new SimpleBean()));
        assertEquals("String", handler._typeIds[0]);

        jp.nextToken(); // consume the next token
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandleTypePropertyValue_withNullBean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"typeProperty\":\"String\",\"value\":\"foo\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SimpleBean().getClass().getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(), null, null);

        handler.start();

        assertTrue(handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty", null));
        assertNull(handler._typeIds[0]);

        jp.nextToken(); // consume the next token
    }

    @Test
    public void testHandleTypePropertyValue_withoutMatchingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"notMatchingProperty\":\"String\",\"value\":\"foo\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SimpleBean().getClass().getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(), null, null);

        handler.start();

        assertFalse(handler.handleTypePropertyValue(jp, new TestContext(), "notMatchingProperty", new SimpleBean()));
        assertNull(handler._typeIds[0]);

        jp.nextToken(); // consume the next token
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property\":\"value\",\"typeProperty\":\"String\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SimpleBean().getClass().getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(), null, null);

        handler.start();

        assertTrue(handler.handlePropertyValue(jp, new TestContext(), "property", new SimpleBean()));
        assertEquals("String", handler._typeIds[0]);

        jp.nextToken(); // consume the next token
        assertNull(handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_withNullBean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property\":\"value\",\"typeProperty\":\"String\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SimpleBean().getClass().getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(), null, null);

        handler.start();

        assertTrue(handler.handlePropertyValue(jp, new TestContext(), "property", null));
        assertNull(handler._typeIds[0]);

        jp.nextToken(); // consume the next token
    }

    @Test
    public void testHandlePropertyValue_withoutMatchingTypeProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property\":\"value\",\"notMatchingTypeProperty\":\"String\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new SimpleBean().getClass().getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(), null, null);

        handler.start();

        assertFalse(handler.handlePropertyValue(jp, new TestContext(), "property", new SimpleBean()));
        assertNull(handler._typeIds[0]);

        jp.nextToken(); // consume the next token
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property1\":\"value1\",\"property2\":\"value2\",\"typeProperty\":\"String\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property2"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        handler.handlePropertyValue(jp, new TestContext(), "property1", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "typeProperty", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "property2", new SimpleBean());

        Object bean = handler.complete(jp, new TestContext(), null);
        SimpleBean simpleBean = mapper.readValue(new ByteArrayInputStream(mapper.writerForBuffer().writeValueAsBytes(bean)), SimpleBean.class);

        assertEquals("value1", simpleBean.getProperty1());
        assertEquals("value2", simpleBean.getProperty2());
    }

    @Test
    public void testComplete_withNullBean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property1\":\"value1\",\"property2\":\"value2\",\"typeProperty\":\"String\"}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property2"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        handler.handlePropertyValue(jp, new TestContext(), "property1", null);
        handler.handlePropertyValue(jp, new TestContext(), "typeProperty", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "property2", new SimpleBean());

        Object bean = handler.complete(jp, new TestContext(), null);
        // Since we pass null as the bean, it should not be set
        assertNull(bean);
    }

    @Test
    public void testComplete_withMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property1\":\"value1\",\"typeProperty\":\"String\"}"; // missing property2
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property2"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        handler.handlePropertyValue(jp, new TestContext(), "property1", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "typeProperty", new SimpleBean());

        try {
            handler.complete(jp, new TestContext(), null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'property2' for external type id 'String'", e.getMessage());
        }
    }

    @Test
    public void testComplete_withMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"typeProperty\":\"String\"}"; // missing property1 and property2
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property2"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        try {
            handler.complete(jp, new TestContext(), null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'property1' for external type id 'String'", e.getMessage());
        }
    }

    @Test
    public void testComplete_withNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property1\":null,\"typeProperty\":null}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        handler.handlePropertyValue(jp, new TestContext(), "property1", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "typeProperty", new SimpleBean());

        try {
            handler.complete(jp, new TestContext(), null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing external type id property 'typeProperty'", e.getMessage());
        }
    }

    @Test
    public void testComplete_withMissingTypeIdAndProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{}"; // missing property1 and typeProperty
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        try {
            handler.complete(jp, new TestContext(), null);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'property1' for external type id 'String'", e.getMessage());
        }
    }

    @Test
    public void testComplete_withDefaultType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property1\":null,\"typeProperty\":null}";
        JsonParser jp = factory.createParser(json);

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer()),
                new ExtTypedProperty(SimpleBean.class.getDeclaredField("typeProperty"), new SimpleStringDeserializer())},
                new HashMap<>(),
                new String[]{"String", "String"},
                new TokenBuffer[2]);

        handler.start();

        handler.handlePropertyValue(jp, new TestContext(), "property1", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "typeProperty", new SimpleBean());

        Object bean = handler.complete(jp, new TestContext(), null);
        SimpleBean simpleBean = mapper.readValue(new ByteArrayInputStream(mapper.writerForBuffer().writeValueAsBytes(bean)), SimpleBean.class);

        assertEquals(null, simpleBean.getProperty1());
        assertEquals(SimpleBean.class.getSimpleName(), handler._properties[0].getDefaultTypeId());
    }

    @Test
    public void testBuilder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String json = "{\"property1\":\"value1\",\"property2\":\"value2\"}";

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(SimpleBean.class.getDeclaredField("property1"), new SimpleStringDeserializer());
        builder.addExternal(SimpleBean.class.getDeclaredField("property2"), new SimpleStringDeserializer());

        ExternalTypeHandler handler = builder.build();

        JsonParser jp = factory.createParser(json);

        handler.start();

        handler.handlePropertyValue(jp, new TestContext(), "property1", new SimpleBean());
        handler.handlePropertyValue(jp, new TestContext(), "property2", new SimpleBean());

        Object bean = handler.complete(jp, new TestContext(), null);
        SimpleBean simpleBean = mapper.readValue(new ByteArrayInputStream(mapper.writerForBuffer().writeValueAsBytes(bean)), SimpleBean.class);

        assertEquals("value1", simpleBean.getProperty1());
        assertEquals("value2", simpleBean.getProperty2());
    }

    private static class SimpleBean {
        private String property1;
        private String property2;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty2() {
            return property2;
        }

        public void setProperty2(String property2) {
            this.property2 = property2;
        }
    }

    private static class SimpleStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class TestContext extends DeserializationContext {
    }
}