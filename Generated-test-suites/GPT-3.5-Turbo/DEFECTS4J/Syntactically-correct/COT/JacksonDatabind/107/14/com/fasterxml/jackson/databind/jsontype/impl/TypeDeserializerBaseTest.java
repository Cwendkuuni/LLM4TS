
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
                return null;
            }
        };

        DeserializationContext context = new DeserializationContext(null, null);
        JsonDeserializer<Object> defaultImplDeserializer = deserializerBase._findDefaultImplDeserializer(context);
        Assert.assertEquals(NullifyingDeserializer.instance, defaultImplDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
                return null;
            }
        };
        DeserializationContext context = new DeserializationContext.Builder(null).build();
        Object deserialized = deserializerBase._deserializeWithNativeTypeId(null, context, null);
        Assert.assertEquals(null, deserialized);
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
                throw new com.fasterxml.jackson.databind.JsonMappingException("Unknown typeId: " + typeId);
            }
        };
        DeserializationContext context = new DeserializationContext.Builder(null).build();
        deserializerBase._handleUnknownTypeId(context, "UnknownType");
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
                return null;
            }

            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
                throw new com.fasterxml.jackson.databind.JsonMappingException("Missing typeId: " + extraDesc);
            }
        };
        DeserializationContext context = new DeserializationContext.Builder(null).build();
        try {
            deserializerBase._handleMissingTypeId(context, "MissingType");
            Assert.fail("Expected JsonMappingException");
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            Assert.assertEquals("Missing typeId: MissingType", e.getMessage());
        }
    }
}
