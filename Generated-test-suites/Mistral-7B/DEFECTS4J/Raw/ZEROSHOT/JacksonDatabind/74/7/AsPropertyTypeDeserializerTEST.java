package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test cases for {@link AsPropertyTypeDeserializer}.
 */
public class AsPropertyTypeDeserializerTest extends BaseTest {

    @Test
    public void testDeserializeTypedFromObjectWithProperty() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(AsPropertyTypeDeserializerTest.class);
        TypeIdResolver idResolver = mapper.getTypeFactory().getExtendedGoldenTypeIdResolver();
        String typePropertyName = "typeProperty";
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);

        // When
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);
        JsonParser jp = createParser("{\"typeProperty\":\"StringType\",\"data\":\"test\"}");
        Object result = deserializer.deserializeTypedFromObject(jp, new TestContext());

        // Then
        assertEquals("test", result);
        assertNull(jp.getCurrentName());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(AsPropertyTypeDeserializerTest.class);
        TypeIdResolver idResolver = mapper.getTypeFactory().getExtendedGoldenTypeIdResolver();
        String typePropertyName = "typeProperty";
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);

        // When
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);
        String typeId = "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializerTest";
        jpSetCurrentName(jp, "typeId");
        jpSetValue(jp, typeId);
        jpNextToken();
        Object result = deserializer.deserializeTypedFromObject(jp, new TestContext());

        // Then
        assertEquals(AsPropertyTypeDeserializerTest.class, result.getClass());
        assertEquals(typeId, jp.getText());
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(AsPropertyTypeDeserializerTest.class);
        TypeIdResolver idResolver = mapper.getTypeFactory().getExtendedGoldenTypeIdResolver();
        String typePropertyName = "typeProperty";
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);

        // When
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(createParser("{\"data\":42}"), new TestContext());

        // Then
        assertEquals(42, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObjectWithMissingProperty() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(AsPropertyTypeDeserializerTest.class);
        TypeIdResolver idResolver = mapper.getTypeFactory().getExtendedGoldenTypeIdResolver();
        String typePropertyName = "typeProperty";
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);

        // When
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, true, defaultImpl, As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(createParser("{\"data\":\"test\"}"), new TestContext());

        // Then
        fail("Expected JsonMappingException, but got: " + result);
    }

    // Add more test cases as needed

    private static JsonParser createParser(String json) throws IOException {
        return mapper.getFactory().createParser(jsonToBytes(json));
    }

    private static void jpSetCurrentName(JsonParser jp, String name) throws IOException {
        jp.setCurrentName(name);
        jp.nextToken();
    }

    private static void jpSetValue(JsonParser jp, String value) throws IOException {
        jp.setValue(value);
    }

    private static byte[] jsonToBytes(String json) {
        return mapper.writeValueAsBytes(json);
    }

    static class TestContext extends DeserializationContext {
        TestContext() {
            super(new ObjectMapper(), new BeanDescription[0]);
        }

        @Override
        public ObjectFactory getFactory() {
            return new ObjectFactory() {
                @Override
                public <T> T createInstance(Class<T> clazz) {
                    return mapper.getInstanceFactory().instantiateValue(clazz);
                }

                @Override
                public <T> T instantiateValue(Class<T> valueType) {
                    return mapper.getDeserializerFactory().createValueDeserializer(valueType, this).deserializeValue();
                }
            };
        }
    }
}