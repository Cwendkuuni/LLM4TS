package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tests for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() throws IOException {
        // Given
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);

        // When
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(null);

        // Then
        assertEquals(Foo.class, forProperty.getType());
        assertEquals(JsonTypeInfo.As.PROPERTY, forProperty.getTypeInclusion());
        assertEquals(Foo.class.getName(), typeDeserializerBase.baseTypeName());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(defaultImpl.getRawClass(), typeDeserializerBase.getDefaultImpl());
        assertEquals("TypeDeserializerBase[base-type:class Foo; id-resolver:StandardTypeIdResolver;]",
                typeDeserializerBase.toString());

        // When
        TypeDeserializerBase copy = new TypeDeserializerBase(typeDeserializerBase, new BeanProperty(""));

        // Then
        assertEquals(Foo.class, copy.getType());
        assertEquals(JsonTypeInfo.As.PROPERTY, copy.getTypeInclusion());
        assertEquals(Foo.class.getName(), copy.baseTypeName());
        assertEquals("", copy.getPropertyName());
        assertEquals(idResolver, copy.getTypeIdResolver());
        assertEquals(defaultImpl.getRawClass(), copy.getDefaultImpl());
        assertEquals("TypeDeserializerBase[base-type:class Foo; id-resolver:StandardTypeIdResolver;]",
                copy.toString());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper objectMapperWithTypeIdResolver = ObjectMapper.getDefaultInstance();
        objectMapperWithTypeIdResolver.setTypeIdResolver(idResolver);

        // When
        JsonParser jsonParser = objectMapperWithTypeIdResolver.getFactory()
                .createParser("{\"foo\": 1, \"type\": \"com.fasterxml.jackson.databind.jsontype.impl.Bar\"}");
        DeserializationContext ctxt = objectMapperWithTypeIdResolver.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "com.fasterxml.jackson.databind.jsontype.impl.Bar");

        // Then
        assertNotNull(deserializer);
        assertEquals(BarDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);

        // When
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        // Then
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullifyingDeserializer() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        ObjectMapper objectMapperWithFailOnInvalidSubtype = ObjectMapper.getDefaultInstance()
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Void.TYPE);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);

        // When
        DeserializationContext ctxt = objectMapperWithFailOnInvalidSubtype.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        // Then
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImplementation() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);

        // When
        ObjectMapper objectMapperWithContextualValueDeserializer = ObjectMapper.getDefaultInstance()
                .findContextualValueDeserializer(Bar.class, new BeanProperty(""));

        synchronized (defaultImpl) {
            typeDeserializerBase._defaultImplDeserializer = objectMapperWithContextualValueDeserializer;
        }

        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        // Then
        assertNotNull(deserializer);
        assertEquals(BarDeserializer.class, deserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper objectMapperWithTypeIdResolver = ObjectMapper.getDefaultInstance();
        objectMapperWithTypeIdResolver.setTypeIdResolver(idResolver);

        // When
        JsonParser jsonParser = objectMapperWithTypeIdResolver.getFactory()
                .createParser("{\"foo\": 1, \"type\": \"UnknownTypeId\"}");
        DeserializationContext ctxt = objectMapperWithTypeIdResolver.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "UnknownTypeId");

        // Then
        fail("Should throw an IOException.");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);

        // When
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, "UnknownTypeId", idResolver, baseType);

        // Then
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws IOException {
        // Given
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeIdResolver idResolver = new CustomTypeIdResolver();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Foo.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(Bar.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);

        // When
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, "CustomTypeId", idResolver, baseType);

        // Then
        assertNotNull(result);
        assertEquals(FooWithCustomTypeId.class, result.getRawClass());
    }

    private static class CustomTypeIdResolver extends StdTypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            if (value instanceof FooWithCustomTypeId) {
                return "CustomTypeId";
            }
            return null;
        }
    }

    private static class Foo {
    }

    private static class FooWithCustomTypeId extends Foo {
        private final String customTypeId;

        public FooWithCustomTypeId() {
            this.customTypeId = "CustomTypeId";
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
        public String getCustomTypeId() {
            return customTypeId;
        }
    }

    private static class BarDeserializer extends JsonDeserializer<Bar> {
    }

    private static class Bar {
    }
}