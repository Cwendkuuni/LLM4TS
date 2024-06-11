
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() {
        typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        DeserializationContext ctxt = null;
        Map<String,JsonDeserializer<Object>> deserializers = new HashMap<>();
        JsonDeserializer<Object> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws com.fasterxml.jackson.core.JsonProcessingException {
                return null;
            }
        };
        deserializers.put("testTypeId", deser);
        typeDeserializer._deserializers.putAll(deserializers);
        assertNotNull(typeDeserializer._findDeserializer(ctxt, "testTypeId"));
        assertEquals(deser, typeDeserializer._findDeserializer(ctxt, "testTypeId"));
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        DeserializationContext ctxt = null;
        Map<String,JsonDeserializer<Object>> deserializers = new HashMap<>();
        JsonDeserializer<Object> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws com.fasterxml.jackson.core.JsonProcessingException {
                return null;
            }
        };
        deserializers.put("testTypeId", deser);
        typeDeserializer._deserializers.putAll(deserializers);
        assertNull(typeDeserializer._findDeserializer(ctxt, null));
    }

    @Test
    public void testFindDeserializerWithInvalidTypeId() throws Exception {
        DeserializationContext ctxt = null;
        assertNull(typeDeserializer._findDeserializer(ctxt, "invalidTypeId"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws Exception {
        typeDeserializer._deserializeWithNativeTypeId(null, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdWithInvalidTypeId() throws Exception {
        typeDeserializer._deserializeWithNativeTypeId(null, null, "invalidTypeId");
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = null;
        assertNull(typeDeserializer._handleUnknownTypeId(ctxt, "invalidTypeId", null, null));
    }
}
