package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

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

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;

    @Before
    public void setup() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        property = mock(BeanProperty.class);
        
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            
            private static final long serialVersionUID = 1L;

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
    }

    @Test
    public void testConstructor() {
        assertEquals(baseType, typeDeserializerBase.baseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(ClassUtil.rawClass(defaultImpl), typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "id";
        String typeIdStr = typeId;

        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(ctxt, typeIdStr);
        assertNull(deser);

        typeId = null;
        typeIdStr = String.valueOf(typeId);
        deser = typeDeserializerBase._findDeserializer(ctxt, typeIdStr);
        assertNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(NullifyingDeserializer.instance, deser);

        typeDeserializerBase._defaultImplDeserializer = mock(JsonDeserializer.class);
        deser = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(typeDeserializerBase._defaultImplDeserializer, deser);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = mock(JsonParser.class);
        Object typeId = "id";

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);

        typeId = null;
        result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "id";
        String extraDesc = "additional description";
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNull(result);

        typeDeserializerBase._property = property;
        result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String extraDesc = "additional description";
        JavaType result = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);
        assertNull(result);
    }
}