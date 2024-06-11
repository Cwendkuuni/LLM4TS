package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    // Test helper classes

    static class TestTypeDeserializerBase extends TypeDeserializerBase {
        TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.INCLUDE;
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                throws IOException {
            return null;
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId)
                throws IOException {
            throw new RuntimeException("Should not be reached.");
        }

        @Override
        protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                throws IOException {
            throw new RuntimeException("Should not be reached.");
        }
    }

    static class TestTypeIdResolver implements TypeIdResolver {

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
    }

    // Test methods

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer result = deserializer.forProperty(prop);

        assertNull(result);
        verify(idRes, never()).typeFromId(any(DeserializationContext.class), anyString());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        JsonTypeInfo.As result = deserializer.getTypeInclusion();

        assertEquals(JsonTypeInfo.As.INCLUDE, result);
        verifyNoMoreInteractions(idRes);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, null, null, false, null);

        String result = deserializer.baseTypeName();

        assertEquals(Object.class.getName(), result);
    }

    @Test
    public void testPropertyName() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        String result = deserializer.getPropertyName();

        assertEquals(typePropertyName, result);
        verifyNoMoreInteractions(idRes);
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        assertEquals(idRes, deserializer.getTypeIdResolver());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultImpl() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        deserializer.getDefaultImpl();
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = new TestTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        JsonDeserializer<Object> result = deserializer._findDeserializer(ctxt, typeId);

        assertNull(result);
        verify(idRes).typeFromId(ctxt, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> result = deserializer._findDefaultImplDeserializer(ctxt);

        assertNull(result);
        verify(ctxt).findContextualValueDeserializer(deserializer.getDefaultImpl(), deserializer._property);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = new TestTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "typeId";
        when(jp.getTypeId()).thenReturn(typeId);

        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);

        assertNull(result);
        verify(ctxt).handleMissingTypeId(deserializer.baseType(), idRes.getDescForKnownTypeIds());
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = new TestTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";

        try {
            deserializer._handleUnknownTypeId(ctxt, typeId);
            fail("Should throw exception.");
        } catch (IOException e) {
            // Expected
        }

        verify(ctxt).handleMissingTypeId(deserializer.baseType(), idRes.getDescForKnownTypeIds());
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = new TestTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        String extraDesc = "extraDesc";

        try {
            deserializer._handleMissingTypeId(ctxt, extraDesc);
            fail("Should throw exception.");
        } catch (IOException e) {
            // Expected
        }

        verify(ctxt).handleMissingTypeId(deserializer.baseType(), extraDesc);
    }

    @Test
    public void testDeserializers() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idRes = new TestTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(baseType, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Test empty Map
        Map<String, JsonDeserializer<Object>> deserializers = deserializer._deserializers;
        assertEquals(0, deserializers.size());

        // Test set value
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer1 = mock(JsonDeserializer.class);
        deserializers.put(typeId, deserializer1);
        JsonDeserializer<Object> result = deserializer._findDeserializer(ctxt, typeId);

        assertSame(deserializer1, result);
        verify(idRes).typeFromId(ctxt, typeId);
        verify(ctxt).findContextualValueDeserializer(deserializer1, deserializer._property);

        // Test concurrent modification exception
        String typeId2 = "typeId2";
        JsonDeserializer<Object> deserializer2 = mock(JsonDeserializer.class);

        try {
            deserializers.put(typeId2, deserializer2);
            deserializers.put(typeId, deserializer1);
            fail("Should throw exception.");
        } catch (ConcurrentModificationException e) {
            // Expected
        }

        // Test get value
        JsonDeserializer<Object> result2 = deserializer._findDeserializer(ctxt, typeId2);

        assertNull(result2);
        verify(idRes, never()).typeFromId(ctxt, typeId2);
    }
}