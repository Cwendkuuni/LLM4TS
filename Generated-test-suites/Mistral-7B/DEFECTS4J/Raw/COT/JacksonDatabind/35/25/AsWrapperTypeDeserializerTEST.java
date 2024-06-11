package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;
    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setup() throws JacksonAnnotationIntrospector.FindMixInAnnotations {
        objectMapper = new ObjectMapper();
        AsWrapperTypeDeserializer.MapsupWith _mapper = new AsWrapperTypeDeserializer.MapsupWith(objectMapper);
        AsWrapperTypeDeserializer baseTypeDeserializer = new AsWrapperTypeDeserializer(
                JavaType.constructParametricType(AsWrapperTypeDeserializer.class, Integer.class),
                null, "type", true, Integer.class);
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseTypeDeserializer, null);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        String json = "{\"type\":\"java.lang.Integer\",\"value\":42}";
        Integer result = (Integer) asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        String json = "[{\"type\":\"java.lang.Integer\",\"value\":42}]";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertArrayEquals(new Object[]{42}, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        String json = "42";
        Integer result = (Integer) asWrapperTypeDeserializer.deserializeTypedFromScalar(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        String json = "{\"type\":\"java.lang.String\",\"value\":\"Hello\"}";
        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals("Hello", result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonObjectFormat() throws IOException {
        String json = "{\"type\":\"java.lang.Integer\",\"value\":42,\"invalidField\":\"InvalidValue\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeIdField() throws IOException {
        String json = "{\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingValueField() throws IOException {
        String json = "{\"type\":\"java.lang.Integer\"}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeId() throws IOException {
        String json = "{\"type\":\"java.lang.InvalidType\",\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testNullTypeId() throws IOException {
        String json = "{\"type\":null,\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testEmptyTypeId() throws IOException {
        String json = "{\"type\":\"\",\"value\":42}";
        asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        String json = "{\"@type\":\"java.lang.Integer\",\"value\":42}";
        Integer result = (Integer) asWrapperTypeDeserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(42, result);
    }
}