package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                Object.class, new StandardTypeIdResolver(), "_typeId", true, String.class);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"value\":\"test\"}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
        Assert.assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        String json = "[{\"typeId\":\"java.lang.String\",\"value\":\"test\"}]";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(objectMapper.getFactory().createParser(json), new TestContext());
        Assert.assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"value\":\"test\"}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(objectMapper.getFactory().createParser(json), new TestContext());
        Assert.assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        String json = "[{\"typeId\":\"java.lang.String\",\"value\":\"test\"}]";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(objectMapper.getFactory().createParser(json), new TestContext());
        Assert.assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws IOException {
        String json = "[{\"typeId\":\"java.lang.String\",\"value\":\"test\"}notValidJson";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws IOException {
        String json = "{\"value\":\"test\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeId() throws IOException {
        String json = "{\"typeId\":\"invalidTypeId\",\"value\":\"test\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
    }

    @Test
    public void testMergeTypeId() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"_typeId\":{\"typeId\":\"java.util.Map\",\"value\":{}}," +
                "\"value\":\"test\"}";
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("_typeId", new HashMap<>());
        expectedResult.put("value", "test");
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeIdMerging() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"_typeId\":{\"typeId\":\"java.util.Map\",\"value\":{}}," +
                "\"value\":\"test\",\"notValidJson\":\"notValidJson\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
    }

    @Test
    public void testFindDeserializerWithNativeTypeId() throws IOException {
        String json = "{\"typeId\":\"java.util.Map\",\"value\":{\"key\":\"testKey\",\"value\":\"testValue\"}}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
        Assert.assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        String json = "{\"typeId\":\"invalidTypeId\",\"value\":\"testValue\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class TestContext extends DeserializationContext {
        @Override
        public BeanDescription getBeanDescription() {
            return null;
        }

        @Override
        public ObjectFactory getAnnotationIntrospector() {
            return null;
        }

        @Override
        public ObjectFactory getFactory() {
            return null;
        }

        @Override
        public JavaType getTypeFactory() {
            return null;
        }

        @Override
        public BeanDescription findBeanDescription(Class<?> beanClass, MapperConfig config) {
            return null;
        }

        @Override
        public BeanDescription findBeanDescription(Class<?> beanClass, MapperConfig config, String name) {
            return null;
        }

        @Override
        public BeanDescription findBeanDescription(String name, MapperConfig config) {
            return null;
        }

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }

        @Override
        public void reportError(MessageBase message) throws IOException {
            throw new RuntimeException(message.toString());
        }

        @Override
        public void reportError(MessageBase message, Throwable t) throws IOException {
            throw new RuntimeException(message.toString(), t);
        }

        @Override
        public void reportWarning(MessageBase message) {
            // do nothing
        }

        @Override
        public void reportWarning(MessageBase message, Throwable t) {
            // do nothing
        }

        @Override
        public void handleMissingClass(Class<?> type, Object context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(String tn, JavaType type, Object context) throws IOException {
            // do nothing
        }

        @Override
        public JavaType handleUnknownProperty(String name, Object bean, JavaType type, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public void handleInvalidTypeId(JavaType id, JavaType type, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleIncompleteTypeId(JavaType type, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleNonWritableProperty(String propName, JavaType type, Object bean, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleNoDefaultConstructor(Class<?> type, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleUnknownClass(Class<?> type, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleUnexpectedTypeId(JavaType typeId, JavaType expectedTypeId, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(String propertyName, JavaType propertyType, Object bean, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(PropertyName propertyName, JavaType propertyType, Object bean, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleFieldMissing(FieldDesc fieldDesc, JavaType type, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleFieldMissing(FieldDesc fieldDesc, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(PropertyName propertyName, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(String propertyName, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(String propertyName, BeanProperty property, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handlePropertyMissing(PropertyName propertyName, BeanProperty property, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handlePropertyMissing(String propertyName, BeanProperty property, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(FieldDesc fieldDesc, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(PropertyName propertyName, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public void handleMissingValue(PropertyName propertyName, BeanProperty property, Object instance, DeserializationContext context) throws IOException {
            // do nothing
        }

        @Override
        public Object findCreator(Class<?> constructorType, JavaType[] parameterTypes, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(String propertyName, JavaType type, Object parent, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(PropertyName propertyName, JavaType type, Object parent, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(String fieldName, JavaType type, Object parent, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(PropertyDescriptor propertyDescriptor, JavaType type, Object parent, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(PropertyName propertyName, JavaType type, Object parent, DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(String propertyName, JavaType type, Object parent, DeserializationContext context, boolean allowEmpty) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(PropertyName propertyName, JavaType type, Object parent, DeserializationContext context, boolean allowEmpty) throws IOException {
            return null;
        }

        @Override
        public Object findValueInRoot(String propertyName, JavaType type, Object parent, DeserializationContext context, boolean allowEmpty, boolean isSelectMethod) throws IOException {
            return null;
        }
    }
}