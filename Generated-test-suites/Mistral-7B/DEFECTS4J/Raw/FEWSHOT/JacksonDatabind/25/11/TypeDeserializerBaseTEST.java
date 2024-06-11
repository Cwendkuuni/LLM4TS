package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetBaseType() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
        String expected = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }

            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>(16, 0.75f, 4);

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                JsonDeserializer<Object> deser = super._findDeserializer(ctxt, typeId);
                if (deser == null && "String".equals(typeId)) {
                    deser = new StringDeserializer();
                }
                return deser;
            }
        };

        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, "String");
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StringDeserializer);

        deserializer = typeDeserializerBase._findDeserializer(context, "Int");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }

            @Override
            protected synchronized JsonDeserializer<Object> _defaultImplDeserializer;

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                if (_defaultImplDeserializer == null) {
                    _defaultImplDeserializer = new StringDeserializer();
                }
                return _defaultImplDeserializer;
            }
        };

        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StringDeserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName().equals("java.lang.String") ? String.class : Object.class,
                null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                throw new RuntimeException("Unexpected call to _handleUnknownTypeId");
            }
        };

        DeserializationContext context = new TestDeserializationContext();
        try {
            typeDeserializerBase._handleUnknownTypeId(context, "String", null, null);
            fail();
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            // Expected
        }

        try {
            typeDeserializerBase._handleUnknownTypeId(context, "Int", null, null);
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(String.class, vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class TestDeserializationContext extends DeserializationContextBase {
        @Override
        public Object handleUnknownProperty(String name, JsonParser p) throws IOException {
            return super.handleUnknownProperty(name, p);
        }

        @Override
        public Object handleUnknownValue(JsonValue value, DeserializationContext ctxt) throws IOException {
            return super.handleUnknownValue(value, ctxt);
        }

        @Override
        public Object deserializeWithTypeId(JsonParser p, DeserializationContext ctxt, JavaType type)
                throws IOException {
            return super.deserializeWithTypeId(p, ctxt, type);
        }

        @Override
        public Object deserializeFromObject(Object value, DeserializationContext ctxt) throws IOException {
            return super.deserializeFromObject(value, ctxt);
        }

        @Override
        public JavaType findRootValueType(Parser p) throws IOException {
            return null;
        }

        @Override
        public JavaType findContextualType(DeserializationContext ctxt, BeanProperty property)
                throws IOException {
            return null;
        }

        @Override
        public void checkForExtension(JsonParser p, JavaType type, JsonDeserializer<?> deserializer)
                throws IOException {
            // Empty
        }

        @Override
        public void afterDeserialization(Object bean, JsonParser p) throws IOException {
            // Empty
        }

        @Override
        public void beforeDeserialization(DeserializationContext ctxt) throws IOException {
            // Empty
        }

        @Override
        public void configure(DeserializationFeature feature) {
            // Empty
        }

        @Override
        public void configure(SerializerFeature feature) {
            // Empty
        }

        @Override
        public void configure(AnnotationFeature feature) {
            // Empty
        }

        @Override
        public void configure(JsonVersion version) {
            // Empty
        }

        @Override
        public void configure(FeatureName featureName, boolean value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, Object value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, String value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, int value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, long value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, float value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, double value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, boolean value) {
            // Empty
        }

        @Override
        public void configure(Feature feature, String[] values) {
            // Empty
        }
    }
}