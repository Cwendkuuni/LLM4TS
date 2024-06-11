package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;
    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        context = new TestContext();
        parser = context.createParser(TypeDeserializerBase.class.getResourceAsStream("/TestType.json"));
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = new BeanProperty(null, "test");
        TypeDeserializer typeDeserializer = testee.forProperty(property);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws IOException {
        JsonTypeInfo.As typeInclusion = testee.getTypeInclusion();
        assertNotNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        String baseTypeName = testee.baseTypeName();
        assertTrue(baseTypeName.length() > 0);
    }

    @Test
    public void testgetPropertyName() throws IOException {
        String propertyName = testee.getPropertyName();
        assertTrue(propertyName.length() > 0);
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        TypeIdResolver typeIdResolver = testee.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        Class<?> defaultImpl = testee.getDefaultImpl();
        assertTrue(defaultImpl != null);
    }

    @Test
    public void testToString() throws IOException {
        String toString = testee.toString();
        assertTrue(toString.length() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullContext() throws IOException {
        testee._findDeserializer(null, "typeId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        testee._findDeserializer(context, null);
    }

    @Test
    public void testFindDeserializerEmptyMap() throws IOException {
        TypeDeserializerBase emptyMapTestee = new TypeDeserializerBase(
                null, null, null, false, null) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers = new HashMap<>();
        };
        JsonDeserializer<Object> deserializer = emptyMapTestee._findDeserializer(context, "typeId");
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithDeserializer() throws IOException {
        testee._deserializers.put("typeId", new JsonDeserializer<Object>() {});
        JsonDeserializer<Object> deserializer = testee._findDeserializer(context, "typeId");
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerMissingDeserializer() throws IOException {
        testee._findDeserializer(context, "missingTypeId");
    }

    @Test
    public void testFindContextualValueDeserializerNullContext() throws IOException {
        testee._findContextualValueDeserializer(null, null);
    }

    @Test
    public void testFindContextualValueDeserializerNullType() throws IOException {
        testee._findContextualValueDeserializer(context, null);
    }

    @Test
    public void testFindContextualValueDeserializerWithDeserializer() throws IOException {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        JsonDeserializer<String> deserializer = context.findContextualValueDeserializer(javaType, null);
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindContextualValueDeserializerMissingDeserializer() throws IOException {
        testee._findContextualValueDeserializer(context, null);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        testee._findDefaultImplDeserializer(null);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNullProperty() throws IOException {
        testee._findDefaultImplDeserializer(context);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNoDeserializer() throws IOException {
        testee._findDefaultImplDeserializer(context);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDeserializer()
        throws IOException {
        testee._defaultImplDeserializer = context.findContextualValueDeserializer(testee._defaultImpl, null);
        JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        testee._deserializeWithNativeTypeId(null, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        testee._deserializeWithNativeTypeId(parser, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdMissingDeserializer() throws IOException {
        testee._deserializeWithNativeTypeId(parser, context, null);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithDeserializer() throws IOException {
        testee._deserializers.put("typeId", new JsonDeserializer<Object>() {});
        Object deserialized = testee._deserializeWithNativeTypeId(parser, context, "typeId");
        assertNotNull(deserialized);
    }

    @Test
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        JavaType javaType = null;
        JavaType handledType = testee._handleUnknownTypeId(context, null, null, javaType);
        assertNull(handledType);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleUnknownTypeIdNullTypeIdResolver() throws IOException {
        TypeIdResolver idResolver = null;
        JavaType handledType = testee._handleUnknownTypeId(context, null, idResolver, null);
        assertNull(handledType);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws IOException {
        JavaType baseType = null;
        JavaType handledType = testee._handleUnknownTypeId(context, null, null, baseType);
        assertNull(handledType);
    }

    @Test
    public void testHandleUnknownTypeIdWithTypeIdResolver() throws IOException {
        TypeIdResolver idResolver = context.getTypeIdResolver();
        JavaType handledType = testee._handleUnknownTypeId(context, null, idResolver, testee._baseType);
        assertNotNull(handledType);
    }
}