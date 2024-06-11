package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithNullId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, null);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithEmptyId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When
        String typeId = "";
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithValidId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        String typeId = "TestType";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithUnknownId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        String typeId = "UnknownType";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNull(deserializer);

        // And when using default implementation
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializer.findDefaultImplDeserializer(context);
        Object result = defaultImplDeserializer.deserialize(null, context);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolution() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        String typeId = "com.example.TestType";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolutionUsingNullProperties() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, null);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolutionUsingEmptyProperties() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        String typeId = "";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolutionUsingUnknownType() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        String typeId = "UnknownType";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolutionUsingTypeIdResolver() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeIdResolver typeIdResolver = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, typeIdResolver, null, null, null);
        String typeId = "TestType";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolutionUsingNullTypeIdResolver() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, null);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolutionUsingEmptyTypeIdResolver() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeIdResolver typeIdResolver = new TestTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, typeIdResolver, null, null, null);
        String typeId = "";

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDeserializer(context, typeId);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        String typeId = "UnknownType";

        // When
        JavaType result = typeDeserializer.handleUnknownTypeId(context, typeId);

        // Then
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When
        JavaType result = typeDeserializer.handleMissingTypeId(context, "");

        // Then
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When
        JsonDeserializer<Object> deserializer = typeDeserializer.findDefaultImplDeserializer(context);

        // Then
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws Exception {
        // Given
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);

        // When and Then
        assertNull(typeDeserializer.findDefaultImplDeserializer(null));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        JsonParser jp = new TestingJsonParser(new StringReader("{\"typeId\": \"TestType\"}"));

        // When
        Object result = typeDeserializer.deserializeWithNativeTypeId(jp, context);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNullContext() throws Exception {
        // Given
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        JsonParser jp = new TestingJsonParser(new StringReader("{\"typeId\": \"TestType\"}"));

        // When and Then
        assertNull(typeDeserializer.deserializeWithNativeTypeId(jp, null));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithEmptyTypeId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        JsonParser jp = new TestingJsonParser(new StringReader("{\"typeId\": \"\"}"));

        // When
        Object result = typeDeserializer.deserializeWithNativeTypeId(jp, context);

        // Then
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws Exception {
        // Given
        DeserializationContext context = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, null, null, null);
        JsonParser jp = new TestingJsonParser(new StringReader("{\"typeId\": null}"));

        // When
        Object result = typeDeserializer.deserializeWithNativeTypeId(jp, context);

        // Then
        assertNull(result);
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase
    {
        public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        public TestTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestTypeIdResolver implements TypeIdResolver
    {
        @Override
        public String idFromValueAndType(Object value, Class<?> valueType) {
            return null;
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                throws IOException {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
    }

    private static class TestingDeserializationContext extends DeserializationContext {
        public TestingDeserializationContext() {
            super(null, null, null);
        }

        @Override
        public DeserializationFeature getDeserializationFeature(DeserializationFeature feature) {
            return null;
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(JavaType type, BeanProperty property)
                throws IOException {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType) throws IOException {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType, String propertyName) throws IOException {
            return null;
        }

        @Override
        public Object findRootValueDeserializer() throws IOException {
            return null;
        }
    }

    private static class TestingJsonParser extends JsonParser {
        public TestingJsonParser(Reader reader) {
            super(new TestingObjectCodec(), reader);
        }
    }
}