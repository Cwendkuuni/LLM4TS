package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest extends BaseTest {

    

    @org.junit.Test
    public void testConstructor() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        assertNotNull(typeDeserializerBase);
    }

    @org.junit.Test
    public void testForProperty() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        TypeDeserializer result = typeDeserializerBase.forProperty(property);
        assertNotNull(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        typeDeserializerBase.forProperty(null);
    }

    @org.junit.Test
    public void testGetTypeInclusion() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, result);
    }

    @org.junit.Test
    public void testBaseTypeName() {
        JavaType baseType = mapType(String.class, int.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        String result = typeDeserializerBase.baseTypeName();
        assertEquals(String.class.getName(), result);
    }

    @org.junit.Test
    public void testGetPropertyName() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        String result = typeDeserializerBase.getPropertyName();
        assertNull(result);
    }

    @org.junit.Test
    public void testToString() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        String result = typeDeserializerBase.toString();
        assertTrue(result.startsWith("[TypeDeserializerBase"));
    }

    @org.junit.Test
    public void testFindDeserializerNullContext() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        String typeId = "invalidTypeId";
        assertNull(typeDeserializerBase._findDeserializer(null, typeId));
    }

    @org.junit.Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        assertNull(typeDeserializerBase._findDeserializer(mockContext(), null));
    }

    @org.junit.Test
    public void testFindDeserializerEmptyMap() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockContext(), "defaultTypeId");
        assertNull(result);
    }

    @org.junit.Test
    public void testFindDeserializerWithDeserializer() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._deserializers.put("defaultTypeId", jsonDeserializer);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockContext(), "defaultTypeId");
        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerNullContext() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerNullDefaultImpl() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(mockContext()));
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerWithDeserializer() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._defaultImplDeserializer = jsonDeserializer;
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNullDefaultImplType() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertNull(result);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        JavaType defaultImpl = mapType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, defaultImpl);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._defaultImplDeserializer = jsonDeserializer;
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdNullContext() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        String typeId = "invalidTypeId";
        assertNull(typeDeserializerBase._handleUnknownTypeId(mockContext(), typeId, idRes, baseType));
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdNullIdResolver() {
        JavaType baseType = mapType(String.class, int.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        String typeId = "invalidTypeId";
        assertNull(typeDeserializerBase._handleUnknownTypeId(mockContext(), typeId, null, baseType));
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(mockContext(), null, idRes, baseType);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullBaseType() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idRes, null, false, null);
        String typeId = "invalidTypeId";
        typeDeserializerBase._handleUnknownTypeId(mockContext(), typeId, idRes, null);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithException() {
        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idRes = mapTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        String typeId = "invalidTypeId";
        typeDeserializerBase._handleUnknownTypeId(mockContext(), typeId, idRes, baseType);

        assertFalse(typeDeserializerBase._deserializers.isEmpty());
        assertTrue(typeDeserializerBase._deserializers.containsKey(typeId));
        assertNotNull(typeDeserializerBase._deserializers.get(typeId));
    }

    

    private ContextualSerializationContext mockContext() {
        return mock(ContextualSerializationContext.class);
    }

    private TypeIdResolver mapTypeIdResolver() {
        return mock(TypeIdResolver.class);
    }

    private JavaType mapType(Class<?>... types) {
        return mapper().getTypeFactory().constructType(types);
    }
}