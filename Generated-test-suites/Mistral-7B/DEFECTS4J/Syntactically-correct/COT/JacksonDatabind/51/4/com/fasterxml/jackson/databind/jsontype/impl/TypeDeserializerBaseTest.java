package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest extends TestBase {

    private TypeDeserializerBase testee;
    private ObjectMapper mapper;
    private TypeIdResolver idResolver;
    private DeserializationContext ctxt;
    private JsonParser parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mapper = new ObjectMapper();
        idResolver = mapper.getTypeIdResolver();
        ctxt = mapper.getDeserializationContext();
        testee = new TypeDeserializerBase(null, idResolver, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }
        };
        parser = mapper.getFactory().createParser("{\"typeId\":\"TestClass\"}");
    }

    @org.junit.Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty(getClass(), "testee", TypeDeserializerBase.class);
        TypeDeserializer forProperty = testee.forProperty(property);
        assertNotNull(forProperty);
    }

    @org.junit.Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As inclusion = testee.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, inclusion);
    }

    @org.junit.Test
    public void testBaseTypeName() throws Exception {
        assertEquals(TypeDeserializerBase.class.getName(), testee.baseTypeName());
    }

    @org.junit.Test
    public void testPropertyName() throws Exception {
        assertEquals("", testee.getPropertyName());
    }

    @org.junit.Test
    public void testgetTypeIdResolver() throws Exception {
        assertSame(idResolver, testee.getTypeIdResolver());
    }

    @org.junit.Test
    public void testDefaultImpl() throws Exception {
        assertEquals(null, testee.getDefaultImpl());
    }

    @org.junit.Test
    public void testToString() throws Exception {
        String expected = "[TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver=class com.fasterxml.jackson.databind.JsonTypeResolverBase]";
        assertEquals(expected, testee.toString());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() throws Exception {
        testee.forProperty(null);
    }

    @org.junit.Test
    public void testFindDeserializerNullTypeId() throws Exception {
        assertNull(testee._findDeserializer(ctxt, null));
    }

    @org.junit.Test
    public void testFindDeserializerTypeIdNotFound() throws Exception {
        String typeId = "NonExistentType";
        assertNull(testee._findDeserializer(ctxt, typeId));
    }

    @org.junit.Test
    public void testFindDeserializerRegisteredTypeId() throws Exception {
        String typeId = "TestClass";
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        testee._deserializers.put(typeId, deserializer);
        JsonDeserializer<Object> foundDeserializer = testee._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, foundDeserializer);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws Exception {
        assertNull(testee._findDefaultImplDeserializer(ctxt));
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerRegisteredDefaultImpl() throws Exception {
        Class<Object> defaultImplType = Object.class;
        testee._defaultImpl = mapper.getTypeFactory().constructType(defaultImplType);
        JsonDeserializer<Object> expectedDeserializer = NullifyingDeserializer.instance;
        JsonDeserializer<Object> foundDeserializer = testee._findDefaultImplDeserializer(ctxt);
        assertEquals(expectedDeserializer, foundDeserializer);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
        assertNull(testee._deserializeWithNativeTypeId(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
        String unknownTypeId = "UnknownTypeId";
        testee._idResolver = new StubTypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                assertEquals(unknownTypeId, typeId);
                return mapper.getTypeFactory().constructType(String.class);
            }
        };
        assertNull(testee._deserializeWithNativeTypeId(parser, ctxt));
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdRegisteredTypeId() throws Exception {
        String registeredTypeId = "TestClass";
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        testee._deserializers.put(registeredTypeId, deserializer);
        assertEquals(deserializer.getValueClass(), testee._deserializeWithNativeTypeId(parser, ctxt, registeredTypeId));
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        String unknownTypeId = "UnknownTypeId";
        testee._idResolver = new StubTypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                assertEquals(unknownTypeId, typeId);
                throw new IOException();
            }
        };
        testee._handleUnknownTypeId(ctxt, unknownTypeId, idResolver, null);
    }

    static class StubJsonDeserializer extends NullifyingDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public Class<?> getValueClass() {
            return Object.class;
        }
    }

    static class StubTypeIdResolver extends TypeIdResolverBase {
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        public String getDescForKnownTypeIds() {
            return "";
        }
    }
}