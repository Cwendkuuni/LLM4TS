package com.example.junit; // replace with your testing package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeDeserializerUtils;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testAsPropertyTypeDeserializer_Constructor_WithTypeIdResolver() throws Exception {
        JavaType javaType = TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                JsonTypeFactory.instance.constructValueType(String.class),
                new TypeIdResolver() {
                    @Override
                    public IdReader getIdReaderForTypeId(TypeDeserializer deserializer, TypeId id) {
                        return null;
                    }

                    @Override
                    public TypeId idFromValue(Object value) {
                        return null;
                    }
                }, "typePropertyName", true, null);

        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(javaType,
                null, "typePropertyName", true, null, As.PROPERTY);
        assertEquals(As.PROPERTY, asPropertyTypeDeserializer.getTypeInclusion());
    }

    @Test
    public void testAsPropertyTypeDeserializer_Constructor_WithProperty() throws Exception {
        JavaType javaType = TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                JsonTypeFactory.instance.constructValueType(String.class),
                null, "typePropertyName", true, null);

        BeanProperty property = new BeanProperty("propertyName", JsonTypeFactory.instance.constructType(String.class));
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(javaType, null,
                "typePropertyName", true, null, property);
        assertEquals(property, asPropertyTypeDeserializer._property);
    }

    @Test
    public void testDeserializeTypedFromObject_WithPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":{\"type\":\"String\",\"value\":\"test\"}}";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(String.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@type\":\"java.lang.String\"}test";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(String.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_WithoutPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"test\"}";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(String.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        fail("Expected IOException but no exception was thrown");
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_WithInvalidPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalidPropertyName\":{\"type\":\"String\",\"value\":\"test\"}}";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(String.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        fail("Expected IOException but no exception was thrown");
    }

    @Test
    public void testDeserializeTypedFromObject_WithDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":123}";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(Integer.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithArrayWrapper() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"java.lang.String\"]test";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(String.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, mapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithUnknownType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"unknown\":\"test\"}";
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                TypeDeserializerUtils.constructType(AsPropertyTypeDeserializer.class,
                        JsonTypeFactory.instance.constructValueType(String.class), null,
                        "typePropertyName", true, null, As.PROPERTY), null,
                "typePropertyName", true, null, null);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Object> deserializer = asPropertyTypeDeserializer._findDeserializer(mapper.getDeserializationContext(), "java.lang.String");
        Object result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
        assertEquals("test", result);
    }
}