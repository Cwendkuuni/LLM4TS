package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Unit tests for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ExternalTypeHandler handler;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        node.put("typeId", "com.example.PersonType");

        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(JsonTestHelper.createTestProperty("name"), null));
        properties.add(new ExtTypedProperty(JsonTestHelper.createTestProperty("age"), new IntDeserializer()));
        properties.add(new ExtTypedProperty(JsonTestHelper.createTestProperty("typeId"), new SimpleTypeIdResolver()));

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);
        handler.start();
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":\"Jane\",\"typeId\":\"com.example.OtherType\"}"));
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
        assertTrue(handler.isPropertyValueHandled());
        assertEquals("com.example.OtherType", handler.getTypeId());

        handler.reset();

        parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":\"John\",\"typeId\":null}"));
        handler.handleTypePropertyValue(parser, ctxt, "typeId", null);
        assertFalse(handler.isPropertyValueHandled());
        assertNull(handler.getTypeId());
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":\"John\",\"age\":35}"));
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        handler.handlePropertyValue(parser, ctxt, "name", null);
        assertTrue(handler.isPropertyValueHandled());
        assertEquals("John Doe", handler.getStoredPropertyValue("name"));

        handler.handlePropertyValue(parser, ctxt, "age", null);
        assertTrue(handler.isPropertyValueHandled());
        assertEquals(35, handler.getStoredPropertyValue("age"));

        handler.reset();

        parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":null,\"age\":null}"));
        handler.handlePropertyValue(parser, ctxt, "name", null);
        assertFalse(handler.isPropertyValueHandled());
        assertNull(handler.getStoredPropertyValue("name"));

        handler.handlePropertyValue(parser, ctxt, "age", null);
        assertFalse(handler.isPropertyValueHandled());
        assertNull(handler.getStoredPropertyValue("age"));
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser parser = mapper.getFactory().createParser(JsonTestHelper.createTestString(node.toString()));
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object result = handler.complete(parser, ctxt, null);
        JsonTestHelper.assertObjectEquals(mapper.readValue(JsonTestHelper.createTestString(node.toString()), Map.class), result);

        handler.reset();

        Object result2 = handler.complete(parser, ctxt, null, null);
        JsonTestHelper.assertObjectEquals(null, result2);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws Exception {
        JsonParser parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":null,\"age\":null}"));
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);

        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        Object result = handler.complete(parser, ctxt, buffer, creator);
        JsonTestHelper.assertObjectEquals(null, result);

        Mockito.verifyNoMoreInteractions(buffer);

        Mockito.reset(buffer);

        parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":\"John\",\"age\":35}"));

        Mockito.when(buffer.getBufferArray()).thenReturn(new Object[handler._properties.size()]);

        handler.handlePropertyValue(parser, ctxt, "name", null);
        handler.handlePropertyValue(parser, ctxt, "age", null);

        handler.complete(parser, ctxt, buffer, creator);

        Object[] values = (Object[]) Mockito.verify(buffer).getBufferArray();
        assertEquals("John Doe", values[0]);
        assertEquals(35, values[1]);

        Mockito.verify(creator).build(ctxt, buffer);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingPropertyForExternalTypeId() throws Exception {
        JsonParser parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":\"John\",\"typeId\":null}"));
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        handler.complete(parser, ctxt, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingExternalTypeIdProperty() throws Exception {
        JsonParser parser = mapper.getFactory().createParser(JsonTestHelper.createTestString("{\"name\":null,\"typeId\":\"com.example.OtherType\"}"));
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        handler.complete(parser, ctxt, null);
    }

    private String getTestString(Object node) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createTestString(String str) {
        return "{\"root\":{" + str + "}}";
    }

    private ObjectNode createTestNode(ObjectNode root, String property, Object value) {
        ObjectNode node = mapper.createObjectNode();
        node.put(property, value);
        root.set(property, node);
        return node;
    }

    private ObjectNode createTestNode(ObjectNode root, String property1, Object value1, String property2, Object value2) {
        ObjectNode node1 = mapper.createObjectNode();
        node1.put(property1, value1);
        ObjectNode node2 = mapper.createObjectNode();
        node2.put(property2, value2);
        root.set(property1, node1);
        root.set(property2, node2);
        return node1;
    }

    private ObjectNode createTestNode(ObjectNode root, String property, String value) {
        ObjectNode node = mapper.createObjectNode();
        node.put(property, value);
        root.set(property, node);
        return node;
    }

    private ObjectNode createTestNode(ObjectNode root, String property, String propertyName, Object value) {
        ObjectNode node = mapper.createObjectNode();
        node.put(propertyName, value);
        root.set(property, node);
        return node;
    }

    private ObjectNode createTestNode(ObjectNode root, String property, Object value, String typeId) {
        ObjectNode node = mapper.createObjectNode();
        node.put("property", value);
        node.put("typeId", typeId);
        root.set(property, node);
        return node;
    }

    private Object createTestProperty(String name) {
        return JsonTestHelper.createTestProperty(name);
    }

    private static class JsonTestHelper {
        public static ObjectNode createTestNode(ObjectMapper mapper, String name, Object value) {
            ObjectNode node = mapper.createObjectNode();
            node.put(name, value);
            return node;
        }

        public static ObjectNode createTestNode(ObjectMapper mapper, String name1, Object value1, String name2, Object value2) {
            ObjectNode node1 = mapper.createObjectNode();
            node1.put(name1, value1);
            ObjectNode node2 = mapper.createObjectNode();
            node2.put(name2, value2);
            node1.set("node2", node2);
            return node1;
        }

        public static ObjectNode createTestNode(ObjectMapper mapper, String name, String value) {
            ObjectNode node = mapper.createObjectNode();
            node.put(name, value);
            return node;
        }

        public static ObjectNode createTestNode(ObjectMapper mapper, String name, String propertyName, Object value) {
            ObjectNode node = mapper.createObjectNode();
            node.put(propertyName, value);
            node.set(name, node);
            return node;
        }

        public static ObjectNode createTestNode(ObjectMapper mapper, String name, String propertyName, Object value, String typeId) {
            ObjectNode node = mapper.createObjectNode();
            node.put(propertyName, value);
            node.put("typeId", typeId);
            node.set(name, node);
            return node;
        }

        public static ObjectNode createTestNode(ObjectMapper mapper, String name, Object value, String typeId) {
            ObjectNode node = mapper.createObjectNode();
            node.put(name, value);
            node.put("typeId", typeId);
            return node;
        }

        public static Object createTestProperty(String name) {
            return mapper.findProperty(name);
        }

        public static ObjectMapper mapper;

        @BeforeClass
        public static void setupClass() throws Exception {
            mapper = new ObjectMapper();
        }

        public static Object readValue(String json, Class<?> clazz) throws IOException {
            return mapper.readValue(JsonFactory.instance.createParser(json), clazz);
        }

        public static String writeValueAsString(Object value) throws IOException {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        }

        public static String writeValueAsString(Object value, Class<?> valueType) throws IOException {
            return mapper.writerForTypeValue(valueType).writeValueAsString(value);
        }

        public static TokenBuffer asParser(JsonParser parser, TokenBuffer buffer) {
            buffer.reset();
            buffer.copyCurrentContext(parser.getCurrentContextValue());
            buffer.copyCurrentStructure(parser);
            return buffer;
        }

        public static boolean isObject(JsonToken token) {
            return token.isContainerClass();
        }

        public static boolean isScalarValue(JsonToken token) {
            return token.isScalarValue();
        }

        public static boolean isValueNull(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = (token == null || token.isNullValue());
            parser.nextToken(); // consume token
            return result;
        }

        public static boolean isValueBoolean(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = (token == JsonToken.VALUE_TRUE || token == JsonToken.VALUE_FALSE);
            parser.nextToken(); // consume token
            return result;
        }

        public static boolean isValueNumber(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = (token.isNumberType() || token.isIntegralNumber() || token.isShortText());
            parser.nextToken(); // consume token
            return result;
        }

        public static boolean isValueString(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = token.isTextual();
            parser.nextToken(); // consume token
            return result;
        }

        public static boolean isValueNullOrEmptyString(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = (token == null || token.isNullValue() || token.isTextEmpty());
            parser.nextToken(); // consume token
            return result;
        }

        public static boolean isValueArray(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = token.isStartArray();
            parser.nextToken(); // consume '['
            return result;
        }

        public static boolean isValueObject(JsonParser parser) throws IOException {
            JsonToken token = parser.nextToken();
            boolean result = token.isStartObject();
            parser.nextToken(); // consume '{'
            return result;
        }

        public static Object deserialize(JsonParser parser, DeserializationContext ctxt, Class<?> clazz) throws IOException {
            return parser.readValueAs(clazz);
        }

        public static Object deserializeAndSet(JsonParser parser, DeserializationContext ctxt, Object bean, String propertyName, Object value) throws IOException {
            Object property = ctxt.findProperty(propertyName);
            if (property == null) {
                throw new IllegalStateException("Property not found: " + propertyName);
            }
            property.set(bean, value);
            return value;
        }

        public static void assertObjectEquals(Object expected, Object actual) {
            assertEquals(expected.getClass(), actual.getClass());
            assertEquals(expected, actual);
        }
    }
}