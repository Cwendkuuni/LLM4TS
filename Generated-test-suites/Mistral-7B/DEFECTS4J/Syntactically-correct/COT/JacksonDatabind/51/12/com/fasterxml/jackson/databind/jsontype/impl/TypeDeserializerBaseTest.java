package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.test.BaseMapTest;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() throws Exception {
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializerBase);
    }

    @Test
    public void testTypeDeserializerBaseForProperty() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        BeanProperty property = Mockito.mock(BeanProperty.class);

        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNotNull(typeInclusion);
    }

    @Test
    public void testToString() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        String toString = typeDeserializerBase.toString();
        assertNotNull(toString);
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_ValidTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String typeId = "typeId";
        JsonDeserializer<Object> expectedDeserializer = Mockito.mock(JsonDeserializer.class);

        Mockito.when(ctxt.findContextualValueDeserializer(ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any(BeanProperty.class)))
                .thenReturn(expectedDeserializer);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
        Mockito.verify(ctxt).findContextualValueDeserializer(ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any(BeanProperty.class));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Class<?> defaultImplClass = Object.class;
        JsonDeserializer<Object> expectedDeserializer = Mockito.mock(JsonDeserializer.class);

        Mockito.when(ctxt.findContextualValueDeserializer(ArgumentMatchers.eq(defaultImplClass), ArgumentMatchers.any(BeanProperty.class)))
                .thenReturn(expectedDeserializer);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        Mockito.verify(ctxt).findContextualValueDeserializer(ArgumentMatchers.eq(defaultImplClass), ArgumentMatchers.any(BeanProperty.class));
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String typeId = "typeId";
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType baseType = Mockito.mock(JavaType.class);

        Mockito.when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);

        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId_WhenJsonDeserializerIsAvailable() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String typeId = "typeId";
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        JavaType expectedJavaType = Mockito.mock(JavaType.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);

        Mockito.when(idResolver.typeFromId(ctxt, typeId)).thenReturn(expectedJavaType);
        Mockito.when(ctxt.findContextualValueDeserializer(expectedJavaType, ArgumentMatchers.any(BeanProperty.class)))
                .thenReturn(deserializer);

        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertNotNull(result);
        Mockito.verify(idResolver).typeFromId(ctxt, typeId);
        Mockito.verify(ctxt).findContextualValueDeserializer(expectedJavaType, ArgumentMatchers.any(BeanProperty.class));
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeId_ValidTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestHelper().createTypeDeserializerBase();
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String typeId = "typeId";
        Object expectedResult = new Object();
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);

        Mockito.when(ctxt.findContextualValueDeserializer(ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any(BeanProperty.class)))
                .thenReturn(deserializer);

        Mockito.when(deserializer.deserialize(jp, ctxt)).thenReturn(expectedResult);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNotNull(result);
        Mockito.verify(ctxt).findContextualValueDeserializer(ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any(BeanProperty.class));
        Mockito.verify(deserializer).deserialize(jp, ctxt);
    }

    private static class TypeDeserializerBaseTestHelper {

        public TypeDeserializerBase createTypeDeserializerBase() throws Exception {
            TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
            JavaType baseType = Mockito.mock(JavaType.class);

            String typePropertyName = "typePropertyName";
            boolean typeIdVisible = true;
            JavaType defaultImpl = Mockito.mock(JavaType.class);

            return new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class MockitoJUnitRunner extends org.junit.runner.RunnerWrapper {

        @Override
        public Description getDescription(Class<?> clazz) {
            return Description.createTestDescription("MockitoJUnitRunner", clazz);
        }

    }
}