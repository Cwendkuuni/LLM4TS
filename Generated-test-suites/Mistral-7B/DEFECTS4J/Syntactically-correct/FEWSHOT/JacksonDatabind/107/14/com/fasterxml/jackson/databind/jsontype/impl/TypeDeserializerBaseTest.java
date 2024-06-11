package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        assertNull(typeDeserializerBase._findDeserializer(null, null));
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        assertNull(typeDeserializerBase._findDeserializer(null, ""));
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer<>();
        typeDeserializerBase._deserializers.put("test", deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(null, "test");
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, new JavaClass(_class1.class));
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer<>();
        typeDeserializerBase._defaultImplDeserializer = deserializer;

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(null);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(null, "test");
    }

    @Test
    public void testHandleMissingTypeIdWithNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        typeDeserializerBase._handleMissingTypeId(null, "test");
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithContextAndEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        typeDeserializerBase._handleUnknownTypeId(context, "");
    }

    @Test
    public void testHandleUnknownTypeIdWithContextAndValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        typeDeserializerBase._handleUnknownTypeId(context, "test");
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeIdWithContextAndEmptyExtraDesc() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        typeDeserializerBase._handleMissingTypeId(context, "");
    }

    @Test
    public void testHandleMissingTypeIdWithContextAndValidExtraDesc() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                null, null, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        typeDeserializerBase._handleMissingTypeId(context, "test: extra description");
    }

    static class StubJsonDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return (T) new Object();
        }
    }

    static class TestDeserializationContext
            extends DeserializationContextBase {
        @Override
        public JsonFactory _rootValueFactory() {
            return new TestJsonFactory();
        }

        static class TestJsonFactory extends JsonFactory {
            @Override
            public Object parseValue(String str, Class<?> valueType)
                    throws IOException {
                return new Object();
            }
        }
    }

    static class _class1 {}
}