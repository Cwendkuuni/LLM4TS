package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.util.TestUtil.createParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static AsWrapperTypeDeserializer getDeserializerUnderTest(JavaType baseType) {
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        return new AsWrapperTypeDeserializer(baseType, idResolver, null, false, null);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("{\"typeId\":\"java.lang.Integer\",\"value\":42}");
        Object result = deserializer.deserializeTypedFromObject(parser, new TestContext());
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("[{\"typeId\":\"java.lang.Integer\",\"value\":42}]");
        Object result = deserializer.deserializeTypedFromArray(parser, new TestContext());
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(String.class);

        JsonParser parser = createParser("\"typeId\":\"java.lang.String\",\"value\":\"Hello\"");
        Object result = deserializer.deserializeTypedFromScalar(parser, new TestContext());
        assertEquals("Hello", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Double.class);

        JsonParser parser = createParser("{\"typeId\":\"java.lang.String\",\"value\":\"42.0\"}");
        Object result = deserializer.deserializeTypedFromAny(parser, new TestContext());
        assertEquals(Double.valueOf(42.0), result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("{\"typeId\":\"java.lang.Integer\"}");
        Object result = deserializer.deserializeWithNativeTypeId(parser, new TestContext());
        assertNotNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testWrongTokenException() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("{}");
        deserializer.deserialize(parser, new TestContext());
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingTypeId() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("{\"value\":42}");
        deserializer.deserialize(parser, new TestContext());
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingTypeIdAndValue() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("{\"value\":42}");
        deserializer.deserialize(parser, new TestContext());
    }

    @Test(expected = JsonProcessingException.class)
    public void testMissingEndObject() throws IOException {
        AsWrapperTypeDeserializer deserializer = getDeserializerUnderTest(Integer.class);

        JsonParser parser = createParser("{\"typeId\":\"java.lang.Integer\",\"value\":42}");
        parser.nextToken(); // Skip START_OBJECT
        deserializer.deserialize(parser, new TestContext());
    }
}

class TestContext extends DeserializationContext {
    @Override
    public Object instantiateValue(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }
}