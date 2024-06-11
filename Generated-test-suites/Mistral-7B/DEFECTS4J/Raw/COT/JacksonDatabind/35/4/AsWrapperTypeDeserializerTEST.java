package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    private ObjectMapper objectMapper;
    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        AsWrapperTypeDeserializer.BaseType baseType = new AsWrapperTypeDeserializer.BaseType();
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType.getClass(), objectMapper.getTypeIdResolver(), "_type", true, null);
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"typeId\":\"BaseType\",\"value\":42}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
        assertEquals(baseType, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"typeId\":\"BaseType\",\"value\":42}]";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(objectMapper.parse(json), context());
        assertEquals(baseType, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "{\"typeId\":\"BaseType\",\"value\":42}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(objectMapper.parse(json), context());
        assertEquals(baseType, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "{\"typeId\":\"BaseType\",\"value\":42}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(objectMapper.parse(json), context());
        assertEquals(baseType, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWhenNoTypeId() throws Exception {
        String json = "{\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWhenInvalidTypeId() throws Exception {
        String json = "{\"typeId\":\"InvalidType\",\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWhenMissingTypeId() throws Exception {
        String json = "{\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWhenMissingValue() throws Exception {
        String json = "{\"typeId\":\"BaseType\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$BaseType\",\"value\":42}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
        assertEquals(baseType, result);
    }

    @Test
    public void testDeserializeWithMergedTypeId() throws Exception {
        String json = "{\"typeId\":\"BaseType\",\"@type\":\"BaseType\",\"value\":42}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.parse(json), context());
        assertEquals(baseType, result);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializer deserializer = asWrapperTypeDeserializer.findDeserializer(context(), "BaseType");
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerWithNullTypeId() throws Exception {
        asWrapperTypeDeserializer.findDeserializer(context(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerWithEmptyTypeId() throws Exception {
        asWrapperTypeDeserializer.findDeserializer(context(), "");
    }
}