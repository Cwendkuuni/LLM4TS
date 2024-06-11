package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() {
        // Test default constructor
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null);
        assertNotNull(deserializer);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", deserializer.toString());

        // Test constructor with arguments
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleModule().addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer2 = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, deserializer2._baseType);
        assertEquals(idResolver, deserializer2._idResolver);
        assertEquals(typePropertyName, deserializer2._typePropertyName);
        assertEquals(typeIdVisible, deserializer2._typeIdVisible);
        assertEquals(defaultImpl, deserializer2._defaultImpl);
        assertEquals(null, deserializer2._property);
        assertEquals(16, deserializer2._deserializers.size());
    }

    @Test
    public void testForProperty() {
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleModule().addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer forProperty = deserializer.forProperty(property);
        assertNotNull(forProperty);
        assertEquals(deserializer.getClass(), forProperty.getClass());
        assertEquals(deserializer._property, forProperty._property);
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleModule().addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As typeInclusion = deserializer.getTypeInclusion();
        assertNotNull(typeInclusion);
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, null, null, false, null);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", deserializer.baseTypeName());
        assertEquals(String.class.getName(), baseType.getRawClass().getName());
    }

    @Test
    public void testGetPropertyName() {
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleModule().addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(typePropertyName, deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleModule().addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(idResolver, deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType baseType = new SimpleModule().addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new SimpleModule().addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(defaultImpl, deserializer.getDefaultImpl());
        assertNull(deserializer.getDefaultImplDeserializer());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        SimpleModule module = new SimpleModule();
        JavaType baseType = module.addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = module.addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer2 = mock(JsonDeserializer.class);

        when(idResolver.typeFromId(context, typeId)).thenReturn(null);

        JsonDeserializer<Object> result = deserializer._findDeserializer(context, typeId);
        assertNull(result);

        when(idResolver.typeFromId(context, typeId)).thenReturn(module.constructType(String[].class));
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(deserializer2);

        result = deserializer._findDeserializer(context, typeId);
        assertEquals(deserializer2, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        SimpleModule module = new SimpleModule();
        JavaType baseType = module.addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = module.addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mock(DeserializationContext.class);

        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(context.findContextualValueDeserializer(defaultImpl, any(BeanProperty.class))).thenReturn(new IntDeserializer());

        JsonDeserializer<Object> result = deserializer._findDefaultImplDeserializer(context);
        assertEquals(new IntDeserializer(), result);

        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        result = deserializer._findDefaultImplDeserializer(context);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        SimpleModule module = new SimpleModule();
        JavaType baseType = module.addTypeHandler(String.class, new StringDeserializer()).constructType(String[].class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = module.addTypeHandler(Integer.class, new IntDeserializer()).constructType(Integer[].class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "typeId";

        when(idResolver.getTypeFromId(context, typeId)).thenReturn(null);

        JavaType result = deserializer._handleUnknownTypeId(context, typeId, idResolver, baseType);
        assertNull(result);

        when(idResolver.getTypeFromId(context, typeId)).thenReturn(module.constructType(String[].class));
        when(context.handleUnknownTypeId(baseType, typeId, idResolver, anyString())).thenReturn(module.constructType(String[].class));

        result = deserializer._handleUnknownTypeId(context, typeId, idResolver, baseType);
        assertEquals(module.constructType(String[].class), result);
    }
}