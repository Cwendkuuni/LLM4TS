package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static final String TYPE_ID_STRING = "typeId";

    private DeserializationContext context;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() throws JacksonException {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
        typeDeserializerBase = new TypeDeserializerBase(JsonType.construct(Object.class, null), context.getTypeIdResolver(), "", false, null);
    }

    @Test
    public void testDefaultImpl() throws IOException {
        // Test with default implementation
        String json = "{}";
        Object result = typeDeserializerBase.deserialize(new JsonParser(new ByteArrayInputStream(json.getBytes())), context);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        // Test finding deserializer with known type id
        JsonDeserializer<String> stringDeserializer = context.findValueDeserializer(String.class, context.getTypeFactory().constructType(String.class));
        typeDeserializerBase._deserializers.put(TYPE_ID_STRING, stringDeserializer);

        String json = "{\"typeId\":\"" + TYPE_ID_STRING + "\"}";
        Object result = typeDeserializerBase.deserialize(new JsonParser(new ByteArrayInputStream(json.getBytes())), context);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Test finding default implementation deserializer
        JsonDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
        typeDeserializerBase._defaultImplDeserializer = nullifyingDeserializer;

        String json = "{}";
        Object result = typeDeserializerBase.deserialize(new JsonParser(new ByteArrayInputStream(json.getBytes())), context);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        // Test handling unknown type id
        String json = "{\"typeId\":\"unknownTypeId\"}";
        Object result = typeDeserializerBase.deserialize(new JsonParser(new ByteArrayInputStream(json.getBytes())), context);
        fail("Expected IOException but got " + result.getClass().getName());
    }

    @Test
    public void testToString() throws IOException {
        String json = "{}";
        String result = typeDeserializerBase.toString();
        String expected = "[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@5629f021]";
        assertEquals(expected, result);
    }
}