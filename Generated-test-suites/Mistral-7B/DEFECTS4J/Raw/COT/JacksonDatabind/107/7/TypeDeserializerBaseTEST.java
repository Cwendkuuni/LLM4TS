package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TEST_INSTANCE = new TestTypeDeserializerBase();

    @SuppressWarnings("rawtypes")
    private static final BeanProperty TEST_PROPERTY = new BeanProperty.Std("testProperty", String.class);

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        TypeDeserializer actual = TEST_INSTANCE.forProperty(TEST_PROPERTY);
        assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() {
        TEST_INSTANCE.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As actual = TEST_INSTANCE.getTypeInclusion();
        assertNotNull(actual);
    }

    @Test
    public void testBaseTypeName() {
        String actual = TEST_INSTANCE.baseTypeName();
        assertNotNull(actual);
    }

    @Test
    public void testGetPropertyName() {
        String actual = TEST_INSTANCE.getPropertyName();
        assertNotNull(actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver actual = TEST_INSTANCE.getTypeIdResolver();
        assertNotNull(actual);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> actual = TEST_INSTANCE.getDefaultImpl();
        assertNotNull(actual);
    }

    @Test
    public void testBaseType() {
        JavaType actual = TEST_INSTANCE.baseType();
        assertNotNull(actual);
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> actual = TEST_INSTANCE._findDeserializer(ctxt, null);
        assertNull(actual);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "unknownTypeId";
        TEST_INSTANCE._findDeserializer(ctxt, typeId);
    }

    @Test
    public void testFindDeserializerKnownTypeId() throws IOException {
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "StringDeserializer";
        JsonDeserializer<Object> actual = TEST_INSTANCE._findDeserializer(ctxt, typeId);
        assertNotNull(actual);
        assertTrue(actual instanceof StringDeserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        TEST_INSTANCE._findDefaultImplDeserializer(null);
    }

    @Test
    public void testFindDefaultImplDeserializerDefaultImplIsNotNull() throws IOException {
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> actual = TEST_INSTANCE._findDefaultImplDeserializer(ctxt);
        assertNotNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerDefaultImplIsNull() throws IOException {
        TEST_INSTANCE._findDefaultImplDeserializer(new TestDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JsonParser jp = new TestJsonParser();
        Object actual = TEST_INSTANCE._deserializeWithNativeTypeId(jp, new TestDeserializationContext(), null);
        fail("Should throw JsonMappingException");
    }

    @Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws IOException {
        JsonParser jp = new TestJsonParser();
        String typeId = "StringDeserializer";
        Object actual = TEST_INSTANCE._deserializeWithNativeTypeId(jp, new TestDeserializationContext(), typeId);
        assertNotNull(actual);
        assertTrue(actual instanceof String);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        TEST_INSTANCE._handleUnknownTypeId(new TestDeserializationContext(), null);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = new TestDeserializationContext();
        String extraDesc = "Missing type id";
        JavaType actual = TEST_INSTANCE._handleMissingTypeId(ctxt, extraDesc);
        assertNotNull(actual);
    }
}

class TestDeserializationContext extends DeserializationContext {
    @Override
    public JavaType typeIdResolutionStatus(DeserializationFeature f) {
        return null;
    }

    @Override
    public JavaType typeIdFromId(JsonParser p, String id) throws IOException {
        return null;
    }
}

class TestJsonParser extends JsonParser {
    private static final long serialVersionUID = 1L;

    @Override
    public String getText() {
        return "";
    }

    @Override
    public char nextChar() throws IOException, JsonProcessingException {
        return 0;
    }

    @Override
    public int nextValueAsInt() throws IOException, JsonProcessingException {
        return 0;
    }

    @Override
    public String getTypeId() {
        return null;
    }
}

class StringDeserializer extends JsonDeserializer<String> {
    // empty implementation
}

class TestTypeDeserializerBase extends TypeDeserializerBase {
    // empty constructor and fields
}