package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "propertyName", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("propertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = new TypeIdResolver() {
            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                return null;
            }

            @Override
            public String idFromBaseType() {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
        };

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolver, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithType() throws IOException {
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, "typeId");
    }

    @Test(expected = IOException.class) public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext deserializationContext = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        typeDeserializerBase._handleUnknownTypeId(deserializationContext, "typeId",
                new TypeIdResolver() {
                    @Override
                    public String idFromValue(Object value) {
                        return null;
                    }

                    @Override
                    public String idFromValueAndType(Object value, Class<?> suggestedType) {
                        return null;
                    }

                    @Override
                    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                        return null;
                    }

                    @Override
                    public String idFromBaseType() {
                        return null;
                    }

                    @Override
                    public String getDescForKnownTypeIds() {
                        return null;
                    }
                },
                null);
    }
}