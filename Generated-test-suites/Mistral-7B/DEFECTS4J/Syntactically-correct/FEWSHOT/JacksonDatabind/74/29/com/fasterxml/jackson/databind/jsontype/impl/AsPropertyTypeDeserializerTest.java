package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.TestTokens;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, idResolver, "typePropertyName", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"typePropertyName\": \"String\"}");
        Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertEquals("String", result);

        parser = mapper.getFactory().createParser("{\"typePropertyName\": null}");
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        parser = mapper.getFactory().createParser("{\"typePropertyName\": [1, 2, 3]}");
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        parser = mapper.getFactory().createParser("{foo: \"String\"}");
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        parser = mapper.getFactory().createParser("{\"typePropertyName\": {}}");
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        parser = mapper.getFactory().createParser("{\"typePropertyName\": [1, \"String\"]}");
        assertNull(deserializer.deserializeTypedFromObject(parser, new TestContext()));

        parser = mapper.getFactory().createParser("{\"typePropertyName\": [\"String\"]}");
        assertEquals(JavaType.valueOf(String[].class), deserializer.getTypeDeserializer().forProperty(null).getType());
        Object arrayResult = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertArrayEquals(new String[]{"String"}, (Object) arrayResult);

        parser = mapper.getFactory().createParser("{\"typePropertyName\": {\"typeId\": \"com.fasterxml.jackson.databind.JsonNode\", \"typePropertyName\": \"node\"}}");
        Object nodeResult = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertTrue(nodeResult instanceof JsonNode);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, idResolver, "typePropertyName", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("[\"String\"]");
        Object result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertArrayEquals(new String[]{"String"}, (Object) result);

        parser = mapper.getFactory().createParser("[null]");
        assertArrayEquals(new String[0], deserializer.deserializeTypedFromArray(parser, new TestContext()));

        parser = mapper.getFactory().createParser("[1]");
        assertArrayEquals(new String[0], deserializer.deserializeTypedFromArray(parser, new TestContext()));

        parser = mapper.getFactory().createParser("[1, \"String\"]");
        assertArrayEquals(new String[]{"String"}, deserializer.deserializeTypedFromArray(parser, new TestContext()));

        parser = mapper.getFactory().createParser("[{\"typeId\": \"com.fasterxml.jackson.databind.JsonNode\", \"typePropertyName\": \"node\"}]");
        Object nodeResult = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertTrue(nodeResult instanceof JsonNode);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, idResolver, "typePropertyName", true, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("5");
        Object result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertEquals(5, result);
    }

    static class TestContext extends TestBaseContext {}
}