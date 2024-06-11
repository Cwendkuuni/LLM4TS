package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        String json = "{\"typeName\":\"java.lang.String\",\"foo\":\"bar\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), null);
        assertEquals("bar", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(int[].class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                int[].class);

        String json = "[{\"typeName\":\"java.lang.Integer\",1}]";
        Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), null);
        assertEquals(1, ((int[]) result)[0]);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(Double.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                Double.class);

        String json = "{\"typeName\":\"java.lang.Double\",\"value\":4.0}";
        Object result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(json), null);
        assertEquals(4.0, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(List.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                List.class);

        String json = "[{\"typeName\":\"java.util.ArrayList\",[1,2,3]}]";
        Object result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), null);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        String jsonWithNativeTypeId = "{\"typeName\":\"java.lang.String\",\"foo\":\"bar\"}";
        Object result = deserializer._deserialize(mapper.getFactory().createParser(jsonWithNativeTypeId), null);
        assertEquals("bar", result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidJsonObject() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        String json = "{foo: \"bar\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), null);
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidJsonArray() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(int[].class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                int[].class);

        String json = "[1]";
        deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), null);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        assertEquals(stringDeserializer, deserializer._findDeserializer(null, "java.lang.String"));
    }

    @Test
    public void testDeserializeWithEmptyTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        String json = "{}";
        Object result = deserializer._deserialize(mapper.getFactory().createParser(json), null);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithInvalidTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        String json = "{\"typeName\":\"UnknownType\",\"value\":\"bar\"}";
        deserializer._deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test
    public void testMergeTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeName",
                true,
                String.class);

        String json = "{\"typeName\":{\"typeName\":\"java.lang.String\",\"foo\":\"bar\"}}";
        Object result = deserializer._deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("bar", result);
    }
}