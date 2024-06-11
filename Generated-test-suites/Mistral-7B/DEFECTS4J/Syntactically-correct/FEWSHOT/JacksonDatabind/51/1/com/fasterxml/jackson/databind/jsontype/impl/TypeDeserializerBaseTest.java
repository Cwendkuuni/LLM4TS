package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
                if (id.equals("MyClass")) {
                    return ctxt.getTypeFactory().constructType(MyClass.class);
                }
                return null;
            }
        };

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, idResolver, "", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("MyClass")) {
                    return new MyClassDeserializer();
                }
                return super._findDeserializer(ctxt, typeId);
            }
        };

        assertEquals(MyClass.class, typeDeserializer._findDeserializer(ctxt, "MyClass").getDeserializedClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return new MyClassDeserializer();
            }
        };

        assertNotNull(typeDeserializer._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc)
                    throws IOException {
                assertEquals("Unexpected type id: ", typeId, "UnknownTypeId");
                return ctxt.getTypeFactory().constructType(UnknownTypeIdClass.class);
            }
        };

        assertEquals(UnknownTypeIdClass.class, typeDeserializer._handleUnknownTypeId(ctxt, "UnknownTypeId", null, "").getRawClass());
    }

    static class MyClassDeserializer extends StdDeserializer<MyClass> {
        public MyClassDeserializer() {
            this(null);
        }

        public MyClassDeserializer(Class<?> vc) {
            super(MyClass.class, vc);
        }

        @Override
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyClass();
        }
    }

    static class MyClass {
    }

    static class UnknownTypeIdClass {
    }
}