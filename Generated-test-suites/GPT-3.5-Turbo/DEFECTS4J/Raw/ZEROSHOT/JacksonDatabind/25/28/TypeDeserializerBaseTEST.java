package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void test_baseTypeName() {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class);
        assertEquals(deserializer.baseTypeName(), Object.class.getName());
    }

    @Test
    public void test_getPropertyName() {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class);
        assertEquals(deserializer.getPropertyName(), "type");
    }

    @Test
    public void test_getTypeIdResolver() {
        JavaType type = new JavaType();
        TypeIdResolverImpl idResolver = new TypeIdResolverImpl();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, idResolver,
                "type", true, Object.class);
        assertEquals(deserializer.getTypeIdResolver(), idResolver);
    }

    @Test
    public void test_getDefaultImpl() {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class);
        assertEquals(deserializer.getDefaultImpl(), Object.class);

        deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, null);
        assertNull(deserializer.getDefaultImpl());
    }

    @Test
    public void test_toString() {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class);
        assertEquals(deserializer.toString(), "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl]");
    }

    @Test
    public void test_findDeserializer() throws IOException {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class) {
            @Override
            public BeanProperty forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JsonParser jp = new JsonParser();

        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        deserializer._deserializers.put("test", new TestJsonDeserializer());
        deserializer._deserializers.put("null", new NullifyingDeserializer(type));
        deserializer._deserializers.put("default", null);

        JsonDeserializer<Object> deser = deserializer._findDeserializer(new DeserializationContext(), "test");
        assertTrue(deser instanceof TestJsonDeserializer);

        deser = deserializer._findDeserializer(new DeserializationContext(), "null");
        assertTrue(deser instanceof NullifyingDeserializer);

        deser = deserializer._findDeserializer(new DeserializationContext(), "default");
        assertNull(deser);
    }

    @Test(expected = IOException.class)
    public void test_findDeserializerUnknownTypeId() throws IOException {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class) {
            @Override
            public BeanProperty forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
                    throws IOException {
                throw new IOException("Unknown type id: " + typeId);
            }
        };

        deserializer._findDeserializer(new DeserializationContext(), "unknown");
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws IOException {
        JavaType type = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(type, new TypeIdResolverImpl(),
                "type", true, Object.class) {
            @Override
            public BeanProperty forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return new NullifyingDeserializer(type);
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                if (typeId.equals("test")) {
                    return new TestJsonDeserializer();
                }
                return null;
            }
        };

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        deserializer._findDeserializer(ctxt, "test");
        Object obj = deserializer._deserializeWithNativeTypeId(jp, ctxt, "test");
        assertTrue(obj instanceof TestJsonDeserializer);

        jp = new JsonParser();
        obj = deserializer._deserializeWithNativeTypeId(jp, ctxt, "default");
        assertNull(obj);
    }

    private static class JavaType {

        public Class<?> getRawClass() {
            return Object.class;
        }
    }

    private static class TypeIdResolverImpl implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {

        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
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

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }

    private static class TestJsonDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}