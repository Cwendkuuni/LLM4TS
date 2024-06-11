package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(JavaType.constructType(String.class), new ClassTypeIdResolver(), "", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "StringType");
        assertNotNull(deserializer);
        assertEquals(StringDeserializer.class, deserializer.getClass());

        deserializer = typeDeserializerBase._findDeserializer(ctxt, "UnknownTypeId");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(JavaType.constructType(String.class), new ClassTypeIdResolver(), "", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);

        typeDeserializerBase = new BaseTypeDeserializer(JavaType.constructType(Void.class), new ClassTypeIdResolver(), "", false, null);
        deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(JavaType.constructType(String.class), new ClassTypeIdResolver(), "", false, null);
        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(ctxt, "UnknownTypeId", new ClassTypeIdResolver(), null);
        assertNull(javaType);

        ctxt.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        javaType = typeDeserializerBase._handleUnknownTypeId(ctxt, "UnknownTypeId", new ClassTypeIdResolver(), null);
        assertNull(javaType);

        TypeIdResolver customTypeIdResolver = new CustomTypeIdResolver();
        javaType = typeDeserializerBase._handleUnknownTypeId(ctxt, "UnknownTypeId", customTypeIdResolver, null);
        assertNull(javaType);

        JavaType stringType = JavaType.constructType(String.class);
        javaType = typeDeserializerBase._handleUnknownTypeId(ctxt, "StringType", customTypeIdResolver, stringType);
        assertEquals(stringType, javaType);
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase {
        public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return super._findDeserializer(ctxt, typeId);
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return super._findDefaultImplDeserializer(ctxt);
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc)
                throws IOException {
            return super._handleUnknownTypeId(ctxt, typeId, idResolver, null);
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

    static class CustomTypeIdResolver extends ClassTypeIdResolverBase {
        @Override
        public String getIdForValueAndType(Object value, Class<?> valueType) {
            return value.getClass().getName();
        }
    }
}