package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.*;
import java.util.*;

/**
 * Test cases for {code ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String jsonString = "{\"typeProperty\":\"MyType\",\"dataProperty\":\"dataValue\"}";
        JsonParser jp = createParser(jsonString);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty"), new SimpleTypeDeserializer())
            .build())
            .start();

        // Test with valid type property value
        handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty", bean);
        assertEquals("MyType", handler._typeIds[0]);

        // Test with invalid type property name
        jp.nextToken(); // move to "dataProperty"
        assertFalse(handler.handleTypePropertyValue(jp, new TestContext(), "invalidProperty", bean));

        // Test with null bean
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty"), new SimpleTypeDeserializer())
            .build())
            .start();
        assertNull(handler._tokens[0]);
        handler.handleTypePropertyValue(jp, new TestContext(), "typeProperty", null);
        assertNull(handler._typeIds[0]);

        // Test with null JsonParser
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty"), new SimpleTypeDeserializer())
            .build())
            .start();
        assertNull(handler._tokens[0]);
        assertFalse(handler.handleTypePropertyValue(null, new TestContext(), "typeProperty", bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String jsonString = "{\"typeProperty\":\"MyType\",\"dataProperty\":\"dataValue\"}";
        JsonParser jp = createParser(jsonString);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty"), new SimpleTypeDeserializer())
            .addExternal(new SimplePojoProperty("dataProperty"), new SimpleTypeDeserializer())
            .build())
            .start();

        // Test with valid property value
        handler.handlePropertyValue(jp, new TestContext(), "dataProperty", bean);
        assertNull(handler._typeIds[1]);
        assertEquals("dataValue", handler._tokens[1].asStringValue());

        // Test with null bean
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty"), new SimpleTypeDeserializer())
            .addExternal(new SimplePojoProperty("dataProperty"), new SimpleTypeDeserializer())
            .build())
            .start();
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);
        handler.handlePropertyValue(jp, new TestContext(), "dataProperty", null);
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);

        // Test with null JsonParser
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty"), new SimpleTypeDeserializer())
            .addExternal(new SimplePojoProperty("dataProperty"), new SimpleTypeDeserializer())
            .build())
            .start();
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);
        assertFalse(handler.handlePropertyValue(null, new TestContext(), "dataProperty", bean));
    }

    @Test
    public void testComplete() throws IOException {
        String jsonString = "{\"typeProperty1\":\"MyType1\",\"typeProperty2\":\"MyType2\"}";
        JsonParser jp = createParser(jsonString);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty1"), new SimpleTypeDeserializer1())
            .addExternal(new SimplePojoProperty("typeProperty2"), new SimpleTypeDeserializer2())
            .build())
            .start();

        handler.handlePropertyValue(jp, new TestContext(), "typeProperty1", bean);
        handler.handlePropertyValue(jp, new TestContext(), "typeProperty2", bean);
        handler.complete(jp, new TestContext(), bean);

        // Test with no missing external type id property
        assertEquals("MyType1", handler._properties[0].getProperty().getName());
        assertEquals("MyType2", handler._properties[1].getProperty().getName());

        // Test with missing external type id property
        String jsonStringMissing = "{\"typeProperty1\":\"MyType1\"}";
        JsonParser jpMissing = createParser(jsonStringMissing);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty1"), new SimpleTypeDeserializer1())
            .build())
            .start();
        handler.handlePropertyValue(jpMissing, new TestContext(), "typeProperty1", bean);
        handler.complete(jpMissing, new TestContext(), bean);
        assertTrue(handler._properties[0].hasDefaultType());
        assertEquals("default", handler._properties[0].getDefaultTypeId());

        // Test with missing property for external type id
        String jsonStringMissingProp = "{\"typeProperty1\":\"MyType1\",\"anotherProperty\":\"otherValue\"}";
        JsonParser jpMissingProp = createParser(jsonStringMissingProp);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("typeProperty1"), new SimpleTypeDeserializer1())
            .build())
            .start();
        handler.handlePropertyValue(jpMissingProp, new TestContext(), "typeProperty1", bean);
        handler.complete(jpMissingProp, new TestContext(), bean);
        assertNotNull(handler._properties[0].getProperty());
        assertFalse(handler._properties[0].getProperty().getName().equals("anotherProperty"));
    }

    @Test
    public void testCompleteWithBufferAndCreator() throws IOException {
        String jsonString = "{\"MyProperty1\":{ \"typeProperty1\":\"MyType1\",\"typeProperty2\":\"MyType2\"},\"MyProperty2\":\"dataValue\"}";
        Object bean = new ObjectWithCreator();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("MyProperty1.typeProperty1"), new SimpleTypeDeserializer1())
            .addExternal(new SimplePojoProperty("MyProperty1.typeProperty2"), new SimpleTypeDeserializer2())
            .build())
            .start();

        handler.handlePropertyValue(createParser(jsonString).asParser(handler._tokens[0]), new TestContext(), "MyProperty1.typeProperty1", bean);
        handler.handlePropertyValue(createParser(jsonString).asParser(handler._tokens[1]), new TestContext(), "MyProperty1.typeProperty2", bean);
        handler.handlePropertyValue(createParser(jsonString).asParser(handler._tokens[2]), new TestContext(), "MyProperty2", bean);
        handler.complete(createParser(jsonString).asParser(handler._tokens[3]), new TestContext(), buffer, new ObjectCreator());

        // Test with all external type ids handled
        Object result = handler.getResult();
        assertEquals("MyType1", result.getClass().getField("typeProperty1").get(result));
        assertEquals("MyType2", result.getClass().getField("typeProperty2").get(result));
        assertEquals("dataValue", result.getClass().getField("MyProperty2").get(result));

        // Test with missing external type id property
        String jsonStringMissing = "{\"MyProperty1\":{ \"typeProperty1\":\"MyType1\"},\"MyProperty2\":\"dataValue\"}";
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .addExternal(new SimplePojoProperty("MyProperty1.typeProperty1"), new SimpleTypeDeserializer1())
            .build())
            .start();
        handler.handlePropertyValue(createParser(jsonStringMissing).asParser(handler._tokens[0]), new TestContext(), "MyProperty1.typeProperty1", bean);
        handler.handlePropertyValue(createParser(jsonStringMissing).asParser(handler._tokens[1]), new TestContext(), "MyProperty2", bean);
        handler.complete(createParser(jsonStringMissing).asParser(handler._tokens[2]), new TestContext(), buffer, new ObjectCreator());
        assertEquals("default", handler.getClass().getField("defaultTypeId").get(handler));
    }

    @Test
    public void testDeserialize() throws IOException {
        String jsonString = "\"MyTypeId\"[1.2,3.4,\"MyStringValue\"]";
        JsonParser jp = createParser(jsonString);
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .build())
            .start();

        // Test with valid JSON string
        handler._deserializeAndSet(jp, new TestContext(), bean, 0, jsonString);
        assertArrayEquals(new Object[]{1.2, 3.4, "MyStringValue"}, handler._properties[0].getProperty().get(bean));

        // Test with empty JSON array
        String jsonStringEmpty = "\"MyTypeId\"[]";
        JsonParser jpEmpty = createParser(jsonStringEmpty);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .build())
            .start();
        handler._deserializeAndSet(jpEmpty, new TestContext(), bean, 0, jsonStringEmpty);
        assertArrayEquals(new Object[0], handler._properties[0].getProperty().get(bean));

        // Test with null JSON string
        String jsonStringNull = "\"MyTypeId\"";
        JsonParser jpNull = createParser(jsonStringNull);
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
            .build())
            .start();
        handler._deserializeAndSet(jpNull, new TestContext(), bean, 0, jsonStringNull);
        assertNull(handler._properties[0].getProperty().get(bean));
    }

    private JsonParser createParser(String jsonString) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(new ByteArrayInputStream(jsonString.getBytes()));
    }

    private static class SimplePojoProperty {
        private final String _name;

        public SimplePojoProperty(String name) {
            this._name = name;
        }

        public String getName() {
            return this._name;
        }

        public String getTypePropertyName() {
            return this._name;
        }

        public SettableBeanProperty getProperty() {
            return new SimpleSettableBeanProperty();
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        private final String _propertyName;

        public SimpleTypeDeserializer() {
            this("SimpleTypeDeserializer");
        }

        public SimpleTypeDeserializer(String propertyName) {
            super(null, null);
            this._propertyName = propertyName;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public String getPropertyName() {
            return this._propertyName;
        }

        @Override
        public String getTypeId() {
            return "SimpleType";
        }
    }

    private static class SimpleTypeDeserializer1 extends SimpleTypeDeserializer {
        @Override
        public String getTypeId() {
            return "MyType1";
        }
    }

    private static class SimpleTypeDeserializer2 extends SimpleTypeDeserializer {
        @Override
        public String getTypeId() {
            return "MyType2";
        }
    }

    private static class ObjectWithCreator {
        private Object typeProperty1;
        private Object typeProperty2;

        public Object getTypeProperty1() {
            return this.typeProperty1;
        }

        public Object getTypeProperty2() {
            return this.typeProperty2;
        }

        public void setTypeProperty1(Object value) {
            this.typeProperty1 = value;
        }

        public void setTypeProperty2(Object value) {
            this.typeProperty2 = value;
        }
    }

    private static class ObjectCreator implements PropertyBasedCreator {
        @Override
        public Object createUsingGuid(JavaType type, JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ObjectWithCreator();
        }

        @Override
        public Object createUsingBeanInfo(JavaType type, BeanDescription beanDesc, Object beans) throws IOException {
            return new ObjectWithCreator();
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt, PropertyName propertyName, JsonValue value)
                throws IOException {
            return new ObjectWithCreator();
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt, BeanDescription beanDesc, JsonValue rootValue)
                throws IOException {
            return new ObjectWithCreator();
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt, PropertyName propertyName, PropertyValueBuffer propertyBuffer)
                throws IOException {
            return new ObjectWithCreator();
        }

        @Override
        public Object buildInstance(DeserializationContext ctxt, BeanDescription beanDesc, PropertyValueBuffer propertyBuffer)
                throws IOException {
            return new ObjectWithCreator();
        }
    }

    private static class SimpleSettableBeanProperty extends SettableBeanProperty {
        private static final long serialVersionUID = 1L;

        @Override
        public void set(Object bean, Object value) {
            // Do nothing
        }

        @Override
        public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean)
                throws IOException {
            // Do nothing
        }

        @Override
        public Object getValue(Object bean) {
            return null;
        }

        @Override
        public void valueSet(Object bean, Object newValue) {
            // Do nothing
        }
    }

    private static class TestContext extends DeserializationContext {
        // Empty for test purposes
    }
}