package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = new TestDeserializationContext();
        assertNull(typeDeserializerBase._findDeserializer(ctxt, null));
    }

    @Test
    public void testFindDeserializerKnownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, String.class);
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "java.lang.String";
        JsonDeserializer<Object> deserializer = new StringDeserializer();
        typeDeserializerBase._deserializers.put(typeId, deserializer);
        assertEquals(deserializer, typeDeserializerBase._findDeserializer(ctxt, typeId));
    }

    @Test
    public void testFindDeserializerUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "com.example.MyClass";
        assertNull(typeDeserializerBase._findDeserializer(ctxt, typeId));
    }

    @Test
    public void testFindDefaultImplDeserializerNullDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = new TestDeserializationContext();
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testFindDefaultImplDeserializerKnownDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, Integer.class);
        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = new IntegerDeserializer();
        synchronized (typeDeserializerBase) {
            typeDeserializerBase._defaultImplDeserializer = deserializer;
        }
        assertEquals(deserializer, typeDeserializerBase._findDefaultImplDeserializer(ctxt));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "com.example.MyClass";
        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, null);
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomMessage() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                throw new JsonMappingException("Custom message");
            }
        };
        DeserializationContext ctxt = new TestDeserializationContext();
        String typeId = "com.example.MyClass";
        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, null);
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        assertEquals("[TypeDeserializerBase; base-type:null; id-resolver:null]", typeDeserializerBase.toString());

        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(null, null, null, false, String.class);
        assertEquals("[TypeDeserializerBase; base-type:class java.lang.String; id-resolver:null]", typeDeserializerBase2.toString());

        TypeDeserializerBase typeDeserializerBase3 = new TypeDeserializerBase(null, null, null, false, null);
        TypeDeserializerBase typeDeserializerBase4 = new TypeDeserializerBase(typeDeserializerBase3, null);
        assertEquals("[TypeDeserializerBase; base-type:[TypeDeserializerBase; base-type:null; id-resolver:null]; id-resolver:null]", typeDeserializerBase4.toString());
    }

    private static class TestDeserializationContext extends DeserializationContextBase {
        TestDeserializationContext() {
            super(new JsonFactory(), new ObjectMapper());
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public JavaType resolveType(Class<?> type, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }
    }

    private static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<? extends String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    private static class IntegerDeserializer extends StdDeserializer<Integer> {
        public IntegerDeserializer() {
            this(null);
        }

        public IntegerDeserializer(Class<? extends Integer> vc) {
            super(vc);
        }

        @Override
        public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsInt();
        }
    }
}