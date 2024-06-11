package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private TypeIdResolver typeIdResolver;
    private AsWrapperTypeDeserializer deserializer;

    @org.junit.Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        typeIdResolver = objectMapper.getTypeFactory();
        deserializer = new AsWrapperTypeDeserializer(typeIdResolver.constructType(String.class), typeIdResolver, null, false, String.class);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"hello\"}");
        assertEquals("hello", deserializer.deserializeTypedFromObject(jp, new TestContext()));
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("[\":java.lang.String\",{\"value\":\"hello\"}]");
        assertEquals("hello", deserializer.deserializeTypedFromArray(jp, new TestContext()));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("\"hello\":java.lang.String");
        assertEquals("hello", deserializer.deserializeTypedFromScalar(jp, new TestContext()));
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.Number\",\"value\":\"42\"}");
        assertNull(deserializer.deserializeTypedFromAny(jp, new TestContext()));

        jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"hello\"}");
        assertEquals("hello", deserializer.deserializeTypedFromAny(jp, new TestContext()));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"hello\"}");
        jp.nextToken(); // consume START_OBJECT
        jp.nextToken(); // consume FIELD_NAME
        jp.nextToken(); // consume STRING typeId
        jp.nextToken(); // consume COLON
        jp.nextToken(); // consume START_OBJECT
        jp.nextToken(); // consume FIELD_NAME
        jp.nextToken(); // consume STRING value
        jp.nextToken(); // consume END_OBJECT

        assertEquals("hello", deserializer.deserializeTypedFromObject(jp, new TestContext()));
    }

    @Test(expected = IOException.class)
    public void testWrongTokenException() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\"}"); // missing value
        deserializer.deserializeTypedFromObject(jp, new TestContext());
    }

    @Test(expected = IOException.class)
    public void testWrongTokenType() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.Number\",\"value\":\"hello\"}"); // incorrect type
        deserializer.deserializeTypedFromObject(jp, new TestContext());
    }

    @Test(expected = IOException.class)
    public void testMissingEndObject() throws IOException {
        JsonParser jp = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"hello\"}");
        deserializer.deserializeTypedFromObject(jp, new TestContext());
        jp.nextToken(); // consume END_OBJECT
    }

    static class TestContext extends DeserializationContext {
        @Override
        public DeserializationConfig getConfig() {
            return null;
        }

        @Override
        public JavaType typeDeserializerForProperty(BeanProperty property) {
            return null;
        }

        @Override
        public JavaType typeDeserializerForAnnotation(BeanProperty property) {
            return null;
        }

        @Override
        public JavaType typeDeserializerForRootValue() {
            return null;
        }

        @Override
        public JavaType typeDeserializerForValue() {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> cls) throws JavaLangMappingException {
            return null;
        }

        @Override
        public Object findValueDeserializer(TypeDeserialator tdes) {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> cls, JsonDeserializer<?> deser) throws JavaLangMappingException {
            return null;
        }

        @Override
        public Object findValueDeserializer(TypeDeserializer tdes, JsonDeserializer<?> deser) {
            return null;
        }

        @Override
        public Object findBeanDeserializer(Class<?> cls) throws JavaLangMappingException {
            return null;
        }

        @Override
        public Object findBeanDeserializer(BeanDescription beanDesc) {
            return null;
        }

        @Override
        public Object findValueInjector(Object value, TypeDeserializer valueTypeDeserializer) {
            return null;
        }

        @Override
        public Object findValueInjector(Object value, TypeDeserializer valueTypeDeserializer, PropertyName propertyName) {
            return null;
        }

        @Override
        public Object findBeanInjector(BeanDescription beanDesc) {
            return null;
        }

        @Override
        public Object findBeanInjector(BeanDescription beanDesc, JavaType propertyType) {
            return null;
        }

        @Override
        public void reportBadTypeId(Class<?> cls, String msg) {
        }

        @Override
        public void reportBadTypeId(Class<?> cls, String msg, Throwable t) {
        }

        @Override
        public void reportBadValueForFormat(Class<?> cls, String msg) {
        }

        @Override
        public void reportBadValueForFormat(Class<?> cls, String msg, Throwable t) {
        }

        @Override
        public void reportUnexpectedTypeId(String typeId, String msg) {
        }

        @Override
        public void reportUnexpectedTypeId(String typeId, String msg, Throwable t) {
        }

        @Override
        public void reportUnexpectedTypeId(TypeDeserializer td, String msg) {
        }

        @Override
        public void reportUnexpectedTypeId(TypeDeserializer td, String msg, Throwable t) {
        }

        @Override
        public void reportError(String msg) {
        }

        @Override
        public void reportError(String msg, Throwable t) {
        }

        @Override
        public JavaType findTypeDeserializer(JavaType type) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(Class<?> cls) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(BeanProperty property) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(BeanProperty property, JavaType propertyType) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(Class<?> cls, JavaType propertyType) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(JavaType propertyType) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(Class<?> cls, JavaType propertyType) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(JavaType propertyType, JavaType baseType) {
            return null;
        }

        @Override
        public JavaType findTypeDeserializer(Class<?> baseType, JavaType propertyType) {
            return null;
        }

        @Override
        public void reportError(Exception e) {
        }

        @Override
        public void reportError(Exception e, String msg) {
        }

        @Override
        public void reportError(Exception e, String msg, String details) {
        }

        @Override
        public void reportError(Exception e, String msg, String details, Throwable cause) {
        }

        @Override
        public void reportError(Exception e, String msg, String details, Throwable cause, Location loc) {
        }
    }
}