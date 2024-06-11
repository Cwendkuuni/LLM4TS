package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TokenBuffer.RootContext;

/**
 * Test class for ExternalTypeHandler class
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testExternalTypeHandler() throws IOException {
        // Test constructor
        ExternalTypeHandler et = new ExternalTypeHandler(null, null, null, null);
        assertNotNull(et);

        // Test start method
        ExternalTypeHandler etStart = et.start();
        assertNotNull(etStart);

        // Test handleTypePropertyValue method with valid arguments
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser p = jsonFactory.createParser("{\"typeId\":\"StringType\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();
        String propName = "typeId";
        assertTrue(etStart.handleTypePropertyValue(p, ctxt, propName, bean));
        assertEquals("StringType", etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test handleTypePropertyValue method with invalid arguments
        p = jsonFactory.createParser("{\"invalidPropName\":\"StringType\"}");
        assertFalse(etStart.handleTypePropertyValue(p, ctxt, "invalidPropName", bean));
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test handlePropertyValue method with valid arguments
        p = jsonFactory.createParser("{\"propertyName\":\"StringProperty\"}");
        assertTrue(etStart.handlePropertyValue(p, ctxt, "propertyName", bean));
        assertEquals("StringProperty", etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test handlePropertyValue method with invalid arguments
        p = jsonFactory.createParser("{\"invalidPropName\":\"StringProperty\"}");
        assertFalse(etStart.handlePropertyValue(p, ctxt, "invalidPropName", bean));
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test complete method with valid arguments
        p.nextToken(); // consume "{"
        Map<String, Object> map = new HashMap<>();
        map.put("StringProperty", "testString");
        bean = map;
        Object result = etStart.complete(p, ctxt, bean);
        assertNotNull(result);
        assertEquals("testString", map.get("StringProperty"));
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test complete method with missing property
        map = new HashMap<>();
        map.put("MissingProperty", "testString");
        bean = map;
        try {
            etStart.complete(p, ctxt, bean);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing property 'MissingProperty' for external type id 'StringProperty'", e.getMessage());
        }
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test complete method with missing type id
        map = new HashMap<>();
        map.put("StringProperty", null);
        bean = map;
        try {
            etStart.complete(p, ctxt, bean);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing external type id property 'StringProperty'", e.getMessage());
        }
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test complete method with default type
        map = new HashMap<>();
        map.put("StringProperty", null);
        bean = map;
        ResultType resultType = new ResultType();
        etStart.linkDefaults(resultType);
        Object resultDefault = etStart.complete(p, ctxt, bean);
        assertNotNull(resultDefault);
        assertEquals(resultType.getDefaultType(), resultDefault.getClass());
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test complete method with creator
        map = new HashMap<>();
        map.put("StringProperty", "testString");
        map.put("StringCreator", new StringCreator());
        bean = map;
        ResultCreator resultCreator = new ResultCreator();
        etStart.linkCreators(resultCreator);
        Object resultCreatorResult = etStart.complete(p, ctxt, bean, new PropertyValueBuffer(), resultCreator);
        assertNotNull(resultCreatorResult);
        assertEquals(resultCreator.createdObject.getClass(), resultCreatorResult.getClass());
        assertEquals("testString", resultCreator.createdObject.toString());
        assertEquals(null, etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test _deserialize method
        p.nextToken(); // consume "{"
        ResultType resultTypeDeserialize = new ResultType();
        Object resultTypeDeserializeObject = etStart._deserialize(p, ctxt, etStart.getFocusIndex(), "StringType");
        assertNotNull(resultTypeDeserializeObject);
        assertEquals(resultTypeDeserialize.getDefaultType(), resultTypeDeserializeObject.getClass());
        etStart.nextIndex();

        // Test _deserializeAndSet method
        p.nextToken(); // consume "{"
        map = new HashMap<>();
        map.put("StringProperty", "testString");
        bean = map;
        etStart._deserializeAndSet(p, ctxt, bean, etStart.getFocusIndex(), "StringType");
        assertEquals("testString", map.get("StringProperty"));
        assertEquals("StringType", etStart.getFocusIndex() < etStart._properties.length ? etStart._typeIds[etStart.getFocusIndex()] : null);
        etStart.nextIndex();

        // Test Builder class
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty(new PropertyName("StringProperty"), String.class);
        TypeDeserializer typeDeserializer = mapper.findValueDeserializer(String.class, null);
        builder.addExternal(property, typeDeserializer);
        ExternalTypeHandler etBuilder = builder.build();
        ExternalTypeHandler etBuilderStart = etBuilder.start();
        assertTrue(etBuilderStart.handleTypePropertyValue(p, ctxt, "StringProperty", bean));
        assertEquals("StringType", etBuilderStart.getFocusIndex() < etBuilderStart._properties.length ? etBuilderStart._typeIds[etBuilderStart.getFocusIndex()] : null);
        etBuilderStart.nextIndex();
        assertTrue(etBuilderStart.handlePropertyValue(p, ctxt, "StringProperty", bean));
        assertEquals("testString", map.get("StringProperty"));
        assertEquals("StringType", etBuilderStart.getFocusIndex() < etBuilderStart._properties.length ? etBuilderStart._typeIds[etBuilderStart.getFocusIndex()] : null);
    }

    private static class ResultType extends BeanDeserializerBase {
        public static final Class<?> DEFAULT_TYPE = ResultType.class;
        public static final As DEFAULT_AS = As.EXTERNAL_PROPERTY;

        public ResultType() {
            super(DEFAULT_TYPE, DEFAULT_AS);
        }

        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return ctxt.getInstanceProvider().getNeutralValue();
        }

        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
            return ctxt.getInstanceProvider().getNeutralValue();
        }
    }

    private static class ResultTypeIdResolver extends TypeDeserializer.Base {
        public static final Class<?> DEFAULT_TYPE = ResultTypeIdResolver.class;

        @Override
        public String idFromValueAndType(String name, Class<?> valueType) {
            return "ResultType";
        }
    }

    private static class ResultTypeHandler extends ExternalTypeHandler {
        public ResultTypeHandler() {
            super(new ExternalTypeHandler.Builder().build());
        }

        @Override
        protected Object _deserialize(JsonParser p, DeserializationContext ctxt, int index, String typeId) throws IOException {
            // This method should not be called as typeId is null in ExternalTypeHandlerTest
            fail("Unexpected call to _deserialize method");
            return null;
        }

        @Override
        protected void _deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, int index, String typeId) throws IOException {
            // This method should not be called as typeId is null in ExternalTypeHandlerTest
            fail("Unexpected call to _deserializeAndSet method");
        }
    }

    private static class ResultCreator extends ValueDeserializer {
        public static final Class<?> DEFAULT_TYPE = ResultCreator.class;
        public static final As DEFAULT_AS = As.PROPERTY;
        public static final PropertyName CREATOR_PROPERTY_NAME = new PropertyName("StringCreator");

        private Object createdObject;

        public ResultCreator() {
            this(null, null);
        }

        public ResultCreator(Class<?>creatorType, PropertyName creatorPropertyName) {
            super(DEFAULT_TYPE, DEFAULT_AS);
            this.createdObject = creatorType != null ? mapper.readValue(p, creatorType) : null;
            this.setCreatorPropertyName(creatorPropertyName);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
            return null;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt, PropertyName propertyName) throws IOException {
            this.createdObject = mapper.readValue(p, propertyName.getRawValue());
            return this;
        }

        @Override
        public Object createValueInstance(SerializerProvider prov, PropertyName propertyName) throws IOException {
            return this;
        }

        @Override
        public void setAllProperties(Object instance, JsonParser p, DeserializationContext ctxt) throws IOException {
            if (createdObject != null) {
                BeanDeserializerBase deserializer = mapper.findBeanDeserializer(createdObject.getClass(), null);
                deserializer.deserialize(p, ctxt, createdObject);
            }
        }
    }

    private static class StringCreator extends ResultCreator {
        public static final PropertyName CREATOR_PROPERTY_NAME = new PropertyName("StringCreator");

        public StringCreator() {
            super(String[].class, CREATOR_PROPERTY_NAME);
        }

        @Override
        public Object createValueInstance(SerializerProvider prov, PropertyName propertyName) throws IOException {
            return new String[0];
        }

        @Override
        public void setAllProperties(Object instance, JsonParser p, DeserializationContext ctxt) throws IOException {
            super.setAllProperties(instance, p, ctxt);
            if (createdObject != null) {
                Arrays.fill((String[]) createdObject, "testString");
            }
        }
    }
}