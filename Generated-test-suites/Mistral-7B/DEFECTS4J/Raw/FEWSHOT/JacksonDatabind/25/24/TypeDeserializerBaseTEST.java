package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testConstructorWithBaseTypeAndIdResolver() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idResolver, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertNull(typeDeserializerBase._property);
        assertNull(typeDeserializerBase._defaultImpl);
        assertNull(typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);
    }

    @Test
    public void testConstructorWithBaseTypeAndIdResolverAndProperty() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, property, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idResolver, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertEquals(property, typeDeserializerBase._property);
        assertNull(typeDeserializerBase._defaultImpl);
        assertNull(typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);
    }

    @Test
    public void testForProperty() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, property, typePropertyName, typeIdVisible, defaultImpl);

        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);

        assertSame(typeDeserializerBase, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, property, typePropertyName, typeIdVisible, defaultImpl);

        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();

        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, property, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, property, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, typePropertyName, typeIdVisible, defaultImpl);

        String expectedResult = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver:";
        assertEquals(expectedResult, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerWithNullContextAndNullTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers() {
                return deserializers;
            }
        };

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, null);

        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithNullContextAndValidTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();
        deserializers.put(typeId, deserializer);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers() {
                return deserializers;
            }
        };

        JsonDeserializer<Object> resultDeserializer = typeDeserializerBase._findDeserializer(null, typeId);

        assertEquals(deserializer, resultDeserializer);
    }

    @Test
    public void testFindDeserializerWithContextAndNullTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers() {
                return deserializers;
            }
        };

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);

        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithContextAndValidTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(ctxt.findContextualValueDeserializer(eq(baseType), any(BeanProperty.class))).thenReturn(deserializer);

        Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();
        deserializers.put(typeId, deserializer);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers() {
                return deserializers;
            }
        };

        JsonDeserializer<Object> resultDeserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);

        assertEquals(deserializer, resultDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        JsonDeserializer<Object> resultDeserializer = typeDeserializerBase._findDefaultImplDeserializer(null);

        assertNull(resultDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithContext() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(ctxt.findContextualValueDeserializer(eq(baseType), any(BeanProperty.class))).thenReturn(deserializer);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _defaultImplDeserializer() {
                return deserializer;
            }
        };

        JsonDeserializer<Object> resultDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        assertEquals(deserializer, resultDeserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithNullContext() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "typeId";

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(null, typeId, idResolver, baseType);
    }

    @Test
    public void testHandleUnknownTypeIdWithContext() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";

        when(ctxt.unknownTypeId()).thenReturn(typeId);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(ctxt, null, idResolver, baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithInvalidTypeIdResolver() throws IOException {
        JavaType baseType = mock(JavaType.class);
        Object invalidIdResolver = new Object();
        String typeId = "typeId";

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, invalidIdResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(null, typeId, invalidIdResolver, baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithInvalidBaseType() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "typeId";

        when(baseType.getClass()).thenReturn(String[].class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(null, typeId, idResolver, baseType);
    }

    @Test
    public void testHandleUnknownTypeIdWithValidTypeId() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";

        when(ctxt.unknownTypeId()).thenReturn(typeId);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);

        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
    }
}