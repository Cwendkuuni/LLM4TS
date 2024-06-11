package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void test_findDeserializer_whenIdIsKnownAndRegistered() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idRes = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "knownTypeId");
        assertNotNull(deserializer);
        assertEquals(TestKnownDeserializer.class, deserializer.getClass());
    }

    @Test
    public void test_findDeserializer_whenIdIsKnownButNotRegistered() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idRes = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "unknownTypeId");
        assertNull(deserializer);
    }

    @Test
    public void test_findDeserializer_whenIdIsNotFound() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idRes = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void test_findDeserializer_whenDefaultImplIsSet() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idRes = new TestTypeIdResolver();
        JavaType defaultImpl = getClass().getDeclaredField("testDefaultImpl").get(null);
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, defaultImpl);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, null);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void test_findDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, "typePropertyName", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void test_deserializeWithNativeTypeId_whenIdIsKnown() throws Exception {
        DeserializationContext ctxt = new TestContext();
        JsonParser jp = createMockParser(ctxt, "{\"typeId\":\"knownTypeId\"}");
        TypeIdResolver idRes = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, null);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertNotNull(result);
        assertEquals("result", result);
    }

    @Test
    public void test_deserializeWithNativeTypeId_whenIdIsNotFound() throws Exception {
        DeserializationContext ctxt = new TestContext();
        JsonParser jp = createMockParser(ctxt, "{\"typeId\":\"unknownTypeId\"}");
        TypeIdResolver idRes = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, null);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void test_handleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeIdResolver idRes = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, idRes, "typePropertyName", false, null);
        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, "unknownTypeId");
        assertNotNull(result);
        assertEquals(UnknownTypeIdHandling.class, result.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void test_handleMissingTypeId() throws Exception {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, "typePropertyName", false, null);
        typeDeserializer._handleMissingTypeId(ctxt, "missingTypeId");
    }

    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(new TestAnnotationIntrospector(), new TestObjectMapper.TestFactory());
            resetAll();
        }

        @Override
        public boolean canOverrideInput(JsonParser p, DeserializationFeature feature) {
            return true;
        }
    }

    private static class TestTypeIdResolver extends TypeIdResolverBase {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            if (typeId.equals("knownTypeId")) {
                return getClass().getDeclaredField("testKnownType").get(null);
            } else if (typeId.equals("unknownTypeId")) {
                return ctxt.handleMissingTypeId(null, null);
            } else {
                throw new IOException("Type id not recognized: " + typeId);
            }
        }
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializerBase(baseType, idResolver, prop, false, defaultImpl);
        }

        static class TestKnownDeserializer extends JsonDeserializer<Object> {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "result";
            }
        }

        static class UnknownTypeIdHandling extends BaseTypeDeserializerMinimalBase<Object> {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        }

        static class TestDefaultImpl {
            static Object testDefaultImpl;
        }
    }

    private static JsonParser createMockParser(DeserializationContext ctxt, String contentInStringForm) throws IOException {
        ObjectReader reader = new TestObjectMapper().readerForElement(null);
        return reader.readValueAs(JsonParser.class, new StringReader(contentInStringForm));
    }
}