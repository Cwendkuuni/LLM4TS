package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.createParser;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeResolverBuilder;

public class AsPropertyTypeDeserializerTest {

    private static final JavaType INT_TYPE = TypeFactory.defaultInstance().constructType(int.class);
    private static final JavaType STRING_TYPE = TypeFactory.defaultInstance().constructType(String.class);

    @Test
    public void testDeserializeTypedFromObject_TypePropertyPresentAndVisible() throws IOException {
        String json = "{\"type\":\"com.test.MyClass\", \"data\":42}";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, "type", true, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromObject(p, mapper.getDeserializationContext());
        assertInstanceOf(Integer.class, result);
    }

    @Test
    public void testDeserializeTypedFromObject_TypePropertyPresentButInvisible() throws IOException {
        String json = "{\"type\":\"com.test.MyClass\", \"data\":42}";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, "type", false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromObject(p, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_TypePropertyNotPresent() throws IOException {
        String json = "{\"data\":42}";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, null, false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromObject(p, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_TypeIdPresent() throws IOException {
        String json = "{\"type\":\"com.test.MyClass\", \"id\":\"myTypeId\", \"data\":42}";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, null, false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromObject(p, mapper.getDeserializationContext());
        assertInstanceOf(Integer.class, result);
    }

    @Test
    public void testDeserializeTypedFromObject_TypeIdNotPresent() throws IOException {
        String json = "{\"data\":42}";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, null, false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromObject(p, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObject_MissingProperty() throws IOException {
        String json = "{\"data\":42}";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, "missingProperty", false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromObject(p, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray_TypeIdPresent() throws IOException {
        String json = "[{\"type\":\"com.test.MyClass\", \"id\":\"myTypeId\"}]";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, null, false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromArray(p, mapper.getDeserializationContext());
        assertInstanceOf(Integer[].class, result);
    }

    @Test
    public void testDeserializeTypedFromArray_TypeIdNotPresent() throws IOException {
        String json = "[42]";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, INT_TYPE, idRes, null, false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromArray(p, mapper.getDeserializationContext());
        assertInstanceOf(Integer[].class, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        String json = "42";
        JsonParser p = createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        TypeResolverBuilder typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class, STRING_TYPE, idRes, null, false, null);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) mapper.findTypeDeserializer(javaType, null);

        Object result = deserializer.deserializeTypedFromScalar(p, mapper.getDeserializationContext());
        assertInstanceOf(String.class, result);
    }
}