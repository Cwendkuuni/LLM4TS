package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Unit tests for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializer;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setUp() throws IOException {
        JavaType baseType = JsonTypeFactory.constructType(Object.class);
        typeIdResolver = new TestTypeIdResolver();
        typeDeserializer = new TypeDeserializerBase(baseType, typeIdResolver, "_typePropertyName", true, null);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
        TypeDeserializer typeDeserializerForProperty = typeDeserializer.forProperty(beanProperty);
        assertNotNull(typeDeserializerForProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializer.getTypeInclusion();
        assertTrue(typeInclusion == JsonTypeInfo.As.EXISTING_PROPERTY);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializer.baseTypeName();
        assertEquals("java.lang.Object", baseTypeName);
    }

    @Test
    public void testPropertyName() {
        String propertyName = typeDeserializer.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testToString() {
        String toString = typeDeserializer.toString();
        assertTrue(toString.contains("['TypeDeserializerBase"));
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyWithoutBeanProperty() throws IOException {
        typeDeserializer.forProperty(null);
    }

    @Test
    public void testFindDeserializerNullContext() throws IOException {
        DeserializationContext context = null;
        String typeId = "testTypeId";
        assertNull(typeDeserializer._findDeserializer(context, typeId));
    }

    @Test
    public void testFindDeserializerWithTypeId() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        String typeId = "TestType";
        JsonDeserializer<Object> deserializer = new TestJsonDeserializer();
        typeDeserializer._deserializers.put(typeId, deserializer);
        assertEquals(deserializer, typeDeserializer._findDeserializer(context, typeId));
    }

    @Test
    public void testFindDeserializerWithMissingTypeId() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        String typeId = "MissingTypeId";
        assertNull(typeDeserializer._findDeserializer(context, typeId));
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        DeserializationContext context = null;
        assertNull(typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerWithProperty() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        assertEquals(TestNullifyingDeserializer.instance, typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerWithoutProperty() throws IOException {
        DeserializationContext context = new TestJsonFactory();
        assertNull(typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        DeserializationContext context = new TestJsonFactory();
        JavaType defaultImpl = JsonTypeFactory.constructType(TestDefaultImpl.class);
        typeDeserializer._defaultImpl = defaultImpl;
        JsonDeserializer<Object> deserializer = context.findContextualValueDeserializer(defaultImpl, null);
        assertEquals(deserializer, typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        DeserializationContext context = null;
        JsonParser jsonParser = new TestJsonFactory().createParser(_jsonString);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        Object result = typeDeserializer._deserializeWithNativeTypeId(context.createParser("{\"typeId\":\"TestType\"}"), context);
        assertTrue(result instanceof TestType);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithoutTypeId() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        Object result = typeDeserializer._deserializeWithNativeTypeId(context.createParser("{}"), context);
        assertTrue(result instanceof TestDefaultImpl);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        DeserializationContext context = null;
        String typeId = "UnknownTypeId";
        typeDeserializer._handleUnknownTypeId(context, typeId);
    }

    @Test
    public void testHandleUnknownTypeIdWithTypeId() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        String typeId = "UnknownTypeId";
        String extraDesc = typeDeserializer._handleUnknownTypeId(context, typeId).toString();
        assertTrue(extraDesc.contains("type ids are not statically known"));
    }

    @Test
    public void testHandleMissingTypeIdNullContext() throws IOException {
        DeserializationContext context = null;
        String extraDesc = typeDeserializer._handleMissingTypeId(context, "missingTypeId").toString();
        assertTrue(extraDesc.contains("java.lang.Object"));
    }

    @Test
    public void testHandleMissingTypeIdWithProperty() throws IOException {
        DeserializationContext context = new TestJsonFactory().createParser(_jsonString).getDeserializationContext();
        String extraDesc = typeDeserializer._handleMissingTypeId(context, "_testProperty").toString();
        assertTrue(extraDesc.contains("TypeDeserializerBase"));
    }

    private static class TestJsonFactory extends TestBaseJsonFactory {
        @Override
        public DeserializationContext createContext(JsonParser p) {
            return createContext(p, new ObjectMapper());
        }

        @Override
        public DeserializationContext createContext(JsonParser p, ObjectMapper mapper) {
            return mapper.getDeserializationContext().withBehavior(JsonDeserializer.Feature.FAIL_ON_EMPTY_BEANS, false);
        }
    }

    private static class TestJsonDeserializer extends TestBaseJsonDeserializer<TestType> {
        @Override
        public TestType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new TestType();
        }
    }

    private static class TestNullifyingDeserializer extends NullifyingDeserializer {
        public static final TestNullifyingDeserializer instance = new TestNullifyingDeserializer();

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class TestDefaultImpl {
    }

    private static class TestType {
    }

    private static String _jsonString = "{\"typeId\":\"TestType\"}";
}