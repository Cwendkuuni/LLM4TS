package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test class for TypeDeserializerBase.
 */
@RunWith(BaseTest.NiceMockitoRunner.class)
public class TypeDeserializerBaseTest {

    
    
    @Test
    public void testConstructor() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Mockito.mock(JavaType.class), Mockito.mock(TypeIdResolver.class),
                "typePropertyName", true, null);
        assertNotNull(typeDeserializerBase);
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("Not the expected base type name", baseType.getRawClass().getName(), baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        String propertyName = typeDeserializerBase.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws Exception {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        String expectedToString = "[" + TypeDeserializerBase.class.getName() + "; base-type:" + baseType + "; id-resolver: "
                + idResolver + "]";
        assertEquals(expectedToString, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typeId = "typeId";
        JsonDeserializer<Object> expectedDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        when(idResolver.typeFromId(context, typeId)).thenReturn(Mockito.mock(JavaType.class));
        when(context.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.any(BeanProperty.class)))
                .thenReturn(expectedDeserializer);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(expectedDeserializer, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> expectedDeserializer = Mockito.mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(baseType, Mockito.any(BeanProperty.class)))
                .thenReturn(expectedDeserializer);
        synchronized (typeDeserializerBase.getDefaultImpl()) {
            JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
            assertEquals(expectedDeserializer, deserializer);
        }
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithNativeTypeIdNull() throws Exception {
        JsonParser parser = new JsonFactory().getParser();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase._deserializeWithNativeTypeId(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
        JsonParser parser = new JsonFactory().getParser();
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        when(typeDeserializerBase._findDeserializer(Mockito.mock(DeserializationContext.class), Mockito.anyString()))
                .thenReturn(deserializer);
        typeDeserializerBase._deserializeWithNativeTypeId(parser, Mockito.mock(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typeId = "typeId";
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(Mockito.mock(DeserializationContext.class), typeId, idResolver, null);
    }

    
}