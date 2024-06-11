package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializer type = typeDeserializerBase.forProperty(prop);
        assertNotNull(type);
        assertEquals(typeDeserializerBase.getClass(), type.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = typeDeserializerBase.getTypeIdResolver();
        assertNull(resolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() {
        String str = typeDeserializerBase.toString();
        assertNotNull(str);
    }

    @Test
    public void testDeserializeWithNativeTypeId() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = null;
        try {
            result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        } catch(IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithId() {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Object result = null;
        try {
            result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        } catch(IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() {
        DeserializationContext ctxt = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        try {
            JavaType unknownTypeId = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
            fail("Exception not thrown.");
        } catch(IOException e) {
            assertNotNull(e.getMessage());
        }
    }
}

class TypeDeserializerBaseImpl extends TypeDeserializerBase {
    
    protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes,
            String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    protected TypeDeserializerBaseImpl(TypeDeserializerBase src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return new TypeDeserializerBaseImpl(this, prop);
    }

    @Override    
    public JsonTypeInfo.As getTypeInclusion() {
        return null;
    }
}