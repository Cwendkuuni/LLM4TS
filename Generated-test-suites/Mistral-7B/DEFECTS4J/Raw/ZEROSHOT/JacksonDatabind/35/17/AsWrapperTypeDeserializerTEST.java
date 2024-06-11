package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AsWrapperTypeDeserializer.
 */
public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    private AsWrapperTypeDeserializer deserializer;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        deserializer = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class, String.class,
                new MyTypeIdResolver(), "typePropertyName", true, String.class);
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"typeId\":\"java.lang.String\", \"value\":\"Hello World\"}";
        Object actual = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("Hello World", actual);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"typeId\":\"java.lang.String\", \"value\":\"Hello World\"}]";
        Object actual = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("Hello World", actual);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "{\"typeId\":\"java.lang.String\", \"value\":42}";
        Object actual = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        fail("Should throw java.lang.IllegalArgumentException for non-string typeId");
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "[{\"typeId\":\"java.lang.String\", \"value\":42}, {\"typeId\":\"java.lang.Integer\", \"value\":42}]";
        Object actual = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        fail("Should throw java.lang.IllegalArgumentException for non-string typeId");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"java.lang.String\", \"value\":\"Hello World\"}";
        Object actual = deserializer.deserializeWithNativeTypeId(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("Hello World", actual);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonObject() throws Exception {
        String json = "{\"typeId\":\"java.lang.String\", \"value\":\"Hello World\", \"invalidField\":\"Value\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws Exception {
        String json = "{\"value\":\"Hello World\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeIdInArray() throws Exception {
        String json = "[{\"value\":\"Hello World\"}]";
        deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeIdInAny() throws Exception {
        String json = "[{\"value\":\"Hello World\"}, {\"value\":\"Hello World2\"}]";
        deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeId() throws Exception {
        String json = "{\"typeId\":\"java.lang.InvalidTypeId\", \"value\":\"Hello World\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(String.class, mapper.getTypeFactory().constructType(Map.class));
        assertEquals(StringDeserializer.instance, deserializer);

        // Test that AsWrapperTypeDeserializer is used when typeId is present
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(
                mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class, String.class, new MyTypeIdResolver(), "typePropertyName", true, String.class), null);
        assertEquals(deserializer, stringDeserializer);

        // Test that default deserializer is used when no typeId is present
        JsonDeserializer<Object> defaultDeserializer = mapper.findValueDeserializer(Object.class, null);
        assertNotNull(defaultDeserializer);
    }

    private static class MyTypeIdResolver extends StdTypeIdResolver {
        @Override
        public TypeId idFromValueAndType(Class<?> clz, Object value) {
            return null;
        }

        @Override
        public String idFromValueAndTypeSerializationParams(Class<?> clz, Object value, JsonSerializer serializers) throws SerializationException {
            return null;
        }
    }
}