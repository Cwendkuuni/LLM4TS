package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Test;

/**
 * Test cases for {@link AsPropertyTypeDeserializer} class.
 */
public class AsPropertyTypeDeserializerTest extends BaseTest {

    @Test
    public void testConstructorWithTypeIdResolver() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        assertNotNull(deserializer);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testConstructorWithDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                createMock(StdDeserializer.class),
                As.PROPERTY);
        assertNotNull(deserializer);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testConstructorWithProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY,
                property);
        assertNotNull(deserializer);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        AsPropertyTypeDeserializer deserializer2 = deserializer1.forProperty(property);
        assertEquals(deserializer1, deserializer2);
    }

    @Test
    public void testGetTypeInclusion() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObjectWithValidTypeId() throws IOException {
        String json = "{\"type\":\"com.test.MyType\", \"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(MyType.class),
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(MyType.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws IOException {
        String json = "{\"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithInvalidTypeId() throws IOException {
        String json = "{\"type\":\"com.test.InvalidType\", \"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        deserializer.deserializeTypedFromObject(parser, createContext());
    }

    @Test
    public void testDeserializeTypedFromObjectWithValidNativeTypeId() throws IOException {
        String json = "{\"type\":\"com.test.MyType\", \"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(MyType.class),
                As.PROPERTY);
        Object result = deserializer._deserializeWithNativeTypeId(parser, createContext(), "com.test.MyType");
        assertEquals(MyType.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingTypeProperty() throws IOException {
        String json = "{\"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingPropertyName() throws IOException {
        String json = "{\"type\":\"com.test.MyType\", \"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_nonExistingPropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyObject() throws IOException {
        String json = "{}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullValue() throws IOException {
        String json = "{\"type\":\"com.test.MyType\", \"_typePropertyName\":null}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                getClassType(String.class),
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        String json = "{}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromObject(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromAnyWithNativeTypeId() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(List.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromAny(parser, createContext());
        assertEquals(List.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromAnyWithDefaultImpl() throws IOException {
        String json = "{\"_typePropertyName\":\"com.test.MyType\", \"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromAny(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromAnyWithObject() throws IOException {
        String json = "{\"type\":\"com.test.MyType\", \"data\":\"test\"}";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(String.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromAny(parser, createContext());
        assertEquals(String.class, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromAnyWithInvalidTypeId() throws IOException {
        String json = "[\"invalidTypeId\"]";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(List.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        deserializer.deserializeTypedFromAny(parser, createContext());
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(List.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromArray(parser, createContext());
        assertEquals(List.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        String json = "123";
        JsonParser parser = createParser(json);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                getClassType(Integer.class),
                createTypeIdResolver(),
                "_typePropertyName",
                true,
                null,
                As.PROPERTY);
        Object result = deserializer.deserializeTypedFromScalar(parser, createContext());
        assertEquals(Integer.class, result.getClass());
    }

    private TypeIdResolver createTypeIdResolver() {
        return new StandardTypeIdResolver();
    }

    private Object createMock(Class<?> clazz) {
        return mock(clazz);
    }

    private static Object getClassType(Class<?> clazz) {
        return JavaType.construct(clazz, new TypeFactory());
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }

}