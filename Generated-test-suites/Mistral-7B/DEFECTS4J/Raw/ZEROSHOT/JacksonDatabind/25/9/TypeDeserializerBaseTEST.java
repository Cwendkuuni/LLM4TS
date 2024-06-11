package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private static final String CLASS_NAME = TypeDeserializerBase.class.getName();
    private static final String BASE_TYPE_NAME = "java.lang.String";
    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final String TYPE_ID_VISIBLE = "true";
    private static final String DEFAULT_IMPL = "java.util.ArrayList";

    @Test
    public void testTypeDeserializerBase() throws IOException {
        JavaType baseType = mapType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = TYPE_PROPERTY_NAME;
        boolean typeIdVisible = Boolean.parseBoolean(TYPE_ID_VISIBLE);
        Class<?> defaultImpl = Class.forName(DEFAULT_IMPL);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(baseType, typeDeserializerBase.getBaseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBase.isTypeIdVisible());
        assertNull(typeDeserializerBase.getProperty());
        assertNull(typeDeserializerBase.getDefaultImpl());
        assertNotNull(typeDeserializerBase.toString());

        TypeDeserializerBase newTypeDeserializerBase = new TypeDeserializerBase(typeDeserializerBase, new BeanProperty(null, "propertyName"));

        assertEquals(baseType, newTypeDeserializerBase.getBaseType());
        assertEquals(idResolver, newTypeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, newTypeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, newTypeDeserializerBase.isTypeIdVisible());
        assertEquals("propertyName", newTypeDeserializerBase.getProperty().getName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = mapType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = TYPE_PROPERTY_NAME;
        boolean typeIdVisible = Boolean.parseBoolean(TYPE_ID_VISIBLE);
        Class<?> defaultImpl = Class.forName(DEFAULT_IMPL);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = createContext();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(idResolver.typeFromId(context, "typeId1")).thenReturn(mapType(Integer.class));
        when(idResolver.typeFromId(context, "typeId2")).thenReturn(mapType(Float.class));
        when(idResolver.typeFromId(context, null)).thenReturn(null);
        when(typeDeserializerBase.findDeserializer(context, "typeId1")).thenReturn(deserializer);

        JsonParser parser = createParser(mapper(), "[1]");
        parser.nextToken();

        Object result = typeDeserializerBase.deserialize(parser, context);

        verify(typeDeserializerBase).findDeserializer(context, "typeId1");
        verify(deserializer).deserialize(parser, context);
        assertEquals(Integer.class, result.getClass());

        parser = createParser(mapper(), "[2.5]");
        parser.nextToken();

        result = typeDeserializerBase.deserialize(parser, context);

        verify(typeDeserializerBase).findDeserializer(context, "typeId2");
        verifyNoMoreInteractions(deserializer);
        assertEquals(Float.class, result.getClass());

        parser = createParser(mapper(), "[null]");
        parser.nextToken();

        result = typeDeserializerBase.deserialize(parser, context);

        verify(typeDeserializerBase).findDeserializer(context, null);
        verifyNoMoreInteractions(deserializer);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        JavaType baseType = mapType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = TYPE_PROPERTY_NAME;
        boolean typeIdVisible = Boolean.parseBoolean(TYPE_ID_VISIBLE);
        Class<?> defaultImpl = Class.forName(DEFAULT_IMPL);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = createContext();
        String unknownTypeId = "unknownTypeId";

        when(idResolver.typeFromId(context, unknownTypeId)).thenReturn(null);

        JsonParser parser = createParser(mapper(), "[1]");
        parser.nextToken();

        typeDeserializerBase.deserialize(parser, context);

        fail("Expected JsonMappingException");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = mapType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = TYPE_PROPERTY_NAME;
        boolean typeIdVisible = Boolean.parseBoolean(TYPE_ID_VISIBLE);
        Class<?> defaultImpl = Class.forName(DEFAULT_IMPL);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = createContext();

        when(idResolver.typeFromId(context, null)).thenReturn(null);

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);

        when(typeDeserializerBase.findDefaultImplDeserializer(context)).thenReturn(defaultImplDeserializer);

        parser().expectNextValueType(String.class);

        typeDeserializerBase.deserialize(parser(), context);

        verify(defaultImplDeserializer).deserialize(parser(), context);
        verifyNoMoreInteractions(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = mapType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = TYPE_PROPERTY_NAME;
        boolean typeIdVisible = Boolean.parseBoolean(TYPE_ID_VISIBLE);
        Class<?> defaultImpl = Class.forName(DEFAULT_IMPL);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = createContext();
        Object typeId = "typeId";

        JsonParser parser = createParser(mapper(), "[1," + typeId + "]");
        parser.nextToken();

        Object result = typeDeserializerBase.deserializeWithNativeTypeId(parser, context);

        verify(typeDeserializerBase).findDeserializer(context, typeId.toString());
        verifyNoMoreInteractions(parser);
        assertEquals(Integer.class, result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        JavaType baseType = mapType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = TYPE_PROPERTY_NAME;
        boolean typeIdVisible = Boolean.parseBoolean(TYPE_ID_VISIBLE);
        Class<?> defaultImpl = Class.forName(DEFAULT_IMPL);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = createContext();
        Object typeId = "unknownTypeId";

        JsonParser parser = createParser(mapper(), "[1," + typeId + "]");
        parser.nextToken();

        typeDeserializerBase.deserializeWithNativeTypeId(parser, context);

        fail("Expected JsonMappingException");
    }
}