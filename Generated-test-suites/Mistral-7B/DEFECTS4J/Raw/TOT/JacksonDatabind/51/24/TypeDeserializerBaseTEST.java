package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseType() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testTypeIdResolver() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), new ClassTypeIdResolver(), null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, String.class.getTypeName()) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
        assertEquals("java.lang.String", deserializer.getDefaultImpl().getName());
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver:null]", deserializer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBeanProperty() throws IOException {
        new TypeDeserializerBase(Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
    }

    @Test(expected = IOException.class)
    public void testMissingDeserializer() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        deserializer.findDeserializer(null, "com.test.TestClass");
    }

    @Test(expected = IOException.class)
    public void testUnknownTypeId() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                throw new IOException("Should not be called");
            }

            @Override
            public JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                throw new IOException("Should not be called");
            }
        };
        deserializer.findDeserializer(null, "com.test.TestClass");
    }

    @Test
    public void testFindDeserializerEmptyMap() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        JsonDeserializer<Object> deserializerForTypeId = deserializer._findDeserializer(null, "com.test.TestClass");
        assertNull(deserializerForTypeId);
    }

    @Test
    public void testFindDeserializerWithDeserializer() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>();

            @Override
            public JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return new StubJsonDeserializer();
            }

            @Override
            public JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
        assertEquals(StubJsonDeserializer.class, deserializer._deserializers.get("com.test.TestClass").getClass());
    }

    @Test
    public void testFindDefaultImplDeserializerEmptyMap() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public synchronized JsonDeserializer<Object> _defaultImplDeserializer = new NullifyingDeserializer.Instance();

            @Override
            public JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        assertSame(NullifyingDeserializer.Instance.instance, deserializer._defaultImplDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDeserializer() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            public synchronized JsonDeserializer<Object> _defaultImplDeserializer = new StubJsonDeserializer();

            @Override
            public JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return new StubJsonDeserializer();
            }
        };
        assertSame(StubJsonDeserializer.class, deserializer._defaultImplDeserializer.getClass());
    }

    private static class StubJsonDeserializer extends JsonDeserializer<Object> {
        public StubJsonDeserializer() {
            super(Object.class);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class ClassTypeIdResolver extends TypeIdResolverBase {
        @Override
        public String idFromValue(Object value) {
            return value.getClass().getName();
        }
    }
}