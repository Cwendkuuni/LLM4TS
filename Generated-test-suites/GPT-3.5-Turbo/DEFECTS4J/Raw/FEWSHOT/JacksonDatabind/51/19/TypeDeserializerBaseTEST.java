package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;
    private DeserializationContext ctxt;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        property = mock(BeanProperty.class);
        ctxt = mock(DeserializationContext.class);
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase deserializerForProperty = typeDeserializerBase.forProperty(property);
        assertEquals(property, deserializerForProperty.getProperty());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase deserializerWithTypeInclusion = typeDeserializerBase.withTypeInclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, deserializerWithTypeInclusion.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        when(baseType.getRawClass()).thenReturn(String.class);
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializerBase.getTypePropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        when(defaultImpl.getRawClass()).thenReturn(String.class);
        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        when(baseType.getRawClass()).thenReturn(String.class);
        String toStringResult = typeDeserializerBase.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver: " + idResolver + "]", toStringResult);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenDeserIsNotNull() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "1";
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(baseType);
        when(ctxt.findContextualValueDeserializer(baseType, property)).thenReturn(deser);
        when(deser.deserialize(jp, ctxt)).thenReturn("Object");
        assertEquals("Object", typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenTypeIdIsNullAndDeserIsNotNull() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(ctxt, null)).thenReturn(null);
        when(ctxt.isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(typeDeserializerBase._findDefaultImplDeserializer(ctxt)).thenReturn(deser);
        when(deser.deserialize(jp, ctxt)).thenReturn("null");
        assertEquals("null", typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdWhenTypeIdIsNullAndDeserIsNull() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(idResolver.typeFromId(ctxt, null)).thenReturn(null);
        when(ctxt.isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenTypeIdIsNotNullAndDeserIsNull() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "1";
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);
        when(typeDeserializerBase._findDefaultImplDeserializer(ctxt)).thenReturn(null);
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId));
    }

    private static class TypeDeserializerBaseMock extends TypeDeserializerBase {

        public TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes,
                                         String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseMock(_baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}