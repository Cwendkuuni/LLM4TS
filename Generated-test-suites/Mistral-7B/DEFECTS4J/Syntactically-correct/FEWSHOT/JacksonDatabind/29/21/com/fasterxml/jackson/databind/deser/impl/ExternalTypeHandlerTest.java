package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestObject(Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new FasterXmlString("{\"name\": \"test\", \"data\": null}").getData(), clazz);
    }

    @SuppressWarnings("resource")
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = mapper.getTypeFactory().constructClass("$TestTypeHandler$HandleTypePropertyValue");

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        Object testObj = createTestObject(clazz);
        JsonParser jp = mapper.getFactory().createParser("{\"name\": \"test\", \"type\": \"TestType1\"}");

        handler.handleTypePropertyValue(jp, null, "type", testObj);
        assertEquals("TestType1", testObj.getClass().getSimpleName());

        jp = mapper.getFactory().createParser("{\"name\": \"test\", \"type\": \"TestType2\"}");
        handler.handleTypePropertyValue(jp, null, "type", testObj);
        assertEquals("TestType2", testObj.getClass().getSimpleName());
    }

    @SuppressWarnings("resource")
    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = mapper.getTypeFactory().constructClass("$TestTypeHandler$HandlePropertyValue");

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("data", String.class), new BasicTypeDeserializer())
                .build());

        Object testObj = createTestObject(clazz);
        JsonParser jp = mapper.getFactory().createParser("{\"name\": \"test\", \"data\": \"test\"}");
        handler.handlePropertyValue(jp, null, "data", testObj);
        assertEquals("test", testObj.getClass().getMethods()[0].invoke(testObj));

        jp = mapper.getFactory().createParser("{\"name\": \"test\", \"type\": \"TestType1\", \"data\": \"test\"}");
        handler.handlePropertyValue(jp, null, "data", testObj);
        assertEquals("test", testObj.getClass().getMethods()[0].invoke(testObj));
    }

    @SuppressWarnings("resource")
    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = mapper.getTypeFactory().constructClass("$TestTypeHandler$Complete");

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("type", String.class), new BasicTypeDeserializer())
                .addExternal(new SettableBeanProperty("data", String.class), new BasicTypeDeserializer())
                .build());

        Object testObj = createTestObject(clazz);
        JsonParser jp = mapper.getFactory().createParser("{\"name\": \"test\", \"type\": \"TestType1\", \"data\": \"test\"}");
        handler.handlePropertyValue(jp, null, "type", testObj);
        handler.handlePropertyValue(jp, null, "data", testObj);
        jp.nextToken(); // consume end of object
        handler.complete(jp, null, testObj);
        assertEquals("TestType1", testObj.getClass().getMethods()[0].invoke(testObj));
        assertEquals("test", testObj.getClass().getMethods()[1].invoke(testObj));
    }

    @SuppressWarnings("resource")
    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = mapper.getTypeFactory().constructClass("$TestTypeHandler$CompleteWithPropertyBasedCreator");

        class TestCreator extends BaseJsonDeserializer<Object> {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                Object result = new Object();
                String name = p.nextText();
                p.nextToken(); // consume ':'
                Object value = p.readValueAs(Object.class);
                p.nextToken(); // consume ','
                SettableBeanProperty prop = findCreatorProperty("data", ctxt.findContextualType(Object.class));
                if (prop != null) {
                    prop.set(result, value);
                }
                return result;
            }
        }

        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("type", String.class), new BasicTypeDeserializer())
                .addExternal(new SettableBeanProperty("data", String.class), new BasicTypeDeserializer())
                .build());

        Object testObj = handler.start().handlePropertyValue(mapper.getFactory().createParser("{\"type\": \"TestType1\"}"), null, "type", null);
        TestCreator creator = new TestCreator();
        Object createdObj = handler.complete(mapper.getFactory().createParser(""), creator, null, null);
        handler.handlePropertyValue(mapper.getFactory().createParser("{\"data\": \"test\"}"), null, "data", createdObj);
        handler.complete(mapper.getFactory().createParser("{\"type\": \"TestType2\", \"data\": \"test\"}"), null, createdObj);

        assertEquals("TestType2", createdObj.getClass().getMethods()[0].invoke(createdObj));
        assertEquals("test", createdObj.getClass().getMethods()[1].invoke(createdObj));
    }

    private static class TestTypeHandler {
        String type;
    }

    private static class TestTypeHandler$HandleTypePropertyValue extends TestTypeHandler {
    }

    private static class TestTypeHandler$HandlePropertyValue extends TestTypeHandler {
    }

    private static class TestTypeHandler$Complete extends TestTypeHandler {
    }

    private static class TestTypeHandler$CompleteWithPropertyBasedCreator extends TestTypeHandler {
    }
}

class TestType1 {}
class TestType2 {}