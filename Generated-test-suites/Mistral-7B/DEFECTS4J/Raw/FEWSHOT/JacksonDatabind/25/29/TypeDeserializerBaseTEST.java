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
    public void testHandleUnknownTypeId() throws IOException {
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                fail("Should not call typeFromId");
                return null;
            }
        };
        JavaType baseType = new Jdk8ClassType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return NullifyingDeserializer.instance;
            }
        };
        DeserializationContext context = new TestDeserializationContext();
        String typeId = "UnknownTypeId";
        assertNull(typeDeserializerBase._findDeserializer(context, typeId));
        try {
            typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, baseType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        JavaType baseType = new Jdk8ClassType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, Void.class) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return new StdDeserializer<>(String.class);
            }
        };
        DeserializationContext context = new TestDeserializationContext();
        assertEquals(String.class, typeDeserializerBase._findDefaultImplDeserializer(context).getClass());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            private final Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();

            @Override
            public JsonDeserializer<Object> deserializerForId(DeserializationContext ctxt, String typeId) throws IOException {
                return deserializers.get(typeId);
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return new Jdk8ClassType(String.class);
            }

            @Override
            public void init(DeserializationConfig config) throws Exception {
                deserializers.put("StringDeserializer", new StdDeserializer<>(String.class));
            }
        };
        JavaType baseType = new Jdk8ClassType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        String typeId = "StringDeserializer";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(String.class, deserializer.getClass());
    }

    @Test
    public void testForProperty() throws IOException {
        TypeIdResolver idResolver = new TypeIdResolverBase();
        JavaType baseType = new Jdk8ClassType(String.class);
        BeanProperty property = new BeanProperty("property", String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "property", false, null);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(String.class, typeDeserializer.getType());
    }

    @Test
    public void testToString() {
        JavaType baseType = new Jdk8ClassType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        String expected = "[TypeDeserializerBase; base-type:class java.lang.String; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
}

class TestDeserializationContext extends DeserializationContext {
    public TestDeserializationContext() { super(new TestObjectMapper()); }
}

class StdDeserializer<T> extends JsonDeserializer<T> {
    public StdDeserializer(Class<T> t) { super(t); }
}