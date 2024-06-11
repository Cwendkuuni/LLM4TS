package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

/**
 * Test class for {@link ExternalTypeHandler}.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Given
        String json = "{\"typeId\": \"StringType\", \"name\": \"John\"}";
        JsonParser p = new ObjectMapper().readTree(json);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        Object bean = new Object();

        // When: handleTypePropertyValue is called
        boolean result = handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);

        // Then: the result should be true and the type id should be set
        assertTrue(result);
        assertEquals("StringType", handler.getTypeIds()[0]);

        // When: handleTypePropertyValue is called again
        result = handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);

        // Then: the result should be false as the type id has already been set
        assertFalse(result);

        // When: handleTypePropertyValue is called with a null bean
        result = handler.handleTypePropertyValue(p, new TestContext(), "typeId", null);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Given
        String json = "{\"name\": \"John\"}";
        JsonParser p = new ObjectMapper().readTree(json);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        Object bean = new Object();

        // When: handlePropertyValue is called
        boolean result = handler.handlePropertyValue(p, new TestContext(), "name", bean);

        // Then: the result should be true and the value should be set
        assertTrue(result);
        assertNotNull(handler.getTokens()[0]);

        // When: handlePropertyValue is called again
        result = handler.handlePropertyValue(p, new TestContext(), "name", bean);

        // Then: the result should be false as the value has already been set
        assertFalse(result);

        // When: handlePropertyValue is called with a null bean
        result = handler.handlePropertyValue(p, new TestContext(), "name", null);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        // Given
        String json = "{\"typeId\": \"StringType\", \"name\": \"John\"}";
        JsonParser p = new ObjectMapper().readTree(json);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        Object bean = new Object();

        // When: handleTypePropertyValue and handlePropertyValue are called
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", bean);
        handler.handlePropertyValue(p, new TestContext(), "name", bean);

        // Then: complete should return the bean with the type id and value set
        Object result = handler.complete(p, new TestContext(), bean);
        assertSame(bean, result);
        assertEquals("John", handler.getProperties()[0].get(bean));
        assertEquals("StringType", handler.getTypeIds()[0]);

        // Given: handlePropertyValue is called without a typeId being set
        String json2 = "{\"name\": \"Jane\"}";
        p = new ObjectMapper().readTree(json2);
        handler.handlePropertyValue(p, new TestContext(), "name", bean);

        // When: complete is called
        result = handler.complete(p, new TestContext(), bean);
        assertSame(bean, result);
        assertNull(handler.getTypeIds()[0]);

        // Given: handleTypePropertyValue is called with a null bean
        String json3 = "{\"typeId\": \"StringType\", \"name\": \"Jim\"}";
        p = new ObjectMapper().readTree(json3);
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", null);

        // When: complete is called
        result = handler.complete(p, new TestContext(), null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        // Given
        String json = "{\"typeId\": \"NumberType\", \"name\": 42}";
        JsonParser p = new ObjectMapper().readTree(json);
        ObjectCreator creator = new ObjectMapper().getTypeFactory().constructValueType(Number.class);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());

        PropertyValueBuffer buffer = new PropertyValueBuffer();

        // When: handleTypePropertyValue and handlePropertyValue are called
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", handler);
        handler.handlePropertyValue(p, new TestContext(), "name", handler);

        // Then: completeWithCreator should create a new instance with the type id and value set
        Object result = handler.complete(p, new TestContext(), buffer, creator);
        assertInstanceOf(Number.class, result);
        assertEquals(42, result.hashCode());

        // Given: handlePropertyValue is called without a typeId being set
        String json2 = "{\"name\": 43}";
        p = new ObjectMapper().readTree(json2);
        handler.handlePropertyValue(p, new TestContext(), "name", handler);

        // When: completeWithCreator is called
        result = handler.complete(p, new TestContext(), buffer, creator);
        assertNull(result);

        // Given: handleTypePropertyValue is called with a null handler
        String json3 = "{\"typeId\": \"NumberType\", \"name\": 44}";
        p = new ObjectMapper().readTree(json3);
        handler = null;
        handler.handleTypePropertyValue(p, new TestContext(), "typeId", handler);

        // When: completeWithCreator is called
        result = handler.complete(p, new TestContext(), buffer, creator);
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public BeanDescription getCurrentBeanDescription() {
            return null;
        }

        @Override
        public Object instantiateValue(Class<?> type, Object value)
                throws InstantiationException, IllegalAccessException {
            return value;
        }
    }
}