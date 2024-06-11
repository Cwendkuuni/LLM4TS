package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<Object> jsonDeserializer;
    private JavaType javaType;

    @Before
    public void setUp() {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        javaType = Mockito.mock(JavaType.class);
        String typePropertyName = "typeName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        deserializationContext = Mockito.mock(DeserializationContext.class);
        jsonDeserializer = Mockito.mock(JsonDeserializer.class);
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("javaMockito.mock.MockedClass", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("typeName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType result = typeDeserializerBase.baseType();
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), deserializationContext);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "unknownType";
        String extraDesc = "Extra Description";
        JavaType result = typeDeserializerBase._handleUnknownTypeId(deserializationContext, typeId);
        assertEquals(javaType, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_TypeIdNotNull() throws IOException {
        Object typeId = "typeId";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), deserializationContext, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        String extraDesc = "Extra Description";
        JavaType result = typeDeserializerBase._handleMissingTypeId(deserializationContext, extraDesc);
        assertEquals(javaType, result);
    }

    @Test
    public void testFindDeserializer_DeserializerNull() throws IOException {
        when(jsonDeserializer.deserialize(any(), any())).thenReturn("deserializer");
        typeDeserializerBase._deserializers.put("typeId", jsonDeserializer);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(deserializationContext, "typeId");
        assertEquals(jsonDeserializer, result);
    }

    @Test
    public void testFindDeserializer_TypeNull() throws IOException {
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(deserializationContext, null);
        assertNull(result);
    }
}