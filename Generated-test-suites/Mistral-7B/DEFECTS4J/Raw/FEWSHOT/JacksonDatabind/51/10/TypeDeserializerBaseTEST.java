package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(new TestContext(), "StringType");
        assertNull(deserializer);

        JavaType stringType = TypeFactory.defaultInstance().constructType(StringType.class);
        deserializer = typeDeserializer._findDeserializer(new TestContext(), stringType.getId().getId());
        assertEquals(StringTypeDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(new TestContext());
        assertNull(deserializer);

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        deserializer = typeDeserializer._findDefaultImplDeserializer(new TestContext());
        assertEquals(StringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        assertNull(typeDeserializer._handleUnknownTypeId(new TestContext(), "UnknownTypeId", idResolver, baseType));
    }

    static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new TestParser(), null, null, null, null);
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {
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

    static class StringTypeDeserializer extends JsonDeserializer<StringType> {
        @Override
        public StringType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new StringType();
        }
    }

    static class StringType {
    }
}