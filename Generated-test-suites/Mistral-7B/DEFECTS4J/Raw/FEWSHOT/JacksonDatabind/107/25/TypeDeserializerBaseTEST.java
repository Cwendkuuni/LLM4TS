package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().toString(), null, null, false, null) {
            @Override
            protected JavaType handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                return ctxt.constructType(String.class);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TypeDeserializer deserializer = deserializerBase.forProperty(null);
        Object result = deserializer.deserialize(jp, ctxt);
        assertEquals(java.util.ArrayList.class, result.getClass());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().toString(), null, null, false, null) {
            @Override
            protected JavaType handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                throw new IOException("Missing type id");
            }

            @Override
            protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                if ("String".equals(typeId)) {
                    return new StringDeserializer();
                }
                return null;
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"foo\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TypeDeserializer deserializer = deserializerBase.forProperty(null);
        Object result = deserializer.deserialize(jp, ctxt);
        assertEquals("foo", result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().toString(), null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return new StringDeserializer();
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[null]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TypeDeserializer deserializer = deserializerBase.forProperty(null);
        Object result = deserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().toString(), null, null, false, null) {
            @Deprecated
            @Override
            protected Object deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
                    throws IOException {
                return ctxt.readValueAs(String.class);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"foo\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        ctxt.enable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);

        TypeDeserializer deserializer = deserializerBase.forProperty(null);
        Object result = deserializer.deserialize(jp, ctxt);
        assertEquals("foo", result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().toString(), null, null, false, null) {
            @Override
            protected JavaType handleUnknownTypeId(DeserializationContext ctxt, String typeId)
                    throws IOException {
                throw new IOException("Unknown type id: " + typeId);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[1]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TypeDeserializer deserializer = deserializerBase.forProperty(null);
        try {
            Object result = deserializer.deserialize(jp, ctxt);
            fail();
        } catch (IOException e) {
            assertEquals("Unknown type id: 1", e.getMessage());
        }
    }

    private static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return p.getValueAsString();
        }
    }
}