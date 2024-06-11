
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.getTypeInclusion();
    }

    @Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.baseTypeName();
    }

    @Test
    public void testGetPropertyName() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.getPropertyName();
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.getTypeIdResolver();
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.getDefaultImpl();
    }

    @Test
    public void testBaseType() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.baseType();
    }

    @Test
    public void testToString() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializerBase.toString();
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        DeserializationContext ctx = null;
        String typeId = "";
        deserializerBase._findDeserializer(ctx, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        DeserializationContext ctx = null;
        deserializerBase._findDefaultImplDeserializer(ctx);
    }

    @Test
    public void testDeserializeWithNativeTypeId1() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        deserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test
    public void testDeserializeWithNativeTypeId2() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        deserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        DeserializationContext ctxt = null;
        String typeId = "";
        deserializerBase._handleUnknownTypeId(ctxt, typeId);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        DeserializationContext ctxt = null;
        String extraDesc = "";
        deserializerBase._handleMissingTypeId(ctxt, extraDesc);
    }
}
